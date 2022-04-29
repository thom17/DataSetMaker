package fileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javacode.CodeInfo;

public class Maker {
    public static void makeCP() {
        String path = "result/cp/level1/";
        for (int i=0; i < 1024; i++) {
            File file = new File(path + "cp" + (i + 1) + ".java");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                CodeInfo code = new CodeInfo();
                writer.write(code.makeCode(1));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        path = "result/cp/level2/";
        for (int i = 0; i < 1024; i++) {
            File file = new File(path + "cp" + (i + 1) + ".java");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                CodeInfo code = new CodeInfo();
                writer.write(code.makeCode(2));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String []args) {
        makeCP();



    }
}
