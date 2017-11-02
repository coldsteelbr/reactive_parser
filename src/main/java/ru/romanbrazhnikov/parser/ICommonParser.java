package ru.romanbrazhnikov.parser;

import java.util.List;

public interface ICommonParser {
    void setPattern(String pattern);

    void setSource(String source);

    void setMatchNames(List<String> names);

    boolean run();
}
