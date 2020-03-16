import math


def count_in_class(c):
    before = [0]
    after = [0]
    xs = xs_from_y[c]
    xs.sort()

    for i in range(1, len(xs)):
        before.append(before[-1] + i * (xs[i] - xs[i - 1]))

    for i in range(2, len(xs) + 1):
        after.append(after[-1] + (i - 1) * (xs[-i + 1] - xs[-i]))

    after.reverse()
    sum_dist_before[c] = before
    sum_dist_after[c] = after


def count_in_list(xs):
    before = [0]
    xs.sort()

    for i in range(1, len(xs)):
        before.append(before[-1] + i * (xs[i] - xs[i - 1]))
    return before


K = int(input())
N = int(input())

x = []
y = []

for i in range(N):
    [x_str, y_str] = input().split(' ')
    x.append(int(x_str))
    y.append(int(y_str))

xs_from_y = {}
sum_dist_before = {}
sum_dist_after = {}

for i in range(N):
    c = y[i]
    if c not in xs_from_y:
        xs_from_y[c] = []
    xs_from_y[c].append(x[i])

for c in xs_from_y:
    count_in_class(c)

in_class = 2 * sum(map(lambda c: sum(sum_dist_before[c]), xs_from_y.keys()))

print(in_class)

whole_dist = 2 * sum(count_in_list(x))

print(whole_dist - in_class)