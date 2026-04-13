#include "matrix.h"
#include <omp.h>

int** naive_omp_multiply(int** A, int** B, int n) {
    if (n < 256) return naive_multiply(A, B, n);

    int** C = mat_alloc(n);
    #pragma omp parallel for schedule(static)
    for (int i = 0; i < n; i++) {
        int* ci = C[i];
        int* ai = A[i];
        for (int k = 0; k < n; k++) {
            int aik = ai[k];
            int* bk  = B[k];
            for (int j = 0; j < n; j++)
                ci[j] += aik * bk[j];
        }
    }
    return C;
}
