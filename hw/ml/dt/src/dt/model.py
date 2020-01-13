from dt.node import Node
import math

def entropy(a, b):
    if a == 0 or b == 0:
        return 0
    return - a * math.log(a) - b * math.log(b)

def entropy_of_sample(P, N):
    return entropy(P / (P + N), N / (P + N))


class Object:
    def __init__(self, features, clazz):
        self.features = features
        self.clazz = clazz


def count_igain(objects, dominant_tuple):
    dominant, p, n = dominant_tuple
    classes = [x.clazz for x in objects]
    P = classes.count(dominant)
    N = len(classes) - P
    return entropy_of_sample(P, N) - \
           entropy_of_sample(p, n) * (p + n) / (P + N) - \
           entropy_of_sample(P - p, N - n) * (P + N - p - n) / (P + N)


def count_dominant(objects):
    classes = [x.clazz for x in objects]
    dominant = max(set(classes), key=classes.count)
    return dominant


def find_dominant_and_count_pn(objects):
    classes = [x.clazz for x in objects]
    dominant = count_dominant(objects)
    true_d = classes.count(dominant)
    false_d = len(classes) - true_d
    return dominant, true_d, false_d


def igain(left, right):
    left_tuple = find_dominant_and_count_pn(left)
    right_tuple = find_dominant_and_count_pn(right)
    objects = left + right

    return max([count_igain(objects, x) for x in [left_tuple, right_tuple]])


parts = 5
len_to_iter_each = 6


class Model:
    def __init__(self, dataset, max_height):
        self.dataset = dataset
        self.class_set = set(map(lambda x: x.clazz, dataset))
        self.root = None
        self.max_height = max_height

    def build_tree(self):
        self.root = self.build(self.dataset)

    def build(self, objects, height=0):
        if len(objects) == 0:
            return Node([], None, None, None, self.dataset[0].clazz)
        if height == self.max_height:
            return Node(objects, None, None, None, count_dominant(objects))
        opt = self.find_opt(objects)
        if opt is None:
            return Node(objects, None, None, None, objects[0].clazz)

        split_f, split_v, left, right = opt
        children = [self.build(group, height + 1) for group in [left, right]]
        return Node(objects, split_f, split_v, children)
    
    def predict(self, object, node=None):
        if node is None:
            node = self.root
        if node.is_leaf():
            return node.leaf_class
        if object.features[node.split_f] >= node.split_v:
            return self.predict(object, node.children[1])
        else:
            return self.predict(object, node.children[0])
        
    def find_opt(self, objects):
        classes = [object.clazz for object in objects]
        if len(set(classes)) <= 1:
            return None
        opt_f = 0
        opt_v = 0
        opt_igain = 0
        left = []
        right = []

        for i in range(len(objects[0].features)):
            objects.sort(key=lambda object: object.features[i])

            length = len(objects)
            if length >= len_to_iter_each:
                part_size = length // parts
                splitptr_range = [part_size * times for times in range(1, parts - 1)]
            else:
                splitptr_range = range(1, len(objects))

            for splitptr in splitptr_range:
                current_left = objects[:splitptr]
                current_right = objects[splitptr:]
                current_igain = igain(current_left, current_right)
                if current_igain > opt_igain:
                    opt_igain = current_igain
                    opt_f = i
                    left = current_left
                    right = current_right
                    opt_v = (objects[splitptr].features[opt_f] + objects[splitptr - 1].features[opt_f]) / 2

        return opt_f, opt_v, left, right


def process_ds():
    max_height = max_heights[optimal_map[n]]
    objects, test_set = datasets[n]
    original_model = Model(objects, max_height)
    original_model.build_tree()
    print(n + 1)
    print("original: " + str(prec(original_model, test_set)))
    rfmodels = []
    for i in range(times):
        copy = []

        for x in objects:
            copy.append(Object(x.features.copy(), x.clazz))

        np.random.shuffle(copy)
        copy = copy[:2 * len(copy) // 3]
        features_n = len(copy[0].features)
        for i in range(int(c * features_n)):
            random_f_n = np.random.randint(features_n)
            for x in copy:
                x.features[random_f_n] = 0
        model = Model(copy, max_height)
        model.build_tree()
        rfmodels.append(model)
    print("random forrest: " + str(acc(lambda x: rfpredict(x, rfmodels), test_set)))
    print()
