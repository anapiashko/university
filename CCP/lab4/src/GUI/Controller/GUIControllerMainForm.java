package GUI.Controller;

import GUI.Model.Shop;
import GUI.View.MainWindow;
import Logic.Commodity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUIControllerMainForm {

    private Shop model;
    private MainWindow view;
    private ControllerTopUpMoneyFrame controllerTopUpMoney;


    public List<Commodity> getListCommodity(){
        return model.getListCommodity();
    }
    public int getCountMoneyInPurse(){
        return model.getPurse().getAmountMoney();
    }



    public GUIControllerMainForm(Shop shop){
        this.model = shop;
    }

    public void execute(){
        view = new MainWindow(getListCommodity(), getCountMoneyInPurse());

        //размещаем форму по центру экрана
        view.setLocationRelativeTo(null);

        //добавляем обрапботчик событий для кнопки "пополнить счёт"
        JButton buttonTopUpMoneyFromMainView = view.getTopUpMoneyButton();
        buttonTopUpMoneyFromMainView.addActionListener(new ListenerForButtonTopUpMoney());

        //добавляем обработчик событий для кнопки "купить товар"
        JButton jButtonBuyCommodity = view.getButtonBuyCommodity();
        jButtonBuyCommodity.addActionListener(new ListenerForButtonBuyCommodity());

        //добавляем обработчик событий для кнопки "купить товар"
        JButton jButtonAddToBasket = view.getButtonBuyCommodity();
        jButtonBuyCommodity.addActionListener(new ListenerForButtonBuyCommodity());

        //добавляем обработчик событий для кнопки проверить качество товара
        JButton jButtonCheckQuality = view.getJButtonCheckQuality();
        jButtonCheckQuality.addActionListener(new ListenerForButtonCheckQuality());

    }

    class ListenerForButtonTopUpMoney implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            controllerTopUpMoney = new ControllerTopUpMoneyFrame(model, view);
            controllerTopUpMoney.execute();
        }
    }

    class ListenerForButtonBuyCommodity implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int commoditySelectIndex = view.getJComboBoxWithListCommodity().getSelectedIndex();
            if (commoditySelectIndex != -1) {
                Commodity selectCommodity = model.getListCommodity().get(commoditySelectIndex);
                if (model.buyCommodity(selectCommodity)) {
                    view.getJComboBoxWithListCommodity().removeItemAt(commoditySelectIndex);
                    view.setJLabelWithAmountMoney(model.purse.getAmountMoney());
                }else {
                    JOptionPane.showMessageDialog(null, "На счёте недостаточно средств");
                }
            }else{
                JOptionPane.showMessageDialog(null, "В магазине закончились товары! Дождитесь " +
                        "поступления новых!");
            }
        }
    }


    class ListenerForButtonCheckQuality implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int indexSelectCommodity = view.getJComboBoxWithListCommodity().getSelectedIndex();
            if (indexSelectCommodity != -1){
                Commodity selectedCommodity = model.listCommodity.get(indexSelectCommodity);
//                JOptionPane.showMessageDialog(null, selectedCommodity.canUse());
            }else{
                JOptionPane.showMessageDialog(null, "Товары отсутствуют в магазине!");
            }
        }
    }
}
