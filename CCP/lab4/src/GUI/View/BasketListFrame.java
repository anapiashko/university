package GUI.View;

import Logic.Basket;
import Logic.Commodity;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

public class BasketListFrame extends JFrame {

    private JPanel mainPanel = new JPanel();
    private JButton deleteButton = new JButton("Удалить");
    private JButton showCheckButton = new JButton("Показать чек");
    private JButton exportCheckButton = new JButton("Экспорт чека в файл");
    private JButton buyCommodityButton = new JButton("Купить товар");
    private DefaultListModel<String> model = new DefaultListModel<>();
    private JList<String> choice;
    private Basket basket;

    public BasketListFrame(Basket basket) {
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        this.basket = basket;
        updateList();
        mainPanel.add(choice, BorderLayout.CENTER);
        JPanel grid = new JPanel(new GridLayout(1, 3, 5, 0));
        grid.add(deleteButton);
        grid.add(showCheckButton);
        grid.add(exportCheckButton);
        grid.add(buyCommodityButton);
        mainPanel.add(grid, BorderLayout.SOUTH);
        deleteButton.addActionListener(new DeleteActionListener());
        showCheckButton.addActionListener(new ShowCheckActionListener());
        buyCommodityButton.addActionListener(new ListenerForButtonBuyCommodity());
        exportCheckButton.addActionListener(new ListenerForButtonExportCheck());
        setSize(500, 250);
        setResizable(false);
        setTitle("Корзина");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateList() {
        try {
            for (Commodity commodity : basket.getListCommodity()) {
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
                DefaultListModel<String> model = (DefaultListModel<String>) choice.getModel();
                basket.getListCommodity().remove(choice.getSelectedIndex());
                model.remove(choice.getSelectedIndex());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public class ShowCheckActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String totalItemList = getTotalItemList();
            JOptionPane.showMessageDialog(null, totalItemList);
        }
    }

    class ListenerForButtonBuyCommodity implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DefaultListModel<String> model = (DefaultListModel<String>) choice.getModel();
                basket.getListCommodity().remove(choice.getSelectedIndex());
                model.remove(choice.getSelectedIndex());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class ListenerForButtonExportCheck implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String totalItemList = getTotalItemList();
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(".\\"));
            int retrival = chooser.showSaveDialog(null);
            if (retrival == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt")) {
                    fw.write(totalItemList);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    private String getTotalItemList() {
        int totalPrize = 0;
        StringBuilder totalItemList = new StringBuilder();

        for (Commodity commodity : basket.getListCommodity()) {
            totalPrize += commodity.getPrice();
            totalItemList.append(commodity.getName()).append("    ").append(commodity.getPrice()).append("\n");
        }
        totalItemList.append("\n").append("Total Prize : ").append(totalPrize).append("\n\n");
        return totalItemList.toString();
    }
}