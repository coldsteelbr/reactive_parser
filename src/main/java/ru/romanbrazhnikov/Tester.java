package ru.romanbrazhnikov;

import ru.romanbrazhnikov.parser.ICommonParser;
import ru.romanbrazhnikov.parser.RegExParser;
import ru.romanbrazhnikov.parser.SourceSuccessConsumer;
import ru.romanbrazhnikov.repository.SimpleRepository;
import ru.romanbrazhnikov.resultsaver.DummySaver;
import ru.romanbrazhnikov.resultsaver.FileSaver;
import ru.romanbrazhnikov.resultsaver.ICommonSaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Tester {

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

    public static void testComplexParser() {
        String pattern = "<td[^>]*>\\s*(?<LEFT>.*?)\\s*</td>\\s*"
                + "<td[^>]*>\\s*(?<RIGHT>.*?)\\s*</td>\\s*";

        List<String> names = new ArrayList<>();
        names.add("LEFT");
        names.add("RIGHT");

        ICommonSaver saver = new DummySaver();
        ICommonSaver fileSaver = new FileSaver("dummy_file.txt");

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
