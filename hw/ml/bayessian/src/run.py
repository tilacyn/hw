from model import Model
from message import Message
from message import MessageClass
from model import epanechnikov
from filereader import FileReader
from os import listdir

train_set = []
messages_root = '../messages/'
parts = listdir(messages_root)

for filename in listdir(messages_root + 'part1/'):
    reader = FileReader(messages_root + 'part1/' + filename)
    train_set.append(reader.read())

# m = Model(epanechnikov, 0.5, [m1, m2, m3, m4, m5], 1)