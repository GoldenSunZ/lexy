package cn.lexy.auth.tests;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Created by john on 16/8/7.
 */
public class ArrayGuess {

    public static void main(String[] args) {
        int[] a = {1,2};
        String[] b  = {"12","34"};
        Object d = a;
        if(d.getClass().isArray()){
            System.out.println(Array.getLength(d));
        }
    }
}
