package ru.romanbrazhnikov;

import ru.romanbrazhnikov.parser.ICommonParser;
import ru.romanbrazhnikov.parser.RegExParser;
import ru.romanbrazhnikov.parser.SourceSuccessConsumer;
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

            // TODO: request parser here
            provider.requestNext()
                    .timeout(10, TimeUnit.SECONDS)
                    .subscribe(
                            new SourceSuccessConsumer(parser),
                            System.out::println);

        }
    }
}
