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
 
def scalar_multiply_normal(x, y):
	res = 0
	for i in range(len(x)):
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
		divisor = scalar_multiply_normal(f, f)
		if divisor == 0:
			divisor = 1
		w.append(scalar_multiply_normal(y, f) / divisor)
	return w
 
 
def count_Q(w):
	res = 0
	for obj in objects:
		res += (scalar_multiply(obj, w) - obj[m]) ** 2
	return res
 
def next_Q(Q, w, obj, a):
	return Q * (1 - a) + a * ((scalar_multiply(obj, w) - obj[m]) ** 2)
 
# stochastic gradient descent
def sgd(iterations, initial_w, step_k):
	w = initial_w
	Q = count_Q(w)
	prec = 1e-7
	prev_Q = 0
	#print("step", step)
	for i in range(iterations):
		obj = objects[i % n]
		der = scalar_multiply(obj, w) - obj[m]
		delta = mult(obj, der)
		#print(delta, component, der, obj, w, scalar_multiply(obj, w))
		step = count_step(obj) * step_k
		#print("obj", obj)
		#print_info(delta, step, w, Q, i)
		prev_Q = Q
		Q = next_Q(Q, w, obj, 1 / iterations)
		w = minus(w, mult(delta, step))
		if abs(Q - prev_Q) < prec or Q > 1e20:
			return [w, Q]
	return [w, Q]
 
 
def init_delta():
	delta = []
	for i in range(m):
		delta.append(0)
	return delta
 
def zero_w():
	delta = []
	for i in range(m):
		delta.append(0)
	return delta
 
 
def print_info(delta, step, w, Q, i):
	print("delta:", delta)
	print("step:", step)
	print("w:", w)
	print("Q:", Q)
	print("iteration", i)
	print()
 
def gd(iterations, initial_w, step_k):
	w = initial_w
	Q = count_Q(w)
	prec = 0.000003
	prev_Q = 0
	step = count_step(objects[0]) * step_k
	#print("step", step)
	for i in range(iterations):
		delta = init_delta()
 
		for obj in objects:
			der = scalar_multiply(obj, w) - obj[m]
			component = mult(obj, der)
			delta = plus(delta, component)
			#print(delta, component, der, obj, w, scalar_multiply(obj, w))
		#step = count_step_by_delta(delta) * (1 / (i + 1))
		#print_info(delta, step, w, Q, i)
		w = minus(w, mult(delta, step))
		prev_Q = Q
		Q = count_Q(w)
		if abs(Q - prev_Q) < prec or Q > max_Q:
			return [w, Q]
	return [w, Q]
 
 
def count_step_by_delta(delta):
	sum = 0
	for i in range(m):
		sum += delta[i] ** 2
	return 1 / math.sqrt(sum)
 
 
def count_step(x):
	sum = 0
	for i in range(m):
		sum += x[i] ** 2
	return 1 / sum
 
def count_w(x):
	w = []
	sumx = 0
	for i in range(m):
		sumx += x[i]
		w.append(1)
	w[0] = x[m] - sumx + 1
	return w
 
 
 
line = input().split(" ")
n = int(line[0])
m = int(line[1])
 
m += 1
 
objects = []
 
max_iter = 600
 
for i in range(n):
	obj = list(map(int, input().split(" ")))
	obj.insert(0, 1)
	objects.append(obj)
 
 
 
#w = count_initial_w()
w = count_w(objects[0])
#w = zero_w()
#print(w)
 
max_Q = 1e40
 
 
def try_steps_and_ws():
	opt = [[], max_Q]
	i_count = 1
	for i in range(i_count):
		res = sgd(max_iter, w, 0.25)
		#print(i * 0.02, res)
		if res[1] < opt[1]:
			opt = res
	return opt
 
 
 
 
opt = try_steps_and_ws()
 
#opt = sgd(max_iter, w, 0.005)
 
#print(opt[0])
#print(opt[1])
 
#print ("opt", opt)
 
for i in range(m - 1):
	print(opt[0][i + 1])
print(opt[0][0])
#print("Q", opt[1])