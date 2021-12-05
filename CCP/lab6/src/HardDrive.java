public class HardDrive {

    private String baudRate;
    private Double gbCapacity;
    private Integer bufferMbSize;

    public HardDrive() {
    }

    public HardDrive(String baudRate, Double gbCapacity, Integer bufferMbSize) {
        this.baudRate = baudRate;
        this.gbCapacity = gbCapacity;
        this.bufferMbSize = bufferMbSize;
    }

    public String getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate;
    }

    public Double getGbCapacity() {
        return gbCapacity;
    }

    public void setGbCapacity(Double gbCapacity) {
        this.gbCapacity = gbCapacity;
    }

    public Integer getBufferMbSize() {
        return bufferMbSize;
    }

    public void setBufferMbSize(Integer bufferMbSize) {
        this.bufferMbSize = bufferMbSize;
    }

    @Override
    public String toString() {
        return "HardDisk{" +
                "\n\tbaudRate='" + baudRate + '\'' +
                ", \n\tgbCapacity=" + gbCapacity +
                ", \n\tbufferMbSize=" + bufferMbSize +
                "\n}";
    }
}
