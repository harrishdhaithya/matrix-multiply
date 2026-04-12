#ifndef MATRIX_H
#define MATRIX_H

#define STRASSEN_THRESHOLD 64

/* Utilities */
int*  mat_alloc(int n);
int*  mat_clone(const int* M, int n);
int*  mat_add(const int* A, const int* B, int n);
int*  mat_sub(const int* A, const int* B, int n);
int*  mat_slice(const int* M, int row, int col, int size, int stride);
int*  mat_combine(const int* C11, const int* C12,
                  const int* C21, const int* C22, int half);
int*  mat_pad(const int* M, int n, int new_n);
int*  mat_unpad(const int* M, int padded_n, int orig_n);
void  mat_random(int* M, int n, unsigned int seed);
int   mat_equals(const int* A, const int* B, int n);

/* Multipliers */
int*  naive_multiply(const int* A, const int* B, int n);
int*  naive_omp_multiply(const int* A, const int* B, int n);
int*  strassen_multiply(const int* A, const int* B, int n);
int*  strassen_omp_multiply(const int* A, const int* B, int n);
#endif
