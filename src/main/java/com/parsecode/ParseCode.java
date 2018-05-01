package com.parsecode;

import java.io.*;
import java.util.*;

import com.dao.DaoUtil;
import com.dao.ProjectDao;
import com.entity.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.*;

/**
 * Created by flyboss on 2018/3/29.
 */
public class ParseCode {
    private String packageName;
    private String className;
    private String filePath;
    private CompilationUnit compilationUnit;
    private String fileContent;
    private static Logger logger = LogManager.getLogger(ParseCode.class);
    public static void main(String[] args) {
//        DaoUtil.truncateTable("code");
//        DaoUtil.truncateTable("code_doc");
//        DaoUtil.truncateTable("func_index");
//        DaoUtil.truncateTable("func_index_code");
        ParseCode parseCode = new ParseCode();
        //parseCode.parsePoject();
        parseCode.run("F:\\codewarehouse\\elasticsearch\\modules\\lang-painless\\src\\main\\java\\org\\elasticsearch\\painless\\node\\EBinary.java","https:");
    }

    public void parsePoject(){
        ProjectDao projectDao=new ProjectDao();
        List<Project> projects=projectDao.getAll();
        for (Project project:projects) {
            if (project.isParse()){
                continue;
            }
            List<File> files=getFilesFromProject(project.getFilePath());
            for (File file : files) {
                run(file.getAbsolutePath(),project.getBaseUrl());
            }
            project.setParse(true);
            projectDao.update(project);
        }

    }

    public void run(String filepath,String bastUrl) {
        this.filePath = filepath;
        compilationUnit = getAst();
        List types = compilationUnit.types();
        if (types.size() == 0) {
            return;
        }
        TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
        if (canSkip(typeDec)) {
            return;
        }

        try{
            packageName = compilationUnit.getPackage().getName().toString();
        } catch (Exception e){
            logger.error(e);
        }
        className = typeDec.getName().getFullyQualifiedName();

        CodeVisitor codeVisitor = new CodeVisitor();
        codeVisitor.setCodeVisitor(packageName, className, filePath, compilationUnit, fileContent,bastUrl);
        compilationUnit.accept(codeVisitor);
    }

    private boolean canSkip(TypeDeclaration typeDec) {
        ITypeBinding iTypeBinding = typeDec.resolveBinding();
        boolean canSkip = false;
        canSkip |= Modifier.isAbstract(iTypeBinding.getModifiers());
        canSkip |= Modifier.isPrivate(iTypeBinding.getModifiers());
        canSkip |= typeDec.isInterface();
        return canSkip;
    }

    private CompilationUnit getAst() {
        ASTParser parser = ASTParser.newParser(AST.JLS9);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);//to parse compilation unit
        parser.setEnvironment(null, null, null, true);
        parser.setUnitName(filePath.substring(filePath.lastIndexOf('\\'), filePath.length()));//需要与代码文件的名称一致
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        fileContent = readToString();


