#!/bin/bash

make extract-actions

./extract-actions kek.g

make all

./main kek.g

#make clean