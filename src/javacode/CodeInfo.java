package javacode;

import fileManager.NameList;

import java.util.ArrayList;
import java.util.Random;

public class CodeInfo {
    Random rand = new Random();
    String label;

    ArrayList<Var> parameterList = new ArrayList<Var>();
    Var[] parameterDataVar;
    Var[] parameterStempVar;
    Var controlVar;

    int stampSize = -1;
    int dataSize = -1;

    Var[] commonVar;
    String commonHaser;

    String funName;

    ArrayList<String> memberList = new ArrayList<String>();
    String []nameCandidateList;
    String []typeList;

    public String makeBlock(int level) {
        boolean allUse = false;
        StringBuilder sb = new StringBuilder("{\n");
        while ( !allUse) {

            sb.append("\t").append(makeExp()).append(";\n");

            //전부 사용 했는지 체크하여 allUse값 설정
            for (Var var : parameterList)
                if (var.used) {
                    allUse = true;
                } else {
                    allUse = false;
                    break;
                }
        }
        sb.append("}");
        return sb.toString();
    }

    private String makeExp() {
        StringBuilder sb = new StringBuilder(randAssign());
        sb.append(makeMethodCallExpr());
        return sb.toString();
    }

    private String makeMethodCallExpr() {
        StringBuilder sb = new StringBuilder();
        // member.
        if (rand.nextBoolean() ) {
            String memberName = nameCandidateList[rand.nextInt(nameCandidateList.length)];
            if (!isIn(parameterList, memberName)) {
                sb.append(memberName).append(".");
            }
        }
        //member.? fun
        String calledMethodName = nameCandidateList[rand.nextInt(nameCandidateList.length)];
        while ( calledMethodName.equals(this.funName) || isIn(parameterList, calledMethodName) || memberList.contains(calledMethodName)) {
            calledMethodName = nameCandidateList[rand.nextInt(nameCandidateList.length)];
        }
        sb.append(calledMethodName);
        sb.append("(");
        //member.?fun( ? ) #단순 메서드인지?
        sb.append(makeArgueMent());
        sb.append(")");
        return sb.toString();
    }

    private String makeArgueMent() {
        StringBuilder sb = new StringBuilder();
        if (rand.nextBoolean()) {
            int argNum = rand.nextInt(4);
            for (int i = 0; i < argNum; i++) {
                //use Param
                if (20 < rand.nextInt(101)) {
                    String name = parameterChoice();
                    sb.append(name);
                    sb.append(", ");
                } else {
                    String arg = nameCandidateList[rand.nextInt(nameCandidateList.length)];
                    if (isIn( parameterList, arg)) {
                        sb.append("this.");
                    }
                    sb.append(arg).append(", ");
                }
            } //for문 끝
            if (0 < argNum) {
                sb.setLength(sb.length() - 2);
            }
        }
        return sb.toString();
    }

    private String parameterChoice() {
        int index = rand.nextInt(parameterList.size());
        Var useVar = null;
        for ( int i = 0; i < 10; i++) {
            index = rand.nextInt(parameterList.size());
            useVar = parameterList.get(index);
            if (!useVar.used)
                useVar.used = true;
                break;
        }
        return useVar.name;
    }

    private String randAssign() {
        StringBuilder sb = new StringBuilder();
        if (rand.nextBoolean()) {
            String memberName = nameCandidateList[rand.nextInt(nameCandidateList.length)];
            if (isIn(parameterList, memberName)) {
                sb.append("this.");
            }
            sb.append(memberName + " = ");
            return sb.toString();
        } else {
            return "";
        }

    }

    public String makeCode(int level) {
        nameCandidateList = NameList.getWordList();
        switch (level) {
            case 4:
                addCommonVar();
            case 3:
                addControlVar();
            case 2:
                addStampVar();
            case 1:
                addDataVar();
            default:
                label = "cp" + level;
                StringBuilder code = new StringBuilder(makeFunctionNameDef());
                code.append(makeBlock(level));
                return code.toString();
        }
    }

    private void addDataVar() {
        if (dataSize < 0) {
            dataSize = rand.nextInt(1, 3);
        }
        parameterDataVar = new Var[dataSize];
        for (int i = 0; i < dataSize; i++) {
            Var var = new Var(NameList.javaDataTypeList, nameCandidateList);
            if ( isIn(parameterList, var.name) ) {
                i--;
            } else {
                parameterDataVar[i] = var;
                parameterList.add(var);
            }
        }
    }

    private void addStampVar() {
        stampSize = rand.nextInt(1, 3);
        parameterStempVar = new Var[stampSize];
        dataSize = rand.nextInt(3);
        for (int i = 0; i < stampSize; i++) {
            Var var = new Var(nameCandidateList, nameCandidateList);
            if ( isIn(parameterList, var.name) ) {
                i--;
            } else {
                var.upperTypeFirst();
                parameterStempVar[i] = var;
                parameterList.add(var);
            }
        }
    }

    private void addControlVar() {
    }

    private void addCommonVar() {

    }

    private String makeFunctionNameDef() {
        funName = nameCandidateList[rand.nextInt(nameCandidateList.length)];
        while ( isIn(parameterList, funName)) {
            funName = nameCandidateList[rand.nextInt(nameCandidateList.length)];
        }

        StringBuilder code = new StringBuilder("public void " + funName + "(");
        for (Var var : parameterList) {
            code.append(var.typeName).append(" ").append(var.name).append(", ");
        }
        code.setLength(code.length() - 2);
        code.append(")");
        return code.toString();
    }

    private boolean isIn(ArrayList<Var> varList, String name) {
        for (Var var : varList) {
            if (name.equals(var.name)) {
                return true;
            }
        }
        return false;
    }

    public String makeStampCp() {


        return null;
    }

    public static void main(String[] args) {
        CodeInfo code = new CodeInfo();
        System.out.println(code.makeCode(1));
    }
}
