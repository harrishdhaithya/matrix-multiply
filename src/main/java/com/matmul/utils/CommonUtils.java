package com.matmul.utils;

import org.json.JSONArray;

public class CommonUtils {
    public static int[][] stringTo2DArray(String str) {
        JSONArray jsonArray = new JSONArray(str);
        int[][] result = new int[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray inner = jsonArray.getJSONArray(i);
            result[i] = new int[inner.length()];
            for (int j = 0; j < inner.length(); j++) {
                result[i][j] = inner.getInt(j);
            }
        }
        return result;
    }
}
