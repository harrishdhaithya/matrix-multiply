#include "matrix.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>

#define NUM_SIZES   10
#define NUM_MULS     4
#define NUM_THREADS  4

static const int SIZES[NUM_SIZES]     = {256, 512, 768, 1024, 1280, 1536, 1792, 2048, 2304, 2560};
static const int THREADS[NUM_THREADS] = {2, 4, 8,10};
static const char* NAMES[NUM_MULS]   = {"Naive", "NaiveOMP", "Strassen", "StrassenOMP"};

typedef int** (*mul_fn)(int**, int**, int);
static const mul_fn MULS[NUM_MULS] = {
    naive_multiply, naive_omp_multiply,
    strassen_multiply, strassen_omp_multiply
};

static long long ms_diff(struct timespec a, struct timespec b) {
    return (b.tv_sec - a.tv_sec) * 1000LL
         + (b.tv_nsec - a.tv_nsec) / 1000000LL;
}

static void write_report(long long times[NUM_THREADS][NUM_SIZES][NUM_MULS]) {
    FILE* f = fopen("report.csv", "w");
    if (!f) {
        perror("Cant open report.csv");
        return;
    }

    // header: times for all, speedup only for concurrent (index 1 and 3)
    fprintf(f, "Threads,Size");
    for (int m = 0; m < NUM_MULS; m++) fprintf(f, ",%s(ms)", NAMES[m]);
    fprintf(f, ",%s Speedup,%s Speedup\n", NAMES[1], NAMES[3]);

    // rows
    for (int t = 0; t < NUM_THREADS; t++) {
        for (int s = 0; s < NUM_SIZES; s++) {
            fprintf(f, "%d,%dx%d", THREADS[t], SIZES[s], SIZES[s]);
            for (int m = 0; m < NUM_MULS; m++)
                fprintf(f, ",%lld", times[t][s][m]);
            // NaiveOMP speedup vs Naive
            if (times[t][s][0] == 0) fprintf(f, ",N/A");
            else fprintf(f, ",%.2f", (double)times[t][s][0] / (double)times[t][s][1]);
            // StrassenOMP speedup vs Strassen
            if (times[t][s][2] == 0) fprintf(f, ",N/A");
            else fprintf(f, ",%.2f", (double)times[t][s][2] / (double)times[t][s][3]);
            fprintf(f, "\n");
        }
    }

    fclose(f);
}

int main(void) {
    printf("Max threads available: %d\n\n", omp_get_max_threads());

    long long times[NUM_THREADS][NUM_SIZES][NUM_MULS];

    for (int t = 0; t < NUM_THREADS; t++) {
        omp_set_num_threads(THREADS[t]);
        printf("=== Threads: %d ===\n", THREADS[t]);

        for (int s = 0; s < NUM_SIZES; s++) {
            int n = SIZES[s];
            int** A = mat_alloc(n);
            int** B = mat_alloc(n);
            mat_random(A, n, 42);
            mat_random(B, n, 137);

            printf("--- %dx%d ---\n", n, n);

            for (int m = 0; m < NUM_MULS; m++) {
                struct timespec t0, t1;
                clock_gettime(CLOCK_MONOTONIC, &t0);
                int** result = MULS[m](A, B, n);
                clock_gettime(CLOCK_MONOTONIC, &t1);
                times[t][s][m] = ms_diff(t0, t1);
                mat_free(result);

                printf("  %-13s %7lldms\n", NAMES[m], times[t][s][m]);
            }

            mat_free(A);
            mat_free(B);
        }
        printf("\n");
    }

    write_report(times);
    printf("Report written to report.csv\n");
    return 0;
}
