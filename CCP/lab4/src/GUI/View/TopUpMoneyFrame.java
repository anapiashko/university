package GUI.View;

import javax.swing.*;
import java.awt.*;

public class TopUpMoneyFrame extends JFrame {


    private TopPanel topPanel;
    private JButton buttonOk;


    public JButton getTopUpMoneyButton() {
        return buttonOk;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }

    public int getTextFromCountMoneyField() {
        return Integer.parseInt(topPanel.countMoneyField.getText());
    }

    public JTextField getJTextFieldWithAmountMoney() {
        return topPanel.countMoneyField;
    }


    public TopUpMoneyFrame() {
        setLocationByPlatform(true);
        setLayout(new GridLayout(2, 1, 15, 5));
        topPanel = new TopPanel();
        buttonOk = new JButton("ОК");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(topPanel);
        add(buttonOk);
        JRootPane rootPane = SwingUtilities.getRootPane(buttonOk);
        rootPane.setDefaultButton(buttonOk);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }


    class TopPanel extends JPanel {
        private JLabel inputCountLabel;
        private JTextField countMoneyField;

        public TopPanel() {
            setLayout(new GridLayout(2, 1, 5, 5));
            inputCountLabel = new JLabel("Введите сумму ");
            inputCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
            inputCountLabel.setVerticalAlignment(SwingConstants.CENTER);
            countMoneyField = new JTextField();
            add(inputCountLabel);
            add(countMoneyField);
        }
    }
}

