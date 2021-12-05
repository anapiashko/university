import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // состовляющие материнской платы
        RAM ram = new RAM("DDR4", "64GB");
        CPU cpui7 = new CPU("Intel core i7 920", 2660D, 64);
        CPU cpui5 = new CPU("Intel core i5 10400", 2900D, 64);
        HardDrive hardDisk = new HardDrive("250 MB/sec", 256D, 64);
        GraphicsCard graphicsCard = new GraphicsCard("GeForce RTX 3060 Ti", "GA104", 1665);

        // материнская плата c cpui5
        Motherboard motherboard1 = new Motherboard("LGA 1200", cpui5, ram, hardDisk, graphicsCard);

        // материнская плата c cpui7
        Motherboard motherboard2 = new Motherboard("LGA 1200", cpui7, ram, hardDisk, graphicsCard);

        // ноутбук с материнской платой 1
        Laptop laptop1 = new Laptop(motherboard1);

        // ноутбук с материнской платой 2
        Laptop laptop2 = new Laptop(motherboard2);

        List<Laptop> laptops = Arrays.asList(laptop1, laptop2);

        Laptop laptopWithMaxCPUFrequency = laptops.stream().max(Laptop::compareCPUFrequency).get();

        System.out.println(laptopWithMaxCPUFrequency);
    }
}