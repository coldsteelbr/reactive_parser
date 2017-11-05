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
import java.util.concurrent.TimeUnit;

public class Tester {
    public static void testParser() {
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
