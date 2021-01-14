import java.io.*;

public class NN3 {
    static int n1 = 10, n2 = 4, n3 = 3, n = 1000;//n=2000
    static double[][] Wij = new double[n1][n2];
    static double[] Tj = new double[n2];
    static double[][] Wjk = new double[n2][n3];
    static double[][] Wjkt = new double[n2][n3];
    static double[] Tk = new double[n3];
    static double[] x = new double[n];
    static double[] y = new double[n];
    static double[] z = new double[n];
    static double[] x_post = new double[n];
    static double[][] vector_x = new double[n][n1];
    static double[][] e = new double[n][n3];
    static double a = 0.01;
    static double[] max = new double[3];
    static double[] min = new double[3];

    //ф-я активации гиперболический тангенс
    static double F(double Sj) {
        return ((Math.exp(Sj) - Math.exp(-Sj)) / (Math.exp(Sj) + Math.exp(-Sj)));
    }

    public static void main(String[] args) throws IOException {
        normir();
        init_WT();
        double[] Pj = new double[n2];//в лекциях вместо "у1" использовали "р"
        double[] Sj = new double[n2];
        double[] y2 = new double[n3];//"у2" эквивалентно "у"
        double[] Sk = new double[n3];
        double E = 10, E_pred = 0, E_o = 0, Em = 0, Em_test = 0, E_test = 0, Emin = 0.001;
        /*
        Emin - желаемая ошибка НС после обучения
        Е - суммарная квадратичная ошибка
        Еm - суммарное значение абсолютной ошибки
        Em_test - ... для данных не входящих в обучение
        E_pred - суммарная квадратичная ошибка предыдущего образа
            для оценки улучшения работы НС после измениний весов и порогов
        dE - для оценки изменений между двумя эпохами, если изменения не
             значительны останавливаем обучение
        E_test - суммарная квадратичная ошибка НС на данных не входящих
            в обучение, после обучения сети
        E_o- суммарная квадратичная ошибка для проверки обобщающей способности
            сети, в случае достижения "хорошей" обощающей способности на данных
            не входящих в обучение - остановливаем обучение сети
        */
        //замерим время обучения сети
        long start = System.currentTimeMillis();
        File file1 = new File("E.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file1);
            int count = 0;
            while (E > Emin) {//эпоха
                E = 0;
                Em = 0;
                E_o = 0;

                for (int i = 0; i < 0.7 * n - n1; i++) {//образ
                    //обучаем НС на 70% данных == 0.7*n
                    for (int i1 = 0; i1 < n2; i1++) {
                        Sj[i1] = 0;
                    }
                    for (int j = 0; j < n2; j++) {
                        for (int o = 0; o < n1; o++) {
                            Sj[j] += Wij[o][j] * vector_x[i][o];
                        }
                        Sj[j] -= Tj[j];
                    }
                    for (int t = 0; t < n2; t++) {
                        Pj[t] = F(Sj[t]);
                    }

                    Sk[0] = Sk[1] = Sk[2] = 0;

                    for (int t = 0; t < n3; t++) {
                        for (int o = 0; o < n2; o++) {
                            Sk[t] += Wjk[o][t] * Pj[o];
                        }
                        Sk[t] -= Tk[t];
                    }
                    for (int i1 = 0; i1 < n3; i1++) {
                        y2[i1] = Sk[i1];
                    }
                    /////////////////////обучение///////////////////////////////
                    for (int i1 = 0; i1 < n3; i1++) {
                        E += ((Math.pow(y2[i1] - e[i][i1], 2)) / 2);
                        Em += ((Math.abs(y2[i1] - e[i][i1])) / (Math.abs(y2[i1] + e[i][i1]) / 2)) * 100;
                    }
                    if (count % 100 == 0) {
                        System.out.println("Е= " + E);
                        System.out.println("y2[i1]= " + y2[0]);
                        System.out.println("e[i][i1]= " + e[i][0]);
                    }
                    for (int i1 = 0; i1 < n2; i1++) {
                        for (int j1 = 0; j1 < n3; j1++) {
                            Wjkt[i1][j1] = Wjk[i1][j1];//запоминаем вес до изменения
                        }
                    }
                    for (int t = 0; t < n3; t++) {//слой j-k
                        for (int o = 0; o < n2; o++) {
                            Wjk[o][t] -= (a * (y2[t] - e[i][t]) * Pj[o]);
                        }
                        Tk[t] += (a * (y2[t] - e[i][t]));
                    }
                    for (int t = 0; t < n2; t++) {//слой 1-2 или k-i
                        for (int o = 0; o < n1; o++) {
                            Wij[o][t] -= (a * ((y2[0] - e[i][0]) * Wjkt[t][0]) * ((1 - Pj[t] * Pj[t])) * vector_x[i][o]);
                        }
                        Tj[t] += (a * ((y2[0] - e[i][0]) * Wjkt[t][0]) * ((1 - Pj[t] * Pj[t])));
                    }
                }
                /*
                Запишем Е в файл для дальнейшего анализа
                 */
                String s = "";
                s += (Double.toString(E));
                s += " ";
                s += (Integer.toString(count));
                s += "\n";
                fr.write(s);
                if (E <= Emin) {
                    System.out.println("достигли желаемой ошибки ");
                    break;
                }
            /*
            в случае незначительности преобразований останавливаем обучение НС
             */
                if (Math.abs(E - E_pred) <= 0.00000001) {
                    System.out.println("нет улучшений ");
                    break;
                }
                E_pred = E;

                //на 15% данных проверяем обобщающую способность сети
                E_o = 0;
                for (int i = (int) (0.7 * n - n1); i < 0.85 * n - n1; i++) {
                    for (int i1 = 0; i1 < n2; i1++) {
                        Sj[i1] = 0;
                    }
                    for (int t = 0; t < n2; t++) {
                        for (int o = 0; o < n1; o++) {
                            Sj[t] += Wij[o][t] * vector_x[i][o];
                        }
                        Sj[t] -= Tj[t];
                    }
                    for (int t = 0; t < n2; t++) {
                        Pj[t] = F(Sj[t]);
                    }
                    for (int i1 = 0; i1 < n3; i1++) {
                        Sk[i1] = 0;
                    }
                    for (int t = 0; t < n3; t++) {
                        for (int o = 0; o < n2; o++) {
                            Sk[t] += Wjk[o][t] * Pj[o];
                        }
                        Sk[t] -= Tk[t];
                    }
                    for (int i1 = 0; i1 < n3; i1++) {
                        y2[i1] = Sk[i1];// линейная ф-я активации
                    }
                    for (int i1 = 0; i1 < n3; i1++) {
                        E_o += ((Math.pow(y2[i1] - e[i][i1], 2)) / 2);
                    }
                }
                if (E_o <= Emin) {
                /*
                сеть обрела хорошую обощающую способность -
                останавливаем обученипе
                 */
//                    System.out.println("сеть обрела хорошую обощающую способность -\n" +
//                            "               останавливаем обучение ");
                    System.out.println("E_о= " + E_o);
                    break;
                }
                count++;
            }
            long finish = System.currentTimeMillis();
            long elapsed = finish - start;
            System.out.println(" sec: " + (elapsed / (1000)));
            Em /= (0.7 * n - n1); //0.7*n - это колличество образовобразов,
            // на которых производится обучение;
            System.out.println("count = " + count);
            System.out.println("На обучаемых данных ошибка Е= " + E);
           // System.out.println("На обучаемых данных абсолютная ошибка Ем= " + Em);

//после обучения НС проверяем на оставшихся 15%, не входящих в обучение, работу НС
            E_test = 0;
            for (int i = (int) (0.85 * n - n1); i < n - n1; i++) {
                for (int i1 = 0; i1 < n2; i1++) {
                    Sj[i1] = 0;
                }
                for (int t = 0; t < n2; t++) {
                    for (int o = 0; o < n1; o++) {
                        Sj[t] += Wij[o][t] * vector_x[i][o];
                    }
                    Sj[t] -= Tj[t];
                }
                for (int t = 0; t < n2; t++) {//ф-я активации гиперболический тангенс
                    Pj[t] = (Math.exp(Sj[t]) - Math.exp(-Sj[t])) / (Math.exp(Sj[t]) + Math.exp(-Sj[t]));
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    Sk[i1] = 0;
                }
                for (int t = 0; t < n3; t++) {
                    for (int o = 0; o < n2; o++) {
                        Sk[t] += Wjk[o][t] * Pj[o];
                    }
                    Sk[t] -= Tk[t];
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    y2[i1] = Sk[i1];// линейная ф-я активации
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    E_test += ((Math.pow(y2[i1] - e[i][i1], 2)) / 2);
                    Em_test += ((Math.abs(y2[i1] - e[i][i1])) / (Math.abs(y2[i1] + e[i][i1]) / 2)) * 100;
                }
            }
            Em_test /= (0.15 * n - n1); //0.15*n - это колличество образовобразов,
            // на которых производится обучение;
            System.out.println("На тестовых (не обучаемых) данных ошибка Е= " + E_test);
           //System.out.println("На тестовых (не обучаемых) данных абсолютная ошибка Ем= " + Em_test);
            /*запишем в файл полученные значения после обучения чтобы изобразить на графике точность НС*/
            in_file();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////
    public static void normir() throws IOException {
        File file = new File("xyz1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        for (int i = 0; (str = br.readLine()) != null && i < n; i++) {
            String[] subStr;
            String delimeter = " "; // Разделитель
            subStr = str.split(delimeter);
            x[i] = Double.parseDouble(subStr[0]);
            y[i] = Double.parseDouble(subStr[1]);
            z[i] = Double.parseDouble(subStr[2]);
        }
        normir_vector(x, 0);
        normir_vector(y, 1);
        normir_vector(z, 2);
        File file1 = new File("xyz_Normir.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file1);
            for (int j = 0; j < x.length; j++) {//ищем максимальное число
                String s = "";
                s += (Double.toString(x[j]));
                s += " ";
                s += (Double.toString(y[j]));
                s += " ";
                s += (Double.toString(z[j]));
                s += "\n";
                fr.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void normir_vector(double[] x, int metka) throws IOException {
        double d = x[0], b = x[0];
        for (int j = 0; j < x.length; j++) {//ищем максимальное число
            if (x[j] > d) {
                d = x[j];
            }
            if (x[j] < b) {
                b = x[j];
            }
        }
        max[metka] = d;
        min[metka] = b;
        for (int j = 0; j < x.length; j++) {//нормируем числа от -1 до1
            x[j] = (2 * x[j] - (d + b)) / (d - b);
        }
    }

    public static void in_file() throws IOException {
        double[] y1 = new double[n2];//в лекциях вместо "у1" использовали "р"
        double[] s1 = new double[n2];
        double[] y2 = new double[n3];//"у2" эквивалентно "у"
        double[] s2 = new double[n3];
        for (int i = 0; i < n - n1; i++) {
            for (int i1 = 0; i1 < n2; i1++) {
                s1[i1] = 0;
            }
            for (int t = 0; t < n2; t++) {
                for (int o = 0; o < n1; o++) {
                    s1[t] += Wij[o][t] * vector_x[i][o];
                }
                s1[t] -= Tj[t];
            }
            for (int t = 0; t < n2; t++) {//ф-я активации гиперболический тангенс
                y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
            }
            for (int i1 = 0; i1 < n3; i1++) {
                s2[i1] = 0;
            }
            for (int t = 0; t < n3; t++) {
                for (int o = 0; o < n2; o++) {
                    s2[t] += Wjk[o][t] * y1[o];
                }
                s2[t] -= Tk[t];
            }
            for (int i1 = 0; i1 < n3; i1++) {
                y2[i1] = s2[i1];// линейная ф-я активации
            }
            x_post[i + n1] = y2[0];
        }
        post_normir(x_post, 2);
        File file1 = new File("z(t)_NN.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file1);
            for (int j = 0; j < x_post.length; j++) {//ищем максимальное число
                String s = "";
                s += (Double.toString(x_post[j]));
                s += "\n";
                fr.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void post_normir(double[] x, int metka) {
        for (int j = 0; j < x.length; j++) {
            x[j] = (x[j] * (max[metka] - min[metka]) + (max[metka] + min[metka])) / 2;
        }
    }

    public static void init_WT() throws IOException {
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                Wij[i][j] = Math.random();
            }
        }
        for (int j = 0; j < n2; j++) {
            Tj[j] = Math.random();
        }
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < n3; j++) {
                Wjk[i][j] = Math.random();
            }
        }
        for (int j = 0; j < n3; j++) {
            Tk[j] = Math.random();
        }
        File file = new File("xyz_Normir.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        int i1 = 0;
        for (int i = 0; (str = br.readLine()) != null && i < n; i++) {
            String[] subStr;
            String delimeter = " "; // Разделитель
            subStr = str.split(delimeter);
            x[i] = Double.parseDouble(subStr[0]);
            y[i] = Double.parseDouble(subStr[1]);
            z[i] = Double.parseDouble(subStr[2]);
            i1 = i;
        }
        for (int i = 0; i < x.length - n1; i++) {
            for (int j = 0; j < n1; j++) {
                vector_x[i][j] = z[i + j];
            }
            e[i][0] = z[i + n1];
        }
    }
}
