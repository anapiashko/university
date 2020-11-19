public class Main {

    static int n = 10000;

    static double[] xi = new double[n];

    static double[][] x = new double[n - 10][10];

    static double[] y = new double[n];

    static double[] e = new double[n - 10];

    static double[][] w12 = new double[10][4];

    static double[] T12 = new double[4];

    static double[] w23 = new double[4];

    static double[] w23t = new double[4];

    static double T23;

    static double E_zelaemay = (Math.pow(10, -4));

    public static void main(String[] args) {

        init();

        double[] y1 = new double[4];

        double[] s1 = new double[4];

        double y2, s2 = 0, a = 0.02, E = 0, Ep = 0;

        for (int j = 0; ; j++) {//эпоха

            E = 0;

            for (int i = 0; i < 0.7 * n - 10; i++) {//образ

                s1[0] = s1[1] = s1[2] = s1[3] = 0;

                for (int t = 0; t < 4; t++) {
                    for (int o = 0; o < 10; o++) {
                        s1[t] += w12[o][t] * x[i][o];
                    }
                    s1[t] -= T12[t];
                }

                for (int t = 0; t < 4; t++) {
                    y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
                }

                s2 = 0;

                for (int t = 0; t < 4; t++) {
                    s2 += w23[t] * y1[t];
                }

                s2 -= T23;
                y2 = (Math.exp(s2) - Math.exp(-s2)) / (Math.exp(s2) + Math.exp(-s2));

///////////obratno

                E += ((Math.pow(y2 - e[i], 2)) / 2);

                for (int t = 0; t < 4; t++) {
                    w23[t] = w23[t] - (a * (y2 - e[i]) * ((1 - y2 * y2) * y1[t]));
                }

                T23 = T23 + (a * (y2 - e[i]) * ((1 - y2 * y2)));

                for (int t = 0; t < 4; t++) {
                    w23t[t] = w23[t];
                }

                for (int t = 0; t < 4; t++) {
                    for (int o = 0; o < 10; o++) {
                        w12[o][t] = w12[o][t] - (a * (y1[t] - e[i]) * ((1 - y2 * y2)) * w23t[t] * ((1 - y1[t] * y1[t])) * x[i][o]);
                    }
                    T12[t] = T12[t] + (a * (y1[t] - e[i]) * ((1 - y2 * y2)) * w23t[t] * ((1 - y1[t] * y1[t])));
                }
            }

            if (E <= E_zelaemay) {
                System.out.println("iter= " + j);
                System.out.println("E na ishodnih data" + E);
                break;
            }

            if ((Math.abs(E - Ep)) <= (Math.pow(10, -10))) {
                if (j == 0) {
                    Ep = E;
                    continue;
                }
                System.out.println("net uluchsheniy ");
                System.out.println("iter= " + j);
                System.out.println("E na ishodnih data" + E);
                break;
            }
            Ep = E;
        }

/////////////////проверка/////////////////////////////////////

        E = 0;

        for (int i = (int) (0.7 * n); i < n - 10; i++) {

            s1[0] = s1[1] = s1[2] = s1[3] = 0;

            for (int t = 0; t < 4; t++) {
                for (int o = 0; o < 10; o++) {
                    s1[t] += w12[o][t] * x[i][o];
                }
                s1[t] -= T12[t];
            }

            for (int t = 0; t < 4; t++) {
                y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
            }

            s2 = 0;

            for (int t = 0; t < 4; t++) {
                s2 += w23[t] * y1[t];
            }

            s2 -= T23;

            y2 = (Math.exp(s2) - Math.exp(-s2)) / (Math.exp(s2) + Math.exp(-s2));

            if (i % 10 == 0) {
                System.out.print("y2 " + y2);
                System.out.println(", e " + (e[i]));
            }

            E += ((Math.pow(y2 - e[i], 2)) / 2);
        }

        System.out.println("E нa необучаемых данных" + E);

    }

    public static void init() {

        for (int i = 0; i < n; i++) {
            xi[i] = i;//(double)i/n;//
            y[i] = 0.2 * Math.cos(0.6 * xi[i]) + 0.05 * Math.sin(0.6 * xi[i]);
            double rr = y[i];
            int yy = 0;
        }

        for (int i = 0; i < 10; i++) {

            for (int j = 0; j < 4; j++) {
                w12[i][j] = Math.random();
            }
        }

        for (int j = 0; j < 4; j++) {
            w23[j] = Math.random();
            T12[j] = Math.random();
        }

        T23 = Math.random();

        for (int i = 0; i < (n - 10); i++) {
            for (int j = 0; j < 10; j++) {
                x[i][j] = y[i + j];
            }
            e[i] = y[i + 10];
        }
    }
}