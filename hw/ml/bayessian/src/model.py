from message import classes
from message import MessageClass
import math

def epanechnikov(x):
    if abs(x) > 1:
        return 0
    return 0.75 * (1 - x ** 2)


class Model:
    def __init__(self, train_set, a, lspam=1, llegit=1):
        self.pr = {}
        self.train_set = train_set
        self.sub_sets = {}
        self.vocabs_counts = {}
        self.subject_vocabs_counts = {}
        self.cache = {}
        for c in classes:
            self.sub_sets[c] = list(filter(lambda m: m.clazz == c, train_set))
            total_set = set()
            subject_total_set = set()
            for m in self.sub_sets[c]:
                total_set = total_set.union(m.words())
                total_set = total_set.union(m.subject)
            self.vocabs_counts[c] = len(total_set)
            self.subject_vocabs_counts[c] = len(subject_total_set)
            self.pr[c] = 0
        self.a = a
        self.l = {MessageClass.SPAM: lspam, MessageClass.LEGIT: llegit}
        for m in train_set:
            self.pr[m.clazz] += 1

    def density(self, word, c):
        if (word, c) in self.cache.keys():
            return self.cache[(word, c)]
        related_with_word_n = len(list(filter(lambda m: word in m.words(), self.sub_sets[c])))
        V = self.vocabs_counts[c]
        result = (related_with_word_n + self.a) / (len(self.sub_sets[c]) + V * self.a)
        self.cache[(word, c)] = result
        return result

    def subj_density(self, word, c):
        related_with_word_in_subject_n = len(list(filter(lambda m: word in m.subject, self.sub_sets[c])))
        V = self.subject_vocabs_counts[c]
        result = (related_with_word_in_subject_n + self.a) / (len(self.sub_sets[c]) + V * self.a)
        return result

    def predict(self, message, subj_weight, llegit=0):
        current_l = self.l
        if llegit != 0:
            current_l[MessageClass.LEGIT] = llegit
        pr_for_class = {}
        for c in classes:
            ds = [self.density(word, c) for word in message.words()]
            subj_ds = [self.subj_density(word, c) for word in message.subject]
            pr_for_class[c] = math.log(current_l[c]) + math.log(self.pr[c]) + sum(list(map(math.log, ds))) +\
                              subj_weight * sum(list(map(math.log, subj_ds)))
        maxv = -1e100
        maxk = MessageClass.UNKNOWN
        for x in pr_for_class:
            if pr_for_class[x] > maxv:
                maxv = pr_for_class[x]
                maxk = x
        return maxk



