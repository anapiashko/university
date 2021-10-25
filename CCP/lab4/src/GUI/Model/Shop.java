package GUI.Model;

import Logic.Commodity;
import Logic.Purse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Shop {

    public Purse purse;
    public List<Commodity> listCommodity = new ArrayList<Commodity>();

    public Shop(Purse purse){
        createCommodityList();
        this.purse = purse;
    }

    public List<Commodity> getListCommodity(){
        return listCommodity;
    }

    public Purse getPurse() {
        return purse;
    }

    /**
     * печатает список товаров которые есть в магазине
     */
    public void printAssortiment(){
        int i = 0;
        for (Commodity commodity : listCommodity){
            System.out.println( ++i + ") " + commodity);
        }
    }

    public void printCommodityInfo(Commodity commodity){
        System.out.println("----------------------------------------------------------------");
//        product.canUse();
    }

    /**
     * Удаляет товар переданный параметром, из списка товаров. Снимает со счёта цену товара.
     * @param commodity - товар который будет удалён.
     */
    public boolean buyCommodity(Commodity commodity) {
        boolean result;
        if (commodity.getPrice() <= purse.getAmountMoney()) {
            int removeIndex = -1;
            for (int i = 0; i < listCommodity.size(); i++) {
                if (compareCommodity(listCommodity.get(i), commodity)) {
                    removeIndex = i;
                }
            }
            if (removeIndex != -1) {
                listCommodity.remove(removeIndex);
            }
            purse.withdrawMoney(commodity.getPrice());
            result = true;
        }else
        {
            result = false;
        }
        return result;
    }

    /**
     * Метод заполняет список товаров данными из файла.
     */
    private void createCommodityList()  {
        try {
            List<String> stringsWithCommodityInfo = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(".\\src\\resources\\itemList.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    stringsWithCommodityInfo.add(line);
                }
            }

            for (String infoForCommodity : stringsWithCommodityInfo) {
                String[] info = infoForCommodity.split(" ");
                listCommodity.add(new Commodity(info[0], Integer.parseInt(info[1])));
            }
        }catch(IOException ex1){
            System.out.println("При чтении файла возникла ошибка!");
        }
    }

    /**
     * Сравнение двух товаров на равенство по их параметрам
     * @return true - товары равны, false - товары не равны.
     */
    private boolean compareCommodity(Commodity c1, Commodity c2){
        boolean result = false;
        if (c1.getName().equals(c2.getName()) && c1.getPrice() == c2.getPrice()){
            result = true;
        }
        return result;
    }
}


