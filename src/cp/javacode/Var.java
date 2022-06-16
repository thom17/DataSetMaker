package cp.javacode;

import java.util.Random;

public class Var {

    public boolean used;
    public boolean isClass = false;

    public String typeName;
    public String name;

    public Var(String []typeNameList, String []nameList) {
        Random random = new Random();
        int r = random.nextInt(typeNameList.length);
        typeName = typeNameList[r];
        r = random.nextInt(nameList.length);
        name = nameList[r];
        used = false;
    }

    public void upperTypeFirst() {
        char []arr = typeName.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        typeName = new String(arr);
        isClass = true;
    }
}
