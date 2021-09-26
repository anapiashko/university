import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        JFrame mainWindow = new JFrame();
        mainWindow.setSize(650, 320);
        mainWindow.setTitle("Noise Reduction");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        JTextField textField = new JTextField(FilterImage.folder + "\\images\\india.jpg");
        textField.setPreferredSize(new Dimension(400, 30));

        JButton load = new JButton("Choose image");
        load.setPreferredSize(new Dimension(115,30));
        load.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            c.setCurrentDirectory(new File(FilterImage.folder));
            int rVal = c.showOpenDialog(mainWindow);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                if (c.getSelectedFile().toString().contains(".jpg") || c.getSelectedFile().toString().contains(".png")) {
                    textField.setText(c.getSelectedFile().toString());
                    FilterImage.folder = c.getCurrentDirectory().toString();
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Pick image in jpg or png format",
                            "Error", JOptionPane.OK_OPTION);
                }

            }
        });

        JTextField sizeXField = new JTextField("3", 5);
        JTextField sizeYField = new JTextField("1", 5);
        sizeYField.setEnabled(false);

        JCheckBox check = new JCheckBox("both");
        JButton start = new JButton("Start");
        start.setPreferredSize(new Dimension(115,30));

        JLabel imageLabel = new JLabel("Image Path: ");
        imageLabel.setPreferredSize(new Dimension(90,50));
        imageLabel.setFont(new Font(imageLabel.getFont().getFontName(), Font.PLAIN, 15));
        mainPanel.add(imageLabel);
        mainPanel.add(textField);
        mainPanel.add(load);

        mainPanel.add(new JLabel("X (N): "));
        mainPanel.add(sizeXField);
        mainPanel.add(new JLabel("Y: "));
        mainPanel.add(sizeYField);
        mainPanel.add(check);
        mainPanel.add(start);

        start.addActionListener(e -> EventQueue.invokeLater(() -> {
            FilterImage frame = new FilterImage(textField.getText(), sizeXField.getText(), sizeYField.getText(), check.isSelected());
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent ev) {
                    frame.setVisible(false);
                    frame.dispose();
                }
            });
            frame.setVisible(true);
        }));

        mainWindow.add(mainPanel);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }
}