        parser.setSource(fileContent.toCharArray());
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null);
    }

    private String readToString() {
        String encoding = "UTF-8";
        File file = new File(filePath);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    //TODO 获得准确的非test java文件
    private List<File> getFilesFromProject(String projectPath){
        Queue<File> folders = new LinkedList<File>();
        List<File> javaFiles = new ArrayList<File>();
        folders.offer(new File(projectPath));
        while (folders.size() != 0) {
            File folder = folders.poll();
            File[] files = folder.listFiles();
            for (File file : files) {
                if(file.isDirectory()){
                    if (!file.getName().contains("test")){
                        folders.offer(file);
                    }
                }else if (file.getName().endsWith(".java")){
                    javaFiles.add(file);
                }
            }
        }
        return javaFiles;
    }

//    private void parseMethod(TypeDeclaration typeDec, CompilationUnit compilationUnit) {
//        MethodDeclaration methodDec[] = typeDec.getMethods();
//        System.out.println("Method:");
//        for (MethodDeclaration method : methodDec) {
//            int startline = compilationUnit.getLineNumber(method.getStartPosition());
//            Integer lastline = new Integer(0);
//            System.out.println(startline);
//            //get method name
//            SimpleName methodName = method.getSearchName();
//            System.out.println("method name:" + methodName);
//
//            //get method javadoc
//            Javadoc javadoc = method.getJavadoc();
//            System.out.println("method javadoc:" + javadoc);
//
//            //get method parameters
//            List param = method.parameters();
//            System.out.println("method parameters:" + param);
//
//            //get method return type
//            Type returnType = method.getReturnType2();
//            System.out.println("method return type:" + returnType);
//            //get method body
//            Block body = method.getBody();
//            List statements = body.statements();
//            Iterator iter = statements.iterator();
//            while (iter.hasNext()) {
//                //get each statement
//                Statement stmt = (Statement) iter.next();
//                parseStatement(stmt);
//
//            }
//            String methodContent = readMethod(startline, lastline);
//            System.out.println(methodContent);
//        }
//    }

//    private void parseStatement(Statement stmt) {
//        //lastline = compilationUnit.getLineNumber(stmt.getStartPosition());
//        if (stmt instanceof ExpressionStatement) {
//            parseExpressionStatement((ExpressionStatement) stmt);
//        } else if (stmt instanceof IfStatement) {
//            IfStatement ifstmt = (IfStatement) stmt;
//            InfixExpression wex = (InfixExpression) ifstmt.getExpression();
//            System.out.println("if-LHS:" + wex.getLeftOperand() + "; ");
//            System.out.println("if-op:" + wex.getOperator() + "; ");
//            System.out.println("if-RHS:" + wex.getRightOperand());
//            System.out.println();
//        } else if (stmt instanceof VariableDeclarationStatement) {
//            VariableDeclarationStatement var = (VariableDeclarationStatement) stmt;
//            System.out.println("Type of variable:" + var.getType());
//            System.out.println("Name of variable:" + var.fragments());
//            System.out.println();
//
//        } else if (stmt instanceof ReturnStatement) {
//            ReturnStatement rtstmt = (ReturnStatement) stmt;
//            System.out.println("return:" + rtstmt.getExpression());
//            System.out.println();
//        } else if (stmt instanceof TryStatement) {
//            parseTryStatement((TryStatement) stmt);
//        }
//    }
//
//    private void parseExpressionStatement(ExpressionStatement expressStmt) {
//        Expression express = expressStmt.getExpression();
//        if (express instanceof Assignment) {
//            Assignment assign = (Assignment) express;
//            System.out.println("LHS:" + assign.getLeftHandSide() + "; ");
//            System.out.println("Op:" + assign.getOperator() + "; ");
//            System.out.println("RHS:" + assign.getRightHandSide());
//        } else if (express instanceof MethodInvocation) {
//            MethodInvocation mi = (MethodInvocation) express;
//            IMethodBinding binding = mi.resolveMethodBinding();
//            if (binding != null) {
//                ITypeBinding it = binding.getDeclaringClass();
//                System.out.println("binding:" + it.getSearchName());
//            }
//            System.out.println("invocation name:" + mi.getSearchName());
//            System.out.println("invocation exp:" + mi.getExpression());
//            System.out.println("invocation arg:" + mi.arguments());
//        }
//    }
//
//    private void parseTryStatement(TryStatement tryStatement) {
//        Block body = tryStatement.getBody();
//        parseBlockStatement(body);
//
//        List<CatchClause> list = tryStatement.catchClauses();
//        for (CatchClause catchClause : list) {
//            parseBlockStatement(catchClause.getBody());
//        }
//
//        Block fina = tryStatement.getFinally();
//        if (fina != null) {
//            parseBlockStatement(fina);
//        }
//    }
//
//    private void parseBlockStatement(Block block) {
//        List statements = block.statements();
//        parseStatementList(statements);
//    }
//
//    private void parseStatementList(List statements) {
//        Iterator iter = statements.iterator();
//        while (iter.hasNext()) {
//            Statement stmt = (Statement) iter.next();
//            parseStatement(stmt);
//        }
//    }


//    private String readMethod(int startline, int lastline) {
//        String[] lines = fileContent.split("\n");
//        StringBuilder stringBuilder = new StringBuilder();
//        int first = startline - 1;
//        //get the first non space char position
//        int spaceNumber = lines[first].indexOf(lines[first].trim().charAt(0));
//        for (int i = startline - 1; i < lastline; i++) {
//            stringBuilder.append(lines[i].substring(spaceNumber) + "\n");
//        }
//        stringBuilder.append("}");
//        return stringBuilder.toString();
//    }


}
