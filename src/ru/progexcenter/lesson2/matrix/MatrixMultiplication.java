package ru.progexcenter.lesson2.matrix;

import java.util.Arrays;

public class MatrixMultiplication {

    public static void main(String[] args) {
        int[][] A = {{3, 2}, {4, 5}};
        int[][] B = {{2},{1}};
        int[][] C = null;
        // C = A*B = {{8}, {13}}
        int[][] A1 =
                {
                        {13, 2, 54, 0, 3},
                        {3,  2, 1,  0, 31},
                        {13, 2, 5, 10, 34},
                        {1,  7, 4,  2, 9},
                };
        int[][] B1 =
                {
                        {51, 34},
                        {0, 343},
                        {43, 234},
                        {5, 25},
                        {1, 65},
                };

        //TODO write multiplication algorithm
        
        //correct way to output the array
        Arrays.deepToString(C);
    }
}
