package ru.romanbrazhnikov.sourceprovider;

import io.reactivex.Single;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpSourceProvider {

    private String mFullUrl = "";

    public HttpSourceProvider(String url) {
        mFullUrl = url;
    }


    public Single<String> requestSource() {
        return Single.create(emitter -> {
            try {
                // opening connection
                URL myURL = new URL(mFullUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) myURL.openConnection();

                // opening input stream from the connection
                InputStream httpResponse = httpConnection.getInputStream();

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
}
