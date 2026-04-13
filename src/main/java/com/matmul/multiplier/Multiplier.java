package com.matmul.multiplier;

public interface Multiplier {
    int[][] multiply(int[][] a, int[][] b);

    default int[][] multiply(int[][] a, int[][] b, int threads) {
        return multiply(a, b);
    }
}
