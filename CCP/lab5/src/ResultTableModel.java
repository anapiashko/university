import javax.swing.table.AbstractTableModel;

public class ResultTableModel extends AbstractTableModel {
    String[] colNames = {"", "По строке", "По столбцу", "По таблице"};
    String[][] colValues = {
            {"Cумма", "", "", ""},
            {"Среднее", "", "", ""},
            {"Минимум", "", "", ""},
            {"Максимум", "", "", ""}
    };

    public ResultTableModel() {
    }

    public void setResultSumm(String byTableRow, String byTableCol, String byTable){
        this.colValues[0][1]=byTableRow;
        this.colValues[0][2]=byTableCol;
        this.colValues[0][3]=byTable;
    }
    public void setResultAverage(String byTableRow, String byTableCol, String byTable){
        this.colValues[1][1]=byTableRow;
        this.colValues[1][2]=byTableCol;
        this.colValues[1][3]=byTable;
    }
    public void setResultMinimum(String byTableRow, String byTableCol, String byTable){
        this.colValues[2][1]=byTableRow;
        this.colValues[2][2]=byTableCol;
        this.colValues[2][3]=byTable;
    }
    public void setResultMaximum(String byTableRow, String byTableCol, String byTable){
        this.colValues[3][1]=byTableRow;
        this.colValues[3][2]=byTableCol;
        this.colValues[3][3]=byTable;
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
            return colValues[rowIndex][columnIndex] == null ? "" : colValues[rowIndex][columnIndex];
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}