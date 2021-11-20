import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TableFrame extends JFrame {

    // начальные значения
    int
            startVal_mainRows = 10, // кол-во строк
            startVal_mainCols = 5, // кол-во столбцов
            startVal_minRand = -20, // произв. число минимальное значение
            startVal_maxRand = 20;// произв. число макснимальное значение

    private JScrollPane mainTableSP; // Панель прокрутки для главной таблицы
    private JTable mainTable; //Главная таблица

    private JPanel bottomPanel; // Нижняя панель с кнопками и таблицей результатов

    /* =================================== | BOTTOM PANEL COMPONENTS - LEFT SIDE | ================================== */
    // Параметры Инициализации таблицы
    private JLabel colCountLabel;
    private JSpinner colCountSelector;

    private JLabel rowCountLabel;
    private JSpinner rowCountSelector;

    // Кнопка Генерация модели таблицы
    private JButton generateTM;

    // Параметры расчета по таблице
    private JLabel colNumberLabel;
    private JSpinner colNumberSelector;

    private JLabel rowNumberLabel;
    private JSpinner rowNumberSelector;

    // Кнопка получения результатов
    private JButton calculate;
    // Кнопка сохранения в xml
    private JButton load;
    private JButton save;
    /* =================================== | BOTTOM PANEL COMPONENTS - LEFT SIDE | ================================== */

    /* ================================== | BOTTOM PANEL COMPONENTS - RIGHT SIDE | ================================== */
    private JScrollPane resultTableSP; // Панель прокрутки для таблицы с результатами
    private JTable resultTable; //Таблица с результатами
    /* ================================== | BOTTOM PANEL COMPONENTS - RIGHT SIDE | ================================== */

    public TableFrame() {
        this.setTitle("Dynamical Table");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(750, 450);
        this.setMinimumSize(getSize());
        initComponents();
        //pack();
    }

    private ValuesTableModel initValuesTM(String[][] values) {
        return new ValuesTableModel(values);
    }

    private ValuesTableModel initValuesTM(int rowsN, int colsN) {
        return this.initValuesTM(rowsN, colsN, startVal_minRand, startVal_maxRand);
    }

    private ValuesTableModel initValuesTM(int _rowsN, int _colsN, int minRand, int maxRand) {

        int rowsN = Math.max(_rowsN, 0);
        int colsN = Math.max(_colsN, 0);

        String[] colNames = new String[colsN];
        String[][] colValues = new String[rowsN][colsN];

        Random random = new Random();

        for (int row = 0; row < rowsN; row++) {
            colValues[row] = new String[colsN];
            for (int col = 0; col < colsN; col++) {
                colValues[row][col] = String.valueOf((random.nextInt((maxRand + 1) - minRand) + minRand));
            }
        }

        return new ValuesTableModel(colNames, colValues);
    }

    private void initComponents() {

        mainTableSP = new JScrollPane();
        mainTable = new JTable(initValuesTM(startVal_mainRows, startVal_mainCols, startVal_minRand, startVal_maxRand));
        mainTableSP.setViewportView(mainTable);//создает область просмотра главной таблицы
        mainTable.getTableHeader().setReorderingAllowed(false); // фиксация табличного пространства (запрет перемещения столбцов) главной таблицы

        mainTable.setShowGrid(true);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        ListSelectionModel mainTableSelectionModel = mainTable.getSelectionModel();
        mainTableSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mainTable.setCellSelectionEnabled(true);
        mainTable.setRowSelectionAllowed(true);
        mainTable.setColumnSelectionAllowed(true);

        mainTableSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                mainTable_handleSelectionEvent(e);
            }
        });

        mainTable.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                mainTable_handleSelectionEvent(e);
            }
        });

        this.add(mainTableSP, BorderLayout.CENTER);

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        Dimension bottomPanelDimension = new Dimension(700, 180);
        bottomPanel.setSize(bottomPanelDimension);
        bottomPanel.setPreferredSize(bottomPanelDimension);
        bottomPanel.setMinimumSize(bottomPanelDimension);
        bottomPanel.setMaximumSize(bottomPanelDimension);

        /* ===================================== | LEFT PANEL COMPONENTS START | ==================================== */
        bottomPanel.add(new JPanel() {{
            //======== init ========
            rowCountLabel = new JLabel();
            rowCountSelector = new JSpinner();
            rowCountSelector.setValue(startVal_mainRows);
            colCountLabel = new JLabel();
            colCountSelector = new JSpinner();
            colCountSelector.setValue(startVal_mainCols);
            generateTM = new JButton();
            rowNumberLabel = new JLabel();
            rowNumberSelector = new JSpinner();
            load = new JButton();
            colNumberLabel = new JLabel();
            colNumberSelector = new JSpinner();
            save = new JButton();

            //======== this ========
            this.setLayout(new GridBagLayout());
            this.setBorder(new TitledBorder("Парематры"));

            Dimension optionsPanelDimension = new Dimension(325, bottomPanelDimension.height - 10);
            this.setSize(optionsPanelDimension);
            this.setPreferredSize(optionsPanelDimension);
            this.setMinimumSize(optionsPanelDimension);
            this.setMaximumSize(optionsPanelDimension);

            ((GridBagLayout) getLayout()).columnWidths = new int[]{130, 70, 100, 0};
            ((GridBagLayout) getLayout()).rowHeights = new int[]{28, 28, 28, 0, 24, 24};

            //---- rowCountLabel ----
            rowCountLabel.setText("Количество строк: ");
            rowCountLabel.setFont(rowCountLabel.getFont().deriveFont(rowCountLabel.getFont().getStyle() | Font.BOLD, rowCountLabel.getFont().getSize() + 3f));
            add(rowCountLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 10), 0, 0));
            add(rowCountSelector, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- colCountLabel ----
            colCountLabel.setText("Количество столбцов: ");
            colCountLabel.setFont(colCountLabel.getFont().deriveFont(colCountLabel.getFont().getStyle() | Font.BOLD, colCountLabel.getFont().getSize() + 3f));
            add(colCountLabel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 10), 0, 0));
            add(colCountSelector, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- generateTM ----
            add(generateTM, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            generateTM.setAction(new AbstractAction("Сформировать таблицу") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    generateTM_handleAction(e);
                }
            });

            //---- rowNumberLabel ----
            rowNumberLabel.setText("Номер строки: ");
            rowNumberLabel.setFont(rowNumberLabel.getFont().deriveFont(rowNumberLabel.getFont().getStyle() | Font.BOLD, rowNumberLabel.getFont().getSize() + 3f));
            add(rowNumberLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 10), 0, 0));
            add(rowNumberSelector, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 10), 0, 0));

            //---- calculateRow ----
            add(load, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            load.setAction(new AbstractAction("Загрузить") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    load_handleAction(e);
                }
            });

            //---- colNumberLabel ----
            colNumberLabel.setText("Номер столбца: ");
            colNumberLabel.setFont(colNumberLabel.getFont().deriveFont(colNumberLabel.getFont().getStyle() | Font.BOLD, colNumberLabel.getFont().getSize() + 3f));
            add(colNumberLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 10), 0, 0));
            add(colNumberSelector, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 10), 0, 0));

            //---- CalculateCol ----
            add(save, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            save.setAction(new AbstractAction("Сохранить") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    save_handleAction(e);
                }
            });

        }}, BorderLayout.WEST);
        /* ===================================== | LEFT PANEL COMPONENTS  END | ===================================== */

        /* ==================================== | RIGHT PANEL COMPONENTS START | ==================================== */
        bottomPanel.add(new JPanel() {{
            this.setBorder(new TitledBorder("Результат"));
            this.setLayout(new BorderLayout());

            Dimension resultPanelDimension = new Dimension(350, bottomPanelDimension.height - 10);
            this.setSize(resultPanelDimension);
            this.setPreferredSize(resultPanelDimension);
            this.setMinimumSize(resultPanelDimension);
            this.setMaximumSize(resultPanelDimension);

            resultTableSP = new JScrollPane();
            resultTable = new JTable(new ResultTableModel());
            resultTableSP.setViewportView(resultTable);//создает область просмотра таблицы результатов
            resultTable.getTableHeader().setReorderingAllowed(false); // фиксация табличного пространства (запрет перемещения столбцов) таблицы результатов
            calculate = new JButton();

            this.add(resultTableSP, BorderLayout.CENTER);

            calculate.setAction(new AbstractAction("Вычислить>>") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calculate_handleAction(e);
                }
            });

            this.add(calculate, BorderLayout.SOUTH);

        }}, BorderLayout.EAST);
        /* ===================================== | RIGHT PANEL COMPONENTS END | ===================================== */

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void load_handleAction(ActionEvent e) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            String[][] result;
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("./saved_table.xml"));
            doc.getDocumentElement().normalize();
            NodeList row_list = doc.getElementsByTagName("row");
            result = new String[row_list.getLength()][];
            for (int row = 0; row < row_list.getLength(); row++) {
                Node row_node = row_list.item(row);
                if (row_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element row_element = (Element) row_node;
                    int row_element_id = (Integer.parseInt(row_element.getAttribute("id")) - 1);
                    NodeList cell_list = row_element.getElementsByTagName("cell");
                    result[row_element_id] = new String[cell_list.getLength()];
                    for (int cell = 0; cell < cell_list.getLength(); cell++) {
                        Node cell_node = cell_list.item(cell);
                        if (cell_node.getNodeType() == Node.ELEMENT_NODE) {
                            Element cell_element = (Element) cell_node;
                            int cell_element_id = (Integer.parseInt(cell_element.getAttribute("id")) - 1);
                            result[row_element_id][cell_element_id] = cell_element.getTextContent();
                        }
                    }
                }
            }
            mainTable_updateTM(this.initValuesTM(result));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.err.println("Can't load xml data: " + ex.getLocalizedMessage());
        }
    }

    private void save_handleAction(ActionEvent e) {
        final String[][] values = ((ValuesTableModel) mainTable.getModel()).getValues();
        try {
            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = dFact.newDocumentBuilder();
            Document doc = build.newDocument();
            Element root = doc.createElement("table");
            doc.appendChild(root);
            int rowN = 0;
            for (String[] row : values) {
                rowN++;
                Element rowElement = doc.createElement("row");
                rowElement.setAttribute("id", String.valueOf(rowN));
                int cellN = 0;
                for (String cell : row) {
                    cellN++;
                    Element cellElement = doc.createElement("cell");
                    cellElement.setAttribute("id", String.valueOf(cellN));
                    cellElement.setTextContent(cell);
                    rowElement.appendChild(cellElement);
                }
                root.appendChild(rowElement);
            }
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();
            aTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            aTransformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "2");
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            try {
                FileWriter fileWriter = new FileWriter("./saved_table.xml");
                StreamResult streamResult = new StreamResult(fileWriter);
                aTransformer.transform(domSource, streamResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (TransformerException | ParserConfigurationException ex) {
            System.err.println("Can't build the document: " + ex.getLocalizedMessage());
        }
    }

    private void calculate_handleAction(ActionEvent e) {

        int selectedRow = Integer.parseInt(rowNumberSelector.getValue().toString()) - 1,
                selectedColumn = Integer.parseInt(colNumberSelector.getValue().toString()) - 1;

        final String[][] values = ((ValuesTableModel) mainTable.getModel()).getValues();

        Integer
                summ_byTable = 0,
                summ_byCol = 0,
                summ_byRow = 0;
        int
                count4average_byTable = 0,
                count4average_byCol = 0,
                count4average_byRow = 0;
        Integer
                min_byTable = Integer.MAX_VALUE,
                min_byCol = Integer.MAX_VALUE,
                min_byRow = Integer.MAX_VALUE;
        Integer
                max_byTable = Integer.MIN_VALUE,
                max_byCol = Integer.MIN_VALUE,
                max_byRow = Integer.MIN_VALUE;

        for (int row = values.length - 1; -1 < row; row--) {
            for (int col = values[row].length - 1; -1 < col; col--) {
                try {
                    int val = Integer.parseInt(values[row][col]);

                    if (row == selectedRow) {
                        summ_byRow += val;
                        count4average_byRow = count4average_byRow + 1;
                        if (val < min_byRow) {
                            min_byRow = val;
                        }
                        if (val > max_byRow) {
                            max_byRow = val;
                        }
                    }

                    if (col == selectedColumn) {
                        summ_byCol += val;
                        count4average_byCol = count4average_byCol + 1;
                        if (val < min_byCol) {
                            min_byCol = val;
                        }
                        if (val > max_byCol) {
                            max_byCol = val;
                        }
                    }

                    summ_byTable += val;
                    count4average_byTable = count4average_byTable + 1;

                    if (val < min_byTable) {
                        min_byTable = val;
                    }
                    if (val > max_byTable) {
                        max_byTable = val;
                    }

                } catch (NumberFormatException ignored){}
            }
        }

        ResultTableModel resultTableModel = (ResultTableModel) resultTable.getModel();

        resultTableModel.setResultSumm(
                String.valueOf(summ_byRow),
                String.valueOf(summ_byCol),
                String.valueOf(summ_byTable)
        );
        resultTableModel.setResultAverage(
                count4average_byRow > 0 ? String.valueOf(summ_byRow / count4average_byRow) : "-",
                count4average_byCol > 0 ? String.valueOf(summ_byCol / count4average_byCol) : "-",
                count4average_byTable > 0 ? String.valueOf(summ_byTable / count4average_byTable) : "-"
        );
        resultTableModel.setResultMinimum(
                min_byRow != Integer.MAX_VALUE ? String.valueOf(min_byRow) : "-",
                min_byCol != Integer.MAX_VALUE ? String.valueOf(min_byCol) : "-",
                min_byTable != Integer.MAX_VALUE ? String.valueOf(min_byTable) : "-"
        );
        resultTableModel.setResultMaximum(
                max_byRow != Integer.MIN_VALUE ? String.valueOf(max_byRow) : "-",
                max_byCol != Integer.MIN_VALUE ? String.valueOf(max_byCol) : "-",
                max_byTable != Integer.MIN_VALUE ? String.valueOf(max_byTable) : "-"
        );

        resultTableModel.fireTableDataChanged();
        resultTable.updateUI();
    }

    private void generateTM_handleAction(ActionEvent e) {
        mainTable_updateTM(initValuesTM(Integer.parseInt(rowCountSelector.getValue().toString()), Integer.parseInt(colCountSelector.getValue().toString())));
    }

    private void mainTable_handleSelectionEvent(ListSelectionEvent e) {
        colNumberSelector.setEnabled(false);
        rowNumberSelector.setEnabled(false);

        int selectedRow = mainTable.getSelectedRow();
        rowNumberSelector.setValue(selectedRow >= 0 ? selectedRow + 1 : 0);

        int selectedColumns = mainTable.getSelectedColumn();
        colNumberSelector.setValue(selectedColumns >= 0 ? selectedColumns + 1 : 0);

        colNumberSelector.setEnabled(true);
        rowNumberSelector.setEnabled(true);
    }

    private void mainTable_updateTM(TableModel tm) {
        mainTable.setEnabled(false);
        mainTable.setModel(tm);
        mainTable.updateUI();
        mainTable.setEnabled(true);
    }

}