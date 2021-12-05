public class CPU {

    private String model;
    private Double frequencyHz;
    private Integer bitness;

    public CPU() {
    }

    public CPU(String model, Double frequencyHz, Integer bitness) {
        this.model = model;
        this.frequencyHz = frequencyHz;
        this.bitness = bitness;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getFrequencyHz() {
        return frequencyHz;
    }

    public void setFrequencyHz(Double frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public Integer getBitness() {
        return bitness;
    }

    public void setBitness(Integer bitness) {
        this.bitness = bitness;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "\n\tmodel='" + model + '\'' +
                ", \n\tclockFrequency=" + frequencyHz +
                ", \n\tbitness=" + bitness +
                "\n}";
    }
}