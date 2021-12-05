public class GraphicsCard {
    private String model;
    private String core;
    private Integer coreFrequency;

    public GraphicsCard() {
    }

    public GraphicsCard(String model, String core, Integer coreFrequency) {
        this.model = model;
        this.core = core;
        this.coreFrequency = coreFrequency;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public Integer getCoreFrequency() {
        return coreFrequency;
    }

    public void setCoreFrequency(Integer coreFrequency) {
        this.coreFrequency = coreFrequency;
    }

    @Override
    public String toString() {
        return "GraphicsCard{" +
                "\n\tmodel='" + model + '\'' +
                ", \n\tcore='" + core + '\'' +
                ", \n\tcoreFrequency=" + coreFrequency +
                "\n}";
    }
}