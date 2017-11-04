package ru.romanbrazhnikov.repository;

import io.reactivex.Single;

public class SimpleRepository implements ICommonRepository {
    @Override
    public Single<String> requestSource(Request request) {
        return null;
    }
}
