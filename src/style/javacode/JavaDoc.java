package style.javacode;

import java.util.Random;

import fileManager.NameList;

public class JavaDoc extends SourceCode {
    public JavaDoc(String tab) {
        this.tab = tab;
        this.code = new StringBuilder();
    }

    /**
     * 1~5줄의 무작위 글 생성.
     * 각 줄당 5~20개의 단어를 나열한다.
     */
    public void simpleMake() {
        Random rand = new Random();
        code.append(tab).append("/**\n");
        for (int line = 0; line < rand.nextInt(1, 6); line++) {
            code.append(tab + "*");
            for (int num = 0; num < rand.nextInt(5, 21); num++) {
                String word = NameList.getRandWord();
                code.append(" "+word);
            }
            code.append(".\n");
        }
        code.append(tab+"*/\n");
    }

    public static void main(String[] test) {
        for (int i = 0; i < 5; i++) {
            JavaDoc code = new JavaDoc("");
            code.simpleMake();
            System.out.println(code.code);
        }

    }
}
