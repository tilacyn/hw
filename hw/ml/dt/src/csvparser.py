import pandas as pd

from os import listdir
from dt.model import Object


def read_file(path):
    return pd.read_csv(path)