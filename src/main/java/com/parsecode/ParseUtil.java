package com.parsecode;

import org.eclipse.jdt.core.dom.*;

import java.io.*;

/**
 * Created by flyboss on 2018/5/12.
 */
public class ParseUtil {
    public static boolean skipParseFile(TypeDeclaration typeDec) {
        ITypeBinding iTypeBinding = typeDec.resolveBinding();
        if (iTypeBinding==null){
            return true;
        }
        boolean canSkip = false;
        canSkip |= Modifier.isAbstract(iTypeBinding.getModifiers());
        canSkip |= Modifier.isPrivate(iTypeBinding.getModifiers());
        canSkip |= typeDec.isInterface();
        return canSkip;
    }

    public static CompilationUnit getAst(String filePath,String fileContent) {
        ASTParser parser = ASTParser.newParser(AST.JLS9);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);//to parse compilation unit
        parser.setEnvironment(null, null, null, true);
        parser.setUnitName(filePath.substring(filePath.lastIndexOf('\\'), filePath.length()));//需要与代码文件的名称一致
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setSource(fileContent.toCharArray());
        parser.setResolveBindings(true);
        ASTNode astNode=null;
        try {
            astNode=parser.createAST(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (astNode!=null){
            return (CompilationUnit)astNode;
        }else{
            return null;
        }
    }

    public static String readToString(String filePath) {
        String encoding = "UTF-8";
        File file = new File(filePath);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (Exception e){
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
}
