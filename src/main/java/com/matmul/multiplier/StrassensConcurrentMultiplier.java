package com.matmul.multiplier;

import com.matmul.utils.CommonConstants;
import com.matmul.utils.CommonUtils;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class StrassensConcurrentMultiplier implements Multiplier {

    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        return multiply(a, b, Runtime.getRuntime().availableProcessors());
    }

    @Override
    public int[][] multiply(int[][] a, int[][] b, int threads) {
        ForkJoinPool pool = new ForkJoinPool(threads);
        try {
            return pool.invoke(new StrassenTask(a, b));
        } finally {
            pool.shutdown();
        }
    }

    private static class StrassenTask extends RecursiveTask<int[][]> {
        private final int[][] a, b;

        StrassenTask(int[][] a, int[][] b) {
            this.a = a;
            this.b = b;
        }

        @Override
        protected int[][] compute() {
            int n = a.length;

            if (n <= CommonUtils.STRASSEN_THRESHOLD)
                return CommonConstants.NAIVE.multiplier.multiply(a, b);

            if (n % 2 != 0) {
                int[][] paddedA = CommonUtils.matPad(a, n + 1);
                int[][] paddedB = CommonUtils.matPad(b, n + 1);
                return CommonUtils.matUnpad(new StrassenTask(paddedA, paddedB).compute(), n);
            }

            int half = n / 2;

            int[][] A11 = CommonUtils.matSlice(a, 0, 0, half);
            int[][] A12 = CommonUtils.matSlice(a, 0, half, half);
            int[][] A21 = CommonUtils.matSlice(a, half, 0, half);
            int[][] A22 = CommonUtils.matSlice(a, half, half, half);

            int[][] B11 = CommonUtils.matSlice(b, 0, 0, half);
            int[][] B12 = CommonUtils.matSlice(b, 0, half, half);
            int[][] B21 = CommonUtils.matSlice(b, half, 0, half);
            int[][] B22 = CommonUtils.matSlice(b, half, half, half);

            StrassenTask t1 = new StrassenTask(CommonUtils.matAdd(A11, A22), CommonUtils.matAdd(B11, B22));
            StrassenTask t2 = new StrassenTask(CommonUtils.matAdd(A21, A22), B11);
            StrassenTask t3 = new StrassenTask(A11, CommonUtils.matSub(B12, B22));
            StrassenTask t4 = new StrassenTask(A22, CommonUtils.matSub(B21, B11));
            StrassenTask t5 = new StrassenTask(CommonUtils.matAdd(A11, A12), B22);
            StrassenTask t6 = new StrassenTask(CommonUtils.matSub(A21, A11), CommonUtils.matAdd(B11, B12));
            StrassenTask t7 = new StrassenTask(CommonUtils.matSub(A12, A22), CommonUtils.matAdd(B21, B22));

            t1.fork(); t2.fork(); t3.fork();
            t4.fork(); t5.fork(); t6.fork();

            int[][] M7 = t7.compute();
            int[][] M6 = t6.join();
            int[][] M5 = t5.join();
            int[][] M4 = t4.join();
            int[][] M3 = t3.join();
            int[][] M2 = t2.join();
            int[][] M1 = t1.join();

            int[][] t1r = CommonUtils.matAdd(M1, M4);
            int[][] t2r = CommonUtils.matSub(t1r, M5);
            int[][] C11 = CommonUtils.matAdd(t2r, M7);
            int[][] C12  = CommonUtils.matAdd(M3, M5);
            int[][] C21  = CommonUtils.matAdd(M2, M4);
            int[][] t3r = CommonUtils.matAdd(M1, M3);
            int[][] t4r = CommonUtils.matSub(t3r, M2);
            int[][] C22 = CommonUtils.matAdd(t4r, M6);

            return CommonUtils.matCombine(C11, C12, C21, C22, half);
        }
    }
}
