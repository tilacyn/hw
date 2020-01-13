from dt.model import Model as DTModel
import math
import numpy as np

def predict(object, weighted_models):
    sumw = sum([x[1] for x in weighted_models])
    weighted_models = [[x[0], x[1] / sumw] for x in weighted_models]
    res = sum([x[0].predict(object) * x[1] for x in weighted_models])
    if res > 0:
        return 1
    else:
        return -1

class BoostModel:
    def __init__(self, objects, T, max_height):
        self.objects = objects
        self.max_height = max_height
        self.T = T
        self.l = len(objects)
        self.ws = [1 / self.l for _ in range(self.l)]
        self.models = []

    def run(self):
        self.weighted_models = []
        suma = 0
        for t in range(self.T):
            dt_ds = []
            for i in range(self.l):
                dt_ds.append(np.random.choice(self.objects, p=self.ws))

            current_model = DTModel(dt_ds, self.max_height)
            current_model.build_tree()

            N = 0
            for i in range(self.l):
                if current_model.predict(self.objects[i]) != self.objects[i].clazz:
                    N += self.ws[i]
            a = 0.5 * math.log(((1 - N) + (1 / self.T)) / (N + (1 / self.T)))
            suma += a

            for i in range(self.l):
                self.ws[i] = self.ws[i] * math.e ** (-a * self.objects[i].clazz * current_model.predict(self.objects[i]))

            self.ws = [w / sum(self.ws) for w in self.ws]
            self.weighted_models.append([current_model, a])
            #print(max(self.ws))
            #print(min(self.ws))
            #print()

        self.weighted_models = [[x[0], x[1] / suma] for x in self.weighted_models]

        return self.weighted_models

    def predict(self, object):
        res = sum([x[0].predict(object) * x[1] for x in self.weighted_models])
        if res > 0:
            return 1
        else:
            return -1