import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Window3 extends JFrame {

    private JLabel label1 = new JLabel("Field for input: ");
    private JTextField field1 = new JTextField("");
    private JButton button1 = new JButton("Enter message");

    private static final int INFO_PART = 11;
    private static final int BYTE_SIZE = 8;

    public Window3() {
        super.setTitle("Window");
        this.setSize(700, 250);
        
        field1.setColumns(20);
        button1.addActionListener(this::buttonClick);
        this.getContentPane().setLayout(new FlowLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.getContentPane().add(label1);
        this.getContentPane().add(field1);
        this.getContentPane().add(button1);
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void buttonClick(ActionEvent e) {
        String var = field1.getText();
        field1.setText("");

        System.out.println("JTextField value: " + var);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < var.length(); i++) {
            int code = var.charAt(i);
            String binary = Integer.toBinaryString(code);
            while (binary.length() != BYTE_SIZE) {
                binary = "0" + binary;
            }
            stringBuilder.append(binary);
        }

        String[] strings = new String[stringBuilder.length() / INFO_PART + 1];
        for (int i = 0, j = 0; i < stringBuilder.length() - INFO_PART; i += INFO_PART, j++) {
            strings[j] = stringBuilder.substring(i, i + INFO_PART);
        }
        int countAdd = 0;
        strings[strings.length - 1] = stringBuilder.substring(stringBuilder.length() - stringBuilder.length() % INFO_PART);
        while (strings[strings.length - 1].length() != INFO_PART) {
            strings[strings.length - 1] = strings[strings.length - 1] + "0";
            countAdd++;
        }

        int[][] words = new int[strings.length][INFO_PART];
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < INFO_PART; j++) {
                words[i][j] = Integer.parseInt(Character.toString(strings[i].charAt(j)));
            }
        }

        Integer[][] recoveredWords = Hamming3.doMain(words);

        StringBuilder recoveredBinaryString = new StringBuilder();
        for (int i = 0; i < recoveredWords.length; i++) {
            recoveredBinaryString.append(Arrays.toString(recoveredWords[i]).replaceAll("\\[|]|,|\\s", ""));
        }

        recoveredBinaryString.delete(recoveredBinaryString.length() - countAdd, recoveredBinaryString.length());

        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i <= recoveredBinaryString.length() - BYTE_SIZE; i += BYTE_SIZE) {
            String nextByte = recoveredBinaryString.substring(i, i + BYTE_SIZE);
            int parseInt = Integer.parseInt(nextByte, 2);
            resultString.append((char) parseInt);
        }

        JOptionPane.showMessageDialog(null, "Recovered message : " + resultString);
    }

    public static void main(String[] args) {
        new Window3();
    }
}
