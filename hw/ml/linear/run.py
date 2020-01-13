import linear as ln
import math
import pseudo_reverse as pr
import numpy as np
import chart
from math import log10, floor

def read_objects(file, data_set, n):
	for i in range(n):
		obj = list(map(int, file.readline().split(" ")))
		obj.insert(0, 1)
		data_set.append(obj)

test_iterations = 1000

def run_and_test_sgd(train_objects, test_objects, m):
	sgd_res = [[], []]
	initial_ws = [ln.zero_w(m), ln.fy_w(train_objects, m)]
	for step_k in step_ks:
		for wi in range(2):
			sgd_res[wi].append(ln.sgd(train_objects, test_iterations, initial_ws[wi], step_k, m)[1])

	sgd_res = np.array(sgd_res)

	(best_initial_w_i, best_step_k_i) = np.unravel_index(np.argmin(sgd_res), sgd_res.shape)

	best_step_k = step_ks[best_step_k_i]
	best_initial_w = initial_ws[best_initial_w_i]

	#print("tested k and intitial w:")
	#print(sgd_res)

	print("best step k:", best_step_k)

	sgd_for_iterations = []

	for iterations in possible_iterations:
		sgd_for_iterations.append(ln.sgd(train_objects, iterations, best_initial_w, best_step_k, m)[0])

	#print(sgd_for_iterations)

	nrmsd_for_iterations = list(map(lambda w: nrmsd(test_objects, w, m), sgd_for_iterations))

	#for obj in test_objects:
	#	print("predicted target:", ln.scalar_multiply_m(obj, w, m))
	#	print("     real target:", obj[m])
	#Q = ln.count_Q(test_objects, w, m, test_n)
	#Q = nrmsd(test_objects, w, m)
	return nrmsd_for_iterations

def rmsd(objects, w, m):
	n = len(objects)
	sum = 0
	for obj in objects:
		deviation = (ln.scalar_multiply_m(obj, w, m) - obj[m]) ** 2
		sum += deviation
	return math.sqrt(sum) / n

def nrmsd(objects, w, m):
	n = len(objects)
	ymin = 1e15
	ymax = -1e15
	for obj in objects:
		if obj[m] > ymax:
			ymax = obj[m]
		if obj[m] < ymin:
			ymin = obj[m]
	return rmsd(objects, w, m) / (ymax - ymin)

def nrmsd_with_exception(objects, w, m):
	if w == pr.linalg_error:
		return 1
	return nrmsd(objects, w, m)


def printrnd(s, l):
	print(s, list(map(lambda w: round(w, -int(floor(log10(abs(w)))) + 2), l)))

def run(filenumber):
	file = open("data/" + str(filenumber) + ".txt", "r")

	test_objects = []
	train_objects = []

	m = int(file.readline())
	m += 1

	#print(m)

	train_n = int(file.readline())
	read_objects(file, train_objects, train_n)
	test_n = int(file.readline())
	read_objects(file, test_objects, test_n)

	print()
	print("filenumber:", filenumber)
	nrmsds = run_and_test_sgd(train_objects, test_objects, m)
	printrnd("nrmsds:", nrmsds)

	chart.add_plot(filenumber, nrmsds, possible_iterations)

	direct_mtx_solutions = []

	for theta in thetas:
		solution_for_theta = pr.pseudo_reverse_mtx(train_objects, theta)
		direct_mtx_solutions.append(solution_for_theta[0])
		svd_mtx_solution = solution_for_theta[1]
	direct_mtx_nrmsds = list(map(lambda w: nrmsd_with_exception(test_objects, w, m), direct_mtx_solutions))
	svd_mtx_nrmsds = nrmsd(test_objects, svd_mtx_solution, m)
	printrnd("direct matrix nrmsds:", direct_mtx_nrmsds)
	print("best direct mtx nrmsd:", min(direct_mtx_nrmsds))
	print("svd matrix nrmsd:", svd_mtx_nrmsds)
	print()
	print()
	print()

possible_iterations = [100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 5000, 7000, 9000, 12000]
step_ks = [0.1, 0.2, 0.25, 0.5, 0.7, 1, 1.2, 1.5]

thetas = [1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7]
#thetas = []
#thetas = [1e4]

for filenumber in range(1, 8):
	run(filenumber)


chart.show_plot()

#for i in range(m - 1):
#	print(opt[0][i + 1])
#print(opt[0][0])
#print("real Q", count_Q(opt[0]))