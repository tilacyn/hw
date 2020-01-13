from enum import Enum

class MessageClass(Enum):
    SPAM = 1
    LEGIT = 2
    UNKNOWN = 3

classes = {MessageClass.LEGIT, MessageClass.SPAM}

class Message:
    def __init__(self, subject, body, clazz=MessageClass.UNKNOWN):
        self.subject = set(subject)
        self.body = body
        self.words_set = set(body)
        self.clazz = clazz

    def print(self):
        print("Subject:", self.subject)
        print("Body:", self.body)
        print("Class:", self.clazz)

    def words(self):
        return self.words_set
