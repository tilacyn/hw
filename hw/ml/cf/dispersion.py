

def avg(xs):
    return sum(xs) / len(xs)

def weighted(xs):
    return sum(xs) / len(xs)


K = int(input())
N = int(input())

x = []
y = []

for i in range(N):
    [x_str, y_str] = input().split(' ')
    y.append(int(x_str))
    x.append(int(y_str))

xs_from_y = {}

for i in range(N):
    c = y[i]
    if c not in xs_from_y:
        xs_from_y[c] = []
    xs_from_y[c].append(x[i])

ds = []

for c in xs_from_y:
    xs = xs_from_y[c]
    E = avg(xs)
    E = E ** 2

    Esq = avg([xi ** 2 for xi in xs])
    ds.append((Esq - E) * len(xs))

print(sum(ds) / N)
