package com.matmul.multiplier;

import java.util.Arrays;

public class NaiveMultiplier implements AbstractMultiplier {
    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        int[][] result = new int[a.length][b[0].length];
        int n = a.length;
        for (int i = 0; i < n; i++) {
            Arrays.fill(result[i], 0);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
}
