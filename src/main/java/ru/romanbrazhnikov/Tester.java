package ru.romanbrazhnikov;

import ru.romanbrazhnikov.parser.ICommonParser;
import ru.romanbrazhnikov.parser.RegExParser;
import ru.romanbrazhnikov.sourceprovider.SourceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Tester {
    public static void testParser() {
        String pattern = "<td[^>]*>\\s*(?<LEFT>.*?)\\s*</td>\\s*"
                + "<td[^>]*>\\s*(?<RIGHT>.*?)\\s*</td>\\s*";

        List<String> names = new ArrayList<>();
        names.add("LEFT");
        names.add("RIGHT");

        ICommonParser parser = new RegExParser();
        parser.setPattern(pattern);
        parser.setMatchNames(names);

        SourceProvider provider = new SourceProvider();

        while (provider.hasMore()) {

            provider.requestNext()
                    .timeout(10, TimeUnit.SECONDS)
                    .subscribe(
                            s -> {
                                // TODO: request parser from a parser pool
                                parser.setSource(s);
                                parser.parse().subscribe(parseResult -> {
                                    List<Map<String, String>> res = parseResult.getResult();
                                    System.out.println("Page:");
                                    for (Map<String, String> curRow : res) {
                                        for (Map.Entry entry : curRow.entrySet()) {
                                            System.out.print(entry + " ");
                                        }
                                        System.out.println();
                                    }
                                }, System.out::println);
                            }, System.out::println);

        }
    }
}
