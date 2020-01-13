#pragma once

#include "tokens.h"
#include <regex>


class Lexer {
public:
	vector<Token> tokens;

	Lexer(vector<Token> tokens) : tokens(tokens)
	{}

	vector<RealToken> lex(FILE* f) {
		string s = "";
		char c;
		vector<RealToken> realTokens;
		while ((c = fgetc(f)) != EOF) {
			s += c;
			for (Token t : tokens) {
				cout << "gonna match regex:\n" << s << " " << t.regex << "\n";
				regex rgx(t.regex);
				if (regex_match(s, rgx)) {
					realTokens.push_back(RealToken(t.name, s));
					s = "";
					break;
				}
			}

		}
		return realTokens;
	}
};