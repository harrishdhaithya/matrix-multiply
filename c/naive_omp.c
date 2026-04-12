#include "matrix.h"
#include <omp.h>

int* naive_omp_multiply(const int* A, const int* B, int n) {
    if (n < 256) return naive_multiply(A, B, n);

    int* C = mat_alloc(n);
    #pragma omp parallel for schedule(static)
    for (int i = 0; i < n; i++)
        for (int k = 0; k < n; k++)
            for (int j = 0; j < n; j++)
                C[i * n + j] += A[i * n + k] * B[k * n + j];
    return C;
}
