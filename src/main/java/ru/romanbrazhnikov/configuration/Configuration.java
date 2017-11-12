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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
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
    private int Step = 1;
    private Cookies mCookies;
    private Markers mMarkers;
    private String mDestination;
    private String mFirstLevelPattern;
    private String mSecondLevelPattern;
    private DataFieldBindings mFirstLevelFieldBindings;
    private DataFieldBindings mSecondLevelFieldBindings;


    //
    // System fields
    //
    private Document f_doc;
    private XPath mXPath;
    private String mErrorMessage;

    public Configuration(){
        mXPath = XPathFactory.newInstance().newXPath();
    }

    public boolean initFromXmlFile(String xmlPath){
        String fileData;

        try
        {
            fileData = FileUtils.readFromFileToString(xmlPath);
        }
        catch (IOException e)
        {
            mErrorMessage = e.getMessage();
            return false;
        }

        return initFromXmlString(fileData);
    }

    public boolean initFromXmlString(String xmlString){
        DocumentBuilderFactory myDocFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder myDocBuilder;
        try
        {
            myDocBuilder = myDocFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            mErrorMessage = e.getMessage();
            return false;
        }


        try
        {
            f_doc = myDocBuilder.parse(new InputSource( new StringReader(xmlString)));
        }
        catch (SAXException e)
        {
            mErrorMessage = "SAXException: " + e.getMessage();
            return false;
        }
        catch (IOException e)
        {
            mErrorMessage = "IOException: " + e.getMessage();
            return false;
        }


        // read successfully
        return true;
    }
}
