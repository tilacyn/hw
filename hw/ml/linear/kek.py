# solve directly
from numpy import array
from numpy.linalg import inv
from matplotlib import pyplot
data = array([
	[1, 2, 3],
	[4, 5, 6],
	[7, 8, 9]
	])
X, y = data[:,:-1], data[:,-1]
print(X)
#X = X.reshape((len(X), 1))

#print(X)
print(y)

# linear least squares
#b = inv(X.T.dot(X)).dot(X.T).dot(y)
#print(b)
# predict using coefficients
#yhat = X.dot(b)
# plot data and predictions
#pyplot.scatter(X, y)
#pyplot.plot(X, yhat, color='red')
#pyplot.show()

