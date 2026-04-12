#ifndef MATRIX_H
#define MATRIX_H

#define STRASSEN_THRESHOLD 64

int**  mat_alloc(int n);
void   mat_free(int** M);
int**  mat_clone(int** M, int n);
int**  mat_add(int** A, int** B, int n);
int**  mat_sub(int** A, int** B, int n);
int**  mat_slice(int** M, int row, int col, int size);
int**  mat_combine(int** C11, int** C12, int** C21, int** C22, int half);
int**  mat_pad(int** M, int n, int new_n);
int**  mat_unpad(int** M, int orig_n);
void   mat_random(int** M, int n, unsigned int seed);
int    mat_equals(int** A, int** B, int n);

int**  naive_multiply(int** A, int** B, int n);
int**  naive_omp_multiply(int** A, int** B, int n);
int**  strassen_multiply(int** A, int** B, int n);
int**  strassen_omp_multiply(int** A, int** B, int n);

#endif
