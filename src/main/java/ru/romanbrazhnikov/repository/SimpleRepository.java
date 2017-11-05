package ru.romanbrazhnikov.repository;

import io.reactivex.Single;
import ru.romanbrazhnikov.sourceprovider.SourceProvider;

public class SimpleRepository implements ICommonRepository {

    private static SimpleRepository sRepository;

    public static SimpleRepository getInstance() {
        if (sRepository == null) {
            sRepository = new SimpleRepository();
        }

        return sRepository;
    }

    private SimpleRepository() {
    }

    private SourceProvider mProvider = new SourceProvider();

    @Override
    public Single<String> requestSource(Request request) {
        return mProvider.requestNext();
    }

    public boolean hasMore() {
        return mProvider.hasMore();
    }
}
