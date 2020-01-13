import math


def euclidean(x, y):
	result = 0
	for i in range(len(x) - 1):
		result += (x[i] - y[i])**2
	return math.sqrt(result)

def chebyshev(x, y):
	cur_max = 0;
	for i in range(len(x) - 1):
		cur_max = max(abs(x[i] - y[i]), cur_max)
	return cur_max


def manhattan(x, y):
	result = 0
	for i in range(len(x) - 1):
		result += abs(x[i] - y[i])
	return result

def triangular(x):
	if abs(x) > 1:
		return 0
	else:
		return 1 - abs(x)

def epanechnikov(x):
	if abs(x) > 1:
		return 0
	return 0.75 * (1 - x**2)

def uniform(x):
	if x < 1 and x > -1:
		return 0.5
	else:
		return 0

def quartic(x):
	if abs(x) > 1:
		return 0
	return (15 / 16) * (1 - x**2)**2

def triweight(x):
	if abs(x) > 1:
		return 0
	return (35 / 32) * (1 - x**2)**3

def tricube(x):
	if abs(x) > 1:
		return 0
	return (70 / 81) * (1 - abs(x)**3)**3

def gaussian(x):
	return 1 / math.sqrt(2 * math.pi) * math.e**(-0.5 * x**2)

def cosine(x):
	if abs(x) > 1:
		return 0
	return (math.pi / 4) * math.cos(math.pi * x / 2)

def logistic(x):
	return 1 / (math.e ** x + 2 + math.e ** (-x))

def sigmoid(x):
	return (2 / math.pi) * (1 / (math.e ** x) + (math.e ** (-x)))

def count_class_metric(class_n):
	result = 0
	for obj in objects:
		if obj[m] != class_n:
			continue
		result += kernel_function(distance_function(obj, q) / window)
	return result

	


line = input().split(" ")
n = int(line[0])
m = int(line[1])

objects = []

target_set = set()

for i in range(n):
	obj = []
	line = input().split(" ")
	for j in range(m + 1):
		obj.append(int(line[j]))
		if j == m and obj[j] not in target_set:
			target_set.add(obj[j])
	objects.append(obj)

line = input().split(" ")
q = []
for i in range(m):
	q.append(int(line[i]))


distance_function_name = input().strip()
kernel_function_name = input().strip()

distance_dict = {
	'euclidean': euclidean,
	'manhattan': manhattan,
	'chebyshev': chebyshev
}

kernel_dict = {
	'uniform': uniform,
	'triangular': triangular,
	'epanechnikov': epanechnikov,
	'quartic': quartic,
	'triweight': triweight,
	'tricube': tricube,
	'gaussian': gaussian,
	'cosine': cosine,
	'logistic': logistic,
	'sigmoid': sigmoid
}

distance_function = distance_dict[distance_function_name]
kernel_function = kernel_dict[kernel_function_name]

window_type = input().strip()

if window_type[:5] == "fixed":
	window = int(input())
else:
	window_index = int(input())
	neighbours = sorted(objects, key=lambda obj: distance_function(obj, q))
	window = distance_function(neighbours[window_index], q)


r = 0
kernel_smoothing = 0


for obj in objects:
	if window == 0:
		if obj[:-1] == q:
			weight = kernel_function(0)
		else:
			weight = 0
	else:
		weight = kernel_function(distance_function(obj, q) / window)
	kernel_smoothing += weight
	inc = obj[m] * weight
	r += inc



if window == 0 and kernel_smoothing == 0:
	sum = 0
	for obj in objects:
		sum += obj[m]
	r = sum / n

if kernel_smoothing == 0:
	kernel_smoothing = 1



print(r / kernel_smoothing)


