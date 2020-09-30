public class Generate {

    public static final int N = 900;
    private int i = 0;
    public double[] kvazi_x = new double[N];

    private int z0 = 12;

    private int z1 = 35;

    private final int a0 = 1;

    private final int a1 = 1;

    private final int M = 100;

//    public void gen() {
//        if (i == N) {
//            return;
//        }
//        int z3 = z1;
//        z1 = a0 * z1 + a1 * z0;
//        z0 = z3;
//        int r = z1 % M;
//        float x1 = (float) r / M;
//        R1[i] = x1;
//        i++;
//        System.out.println(x1);
//        gen();
//    }

    public double[] gen() {
        System.out.println("Kvazi Generate");
        int a0 = 1, a1 = 1, m = 100;
        double z0 = 12.0d, z1 = 35.0d, z2;
        for (int j = 0; j < N; j++) {
            z2 = z0 * a0 + z1 * a1;
            kvazi_x[j] = (z2 % m) / m;
            z0 = z1;
            z1 = z2;
        }

        for (int j = 0; j < N; j++) {
            System.out.println(kvazi_x[j]);
        }

        return kvazi_x;
    }
}
