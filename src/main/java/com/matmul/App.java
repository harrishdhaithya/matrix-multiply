package com.matmul;

import com.matmul.utils.CommonConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class App {

    private static final int[] SIZES = {256, 512, 768, 1024, 1280, 1536, 1792, 2048, 2304, 2560};
    private static final CommonConstants[] MULTIPLIERS = CommonConstants.values();

    public static void main(String[] args) throws IOException {
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors() + "\n");

        long[][] times = new long[SIZES.length][MULTIPLIERS.length];

        for (int s = 0; s < SIZES.length; s++) {
            int n = SIZES[s];
            int[][] a = randomMatrix(n, 42);
            int[][] b = randomMatrix(n, 137);

            System.out.printf("--- %dx%d ---%n", n, n);

            for (int m = 0; m < MULTIPLIERS.length; m++) {
                long start = System.currentTimeMillis();
                MULTIPLIERS[m].multiplier.multiply(a, b);
                times[s][m] = System.currentTimeMillis() - start;

                System.out.printf("  %-20s %6dms%n", MULTIPLIERS[m].label, times[s][m]);
            }
        }

        writeReport(times);
        System.out.println("\nReport written to report.txt");
    }

    private static int[][] randomMatrix(int n, long seed) {
        Random rand = new Random(seed);
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                m[i][j] = rand.nextInt(10);
        return m;
    }

    private static void writeReport(long[][] times) throws IOException {
        int colWidth = 22;
        String separator = "-".repeat(1 + (1 + colWidth) * (1 + MULTIPLIERS.length));

        try (PrintWriter pw = new PrintWriter(new FileWriter("report.txt"))) {
            pw.println("Matrix Multiplication Performance Report");
            pw.println("Environment: " + Runtime.getRuntime().availableProcessors() + " available processors");
            pw.println();
            pw.println(separator);

            pw.printf("| %-" + colWidth + "s", "Size");
            for (CommonConstants c : MULTIPLIERS) pw.printf("| %-" + colWidth + "s", c.label);
            pw.println("|");
            pw.println(separator);

            for (int s = 0; s < SIZES.length; s++) {
                pw.printf("| %-" + colWidth + "s", SIZES[s] + "x" + SIZES[s]);
                for (int m = 0; m < MULTIPLIERS.length; m++)
                    pw.printf("| %-" + colWidth + "s", times[s][m] + "ms");
                pw.println("|");
            }

            pw.println(separator);
            pw.println();

            pw.println("Speedup vs Naive:");
            pw.println(separator);
            pw.printf("| %-" + colWidth + "s", "Size");
            for (CommonConstants c : MULTIPLIERS) pw.printf("| %-" + colWidth + "s", c.label);
            pw.println("|");
            pw.println(separator);

            for (int s = 0; s < SIZES.length; s++) {
                pw.printf("| %-" + colWidth + "s", SIZES[s] + "x" + SIZES[s]);
                for (int m = 0; m < MULTIPLIERS.length; m++) {
                    String cell = m == 0 ? "1.00x (baseline)"
                        : times[s][0] == 0 ? "N/A"
                        : String.format("%.2fx", (double) times[s][0] / times[s][m]);
                    pw.printf("| %-" + colWidth + "s", cell);
                }
                pw.println("|");
            }

            pw.println(separator);
        }
    }
}
