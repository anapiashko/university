import GUI.Controller.GUIControllerMainForm;
import GUI.Model.Shop;

public class Main {

    public static void main(String[] args) {
        Shop shop1 = new Shop(new Logic.Purse(100));

        GUIControllerMainForm controller = new GUIControllerMainForm(shop1);
        controller.execute();
    }
}
