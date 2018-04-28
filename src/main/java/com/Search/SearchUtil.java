package com.Search;

import java.util.*;

/**
 * Created by flyboss on 2018/4/23.
 */
public class SearchUtil {
    private static final int apiMaxNum=20;
    public static double countVectorDistance(double[] vector1,double[] vector2){
        double answer=0;
        for(int i=0;i<vector1.length;i++){
            answer+=vector1[i]*vector2[i];
        }
        return answer;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Comparator<Map.Entry<K,V>> valueComparator = new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        };
        List<Map.Entry<K,V>> list = new ArrayList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(list,valueComparator);

        Map<K, V> result = new HashMap<K, V>();
        int apiNum=0;
        for (Map.Entry<K,V> entry:list) {
            apiNum++;
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
