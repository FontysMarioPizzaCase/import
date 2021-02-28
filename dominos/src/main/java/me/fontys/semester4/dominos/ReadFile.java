package me.fontys.semester4.dominos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {

    public static Scanner openFile(String filepath) throws FileNotFoundException {
        File myFile = new File(filepath);
        return new Scanner(myFile);
    }

    public static void closeFile(Scanner reader) {
        reader.close();
    }
}
