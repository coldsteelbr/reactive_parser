package ru.romanbrazhnikov;

import io.reactivex.Flowable;

public class Main {

    public static void main(String[] args) {
        Flowable.just("Starting...").subscribe(System.out::println);
        Tester.testParser();
    }
}
