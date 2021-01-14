import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// my
public class NN1 {

    static double F(double Sj) {
        return ((Math.exp(Sj) - Math.exp(-Sj)) / (Math.exp(Sj) + Math.exp(-Sj)));
    }

    static double[] max = new double[3];
    static double[] min = new double[3];
    static List<Double> x = new ArrayList<>(); // Входные х-значения
    static List<Double> y = new ArrayList<>();// Входные у-значения
    static List<Double> z = new ArrayList<>();// Входные z-значения
    static int n = 1000;
    static int n1 = 3; // Кол-во входных образов
    static final int n2 = 10;

    public static void main(String[] args) throws IOException {

        long ms = System.currentTimeMillis();

        x.add(0.1);
        y.add(0.1);
        z.add(0.1);

        double Emin = 0.008; // Желаемая среднеквадратичная ошибка сети
        double alp = 0.1; // Шаг обучения
        int NumberOfForms = 20; // Кол-во входных образов для обучения
        int hiddenNeurons = 10; // Кол-во нейронов в скрытом слое
        double[] Tj = new double[hiddenNeurons]; // Пороги нейронной сети
        double[] Tk = new double[3];
        double[][] Wij = new double[3][hiddenNeurons]; // Весовые коэффициэнты для входного и скрытого слоя
        double[][] Wjk = new double[hiddenNeurons][3]; // Весовые коэффициэнты для скрытого и выходного слоя
        double[] Pj = new double[hiddenNeurons]; // Значения скрытого слоя
        double[] gamma_j = new double[hiddenNeurons]; // Ошибка для скрытого слоя
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

//        try (FileWriter xxx = new FileWriter("data_x.txt", false)) {
//            for (int j = 0; j < x.size(); j++) {
//                xxx.append(x.get(j).toString());
//                xxx.append('\n');
//            }
//        }
//        try (FileWriter yyy = new FileWriter("data_y.txt", false)) {
//            for (int j = 0; j < y.size(); j++) {
//                yyy.append(y.get(j).toString());
//                yyy.append('\n');
//            }
//        }
//        try (FileWriter zzz = new FileWriter("data_z.txt", false)) {
//            for (int j = 0; j < z.size(); j++) {
//                zzz.append(z.get(j).toString());
//                zzz.append('\n');
//            }
//        }

        // Заполняем рандомными значениями весовые коэффициэнты и пороги нейронной сети
        // инициализация весов и порогов
        Random rand = new Random(LocalDateTime.now().getSecond());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < hiddenNeurons; j++) {
                Wij[i][j] = rand.nextDouble();
                Wjk[j][i] = rand.nextDouble();
            }
            Tk[i] = rand.nextDouble();
        }
        for (int j = 0; j < hiddenNeurons; j++) {
            Tj[j] = rand.nextDouble();
        }

        double sum = 0, E = 0, Esum = 10e+5;

        // нормирование данных
        normir();

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

        // training 
        int count = 0;
        while (Esum > Emin) {
            System.out.println("Esum = " + Esum);
            Esum = 0;
            for (int kk = 0; kk < 0.7 * n - n1; kk++) {

                for (int j = 0; j < hiddenNeurons; j++) {  // Значения скрытого слоя
                    sum = x.get(kk) * Wij[0][j] + y.get(kk) * Wij[1][j] + z.get(kk) * Wij[2][j];
                    Pj[j] = F(sum - Tj[j]);
                }

                for (int j = 0; j < 3; j++) {   // Значения выходного слоя
                    sum = 0;
                    for (int i = 0; i < hiddenNeurons; i++) {
                        sum += Pj[i] * Wjk[i][j];
                    }
                    xyz[j] = F(sum - Tk[j]);

                    switch (j) {
                        case 0: {
                            E += Math.pow((xyz[j] - x.get(kk)), 2) / 2;
                            gamma_k[j] = xyz[j] - x.get(kk);
                            break;
                        }
                        case 1: {
                            E += Math.pow((xyz[j] - y.get(kk)), 2) / 2;
                            gamma_k[j] = xyz[j] - y.get(kk);
                            break;
                        }
                        case 2: {
                            E += Math.pow((xyz[j] - z.get(kk)), 2) / 2;
                            gamma_k[j] = xyz[j] - z.get(kk);
                            break;
                        }
                    }
                }

                // Вычисление ошибок
                for (int j = 0; j < hiddenNeurons; j++) {
                    gamma_j[j] = gamma_k[0] * (1 - xyz[0] * xyz[0]) * Wjk[j][0]
                            + gamma_k[1] * (1 - xyz[1] * xyz[1]) * Wjk[j][1]
                            + gamma_k[2] * (1 - xyz[2] * xyz[2]) * Wjk[j][2];
                }

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < hiddenNeurons; j++) {
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
                        }
                        Wjk[j][i] -= alp * gamma_k[i] * (1 - xyz[i] * xyz[i]) * Pj[j];
                    }
                }
                for (int j = 0; j < hiddenNeurons; j++) {
                    Tj[j] += alp * gamma_j[j] * (1 - Pj[j] * Pj[j]);
                }
                for (int i = 0; i < 3; i++) {
                    Tk[i] += alp * gamma_k[i] * (1 - xyz[i] * xyz[i]);
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

        for (int kk = 0; kk < n; kk++) {

            for (int i = 0; i < hiddenNeurons; i++) {  // Значения скрытого слоя
                sum = res_x.get(kk) * Wij[0][i] + res_y.get(kk) * Wij[1][i] + res_z.get(kk) * Wij[2][i];
                Pj[i] = F(sum - Tj[i]);
            }

            for (int j = 0; j < 3; j++) {   // Значения выходного слоя
                sum = 0;
                for (int i = 0; i < hiddenNeurons; i++) {
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
        for (int i = 0; i < n - 1; i++) {
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

        System.out.println(((double) (System.currentTimeMillis() - ms) / 1000) + " s");
        System.out.println(count + " iteration");
        // Console.WriteLine("{0} {1}\t{2}", clock.Elapsed, count, E);
    }

    public static void normir() throws IOException {

        normir_vector(x, 0);
        normir_vector(y, 1);
        normir_vector(z, 2);

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
