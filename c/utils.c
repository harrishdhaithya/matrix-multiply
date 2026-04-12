#include "matrix.h"
#include <stdlib.h>
#include <string.h>

int* mat_alloc(int n) {
    return calloc((size_t)n * n, sizeof(int));
}

int* mat_clone(const int* M, int n) {
    int* C = mat_alloc(n);
    memcpy(C, M, (size_t)n * n * sizeof(int));
    return C;
}

int* mat_add(const int* A, const int* B, int n) {
    int* C = mat_alloc(n);
    for (int i = 0; i < n * n; i++) C[i] = A[i] + B[i];
    return C;
}

int* mat_sub(const int* A, const int* B, int n) {
    int* C = mat_alloc(n);
    for (int i = 0; i < n * n; i++) C[i] = A[i] - B[i];
    return C;
}

int* mat_slice(const int* M, int row, int col, int size, int stride) {
    int* S = mat_alloc(size);
    for (int i = 0; i < size; i++)
        for (int j = 0; j < size; j++)
            S[i * size + j] = M[(row + i) * stride + (col + j)];
    return S;
}

int* mat_combine(const int* C11, const int* C12,
                 const int* C21, const int* C22, int half) {
    int n = half * 2;
    int* C = mat_alloc(n);
    for (int i = 0; i < half; i++)
        for (int j = 0; j < half; j++) {
            C[ i       * n + j      ] = C11[i * half + j];
            C[ i       * n + j+half ] = C12[i * half + j];
            C[(i+half) * n + j      ] = C21[i * half + j];
            C[(i+half) * n + j+half ] = C22[i * half + j];
        }
    return C;
}

int* mat_pad(const int* M, int n, int new_n) {
    int* P = mat_alloc(new_n);
    for (int i = 0; i < n; i++)
        memcpy(P + i * new_n, M + i * n, n * sizeof(int));
    return P;
}

int* mat_unpad(const int* M, int padded_n, int orig_n) {
    int* U = mat_alloc(orig_n);
    for (int i = 0; i < orig_n; i++)
        memcpy(U + i * orig_n, M + i * padded_n, orig_n * sizeof(int));
    return U;
}

void mat_random(int* M, int n, unsigned int seed) {
    srand(seed);
    for (int i = 0; i < n * n; i++)
        M[i] = rand() % 10;
}

int mat_equals(const int* A, const int* B, int n) {
    return memcmp(A, B, (size_t)n * n * sizeof(int)) == 0;
}
