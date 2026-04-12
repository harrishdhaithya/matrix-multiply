package com.matmul.multiplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NaiveConcurrentMultiplier implements AbstractMultiplier {
    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        int[][] result = new int[a.length][b[0].length];
        int threads = Runtime.getRuntime().availableProcessors();
        try
        {
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            List<Future<int[]>> futures = new ArrayList<>();
            for (int i = 0; i < a.length; i++) {
                final int row = i;
                Future<int[]> future = executor.submit(() -> {
                    int[] rowResult = new int[b[0].length];
                    for (int j = 0; j < b[0].length; j++) {
                        int sum = 0;
                        for (int k = 0; k < a[0].length; k++) {
                            sum += a[row][k] * b[k][j];
                        }
                        rowResult[j] = sum;
                    }
                    return rowResult;
                });
                futures.add(future);
            }
            for (Future<int[]> future : futures) {
                future.get();
            }
            executor.shutdown();
            executor.close();
        }
        catch (InterruptedException | ExecutionException e)
        {
            System.out.println("Exception in NaiveConcurrentMultiplier");
        }

        return result;
    }
}
