package com.parsecode;

import com.dao.FuncIndexCodeDao;
import com.dao.FuncIndexDao;
import com.entity.Code;
import com.entity.FuncIndex;
import com.entity.FuncIndexCode;

import java.util.HashMap;
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

    public void updateNameIdfAndBodyIdf() {

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
            if (s==null||s.length()==0){
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
