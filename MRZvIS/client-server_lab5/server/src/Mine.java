public class Mine {

    private int mineCapacity = 1000; //шахта
    private int portion; //порция


    public Mine(int portion) {
        this.portion = portion;
    }

    public boolean take() {

        if (mineCapacity - portion < 0) {
            return false;
        } else {
            mineCapacity -= portion;
            return true;
        }
    }
}
