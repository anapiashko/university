package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TrolleybusPark {
    public Statement statement = null;

    public TrolleybusPark() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/trolleybuspark", "root", "1234");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNote(String table, Object[] objects) {
        try {
            switch (table) {
                case "drivers": {
                    statement.execute("INSERT INTO `trolleybuspark`.`drivers` (`first_name`, `second_name`, `experience`) VALUES ('" + objects[0] + "', '" + objects[1] + "', '" + objects[2] + "');");
                    break;
                }
                case "trolleybuses": {
                    statement.execute("INSERT INTO `trolleybuspark`.`trolleybuses` (`number`, `mileage`, `production_year`) VALUES ('" + objects[0] + "', '" + objects[1] + "', '" + objects[2] + "');");
                    break;
                }
                case "routes": {
                    statement.execute("INSERT INTO `trolleybuspark`.`routes` (`description`, `trolleybus_id`, `driver_id`, `route_date`) VALUES ('" + objects[0] + "', '" + objects[1] + "', '" + objects[2] + "', '" + objects[3] + "');");
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Adding error :" + e.getMessage());
        }
    }

    public void removeNote(String table, Integer index) {
        try {
            switch (table) {
                case "drivers": {
                    statement.execute("DELETE FROM `trolleybuspark`.`drivers` WHERE  `id`=" + index + ";");
                    break;
                }
                case "trolleybus": {
                    statement.execute("DELETE FROM `trolleybuspark`.`trolleybuses` WHERE  `id`=" + index + ";");
                    break;
                }
                case "routes": {
                    statement.execute("DELETE FROM `trolleybuspark`.`routes` WHERE  `id`=" + index + ";");
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Deleting error :" + e.getMessage());
        }
    }

    public void updateNote(String table, Object[] objects, Integer index) {
        try {
            switch (table) {
                case "drivers": {
                    statement.execute("UPDATE `trolleybuspark`.`drivers` SET `first_name`='" + objects[0] + "', `second_name`='" + objects[1] + "', `experience`='" + objects[2] + "' WHERE  `id`=" + index + ";");
                    break;
                }
                case "trolleybuses": {
                    statement.execute("UPDATE `trolleybuspark`.`trolleybuses` SET `number`='" + objects[0] + "', `mileage`='" + objects[1] + "', `production_year`='" + objects[2] + "' WHERE  `id`=" + index + ";");
                    break;
                }
                case "routes": {
                    statement.execute("UPDATE `trolleybuspark`.`routes` SET `description`='" + objects[0] + "', `trolleybus_id`='" + objects[1] + "', `driver_id`='" + objects[2] + "', `route_date`='" + objects[3] + "' WHERE  `id`=" + index + ";");
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Updating error :" + e.getMessage());
        }
    }
}
