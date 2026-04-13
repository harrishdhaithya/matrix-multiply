#include "matrix.h"
#include <stdlib.h>

int** mat_alloc(int n) {
    int** M = malloc(n * sizeof(int*)); //initialise a pointer to an arrary.
    int*  data = calloc(n * n, sizeof(int)); //allocate n*n integers.
    for (int i = 0; i < n; i++)
        M[i] = data + i * n;
    return M;
}

void mat_free(int** M) {
    free(M[0]); //Deallocate array
    free(M); //Deallocate pointer to array
}

int** mat_clone(int** M, int n) {
    int** C = mat_alloc(n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            C[i][j] = M[i][j];
    return C;
}

int** mat_add(int** A, int** B, int n) {
    int** C = mat_alloc(n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            C[i][j] = A[i][j] + B[i][j];
    return C;
}

int** mat_sub(int** A, int** B, int n) {
    int** C = mat_alloc(n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            C[i][j] = A[i][j] - B[i][j];
    return C;
}

int** mat_slice(int** M, int row, int col, int size) {
    int** S = mat_alloc(size);
    for (int i = 0; i < size; i++)
        for (int j = 0; j < size; j++)
            S[i][j] = M[row + i][col + j];
    return S;
}

int** mat_combine(int** C11, int** C12, int** C21, int** C22, int half) {
    int n = half * 2;
    int** C = mat_alloc(n);
    for (int i = 0; i < half; i++)
        for (int j = 0; j < half; j++) {
            C[i][j]= C11[i][j];
            C[i][j + half] = C12[i][j];
            C[i + half][j]= C21[i][j];
            C[i + half][j + half] = C22[i][j];
        }
    return C;
}

int** mat_pad(int** M, int n, int new_n) {
    int** P = mat_alloc(new_n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            P[i][j] = M[i][j];
    return P;
}

int** mat_unpad(int** M, int orig_n) {
    int** U = mat_alloc(orig_n);
    for (int i = 0; i < orig_n; i++)
        for (int j = 0; j < orig_n; j++)
            U[i][j] = M[i][j];
    return U;
}

void mat_random(int** M, int n, unsigned int seed) {
    srand(seed);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            M[i][j] = rand() % 10;
}

int mat_equals(int** A, int** B, int n) {
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (A[i][j] != B[i][j])
                return 0;
    return 1;
}
