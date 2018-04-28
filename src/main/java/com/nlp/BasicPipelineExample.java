package com.nlp;


import edu.stanford.nlp.simple.*;
/**
 * Created by flyboss on 2018/4/15.
 */
public class BasicPipelineExample {

    public static void main(String[] args) {
        String text=new String("Joe Smith was born in California. \" +\n" +
                "      \"In 2017, he went to Paris, France in the summer. \" +\n" +
                "      \"His flight left at 3:00pm on July 10th, 2017. \" +\n" +
                "      \"After eating some escargot for the first time, Joe said, \\\"That was delicious!\\\" \" +\n" +
                "      \"He sent a postcard to his sister Jane Smith. \" +\n" +
                "      \"After hearing about Joe's trip, Jane decided she might go to France one day.");
        BasicPipelineExample basicPipelineExample=new BasicPipelineExample();
        basicPipelineExample.dispose(text);
    }

    public void dispose(String text){
        Document doc = new Document(text);
        for (Sentence sent : doc.sentences()) {
            for(int i = 0; i < sent.words().size(); i++) {
                if (sent.posTag(i) != null ) {
                    if(sent.posTag(i).contains("NN")||sent.posTag(i).contains("JJ")||sent.posTag(i).contains("VB")){
                        System.out.print(sent.lemma(i)+"  ");
                        System.out.println(sent.word(i));
                    }

                }
            }
        }
    }
}
