import matplotlib.pyplot as plt

	
def add_plot(filenumber, nrmsds, iterations):
	plt.plot(iterations, nrmsds, label=str(filenumber))
	plt.xlabel('iterations')
	plt.ylabel('nrmsd')




def show_plot():
	plt.title("sgd")
	plt.legend()
	plt.show()