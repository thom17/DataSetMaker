package style.javacode;

import java.util.Random;

import fileManager.NameList;

public class ImportDec extends SourceCode {
    public ImportDec(String tab) {
        this.tab = tab;
        this.code = new StringBuilder();
    }

    /**
     * 경로 개수 1~3개
     * 각 경로의 단어 Merge수 1~3개
     */
    public void simpleMake() {
        Random rand = new Random();
        for (int pathNum = 0; pathNum < rand.nextInt(1, 3); pathNum++) {
            int mergetNum = rand.nextInt(1, 4);
            this.code.append(NameList.getMergeWord(mergetNum));
            this.code.append(".");
        }
        String word = NameList.getRandFirstUpper();
        for (int i = 0; i < rand.nextInt(3); i++) {
            word += NameList.getRandFirstUpper();
        }
        this.code.append(word);

        this.code.setLength(code.length() - 1);
        this.code.append(";\n");
    }

    public static void main(String test[]) {
        ImportDec dec;
        for (int i = 0; i < 20; i++) {
            dec = new ImportDec("");
            dec.simpleMake();
            System.out.println(dec.code);
        }
    }
}
