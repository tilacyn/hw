
class Message:
    def __init__(self, words, c):
        self.c = c
        self.words = set(words)
        self.words_list = words


def get_classified_messages(c):
    return list(filter(lambda m: m.c == c, train_set))



def density(word, y):
    related = get_classified_messages(y)
    related_with_word_n = len(list(filter(lambda m: word in m.words, related)))
    V = vocabs_counts[y]
    result = (related_with_word_n + a) / (len(related) + V * a)
    #print("density", word, y + 1, V, a, density)
    return result


def pr_y_x(m):
    result = []
    for y in classes_set:
        densities = [density(word, y) for word in m.words]
        d = 1
        for den in densities:
            d *= den
        result.append(d * Pr[y])
    return result



k = int(input())

lm = list(map(int, input().split()))
a = int(input())


n = int(input())

train_set = []
Pr = {}
vocabs_counts = {}

for i in range(n):
    msg = input().split()
    train_set.append(Message(msg[2:], int(msg[0]) - 1))

classes_list = list(map(lambda x: x.c, train_set))
classes_set = set(classes_list)
for c in classes_set:
    Pr[c] = classes_list.count(c) / len(classes_list)
    class_messages = get_classified_messages(c)
    total_set = set()
    #total_length = 0
    for m in class_messages:
        total_set = total_set.union(m.words)
        #total_length += len(m.words_list)
    vocabs_counts[c] = len(total_set)


test_set = []

m = int(input())

for i in range(m):
    msg = input().split()
    test_set.append(Message(msg[1:], -1))


def test():
    result = [pr_y_x(test_message) for test_message in test_set]
    return [list(map(lambda x: x / sum(l), l)) for l in result]


res = test()

for x in res:
    for xx in x:
        print(xx, end=' ')
    print()