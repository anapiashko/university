public class Laptop {

    private Motherboard motherboard;

    public Laptop() {
    }

    public Laptop(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public Motherboard getMotherboard() {
        return motherboard;
    }

    public void setMotherboard(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    @Override
    public String toString() {
        return "Computer{\n" +
                "\tmotherboard=" + motherboard +
                "\n}";
    }

    public static int compareCPUFrequency (Laptop l1, Laptop l2){
        if(l1.getMotherboard().getCpu().getFrequencyHz() > l2.getMotherboard().getCpu().getFrequencyHz()) {
            return 1;
        }
        return -1;
    }
}