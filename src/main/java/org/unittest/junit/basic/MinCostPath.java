package org.unittest.junit.basic;

public class MinCostPath {

    public int find(final int[][] matrix, final Cell start, final Cell target) {

        validatetIftheCellOutOfMatrixBoundry(matrix, start);
        validatetIftheCellOutOfMatrixBoundry(matrix, target);

        if (start.equals(target)) {
            return matrix[start.getRow()][start.getColumn()];
        }

        return findMinCostPath(matrix, start, target);
    }

    private int findMinCostPath(final int[][] matrix, final Cell start, final Cell target) {
        if (start.getRow() > target.getRow() || start.getColumn() > target.getColumn()) {
            return Integer.MAX_VALUE;
        }
        if (start.equals(target)) {
            return matrix[start.getRow()][start.getColumn()];
        }

        int rightPathCost = findMinCostPath(matrix, new Cell(start.getRow(), start.getColumn() + 1), target);
        int downPathCost = findMinCostPath(matrix, new Cell(start.getRow() + 1, start.getColumn()), target);
        int diagonalPathCost = findMinCostPath(matrix, new Cell(start.getRow() + 1, start.getColumn() + 1), target);

        final int min = Math.min(rightPathCost, Math.min(downPathCost, diagonalPathCost));

        return min + matrix[start.getRow()][start.getColumn()];
    }

    private static void validatetIftheCellOutOfMatrixBoundry(final int[][] matrix, final Cell cell) {
        if (cell.getRow() >= matrix.length || cell.getRow() < 0) {
            throw new IllegalArgumentException();
        }
    }
}
