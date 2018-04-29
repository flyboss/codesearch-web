package com.Search;

import com.dao.CodeDao;
import com.dao.DocDao;
import com.dao.FuncIndexCodeDao;
import com.dao.FuncIndexDao;
import com.entity.Code;
import com.entity.Doc;
import com.entity.FuncIndex;
import com.entity.FuncIndexCode;
import com.util.StringUtil;

import java.util.*;

/**
 * Created by flyboss on 2018/4/21.
 */
public class Search {


    public static void main(String[] args) {
        Search search = new Search();
        search.run();
    }

    public String helloWorld(){
        return "Hello web";
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        String searchSentence = scanner.nextLine();
        String searchWords = disposeSearchWord(searchSentence);

        ApiNameSearch apiNameSearch = new ApiNameSearch();
        Map<Integer,Double> apiName = apiNameSearch.vsm(searchWords);

        ApiCommentSearch apiCommentSearch = new ApiCommentSearch();
        Map<Integer,Double> apiText = apiCommentSearch.vsm(searchWords);

        Map<Integer, Double> apiRevevant = getApiRelevant(apiName, apiText);
        Queue<FuncValue> queue=beginBooleanModel(apiRevevant,searchWords);
        int i=0;
    }

    private String disposeSearchWord(String seachWord){
        String s = StringUtil.replaceAllPunctuation(seachWord.toLowerCase());
        s = StringUtil.trimCamel(s);
        s = StringUtil.stemming(s);
        s = StringUtil.deleteStopWord(s);
        s = StringUtil.deleteKeyword(s);
        return s;
    }

    private Map<Integer,Double> getApiRelevant(Map<Integer,Double> apiName,Map<Integer,Double> apiText){
        Map<Integer, Double> apiRelevant = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry :apiName.entrySet()) {
            int docId=entry.getKey();
            if(apiText.containsKey(docId)){
                apiRelevant.put(docId, apiName.get(docId) + apiText.get(docId));
            }else {
                apiRelevant.put(docId, apiName.get(docId));
            }
        }

        //TODO 论文这里没看懂
        for (Map.Entry<Integer,Double> entry: apiText.entrySet()) {
            if(!apiRelevant.containsKey(entry.getKey())){

            }
        }
        return SearchUtil.sortByValue(apiRelevant);
    }

    private Queue<FuncValue> beginBooleanModel(Map<Integer, Double> apiRevevant,String searchWords){
        DocDao docDao = new DocDao();
        FuncIndexDao funcIndexDao = new FuncIndexDao();
        FuncIndexCodeDao funcIndexCodeDao = new FuncIndexCodeDao();
        Queue<FuncValue> funcValues = getQueue();
        for (Map.Entry<Integer,Double> entry: apiRevevant.entrySet()) {
            Doc doc = docDao.find(entry.getKey());
            String[] funcSearchWords=deleteApiWords(doc.getSearchName().split(" "),searchWords.split(" "));
            List<Code> codes = docDao.findCodeByDocId(doc.getId());
            //目前先找出调用了这个API的函数有哪些，在这些函数中分别遍历，找那些需要搜索的单词，计算sim（or）并放回结果集中
            for (Code code: codes) {
                List<Double> simOrValues = new ArrayList<Double>();
                for (String word: funcSearchWords) {
                    FuncIndex funcIndex = funcIndexDao.find(word);
                    double wtdName=0;
                    double wtdBody=0;
                    if(code.getSearchName().contains(word)){
                        FuncIndexCode funcIndexCode = funcIndexCodeDao.findByCodeAndFuncIndex(code.getId(), funcIndex.getId(),true);
                        wtdName=0.5+0.5*funcIndexCode.getTermFrequency()*funcIndex.getNameIdf();
                    }
                    if (code.getSearchBody().contains(word)){
                        FuncIndexCode funcIndexCode = funcIndexCodeDao.findByCodeAndFuncIndex(code.getId(), funcIndex.getId(),false);
                        wtdBody=0.5+0.5*funcIndexCode.getTermFrequency()*funcIndex.getBodyIdf();
                    }
                    double simOrValue=simOr(wtdName,wtdBody,1.5,1.0);
                    simOrValues.add(simOrValue);
                }
                double funcValue=funcFinalScore(entry.getValue(),simOrValues);
                funcValues.offer(new FuncValue(code, funcValue));
            }
        }
        return funcValues;
    }

    private String[] deleteApiWords(String[] docSearchname,String[] searchWords){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <searchWords.length ; i++) {
            boolean find=false;
            for (int j = 0; j <docSearchname.length ; j++) {
                if(docSearchname[j].equals(searchWords[i])){
                    find=true;
                }
            }
            if (find==false){
                stringBuilder.append(searchWords[i]+" ");
            }
        }
        return stringBuilder.toString().split(" ");
    }

    private double simOr(double wtd1,double wtd2,double wtq1,double wtq2){
        return (wtq1*wtq1*wtd1*wtd1+wtq2*wtq2+wtd2*wtd2)/(wtq1*wtq1+wtq2*wtq2);
    }

    private double simAnd(double w1, double w2) {
        return 1-Math.sqrt(((1-w1)*(1-w1)+(1-w2)*(1-w2))/2);
    }

    private double funcFinalScore(double apiScore,List<Double> simOrValues){
        double temp=0;
        if (simOrValues.size()>0){
            temp=simOrValues.get(0);
            for (int i = 1; i < simOrValues.size(); i++) {
                temp=simAnd(temp,simOrValues.get(i));
            }
            return simAnd(apiScore, temp);
        }else{
            return simAnd(apiScore, 0);
        }
    }


    private Queue<FuncValue> getQueue(){
        Comparator<FuncValue> comparator=new Comparator<FuncValue>() {
            public int compare(FuncValue o1, FuncValue o2) {
                if (o2.value>o1.value){
                    return 1;
                }else if (o2.value<o1.value){
                    return -1;
                }else{
                    return 0;
                }
            }
        };
        return new PriorityQueue<FuncValue>(20, comparator);
    }

    private class FuncValue{
        public Code code;
        public double value;

        public FuncValue(Code code, double value) {
            this.code = code;
            this.value = value;
        }
    }
}
