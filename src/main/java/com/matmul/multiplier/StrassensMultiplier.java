package com.matmul.multiplier;

public class StrassensMultiplier implements AbstractMultiplier {

    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        int n = a.length;

        if (n == 1) {
            return new int[][]{{a[0][0] * b[0][0]}};
        }

        if (n % 2 != 0) {
            //Add additinal padding if not a square matrix and if the size is not a power of 2
            a = pad(a, n + 1);
            b = pad(b, n + 1);
            int[][] result = multiply(a, b);
            return unpad(result, n);
        }

        int half = n / 2;

        int[][] A11 = constructSubArray(a, 0, 0, half);
        int[][] A12 = constructSubArray(a, 0, half, half);
        int[][] A21 = constructSubArray(a, half, 0, half);
        int[][] A22 = constructSubArray(a, half, half, half);

        int[][] B11 = constructSubArray(b, 0, 0, half);
        int[][] B12 = constructSubArray(b, 0, half, half);
        int[][] B21 = constructSubArray(b, half, 0, half);
        int[][] B22 = constructSubArray(b, half, half, half);

        int[][] M1 = multiply(add(A11, A22), add(B11, B22));
        int[][] M2 = multiply(add(A21, A22), B11);
        int[][] M3 = multiply(A11, subtract(B12, B22));
        int[][] M4 = multiply(A22, subtract(B21, B11));
        int[][] M5 = multiply(add(A11, A12), B22);
        int[][] M6 = multiply(subtract(A21, A11), add(B11, B12));
        int[][] M7 = multiply(subtract(A12, A22), add(B21, B22));

        int[][] C11 = add(subtract(add(M1, M4), M5), M7);
        int[][] C12 = add(M3, M5);
        int[][] C21 = add(M2, M4);
        int[][] C22 = add(subtract(add(M1, M3), M2), M6);

        return combine(C11, C12, C21, C22, half);
    }

    private int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    private int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    private int[][] constructSubArray(int[][] M, int rowStart, int colStart, int size) {
        int[][] S = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                S[i][j] = M[rowStart + i][colStart + j];
        return S;
    }

    private int[][] combine(int[][] C11, int[][] C12, int[][] C21, int[][] C22, int half) {
        int n = half * 2;
        int[][] C = new int[n][n];
        for (int i = 0; i < half; i++)
            for (int j = 0; j < half; j++) {
                C[i][j] = C11[i][j];
                C[i][j + half] = C12[i][j];
                C[i + half][j] = C21[i][j];
                C[i + half][j + half] = C22[i][j];
            }
        return C;
    }

    private int[][] pad(int[][] M, int newSize) {
        int[][] P = new int[newSize][newSize];
        for (int i = 0; i < M.length; i++)
            System.arraycopy(M[i], 0, P[i], 0, M[i].length);
        return P;
    }

    private int[][] unpad(int[][] M, int originalSize) {
        int[][] U = new int[originalSize][originalSize];
        for (int i = 0; i < originalSize; i++)
            System.arraycopy(M[i], 0, U[i], 0, originalSize);
        return U;
    }
}