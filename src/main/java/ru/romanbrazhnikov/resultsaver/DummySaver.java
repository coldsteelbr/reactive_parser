package ru.romanbrazhnikov.resultsaver;

import ru.romanbrazhnikov.parser.ParseResult;

import java.util.List;
import java.util.Map;

public class DummySaver implements ICommonSaver {
    @Override
    public void save(final ParseResult parseResult) {
        List<Map<String, String>> rows = parseResult.getResult();

        for (Map<String, String> curRow : rows) {
            for (Map.Entry<String, String> curEntry : curRow.entrySet()) {
                System.out.println(curEntry.getKey() + ": " + curEntry.getValue());
            }
            System.out.println();
        }
    }
}
