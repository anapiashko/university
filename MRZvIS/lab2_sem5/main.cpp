#include <iostream>
#include <cmath>
#include <ctime>
#include <cstdlib>
#include <unistd.h>

using namespace std;

void init_WT(double Wij[10][4], double *Wj, double *Tj, double *T, int num_x, int n);

double sigmoid(double S);

int main() {

    // y = 0.3cos(0.3x) + 0.07sin(0.3x), 10 входов

    const int n = 4; //кол-во нейронов
    const int num_x = 10; //кол-во входов
    const int L = 50;
    double ref_y[L], x[L];
    double t = 0;
    cout << "reference y : " << endl;
    for (int i = 0; i < L; i++) {
        ref_y[i] = 0.3 * cos(0.3 * t) + 0.07 * sin(0.3 * t);
        x[i] = t;
        cout << "y(" << t << ") = " << ref_y[i] << endl;
        t += 0.1;
    }

//    for(int i = 0; i<50;i++){
//        cout<<x[i]<<";"<<ref_y[i]<<endl;
//    }

    double alfa = 0.02;
    double Em = 0.0001; // желаемая среднеквадратичная ошибка

    double Wij[num_x][n];
    double Wj[n];

    double Tj[n];
    double T;

    init_WT(Wij, Wj, Tj, &T, num_x, n);

    double Yj[n];

    double Sj[n] = {0, 0, 0, 0};

    double Y;

    double S = 0, Ep = 0;

    double E = 1e+25;

    int count = 0;
    while (E > Em) {

        E = 0;

        for (int p = 0; p < 0.7 * L - 10; p++) {

            Sj[0] = Sj[1] = Sj[2] = Sj[3] = 0;
            S = 0;

            for (int j = 0; j < n; j++) {
                for (int i = 0; i < num_x; i++) {
                    Sj[j] += Wij[i][j] * ref_y[i + p];
                }
                Sj[j] -= Tj[j];
            }

            cout << "Sj[0] = " << Sj[0] << "; Sj[1] = " << Sj[1] << "; Sj[2] = " << Sj[2] << "; Sj[3] = " << Sj[3]
                 << endl;

            for (int j = 0; j < n; j++) {
                Yj[j] = sigmoid(Sj[j]);
            }

            cout << "Yj[0] = " << Yj[0] << "; Yj[1] = " << Yj[1] << "; Yj[2] = " << Yj[2] << "; Yj[3] = " << Yj[3]
                 << endl;

            for (int j = 0; j < n; j++) {
                S += Wj[j] * Yj[j];
            }
            S -= T;

            cout << "S = " << S << endl;

            Y = S;

            cout << "Y = " << Y << endl;

            double gamma = Y - ref_y[p + 10];

            for (int j = 0; j < n; j++) {
                Wj[j] = Wj[j] - alfa * gamma * Y * (1 - Y) * Yj[j];
            }

            T = T + (alfa * gamma * ((1 - Y * Y)));

            cout << "Wj[0] = " << Wj[0] << "; Wj[1] = " << Wj[1] << "; Wj[2] = " << Wj[2] << "; Wj[3] = " << Wj[3]
                 << endl;
            cout << "T = " << T << endl;

            double gamma_j[n];
            for (int j = 0; j < n; j++) {
                gamma_j[j] = gamma * Yj[j] * (1 - Yj[j]) * Wj[j];
            }

            for (int j = 0; j < n; j++) {
                for (int i = 0; i < num_x; i++) {
                    Wij[i][j] =
                            Wij[i][j] -
                            alfa * gamma_j[j] * Yj[j] * (1 - Yj[j]) * ref_y[p + i];
                }
                Tj[j] = Tj[j] + alfa * gamma_j[j] * Yj[j] * (1 - Yj[j]);
            }

            cout << "Wij : " << endl;
            for (int i = 0; i < num_x; i++) {
                for (int j = 0; j < n; j++) {
                    cout << Wij[i][j] << " ";
                }
                cout << endl;
            }

            cout << "Tj : ";
            for (int j = 0; j < n; j++) {
                cout << Tj[j] << " ";
            }
            cout << endl;

            E += ((pow(Y - ref_y[p], 2)) / 2);
        }

        count++;
        cout << "E = " << E << endl;
        //sleep(5);
        if (E <= Em) {
            cout << "iter= " << count << endl;
            cout << "E = " << E << endl;
            break;
        }

        if ((abs(E - Ep)) <= (pow(10, -10))) {
            if (count == 0) {
                Ep = E;
                continue;
            }
            cout << "no improvement " << endl;
            cout << "iteration = " << count << endl;
            cout << "E  = " << E << endl;
            break;
        }
        Ep = E;
    }

    E = 0;

    // прогнозирование
    for (int i = 0; i < L - 10; i++) {

        Sj[0] = Sj[1] = Sj[2] = Sj[3] = 0;
        S = 0;

        for (int k = 0; k < 4; k++) {
            for (int o = i, wi = 0; o < i + 10; wi++, o++) {
                Sj[k] += Wij[wi][k] * ref_y[o];
            }
            Sj[k] -= Tj[k];
        }

        for (int k = 0; k < 4; k++) {
            Yj[k] = sigmoid(Sj[k]);
        }

        for (int j = 0; j < 4; j++) {
            S += Wj[j] * Yj[j];
        }
        S -= T;

        Y = S;

       // cout << x[i]<<"; " <<Y<<endl;
        cout << "Y = " << Y;
        cout << ", ref_y =  " << ref_y[i] << endl;

        double err = pow(Y - ref_y[i + 10], 2);
        cout << "err = " << err << endl;
        E += err;

    }

//    // прогнозирование
//    for (int i = 0.7 * L; i < L - 10; i++) {
//
//        Sj[0] = Sj[1] = Sj[2] = Sj[3] = 0;
//        S = 0;
//
//        for (int k = 0; k < 4; k++) {
//            for (int o = i, wi = 0; o < i + 10; wi++, o++) {
//                Sj[k] += Wij[wi][k] * ref_y[o];
//            }
//            Sj[k] -= Tj[k];
//        }
//
//        for (int k = 0; k < 4; k++) {
//            Yj[k] = sigmoid(Sj[k]);
//        }
//
//        for (int j = 0; j < 4; j++) {
//            S += Wj[j] * Yj[j];
//        }
//        S -= T;
//
//        Y = S;
//
//        cout << "Y = " << Y;
//        cout << ", ref_y =  " << ref_y[i] << endl;
//
//        double err = pow(Y - ref_y[i + 10], 2);
//        cout << "err = " << err << endl;
//        E += err;
//    }

    cout << "MSE  = " << E / 2 << endl;
    return 0;
}

double sigmoid(double S) {
    return 1 / (1 + exp(-S));
}

void init_WT(double Wij[10][4], double *Wj, double *Tj, double *T, int num_x, int n) {
    srand(time(NULL));
    for (int i = 0; i < num_x; i++) {
        for (int j = 0; j < n; j++) {
//            Wij[i][j] = ((double)(rand() % 100) ) / 100;
            Wij[i][j] = 0;
        }
    }

    for (int j = 0; j < n; j++) {
        Wj[j] = 0;
        Tj[j] = 0;
    }

    *T = 0.5;
}