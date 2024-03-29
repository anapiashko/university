package text.editor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

import static javax.swing.GroupLayout.Alignment.CENTER;

public class TextEditor extends JFrame {

    private static final String APP_PATH = "/home/anastasiya/university/CCP/lab2_3";

    private JTextArea textArea;
    private boolean isSaved = false;
    private String currentFileLocation = "default";

    public static void main(String[] args) {
        new TextEditor();
    }

    public TextEditor() {

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newFileMenuItem = new JMenuItem("New");
        JMenuItem openFileMenuItem = new JMenuItem("Open");
        JMenuItem saveFileMenuItem = new JMenuItem("Save");
        JMenuItem saveAsFileMenuItem = new JMenuItem("Save As");

        newFileMenuItem.addActionListener((e) -> {
            setTitle("New Document");
            textArea.setText("");
            isSaved = false;
        });
        openFileMenuItem.addActionListener((e) -> {
            this.isSaved = true;
            openFile();
        });
        saveFileMenuItem.addActionListener((e) -> {
            if (isSaved) {
                save();
            } else {
                saveAs();
            }
        });
        saveAsFileMenuItem.addActionListener((e) -> {
            saveAs();
        });

        fileMenu.add(newFileMenuItem);
        fileMenu.add(openFileMenuItem);
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(saveAsFileMenuItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutEditMenuItem = new JMenuItem("Cut");
        JMenuItem copyEditMenuItem = new JMenuItem("Copy");
        JMenuItem pasteEditMenuItem = new JMenuItem("Paste");
        JMenuItem selectAllEditMenuItem = new JMenuItem("Select All");
        cutEditMenuItem.addActionListener((e) -> {
            textArea.cut();
        });
        copyEditMenuItem.addActionListener((e) -> {
            textArea.copy();
        });
        pasteEditMenuItem.addActionListener((e) -> {
            textArea.paste();
        });
        selectAllEditMenuItem.addActionListener((e) -> {
            textArea.selectAll();
        });
        editMenu.add(cutEditMenuItem);
        editMenu.add(copyEditMenuItem);
        editMenu.add(pasteEditMenuItem);
        editMenu.add(selectAllEditMenuItem);

        JMenu formatMenu = new JMenu("Format");
        JMenuItem colorFormatMenuItem = new JMenuItem("Color");
        JMenuItem fontFormatMenuItem = new JMenuItem("Font");
        colorFormatMenuItem.addActionListener((e) -> {
            Color color = JColorChooser.showDialog(null, "Choose a color", Color.black);
            textArea.setForeground(color);
        });
        fontFormatMenuItem.addActionListener((e) -> {
            AboutDialog aboutDialog = new AboutDialog(this);
            aboutDialog.setVisible(true);
        });
        formatMenu.add(colorFormatMenuItem);
        formatMenu.add(fontFormatMenuItem);

        JMenuItem closeMenu = new JMenuItem("Close");
        closeMenu.addActionListener(e -> {
            if (isSaved || textArea.getText().equals("")) {
                System.exit(0);
            } else {
                saveAs();
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(closeMenu);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        setJMenuBar(menuBar);
        setTitle("New Document");
        add(textArea);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void openFile() {
        JFileChooser jFileChooser = new JFileChooser(APP_PATH);
        // add filters
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        jFileChooser.addChoosableFileFilter(xmlFilter);
        jFileChooser.setFileFilter(xmlFilter);

        int r = jFileChooser.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            currentFileLocation = jFileChooser.getSelectedFile().getAbsolutePath();
            this.setTitle(currentFileLocation);
            File file = new File(currentFileLocation);
            try {
                String s1 = "", sl = "";
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);
                sl = br.readLine();
                s1 = br.readLine();
                while (s1 != null) {
                    System.out.println(sl);
                    sl = sl + "\n" + s1;
                    s1 = br.readLine();
                }
                br.close();
                textArea.setText(sl);
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(this, evt.getMessage());
            }
        }
    }

    private void saveAs() {
        JFileChooser jFileChooser = new JFileChooser(APP_PATH);

        // add filters
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        jFileChooser.addChoosableFileFilter(xmlFilter);
        jFileChooser.setFileFilter(xmlFilter);

        int r = jFileChooser.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {

            currentFileLocation = jFileChooser.getSelectedFile().getAbsolutePath();
            if (!currentFileLocation.endsWith(".xml")) {
                currentFileLocation += ".xml";
            }

            this.setTitle(currentFileLocation);
            File fi = new File(currentFileLocation);
            try {
                FileWriter wr = new FileWriter(fi, false);
                BufferedWriter w = new BufferedWriter(wr);
                w.write(textArea.getText());
                w.flush();
                w.close();
                this.isSaved = true;
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(this, evt.getMessage());
            }
        }
    }

    private void save() {
        File fi = new File(currentFileLocation);
        try {
            FileWriter wr = new FileWriter(fi, false);
            BufferedWriter w = new BufferedWriter(wr);
            w.write(textArea.getText());
            w.flush();
            w.close();
            this.isSaved = true;
        } catch (Exception evt) {
            JOptionPane.showMessageDialog(this, evt.getMessage());
        }
    }

    class AboutDialog extends JDialog {

        private JComboBox fontBox;

        public AboutDialog(Frame parent) {
            super(parent);
            initUI();
        }

        private void initUI() {

            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            fontBox = new JComboBox(fonts);
            fontBox.addActionListener(e -> {
                textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
            });
            fontBox.setSelectedItem("Arial");

            JButton okBtn = new JButton("OK");
            okBtn.addActionListener(event -> {

                dispose();
            });

            createLayout(fontBox, okBtn);

            setModalityType(ModalityType.APPLICATION_MODAL);

            setTitle("About Notes");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(getParent());
        }

        private void createLayout(JComponent... arg) {

            Container pane = getContentPane();
            GroupLayout gl = new GroupLayout(pane);
            pane.setLayout(gl);

            gl.setAutoCreateContainerGaps(true);
            gl.setAutoCreateGaps(true);

            gl.setHorizontalGroup(gl.createParallelGroup(CENTER)
                            .addComponent(arg[0])
                            .addComponent(arg[1])
//                    .addComponent(arg[2])
                            .addGap(200)
            );

            gl.setVerticalGroup(gl.createSequentialGroup()
                            .addGap(30)
                            .addComponent(arg[0])
                            .addGap(20)
                            .addComponent(arg[1])
                            .addGap(20)
//                    .addComponent(arg[2])
                            .addGap(30)
            );

            pack();
        }
    }
}