package org.unittest.junit.basic;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MinCostPathTest {

    private MinCostPath minCostPath;

    @BeforeEach
    void setUp() {
        minCostPath = new MinCostPath();
    }

    /**
     * int matrix[][]
     * start cell, target cell
     * check startCell && targetCell are out of boundry
     * startCell == targetCell then return cost of the start cell
     * find minimum cost path for one row matrix
     * find minimum cost path for multiple row matrix
     * rigth cost, down cost, diagnol cost
     */

    private static Cell cell(final int row, final int column) {
        return new Cell(row, column);
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when the start or target cell is out of matrix bound")
    void throwsIllegalArgumentExceptionWhenTheCellIsOutOfMatrixBound() {

        int[][] matrix = new int[][] {
                { 4, 5, 6 },
                { 7, 8, 1 } };

        assertAll("Start cell must be in matrix",
                () -> assertThrows(IllegalArgumentException.class, () -> minCostPath.find(matrix, cell(2, 1), cell(0, 2))),
                () -> assertThrows(IllegalArgumentException.class, () -> minCostPath.find(matrix, cell(-1, 1), cell(0, 2))));

        assertAll("Target cell must be in matrix",
                () -> assertThrows(IllegalArgumentException.class, () -> minCostPath.find(matrix, cell(0, 0), cell(2, 1))),
                () -> assertThrows(IllegalArgumentException.class, () -> minCostPath.find(matrix, cell(0, 0), cell(-1, 2))));
    }

    @Test
    @DisplayName("Return the cost of start cell when the start cell is equal to target cell")
    void returnTheCostStartCellWhenTheStartCellIfEqualtoTargetCell() {

        int[][] matrix = new int[][] { { 3, 5, 7, 9 } };

        assertAll("Start and Target cell equals tests",
                () -> assertEquals(3, minCostPath.find(matrix, cell(0, 0), cell(0, 0))),
                () -> assertEquals(7, minCostPath.find(matrix, cell(0, 2), cell(0, 2))));
    }

    @Test
    @DisplayName("Find min cost path for one row matrix")
    void findMinCostPathForOneRowMatrix() {
        int[][] matrix = new int[][] { { 3, 5, 7, 9 } };

        assertAll("min cost path for one row matrix",
                () -> assertEquals(8, minCostPath.find(matrix, cell(0, 0), cell(0, 1))),
                () -> assertEquals(15, minCostPath.find(matrix, cell(0, 0), cell(0, 2))),
                () -> assertEquals(16, minCostPath.find(matrix, cell(0, 2), cell(0, 3))));
    }

    @Test
    @DisplayName("Find min cost path for multi row matrix")
    void findMinCostPathFprMultiRowMatrix() {

        int[][] matrix = new int[][] {
                { 1, 2, 3, 4 },
                { 1, 3, 1, 2 },
                { 1, 2, 3, 5 }
        };

        assertAll("Find min cost path for multi row matrix",
                () -> assertEquals(4, minCostPath.find(matrix, cell(0, 0), cell(1, 2))),
                () -> assertEquals(4, minCostPath.find(matrix, cell(0, 0), cell(2, 1))),
                () -> assertEquals(9, minCostPath.find(matrix, cell(0, 0), cell(2, 3))));

    }
}
