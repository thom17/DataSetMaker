package fileManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NameList {
    public static final String baseFilePath = "word.txt";
    public static final String[] javaDataTypeList = {"int", "float", "boolean", "double", "String", "long"};
    public static String[] wordList = getWordList();
    public static Random rand = new Random();

    public static String[] getWordList() {
        if (wordList == null) {
            readFile();
        }
        return wordList;
    }

    public static String getRandWord() {
        return wordList[rand.nextInt(wordList.length)];
    }

    /**
     * 복합 단어 생성 ex) alertsLoots
     * @param num : 조합할 단어의 개수
     * @return
     */
    public static String getMergeWord(int num) {
        String word = getRandWord();
        for (int i = 1; i < num; i++) {
            String addWord = getRandWord();
            addWord = getRandFirstUpper();
            word+=addWord;

        }
        return word;
    }
    public static String getRandFirstUpper(){
        String word = getRandWord();
        word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
        return word;
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

    public static void main(String[] test) {
        String li[] = getWordList();
        for (String str : li) {
            System.out.println(str);
        }
    }

}
