public class Storage {

    private int portion; //порция

    private final int storage_size = 50; //размер склада

    private final int for_delete = 25; //количество которое будет удалено из склада

    private static int current_storage = 0; //текущее количество вещей на складе

    private static int toPut = 0; //количество чтобы положить в storage когда освободится

    public Storage(int portion){
        this.portion = portion;
    }

    boolean put() {

        if (current_storage + portion > storage_size) {
            toPut += portion;
            return false;
        } else {
            current_storage += portion;
            return true;
        }
    }

    long timeToDeleting(long start) {

        long end = System.currentTimeMillis();
        long time = (end - start);

        System.out.println("time = " + time);

        return time;
    }

    void cleanStorage(){

        System.out.println("Overtime");

        if (current_storage - for_delete < 0) {
            current_storage = 0;
        } else {
            current_storage -= for_delete;
        }
        current_storage += toPut;
        toPut = 0;
    }
}
