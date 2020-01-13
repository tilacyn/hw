from message import Message
from message import MessageClass


class FileReader:
    def __init__(self, path):
        self.path = path

    def read(self):
        f = open(self.path, 'r')
        contents = list(filter(lambda x: x != '', f.read().split("\n")))
        subject = list(map(int, filter(lambda x: x != '', contents[0][9:].split(' '))))
        body = list(map(int, contents[1].split(' ')))
        if self.path.rfind("legit") != -1:
            clazz = MessageClass.LEGIT
        else:
            clazz = MessageClass.SPAM
        return Message(subject, body, clazz)
