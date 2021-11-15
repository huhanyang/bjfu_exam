package com.bjfu.exam.util;

import java.util.Random;

public class RandomCodeUtil {
    public static String nextCodeWithCharAndNumber(){
        String sources = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuilder code = new StringBuilder();
        for (int j = 0; j < 6; j++) {
            code.append(sources.charAt(rand.nextInt(sources.length())));
        }
        return code.toString();
    }
}
