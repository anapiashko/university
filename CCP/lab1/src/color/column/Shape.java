package color.column;

public enum Shape {

    TRIANGLE("Triangle"),
    SQUARE("Square"),
    CIRCLE("Circle");

    private String name;

    Shape(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
