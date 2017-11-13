package ru.romanbrazhnikov.configuration;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.romanbrazhnikov.configuration.cookies.Cookie;
import ru.romanbrazhnikov.configuration.cookies.CookieRules;
import ru.romanbrazhnikov.configuration.cookies.Cookies;
import ru.romanbrazhnikov.configuration.markers.Marker;
import ru.romanbrazhnikov.configuration.markers.Markers;
import ru.romanbrazhnikov.configuration.requestarguments.RequestArgument;
import ru.romanbrazhnikov.configuration.requestarguments.RequestArgumentValues;
import ru.romanbrazhnikov.configuration.requestarguments.RequestArguments;
import ru.romanbrazhnikov.sourceprovider.HttpMethods;
import ru.romanbrazhnikov.utils.FileUtils;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ConfigBuilder {

    //
    // xPath constants
    //
    private static final String XPATH_NAME = "/Config/@name";
    private static final String XPATH_BASE_URL = "/Config/BaseUrl/@value";
    private static final String XPATH_REQUEST_PARAMS = "/Config/RequestParams/@value";
    private static final String XPATH_REQUEST_ARGUMENTS = "/Config/RequestArguments/Argument";
    private static final String XPATH_METHOD = "/Config/Method/@value";
    private static final String XPATH_ENCODING = "/Config/Encoding/@value";
    private static final String XPATH_FIRST_PAGE = "/Config/FirstPage/@value";
    private static final String XPATH_STEP = "/Config/mStep/@value";
    private static final String XPATH_DELAY_MS = "/Config/Delay/@ms";
    private static final String XPATH_MAX_PAGE_PATTERN = "/Config/MaxPagePattern";
    private static final String XPATH_MARKERS = "/Config/Markers/Marker";
    private static final String XPATH_DESTINATION = "/Config/Destination/@value";
    private static final String XPATH_COOKIES = "/Config/Cookies";
    private static final String XPATH_getCookies = "/Config/Cookies/Cookie";
    private static final String XPATH_FIRST_LEVEL_PATTERN = "/Config/FirstLevelPattern";
    private static final String XPATH_SECOND_LEVEL_PATTERN = "/Config/SecondLevelPattern";

    //
    // System fields
    //
    private Document mDoc;
    private XPath mXPath;
    private String mErrorMessage;

    // CONFIGURATION
    private Configuration mConfiguration;

    public ConfigBuilder() {
        mXPath = XPathFactory.newInstance().newXPath();
    }

    public boolean readFromXmlFile(String xmlPath) {
        String fileData;

        try {
            fileData = FileUtils.readFromFileToString(xmlPath);
        } catch (IOException e) {
            mErrorMessage = e.getMessage();
            return false;
        }

        return readFromXmlString(fileData);
    }

    public boolean readFromXmlString(String xmlString) {
        DocumentBuilderFactory myDocFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder myDocBuilder;
        try {
            myDocBuilder = myDocFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            mErrorMessage = e.getMessage();
            return false;
        }


        try {
            mDoc = myDocBuilder.parse(new InputSource(new StringReader(xmlString)));
        } catch (SAXException e) {
            mErrorMessage = "SAXException: " + e.getMessage();
            return false;
        } catch (IOException e) {
            mErrorMessage = "IOException: " + e.getMessage();
            return false;
        }


        // read successfully
        return true;
    }

    public Configuration init() {
        initPrimitives();
        initRequestArguments();
        initMarkers();
        initCookies();

        return mConfiguration;
    }

    private void initPrimitives() {
        String configName = getParserAttributeAsString(XPATH_NAME);
        String baseUrl = getParserAttributeAsString(XPATH_BASE_URL);
        String formatUrl = getParserAttributeAsString(XPATH_REQUEST_PARAMS);
        String method = getParserAttributeAsString(XPATH_METHOD);
        String encoding = getParserAttributeAsString(XPATH_ENCODING);
        String firstPageAsString = getParserAttributeAsString(XPATH_FIRST_PAGE);
        String stepAsString = getParserAttributeAsString(XPATH_STEP);
        String delay = getByXPath(XPATH_DELAY_MS);
        String maxPagePattern = getByXPath(XPATH_MAX_PAGE_PATTERN).trim();
        String destination = getParserAttributeAsString(XPATH_DESTINATION);
        String firstLevelPattern = getByXPath(XPATH_FIRST_LEVEL_PATTERN).trim();
        String secondLevelPattern = getByXPath(XPATH_SECOND_LEVEL_PATTERN).trim();


        HttpMethods httpMethod = HttpMethods.GET;
        switch (method.toLowerCase()) {
            case "get":
                httpMethod = HttpMethods.GET;
                break;
            case "post":
                httpMethod = HttpMethods.POST;
                break;
        }


        int firstPage = Integer.parseInt(firstPageAsString);
        int step = 1;

        if (stepAsString != null && !stepAsString.equals("")) {
            step = Integer.parseInt(stepAsString);
        }

        int delayInMillis = -1;
        if (delay != null && !delay.equals("")) {
            delayInMillis = Integer.parseInt(delay);
        }

        mConfiguration = new Configuration(
                configName,
                httpMethod,
                encoding,
                delayInMillis,
                baseUrl,
                formatUrl,
                firstPage,
                maxPagePattern,
                step,
                destination,
                firstLevelPattern,
                secondLevelPattern
        );
    }

    private void initRequestArguments() {
        RequestArguments requestArguments = new RequestArguments();
        NodeList formatParamNodeList = (NodeList) getByXPath(XPATH_REQUEST_ARGUMENTS, XPathConstants.NODESET);
        if (formatParamNodeList != null) {
            // for each RequestArgument
            for (int i = 0; i < formatParamNodeList.getLength(); i++) {
                RequestArgument currentFixedParam = new RequestArgument();

                // getting format param attribute
                currentFixedParam.mParamName = getByXPath("@name", formatParamNodeList.item(i));
                currentFixedParam.mField = getByXPath("@field", formatParamNodeList.item(i));

                // getting format param value bindings...
                // ...getting child nodes,
                NodeList CurrentValuePairNodeList =
                        (NodeList) getByXPath("Values", formatParamNodeList.item(i), XPathConstants.NODESET);
                currentFixedParam.mParamValueList = new ArrayList<>();
                // ...for each value pair.
                for (int j = 0; j < CurrentValuePairNodeList.getLength(); j++) {
                    RequestArgumentValues currentValuePair = new RequestArgumentValues();

                    // getting attrs of the current RequestArgumentValues
                    currentValuePair.mArgumentValue = getByXPath("@argumentValue", CurrentValuePairNodeList.item(j));
                    currentValuePair.mFieldValue = getByXPath("@fieldValue", CurrentValuePairNodeList.item(j));

                    // adding current pair to the list
                    currentFixedParam.mParamValueList.add(currentValuePair);
                }
                // adding current fixed param to the configuration
                requestArguments.mParamList.add(currentFixedParam);
            }
            mConfiguration.setRequestArguments(requestArguments);
        }

    }

    private void initMarkers() {
        NodeList markerNodeList = (NodeList) getByXPath(XPATH_MARKERS, XPathConstants.NODESET);
        if (markerNodeList != null) {
            Markers markers = new Markers();

            for (int nodeNum = 0; nodeNum < markerNodeList.getLength(); nodeNum++) {
                // getting necessary attributes
                String field = getByXPath("@field", markerNodeList.item(nodeNum));
                String value = getByXPath("@value", markerNodeList.item(nodeNum));

                // creating a Marker object
                Marker currentMarker = new Marker();
                currentMarker.mField = field;
                currentMarker.mValue = value;

                // appending the currentMarker to the markerNodeList
                markers.mMarkers.add(currentMarker);

            }

            mConfiguration.setMarkers(markers);
        }
    }

    private void initCookies() {
        Node CookiesRequestNode = (Node) getByXPath(XPATH_COOKIES, XPathConstants.NODE);
        if (CookiesRequestNode != null) {
            CookieRules cookieRules = new CookieRules();
            // setting cookie request
            String CookieRequestAddress = getByXPath("@address", CookiesRequestNode);
            String CookieRequestParams = getByXPath("@params", CookiesRequestNode);
            String CookieRequestMethod = getByXPath("@method", CookiesRequestNode);

            if (CookieRequestAddress != null && CookieRequestParams != null && CookieRequestMethod != null) {
                cookieRules.mRequestCookiesAddress = CookieRequestAddress;
                cookieRules.mRequestCookiesParamString = CookieRequestParams;
                cookieRules.mRequestCookiesMethod = CookieRequestMethod;

                Cookies cookies = new Cookies();
                cookies.mCookieRules = cookieRules;
                mConfiguration.setCookies(cookies);
            } else {

                // trying to set custom cookies
                NodeList CookieNodeList = (NodeList) getByXPath(XPATH_getCookies, XPathConstants.NODESET);
                if (CookieNodeList != null) {
                    Cookies cookies = new Cookies();
                    cookies.mCookieList = new ArrayList<>();

                    for (int i = 0; i < CookieNodeList.getLength(); i++) {

                        String Name = getByXPath("@name", CookieNodeList.item(i));
                        String Value = getByXPath("@value", CookieNodeList.item(i));
                        String Domain = getByXPath("@domain", CookieNodeList.item(i));
                        Cookie currentCookie = new Cookie(Name, Value, Domain);

                        cookies.mCookieList.add(currentCookie);

                    }
                    mConfiguration.setCookies(cookies);
                }

            }
        }
    }


    //
    // XPATH METHODS
    //

    private String getParserAttributeAsString(String p_xPath) {
        String resultString = getByXPath(p_xPath);
        if (resultString == null || resultString.equals("")) {
            return null;
        } else {
            return resultString;
        }

    }

    /**
     * Gets a string value by XPath from root
     */
    private String getByXPath(String p_xPathString) {
        return (String) getByXPath(p_xPathString, null, null);
    }

    /**
     * Gets a string value by XPath from an item
     */
    private String getByXPath(String p_xPathString, Object p_item) {
        return (String) getByXPath(p_xPathString, p_item, null);
    }

    /**
     * Gets a specified type value by XPath from root
     */
    private Object getByXPath(String p_xPathString, QName p_xPathReturnType) {
        return getByXPath(p_xPathString, null, p_xPathReturnType);
    }

    /**
     * Gets a specified type value by XPath from mentioned item
     */
    private Object getByXPath(String p_xPathString, Object p_item, QName p_xPathReturnType) {

        Object item;
        // checking if the item is set
        if (p_item == null) {
            // ... not set - use root document
            item = mDoc;
        } else {
            // ... else use the item
            item = p_item;
        }

        QName xPathReturnType;
        if (p_xPathReturnType == null) {
            xPathReturnType = XPathConstants.STRING;
        } else {
            xPathReturnType = p_xPathReturnType;
        }

        try {
            // applying xPath expression to the item
            return mXPath.compile(p_xPathString).evaluate(item, xPathReturnType);
        } catch (XPathExpressionException e) {
            mErrorMessage = "[XPathExpressionException]: " + e.getMessage();
            return null;
        }
    }

}
