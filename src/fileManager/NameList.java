package fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NameList {
    public static final String baseFilePath = "word.txt";
    public static final String[] javaDataTypeList = {"int", "float", "boolean", "double", "String", "long"};
    public static String[] wordList = null;

    public static String[] getWordList() {
        if (wordList == null) {
            readFile();
        }
        return wordList;
    }

    private static void readFile() {
        try {
            BufferedReader file = new BufferedReader(new FileReader(new File(baseFilePath)));
            ArrayList<String> list = new ArrayList<String>();
            String word = file.readLine();
            while (word != null) {
                list.add(word);
                word = file.readLine();
            }
            wordList = list.toArray(new String[0]);

        } catch (FileNotFoundException e) {
            System.out.println("baseFilePath에 해당하는 파일이 없습니다.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String []test) {
        String li[] = getWordList();
        for(String str : li) {
            System.out.println(str);
        }
    }

}
