import javax.swing.table.AbstractTableModel;
import java.util.Arrays;

public class ValuesTableModel extends AbstractTableModel {
    String[] colNames;
    String[][] colValues;

    public String[][] getValues(){
        return this.colValues;
    }

    public ValuesTableModel(String[][] values) {
        this.colNames = new String[values[0].length];
        this.colValues = Arrays.copyOf(values,values.length);
    }

    public ValuesTableModel(String[] cols, String[][] values) {
        this.colNames = Arrays.copyOf(cols,cols.length);
        this.colValues = Arrays.copyOf(values,values.length);
    }

    public ValuesTableModel(int rowsN, int colsN) {
        this.colNames = new String[colsN];
        this.colValues = new String[rowsN][colsN];

        for (int row = 0; row < rowsN; row++) {
            colValues[row] = new String[colsN];
            for (int col = 0; col < colsN; col++) {
                colValues[row][col] = "";
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    @Override
    public int getRowCount() {
        return colValues.length;
    }

    @Override
    public int getColumnCount() {
        return colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (colValues.length >= rowIndex && columnIndex <= colNames.length) {
            return colValues[rowIndex][columnIndex]==null?"":colValues[rowIndex][columnIndex];
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (colValues.length >= rowIndex && columnIndex <= colNames.length) {
            colValues[rowIndex][columnIndex] = String.valueOf(aValue);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}