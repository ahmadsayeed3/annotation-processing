package com.sid.learn.constants;

import java.util.HashMap;
import java.util.Map;

public class DataType {

    public static String dbToJava(int code){
        return javaDataType.get(code);
    }

    private static Map<Integer, String> javaDataType = new HashMap<Integer, String>() {{
        put(-5, "long");
        put(12, "String");
        put(93, "Date");
    }};

}
