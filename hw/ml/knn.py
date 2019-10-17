import math
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

def fmeasure(precision, recall):
	if precision == 0 and recall == 0:
		return 0
	return 2 * (precision * recall) / (precision + recall)



#=======================
# F MEASURE
#=======================
def count_fmeausure(confusion_matrix):
	matrix = confusion_matrix

	all_elements_n = 0

	for row in matrix:
		for v in row:
			all_elements_n += v

	fs = []
	weights = []

	precisions = []
	recalls = []

	for class_n in range(3):
		diagonal_elem = matrix[class_n][class_n]
		row_sum = sum(matrix[class_n])
		if row_sum == 0 and diagonal_elem == 0:
			precision = 0
		else:
			precision = diagonal_elem / row_sum
		col_sum = 0
		for row in matrix:
			col_sum += row[class_n]
		weights.append(col_sum / all_elements_n)
		if col_sum == 0 and diagonal_elem == 0:
			recall = 0
		else:
			recall = diagonal_elem / col_sum
		precisions.append(precision)
		recalls.append(recall)
		fs.append(fmeasure(precision, recall))

	f_micro = 0

	precision = 0
	recall = 0

	for class_n in range(3):
		f_micro += weights[class_n] * fs[class_n]
		precision += weights[class_n] * precisions[class_n]
		recall += weights[class_n] * recalls[class_n]

	#print(fs)
	#print(weights)

	f_macro = fmeasure(precision, recall)
	return f_macro



#=============================
#= = = DISTANCES = = = = = = =
#=============================






def euclidean(x, y, distance_coefficient):
	different_props_n = 0
	for col_n in categorical_cols:
		if (x[col_n] != y[col_n]):
			different_props_n += 1
	numeric_dist = 0
	for col_n in numeric_cols:
		numeric_dist += (x[col_n] - y[col_n])**2
	return distance_coefficient * (different_props_n) + (1 - distance_coefficient) * math.sqrt(numeric_dist)

def chebyshev(x, y):
	cur_max = 0;
	for i in range(len(x) - 1):
		cur_max = max(abs(x[i] - y[i]), cur_max)
	return cur_max


def manhattan(x, y):
	different_props_n = 0
	for col_n in categorical_cols:
		if (x[col_n] != y[col_n]):
			different_props_n += 1#feature_weights[col_n]
	numeric_dist = 0
	for col_n in numeric_cols:
		numeric_dist += abs(x[col_n] - y[col_n])# * feature_weights[col_n]
	return distance_coefficient * (different_props_n) + (1 - distance_coefficient) * numeric_dist

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



def count_class_metric(class_n, data_set, test_object, kernel_function, distance_function, window, distance_coefficient):
	result = 0
	for obj in data_set:
		if obj[target_value_index] != class_n:
			#print("counting class metrics: ", obj[target_value_index], class_n)
			continue
		result += kernel_function(distance_function(obj, test_object, distance_coefficient) / window)
	return result


def test(train_set, test_object, kernel_function, distance_function, window, k, distance_coefficient):
	best_class = 0
	max_class_metric = 0
	# create k nearest neighbours
	data_set = train_set.tolist()
	data_set.sort(key = lambda obj : distance_function(obj, test_object, distance_coefficient))
	data_set = data_set[:k]
	for class_n in target_values:
		metric = count_class_metric(class_n, data_set, test_object, kernel_function, distance_function, window, distance_coefficient)
		#print("metric", metric)
		if metric > max_class_metric:
			max_class_metric = metric
			best_class = class_n
	return best_class


def loo(kernel_function, distance_function, window, k, distance_coefficient):
	test_passed = 0
	confusion_matrix = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]
	for i in range(len(data)):
		train_set = np.delete(data, i, 0)
		#print(data)
		#print(train_set)
		test_object = data[i]
		result = test(train_set, test_object, kernel_function, distance_function, window, k, distance_coefficient)
		expected_class = target_value_ids[test_object[target_value_index]]
		#print(i, result)
		actual_class = target_value_ids[result]
		confusion_matrix[actual_class][expected_class] += 1
	return confusion_matrix


def mean(i):
	col_sum = 0
	for row in data:
		col_sum += row[i]
	return col_sum / len(data)


def round_row(obj):
	for col_n in numeric_cols:
		obj[col_n] = round(obj[col_n], 2)
	return obj

def sort_objects(i):
	datalist = data.tolist()
	lst = list(map(lambda obj: [round_row(obj), round(euclidean(datalist[i], obj), 4)], datalist))
	lst.sort(key = lambda p: p[1])
	for obj in lst:
		print(obj)


feature_weights = [1, 1, 1, 1, 1, 3, 0, 3, 3, 3, 1]


numeric_cols = [1, 2, 7, 8, 9, 10]
categorical_cols = [0, 3, 4, 5]
target_value_index = 6

categorical_cols_number = 4
numeric_cols_number = 6

target_values = ["'Sports'", "'Popular'", "'Grades'"]
target_value_ids = {
	"'Sports'": 0,
	"'Popular'": 1,
	"'Grades'": 2
}

#================
# READ FILE AND PREPARE DATA
#================

data = pd.read_csv('PopularKids.csv')
data.info()
data = data.values


for col_n in numeric_cols:
	col_mean = mean(col_n)
	for row in data:
		row[col_n] /= col_mean


#============
# declare global variables
#===========


tested_k_n = 25
tested_coef_n = 11

result_table = np.arange(tested_k_n * tested_coef_n).reshape(tested_k_n, tested_coef_n)
result_table = result_table.tolist()


final_table_k = []

# FOR WINDOWS

windows = np.linspace(1, 5, 20).tolist()


#========================
# MAIN FUNCTIONS
#========================

def evaluate_for_k_and_distance():
	best_k = 0
	best_coef = 0
	optima_fm = 0
	window = 3
	for k in range(3, 3 + tested_k_n):
		for coef in range(tested_coef_n):
			distance_coefficient = coef * 0.1
			confusion_matrix = loo(epanechnikov, euclidean, window, k, distance_coefficient)
			res = count_fmeausure(confusion_matrix)
			if res > optima_fm:
				best_k = k
				best_coef = coef
				optima_fm = res
			
			result_table[k - 3][coef] = res

	i = 3;
	for row in result_table:
		print("k =", i, ":", list(map(lambda x: round(x, 3), row)))
		i += 1

	print()
	print()
	print("BEST: ", "F MEASURE =", optima_fm, "K =", best_k, "DISTANCE COEFFICIENT =", best_coef, "WINDOW =", window)
	return result_table


def evaluate_for_windows():
	fm_for_windows = []
	for win in windows:
		coef = 0
		confusion_matrix = loo(epanechnikov, euclidean, win, 18, 0)
		res = count_fmeausure(confusion_matrix)
		fm_for_windows.append(res)
	return fm_for_windows


#====================
# MATPLOTLIB PART
#====================


def create_plot_from_k(table):
	x = range(3, 3 + tested_k_n)
	plt.plot(x, [table[i - 3][0] for i in x])
	plt.xlabel('k')
	plt.ylabel('f macro')

	plt.title("F measure depending on k")

	plt.legend()

	plt.show()


def create_plot_from_window(fm_for_windows):
	x = windows
	plt.plot(x, fm_for_windows)
	plt.xlabel('window')
	plt.ylabel('f macro')

	plt.title("F measure depending on window")

	plt.legend()

	plt.show()









#fm_for_windows = evaluate_for_windows()
#create_plot_from_window(fm_for_windows)
table = evaluate_for_k_and_distance()
create_plot_from_k(table)








