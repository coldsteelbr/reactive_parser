package ru.romanbrazhnikov.configuration;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.romanbrazhnikov.configuration.cookies.Cookies;
import ru.romanbrazhnikov.configuration.datafieldbindings.DataFieldBindings;
import ru.romanbrazhnikov.configuration.formatparams.FormatParams;
import ru.romanbrazhnikov.configuration.markers.Markers;
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

public class Configuration {


    //
    // Config fields
    //
    private String mName = "Default Config name";
    private HttpMethods mMethod = HttpMethods.GET;
    private String mEncodingName = "UTF-8";
    private int mDelayInMillis = 3334;
    private String mBaseUrl;
    private String mFormatUrl;
    private FormatParams mFormatParams;
    private int mFirstPage = 1;
    private String mMaxPagePattern;
    private int mStep = 1;
    private Cookies mCookies;
    private Markers mMarkers;
    private String mDestination;
    private String mFirstLevelPattern;
    private String mSecondLevelPattern;
    private DataFieldBindings mFirstLevelFieldBindings;
    private DataFieldBindings mSecondLevelFieldBindings;


    public Configuration(String name, HttpMethods method, String encodingName, int delayInMillis, String baseUrl, String formatUrl, int firstPage, String maxPagePattern, int step, String destination, String firstLevelPattern, String secondLevelPattern) {
        mName = name;
        mMethod = method;
        mEncodingName = encodingName;
        mDelayInMillis = delayInMillis < 0 ? 3334 : delayInMillis;
        mBaseUrl = baseUrl;
        mFormatUrl = formatUrl;
        mFirstPage = firstPage;
        mMaxPagePattern = maxPagePattern;
        mStep = step < 1 ? 1 : step;
        mDestination = destination;
        mFirstLevelPattern = firstLevelPattern;
        mSecondLevelPattern = secondLevelPattern;
    }

    public void setFormatParams(FormatParams formatParams) {
        mFormatParams = formatParams;
    }

    public void setCookies(Cookies cookies) {
        mCookies = cookies;
    }

    public void setMarkers(Markers markers) {
        mMarkers = markers;
    }

    public void setFirstLevelFieldBindings(DataFieldBindings firstLevelFieldBindings) {
        mFirstLevelFieldBindings = firstLevelFieldBindings;
    }

    public void setSecondLevelFieldBindings(DataFieldBindings secondLevelFieldBindings) {
        mSecondLevelFieldBindings = secondLevelFieldBindings;
    }
}
