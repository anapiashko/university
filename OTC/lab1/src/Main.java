public class Main {

    public static void main(String[] args) {
        Generate generate = new Generate();
//        generate.gen();

        double[] R = new double[12];
//        generate.Kvazi(R);
        R[0] = 0.43;
        R[1] = 0.8;
        R[2] = 0.29;
        R[3] = 0.67;
        R[4] = 0.19;
        R[5] = 0.96;
        R[6] = 0.02;
        R[7] = 0.73;
        R[8] = 0.50;
        R[9] = 0.33;
        R[10] = 0.14;
        R[11] = 0.71;


        Uniform uniform = new Uniform();
        uniform.uniform_distribution(R);

        Gamma gamma = new Gamma();
        gamma.gamma_distribution(R);
    }
}
