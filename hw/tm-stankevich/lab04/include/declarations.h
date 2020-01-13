#ifndef __DECLARATIONS_H_
#define __DECLARATIONS_H_

#include <string>
#include <fstream>
#include <cstdlib>

//parse tokens
#define LEX_EXCEPTION "LEX_EXCEPTION"
#define WRONG_TOKEN_ARGUMENT "WRONG_TOKEN_ARGUMENT"
#define UNEXPECTED_MINUS "UNEXPECTED_MINUS"
#define TOKEN_NAME_EXPECTED "TOKEN_NAME_EXPECTED"
#define ARROW_EXPECTED "ARROW_EXPECTED"
#define TOKEN_VALUE_EXPECTED "TOKEN_VALUE_EXPECTED"

// parse grammar
#define RULE_EXPECTED "RULE_EXPECTED";
#define ACTION_EXPECTED "ACTION_EXPECTED"

using namespace std;

inline string readName(FILE* f) {
    char c = fgetc(f);
    string res = "";

    while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        res += c;
        c = fgetc(f);
    }
    ungetc(c, f);
    return res;
}


// main
#define UNEXPECTED_TOKEN_DOUBLE_SHARP "UNEXPECTED_TOKEN_DOUBLE_SHARP"

#define druz(i, n) for(int i = 0; i < n; i++)


#endif