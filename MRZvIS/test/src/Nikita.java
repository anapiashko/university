import java.io.*;

public class Nikita {
    static int n1 = 10, n2 = 50, n3 = 1, n = 1000;//n=2000
    static double[][] w12 = new double[n1][n2];
    static double[] T12 = new double[n2];
    static double[][] w23 = new double[n2][n3];
    static double[][] w23t = new double[n2][n3];
    static double[] T23 = new double[n3];
    static double[] x = new double[n];
    static double[] y = new double[n];
    static double[] z = new double[n];
    static double[] x_post = new double[n];
    static double[] y_post = new double[n];
    static double[] z_post = new double[n];
    static double[][] vector_x = new double[n][n1];
    static double[][] e = new double[n][n3];
    static double a = 0.01;
    static double[] max =new double[3];
    static double[] min =new double[3];


    public static void main(String[] args) throws IOException {
        normir();
        init();
        double[] y1 = new double[n2];//в лекциях вместо "у1" использовали "р"
        double[] s1 = new double[n2];
        double[] y2 = new double[n3];//"у2" эквивалентно "у"
        double[] s2 = new double[n3];
        double E = 0, E_pred = 0, E_o = 0, Em = 0, Em_test = 0, E_test = 0, E_zelaemay = 0.001, dE = 0.000000000001;
        /*
        E_zelaemay - желаемая ошибка НС после обучения
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
        int j = 0;
        for (j = 0; ; j++) {//эпоха
            E = 0;
            Em = 0;
            E_o = 0;
            if (j % 100 == 0) {
                System.out.println("\n \n \n \n j= " + j);
            }
            for (int i = 0; i < 0.7 * n - n1; i++) {//образ
                //обучаем НС на 70% данных == 0.7*n
                for (int i1 = 0; i1 < n2; i1++) {
                    s1[i1] = 0;
                }
                for (int t = 0; t < n2; t++) {
                    for (int o = 0; o < n1; o++) {
                        s1[t] += w12[o][t] * vector_x[i][o];
                    }
                    s1[t] -= T12[t];
                }
                for (int t = 0; t < n2; t++) {//ф-я активации гиперболический тангенс
                    y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    s2[i1] = 0;
                }
                for (int t = 0; t < n3; t++) {
                    for (int o = 0; o < n2; o++) {
                        s2[t] += w23[o][t] * y1[o];
                    }
                    s2[t] -= T23[t];
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    y2[i1] = s2[i1];// линейная ф-я активации
                }
                /////////////////////обучение///////////////////////////////
                for (int i1 = 0; i1 < n3; i1++) {
                    E += ((Math.pow(y2[i1] - e[i][i1], 2)) / 2);
                    Em_test += ((Math.abs(y2[i1] - e[i][i1])) / (Math.abs(y2[i1] + e[i][i1]) / 2)) * 100;
                }
                if (j % 100 == 0) {
                    System.out.println("Е= " + E);
                    System.out.println("y2[i1]= " + y2[0]);
                    System.out.println("e[i][i1]= " + e[i][0]);
                }
                for (int i1 = 0; i1 < n2; i1++) {
                    for (int j1 = 0; j1 < n3; j1++) {
                        w23t[i1][j1] = w23[i1][j1];//запоминаем вес до изменения
                    }
                }
                for (int t = 0; t < n3; t++) {//слой 2-3 или i-j
                    for (int o = 0; o < n2; o++) {
                        w23[o][t] -= (a * (y2[t] - e[i][t]) * y1[o]);
                    }
                    T23[t] += (a * (y2[t] - e[i][t]));
                }
                for (int t = 0; t < n2; t++) {//слой 1-2 или k-i
                    for (int o = 0; o < n1; o++) {
                        w12[o][t] -= (a * ((y2[0] - e[i][0]) * w23t[t][0]) * ((1 - y1[t] * y1[t])) * vector_x[i][o]);
                    }
                    T12[t] += (a * ((y2[0] - e[i][0]) * w23t[t][0]) * ((1 - y1[t] * y1[t])));
                }
            }
            if (E <= E_zelaemay) {
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
                    s1[i1] = 0;
                }
                for (int t = 0; t < n2; t++) {
                    for (int o = 0; o < n1; o++) {
                        s1[t] += w12[o][t] * vector_x[i][o];
                    }
                    s1[t] -= T12[t];
                }
                for (int t = 0; t < n2; t++) {//ф-я активации гиперболический тангенс
                    y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    s2[i1] = 0;
                }
                for (int t = 0; t < n3; t++) {
                    for (int o = 0; o < n2; o++) {
                        s2[t] += w23[o][t] * y1[o];
                    }
                    s2[t] -= T23[t];
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    y2[i1] = s2[i1];// линейная ф-я активации
                }
                for (int i1 = 0; i1 < n3; i1++) {
                    E_o += ((Math.pow(y2[i1] - e[i][i1], 2)) / 2);
                }
            }
            if (E_o <= E_zelaemay) {
                /*
                сеть обрела хорошую обощающую способность -
                останавливаем обученипе
                 */
                System.out.println("сеть обрела хорошую обощающую способность -\n" +
                        "               останавливаем обучение ");
                System.out.println("E_о= " + E_o);
                break;
            }
        }
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println(" времени обучения НС, sec: " + (elapsed / (1000)));
        Em /= (0.7 * n - n1); //0.7*n - это колличество образовобразов,
        // на которых производится обучение;
        System.out.println("колличество итераций j= " + j);
        System.out.println("На обучаемых данных ошибка Е= " + E);
        System.out.println("На обучаемых данных абсолютная ошибка Ем= " + Em);
