//#pragma once
#ifndef __GRAMMAR_H_
#define __GRAMMAR_H_


#include <string>
#include <cstdlib>

#include "declarations.h"

using namespace std;

class NT {
public:
	string name;
	NT(string name): name(name) {}
	NT() {
		name = "";
	}
};

class Rule {
public:
	vector<NT> right;
	NT left;

	Rule() {
		left = NT();
		right = vector<NT>();
	}

};


#endif