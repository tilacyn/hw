import math

max_iter = 5e3

def argmin(arr):
	minv = 1e15
	for i in range(len(arr)):
		if arr[i] < minv:
			minv = arr[i]
			mini = i
	return mini


def argmax(arr):
	maxv = -1e15
	for i in range(len(arr)):
		if arr[i] > maxv:
			maxv = arr[i]
			maxi = i
	return maxi


def zeros(x):
	result = []
	for i in range(x):
		result.append(0)
	return result

def abs(x):
	if x < 0:
		return -x
	else:
		return x


class SMO:
	def __init__(self, kernel, y, C):
		self.kernel = kernel
		self.y = y
		self.C = C
		self.n = len(kernel)
		self.a = zeros(self.n)
		self.eps = 1e-5
		self.b = 0
		self.tol = 1e-5
		self.errors = zeros(self.n)
		for i in range(self.n):
			self.errors[i] = self.decision(i) - y[i]
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
		for i in range(n):
			for j in range(n):
				second += alphas[i] * alphas[j] * self.kernel[i][j] * self.y[i] * self.y[j]
		return suma - 0.5 * second
	
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

	def optimizeTwo(self, i, j):
		self.iterations += 1
		if i == j:
			return 0

		kernel = self.kernel
		a1 = self.a[i]
		a2 = self.a[j]
		eps = self.eps
		y = self.y

		L, H = self.countLH(i, j)

		if L == H:
			return False
		#print("enter optimizeTwo:", i, j, a1, a2, L, H)
		eta = kernel[i][i] + kernel[j][j] - 2 * kernel[i][j]
		if eta > 0:
			a2_new = a2 + y[j] * (self.errors[i] - self.errors[j]) / eta
			#print("a2_new", a2_new)
			if a2_new < L:
				a2_new = L
			if a2_new > H:
				a2_new = H
		else:
			La = self.a.copy()
			Ha = self.a.copy()
			Ha[j] = H
			La[j] = L
			L_obj = self.objective(La)
			H_obj = self.objective(Ha)
			#print("L, H objectives", L_obj, H_obj)
			if L_obj > H_obj + eps:
				a2_new = L
			elif L_obj < H_obj - eps:
				a2_new = H
			else:
				a2_new = a2

		#print(a2_new)


		if a2_new < eps:
			a2_new = 0
		if a2_new > self.C - eps:
			a2_new = self.C

		if abs(a2 - a2_new) < eps * (a2 + a2_new + eps):
			return False
		a1_new = a1 + y[i] * y[j] * (a2 - a2_new)
		b1 = self.errors[i] + y[i] * (a1_new - a1) * kernel[i][i] + \
		                              y[j] * (a2_new - a2) * kernel[i][j] + self.b
		b2 = self.errors[j] + y[i] * (a1_new - a1) * kernel[i][j] + \
		                              y[j] * (a2_new - a2) * kernel[j][j] + self.b

		if a1 > 0 and a1 < self.C:
			b_new = b1
		elif a2 > 0 and a2 < self.C:
			b_new = b2
		else:
			b_new = (b1 + b2) * 0.5

		for k in range(n):
			self.errors[k] = self.errors[k] + y[i] * \
			    (a1_new - a1) * kernel[i][k] + y[j] * \
			     (a2_new - a2) * kernel[j][k] + self.b - b_new




		self.b = b_new
		self.a[i] = a1_new
		self.a[j] = a2_new
		#print(self.a)
		#print("optimized:", i, j)
		
		return True

	def examine_example(self, j):
		a2 = self.a[j]
		r2 = self.errors[j] * self.y[j]
		if (r2 < -self.tol and a2 < self.C) or (r2 > self.tol and a2 > 0):

			number_of_good_a = 0
			for alpha in self.a:
				if alpha != 0 and alpha != self.C:
					number_of_good_a += 1;

			if number_of_good_a > 1:
            	# Use 2nd choice heuristic is choose max difference in error
				if self.errors[j] > 0:
					i = argmin(self.errors)
				elif self.errors[j] <= 0:
					i = argmax(self.errors)
				step_result = self.optimizeTwo(i, j)
				if step_result:
					return 1

			# Loop through non-zero and non-C alphas, starting at a random point
			#for i in np.roll(np.where((self.a != 0) & (self.a != self.C))[0],
			#					np.random.choice(np.arange(self.n))):
			#for 
			#	step_result = self.optimizeTwo(i, j)
			#	if step_result:
			#		return 1

	        # loop through all alphas, starting at a random point
			for i in range(n):
				step_result = self.optimizeTwo(i, j)
				if step_result:
					return 1
		return 0

	def solve(self):
		numChanged = 0
		examineAll = 1

		self.iterations = 0
		while(numChanged > 0) or (examineAll):
			if self.iterations > max_iter:
				break
			numChanged = 0
			if examineAll:
	            # loop over all training examples
				for i in range(n):
					examine_result = self.examine_example(i)
					numChanged += examine_result
					if examine_result:
						obj_result = self.objective(self.a)
						#print("objective result", obj_result)
			else:
				# loop over examples where alphas are not already at their limits
				for i in range(n):
					if self.a[i] == self.C or self.a[i] == 0:
						continue
					examine_result = self.examine_example(i)
					numChanged += examine_result
					if examine_result:
						obj_result = self.objective(self.a)
						#print("objective result", obj_result)
			if examineAll == 1:
				examineAll = 0
			elif numChanged == 0:
				examineAll = 1
		return self.a, self.b




 
 
n = int(input())
 
distances = []

targets = []

for i in range(n):
	obj_distances = list(map(int, input().split(" ")))
	y = obj_distances[-1]
	obj_distances = obj_distances[:-1]
	distances.append(obj_distances)
	targets.append(y)

c = int(input())

smo = SMO(distances, targets, c)

a, b = smo.solve()

#print()
#print()
#print("RESULT:")

for alpha in a:
	print(alpha)

print(-b)


#for i in range(n):
	#print(i, smo.decision(i))
 


