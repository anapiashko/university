package GUI.View;

import Logic.Commodity;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;


public class MainWindow extends JFrame {

    private PursePanel pursePanel;
    private CommodityPanel commodityPanel;
    private final ImageIcon purseIcon = new ImageIcon("../pursePicture.png");
    private final Image shopIcon = new ImageIcon("../imageShop.png").getImage();
    private TopUpMoneyFrame topUpMoneyFrame;
    private JButton buyCommodityButton;
    private JButton addToBasketButton;
    private JButton topUpMoney;
    private JLabel moneyInPurseJLabel;
    private JPanel lowCommodityPanel;
    private JComboBox listCommodityComboBox;


    public JFrame getTopUpMoneyFrame() {
        return topUpMoneyFrame;
    }

    public JLabel getLabelWithAmountMoney() {
        return moneyInPurseJLabel;
    }

    public JButton getTopUpMoneyButton() {
        return topUpMoney;
    }

    public JLabel getMoneyInPurseJLabel() {
        return moneyInPurseJLabel;
    }

    public JButton getButtonBuyCommodity() {
        return buyCommodityButton;
    }

    public JComboBox getJComboBoxWithListCommodity() {
        return listCommodityComboBox;
    }

    public JButton getJButtonCheckQuality() {
        return addToBasketButton;
    }


    public void setJLabelWithAmountMoney(int newAmountMoney) {
        moneyInPurseJLabel.setText("Баланс кошелька: " + newAmountMoney);
    }

    public void setTopUpMoneyFrame(TopUpMoneyFrame topUpMoneyFrame) {
        this.topUpMoneyFrame = topUpMoneyFrame;
    }

    public void setJComboBoxWithListCommodity(List<Commodity> listCommodity) {
        listCommodityComboBox = createComboBox(listCommodity);
    }


    public MainWindow(List<Commodity> listCommodity, int countMoneyInPurse) {
        setLayout(new GridLayout(1, 2, 15, 5));
        setTitle("Магазин");
        setIconImage(shopIcon);
        pursePanel = new PursePanel(countMoneyInPurse);
        commodityPanel = new CommodityPanel(listCommodity);
        add(pursePanel);
        add(commodityPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }


    class PursePanel extends JPanel {

        public PursePanel(int countMoneyInPurse) {
            setLayout(new BorderLayout());
            //устанавливаем отступы по 10 пикселей со всех сторон для PurseFrame
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            topUpMoney = new JButton("Пополнить кошелёк");
            moneyInPurseJLabel = new JLabel("Баланс кошелька: " + countMoneyInPurse);
            //создаём рамку и добавляем её к JLabel
            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            moneyInPurseJLabel.setBorder(border);
            //добавляем иконку кошелька
            moneyInPurseJLabel.setIcon(purseIcon);
            //устанавливаем вырвнивание текста по центру по вертикали и по горизонтали
            moneyInPurseJLabel.setVerticalAlignment(SwingConstants.CENTER);
            moneyInPurseJLabel.setHorizontalAlignment(SwingConstants.CENTER);

            add(moneyInPurseJLabel, BorderLayout.CENTER);
            add(topUpMoney, BorderLayout.SOUTH);
        }
    }


    class CommodityPanel extends JPanel {

        public CommodityPanel(List<Commodity> listCommodity) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            //создаём список, заполненный товарами
            listCommodityComboBox = createComboBox(listCommodity);
            lowCommodityPanel = new LowCommodityPanel();
            add(listCommodityComboBox, BorderLayout.CENTER);
            add(lowCommodityPanel, BorderLayout.SOUTH);
        }

    }


    public JComboBox createComboBox(List<Commodity> commodities) {
        String[] items = new String[commodities.size()];
        for (int i = 0; i < commodities.size(); i++) {
            items[i] = commodities.get(i).toString();
        }
        return (new JComboBox(items));
    }


    class LowCommodityPanel extends JPanel {

        public LowCommodityPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            buyCommodityButton = new JButton("Купить товар");
            addToBasketButton = new JButton("Добавить в корзину");
            add(buyCommodityButton);
            add(addToBasketButton);
            setVisible(true);
        }
    }

}



