package com.dt.utils.config;

/**
 * @Author jiangyao
 * @Date 2020/5/11 17:32
 **/
public class T {
    public static  T t = null;

    private T(){

    }

    public static T getInsta(){
        if (t==null){
            t = new T();
        }
        return t;
    }
}

