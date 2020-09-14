public class Gamma {
    private final int n = 3;
    private final float l = 0.8f;

    private double[] x;

    public void gamma_distribution(double[] R) {
        System.out.println();
        System.out.println("gamma_distribution");

        final int size_x = R.length / n;
        x = new double[size_x];

        for (int i = 0, k = 0; i < R.length; i += n, k++) {
            double sum = 0;
            for (int j = i; j < i + n; j++) {
                sum+=R[j];
            }
            x[k] = (-1/l)*(Math.log(sum));
        }

        for (int j = 0; j < size_x; j++) {
            System.out.println(x[j]);
        }
    }
}
