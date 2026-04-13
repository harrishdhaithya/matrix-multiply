package com.matmul.utils;

public class CommonUtils {

    public static final int STRASSEN_THRESHOLD = 64;

    public static int[][] matAdd(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    public static int[][] matSub(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    public static int[][] matSlice(int[][] M, int rowStart, int colStart, int size) {
        int[][] S = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                S[i][j] = M[rowStart + i][colStart + j];
        return S;
    }

    public static int[][] matCombine(int[][] C11, int[][] C12, int[][] C21, int[][] C22, int half) {
        int n = half * 2;
        int[][] C = new int[n][n];
        for (int i = 0; i < half; i++)
            for (int j = 0; j < half; j++) {
                C[i][j]= C11[i][j];
                C[i][j + half]= C12[i][j];
                C[i + half][j]= C21[i][j];
                C[i + half][j + half] = C22[i][j];
            }
        return C;
    }

    public static int[][] matPad(int[][] M, int newSize) {
        int[][] P = new int[newSize][newSize];
        for (int i = 0; i < M.length; i++)
            System.arraycopy(M[i], 0, P[i], 0, M[i].length);
        return P;
    }

    public static int[][] matUnpad(int[][] M, int originalSize) {
        int[][] U = new int[originalSize][originalSize];
        for (int i = 0; i < originalSize; i++)
            System.arraycopy(M[i], 0, U[i], 0, originalSize);
        return U;
    }
}