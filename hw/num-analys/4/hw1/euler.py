from mpl_toolkits import mplot3d
import math
import numpy as np
import matplotlib.pyplot as plt
import random


def norm(x, y):
    res = 0
    for i in range(len(x)):
        res += (x[i] - y[i]) ** 2
    return math.sqrt(res)


class System:
	def __init__(self, s, b, r):
		self.s = s
		self.b = b
		self.r = r

	def apply_lorenz(self, t, y):
		return np.array([self.s * (y[1] - y[0]),
						self.r * y[0] - y[1] - y[0] * y[2],
						y[0] * y[1] - self.b * y[2]])

	def apply(self, t, y):
		return np.array([y[1] - t ** 2 + 2, y[0]])

	def next_y_implicit(self, t, y, h):
		eps = 1e-9
		iterations = 1000
		res = y.copy()
		for i in range(iterations):
			old_res = res
			res = self.simple_iter(h, y, old_res)
			if norm(old_res, res) < eps:
				break
		return res

	def simple_iter(self, h, y0, y):
		return [(h * self.s * y[1] + y0[0]) / (1 + h * self.s),
				(h * self.r * y[0] - h * y[0] * y[2] + y0[1]) / (1 + h),
				(h * y[0] * y[1] + y0[2]) / (1 + h * self.b)]


class EulerSolver:
    def __init__(self, n, s, b, r):
        self.system = System(s, b, r)
        self.n = n

    def run_explicit(self, t0, t1, y0):
        h = (t1 - t0) / self.n
        net = np.linspace(t0, t1, self.n)
        y = y0
        result = []
        for t in net:
            y = np.add(y, self.system.apply_lorenz(t, y).dot(h))
            result.append(y)

        return result

    def run_implicit(self, t0, t1, y0):
        h = (t1 - t0) / self.n
        net = np.linspace(t0, t1, self.n)
        y = y0
        result = []
        for t in net:
            y = self.system.next_y_implicit(t, y, h)
            result.append(y)

        return result

n = 10_000
s = 10
b = 8 / 3
r = 28


eulerSolver = EulerSolver(n, s, b, r)

y0 = [10, 10, 10]
t0 = 1
t1 = 10


def run_on_r(rs):
	for r in rs:
		print(r)
		
		eulerSolver = EulerSolver(n, s, b, r)

		explicit_result = eulerSolver.run_explicit(0, t1, y0)
		implicit_result = eulerSolver.run_implicit(0, t1, y0)

		explicit_result = np.array(explicit_result)

		ax = plt.axes(projection="3d")
		ax.scatter3D(explicit_result[:,0], explicit_result[:,1], explicit_result[:,2], cmap='hsv')

		plt.show()


run_on_r([20.927])






