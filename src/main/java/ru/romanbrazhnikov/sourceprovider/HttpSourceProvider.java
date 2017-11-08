package ru.romanbrazhnikov.sourceprovider;

import io.reactivex.Single;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class HttpSourceProvider {
    // TODO: Add encoding conversion
    // TODO: from server: ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(myString)
    private String mBaseUrl = "";
    private List<String> mCookieHeaderList;
    private String mClientCharSet;
    private HttpMethods mHttpMethod;
    private String mQueryParamString;

    public HttpSourceProvider(String url, String clientCharSet, HttpMethods method, String queryParamString) {
        mBaseUrl = url;
        mClientCharSet = clientCharSet;
        mHttpMethod = method;
        mQueryParamString = queryParamString;
    }

    public Single<String> requestSource() {
        return Single.create(emitter -> {
            try {
                // opening connection
                URL myURL;// = new URL(mBaseUrl);
                HttpURLConnection httpConnection = null;// = (HttpURLConnection) myURL.openConnection();

                // METHOD
                switch (mHttpMethod) {

                    case GET:
                        myURL = new URL(mBaseUrl + "?" + mQueryParamString);
                        httpConnection = (HttpURLConnection) myURL.openConnection();
                        break;
                    case POST:
                        myURL = new URL(mBaseUrl);
                        httpConnection = (HttpURLConnection) myURL.openConnection();
                        httpConnection.setDoOutput(true); // Triggers POST.
                        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + mClientCharSet);

                        // Sending POST form
                        try (OutputStream output = httpConnection.getOutputStream()) {
                            output.write(mQueryParamString.getBytes(mClientCharSet));
                        }

                        break;
                }
                // setting encoding
                // TODO: TBD setting encoding from server

                // opening input stream from the connection
                InputStream httpResponse = httpConnection.getInputStream();

                // getting cookies
                Set httpHeaders = httpConnection.getHeaderFields().keySet();

                // cookie headers
                if (httpHeaders.contains("Set-Cookie")) {
                    mCookieHeaderList = httpConnection.getHeaderFields().get("Set-Cookie");
                }

                // result html response
                StringBuilder responseHtmlBuilder = new StringBuilder();

                // trying to read from the input stream
                try (Scanner responseScanner = new Scanner(httpResponse)) {
                    // while there's something to read...
                    while (responseScanner.hasNextLine()) {
                        // reading current line
                        responseHtmlBuilder.append(responseScanner.nextLine()).append("\n");
                    }
                    // closing the scanner after reading
                    responseScanner.close();

                    // returning result
                    emitter.onSuccess(responseHtmlBuilder.toString());
                } catch (Exception e) {
                    emitter.onError(e);
                } finally {
                    // closing input stream
                    httpResponse.close();
                }

            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    public List<String> getCookieHeadersFromResponse() {
        return mCookieHeaderList;
    }
}
