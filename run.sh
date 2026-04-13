#!/bin/bash
set -e

echo "=== Java ==="
javac -cp out -d out src/main/java/com/matmul/App.java src/main/java/com/matmul/utils/* src/main/java/com/matmul/multiplier/*
java -cp out com.matmul.App
echo "Report: report.csv"

echo ""
echo "=== C ==="
cd c_src
gcc-15 -O3 -march=native -fopenmp -o matmul main.c utils.c naive.c naive_omp.c strassen.c strassen_omp.c
./matmul
echo "Report: c_src/report.csv"
