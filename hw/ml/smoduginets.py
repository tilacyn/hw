import math
import numpy as np
import random


class SMO:
    def __init__(self, kernel, kernel_f, y, C):
        self.kernel = kernel
        self.kernel_f = kernel_f
        self.y = y
        self.C = C
        self.n = len(kernel)
        self.a = np.zeros(self.n)
        self.eps = 1e-4
        self.b = 0
        self.tol = 1e-2
        self.errors = np.zeros(self.n)
        self.steps = int(500)
        for i in range(self.n):
            self.errors[i] = -y[i]
        #print("errors", self.errors)


    def decision(self, j):
        #print(self.a, self.y, self.kernel)
        result = 0
        for i in range(self.n):
            result += self.a[i] * self.y[i] * self.kernel[i][j];
        return result - self.b

    def objective(self, alphas):
        suma = 0
        for a in alphas:
            suma += a
        second = 0
        for i in range(self.n):
            for j in range(self.n):
                second += alphas[i] * alphas[j] * self.kernel[i][j] * self.y[i] * self.y[j]
        return suma - 0.5 * second

    def predict(self, data, obj):
        res = 0
        for i in range(self.n):
            res += self.a[i] * self.y[i] * self.kernel_f(data[i], obj)
        res -= self.b
        if res > 0:
            return 1
        else:
            return -1

    def countLH(self, i, j):
        C = self.C
        a1 = self.a[i]
        a2 = self.a[j]

        if self.y[i] != self.y[j]:
            L = max(0, a2 - a1)
            H = min(C, C + a2 - a1)
        else:
            L = max(0, a1 + a2 - C)
            H = min(C, a2 + a1)
        return L, H

    def optimize_two(self, i, j):
        if i == j:
            return 0
        #print(self.a)

        kernel = self.kernel
        a1 = self.a[i]
        a2 = self.a[j]
        eps = self.eps
        y = self.y

        L, H = self.countLH(i, j)

        if L == H:
            return False

        eta = kernel[i][i] + kernel[j][j] - 2 * kernel[i][j]
        #print("eta:", eta)
        if eta > 0:
            a2_new = a2 + y[j] * (self.errors[i] - self.errors[j]) / eta
            if a2_new < L:
                a2_new = L
            if a2_new > H:
                a2_new = H
        else:
            return False



        if a2_new < eps:
            a2_new = 0
        if a2_new > self.C - eps:
            a2_new = self.C

        if np.abs(a2 - a2_new) < self.tol * (a2 + a2_new + eps):
            return False
        a1_new = a1 + y[i] * y[j] * (a2 - a2_new)
        b1 = self.errors[i] + y[i] * (a1_new - a1) * kernel[i][i] + \
             y[j] * (a2_new - a2) * kernel[i][j] + self.b
        b2 = self.errors[j] + y[i] * (a1_new - a1) * kernel[i][j] + \
             y[j] * (a2_new - a2) * kernel[j][j] + self.b


        if 0 < a1_new < self.C:
            self.errors[i] = 0
        if 0 < a2_new < self.C:
            self.errors[j] = 0


        if 0 < a1_new < self.C:
            b_new = b1
        elif 0 < a2_new < self.C:
            b_new = b2
        else:
            b_new = (b1 + b2) * 0.5

        for k in range(self.n):
            if k == i or k == j:
                continue
            self.errors[k] = self.errors[k] + y[i] * \
                             (a1_new - a1) * kernel[i][k] + y[j] * \
                             (a2_new - a2) * kernel[j][k] + self.b - b_new




        self.b = b_new
        self.a[i] = a1_new
        self.a[j] = a2_new

        #print(self.errors)
        #print(self.a[i], self.a[j])
        #print("b:", self.b)
        #print("objective:", self.objective(self.a))

        #print("optimized:", i, j, '\n', '\n')

        return True


    def solve(self):
        #self.optimize_two(2, 74)
        #self.optimize_two(1, 116)
        #self.optimize_two(2, 25)
        #self.optimize_two(7, 113)
        #self.optimize_two(9, 82)
        #self.optimize_two(12, 103)
        #self.optimize_two(17, 96)
        #self.optimize_two(18, 103)
        #self.optimize_two(19, 66)
        #print(self.errors)
        #print(self.a[2], self.a[74])
        #print("b:", self.b)
        #print("objective:", self.objective(self.a))
        for step in range(self.steps):
            print("step", step)
            for i in range(self.n):
                #j = random.randint(0, self.n - 1)
                j = (i + step + 1) % self.n
                self.optimize_two(i, j)
            print(self.objective(self.a))
        return self.a, self.b







