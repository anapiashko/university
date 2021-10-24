package GUI.Controller;

import GUI.Model.Shop;
import GUI.View.BasketListFrame;
import GUI.View.MainWindow;
import Logic.Basket;
import Logic.Commodity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class GUIControllerMainForm {

    private Shop shop;
    private MainWindow view;
    private ControllerTopUpMoneyFrame controllerTopUpMoney;
    private Basket basket = new Basket();


    public List<Commodity> getListCommodity() {
        return shop.getListCommodity();
    }

    public int getCountMoneyInPurse() {
        return shop.getPurse().getAmountMoney();
    }


    public GUIControllerMainForm(Shop shop) {
        this.shop = shop;
    }

    public void execute() {
        view = new MainWindow(getListCommodity(), getCountMoneyInPurse());

        //размещаем форму по центру экрана
        view.setLocationRelativeTo(null);

        //добавляем обрапботчик событий для кнопки "пополнить счёт"
        JButton buttonTopUpMoneyFromMainView = view.getTopUpMoneyButton();
        buttonTopUpMoneyFromMainView.addActionListener(new ListenerForButtonTopUpMoney());

        //добавляем обработчик событий для кнопки "найти товар"
        JButton jButtonFindCommodity = view.getJButtonFindCommodity();
        jButtonFindCommodity.addActionListener(new ListenerForButtonFindCommodity());

        //добавляем обработчик событий для кнопки добадения в корзину товара
        JButton jButtonAddToBasket = view.getJButtonAddToBasket();
        jButtonAddToBasket.addActionListener(new ListenerForButtonAddToBasket());

        //добавляем обработчик событий для кнопки "просмотр корзины"
        JButton jButtonShowBasket = view.getJButtonShowBasket();
        jButtonShowBasket.addActionListener(new ListenerForButtonButtonShowBasket());

        //добавляем обработчик событий для кнопки "купить товар"
        JButton jButtonBuyCommodity = view.getButtonBuyCommodity();
        jButtonBuyCommodity.addActionListener(new ListenerForButtonBuyCommodity());

    }

    class ListenerForButtonTopUpMoney implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            controllerTopUpMoney = new ControllerTopUpMoneyFrame(shop, view);
            controllerTopUpMoney.execute();
        }
    }

    class ListenerForButtonButtonShowBasket implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new BasketListFrame(basket);
        }
    }

    class ListenerForButtonFindCommodity implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String response = JOptionPane.showInputDialog(null,
                    "Type name :",
                    "Searching for commodity",
                    JOptionPane.QUESTION_MESSAGE);

            Optional<Commodity> foundCommodity = shop.getListCommodity().stream()
                    .filter(c -> c.getName().equalsIgnoreCase(response))
                    .findFirst();

            if (foundCommodity.isPresent()) {
                view.setJComboBoxSelectedCommodity(foundCommodity.get());
            } else {
                JOptionPane.showMessageDialog(null, "Товар не найден");
            }
        }
    }

    class ListenerForButtonBuyCommodity implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int commoditySelectIndex = view.getJComboBoxWithListCommodity().getSelectedIndex();
            if (commoditySelectIndex != -1) {
                Commodity selectCommodity = shop.getListCommodity().get(commoditySelectIndex);
                if (shop.buyCommodity(selectCommodity)) {
                    view.getJComboBoxWithListCommodity().removeItemAt(commoditySelectIndex);
                    view.setJLabelWithAmountMoney(shop.purse.getAmountMoney());
                } else {
                    JOptionPane.showMessageDialog(null, "На счёте недостаточно средств");
                }
            } else {
                JOptionPane.showMessageDialog(null, "В магазине закончились товары! Дождитесь " +
                        "поступления новых!");
            }
        }
    }


    class ListenerForButtonAddToBasket implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int indexSelectCommodity = view.getJComboBoxWithListCommodity().getSelectedIndex();
            if (indexSelectCommodity != -1) {
                Commodity selectedCommodity = shop.listCommodity.get(indexSelectCommodity);
                basket.add(selectedCommodity);
            } else {
                JOptionPane.showMessageDialog(null, "Товары отсутствуют в магазине!");
            }
        }
    }
}
