public class Main {

    static double min = 100000, max = 0;

    public static void main(String[] args) {
        Generate generate = new Generate();
        double[] kavzi_x = generate.gen();

        Uniform uniform = new Uniform();
        double[] uniform_x = uniform.uniform_distribution(kavzi_x);

        Gamma gamma = new Gamma();
        double[] gamma_x = gamma.gamma_distribution(kavzi_x);

        // -------------2 lab---------------
        System.out.println("M for kvazi");
        double M=M(kavzi_x);
        System.out.println(M);
        System.out.println("D for kvazi");
        double D=D(kavzi_x,M);
        System.out.println(D);

        System.out.println("M for uniform");
        M = M(uniform_x);
        System.out.println(M);
        System.out.println("D for uniform");
        D = D(uniform_x, M);
        System.out.println(D);

        System.out.println("M for gamma");
        M = M(gamma_x);
        System.out.println(M);
        System.out.println("D for gamma");
        D = D(gamma_x, M);
        System.out.println(D);

        findMinMax(gamma_x);
        System.out.println("min = " + min);
        System.out.println("max = " + max);

        double h = max- min;

        int k = (int) (1 + 3.322 * Math.log10(20));
        double delta = h / k;
        System.out.println("k = " + k);
        System.out.println("delta = " + delta);

        double a = -1.05, b = -0.455;
        double[] R_ = gamma_x;
        int count = 0;
        for (int i = 0; i < R_.length; i++) {
            if (R_[i] >= a && R_[i] < b) {
                count++;
            }
        }
        System.out.println("count = " + count);

        int[] m = {10, 4, 6};
        double[] del = {0.586, 1.172, 1.172};
        double[] f = new double[3];

        for (int i = 0; i < f.length; i++) {
            f[i] = m[i] / (Generate.N * del[i]);
        }

        System.out.println ("f array : ");
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i]);
        }

        // -------------2 lab end---------------

    }

    public static void findMinMax(double[] R) {
        max = 0;
        min = 1000000;
        for (int i = 0; i < R.length; i++) {
            if(max < R[i]){
                max = R[i];
            }
            if(min > R[i]){
                min = R[i];
            }
        }
    }

    public static double M(double[] R) {
        double sum = 0;
        double d_sum = 0;
        for (int i = 0; i < R.length; i++) {
            sum += R[i];
        }
        return sum / R.length;
    }

    public static double D(double[] R, double M) {
        double sum = 0;
        for (int i = 0; i < R.length; i++) {
            sum += Math.pow(R[i] - M, 2);
        }
        return sum / (R.length - 1);
    }
}
