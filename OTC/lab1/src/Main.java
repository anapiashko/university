public class Main {

    static double min = 100000, max = 0;

    public static void main(String[] args) {
        Generate generate = new Generate();
        double[] kavzi_x = generate.gen();

//        Uniform uniform = new Uniform();
//        double[] uniform_x = uniform.uniform_distribution(kavzi_x);

        Gamma gamma = new Gamma();
        double[] gamma_x = gamma.gamma_distribution(kavzi_x);

        // -------------2 lab---------------
        System.out.println("M for kvazi");
        double M=M(kavzi_x);
        System.out.println(M);
        System.out.println("D for kvazi");
        double D=D(kavzi_x,M);
        System.out.println(D);

//        System.out.println("M for uniform");
//        M = M(uniform_x);
//        System.out.println(M);
//        System.out.println("D for uniform");
//        D = D(uniform_x, M);
//        System.out.println(D);
//
        System.out.println("M for gamma");
        M = M(gamma_x);
        System.out.println(M);
        System.out.println("D for gamma");
        D = D(gamma_x, M);
        System.out.println(D);
//
        findMinMax(gamma_x);
        System.out.println("min = " + min);
        System.out.println("max = " + max);

        double h = max- min;

        int k = (int) (1 + 3.322 * Math.log10(gamma_x.length));
        double delta = h / k;
        System.out.println("k = " + k);
        System.out.println("delta = " + delta);

        double[] R_ = gamma_x;
        int n = 15;
        int[] m = new int [n];
        for (int q = 0; q < n; q++) {
            double a = min + q * delta, b = a + delta;
            int count = 0;
            System.out.println("a = " + a + ", b = " + b + ", q = " + q);
            for (int i = 0; i < R_.length; i++) {
                if (R_[i] >= a && R_[i] < b) {
                    count++;
                }
            }
            m[q] = count;
            System.out.println("count = " + count);
        }

        System.out.print("m = { ");
        for (int q = 0; q < m.length; q++) {
            System.out.print(" " + m[q] + " ");
        }
        System.out.println("}");

        double[] del = {delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta, delta};
        double[] f = new double[m.length];

        for (int i = 0; i < f.length; i++) {
            f[i] = (double) m[i] / (gamma_x.length * del[i]);
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
