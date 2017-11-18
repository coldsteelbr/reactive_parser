package ru.romanbrazhnikov;

import io.reactivex.Flowable;
import ru.romanbrazhnikov.incubator.ParserStarter;

import java.io.IOException;

public class Main {

    private static void startAllTests() {
        //
        //  TESTING RegExParser
        //
        //Tester.testRegExParser_ValidInputPrintingResult();
        //Tester.testRegExParser_NoSourceSet();
        //Tester.testRegExParser_EmptyResult();
        //Tester.testRegExParser_NoMatchingNamesSet();
        //Tester.testRegExParser_EmptyMatchingNamesSet();


        //
        // TESTING TextFileSaver
        //
        //Tester.testTextFileResultSaver_ValidResultSaving();
        //Tester.testTextFileResultSaver_InvalidFileNameResultSaving();

        //
        //  TESTING Http connect
        //
        //Tester.testHttpSourceProvider_printingCookieHeaders();
        //Tester.testHttpSourceProvider_httpGetRequest();
        // TODO: find "post" site
        //Tester.testHttpSourceProvider_httpPostRequest();

        //Tester.testCookie();
        //Tester.testHttpSourceProvider_settingCookieHeaders();

        //Tester.testConfiguration_fullSampleConfig();

        //
        // TESTING complex
        //
        Tester.testComplexConfig_OneThreadParser();
    }

    private static void startIncubator() {
        ParserStarter starter = new ParserStarter();
        starter.run();
    }

    public static void main(String[] args) {
        Flowable.just("Starting...").subscribe(System.out::println);


        startAllTests();
        //startIncubator();


        System.out.println("Hit {ENTER} key to terminate");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
