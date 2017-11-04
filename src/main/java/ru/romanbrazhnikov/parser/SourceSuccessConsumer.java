package ru.romanbrazhnikov.parser;

import io.reactivex.functions.Consumer;

import java.util.List;
import java.util.Map;

public class SourceSuccessConsumer implements Consumer<String>{

    private final ICommonParser mParser;
    public SourceSuccessConsumer(ICommonParser parser){
        mParser = parser;
    }

    @Override
    public void accept(String source) throws Exception {
        mParser.setSource(source);
        mParser.parse().subscribe(parseResult -> {
            List<Map<String, String>> res = parseResult.getResult();
            System.out.println("Page:");
            for (Map<String, String> curRow : res) {
                for (Map.Entry entry : curRow.entrySet()) {
                    System.out.print(entry + " ");
                }
                System.out.println();
            }
        }, System.out::println);
    }
}
