import pandas as pd
import numpy as np
import plots
import smoduginets
import kernels
import math


class_dict = {
    'N': -1,
    'P': 1
}

def fscore_inner(precision, recall):
    return 2 * (precision * recall) / (precision + recall)

def fscore(classify, test_set):
    mtx = np.array([[0, 0], [0, 0]])
    for x in test_set:
        classified = classify(x)
        expected = class_dict[x[2]]
        if expected == -1 and classified == -1:
            mtx[0][0] += 1
        if expected == 1 and classified == -1:
            mtx[1][0] += 1
        if expected == -1 and classified == 1:
            mtx[0][1] += 1
        if expected == 1 and classified == 1:
            mtx[1][1] += 1
    w = mtx.sum(0)
    w = list(map(lambda x: x / mtx.sum(), w))
    f1 = fscore_inner(mtx[0][0] / mtx.sum(0)[0], mtx[0][0] / mtx.sum(1)[0])
    f2 = fscore_inner(mtx[1][1] / mtx.sum(0)[1], mtx[1][1] / mtx.sum(1)[1])
    return f1 * w[0] + f2 * w[1], mtx



def draw(classify, data):
    return plots.draw(classify, data, minmax[0], minmax[1], minmax[2], minmax[3])

def draw_on_data(model, data):
    draw(model.predict, data)

def separate(data, k):
    shuffled = data.copy()
    np.random.shuffle(shuffled)
    return shuffled[:k], shuffled[k:]


def run_svm(kernel_f, train_set, c):
    n = len(train_set)
    kernel = np.zeros([n, n])
    y = []

    for i in range(n):
        for j in range(n):
            kernel[i][j] = kernel_f(train_set[i], train_set[j])
        y.append(class_dict[train_set[i][2]])

    smo_model = smoduginets.SMO(train_set, kernel, kernel_f, y, c)
    smo_model.solve()

    #draw(lambda x: smo_model.predict(train_set, x), train_set)
    return smo_model


times = range(5)

def run_cv(kernel, train_set_n, c):
    sum_f = 0
    for t in times:
        train_set, test_set = separate(data, train_set_n)
        model = run_svm(kernel, train_set, c)
        f, mtx = fscore(model.predict, test_set)
        print("c:", c, "t:", t, "fscore:", f)
        sum_f += f
    return [model, sum_f / len(times), c]


def sort_and_filter(res):
    res = list(filter(lambda x: not math.isnan(x[1]), res))
    res.sort(key=lambda x: -x[1])
    return res

minmax = []

def run(train_set_n, file):
    minmax.clear()
    dataframe = pd.read_csv(file)

    dataframe.info()

    data = dataframe.values

    minmax.append(min(data, key=lambda x: x[1])[1])
    minmax.append(max(data, key=lambda x: x[1])[1])
    minmax.append(min(data, key=lambda x: x[0])[0])
    minmax.append(max(data, key=lambda x: x[0])[0])
    cs = [1, 5, 10, 100]
    gammas = np.linspace(0.2, 1.6, 8)

    exp_results = []
    pol_results = []
    s2d_results = []

    for c in cs:
        for gamma in gammas:
            result = run_cv(kernels.exponentialg(gamma), train_set_n, c)
            result.append(gamma)
            exp_results.append(result)

        s2d_result = run_cv(kernels.scalar_2d, train_set_n, c)
        s2d_results.append(s2d_result)

        pol_result = run_cv(kernels.polynomial2, train_set_n, c)
        pol_results.append(pol_result)

    exp_res = sort_and_filter(exp_results)
    s2d_res = sort_and_filter(s2d_results)
    pol_res = sort_and_filter(pol_results)

    return exp_res, s2d_res, pol_res



