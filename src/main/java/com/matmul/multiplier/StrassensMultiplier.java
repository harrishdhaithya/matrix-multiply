package com.matmul.multiplier;

import com.matmul.utils.CommonUtils;

public class StrassensMultiplier implements Multiplier {

    private static final NaiveMultiplier NAIVE = new NaiveMultiplier();

    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        int n = a.length;

        if (n <= CommonUtils.STRASSEN_THRESHOLD)
            return NAIVE.multiply(a, b);

        if (n % 2 != 0) {
            int[][] paddedA = CommonUtils.matPad(a, n + 1);
            int[][] paddedB = CommonUtils.matPad(b, n + 1);
            return CommonUtils.matUnpad(multiply(paddedA, paddedB), n);
        }

        int half = n / 2;

        int[][] A11 = CommonUtils.matSlice(a, 0,    0,    half);
        int[][] A12 = CommonUtils.matSlice(a, 0,    half, half);
        int[][] A21 = CommonUtils.matSlice(a, half, 0,    half);
        int[][] A22 = CommonUtils.matSlice(a, half, half, half);

        int[][] B11 = CommonUtils.matSlice(b, 0,    0,    half);
        int[][] B12 = CommonUtils.matSlice(b, 0,    half, half);
        int[][] B21 = CommonUtils.matSlice(b, half, 0,    half);
        int[][] B22 = CommonUtils.matSlice(b, half, half, half);

        int[][] M1 = multiply(CommonUtils.matAdd(A11, A22), CommonUtils.matAdd(B11, B22));
        int[][] M2 = multiply(CommonUtils.matAdd(A21, A22), B11);
        int[][] M3 = multiply(A11,                          CommonUtils.matSub(B12, B22));
        int[][] M4 = multiply(A22,                          CommonUtils.matSub(B21, B11));
        int[][] M5 = multiply(CommonUtils.matAdd(A11, A12), B22);
        int[][] M6 = multiply(CommonUtils.matSub(A21, A11), CommonUtils.matAdd(B11, B12));
        int[][] M7 = multiply(CommonUtils.matSub(A12, A22), CommonUtils.matAdd(B21, B22));

        int[][] t1  = CommonUtils.matAdd(M1, M4);
        int[][] t2  = CommonUtils.matSub(t1, M5);
        int[][] C11 = CommonUtils.matAdd(t2, M7);

        int[][] C12 = CommonUtils.matAdd(M3, M5);
        int[][] C21 = CommonUtils.matAdd(M2, M4);

        int[][] t3  = CommonUtils.matAdd(M1, M3);
        int[][] t4  = CommonUtils.matSub(t3, M2);
        int[][] C22 = CommonUtils.matAdd(t4, M6);

        return CommonUtils.matCombine(C11, C12, C21, C22, half);
    }
}
