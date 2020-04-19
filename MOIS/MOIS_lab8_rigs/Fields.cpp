#include <algorithm>
#include<iostream>
#include<vector>

using namespace std;

class Fields {
private:
    vector<int> field;
public:
    Fields(vector<int> vector) {
        field = vector;
    }

    void InverseElements() {
        cout << "Обратные элементы поля относительно сложения:  ";
        for (int i = 0; i < field.size(); ++i)
            for (int j = 0; j < field.size(); ++j)
                if ((field[i] + field[j]) % field.size() == 0)
                    cout << field[i] << "-->" << field[j] << "  ";
        cout << endl;

        cout << "Обратные элементы поля относительно умножения:  ";
        for (int i = 0; i < field.size(); ++i)
            for (int j = 0; j < field.size(); ++j)
                if ((field[i] * field[j]) % field.size() == 1)
                    cout << field[i] << "-->" << field[j] << "  ";
        cout << endl;
    }

    void FieldCharacteristic() {
        int n = 0, sum = 0;
        for (int i = 0; i < field.size(); ++i) {
            sum += 1;
            if (sum % field.size() == 0) {
                n = sum;
                break;
            }

        }
        cout << "Характеристика поля: " << n << endl;
    }

    void PrimitiveElements() {
        cout << "Примитивные элементы: ";
        for (int i = 1; i < field.size(); i++) {
            vector<int> primitive;
            int x = field[i];
            for (int j = 0; j < field.size(); j++) {
                x = (x * field[i]) % field.size();
                if (any_of(primitive.begin(), primitive.end(), [&x](int i) { return i == x; })) break;
                primitive.push_back(x);
            }
            if (primitive.size() == field.size() - 1) {
                cout << "<" << field[i]<<">  ";
            }
        }
        cout << endl;
    }

    void EquationSolution() {
        cout << "Уравнение 8*x+11=0." << endl;
        cout << "x = 8^-1*(-11) = ";
        int first = 0, second = 0;
        for (int i = 0; i < field.size(); i++) {
            if ((field[8] * field[i]) % field.size() == 1)
                first = i;
            if ((field[11] + field[i]) % field.size() == 0)
                second = i;
        }
        cout<<first<<" * "<<second <<" = " << first * second % field.size()<<endl;
    }

    void FieldExpansion() {
        cout << "Расширение поля GF(2^4) по модулю x^4+x+1: " << endl;
        cout << "Таблица Кэли по сложению:" << "\n+ mod2\t";
        for (int i = 0; i < 16; ++i)
            cout << i << "\t";
        cout << "\n\n";
        for (int i = 0; i < 16; i++) {
            cout << i << "\t";
            for (int j = 0; j < 16; j++) {
                cout << (i ^ j);
                if (j != 16 - 1)
                    cout << "\t";
            }
            cout << endl;
        }

        cout << "Таблица Кэли по умножению:" << "\n* mod2\t";
        for (int i = 0; i < 16; ++i)
            cout << i << "\t";
        cout << "\n\n";
        for (int i = 0; i < 16; i++) {
            cout << i << "\t";
            for (int j = 0; j < 16; j++) {
                cout << MultMod(i, j) <<" ";
                if (j != 16 - 1)
                    cout << "\t";
            }
            cout << endl;
        }
        cout << ", где: ";
        cout << " 0->0 \n 1->1 \n 2->x \n 3->x+1 \n 4->x^2 \n 5->x^2+1 \n 6->x^2+x \n 7->x^2+x+1 \n 8->x^3 \n 9->x^3+1 \n 10->x^3+x"
                " \n 11->x^3+x+1 \n 12->x^3+x^2 \n 13->x^3+x^2+1 \n 14->x^3+x^2+x \n 15->x^3+x^2+x+1"
                << endl;
    }

private:
    int MultMod(int a, int b) {
        int p = 0;
        int Counter;
        int hi_bit_set;
        for (Counter = 0; Counter < 4; Counter++) {
            if ((b & 1) != 0)
                p ^= a;
            hi_bit_set = (a & 0x8);
            a <<= 1;
            if (hi_bit_set != 0)
                a ^= 0x13; // x^4 + x + 1   //10011
            b >>= 1;
        }
        p &= 0xF;
        return p;
    }
};