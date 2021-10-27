package GUI.Model;

import Logic.Commodity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Shop {

    public List<Commodity> listCommodity = new ArrayList<Commodity>();

    public Shop(){
        createCommodityList();
    }

    public List<Commodity> getListCommodity(){
        return listCommodity;
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


