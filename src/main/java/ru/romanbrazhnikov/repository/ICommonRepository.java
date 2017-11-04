package ru.romanbrazhnikov.repository;

import io.reactivex.Single;

public interface ICommonRepository {
    Single<String> requestSource(Request request);
}
