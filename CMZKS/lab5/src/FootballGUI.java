import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FootballGUI implements Runnable {

    private DefaultTableModel model;
    private JFrame frame;
    private JTable table;
    private String[][] data;

    // Launch the application.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new FootballGUI());
    }

    public FootballGUI() {
        String[] columnNames = { "First Name", "Last Name", "Sport", 
                "# of Years", "Vegetarian" };
        this.model = new DefaultTableModel();

        for (String s : columnNames) {
            model.addColumn(s);
        }

        this.data = new String[][] { { "Kathy", "Smith", "Snowboarding", "5", "false" },
                { "John", "Doe", "Rowing", "3", "true" }, 
                { "Sue", "Black", "Knitting", "2", "false" },
                { "Jane", "White", "Speed reading", "20", "true" }, 
                { "Joe", "Brown", "Pool", "10", "false" } };
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

        JButton displayTeams = new JButton("Display all teams");
        displayTeams.addActionListener(new MyActionListener());
        panel.add(displayTeams, gbc);

        gbc.gridy++;
        JButton goalSort = new JButton("Sort list by goals");
        panel.add(goalSort, gbc);

        gbc.gridy++;
        JButton winSort = new JButton("Sort list by most wins");
        panel.add(winSort, gbc);

        gbc.gridy++;
        JButton randomMatch = new JButton("Generate random match");
        panel.add(randomMatch, gbc);

        gbc.gridy++;
        JButton displayMatches = new JButton("Display all played matches");
        panel.add(displayMatches, gbc);

        gbc.gridy++;
        JButton btnEnter = new JButton("Search for a match");
        panel.add(btnEnter, gbc);

        return panel;
    }

    public class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int count = model.getRowCount();
            for (int i = 0; i < count; i++) {
                model.removeRow(0);
            }
            
            for (int i = 0; i < data.length; i++) {
                model.addRow(data[i]);
            }
        }

    }
    
}