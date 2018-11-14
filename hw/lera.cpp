#define _USE_MATH_DEFINES
#include <cmath>

#include <fstream>
#include <string>
#include <iostream>

using namespace std;

// input data size
const int N = 200;


int main() {
    ifstream in;

    in.open("eps2_GaAs.dat");

    // arrays for arguments and function values
    double x[N];
    double y[N];

    double integral = 0;


    for (int i = 0; i < N; i++) {
        // reading next argument
        string next_x;
        in >> next_x;
        x[i] = stod(next_x);

        // reading next number from the file, evaluating next function value
        string next_number;
        in >> next_number;
        y[i] = stod(next_number) * 2 / x[i];

        // skipping the first read, because we have N-1 trapeziums
        if (i == 0) continue;

        // evaluating square of the previous trapezium
        double average_y = (y[i] + y[i - 1]) / 2;
        integral += (x[i - 1] - x[i]) * average_y;
    }

    cout << integral / M_PI + 1;

    in.close();

    return 0;
}
