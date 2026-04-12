package com.matmul.testcases;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MatrixTestCasesGenerator {
    public static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (int) (Math.random() * 10); // Random values between 0 and 9
            }
        }
        return matrix;
    }
    public static void main(String[] args) throws IOException {
        List<int[][]> matrices = new LinkedList<int[][]>();
        int[][][] testCases = new int[10][][];
        int seed = 256;
        File file = new File("src/main/resources/testcases/testcases.json");
        boolean exists = file.exists();
        if(!exists){
            exists=file.createNewFile();
        }
        if(exists){
            FileWriter fw = new FileWriter(file);
            for (int i = 1; i <= testCases.length; i++) {
                int[][] matrixA = generateMatrix(seed*i,seed*i);
                int[][] matrixB = generateMatrix(seed*i,seed*i);
                matrices.add(matrixA);
                matrices.add(matrixB);
            }
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < matrices.size(); i=i+2) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("MatrixA", Arrays.deepToString(matrices.get(i)));
                jsonObject.put("MatrixB", Arrays.deepToString(matrices.get(i+1)));
                jsonArray.put(jsonObject);
            }
            fw.write(jsonArray.toString());
            fw.flush();
            fw.close();
        }
        else
        {
            System.out.println("Failed to create the file.");
        }
    }
}
