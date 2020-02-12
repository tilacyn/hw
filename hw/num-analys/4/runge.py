from mpl_toolkits import mplot3d

import numpy as np
import matplotlib.pyplot as plt
import random

s = 10
r = 28
b = 8 / 3


def dx_dt(x, y, z, h):
    return s * (y - x) * h

def dy_dt(x, y, z, h):
    return (r * x - y - x * z) * h

def dz_dt(x, y, z, h):
    return (x * y - b * z) * h

def runge_kutta(x0, y0, z0, h, dt=1e-2, n_iter=10000):
    xs = [0.0] * n_iter
    ys = [0.0] * n_iter
    zs = [0.0] * n_iter

    xs[0] = x0
    ys[0] = y0
    zs[0] = z0

    for i in range(1, n_iter):
        x_n = xs[i - 1]
        y_n = ys[i - 1]
        z_n = zs[i - 1]

        k1 = dx_dt(x_n, y_n, z_n, h)
        k2 = dx_dt(x_n + 0.5 * dt * k1, y_n, z_n, h)
        k3 = dx_dt(x_n + 0.5 * dt * k2, y_n, z_n, h)
        k4 = dx_dt(x_n + dt * k3, y_n, z_n, h)
        x = dt * (k1 + 2 * k2 + 2 * k3 + k4) / 6

        k1 = dy_dt(x_n, y_n, z_n, h)
        k2 = dy_dt(x_n, y_n + 0.5 * dt * k1, z_n, h)
        k3 = dy_dt(x_n, y_n + 0.5 * dt * k2, z_n, h)
        k4 = dy_dt(x_n, y_n + dt * k3, z_n, h)
        y = dt * (k1 + 2 * k2 + 2 * k3 + k4) / 6

        k1 = dz_dt(x_n, y_n, z_n, h)
        k2 = dz_dt(x_n, y_n, z_n + 0.5 * dt * k1, h)
        k3 = dz_dt(x_n, y_n, z_n + 0.5 * dt * k2, h)
        k4 = dz_dt(x_n, y_n, z_n + dt * k3, h)
        z = dt * (k1 + 2 * k2 + 2 * k3 + k4) / 6

        xs[i] = x_n + x
        ys[i] = y_n + y
        zs[i] = z_n + z

    return xs, ys, zs

def main():
    x0 = 1
    y0 = 1
    z0 = 1

    for x in range(5):
        r = random.uniform(1.0, 30.0)

        print(r)

        res = runge_kutta(x0, y0, z0, 0.2)
        res_x = res[0]
        res_y = res[1]
        res_z = res[2]

        #fig = plt.figure()
        ax = plt.axes(projection="3d")
        ax.scatter3D(res_x, res_y, res_z, c=res_z, cmap='hsv');

        plt.show()
