import numpy as np

def fscore_inner(precision, recall):
    return 2 * (precision * recall) / (precision + recall)

def fscore(mtx):
    mtx = np.array(mtx)
    w = mtx.sum(0)
    w = list(map(lambda x: x / mtx.sum(), w))
    f1 = fscore_inner(mtx[0][0] / mtx.sum(0)[0], mtx[0][0] / mtx.sum(1)[0])
    f2 = fscore_inner(mtx[1][1] / mtx.sum(0)[1], mtx[1][1] / mtx.sum(1)[1])
    return f1 * w[0] + f2 * w[1], mtx