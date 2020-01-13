//#pragma once
#ifndef __TOKENS_H_
#define __TOKENS_H_


#include <string>
#include <cstdlib>
#include "grammar.h"

using namespace std;

class Token : public NT {
public:
	bool isString;
	bool isRegex;
	bool isSkip;
	string regex;

	Token(bool isString=0, bool isRegex=0, bool isSkip=0, string regex="", string name="") :
	   	isString(isString),
	 	isRegex(isRegex),
	  	isSkip(isSkip),
	    regex(regex)
	{
		this->name = name;
	}

	static Token* unite(Token* f, Token* s) {
		Token* res = new Token();
		if (f->isString || s->isString) {
			res->isString = 1;
		}
		if (f->isSkip || s->isSkip) {
			res->isSkip = 1;
		}
		if (f->isRegex || s->isRegex) {
			res->isRegex = 1;
		}
		if (f->regex != "") {
			res->regex = f->regex;
		}
		if (s->regex != "") {
			res->regex = s->regex;
		}
		if (f->name != "") {
			res->name = f->name;
		}
		if (s->name != "") {
			res->name = s->name;
		}
		return res;
	}

	void kek() {
		name = "kek";
	}


};


class RealToken {
public:	
	string name;
	string val;
	RealToken(string name, string val) : name(name), val(val) {}
};


#endif