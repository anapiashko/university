package GUI.View;

import Logic.Commodity;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;


public class MainWindow extends JFrame {

    private CommodityPanel commodityPanel;
    private final Image shopIcon = new ImageIcon("../imageShop.png").getImage();
    private JButton addToBasketButton;
    private JButton findCommodityButton;
    private JButton showBasketButton;
    private JButton topUpMoney;
    private JLabel moneyInPurseJLabel;
    private JPanel lowCommodityPanel;
    private JComboBox listCommodityComboBox;

    public JLabel getLabelWithAmountMoney() {
        return moneyInPurseJLabel;
    }

    public JButton getTopUpMoneyButton() {
        return topUpMoney;
    }

    public JLabel getMoneyInPurseJLabel() {
        return moneyInPurseJLabel;
    }

    public JButton getJButtonAddToBasket() {
        return addToBasketButton;
    }

    public JButton getJButtonFindCommodity () {
        return findCommodityButton;
    }

    public JButton getJButtonShowBasket () {
        return showBasketButton;
    }

    public JComboBox getJComboBoxWithListCommodity() {
        return listCommodityComboBox;
    }

    public void setJComboBoxWithListCommodity(List<Commodity> listCommodity) {
        listCommodityComboBox = createComboBox(listCommodity);
    }

    public void setJComboBoxSelectedCommodity(Commodity commodity) {
        listCommodityComboBox.getModel().setSelectedItem(commodity);
    }

    public MainWindow(List<Commodity> listCommodity) {
        setLayout(new GridLayout(1, 2, 15, 5));
        setTitle("Магазин");
        setIconImage(shopIcon);
        commodityPanel = new CommodityPanel(listCommodity);
        add(commodityPanel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
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
            addToBasketButton = new JButton("Добавить в корзину");
            findCommodityButton = new JButton("Найти товар");
            showBasketButton = new JButton("Показать корзину");
            add(findCommodityButton);
            add(addToBasketButton);
            add(showBasketButton);
            setVisible(true);
        }
    }

}



