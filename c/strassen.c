#include "matrix.h"
#include <stdlib.h>

int* strassen_multiply(const int* A, const int* B, int n) {
    if (n <= STRASSEN_THRESHOLD)
        return naive_multiply(A, B, n);

    if (n % 2 != 0) {
        int* pA = mat_pad(A, n, n + 1);
        int* pB = mat_pad(B, n, n + 1);
        int* r  = strassen_multiply(pA, pB, n + 1);
        free(pA); free(pB);
        int* result = mat_unpad(r, n + 1, n);
        free(r);
        return result;
    }

    int half = n / 2;

    int* A11 = mat_slice(A, 0,    0,    half, n);
    int* A12 = mat_slice(A, 0,    half, half, n);
    int* A21 = mat_slice(A, half, 0,    half, n);
    int* A22 = mat_slice(A, half, half, half, n);
    int* B11 = mat_slice(B, 0,    0,    half, n);
    int* B12 = mat_slice(B, 0,    half, half, n);
    int* B21 = mat_slice(B, half, 0,    half, n);
    int* B22 = mat_slice(B, half, half, half, n);

    int *t1, *t2;

    t1 = mat_add(A11, A22, half); t2 = mat_add(B11, B22, half);
    int* M1 = strassen_multiply(t1, t2, half); free(t1); free(t2);

    t1 = mat_add(A21, A22, half);
    int* M2 = strassen_multiply(t1, B11, half); free(t1);

    t1 = mat_sub(B12, B22, half);
    int* M3 = strassen_multiply(A11, t1, half); free(t1);

    t1 = mat_sub(B21, B11, half);
    int* M4 = strassen_multiply(A22, t1, half); free(t1);

    t1 = mat_add(A11, A12, half);
    int* M5 = strassen_multiply(t1, B22, half); free(t1);

    t1 = mat_sub(A21, A11, half); t2 = mat_add(B11, B12, half);
    int* M6 = strassen_multiply(t1, t2, half); free(t1); free(t2);

    t1 = mat_sub(A12, A22, half); t2 = mat_add(B21, B22, half);
    int* M7 = strassen_multiply(t1, t2, half); free(t1); free(t2);

    free(A11); free(A12); free(A21); free(A22);
    free(B11); free(B12); free(B21); free(B22);

    t1 = mat_add(M1, M4, half);
    t2 = mat_sub(t1, M5, half); free(t1);
    int* C11 = mat_add(t2, M7, half); free(t2);

    int* C12 = mat_add(M3, M5, half);
    int* C21 = mat_add(M2, M4, half);

    t1 = mat_add(M1, M3, half);
    t2 = mat_sub(t1, M2, half); free(t1);
    int* C22 = mat_add(t2, M6, half); free(t2);

    free(M1); free(M2); free(M3); free(M4);
    free(M5); free(M6); free(M7);

    int* result = mat_combine(C11, C12, C21, C22, half);
    free(C11); free(C12); free(C21); free(C22);
    return result;
}
