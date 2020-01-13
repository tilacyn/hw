#include "actions.h"
#include "parse-tokens.h"
#include "parse-grammar.h"
#include "lexer.h"


void pritnException(const char* s) {
	cout << s << "\n";
}

void separateTokensAndGrammar(FILE* in, FILE* tokenFile, FILE* grammarFile) {
	char c = 'a';
	while(c) {
		c = fgetc(in);
		if (c == '#') {
			if ((c = fgetc(in)) == '#') {
				int ii = 5;
				while(ii--) fgetc(in);
				break;	
			}
			ungetc(c, in);
			c = '#';
		}
		//putc(c, tokenFile);
		fprintf(tokenFile, "%c", c);
		//printf("%c", 'a');
		//cout << c;
	}

	while((c = fgetc(in)) != EOF) {
		fprintf(grammarFile, "%c", c);
		//cout << (int) c;
	}
}

int main(int argc, char** args) {
	FILE* tokensGrammarFile = fopen(args[1], "r");


	FILE* tokenFile = fopen("tmp/tokens", "w");
	FILE* grammarFile = fopen("tmp/grammar", "w");

	separateTokensAndGrammar(tokensGrammarFile, tokenFile, grammarFile);

	fclose(tokenFile);
	fclose(grammarFile);
	fclose(tokensGrammarFile);

	// tokens and grammar separated
	// we can start parsing them

	tokenFile = fopen("tmp/tokens", "r");
	grammarFile = fopen("tmp/grammar", "r");

	

	vector<Token> tokens;
	try {
		tokens = parseTokens(tokenFile);
	} catch (const char* s) {
		pritnException(s);
		throw s;
	}

	for (Token& t : tokens) {
		cout << t.name << "\n";
	}

	cout << "Tokens successfully parsed\n";


	vector<Rule> rules;
	try {
		rules = parseGrammar(grammarFile);
	} catch (const char* s) {
		pritnException(s);
		throw s;
	}

	for (Rule r : rules) {
		cout << r.left.name << "\n";
	}


	cout << "Grammar successfully parsed\n";


	Lexer l(tokens);

	FILE* input = fopen("input", "r");

	vector<RealToken> realTokens = l.lex(input);



	for (RealToken rt : realTokens) {
		cout << rt.name << "\n";
	}

	cout << "Real tokens successfully parsed\n";

	
	//system("cat Makefile");

	return 0;
}