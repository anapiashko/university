public class RAM {

    private String type;
    private String volume;

    public RAM() {
    }

    public RAM(String type, String volume) {
        this.type = type;
        this.volume = volume;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "RAM{" +
                "\n\ttype='" + type + '\'' +
                ", \n\tvolume='" + volume + '\'' +
                "\n}";
    }
}