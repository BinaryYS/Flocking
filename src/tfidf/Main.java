package tfidf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
    	
    	String filePath = "E:/dar/旅游类测试文档/B_城市概况";
     /*   Map<String, HashMap<String, Integer>> normal = ReadFiles.NormalTFOfAll(filePath);
        for (String filename : normal.keySet()) {
            System.out.println("fileName " + filename);
            System.out.println("TF " + normal.get(filename).toString());
        }

        System.out.println("-----------------------------------------");

        Map<String, HashMap<String, Float>> notNarmal = ReadFiles.tfOfAll(filePath);
        for (String filename : notNarmal.keySet()) {
            System.out.println("fileName " + filename);
            System.out.println("TF " + notNarmal.get(filename).toString());
        }
*/
  /*      System.out.println("-----------------------------------------");

       Map<String, Float> idf = ReadFiles.idf(filePath);
        for (String word : idf.keySet()) {
            System.out.println("keyword :" + word + " idf: " + idf.get(word));
        }

        System.out.println("-----------------------------------------");

        Map<String, HashMap<String, Float>> tfidf = ReadFiles.tfidf(filePath);
        for (String filename : tfidf.keySet()) {
            System.out.println("fileName " + filename);
            System.out.println(tfidf.get(filename));
        }*/
        System.out.println("-----------------------------------------");
        List<String> fileList = ReadFiles.readDirs(filePath);
    	System.out.println(fileList.size());
    	
         for (int i = 0; i < fileList.size(); i++) {
        	for (int j = i+1; j < fileList.size(); j++) {
        		System.out.println(i+"和"+j+"的相似度为");
        		System.out.println(CosineSimilarAlgorithm.getSimilarity(ReadFiles.readFiles(fileList.get(i)),ReadFiles.readFiles(fileList.get(j)))+"\n");
        	}
        } 
    }
}