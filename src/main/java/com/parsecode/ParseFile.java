package com.parsecode;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.List;

/**
 * Created by flyboss on 2018/5/12.
 */
public class ParseFile implements Runnable {
    private String baseUrl;
    private String filePath;

    public ParseFile(String filepath,String bastUrl){
        this.filePath = filepath;
        this.baseUrl=bastUrl;
    }

    @Override
    public void run() {
        String fileContent=ParseUtil.readToString(filePath);
        CompilationUnit compilationUnit = ParseUtil.getAst(filePath,fileContent);
        if (compilationUnit==null){
            return;
        }
        List types = compilationUnit.types();
        if (types.size() == 0) {
            return;
        }
        TypeDeclaration typeDec = (TypeDeclaration) types.get(0);
        if (ParseUtil.skipParseFile(typeDec)) {
            return;
        }
        String packageName=null;
        try{
            packageName = compilationUnit.getPackage().getName().toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        String className = typeDec.getName().getFullyQualifiedName();

        CodeVisitor codeVisitor = new CodeVisitor();
        codeVisitor.setCodeVisitor(packageName, className, filePath, compilationUnit, fileContent,baseUrl);
        compilationUnit.accept(codeVisitor);
    }
}
