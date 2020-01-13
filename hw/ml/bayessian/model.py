from message import classes
from message import MessageClass
import math

def epanechnikov(x):
    if abs(x) > 1:
        return 0
    return 0.75 * (1 - x ** 2)


def fscore_inner(precision, recall):
    return 2 * (precision * recall) / (precision + recall)

def fscore(mtx):
    mtx = np.array(mtx)
    w = mtx.sum(0)
    w = list(map(lambda x: x / mtx.sum(), w))
    f1 = fscore_inner(mtx[0][0] / mtx.sum(0)[0], mtx[0][0] / mtx.sum(1)[0])
    f2 = fscore_inner(mtx[1][1] / mtx.sum(0)[1], mtx[1][1] / mtx.sum(1)[1])
    return f1 * w[0] + f2 * w[1], mtx



class Model:
    def __init__(self, kernel, h, train_set, a):
        self.pr = {}
        self.kernel = kernel
        self.h = h
        self.train_set = train_set
        self.sub_sets = {}
        for c in classes:
            self.sub_sets[c] = list(filter(lambda m: m.clazz == c, train_set))
        self.a = a
        for c in classes:
            self.pr[c] = 0
        for m in train_set:
            self.pr[m.clazz] += 1

    def smoothed_frequency(self, message, word):
        if word not in message.words():
            return 3e-3
        divisor = len(message.body) + self.a * len(message.words())
        return (message.frequency(word) + self.a) / divisor

    def density(self, word, frequency, c):
        res = 0
        for m in self.sub_sets[c]:
            res += epanechnikov((self.smoothed_frequency(m, word) - frequency) / self.h)
        return res / (len(self.sub_sets[c]))

    def predict(self, message):
        pr_for_class = {}
        for c in classes:
            ds = [self.density(word, self.smoothed_frequency(message, word), c) for word in message.words()]
            result_d = 1
            for d in ds:
                result_d *= d
            pr_for_class[c] = self.pr[c] * result_d
        maxv = 0
        maxk = MessageClass.UNKNOWN
        for x in pr_for_class:
            if pr_for_class[x] > maxv:
                maxv = pr_for_class[x]
                maxk = x
        return maxk






# k = int(input())
#
# lm = list(map(int, input().split()))
# a = int(input())
#
#
# n = int(input())
#
# train_set = []
# Pr = {}
#
# for i in range(n):
#     msg = input().split()
#     train_set.append(Message(msg[2:], int(msg[0]) - 1, a))
#
# test_set = []
#
# m = int(input())
#
# for i in range(m):
#     msg = input().split()
#     test_set.append(Message(msg[1:], -1, a))
#
#
# classes_list = list(map(lambda x: x.c, train_set))
# classes_set = set(classes_list)
# for c in classes_set:
#     Pr[c] = classes_list.count(c) / len(classes_list)
#
#
# def test(h):
#     result = [pr_y_x(test_message, h) for test_message in test_set]
#     return [list(map(lambda x: x / sum(l), l)) for l in result]
#
#
# res = test(1)
#
# for x in res:
#     for xx in x:
#         print(xx, end=' ')
#     print()