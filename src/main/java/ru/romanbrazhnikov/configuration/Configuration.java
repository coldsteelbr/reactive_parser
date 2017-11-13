package ru.romanbrazhnikov.configuration;

import ru.romanbrazhnikov.configuration.cookies.Cookies;
import ru.romanbrazhnikov.configuration.datafieldbindings.DataFieldBindings;
import ru.romanbrazhnikov.configuration.markers.Markers;
import ru.romanbrazhnikov.configuration.requestarguments.RequestArguments;
import ru.romanbrazhnikov.sourceprovider.HttpMethods;

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
    private RequestArguments mRequestArguments;
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

    public void setRequestArguments(RequestArguments requestArguments) {
        mRequestArguments = requestArguments;
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

    public String getDebugInfo() {
        StringBuilder builder = new StringBuilder();

        builder.append("Name: ").append(mName).append("\n");
        builder.append("Method: ").append(mMethod).append("\n");
        builder.append("Encoding: ").append(mEncodingName).append("\n");
        builder.append("Delay: ").append(mDelayInMillis).append("\n");
        builder.append("Base Url: ").append(mBaseUrl).append("\n");
        builder.append("Format Url:").append(mFormatUrl).append("\n");
        builder.append("Request arguments: ").append(mRequestArguments).append("\n");
        builder.append("First page: ").append(mFirstPage).append("\n");
        builder.append("Max page pattern: ").append(mMaxPagePattern).append("\n");
        builder.append("Step: ").append(mStep).append("\n");
        builder.append("Cookies: ").append(mCookies).append("\n");
        builder.append("Markers: ").append(mMarkers).append("\n");
        builder.append("Destination: ").append(mDestination).append("\n");
        builder.append("First Level Pattern: ").append(mFirstLevelPattern).append("\n");
        builder.append("Second level pattern: ").append(mSecondLevelPattern).append("\n");
        builder.append("1L field bindings: ").append(mFirstLevelFieldBindings).append("\n");
        builder.append("2L field bindings: ").append(mSecondLevelFieldBindings).append("\n");

        return builder.toString();
    }
}
