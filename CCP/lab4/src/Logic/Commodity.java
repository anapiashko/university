package Logic;

public class Commodity {

    private int price;
    private String name;

    public Commodity(String name, int price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

    public String toString(){
        return "Название товара: " + name + "; Цена товара" + price;
    }
}
