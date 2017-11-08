package ru.romanbrazhnikov.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileUtils {
    public static void saveToFile(String p_stringToSave, String p_fileName)
    {
        try(  PrintWriter out = new PrintWriter(p_fileName)  ){
            out.println(p_stringToSave);
            out.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getClass() +": " + e.getMessage());
        }
    }

}
