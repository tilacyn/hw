
def fmeasure(precision, recall):
	if precision == 0 and recall == 0:
		return 0
	return 2 * (precision * recall) / (precision + recall)


K = int(input())

lines = []

for i in range(K):
	lines.append(input().split(" "))

matrix = []
for i in range(K):
	matrix.append([])
for i in range(K):
	for j in range(K):
		matrix[i].append(int(lines[j][i]))

#print(matrix)

all_elements_n = 0

for row in matrix:
	for v in row:
		all_elements_n += v

fs = []
weights = []

precisions = []
recalls = []

for class_n in range(K):
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

for class_n in range(K):
	f_micro += weights[class_n] * fs[class_n]
	precision += weights[class_n] * precisions[class_n]
	recall += weights[class_n] * recalls[class_n]

#print(fs)
#print(weights)

f_macro = fmeasure(precision, recall)
print(f_macro)
print(f_micro)




#for row in matrix:
#	for x in row:
#		print(x)
#	print()