//после обучения НС проверяем на оставшихся 15%, не входящих в обучение, работу НС
        E_test = 0;
        for (int i = (int) (0.85 * n - n1); i < n - n1; i++) {
            for (int i1 = 0; i1 < n2; i1++) {
                s1[i1] = 0;
            }
            for (int t = 0; t < n2; t++) {
                for (int o = 0; o < n1; o++) {
                    s1[t] += w12[o][t] * vector_x[i][o];
                }
                s1[t] -= T12[t];
            }
            for (int t = 0; t < n2; t++) {//ф-я активации гиперболический тангенс
                y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
            }
            for (int i1 = 0; i1 < n3; i1++) {
                s2[i1] = 0;
            }
            for (int t = 0; t < n3; t++) {
                for (int o = 0; o < n2; o++) {
                    s2[t] += w23[o][t] * y1[o];
                }
                s2[t] -= T23[t];
            }
            for (int i1 = 0; i1 < n3; i1++) {
                y2[i1] = s2[i1];// линейная ф-я активации
            }
            for (int i1 = 0; i1 < n3; i1++) {
                E_test += ((Math.pow(y2[i1] - e[i][i1], 2)) / 2);
                Em_test += ((Math.abs(y2[i1] - e[i][i1])) / (Math.abs(y2[i1] + e[i][i1]) / 2)) * 100;
            }
        }
        Em_test /= (0.15 * n - n1); //0.15*n - это колличество образовобразов,
        // на которых производится обучение;
        System.out.println("На тестовых (не обучаемых) данных ошибка Е= " + E_test);
        System.out.println("На тестовых (не обучаемых) данных абсолютная ошибка Ем= " + Em_test);
        /*запишем в файл полученные значения после обучения чтобы изобразить на графике точность НС*/
        in_file();
    }

    ////////////////////////////////////////////////////////////
    public static void normir() throws IOException {
        File file = new File("xyz1.txt");
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
        }
        normir_x(x, 0);
        normir_x(y,1);
        normir_x(z, 2);
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

    public static void normir_x(double[] x, int metka) throws IOException {
        double d=x[0], b=x[0];
        for (int j = 0; j < x.length; j++) {//ищем максимальное число
            if (x[j] > d) {
                d = x[j];
            }
            if (x[j] < b) {
                b = x[j];
            }
        }
        max[metka]=d;
        min[metka]=b;
        for (int j = 0; j < x.length; j++) {//нормируем числа от -1 до1
            x[j] = (2 * x[j] - (d + b)) / (d - b);
        }
    }
    public static void in_file() throws IOException {
        double[] y1 = new double[n2];//в лекциях вместо "у1" использовали "р"
        double[] s1 = new double[n2];
        double[] y2 = new double[n3];//"у2" эквивалентно "у"
        double[] s2 = new double[n3];
        for (int i = 0; i < n-n1; i++) {
            for (int i1 = 0; i1 < n2; i1++) {
                s1[i1] = 0;
            }
            for (int t = 0; t < n2; t++) {
                for (int o = 0; o < n1; o++) {
                    s1[t] += w12[o][t] * vector_x[i][o];
                }
                s1[t] -= T12[t];
            }
            for (int t = 0; t < n2; t++) {//ф-я активации гиперболический тангенс
                y1[t] = (Math.exp(s1[t]) - Math.exp(-s1[t])) / (Math.exp(s1[t]) + Math.exp(-s1[t]));
            }
            for (int i1 = 0; i1 < n3; i1++) {
                s2[i1] = 0;
            }
            for (int t = 0; t < n3; t++) {
                for (int o = 0; o < n2; o++) {
                    s2[t] += w23[o][t] * y1[o];
                }
                s2[t] -= T23[t];
            }
            for (int i1 = 0; i1 < n3; i1++) {
                y2[i1] = s2[i1];// линейная ф-я активации
            }
            x_post[i+n1]=y2[0];
        }
        post_normir(x, 0);
        File file1 = new File("x(t)_NN.txt");
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
    public static void post_normir(double[] x, int metka){
        for (int j = 0; j < x.length; j++) {//нормируем числа от -1 до1
            x[j]=(x[j]*(max[metka]-min[metka])+(max[metka]+min[metka]))/2;
        }
    }
    public static void init() throws IOException {
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                w12[i][j] = Math.random();
            }
        }
        for (int j = 0; j < n2; j++) {
            T12[j] = Math.random();
        }
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < n3; j++) {
                w23[i][j] = Math.random();
            }
        }
        for (int j = 0; j < n3; j++) {
            T23[j] = Math.random();
        }
        File file = new File("xyz_Normir.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        int i1=0;
        for (int i=0;(str = br.readLine()) != null && i<n;i++){
            String[] subStr;
            String delimeter = " "; // Разделитель
            subStr = str.split(delimeter);
            x[i]= Double.parseDouble(subStr[0]);
            y[i]= Double.parseDouble(subStr[1]);
            z[i]= Double.parseDouble(subStr[2]);
            i1=i;
        }
        for (int i=0; i<x.length-n1;i++) {
            for ( int j=0; j<n1;j++) {
                vector_x[i][j] = x[i+j];
            }
            e[i][0]=x[i+n1];
        }
    }
}
