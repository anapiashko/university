import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FootballGUI implements Runnable {

    private DefaultTableModel model;
    private JFrame frame;
    private JTable table;
    private Map<String, String> data;
    private JTextField textFieldA;
    private JTextField textFieldB;

    static Main2 main2 = new Main2();

    // Launch the application.
    public static void main(String[] args) {
        main2.start();
        SwingUtilities.invokeLater(new FootballGUI());
    }

    public FootballGUI() {
        String[] columnNames = {" ", "AD - BC = 1"};
        this.model = new DefaultTableModel();

        for (String s : columnNames) {
            model.addColumn(s);
        }

        this.data = new LinkedHashMap<String, String>() {{
            put(" ", "");
            put("N", Main2.N.toString());
            put("e1", String.valueOf(Main2.e1));
            put("e2", String.valueOf(Main2.e2));
            put("   ", "");
            put("C1", Main2.C1.toString());
            put("C2", Main2.C2.toString());
            put("r", main2.getR().toString());
            put("s", main2.getS().toString());
            put("", "e1*r - e2*s");
            put("C1^r", main2.getC1r().toString());
            put("C2^(-s)", main2.getC2s().toString());
            put("C1^r * C2^(-s)", main2.getC2s().toString());
            put("m", main2.getM().toString());
            put("mModN", main2.getMModN().toString());
        }};
    }

    // Create the application.
    @Override
    public void run() {
        frame = new JFrame("Football GUI");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(createTablePanel(), BorderLayout.CENTER);
        frame.add(createButtonPanel(), BorderLayout.AFTER_LINE_ENDS);

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel();

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0d;

        JLabel labelA = new JLabel("A");
        panel.add(labelA, gbc);

        gbc.gridy++;
        textFieldA = new JTextField(16);
        panel.add(textFieldA, gbc);

        gbc.gridy++;
        JLabel labelB = new JLabel("B");
        panel.add(labelB, gbc);

        gbc.gridy++;
        textFieldB = new JTextField(16);
        panel.add(textFieldB, gbc);

        gbc.gridy++;
        JLabel labelc = new JLabel("C");
        panel.add(labelc, gbc);

        gbc.gridy++;
        JTextField textFieldc = new JTextField(16);
        panel.add(textFieldc, gbc);

        gbc.gridy++;
        JLabel labelD = new JLabel("D");
        panel.add(labelD, gbc);

        gbc.gridy++;
        JTextField textFieldD = new JTextField(16);
        panel.add(textFieldD, gbc);

        gbc.gridy++;
        JButton displayTeams = new JButton("A*D - B*C = N");
        displayTeams.addActionListener(new MyActionListener());
        panel.add(displayTeams, gbc);

        gbc.gridy++;
        JButton goalSort = new JButton("Sort list by goals");
        panel.add(goalSort, gbc);

        return panel;
    }

    public class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
//            main2.start(Integer.parseInt(textFieldA.toString()), Integer.parseInt(textFieldB.toString()));
            int count = model.getRowCount();
            for (int i = 0; i < count; i++) {
                model.removeRow(0);
            }
            
//            for (int i = 0; i < data.size(); i++) {
//                model.addRow(data.entrySet());
//            }

            for (Map.Entry<String, String> entry : data.entrySet()) {
                model.addRow(new Object[]{entry.getKey(),entry.getValue()});
            }
        }
    }
    
}