#include "matrix.h"
#include <stdlib.h>
#include <omp.h>

static int** strassen_omp_task(int** A, int** B, int n);

int** strassen_omp_multiply(int** A, int** B, int n) {
    int** result;
    #pragma omp parallel
    #pragma omp single
    result = strassen_omp_task(A, B, n);
    return result;
}

static int** strassen_omp_task(int** A, int** B, int n) {
    if (n <= STRASSEN_THRESHOLD)
        return naive_multiply(A, B, n);

    if (n % 2 != 0) {
        int** pA = mat_pad(A, n, n + 1);
        int** pB = mat_pad(B, n, n + 1);
        int** r  = strassen_omp_task(pA, pB, n + 1);
        mat_free(pA);
        mat_free(pB);
        int** result = mat_unpad(r, n);
        mat_free(r);
        return result;
    }

    int half = n / 2;
    int** A11 = mat_slice(A,0,0,half);
    int** A12 = mat_slice(A,0,half, half);
    int** A21 = mat_slice(A,half, 0,half);
    int** A22 = mat_slice(A,half, half, half);
    int** B11 = mat_slice(B,0,0,half);
    int** B12 = mat_slice(B,0,half, half);
    int** B21 = mat_slice(B,half,0,half);
    int** B22 = mat_slice(B,half,half,half);

    int **M1, **M2, **M3, **M4, **M5, **M6, **M7;

    #pragma omp task shared(M1)
    {
        int **a = mat_add(A11, A22, half);
        int **b = mat_add(B11, B22, half);
        M1 = strassen_omp_task(a, b, half);
        mat_free(a);
        mat_free(b);
    }

    #pragma omp task shared(M2)
    {
        int** a = mat_add(A21, A22, half);
        M2 = strassen_omp_task(a, B11, half);
        mat_free(a);
    }

    #pragma omp task shared(M3)
    {
        int** b = mat_sub(B12, B22, half);
        M3 = strassen_omp_task(A11, b, half);
        mat_free(b);
    }

    #pragma omp task shared(M4)
    {
        int** b = mat_sub(B21, B11, half);
        M4 = strassen_omp_task(A22, b, half);
        mat_free(b);
    }

    #pragma omp task shared(M5)
    {
        int** a = mat_add(A11, A12, half);
        M5 = strassen_omp_task(a, B22, half);
        mat_free(a);
    }

    #pragma omp task shared(M6)
    {
        int** a = mat_sub(A21, A11, half);
        int** b = mat_add(B11, B12, half);
        M6 = strassen_omp_task(a, b, half);
        mat_free(a);
        mat_free(b);
    }

    {
        int** a = mat_sub(A12, A22, half);
        int **b = mat_add(B21, B22, half);
        M7 = strassen_omp_task(a, b, half);
        mat_free(a);
        mat_free(b);
    }

    #pragma omp taskwait
    //Addjusted indentation for readability
    mat_free(A11); mat_free(A12); mat_free(A21); mat_free(A22);
    mat_free(B11); mat_free(B12); mat_free(B21); mat_free(B22);

    int **t1, **t2;
    t1 = mat_add(M1, M4, half); t2 = mat_sub(t1, M5, half); mat_free(t1);
    int** C11 = mat_add(t2, M7, half); mat_free(t2);
    int** C12 = mat_add(M3, M5, half);
    int** C21 = mat_add(M2, M4, half);
    t1 = mat_add(M1, M3, half); t2 = mat_sub(t1, M2, half); mat_free(t1);
    int** C22 = mat_add(t2, M6, half); mat_free(t2);

    mat_free(M1); mat_free(M2); mat_free(M3); mat_free(M4);
    mat_free(M5); mat_free(M6); mat_free(M7);

    int** result = mat_combine(C11, C12, C21, C22, half);
    mat_free(C11); mat_free(C12); mat_free(C21); mat_free(C22);
    return result;
}
