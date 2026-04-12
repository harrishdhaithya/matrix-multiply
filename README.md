# How to run

## Java

### Prerequisites
- Java 11+

### Run

```bash
# Compile
find src/main/java -name "*.java" | xargs javac -d out

# Run
java -cp out com.matmul.App
```

Output: `report.txt` in the project root.

---

## C

### Prerequisites

**macOS**
```bash
brew install gcc
```

**Linux** — GCC is available by default.

### Build & Run

**macOS**
```bash
cd c
gcc-15 -O2 -fopenmp -o matmul main.c utils.c naive.c naive_omp.c strassen.c strassen_omp.c -lm
./matmul
```

**Linux**
```bash
cd c
gcc -O2 -fopenmp -o matmul main.c utils.c naive.c naive_omp.c strassen.c strassen_omp.c -lm
./matmul
```

Output: `report.txt` inside the `c/` directory.

---

## Project Structure

```
matrix-mul/
├── c/
│   ├── matrix.h              # Shared declarations
│   ├── utils.c               # Matrix utilities (alloc, add, sub, slice, ...)
│   ├── naive.c               # Naive multiplier
│   ├── naive_omp.c           # Naive with OpenMP parallel for
│   ├── strassen.c            # Strassen multiplier
│   ├── strassen_omp.c        # Strassen with OpenMP tasks
│   └── main.c                # Benchmark runner
├── src/main/java/com/matmul/
│   ├── App.java              # Benchmark runner
│   ├── multiplier/
│   │   ├── AbstractMultiplier.java
│   │   ├── NaiveMultiplier.java
│   │   ├── NaiveConcurrentMultiplier.java
│   │   ├── StrassensMultiplier.java
│   │   └── StrassensConcurrentMultiplier.java
│   └── utils/
│       └── CommonUtils.java  # Shared matrix utilities
└── pom.xml
```
