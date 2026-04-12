#include "matrix.h"
#include <omp.h>

int** naive_omp_multiply(int** A, int** B, int n) {

    int** C = mat_alloc(n);
    #pragma omp parallel for schedule(static)
    for (int i = 0; i < n; i++)
        for (int k = 0; k < n; k++)
            for (int j = 0; j < n; j++)
                C[i][j] += A[i][k] * B[k][j];
    return C;
}
