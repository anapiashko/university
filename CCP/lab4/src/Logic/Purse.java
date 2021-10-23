package Logic;

public class Purse {

    private int amountMoney;

    public Purse(int amountMoney){
        this.amountMoney = amountMoney;
    }

    public void topUpAmountMoney(int money){
        amountMoney += money;
    }

    public void withdrawMoney(int money){
        amountMoney -= money;
    }

    public int getAmountMoney(){
        return amountMoney;
    }

    public void infoAmountMoney(){
    System.out.println("Остаток средств на счёте: " + amountMoney);
    }
}
