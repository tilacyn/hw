import math
import numpy as np

def radial(x, y):
    return (x[0] ** 2) * (y[0] ** 2) + (x[1] ** 2) * (y[1] ** 2)

def norm2(x):
    return x[0] * x[0] + x[1] * x[1]

def scalar_2d(p1, p2):
    return p1[0] * p2[0] + p1[1] * p2[1]

def polynomial(p1, p2, deg):
    return (scalar_2d(p1, p2)) ** deg

polynomial2 = lambda x, y: polynomial(x, y, 2)
polynomial3 = lambda x, y: polynomial(x, y, 3)

exponential = lambda x, y, g: math.e ** (-g * (norm2(np.subtract(x[:2], y[:2]))))

def exponentialg(g):
    return lambda x, y: exponential(x, y, g)







def generate_data():
    w = [1, 1]
    w0 = 0.7
    n = 200
    res = []
    for i in range(n):
        x = random.uniform(-1, 1)
        y = random.uniform(-1, 1)
        if radial([x, y], w) - w0 > 0:
            c = 'P'
        else:
            c = 'N'
        res.append([x, y, c])
    return np.array(res, dtype='object')
