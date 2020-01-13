#!/bin/bash


сat decl.cpp >> output.cpp

echo >> output.cpp
echo >> output.cpp
echo "//end of declaration">> output.cpp
echo >> output.cpp
echo >> output.cpp


cat kek.g4 | ./src/

echo >> output.cpp
echo >> output.cpp
echo "//main function">> output.cpp
echo >> output.cpp
echo >> output.cpp


сat main.cpp.in >> output.cpp