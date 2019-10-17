import math

def kek(x):
	return x[0]

dict = {
	"0": kek,
	"1": lambda x: x[1]
}

x = ["first", "second"]

print(dict["0"](x))
print(dict["1"](x))



def gaussian(x):
	return 1 / math.sqrt(2 * math.pi) * math.e**(-0.5 * x**2)


print(gaussian(10))
print(gaussian(0))