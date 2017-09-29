package com.munatayev.timur.ibm.ebankingdemov3.Utile;

public class Filter {

    public static String groszFilter(String line){
        String result = "";
        for(String word : line.split(" ")) {
            if(word.equals("zł")) {
                result += " dollars ";
            } else if(word.equals("gr")){
                result += " cents ";
            }else {
                result += word+" ";
            }
        }
        return result;
    }
}
