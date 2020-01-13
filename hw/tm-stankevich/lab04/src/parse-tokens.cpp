#include "parse-tokens.h"

using namespace std;


string valT;

enum {
    EOFF, LP, RP, COMA, ARROW, VAL, NAME, TOKEN
};

int tokenLex(FILE* f) {
    //cout << "tokenLex: " << " " << "\n";
    char c = fgetc(f);

    while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
        c = fgetc(f);
    }

    if (c == EOF) {
        return 0;
    }
    
    if (c == ',') {
        return COMA;
    }
    if (c == '{') {
        return LP;
    }
    if (c == '}') {
        return RP;
    }
    if (c == '-') {
        c = fgetc(f);
        if (c == '>') {
            return ARROW;
        }
        throw UNEXPECTED_MINUS;
    }
    if (c == '\'') {
        valT = "";
        while ((c = fgetc(f)) != '\'') {
            valT += c;
        }
        return VAL;
    }
    if (c == '\"') {
        valT = "";
        while ((c = fgetc(f)) != '\"') {
            valT += c;
        }
        return VAL;
    }
    if (c == '#') {
        readName(f);
        return TOKEN;
    }
    ungetc(c, f);
    valT = readName(f);
    return NAME;
}

vector<Token> parseTokens(FILE* file) {
    cout << "HEY\n";
    vector<Token> res;
    
    int next;
    //cout << "Enter the loop\n";

    while(next = tokenLex(file)) {
        //cout << "token processing\n";
        if (next == TOKEN) {
            Token* t = new Token();
            tokenLex(file);
            int arg;
            while ((arg = tokenLex(file)) != RP) {
                if (valT == "string") {
                    t->isString = 1;
                } else if (valT == "regex") {
                    t->isRegex = 1;
                } else if (valT == "skip") {
                    t->isSkip = 1;
                } else {
                    throw WRONG_TOKEN_ARGUMENT;
                }
            }
            if (tokenLex(file) != NAME) {
                throw TOKEN_NAME_EXPECTED;
            }
            t->name = valT;
            if (tokenLex(file) != ARROW) {
                throw ARROW_EXPECTED;
            }
            if (tokenLex(file) != VAL) {
                throw TOKEN_VALUE_EXPECTED;
            }
            t->regex = valT;
            res.push_back(*t);
        }
    }
    return res;
}


