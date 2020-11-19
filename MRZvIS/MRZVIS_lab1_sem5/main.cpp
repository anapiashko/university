#include <iostream>
#include <cmath>

using namespace std;

int threshold_function(double S);

int main() {

    float a = 0.5;
    const int number_inputs = 2;
//    const int number_input_sets = pow(2, number_inputs);
    float w[number_inputs] = {0, 0};
    float T = 0;
    int x[4][number_inputs] = {{1,  1},
                               {-1, 1},
                               {-1, -1},
                               {1, -1}};
    int e[4] = {-1, -1, 1, -1};
    double error = 1;
    int y = 0;
    int count = 0;

    while (error != 0) {

        error = 0;

        for (int i = 0; i < 4; i++) {

            // считаем взвешанную сумму
            float S = 0;
            for (int j = 0; j < number_inputs; j++) {
                S += x[i][j] * w[j];
            }
            S -= T;


            // определяем y(S)
            y = threshold_function(S);

            if (pow((y - e[i]), 2)) {
                // меняем веса и пороги
                for (int k = 0; k < number_inputs; k++) {
                    w[k] = w[k] - a * x[i][k] * (y - e[i]);
                }
                T = T + a * (y - e[i]);
            }
            cout << "S = " << S << "; y = " << y << ";\te[i] = " << e[i] << endl;

            cout << "w1 = " << w[0] << ", w2 = " << w[1]<<endl;
            cout << "T = " << T << endl;

            error += pow((y - e[i]), 2);
            cout << "------" << endl;
        }
        cout << "------------------" << endl;
        error /= 2;
        cout << "error = " << error << endl;
        count++;
        cout << "count = " << count << endl;
        cout << "------------------" << endl;
}
    return 0;
}

int threshold_function(double S) {
    if (S >= 0) {
        return 1;
    } else {
        return -1;
    }
}