package com.system.utils;

public class UrlUtils {


    public static String getSchema(String url){
        boolean contains = url.contains("?");
        if (contains) {
            String[] split = url.split("\\?");
            String[] split1 = split[0].split("\\/");
            String s = split1[split1.length - 1];
            return s;
        }else {
            String[] split = url.split("\\/");
            String s = split[split.length - 1];
            return s ;
        }
    }

}
