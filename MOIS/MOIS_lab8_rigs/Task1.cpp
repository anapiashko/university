#include <algorithm>
#include <iostream>
#include <vector>
#include <unordered_set>

using namespace std;

class Task1 {

private:
    vector<int> ring;
    vector<vector<int>> ideals;

public:

    Task1(vector<int> vector) {
        ring = vector;
    }

    void CayleyTable(vector<int> ring) {

        cout << "Таблица Кэли по сложению:" << "\n+ mod" << ring.size() << "\t";

        for (int i : ring) {
            cout << i << "\t";
        }
        cout << endl << endl;
        for (int i = 0; i < ring.size(); ++i) {
            cout << ring[i] << "\t";
            for (int j = 0; j < ring.size(); ++j) {
                cout << (i + j) % ring.size();
                if (j != ring.size() - 1)
                    cout << "\t";
            }
            cout << endl;
        }

        cout << "\nТаблица Кэли по умножению:" << "\n* mod" << ring.size() << "\t";
        for (int i = 0; i < ring.size(); ++i) {
            cout << ring[i] << "\t";
        }

        cout << endl << endl;
        for (int i = 0; i < ring.size(); ++i) {

            cout << ring[i] << "\t";
            for (int j = 0; j < ring.size(); ++j) {
                cout << (i * j) % ring.size();
                if (j != ring.size() - 1)
                    cout << "\t";
            }
            cout << endl;
        }
    }

    bool RingProperties() {

        for (int i = 0; i < ring.size(); ++i)
            for (int j = 0; j < ring.size(); ++j)
                if ((i * j) % ring.size() != (j * i) % ring.size())
                    return false;
        return true;
    }

    bool WithOrWithoutUnit() {

        for (int i = 0; i < ring.size(); ++i)
            for (int j = 0; j < ring.size(); ++j)
                if ((i * j) % ring.size() == (j * i) % ring.size() && (i * j) % ring.size() == i % ring.size())
                    return true;
        return false;
    }

    void MultiplicativeGroupAndInverseElements() {

        vector<int> multGroup;
        for (int i = 0; i < ring.size(); ++i) {
            for (int j = 0; j < ring.size(); ++j) {
                if ((i * j) % ring.size() == 1) {
                    multGroup.push_back(i);
                }
            }
        }
        vector<int> mGroup;
        mGroup.insert(mGroup.end(), multGroup.begin(), multGroup.end());
        cout << "Мультипликативная группа кольца: R* = { ";
        for (int i = 0; i < mGroup.size(); i++) cout << mGroup[i] << " ";
        cout << "}" << endl;

        cout << "\nТаблица Кэли для мультипликативной группы:" << "\n* mod" << ring.size() << "\t";
        for (int i = 0; i < mGroup.size(); ++i)
            cout << mGroup[i] << "\t";
        cout << "\n";
        for (int i = 0; i < mGroup.size(); ++i) {
            cout << mGroup[i] << "\t";
            for (int j = 0; j < mGroup.size(); ++j) {
                cout << (mGroup[i] * mGroup[j]) % ring.size();
                if (j != mGroup.size() - 1)
                    cout << "\t";
            }
            cout << endl;
        }

        cout << "Обратные элементы кольца:  ";
        for (int i = 0; i < mGroup.size(); ++i)
            for (int j = 0; j < mGroup.size(); ++j)
                if ((mGroup[i] * mGroup[j]) % ring.size() == 1)
                    cout << mGroup[i] << "-->" << mGroup[j] << "  ";
        cout << endl;
    }

    void RingCharacteristic() {

        int n = 0;
        for (int i = 0; i < ring.size(); ++i) {

            int check = 0;
            for (int j = 0; j < ring.size(); j++) {
                int na = 0;
                for (int k = 0; k < i + 1; k++) {
                    na += ring[j];
                }
                if (na % ring.size() == 0) {
                    check++;
                    continue;
                } else
                    break;
            }
            if (check == ring.size())
                n = i + 1;
        }
        cout << "Характеристика кольца: " << n << endl;
    }

    void ZeroDivisors() {

        vector<vector<int>> zeroDiv;
        for (int i = 1; i < ring.size(); ++i)
            for (int j = 1; j < ring.size(); ++j)
                if ((ring[i] * ring[j]) % ring.size() == 0 && ring[i] != 0 && ring[j] != 0) {
                    vector<int> zero;
                    zero.push_back(ring[i]);
                    zero.push_back(ring[j]);
                    zeroDiv.push_back(zero);
                }

        cout << "Делители нуля: ";
        for (int i = 0; i < zeroDiv.size(); i++)
            cout << "(" << zeroDiv[i][0] << ";" << zeroDiv[i][1] << ")  ";
        cout << endl;
    }

