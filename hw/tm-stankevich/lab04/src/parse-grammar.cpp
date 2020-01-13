#include "parse-grammar.h"


using namespace std;


//FILE* f = fopen("input", "r");
string valG;

enum {
    EOFF, LP, RP, COMA, ARROW, NAME, TOKEN, RULE, ACTION
};

int grammarLex(FILE* f) {
    //cout << "tokenLex: " << " " << "\n";
    char c = fgetc(f);

    while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
        c = fgetc(f);
    }

    if (c == EOF) {
        return 0;
    }
    if (c == '-') {
        c = fgetc(f);
        if (c == '>') {
            return ARROW;
        }
        throw UNEXPECTED_MINUS;
    }
    if (c == '#') {
        valG = readName(f);
        if (valG == "rule") {
        	return RULE;
        }
        if (valG == "action") {
        	while ((c = fgetc(f)) != '#') {
        	}
        	druz(i, 6) fgetc(f);
        	return ACTION;
        }
    }
    ungetc(c, f);
    valG = readName(f);
    return NAME;
}

vector<Rule> parseGrammar(FILE* file) {
    vector<Rule> res;
    
    int next;
    //cout << "Enter the loop\n";

    while(next = grammarLex(file)) {
        //cout << "token processing\n";        
        if (next == RULE) {
            Rule* rule = new Rule();
            grammarLex(file);
            rule->left = NT(valG);
            if (grammarLex(file) != ARROW) {
            	throw ARROW_EXPECTED;
            }
            rule->right = vector<NT>();
            while ((next = grammarLex(file)) == NAME) {
            	rule->right.push_back(NT(valG));
            }
            if (next != ACTION) {
            	throw ACTION_EXPECTED;
            }
            res.push_back(*rule);
        } else {
        	throw RULE_EXPECTED;
        }
    }
    return res;
}

