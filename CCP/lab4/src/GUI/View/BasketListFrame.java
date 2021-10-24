package GUI.View;

import Logic.Basket;
import Logic.Commodity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BasketListFrame extends JFrame {

    private JPanel mainPanel = new JPanel();
    private JButton deleteButton = new JButton("Удалить");
    private JButton showCheckButton = new JButton("Показать чек");
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> choice;
    private Basket basket;

    public BasketListFrame(Basket basket) {
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        this.basket = basket;
        updateList();
        mainPanel.add(choice, BorderLayout.CENTER);
        JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0) );
        grid.add(deleteButton);
        grid.add(showCheckButton);
        mainPanel.add(grid, BorderLayout.SOUTH);
        deleteButton.addActionListener(new DeleteActionListener());
        showCheckButton.addActionListener(new ShowCheckActionListener());
        setSize(400, 200);
        setResizable(false);
        setTitle("Delete article");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public  void updateList() {
        try {
            for (Commodity commodity: basket.getListCommodity()){
                model.addElement(commodity.getName() + "    " + commodity.getPrice());
            }
            choice = new JList<>(model);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public class DeleteActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DefaultListModel<String> model = (DefaultListModel<String>)choice.getModel();
                model.remove(choice.getSelectedIndex());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public class ShowCheckActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int totalPrize = 0;
            StringBuilder totalItemList = new StringBuilder();
            try {
                for (Commodity commodity: basket.getListCommodity()){
                    totalPrize += commodity.getPrice();
                    totalItemList.append(commodity.getName()).append("    ").append(commodity.getPrice()).append("\n");
                }
                totalItemList.append("\n").append("Total Prize : ").append(totalPrize).append("\n\n");
                JOptionPane.showMessageDialog(null, totalItemList);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}