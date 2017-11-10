package ru.romanbrazhnikov;

import ru.romanbrazhnikov.parser.ICommonParser;
import ru.romanbrazhnikov.parser.ParseResult;
import ru.romanbrazhnikov.parser.RegExParser;
import ru.romanbrazhnikov.parser.SourceSuccessConsumer;
import ru.romanbrazhnikov.repository.SimpleRepository;
import ru.romanbrazhnikov.resultsaver.DummySaver;
import ru.romanbrazhnikov.resultsaver.ICommonSaver;
import ru.romanbrazhnikov.resultsaver.TextFileSaver;
import ru.romanbrazhnikov.sourceprovider.Cookie;
import ru.romanbrazhnikov.sourceprovider.HttpMethods;
import ru.romanbrazhnikov.sourceprovider.HttpSourceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Tester {
    static final String sValidFileName = "parsed_result";
    static final String sInValidFileName = "txt/txt";

    static final String sValidPattern
            = "<td[^>]*>\\s*(?<LEFT>.*?)\\s*</td>\\s*"
            + "<td[^>]*>\\s*(?<RIGHT>.*?)\\s*</td>\\s*";

    static final String sValidSource =
            "<table>\n" +
                    "  <tr>\n" +
                    "    <td class=\"left\">Car: </td>\n" +
                    "    <td>Mazda</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td class=\"left\">Glasses: </td>\n" +
                    "    <td>Optic 7</td>\n" +
                    "  </tr>\n" +
                    "</table>\n";

    static final String sEmptySource = "";

    static List<String> sValidNames = new ArrayList<>();

    static {
        sValidNames.add("LEFT");
        sValidNames.add("RIGHT");
    }

    static ParseResult sValidResult = new ParseResult();

    static {
        Map<String, String> row1 = new HashMap<>();
        row1.put("G1", "One");
        row1.put("G2", "Two");

        Map<String, String> row2 = new HashMap<>();
        row2.put("G1", "Once");
        row2.put("G2", "Twice");

        sValidResult.addRow(row1);
        sValidResult.addRow(row2);
    }

    //
    //  RegExParser
    //
    public static void testRegExParser_ValidInputPrintingResult() {
        ICommonParser parser = new RegExParser();
        parser.setPattern(sValidPattern);
        parser.setMatchNames(sValidNames);
        parser.setSource(sValidSource);

        // parse and display
        System.out.println("testRegExParser_ValidInputPrintingResult:");
        parser.parse().subscribe(
                parseResult -> {
                    for (Map<String, String> currentRow : parseResult.getResult()) {
                        for (Map.Entry<String, String> currentEntry : currentRow.entrySet()) {
                            System.out.println(currentEntry);
                        }
                        System.out.println();
                    }
                },
                System.out::println); // error case

    }

    public static void testRegExParser_NoSourceSet() {


        ICommonParser parser = new RegExParser();
        parser.setPattern(sValidPattern);
        parser.setMatchNames(sValidNames);

        // parse and display
        System.out.println("testRegExParser_NoSourceSet:");
        parser.parse().subscribe(
                parseResult -> {
                    for (Map<String, String> currentRow : parseResult.getResult()) {
                        for (Map.Entry<String, String> currentEntry : currentRow.entrySet()) {
                            System.out.println(currentEntry);
                        }
                        System.out.println();
                    }
                },
                System.out::println); // error case

    }

    public static void testRegExParser_EmptyResult() {


        ICommonParser parser = new RegExParser();
        parser.setPattern(sValidPattern);
        parser.setMatchNames(sValidNames);
        parser.setSource(sEmptySource); // Empty Source to cause empty result


        // parse and display
        System.out.println("testRegExParser_EmptyResult:");
        parser.parse().subscribe(
                parseResult -> {
                    for (Map<String, String> currentRow : parseResult.getResult()) {
                        for (Map.Entry<String, String> currentEntry : currentRow.entrySet()) {
                            System.out.println(currentEntry);
                        }
                        System.out.println();
                    }
                },
                System.out::println); // error case
    }

    public static void testRegExParser_NoMatchingNamesSet() {

        ICommonParser parser = new RegExParser();
        parser.setPattern(sValidPattern);
        parser.setSource(sValidSource);

        // parse and display
        System.out.println("testRegExParser_ValidInputPrintingResult:");
        parser.parse().subscribe(
                parseResult -> {
                    for (Map<String, String> currentRow : parseResult.getResult()) {
                        for (Map.Entry<String, String> currentEntry : currentRow.entrySet()) {
                            System.out.println(currentEntry);
                        }
                        System.out.println();
                    }
                },
                System.out::println); // error case

    }

    public static void testRegExParser_EmptyMatchingNamesSet() {

        ICommonParser parser = new RegExParser();
        parser.setPattern(sValidPattern);
        parser.setSource(sValidSource);
        parser.setMatchNames(new ArrayList<>()); // Empty match names

        // parse and display
        System.out.println("testRegExParser_ValidInputPrintingResult:");
        parser.parse().subscribe(
                parseResult -> {
                    for (Map<String, String> currentRow : parseResult.getResult()) {
                        for (Map.Entry<String, String> currentEntry : currentRow.entrySet()) {
                            System.out.println(currentEntry);
                        }
                        System.out.println();
                    }
                },
                System.out::println); // error case

    }

    //
    //  TextFileResultSaver
    //
    public static void testTextFileResultSaver_ValidResultSaving() {
        ICommonSaver saver = new TextFileSaver(sValidFileName + ".txt");

        saver.save(sValidResult).subscribe(
                () -> System.out.println("Saved."),
                throwable -> System.out.println(throwable.getMessage()));
    }

    public static void testTextFileResultSaver_InvalidFileNameResultSaving() {
        ICommonSaver saver = new TextFileSaver(sInValidFileName + ".txt");

        saver.save(sValidResult).subscribe(
                () -> System.out.println("Saved."),
                throwable -> System.out.println(throwable.getMessage()));
    }

    //
    //  HttpSourceProvider
    //
    public static void testHttpSourceProvider_httpGetRequest() {
        String baseUrl = "http://spran.ru/sell/comm.html";
        String queryParams = "currency=1&costMode=1&cities%5B0%5D=21";
        HttpSourceProvider provider = new HttpSourceProvider(baseUrl, "UTF-8", HttpMethods.GET, queryParams);

        provider.requestSource().subscribe(
                source -> System.out.println("Source: " + source),
                System.out::println);

    }

    public static void testHttpSourceProvider_httpPostRequest() {
        String baseUrl = "";
        String queryParams = "";
        HttpSourceProvider provider = new HttpSourceProvider(baseUrl, "UTF-8", HttpMethods.POST, queryParams);

        provider.requestSource().subscribe(
                source -> System.out.println("Source: " + source),
                System.out::println);
    }

    public static void testHttpSourceProvider_printingCookieHeaders() {
        String baseUrl = "http://spran.ru/sell/comm.html";
        String queryParams = "currency=1&costMode=1&cities%5B0%5D=21";
        HttpSourceProvider provider = new HttpSourceProvider(baseUrl, "UTF-8", HttpMethods.GET, queryParams);

        provider.requestSource().subscribe(
                source -> {
                    if (provider.getCookieHeadersFromResponse() != null) {
                        System.out.println("Cookie headers:");
                        for (String currentHeader : provider.getCookieHeadersFromResponse()) {
                            System.out.println(currentHeader);
                        }
                    }
                },
                System.out::println);

    }

    public static void testHttpSourceProvider_settingCookieHeaders() {
        String baseUrl = "http://prosto.tomsk.ru/";
        String queryParams = "rm=prosto_offers_list&l_page=2";

        HttpSourceProvider provider = new HttpSourceProvider(baseUrl, "UTF-8", HttpMethods.GET, queryParams);
        List<Cookie> cookieList = new ArrayList<>();
        cookieList.add(new Cookie("PHPSESSID", "h5dtutre745mrvgkl6tonc7sc3", null));
        provider.setCustomCookies(cookieList);

        provider.requestSource().subscribe(
                source -> {
                    System.out.println(source);

                    if (provider.getCookieHeadersFromResponse() != null) {
                        System.out.println("Cookie headers:");
                        for (String currentHeader : provider.getCookieHeadersFromResponse()) {
                            System.out.println(currentHeader);
                        }
                    }
                },
                Throwable::printStackTrace);

    }


    // Cookie
    public static void testCookie(){
        Cookie cookie = new Cookie("PHPSESSION", "qwe123", ".example.com");

        System.out.println(cookie.getHeader());
    }

    //
    public static void testComplexParser() {
        String pattern = "<td[^>]*>\\s*(?<LEFT>.*?)\\s*</td>\\s*"
                + "<td[^>]*>\\s*(?<RIGHT>.*?)\\s*</td>\\s*";

        List<String> names = new ArrayList<>();
        names.add("LEFT");
        names.add("RIGHT");

        ICommonSaver saver = new DummySaver();
        ICommonSaver fileSaver = new TextFileSaver("dummy_file.txt");

        ICommonParser parser = new RegExParser();
        parser.setPattern(pattern);
        parser.setMatchNames(names);

        SimpleRepository repository = SimpleRepository.getInstance();

        while (repository.hasMore()) {

            // TODO: request parser here
            repository.requestSource(null)
                    .timeout(10, TimeUnit.SECONDS)
                    .subscribe(
                            new SourceSuccessConsumer(parser, fileSaver),
                            System.out::println);

        }
    }

}
