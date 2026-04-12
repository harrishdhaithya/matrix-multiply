#include "matrix.h"

int* naive_multiply(const int* A, const int* B, int n) {
    int* C = mat_alloc(n);
    for (int i = 0; i < n; i++)
        for (int k = 0; k < n; k++)
            for (int j = 0; j < n; j++)
                C[i * n + j] += A[i * n + k] * B[k * n + j];
    return C;
}
