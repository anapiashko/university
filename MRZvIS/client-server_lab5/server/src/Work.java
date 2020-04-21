public class Work {

    private final int portion = 4; //порция

    private static long  start = System.currentTimeMillis();

    private Mine mine = new Mine(portion);

    private Storage storage = new Storage(portion);

    public String workingProcess() {

        if (!mine.take()) {
            return "Mine resources is empty";
        }

        if (!storage.put()) {
            System.out.println("Storage is overloaded");
        }

        if (storage.timeToDeleting(start) >= 10.0) {

            storage.cleanStorage();
            start = System.currentTimeMillis();
        }

        return "Success work";
    }
}
