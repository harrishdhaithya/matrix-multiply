@echo off

echo === Java ===
if not exist out mkdir out
javac -d out src\main\java\com\matmul\App.java src\main\java\com\matmul\utils\*.java src\main\java\com\matmul\multiplier\*.java
java -cp out com.matmul.App
echo Report: report.csv

echo.
echo === C ===
cd c_src
gcc -O3 -march=native -fopenmp -o matmul.exe main.c utils.c naive.c naive_omp.c strassen.c strassen_omp.c
matmul.exe
echo Report: c_src\report.csv
