package GUI.Controller;

import GUI.Model.Shop;
import GUI.View.MainWindow;
import GUI.View.TopUpMoneyFrame;
import Logic.Purse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControllerTopUpMoneyFrame {


    private Shop model;
    private TopUpMoneyFrame viewTopUpMoney;
    private MainWindow mainView;
    private JLabel jLabelFromMainViewWithAmountMoney;
    private Purse purse;
    private JButton jButtonFromTopUpMoneyOk;
    private JTextField jTextFieldWithAmountMoney;


    public ControllerTopUpMoneyFrame(Shop shop, MainWindow mainView) {
        viewTopUpMoney = new TopUpMoneyFrame();
        viewTopUpMoney.setLocationRelativeTo(null);
        this.model = shop;
        this.mainView = mainView;
        this.jLabelFromMainViewWithAmountMoney = mainView.getLabelWithAmountMoney();
        //  this.purse = model.getPurse();
        this.jButtonFromTopUpMoneyOk = viewTopUpMoney.getTopUpMoneyButton();
        this.jTextFieldWithAmountMoney = viewTopUpMoney.getJTextFieldWithAmountMoney();
    }


    public void execute() {
        jButtonFromTopUpMoneyOk.addActionListener(new ListenerForButtonOk());
        jTextFieldWithAmountMoney.addKeyListener(new ListenerForJTextField());
    }


    class ListenerForButtonOk implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (jTextFieldWithAmountMoney.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Введите сумму которую хотите " +
                        "положить на счёт!");
            } else {
                int amountMoneyForTopUpPurse = viewTopUpMoney.getTextFromCountMoneyField();
                model.purse.topUpAmountMoney(amountMoneyForTopUpPurse);
                mainView.setJLabelWithAmountMoney(model.purse.getAmountMoney());
                viewTopUpMoney.dispose();
            }
        }
    }


    class ListenerForJTextField implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            char character = e.getKeyChar();
            if ((character < '0') || (character > '9')) {
                e.consume();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }


}

