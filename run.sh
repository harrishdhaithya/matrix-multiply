#!/bin/bash
set -e

echo "=== Java ==="
mkdir -p out
find src/main/java -name "*.java" | xargs javac -d out
java -cp out com.matmul.App
echo "Report: report.txt"

echo ""
echo "=== C ==="
cd c
gcc -O2 -fopenmp -o matmul main.c utils.c naive.c naive_omp.c strassen.c strassen_omp.c -lm
./matmul
echo "Report: c/report.txt"
