import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NN2 {

    static double F(double Sj) {
        return ((Math.exp(Sj) - Math.exp(-Sj)) / (Math.exp(Sj) + Math.exp(-Sj)));
    }

    static List<Double> x = new ArrayList<>(); // Входные х-значения
    static List<Double> y = new ArrayList<>();// Входные у-значения
    static List<Double> z = new ArrayList<>();// Входные z-значения
    static int n = 1000;
    static int n1 = 6; // Кол-во входных образов
    static final int n2 = 10;
    static final int n3 = 3;
    static double[] max = new double[3];
    static double[] min = new double[3];

    public static void main(String[] args) throws IOException {

        long ms = System.currentTimeMillis();

        x.add(0.1);
        y.add(0.1);
        z.add(0.1);

        double Emin = 0.05; // Желаемая среднеквадратичная ошибка сети
        double alp = 0.1; // Шаг обучения
        int NumberOfForms = 20; // Кол-во входных образов для обучения
        int p = 10; // Кол-во нейронов в скрытом слое
        double[] Tj = new double[p]; // Пороги нейронной сети
        double[] Tk = new double[3];
        double[][] Wij = new double[n1][p]; // Весовые коэффициэнты для входного и скрытого слоя
        double[][] Wjk = new double[p][3]; // Весовые коэффициэнты для скрытого и выходного слоя
        double[] Pj = new double[p]; // Значения скрытого слоя
        double[] gamma_j = new double[p]; // Ошибка для скрытого слоя
        double[] gamma_k = new double[3]; // Ошибка для выходного слоя
        double[] xyz = new double[3];

        File file = new File("xyz1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        for (int i = 0; (str = br.readLine()) != null && i < n; i++) {
            String[] subStr;
            String delimeter = " "; // Разделитель
            subStr = str.split(delimeter);
            x.add(Double.parseDouble(subStr[0]));
            y.add(Double.parseDouble(subStr[1]));
            z.add(Double.parseDouble(subStr[2]));
        }

        // Заполняем рандомными значениями весовые коэффициэнты и пороги нейронной сети
        Random rand = new Random(LocalDateTime.now().getSecond());
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < p; j++) {
                Wij[i][j] = rand.nextDouble();
            }
        }
        for (int j = 0; j < p; j++) {
            for (int kk = 0; kk < n3; kk++) {
                Wjk[j][kk] = rand.nextDouble();
            }
            Tj[j] = rand.nextDouble();
        }
        for (int kk = 0; kk < n3; kk++) {
            Tk[kk] = rand.nextDouble();
        }

        try (FileWriter init = new FileWriter("init.txt", false)) {
            init.append("Wij : \n");
            for (int i = 0; i < n1; i++) {
                for (int j = 0; j < p; j++) {
                    init.append(String.valueOf(Wij[i][j])).append(" ");
                }
                init.append('\n');
            }
            init.append("Wjk : \n");
            for (int j = 0; j < p; j++) {
                for (int kk = 0; kk < n3; kk++) {
                    init.append(String.valueOf(Wjk[j][kk])).append(" ");
                }
                init.append('\n');
            }
            init.append("Tj : \n");
            for (int j = 0; j < p; j++) {
                init.append(String.valueOf(Tj[j])).append(" ");
            }
            init.append('\n');
            init.append("Tk : \n");
            for (int kk = 0; kk < n3; kk++) {
                init.append(String.valueOf(Tk[kk])).append(" ");
            }
            init.append('\n');
        }

//        Wij[0][0] = 0.7295344790213775;
//        Wij[0][1] = 0.953246763704099;
//        Wij[0][2] = 0.762176185011977;
//        Wij[0][3] = 0.06691763977373688;
//        Wij[0][4] = 0.5209152497174534;
//        Wij[0][5] = 0.09959898370707931;
//        Wij[0][6] = 0.6414391749756414;
//        Wij[0][7] = 0.2652232367227114;
//        Wij[0][8] = 0.967380854919256;
//        Wij[0][9] = 0.8745765857956704;
//
//        Tj[0] = 0.33270252339363016;
//        Tj[1] = 0.35997150933296995;
//        Tj[2] = 0.5651809314594766;
//        Tj[3] = 0.15039903872494242;
//        Tj[4] = 0.3670791248968839;
//        Tj[5] = 0.9330848000738823;
//        Tj[6] = 0.7384807785134201;
//        Tj[7] = 0.8205893544230853;
//        Tj[8] = 0.20697487582633944;
//        Tj[9] = 0.3744344485723229;


