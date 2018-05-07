package com.parsecode;

import com.dao.CodeDao;
import com.dao.FuncIndexCodeDao;
import com.dao.FuncIndexDao;
import com.entity.Code;
import com.entity.FuncIndex;
import com.entity.FuncIndexCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flyboss on 2018/4/25.
 */
public class ExtractIndex {
    private FuncIndexDao funcIndexDao;
    private FuncIndexCodeDao funcIndexCodeDao;


    public ExtractIndex() {
        funcIndexDao = new FuncIndexDao();
        funcIndexCodeDao = new FuncIndexCodeDao();
    }

    //TODO 这部分代码有以下问题  1.idf 有大量空值   2.
    public static void main(String[] args) {
        ExtractIndex extractIndex = new ExtractIndex();
        CodeDao codeDao=new CodeDao();
        long codeCount=codeDao.getCodeCount();
        //extractIndex.setNameIdfOrBodyIdf(true,codeCount);
        extractIndex.setNameIdfOrBodyIdf(false,codeCount);
        System.out.println("finish");
    }

    public void setNameIdfOrBodyIdf(boolean isName, long codeCount) {
        List<Object[]> objects=funcIndexCodeDao.countIDF(isName);
        for (Object[] object : objects) {
            int id = Integer.parseInt(object[0].toString());
            int count = Integer.parseInt(object[1].toString());
            FuncIndex funcIndex = funcIndexDao.find(id);
            if (isName){
                funcIndex.setNameIdf(Math.log10(codeCount/count));
            }else{
                funcIndex.setBodyIdf(Math.log10(codeCount/count));
            }
            funcIndexDao.update(funcIndex);
        }
        FuncIndexDao funcIndexDao=new FuncIndexDao();
        double maxIdf=funcIndexDao.findMaxIdf(isName);
        List<FuncIndex> funcIndices=funcIndexDao.getAll();
        for (FuncIndex funcIndex:funcIndices) {
            //TODO 这里不应该有空值才对
            if (isName){
                if (funcIndex.getNameIdf()==null){
                    funcIndex.setNameIdf(0/maxIdf);
                }else{
                    funcIndex.setNameIdf(funcIndex.getNameIdf()/maxIdf);
                }
            }else{
                if (funcIndex.getBodyIdf()==null){
                    funcIndex.setBodyIdf(0/maxIdf);
                }else{
                    funcIndex.setBodyIdf(funcIndex.getBodyIdf()/maxIdf);
                }
            }
            funcIndexDao.update(funcIndex);
        }
    }

    public void parseFunc(Code code) {
        buildIndex(code, code.getSearchName(), true);
        buildIndex(code, code.getSearchBody(), false);
    }

    private void buildIndex(Code code, String string, Boolean isName) {
        String[] strings = string.split(" ");
        Map<FuncIndex, Integer> tf = new HashMap<FuncIndex, Integer>();
        double maxTf = 1;
        for (String s : strings) {
            s=s.trim();
            if (s==null||s.length()==0){
                continue;
            }else if (s.length()==1&&Character.isWhitespace(s.charAt(0))){
                continue;
            }
            FuncIndex funcIndex = funcIndexDao.find(s);
            if (funcIndex == null) {
                funcIndex = funcIndexDao.add(new FuncIndex(s));
            }
            if (tf.containsKey(funcIndex)) {
                int tfTemp = tf.get(funcIndex) + 1;
                tf.put(funcIndex, tfTemp);
                maxTf = maxTf > tfTemp ? maxTf : tfTemp;
            } else {
                tf.put(funcIndex, 1);
            }
        }
        for (Map.Entry<FuncIndex, Integer> entry : tf.entrySet()) {
            FuncIndex funcIndex = entry.getKey();
            FuncIndexCode funcIndexCode = new FuncIndexCode(funcIndex, code, isName, entry.getValue() / maxTf);
            funcIndexCodeDao.add(funcIndexCode);
        }
    }


}
