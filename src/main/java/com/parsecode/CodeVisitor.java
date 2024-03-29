package com.parsecode;

import com.dao.CodeDao;
import com.dao.DocDao;
import com.entity.Code;
import com.entity.Doc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by flyboss on 2018/4/9.
 */
public class CodeVisitor extends ASTVisitor {
    private static Logger logger = LogManager.getLogger(CodeVisitor.class);

    private Set<Doc> docs = new HashSet<Doc>();
    private MethodDeclaration methodDeclaration = null;


    private String packageName;
    private String className;
    private String filePath;
    private CompilationUnit compilationUnit;
    private String[] fileContent;
    private String baseUrl;
    private static ExtractIndex extractIndex = new ExtractIndex();

    public void setCodeVisitor(String packageName, String className, String filePath, CompilationUnit compilationUnit, String fileContent, String baseUrl) {
        this.packageName = packageName;
        this.className = className;
        this.filePath = filePath;
        this.compilationUnit = compilationUnit;
        this.fileContent = fileContent.split("\n");
        this.baseUrl = baseUrl;
        System.out.println("\n" + filePath + "----------------------------------------------");
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        SimpleName methodName = node.getName();
        System.out.println("---------------begin--------------");
        System.out.println("method name:" + methodName);
        methodDeclaration = node;
        docs.clear();
        return true;
    }


    @Override
    public void endVisit(MethodDeclaration node) {
        generateCode();
        System.out.println("---------------end----------------");
        methodDeclaration = null;
        docs.clear();
    }

    private void generateCode() {
        if (methodDeclaration == null) {
            return;
        }
        String methodName;
        int starline, endline;
        String originBody;
        String url;
        String doc;
        try {
            methodName = methodDeclaration.getName().toString();
            //doc of the method
            doc = getMethodDoc(methodDeclaration);
            int[] length = getMethodLength(methodDeclaration);
            starline = length[0];
            endline = length[1];
            originBody = getContent(starline, endline);
            if (doc.length() + originBody.length() > 1000) {
                return;
            }
            String temp = filePath.replaceFirst("F:\\\\codewarehouse\\\\", "");
            temp = temp.substring(temp.indexOf("\\") + 1);
            url = baseUrl + temp.replaceAll("\\\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            return;
        }
        Code code = new Code(packageName, className, methodName, starline, endline, originBody, filePath, doc, docs, url);
        CodeDao codeDao = new CodeDao();
        codeDao.add(code);

        extractIndex.parseFunc(code);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        try {
            if (methodDeclaration == null) {
                return true;
            }
            List<Expression> expressions = node.arguments();
            String arguments = getArguments(expressions);
            String functionName = null;
            Expression expression = node.getExpression();
            if (expression != null) {
                ITypeBinding typeBinding = expression.resolveTypeBinding();
                if (typeBinding != null) {
                    functionName = typeBinding.getPackage().getName() + "." + typeBinding.getName() + "." + node.getName();
                    System.out.println(functionName);
                }
            }
            if (functionName != null) {
                serachDoc(functionName, arguments.toString());
            }
        } catch (Exception e) {
            logger.error(filePath + " MethodInvocation " +node.toString()+" "+e);
            return false;
        }
        return true;
    }

    private String getArguments(List<Expression> expressions) {
        StringBuilder arguments = new StringBuilder();
        for (Expression expression : expressions) {
            ITypeBinding expressionBinding = expression.resolveTypeBinding();
            if (expressionBinding != null) {
                String name = expressionBinding.getName();
                if (name != null) {
                    arguments.append(name + ", ");
                }
            }
        }
        if (arguments.length() != 0) {
            return arguments.substring(0, arguments.length() - 2);
        } else {
            return new String("");
        }
    }

    @Override
    public void endVisit(ClassInstanceCreation node) {
        try {
            if (methodDeclaration == null) {
                return;
            }
            List<Expression> expressions = node.arguments();
            String arguments = getArguments(expressions);
            String className = null;
            ITypeBinding typeBinding = node.resolveTypeBinding();
            if (typeBinding != null) {
                className = typeBinding.getPackage().getName() + "." + typeBinding.getName() + "." + node.getType().toString();
                System.out.println(className);
            }
            if (className != null) {
                serachDoc(className, arguments.toString());
            }
        }catch (Exception e){
            logger.error(filePath+" ClassInstanceCreation:\t" + node.toString()+" "+e);
        }

    }

    @Override
    public boolean visit(AnonymousClassDeclaration classDeclarationStatement) {
        return false;
    }

    private String getMethodDoc(MethodDeclaration node) {
        Javadoc javadoc = node.getJavadoc();
        if (javadoc != null) {
            return javadoc.toString();
        } else {
            return "";
        }

    }

    private int[] getMethodLength(MethodDeclaration node) {
        Block body = node.getBody();
        int startline = compilationUnit.getLineNumber(body.getStartPosition());
        int nodeLength = body.getLength();
        int endline = compilationUnit.getLineNumber(body.getStartPosition() + nodeLength);
        System.out.println(startline + "  " + endline);
        return new int[]{startline, endline};
    }

    private String getContent(int startline, int endline) {
        int firstline = startline - 1;
        //get the first non space char position
        int spaceNumber = fileContent[firstline].indexOf(fileContent[firstline].trim().charAt(0));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = startline - 1; i < endline; i++) {
            if (fileContent[i].length() <= spaceNumber) {
                stringBuilder.append(fileContent[i] + "\n");
            } else {
                stringBuilder.append(fileContent[i].substring(spaceNumber) + "\n");
            }
        }
        return stringBuilder.toString();
    }

    private void serachDoc(String fullname, String arguments) {
        DocDao docDao = new DocDao();
        Doc doc = docDao.findByFullname(fullname, arguments);
        if (doc != null) {
            docs.add(doc);
        }
    }

}