//        Wjk :
//        0.42951304212264785 0.06506115455380634 0.31684591780054727
//        0.7304821451813145 0.8310611611248789 0.9908281824916119
//        0.525908466861114 0.08800168254019625 0.8875148315614323
//        0.0041966311114634 0.010049135993713931 0.3453052202493765
//        0.02114044987690955 0.8694078870898104 0.08869614322726282
//        0.5027938087722469 0.374830619492294 0.5463792493455035
//        0.004913056331603327 0.27287867587970027 0.39721417947641857
//        0.8335421807407632 0.3950678053216463 0.4014808455615081
//        0.7572890336927227 0.5615748920711511 0.9664162812546171
//        0.7490673622283647 0.010969255835529212 0.567892443733358
//
//        Tk :
//        0.38355551064297677 0.315472027157224 0.10419343769871936

        // нормирование данных и запись их в файл
        normir();

        double sum = 0, E = 0, Esum = 10e+5;
        double xx = 0, yy = 0, zz = 0;

        try (FileWriter xxx = new FileWriter("x.txt", false)) {
            for (int j = 0; j < x.size(); j++) {
                xxx.append(x.get(j).toString());
                xxx.append('\n');
            }
        }

        try (FileWriter yyy = new FileWriter("y.txt", false)) {
            for (int j = 0; j < y.size(); j++) {
                yyy.append(y.get(j).toString());
                yyy.append('\n');
            }
        }

        try (FileWriter zzz = new FileWriter("z.txt", false)) {
            for (int j = 0; j < z.size(); j++) {
                zzz.append(z.get(j).toString());
                zzz.append('\n');
            }
        }

        try (BufferedWriter xyzWriter = new BufferedWriter(new FileWriter("normir_xyz.txt"))) {
            for (int j = 0; j < z.size(); j++) {
                xyzWriter.write("" + x.get(j) + " " + y.get(j) + " " + z.get(j) + '\n');
            }
        }

        // training
        int count = 0;
        while (Esum > Emin) {
            System.out.println("Esum = " + Esum);
            Esum = 0;
            for (int kk = 0; kk < 0.7 * n - n1; kk++) {

                for (int j = 0; j < p; j++) {  // Значения скрытого слоя
                    sum = x.get(kk) * Wij[0][j] + y.get(kk) * Wij[1][j] + z.get(kk) * Wij[2][j]
                            + x.get(kk + 1) * Wij[3][j] + y.get(kk + 1) * Wij[4][j] + z.get(kk + 1) * Wij[5][j];
                    Pj[j] = F(sum - Tj[j]);
                }

                for (int k = 0; k < n3; k++) {   // Значения выходного слоя
                    sum = 0;
                    for (int i = 0; i < p; i++) {
                        sum += Pj[i] * Wjk[i][k];
                    }
                    xyz[k] = F(sum - Tk[k]);

                    switch (k) {
                        case 0: {
                            E += Math.pow((xyz[k] - x.get(kk + 2)), 2) / 2;
                            gamma_k[k] = xyz[k] - x.get(kk + 2);
                            break;
                        }
                        case 1: {
                            E += Math.pow((xyz[k] - y.get(kk + 2)), 2) / 2;
                            gamma_k[k] = xyz[k] - y.get(kk + 2);
                            break;
                        }
                        case 2: {
                            E += Math.pow((xyz[k] - z.get(kk + 2)), 2) / 2;
                            gamma_k[k] = xyz[k] - z.get(kk + 2);
                            break;
                        }
                    }
                }
                // Вычисление ошибок
                for (int i = 0; i < p; i++) {
                    gamma_j[i] = gamma_k[0] * (1 - xyz[0] * xyz[0]) * Wjk[i][0]
                            + gamma_k[1] * (1 - xyz[1] * xyz[1]) * Wjk[i][1]
                            + gamma_k[2] * (1 - xyz[2] * xyz[2]) * Wjk[i][2];
                }
                for (int i = 0; i < n1; i++) {
                    for (int j = 0; j < p; j++) {
                        switch (i) {
                            case 0: {
                                Wij[i][j] -= alp * gamma_j[j] * (1 - Pj[j] * Pj[j]) * x.get(kk);
                                break;
                            }
                            case 1: {
                                Wij[i][j] -= alp * gamma_j[j] * (1 - Pj[j] * Pj[j]) * y.get(kk);
                                break;
                            }
                            case 2: {
                                Wij[i][j] -= alp * gamma_j[j] * (1 - Pj[j] * Pj[j]) * z.get(kk);
                                break;
                            }
                            case 3: {
                                Wij[i][j] -= alp * gamma_j[j] * (1 - Pj[j] * Pj[j]) * x.get(kk+1);
                                break;
                            }
                            case 4: {
                                Wij[i][j] -= alp * gamma_j[j] * (1 - Pj[j] * Pj[j]) * y.get(kk+1);
                                break;
                            }
                            case 5: {
                                Wij[i][j] -= alp * gamma_j[j] * (1 - Pj[j] * Pj[j]) * z.get(kk +1);
                                break;
                            }
                        }
                    }
                }
                for (int j = 0; j < p; j++) {
                    Tj[j] += alp * gamma_j[j] * (1 - Pj[j] * Pj[j]);
                }
                for (int k1 = 0; k1 < n3; k1++) {
                    for (int j = 0; j < p; j++) {
                        Wjk[j][k1] -= alp * gamma_k[k1] * (1 - xyz[k1] * xyz[k1]) * Pj[j];
                    }
                    Tk[k1] += alp * gamma_k[k1] * (1 - xyz[k1] * xyz[k1]);
                }
                Esum += E;

                E = 0;
            }
            count++;
        }

        // testing
        List<Double> res_x = new ArrayList<>();

        List<Double> res_y = new ArrayList<>();

        List<Double> res_z = new ArrayList<>();

        res_x.add(x.get(0));
        res_y.add(y.get(0));
        res_z.add(z.get(0));

        res_x.add(x.get(1));
        res_y.add(y.get(1));
        res_z.add(z.get(1));

        for (int kk = 0; kk < n; kk++) {

            for (int i = 0; i < p; i++) {  // Значения скрытого слоя
                sum = res_x.get(kk) * Wij[0][i]
                        + res_y.get(kk) * Wij[1][i]
                        + res_z.get(kk) * Wij[2][i]
                        + res_x.get(kk + 1) * Wij[3][i]
                        + res_y.get(kk + 1) * Wij[4][i]
                        + res_z.get(kk + 1) * Wij[5][i];
                Pj[i] = F(sum - Tj[i]);
            }

            for (int j = 0; j < 3; j++) {   // Значения выходного слоя
                sum = 0;
                for (int i = 0; i < p; i++) {
                    sum += Pj[i] * Wjk[i][j];
                }
                xyz[j] = F(sum - Tk[j]);
                switch (j) {
                    case 0: {
                        res_x.add(xyz[j]);
                        break;
                    }
                    case 1: {
                        res_y.add(xyz[j]);
                        break;
                    }
                    case 2: {
                        res_z.add(xyz[j]);
                        break;
                    }
                }
            }
        }

        E = 0;
        for (int i = 0; i < NumberOfForms - 1; i++) {
            E += (Math.pow((x.get(i) - res_x.get(i)), 2) + Math.pow((y.get(i) - res_y.get(i)), 2) + Math.pow((z.get(i) - res_z.get(i)), 2)) / 2;
        }

        try (FileWriter res_xxx = new FileWriter("res_x.txt", false)) {
            for (int j = 0; j < res_x.size(); j++) {
                res_xxx.append(res_x.get(j).toString());
                res_xxx.append('\n');
            }
        }

        try (FileWriter res_yyy = new FileWriter("res_y.txt", false)) {
            for (int j = 0; j < res_y.size(); j++) {
                res_yyy.append(res_y.get(j).toString());
                res_yyy.append('\n');
            }
        }

        try (FileWriter res_zzz = new FileWriter("res_z.txt", false)) {
            for (int j = 0; j < res_z.size(); j++) {
                res_zzz.append(res_z.get(j).toString());
                res_zzz.append('\n');
            }
        }

        post_normir(res_x,0);
        post_normir(res_y,1);
        post_normir(res_z,2);

        try (BufferedWriter xyzWriter = new BufferedWriter(new FileWriter("res_xyz.txt"))) {
            for (int j = 0; j < res_z.size(); j++) {
                xyzWriter.write("" + res_x.get(j) + " " + res_y.get(j) + " " + res_z.get(j) + '\n');
            }
        }

        System.out.println(((double) (System.currentTimeMillis() - ms) / 1000) + " s");
        System.out.println(count + " iteration");
        // Console.WriteLine("{0} {1}\t{2}", clock.Elapsed, count, E);
    }

    public static void normir() throws IOException {

        normir_vector(x, 0);
        normir_vector(y, 1);
        normir_vector(z, 2);

    }

    public static void post_normir(List<Double> vector, int metka) {
        for (int j = 0; j < vector.size(); j++) {
            vector.set(j, (vector.get(j) * (max[metka] - min[metka]) + (max[metka] + min[metka])) / 2);
        }
    }

    public static void normir_vector(List<Double> vector, int metka) throws IOException {
        double d = vector.get(0), b = vector.get(0);
        for (int j = 0; j < vector.size(); j++) {//ищем максимальное число
            if (vector.get(j) > d) {
                d = vector.get(j);
            }
            if (vector.get(j) < b) {
                b = vector.get(j);
            }
        }
        max[metka] = d;
        min[metka] = b;
        for (int j = 0; j < vector.size(); j++) {//нормируем числа от -1 до1
            vector.set(j, (2 * vector.get(j) - (d + b)) / (d - b));
        }
    }
}
