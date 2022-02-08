import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Window3 extends JFrame {
    private JLabel label1 = new JLabel("Поле для сообщения: ");
    private JTextField field1 = new JTextField("");
    private JButton button1 = new JButton("Передалась переменной");

    public Window3() {
        super.setTitle("Window");
        this.setSize(800, 500);     
        
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
            while (binary.length() != 8) {
                binary = "0" + binary;
            }
            stringBuilder.append(binary);
        }

        String[] strings = new String[stringBuilder.length() / 11 + 1];
        for (int i = 0, j = 0; i < stringBuilder.length() - 11; i += 11, j++) {
            strings[j] = stringBuilder.substring(i, i + 11);
        }
        int countAdd = 0;
        strings[strings.length - 1] = stringBuilder.substring(stringBuilder.length() - stringBuilder.length() % 11);
        while (strings[strings.length - 1].length() != 11) {
            strings[strings.length - 1] = strings[strings.length - 1] + "0";
            countAdd++;
        }

        int[][] words = new int[strings.length][11];
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < 11; j++) {
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
        for (int i = 0; i <= recoveredBinaryString.length() - 8; i += 8) {
            String nextByte = recoveredBinaryString.substring(i, i + 8);
            int parseInt = Integer.parseInt(nextByte, 2);
            resultString.append((char) parseInt);
        }

        JOptionPane.showMessageDialog(null, "Recovered message : " + resultString);
    }

    public static void main(String[] args) {
        new Window3();
    }
}
