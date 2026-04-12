package com.matmul.multiplier;

public class NaiveMultiplier implements Multiplier {
    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        int n = a.length;
        int[][] result = new int[n][b[0].length];
        for (int i = 0; i < n; i++)
            for (int k = 0; k < n; k++)
                for (int j = 0; j < b[0].length; j++)
                    result[i][j] += a[i][k] * b[k][j];
        return result;
    }
}
