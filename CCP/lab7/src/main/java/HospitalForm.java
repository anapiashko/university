package main.java;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HospitalForm extends JFrame {

    Integer selectedTab = 0;
    Hospital database;
    JTabbedPane tabbedPane = new JTabbedPane();
    ArrayList<JTable> tables;
    String[] tabs = {"Patients", "Records"};

    JPanel panel = new JPanel(new BorderLayout());

    public HospitalForm() {
        tables = new ArrayList<>();
        database = new Hospital();
        createWindow();
        tables.add(createPatientsTable());
        tables.add(createRecordsTable());


        for (int i = 0; i < tables.size(); i++) {
            JScrollPane scroll = new JScrollPane(tables.get(i));
            tables.get(i).setFillsViewportHeight(true);
            tabbedPane.add(tabs[i], scroll);
        }
        panel.add(bottomPanel(), BorderLayout.SOUTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addChangeListener(e -> selectedTab = ((JTabbedPane) e.getSource()).getSelectedIndex());
        add(panel);
        setVisible(true);
    }

    private void createWindow() {
        setTitle("Hospital");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void update() {
        remove(panel);
        int selectedI = tabbedPane.getSelectedIndex();
        tabbedPane.removeAll();
        panel = new JPanel(new BorderLayout());
        for (int i = 0; i < tables.size(); i++) {
            JScrollPane scroll = new JScrollPane(tables.get(i));
            tables.get(i).setFillsViewportHeight(true);
            tabbedPane.add(tabs[i], scroll);
        }
        panel.add(bottomPanel(), BorderLayout.SOUTH);
        panel.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addChangeListener(e -> selectedTab = ((JTabbedPane) e.getSource()).getSelectedIndex());
        add(panel);
        revalidate();
        tabbedPane.setSelectedIndex(selectedI);
        repaint();
    }

    private JTable createPatientsTable() {
        return createTable("SELECT * FROM patients");
    }

    private JTable createRecordsTable() {
        return createTable("SELECT * FROM records");
    }

    private JTable createTable(String query) {
        JTable table = null;
        try {
            ResultSet resultSet = database.statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String[] columnNames = new String[resultSetMetaData.getColumnCount()];

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
                columnNames[i - 1] = resultSetMetaData.getColumnLabel(i);

            ArrayList<ArrayList<Object>> dataList = new ArrayList<>();
            for (int i = 0; resultSet.next(); i++) {
                dataList.add(new ArrayList<>());
                for (String value : columnNames) {
                    dataList.get(i).add(resultSet.getObject(value));
                }
            }


            Object[][] data = new Object[dataList.size()][dataList.get(0).size()];
            for (int i = 0; i < dataList.size(); i++)
                for (int j = 0; j < dataList.get(0).size(); j++)
                    data[i][j] = dataList.get(i).get(j);

            table = new JTable(data, columnNames);
            table.setSelectionMode(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return table;
    }

    private JPanel bottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            editForm(-1, -1);
        });
        JButton edit = new JButton("Edit");
        edit.addActionListener(e -> {
            editForm((Integer) tables.get(selectedTab).getValueAt(tables.get(selectedTab).getSelectedRow(), 0), tables.get(selectedTab).getSelectedRow());
        });

        JButton remove = new JButton("Remove");
        remove.addActionListener(e -> {
            switch (selectedTab) {
                case 0: {
                    database.removeNote("patients", (Integer) tables.get(selectedTab).getValueAt(tables.get(selectedTab).getSelectedRow(), 0));
                    tables.set(0, createPatientsTable());
                    update();
                    break;
                }
                case 1: {
                    database.removeNote("records", (Integer) tables.get(selectedTab).getValueAt(tables.get(selectedTab).getSelectedRow(), 0));
                    tables.set(1, createRecordsTable());
                    update();
                    break;
                }
            }
        });
        panel.add(addButton);
        panel.add(edit);
        panel.add(remove);
        return panel;
    }

    public void func(JPanel p, JComponent c, Integer x, Integer y, Integer w, Integer h, boolean b, GridBagConstraints gbc) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.fill = b ? GridBagConstraints.HORIZONTAL : GridBagConstraints.VERTICAL;
        p.add(c, gbc);
    }

    private void editForm(Integer index, Integer row) {

        JFrame frame = new JFrame();
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel panel = new JPanel(new BorderLayout());

        JPanel p1 = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTH;

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        switch (selectedTab) {
            // Drivers
            case 0: {
                JTextField firstName = index.equals(-1) ? new JTextField(10) : new JTextField(tables.get(selectedTab).getValueAt(row, 1).toString(), 10);
                func(p1, new JLabel("First name"), 0, 0, 1, 1, true, gbc);
                func(p1, firstName, 1, 0, 4, 1, true, gbc);

                JTextField secondName = index.equals(-1) ? new JTextField(10) : new JTextField(tables.get(selectedTab).getValueAt(row, 2).toString(), 10);
                func(p1, new JLabel("Second name"), 0, 1, 1, 1, true, gbc);
                func(p1, secondName, 1, 1, 4, 1, false, gbc);

                JTextField diagnose = index.equals(-1) ? new JTextField(10) : new JTextField(tables.get(selectedTab).getValueAt(row, 3).toString(), 10);
                func(p1, new JLabel("Diagnose"), 0, 2, 1, 1, true, gbc);
                func(p1, diagnose, 1, 2, 4, 1, false, gbc);

                save.addActionListener(e -> {
                    try {
                        if (firstName.getText().equals("") || secondName.getText().equals("") || diagnose.getText().equals("")) {
                            throw new Exception();
                        }
                        if (index.equals(-1)) {
                            database.addNote("patients", new Object[]{
                                    firstName.getText(),
                                    secondName.getText(),
                                    diagnose.getText()
                            });
                        } else {
                            database.updateNote("patients", new Object[]{
                                    firstName.getText(),
                                    secondName.getText(),
                                    diagnose.getText()
                            }, index);
                        }
                        frame.dispose();
                        frame.setVisible(false);
                        tables.set(0, createPatientsTable());
                        update();
                    } catch (Exception generalException) {
                        JOptionPane.showMessageDialog(this, "Empty fields", "Error", JOptionPane.OK_OPTION);
                    }
                });
                cancel.addActionListener(e -> {
                    frame.dispose();
                    frame.setVisible(false);
                });
                break;
            }
            // Records
            case 1: {
                LinkedHashMap<String, Integer> drivers = new LinkedHashMap<>();

                JComboBox patient = new JComboBox();

                try {
                    ResultSet resultSet = database.statement.executeQuery("SELECT * FROM patients");
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    String[] columnNames = new String[resultSetMetaData.getColumnCount()];

                    for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                        columnNames[i - 1] = resultSetMetaData.getColumnLabel(i);
                    }
                    columnNames = new String[]{columnNames[0], columnNames[2]};
                    for (int i = 0; resultSet.next(); i++) {
                        drivers.put(resultSet.getString(columnNames[1]), resultSet.getInt(columnNames[0]));
                        patient.addItem(resultSet.getString(columnNames[1]));
                    }

                } catch (SQLException e) {
                    System.out.println("Records table, getting initial content error : " + e.getMessage());
                }

                if (!index.equals(-1)) {
                    patient.setSelectedItem(tables.get(selectedTab).getValueAt(row, 3).toString());
                }
                func(p1, new JLabel("Patient"), 0, 2, 1, 1, true, gbc);
                func(p1, patient, 1, 2, 4, 1, false, gbc);

                JTextField date = index.equals(-1) ? new JTextField("2021-01-01", 10) : new JTextField(tables.get(selectedTab).getValueAt(row, 2).toString(), 10);
                func(p1, new JLabel("Date"), 0, 3, 1, 1, true, gbc);
                func(p1, date, 1, 3, 4, 1, false, gbc);

                JTextField description = index.equals(-1) ? new JTextField(10) : new JTextField(tables.get(selectedTab).getValueAt(row, 3).toString(), 10);
                func(p1, new JLabel("Description"), 0, 0, 1, 1, true, gbc);
                func(p1, description, 1, 0, 4, 1, true, gbc);

                save.addActionListener(e -> {
                    try {
                        if (description.getText().equals("") || date.getText().equals("")) {
                            throw new Exception();
                        }
                        if (index.equals(-1)) {
                            database.addNote("records", new Object[]{
                                    description.getText(),
                                    drivers.get(patient.getSelectedItem()),
                                    date.getText()
                            });
                        } else {
                            database.updateNote("records", new Object[]{
                                    description.getText(),
                                    drivers.get(patient.getSelectedItem()),
                                    date.getText()
                            }, index);
                        }
                        frame.dispose();
                        frame.setVisible(false);
                        tables.set(1, createRecordsTable());
                        update();
                    } catch (NumberFormatException exception) {
                        JOptionPane.showMessageDialog(this, "Fields must be number", "Error", JOptionPane.OK_OPTION);
                    } catch (Exception generalException) {
                        JOptionPane.showMessageDialog(this, "Empty fields", "Error", JOptionPane.OK_OPTION);
                    }
                });
                cancel.addActionListener(e -> {
                    frame.dispose();
                    frame.setVisible(false);
                });
                break;
            }
        }

        p2.add(save);
        p2.add(cancel);

        p1.setSize(700, 700);
        panel.add(p1, BorderLayout.CENTER);
        panel.add(p2, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HospitalForm();
    }
}
