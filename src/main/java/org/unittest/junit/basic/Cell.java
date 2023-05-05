package org.unittest.junit.basic;

public class Cell {

    private final int row;
    private final int column;

    public Cell(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!Cell.class.isInstance(obj)) {
            return false;
        }
        final Cell cell = (Cell) obj;
        return this.row == cell.getRow() && this.column == cell.getColumn();
    }
}
