#include "matrix.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <omp.h>

#define NUM_SIZES 10
#define NUM_MULS   4

static const int   SIZES[NUM_SIZES] = {256, 512, 768, 1024, 1280, 1536, 1792, 2048, 2304, 2560};
static const char* NAMES[NUM_MULS]  = {
    "Naive", "NaiveOMP", "Strassen", "StrassenOMP"
};

typedef int** (*mul_fn)(int**, int**, int);
static const mul_fn MULS[NUM_MULS] = {
    naive_multiply, naive_omp_multiply,
    strassen_multiply, strassen_omp_multiply
};

static long long ms_diff(struct timespec a, struct timespec b) {
    return (b.tv_sec - a.tv_sec) * 1000LL
         + (b.tv_nsec - a.tv_nsec) / 1000000LL;
}

static void write_report(long long times[NUM_SIZES][NUM_MULS],
                         int correct[NUM_SIZES][NUM_MULS]) {
    const int col_w   = 20;
    const int sep_len = 1 + (col_w + 1) * (NUM_MULS + 1);
    char sep[256];
    memset(sep, '-', sep_len);
    sep[sep_len] = '\0';

    FILE* f = fopen("report.txt", "w");
    if (!f) {
     perror("Cant open report.txt");
     return;
    }

    fprintf(f, "Matrix Multiplication Performance Report C(OpenMP)\n");
    fprintf(f, "OpenMP threads: %d\n\n", omp_get_max_threads());

    /* Timing table */
    fprintf(f, "%s\n", sep);
    fprintf(f, "| %-*s", col_w, "Size");
    for (int m = 0; m < NUM_MULS; m++)
     fprintf(f, "| %-*s", col_w, NAMES[m]);

    fprintf(f, "|\n%s\n", sep);

    for (int s = 0; s < NUM_SIZES; s++) {
        char label[32];
        snprintf(label, sizeof(label), "%dx%d", SIZES[s], SIZES[s]);
        fprintf(f, "| %-*s", col_w, label);
        for (int m = 0; m < NUM_MULS; m++) {
            char cell[32];
            snprintf(cell, sizeof(cell), "%lldms%s",
                     times[s][m], correct[s][m] ? "" : " [WRONG]");
            fprintf(f, "| %-*s", col_w, cell);
        }
        fprintf(f, "|\n");
    }
    fprintf(f, "%s\n\n", sep);

    /* Speedup vs Naive table */
    fprintf(f, "Speedup vs Naive:\n%s\n", sep);
    fprintf(f, "| %-*s", col_w, "Size");
    for (int m = 0; m < NUM_MULS; m++) fprintf(f, "| %-*s", col_w, NAMES[m]);
    fprintf(f, "|\n%s\n", sep);

    for (int s = 0; s < NUM_SIZES; s++) {
        char label[32];
        snprintf(label, sizeof(label), "%dx%d", SIZES[s], SIZES[s]);
        fprintf(f, "| %-*s", col_w, label);
        for (int m = 0; m < NUM_MULS; m++) {
            char cell[32];
            if (m == 0)
                snprintf(cell, sizeof(cell), "1.00x (baseline)");
            else if (times[s][0] == 0)
                snprintf(cell, sizeof(cell), "N/A");
            else
                snprintf(cell, sizeof(cell), "%.2fx",
                         (double)times[s][0] / (double)times[s][m]);
            fprintf(f, "| %-*s", col_w, cell);
        }
        fprintf(f, "|\n");
    }
    fprintf(f, "%s\n", sep);

    fclose(f);
}

int main(void) {
    printf("OpenMP threads: %d\n\n", omp_get_max_threads());

    long long times[NUM_SIZES][NUM_MULS];
    int correct[NUM_SIZES][NUM_MULS];

    for (int s = 0; s < NUM_SIZES; s++) {
        int n = SIZES[s];
        int** A = mat_alloc(n);
        int** B = mat_alloc(n);
        mat_random(A, n, 42);
        mat_random(B, n, 137);

        printf("--- %dx%d ---\n", n, n);

        int** reference = NULL;
        for (int m = 0; m < NUM_MULS; m++) {
            struct timespec t0, t1;
            clock_gettime(CLOCK_MONOTONIC, &t0);
            int** result = MULS[m](A, B, n);
            clock_gettime(CLOCK_MONOTONIC, &t1);
            times[s][m] = ms_diff(t0, t1);

            if (m == 0) {
                reference     = result;
                correct[s][m] = 1;
            } else {
                correct[s][m] = mat_equals(result, reference, n);
                mat_free(result);
            }
            printf("  %-13s %7lldms  correct=%s\n",
                   NAMES[m], times[s][m], correct[s][m] ? "true" : "false");
        }

        mat_free(reference);
        mat_free(A);
        mat_free(B);
    }

    write_report(times, correct);
    printf("\nReport written to report.txt\n");
    return 0;
}
