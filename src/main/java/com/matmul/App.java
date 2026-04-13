package com.matmul;

import com.matmul.utils.CommonConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class App {

    private static final int[] SIZES   = {256, 512, 768, 1024, 1280, 1536, 1792, 2048, 2304, 2560};
    private static final int[] THREADS = {2, 4, 8, 10};
    private static final CommonConstants[] MULTIPLIERS = CommonConstants.values();

    public static void main(String[] args) throws IOException {
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors() + "\n");

        // times[threadIdx][sizeIdx][mulIdx]
        long[][][] times = new long[THREADS.length][SIZES.length][MULTIPLIERS.length];

        for (int t = 0; t < THREADS.length; t++) {
            int threads = THREADS[t];
            System.out.println("=== Threads: " + threads + " ===");

            for (int s = 0; s < SIZES.length; s++) {
                int n = SIZES[s];
                int[][] a = randomMatrix(n, 42);
                int[][] b = randomMatrix(n, 137);

                System.out.printf("--- %dx%d ---%n", n, n);

                for (int m = 0; m < MULTIPLIERS.length; m++) {
                    long start = System.currentTimeMillis();
                    MULTIPLIERS[m].multiplier.multiply(a, b, threads);
                    times[t][s][m] = System.currentTimeMillis() - start;

                    System.out.printf("  %-20s %6dms%n", MULTIPLIERS[m].label, times[t][s][m]);
                }
            }
            System.out.println();
        }

        writeReport(times);
        System.out.println("Report written to report.csv");
    }

    private static int[][] randomMatrix(int n, long seed) {
        Random rand = new Random(seed);
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                m[i][j] = rand.nextInt(10);
        return m;
    }

    private static void writeReport(long[][][] times) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter("report.csv"))) {
            // header
            StringBuilder header = new StringBuilder("Threads,Size");
            for (CommonConstants c : MULTIPLIERS) header.append(",").append(c.label).append("(ms)");
            for (int m = 0; m < MULTIPLIERS.length; m++)
                if (MULTIPLIERS[m].isConcurrent)
                    header.append(",").append(MULTIPLIERS[m].label).append(" Speedup");
            pw.println(header);

            // rows
            for (int t = 0; t < THREADS.length; t++) {
                for (int s = 0; s < SIZES.length; s++) {
                    StringBuilder row = new StringBuilder();
                    row.append(THREADS[t]).append(",").append(SIZES[s]).append("x").append(SIZES[s]);
                    for (int m = 0; m < MULTIPLIERS.length; m++)
                        row.append(",").append(times[t][s][m]);
                    for (int m = 0; m < MULTIPLIERS.length; m++) {
                        if (!MULTIPLIERS[m].isConcurrent) continue;
                        long baseline = times[t][s][m - 1]; // assumed as in order
                        String speedup = baseline == 0 ? "N/A"
                            : String.format("%.2f", (double) baseline / times[t][s][m]);
                        row.append(",").append(speedup);
                    }
                    pw.println(row);
                }
            }
        }
    }
}
