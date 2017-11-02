package ru.romanbrazhnikov.parser;

import io.reactivex.Single;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegExParser implements ICommonParser {

    private String mPatternRegEx;
    private String mSource;
    private List<String> mGroupNames;
    private long mDelayInMillis;

    private Single rxParser;

    private Pattern mPattern;

    private List<Map<String, String>> mResultTable = new ArrayList<>();


    public RegExParser() {
        mDelayInMillis = 3000;
    }

    @Override
    public void setSource(String source) {
        mSource = source;
    }

    @Override
    public void setPattern(String pattern) {
        mPatternRegEx = pattern;
    }

    @Override
    public void setMatchNames(List<String> names) {
        mGroupNames = names;
    }

    @Override
    public boolean run() {

        try {
            mPattern = Pattern.compile(mPatternRegEx,
                    Pattern.CASE_INSENSITIVE | // A=a, B=b...
                            Pattern.UNICODE_CASE | // UNICODE mode on
                            Pattern.COMMENTS); // Comments and whitespaces permitted
        } catch (PatternSyntaxException e) {
            // TODO: set error
            return false;
        }

        // MULTITHREADING GOES HERE
        Matcher m = mPattern.matcher(mSource);

        while (m.find()) {
            int gCount = m.groupCount();
            System.out.println(gCount);
            Map<String, String> currentResultRow = new HashMap<>();
            for (String currentName : mGroupNames) {
                currentResultRow.put(currentName, m.group(currentName));
            }
            mResultTable.add(currentResultRow);
        }
        return true;
    }

    public void logger() {
        for (Map<String, String> curRow : mResultTable) {
            for (Map.Entry<String, String> currentResult : curRow.entrySet()) {
                System.out.print(currentResult.getKey() + ":" + currentResult.getValue() + " ");
            }
            System.out.println();
        }
    }
}
