package ru.romanbrazhnikov.resultsaver;

import io.reactivex.Completable;
import ru.romanbrazhnikov.parser.ParseResult;

public interface ICommonSaver {
    Completable save(ParseResult parseResult);
}
