package ru.romanbrazhnikov;

import io.reactivex.Flowable;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Flowable.just("Starting...").subscribe(System.out::println);


        Tester.testRegExParser_ValidInputPrintingResult();
        Tester.testRegExParser_NoSourceSet();
        Tester.testRegExParser_EmptyResult();
        Tester.testRegExParser_NoMatchingNamesSet();
        Tester.testRegExParser_EmptyMatchingNamesSet();


        System.out.println("Hit any key to terminate");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
