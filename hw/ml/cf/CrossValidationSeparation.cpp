#include <vector>
#include <iostream>
#include <cstdlib>
#include <set>
#include <map>
#include <algorithm>

using namespace std;

typedef pair<int, int> pi;

int N, M, K;

bool cmp(const pi& first, const pi& second) {
    return first.first < second.first;
}

int main() {
    cin >> N >> M >> K;

    vector<int> pointers(M, 0);

    vector<int> result[K];

    vector<pi> items;

    for (int i = 0; i < N; i++) {
        int currentClass;
        cin >> currentClass;

        items.push_back({currentClass - 1, i});
    }

    sort(items.begin(), items.end(), cmp);

    int ptr = 0;

    for (auto classAndIndex : items) {
        result[ptr % K].push_back(classAndIndex.second);
        ptr++;
    }

    for (int i = 0; i < K; i++) {
        cout << result[i].size() << " ";
        for (auto kek : result[i]) {
            cout << kek + 1 << " ";
        }
        cout << "\n";
    }

    return 0;
}