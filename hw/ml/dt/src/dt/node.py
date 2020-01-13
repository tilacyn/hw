

class Node:
    def __init__(self, objects, split_f, split_v, children, leaf_class=None):
        self.split_f = split_f
        self.split_v = split_v
        self.objects = objects
        self.children = children
        self.leaf_class = leaf_class

    def set_leaf_class(self, leaf_class):
        self.leaf_class = leaf_class

    def is_leaf(self):
        return self.leaf_class is not None

    def print(self, level=1):
        for x in range(level):
            print("--", end='')

        if self.is_leaf():
            print(" Node: " + str(len(self.objects)) + " objects, leaf class:" + str(self.leaf_class))
            return

        print(" Node: " + str(len(self.objects)) + " objects, " + str(self.split_f) +
              " feature out of " + str(len(self.objects[0].features)) + " split v: " + str(self.split_v))

        for child in self.children:
            child.print(level + 1)
