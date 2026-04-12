package com.matmul.multiplier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NaiveConcurrentMultiplier implements Multiplier {
    @Override
    public int[][] multiply(int[][] a, int[][] b) {
        int[][] result = new int[a.length][b[0].length];
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<int[]>> futures = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {
            final int row = i;
            futures.add(executor.submit(() -> {
                int[] rowResult = new int[b[0].length];
                for (int j = 0; j < b[0].length; j++) {
                    int sum = 0;
                    for (int k = 0; k < a[0].length; k++)
                        sum += a[row][k] * b[k][j];
                    rowResult[j] = sum;
                }
                return rowResult;
            }));
        }

        executor.shutdown();

        try {
            for (int i = 0; i < futures.size(); i++)
                result[i] = futures.get(i).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Matrix multiplication interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Matrix multiplication failed", e);
        }

        return result;
    }
}
