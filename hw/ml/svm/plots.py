import matplotlib.pyplot as plt
import numpy as np

net_size = 40


def draw(classify, data, miny=-2, maxy=2, minx=-2, maxx=2):

	for x in np.linspace(minx, maxx, net_size):
		for y in np.linspace(miny, maxy, net_size):
			clazz = classify([x, y])
			if clazz == -1:
				c = '#5ca532'
			elif clazz == 1:
				c = '#db587b'
			else:
				c = '#fffacd'
			plt.scatter(x, y, c=c, s=20)

	for x in data:
		if x[2] == 'N':
			c = 'Green'
		else:
			c = 'Red'
		plt.scatter(x[0], x[1], c=c, s=100)

	plt.legend()

	plt.show()


def draw_data(data):
	for x in data:
		if x[2] == 'N':
			c = 'Green'
		else:
			c = 'Red'
		plt.scatter(x[0], x[1], c=c, s=100)



	plt.legend()

	plt.show()
