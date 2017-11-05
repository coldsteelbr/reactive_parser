package ru.romanbrazhnikov.resultsaver;

import ru.romanbrazhnikov.parser.ParseResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Saves result to file
 */
public class FileSaver implements ICommonSaver {

    private String mFileName;

    public FileSaver(String fileName) {
        mFileName = fileName;
    }

    private void saveStringToFile(String stringToSave) {
        Path file = Paths.get(mFileName);
        try {
            Files.write(file, stringToSave.getBytes(), CREATE, APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(ParseResult parseResult) {
        StringBuffer buffer = new StringBuffer();
        for (Map<String, String> currentRow : parseResult.getResult()) {
            for (Map.Entry currentValue : currentRow.entrySet()) {
                buffer.append(" ")
                        .append(currentValue.getValue());
            }
            buffer.append("\n");
        }
        saveStringToFile(buffer.toString());
    }
}
