import numpy as np


max_mtx_n = 1000

linalg_error = "non singular matrix"

def pseudo_reverse_mtx(train_objects, theta):
	n = len(train_objects)
	if n > max_mtx_n:
		train_objects = train_objects[:max_mtx_n]
		n = max_mtx_n
	#print(train_objects)


	X = np.array(train_objects)[:, :-1]
	y = np.array(train_objects)[:, -1]
	m = np.size(X, 1)
	
	reg_mtx = np.diag(np.full(m, theta))
	#print(n, m)

	#if m > n:
		#reg_mtx = np.pad(reg_mtx, ((0, 0), (0, m - n)), 'constant', constant_values=(0))
#	if n > m:
#		X = np.pad(X, ((0, 0), (0, n - m)), 'constant', constant_values=(0))

#	X = np.add(X, reg_mtx)
	sgd_solution = np.linalg.pinv(X).dot(y)
	

	try:
		direct_solution = np.linalg.inv(np.add(X.T.dot(X), reg_mtx)).dot(X.T).dot(y)
	except np.linalg.linalg.LinAlgError:
		return [linalg_error, sgd_solution]
	return [direct_solution, sgd_solution]

#print(pseudo_reverse_mtx([[1, 2015, 2045], [1, 2016, 2076]], 1))