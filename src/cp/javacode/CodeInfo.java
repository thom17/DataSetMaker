package cp.javacode;

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
    String []typeList = NameList.javaDataTypeList;

    public String makeBlock(int level) {
        boolean allUse = false;
        StringBuilder sb = new StringBuilder("{\n");
        ArrayList<String> lines = new ArrayList<String>();
        while ( !allUse) {
            lines.add(makeExp());

            //전부 사용 했는지 체크하여 allUse값 설정
            for (Var var : parameterList)
                if (var.used) {
                    allUse = true;
                } else {
                    allUse = false;
                    break;
                }
        }

        if ( 2 < level) {
            String tab = "\t";
            int pos = 0;
            int []sep = addIfStmt(lines, controlVar);
            for(int i=0; i < lines.size(); i++) {
                if (pos == 3 || sep[pos] == i) {
                    tab = "\t";
                    sb.append(tab+lines.get(i)+"\n");
                    if(pos < 3) {
                        tab ="\t\t";
                        pos++;
                    }
                } else {
                    sb.append(tab+lines.get(i)+";\n");
                }


            }
        } else {
            String tab = "\t";
            for (int i=0; i < lines.size(); i++) {
                sb.append(tab + lines.get(i) + ";\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    private int[] addIfStmt(ArrayList<String> lines, Var condVar) {

        if (5 < lines.size()) {
            int []sep = new int[3];
            do {
                sep[0] = rand.nextInt(lines.size());
                sep[1] = rand.nextInt(lines.size()-sep[0]) + sep[0];
                sep[2] = rand.nextInt(lines.size()-sep[1]) + sep[1];
            } while (sep[0] == sep[1] || sep[1] == sep[2]);

            if( condVar.typeName.equals("boolean") && 4 < rand.nextInt(12) ) {
                String cond = "if ( " +condVar.name+" ) {";
                lines.add(sep[0], cond);
            } else {
                String cond = "if ( "+ nameCandidateList[rand.nextInt(nameCandidateList.length)]+"("+condVar.name+")) {";
                lines.add(sep[0], cond);
            }

            lines.add(++sep[1] , "} else {");
            lines.add(++sep[2]+1, "}");
            return sep;
        }else {
            String cond = "if ( "+ nameCandidateList[rand.nextInt(nameCandidateList.length)]+"("+condVar.name+")) {";
            lines.add(0, cond);
            lines.add(2, "}");
            int []sep = new int[3];
            sep[0] = 0;
            sep[1] = 2;
            sep[2] = 99999999;
            return sep;
        }

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
            var.isClass = false;
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
        Var var = null;
        if ( 40 < rand.nextInt(101)) {
            var = new Var(typeList, nameCandidateList);
        } else {
            var = new Var(nameCandidateList, nameCandidateList);
            var.upperTypeFirst();
        }
        parameterList.add(var);
        controlVar = var;
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
