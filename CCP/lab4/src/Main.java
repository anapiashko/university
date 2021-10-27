import GUI.Controller.GUIControllerMainForm;
import GUI.Model.Shop;

public class Main {

    public static void main(String[] args) {
        Shop shop = new Shop();

        GUIControllerMainForm controller = new GUIControllerMainForm(shop);
        controller.execute();
    }
}
