import math
#import random


def L(x):
	return x ** 2

def Lder(x):
	return 2 * x 


def minus(x, y):
	res = []
	for i in range(m):
		res.append(x[i] - y[i])
	return res

def plus(x, y):
	res = []
	for i in range(m):
		res.append(x[i] + y[i])
	return res


def mult(x, c):
	res = []
	for i in range(m):
		res.append(x[i] * c)
	return res

def scalar_multiply(x, y):
	res = 0
	for i in range(m):
		res += x[i] * y[i]
		#print(res)
	return res


def count_initial_w():
	y = []
	w = []
	for i in range(n):
		y.append(objects[i][m])
	for feature in range(m):
		f = []
		for i in range(n):
			f.append(objects[i][feature])
		w.append(scalar_multiply(y, f) / scalar_multiply(f, f))
	return w


def count_Q(w):
	res = 0
	for obj in objects:
		res += (scalar_multiply(obj, w) - obj[m]) ** 2
	return res


# stochastic gradient descent
def sgd(iterations, initial_w):
	w = initial_w
	Q = 0
	for i in range(iterations):
		#index = random.randint(0, n - 1)
		index = 1
		obj = objects[index]
		Larg = scalar_multiply(obj, w) - obj[m]
		w = minus(w, mult(obj, count_learn_rate(obj) * Lder(Larg)))
		Q = count_Q(w)
		print(obj, w, Q, Larg)


def init_delta():
	delta = []
	for i in range(m):
		delta.append(0)
	return delta


def print_info(delta, w, Q, i):
	print("delta:", delta)
	print("w:", w)
	print("Q:", Q)
	print("iteration", i)
	print()

def gd(iterations, initial_w):
	w = initial_w
	Q = count_Q(w)
	prec = 0.000003
	prev_Q = 0
	step = count_step(objects[0]) * 0.1
	#print("step", step)
	for i in range(iterations):
		delta = init_delta()

		for obj in objects:
			delta = plus(delta, mult(obj, scalar_multiply(obj, w) - obj[m]))
		#print(delta)
		print_info(delta, w, Q, i)
		if i % 30 == 0:
			w = minus(w, mult(delta, step * 10))
		else:
			w = minus(w, mult(delta, step))
		prev_Q = Q
		Q = count_Q(w)
		if (abs(Q - prev_Q) < prec):
			return w
	return w




def count_step(x):
	sum = 0
	for i in range(m):
		sum += x[i] ** 2
	return 1 / sum




line = input().split(" ")
n = int(line[0])
m = int(line[1])

m += 1

objects = []

max_iter = 2000

for i in range(n):
	obj = list(map(int, input().split(" ")))
	obj.insert(0, 1)
	objects.append(obj)



w = count_initial_w()
#print(w)

result_w = gd(max_iter, w)

for i in range(m - 1):
	print(result_w[i + 1])
print(result_w[0])
	 

