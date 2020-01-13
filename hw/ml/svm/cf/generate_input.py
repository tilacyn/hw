import random


f = open("input", "w")


n = 10

f.write(str(n))
f.write("\n")

for i in range(n):
	for j in range(n):
		f.write(str(random.randint(1, 10)))
		f.write(" ")
	f.write(str(random.choice([-1, 1])))
	f.write("\n")

f.write(str(1))
f.write("\n")

f.close()