public class Gamma {
    private final int n = 3;
    private final float l = 0.8f;

    private double[] gamma_x;

    public double[] gamma_distribution(double[] R) {
        System.out.println();
        System.out.println("gamma_distribution");

        final int size_x = R.length / n ;
        gamma_x = new double[size_x];

        for (int i = 0, k = 0; i < R.length; i += n, k++) {
            double sum = 0;
            for (int j = i; j < i + n; j++) {
                sum+=R[j];
            }
            gamma_x[k] = (-1/l)*(Math.log(Math.abs(sum)));
        }

        for (int j = 0; j < 20; j++) {
            System.out.println(gamma_x[j]);
        }

        return gamma_x;
    }
}
