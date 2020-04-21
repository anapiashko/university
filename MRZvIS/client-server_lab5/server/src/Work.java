public class Work {

    private int portion; //порция

    private static long  start = System.currentTimeMillis();

    private Mine mine;

    private Storage storage;

    public Work(int portion){
        this.portion = portion;
        this.mine = new Mine(portion);
        this.storage = new Storage(portion);
    }

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
