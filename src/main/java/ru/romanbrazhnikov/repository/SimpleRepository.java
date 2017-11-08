package ru.romanbrazhnikov.repository;

import io.reactivex.Single;
import ru.romanbrazhnikov.sourceprovider.DummySourceProvider;

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

    private DummySourceProvider mProvider = new DummySourceProvider();

    @Override
    public Single<String> requestSource(Request request) {
        return mProvider.requestNext();
    }

    public boolean hasMore() {
        return mProvider.hasMore();
    }
}