    void RingIdealsAndMainIdeals() {

        vector<int> elem;
        for (int i = 0; i < ring.size(); ++i) {
            vector<int> subgr;
            int tmp = 0;
            do {
                tmp = (tmp + ring[i]) % ring.size();
                subgr.push_back(tmp);
            } while (tmp != 0);
            sort(subgr.begin(), subgr.end());
            if (!CheckIdeals(subgr) && subgr.size() != 1 && subgr.size() != ring.size()) {
                ideals.push_back(subgr);
                elem.push_back(ring[i]);
            }
        }
        cout << "Идеалы кольца:" << endl;
        for (int i = 0; i < ideals.size(); i++) {
            cout << "I" << (i + 1) << " = { ";
            for (int j = 0; j < ideals[i].size(); j++)
                cout << ideals[i][j] << " ";
            cout << "}" << endl;
        }
        cout << "Главные идеалы кольца:" << endl;
        for (int i = 0; i < ideals.size(); i++) {
            cout << "I" << (i + 1) << "' = { ";
            for (int j = 0; j < ideals[i].size(); j++)
                cout << ideals[i][j] << " ";
            cout << "} = <" << elem[i] << ">" << endl;
        }
    }

    bool CheckIdeals(vector<int> subGr) {
        for (int i = 0; i < ideals.size(); i++) {
            int k = 0;
            for (int j = 0; j < ideals[i].size(); j++) {
                if (ideals[i].size() != subGr.size() || ideals[i][j] != subGr[j])
                    break;
                else k++;
            }
            if (k == ideals[i].size())
                return true;
        }
        return false;
    }

    void MaxIdeals() {
        int num = 0;
        cout << "Максимальные идеалы: " << endl;
        for (int i = 0; i < ideals.size(); i++) {
            bool flag = true;
            for (int j = 0; j < ideals.size(); j++) {
                if (i == j) {
                    continue;
                }

                vector<int> idealsj = ideals[j];

                if (ideals[j].size() > ideals[i].size() &&
                    all_of(ideals[i].begin(), ideals[i].end(),
                           [&idealsj](int idealsix) {
                               return any_of(idealsj.begin(), idealsj.end(),
                                             [&idealsix](int idealsjx) { return idealsix == idealsjx; });
                           })) {
                    flag = false;
                }
            }
            if (flag) {
                cout << "I" << num++ << " = { ";
                for (int item : ideals[i])
                    cout << item << " ";
                cout << "}" << endl;
            }
        }
    }

    void FactorRings() {
        int num = 1;
        cout << "Фактор-кольцо для идеала: " << num << endl;
        vector<vector<int>> factorRings;
        factorRings.push_back(ideals[num]);
        cout << "Классы вычетов: " << endl;
        for (int i = 1; i < ring.size(); i++) {
            vector<int> fRing;
            for (int j = 0; j < ideals[num].size(); j++) {
                fRing.push_back((ideals[num][j] + ring[i]) % ring.size());
            }
            sort(fRing.begin(), fRing.end());

            if (any_of(factorRings.begin(), factorRings.end(), [&fRing](vector<int> s) { return s == fRing; })) {
                continue;
            }
            factorRings.push_back(fRing);
        }
        for (int i = 0; i < factorRings.size(); i++) {
            cout << "[" << i << "] = { ";
            for (int j = 0; j < factorRings[i].size(); j++) {
                cout  << factorRings[i][j] << " ";
            }
            cout << "}" << endl;
        }
        cout << "Таблица Кэли для индуцированной операции +" << "\n +\t";
        for (int i = 0; i < factorRings.size(); i++) {
            cout << "[" << ring[i]<<"]\t";
        }
        cout << "\n";
        for (int i = 0; i < factorRings.size(); ++i) {
            cout << "[" << ring[i]<<"]\t";
            for (int j = 0; j < factorRings.size(); ++j) {
                cout << "[" << (ring[i] + ring[j]) % factorRings.size()<<"]\t";
            }
            cout << endl;
        }
        cout << "Таблица Кэли для индуцированной операции *" << "\n *\t";
        for (int i = 0; i < factorRings.size(); i++) {
            cout << "["<< ring[i]<<"]\t";
        }
        cout << "\n";
        for (int i = 0; i < factorRings.size(); ++i) {
            cout << "[" << ring[i]<<"]\t";
            for (int j = 0; j < factorRings.size(); ++j) {
                cout << "[" << (ring[i] * ring[j]) % factorRings.size()<<"]\t";
            }
            cout << endl;
        }
    }
};