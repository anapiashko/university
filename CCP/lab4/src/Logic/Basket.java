package Logic;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    public List<Commodity> listCommodity = new ArrayList<>();

    public void add(Commodity commodity) {
        listCommodity.add(commodity);
    }

    public List<Commodity> getListCommodity(){
        return this.listCommodity;
    }
}
