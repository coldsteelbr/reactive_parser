package ru.romanbrazhnikov;

import ru.romanbrazhnikov.parser.ICommonParser;
import ru.romanbrazhnikov.parser.RegExParser;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static void testParser() {
        String pattern = "<td[^>]*>\\s*(?<LEFT>.*?)\\s*</td>\\s*"
                + "<td[^>]*>\\s*(?<RIGHT>.*?)\\s*</td>\\s*";
        String source =
                "<table>" +
                        "<tr>" +
                        "<td>Top left</td>" +
                        "<td>Top right</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>Bottom left</td>" +
                        "<td>Bottom right</td>" +
                        "</tr>" +
                        "</table>";
        List<String> names = new ArrayList<>();
        names.add("LEFT");
        names.add("RIGHT");

        ICommonParser parser = new RegExParser();
        parser.setPattern(pattern);
        parser.setSource(source);
        parser.setMatchNames(names);

        if (!parser.run()) {
            System.out.println("RUN crashed");
            return;
        }

        ((RegExParser) parser).logger();

    }
}
