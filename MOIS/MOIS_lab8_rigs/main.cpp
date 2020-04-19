#include <iostream>
#include <vector>
#include "Fields.cpp"
#include "Task1.cpp"

using namespace std;

int main() {
    vector<int> ring;
    cout << "Введите количество элементов в множестве: ";
    int n;
    cin >> n;
    for (int i = 0; i < n; i++)
        ring.push_back(i);
    cout << "Кольцо  R=(A, + mod" << n << ", * mod" << n << ")";

    Task1 task1(ring);
    task1.CayleyTable(ring);
    if (task1.RingProperties()) {
        cout << "\nКоммутативное кольцо";
    } else {
        cout << "\nНекоммутативное кольцо";
    }
    if (task1.WithOrWithoutUnit()) {
        cout << " с единицей" << endl;
    } else {
        cout << " без единицы" << endl;
    }

    task1.MultiplicativeGroupAndInverseElements();
    task1.RingCharacteristic();
    task1.ZeroDivisors();
    task1.RingIdealsAndMainIdeals();
    task1.MaxIdeals();
    task1.FactorRings();


    vector<int> field;
    cout << "\nВведите количество элементов в поле: ";
    cin >> n;

    for (int i = 0; i < n; i++)
        field.push_back(i);

    cout << "Поле  GF=(A, + mod" << n << ", * mod" << n << ")" << endl;
    Fields Field(field);
    task1.CayleyTable(field);
    Field.InverseElements();
    Field.FieldCharacteristic();
    Field.PrimitiveElements();
    Field.EquationSolution();
    Field.FieldExpansion();

    return 0;
}