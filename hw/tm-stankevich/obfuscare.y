/* Obfuscare */

%{
#define YYSTYPE char*
#include <math.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>


#define ANSI_COLOR_RED     "\x1b[31m"
#define ANSI_COLOR_GREEN   "\x1b[32m"
#define ANSI_COLOR_YELLOW  "\x1b[33m"

#define ANSI_COLOR_RESET   "\x1b[0m"

#define MAX_SIZE 1000

char* construct(int n, char** args) {
	char* result = malloc(MAX_SIZE);
	for (int i = 0; i < n; i++) {
		strcat(result, args[i]);
	}
	return result;
}

char getEncChar(int i) {
	if (i == 0) return 'I';
	if (i == 1) return 'O';
	if (i == 2) return '1';
	if (i == 3) return '0';
}

char* encrypt(char* name) {
	int index = 0;
	char* res = malloc(MAX_SIZE);

	if (strlen(name) % 2 == 0) res[index++] = 'I';
	else res[index++] = 'O';
 
	for (int i = 0; i < strlen(name); i++) {
	    res[index++] = getEncChar(name[i] % 4);
	    res[index++] = getEncChar((name[i] * 3 - 'a') % 4);
	    res[index++] = getEncChar((name[i] * 37 - '0') % 4);
	}
	
	return res;
}


char* createName() {
    int r = rand();
    int len = rand() % 12 + 5;

    char* name = malloc(MAX_SIZE);

    if (r % 2 == 0) name[0] = 'I';
    else name[0] = 'O';

    for (int i = 1; i < len; i++) {
        name[i] = getEncChar(rand() % 4);
    }

    return name;
    
}


void sillyDeclare(char* s) {
    char* res = malloc(MAX_SIZE);
    char* type;
    if (rand() % 2) {
	type = "int ";
    } else {
	type = "char* ";
    }
    strcpy(res, type);
    strcat(res, createName());
    strcat(res, ";  (extra)\n\t");
    strcat(s, res);
}



char* intName;


void uselessOp(char* s) {
    strcat(s, intName);
    strcat(s, " += ");

    char n[12];
    sprintf(n, "%d", rand() % 10000);

    strcat(s, n);
    strcat(s, ";   (extra)\n\t");


    strcat(s, intName);
    strcat(s, " -= ");

    
    strcat(s, n);
    strcat(s, ";   (extra)\n\t");

}



FILE* out;


char** nonterminals;

int nt = 0;

    
%}







%token TYPE
%token NAME
%token RETURN

%token COMA
%token SEMICOLON
%token PTR

%token NUMBER
%token STRING
%token EQUALS

%token RPAREN
%token LPAREN
%token RSPAREN
%token LSPAREN
%token LFPAREN
%token RFPAREN

%right BLANKS



%% /* Далее следуют правила грамматики и действия */

input:    /* пусто */
        | mblanks fun mblanks
{
	char* args[] = {$1, $2, $3};
	$$ = construct(3, args);

	fprintf(out, "%s\n", $$);
	printf ("\n%s\n", $$);
};



fun:   	head body RFPAREN	
{   
	char* args[] = {$1, $2, $3};
	$$ = construct(3, args);
};


head:   type BLANKS fname LPAREN hargs RPAREN mblanks LFPAREN mblanks // head of the function
{
	char* args[] = {$1, $2, $3, $4, $5, $6, $7, $8, $9};
	$$ = construct(9, args);
	nonterminals[nt++] = "head";
};


body:  	
{
	$$ = "";
}
	| body instr SEMICOLON mblanks // body -> sequence of instr; <blanks>
{
	char* args[] = {$1, $2, $3, $4};
	$$ = construct(4, args);
	nonterminals[nt++] = "body";

        if (rand() % 2) {
	    sillyDeclare($$);
	}

	if (rand() %3) {
	    if (intName != NULL) {
		uselessOp($$);
	    }
	}

};


instr:	declaration
{
	//printf("declaration -> instr\n");
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "instr";
}
	| assignment
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "instr";
}
	| declar_assign
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "instr";
}
	| fcall
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "instr";
}
	| return
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "instr";
};


declaration: 	type BLANKS name
{
	char* args[] = {$1, $2, $3};
	$$ = construct(3, args);
        if ($$[0] == 'i' && $$[1] == 'n' && $$[2] == 't') {
	    intName = $3;
        }
	nonterminals[nt++] = "declaration";
};


assignment:	name EQUALS BLANKS rvalue
{
	char* args[] = {$1, $2, $3, $4};
	$$ = construct(4, args);
	nonterminals[nt++] = "assignment";
};


declar_assign:	type BLANKS name EQUALS mblanks rvalue // instr -> 
{
	char* args[] = {$1, $2, $3, $4, $5, $6};
	$$ = construct(6, args);
	nonterminals[nt++] = "declare_assign";
};


fcall: 	fname LPAREN args RPAREN mblanks
{
	char* args[] = {$1, $2, $3, $4, $5};
	$$ = construct(5, args);
	nonterminals[nt++] = "fcall";
};


type:	TYPE
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "type";
}
	| type PTR
{
	char* args[] = {$1, $2};
	$$ = construct(2, args);
	nonterminals[nt++] = "type";
};


name: 	NAME mblanks
{
	//printf("NAME -> name\n");
	char* args[] = {encrypt($1), $2};
	$$ = construct(2, args);
	nonterminals[nt++] = "name";
};


fname:	NAME mblanks
{
	//printf("NAME -> fname\n");
	char* args[] = {$1, $2};
	$$ = construct(2, args);
	nonterminals[nt++] = "fname";
};


rvalue:	name
{
	//printf("name -> rvalue\n");
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "rvalue";
}
	| fcall
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "rvalue";
}
	| NUMBER
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "rvalue";
}
	| STRING
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "rvalue";
};


args:	
{
	$$ = "";
}
	| mblanks rvalue
{
	char* args[] = {$1, $2};
	$$ = construct(2, args);
}
	| args COMA mblanks rvalue mblanks
{
	char* args[] = {$1, $2, $3, $4, $5};
	$$ = construct(5, args);
};


hargs:	nohargs
{
	$$ = "";
}
	| yeshargs
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "hargs";
};


nohargs:
{
	$$ = "";
};

yeshargs:	mblanks type BLANKS name mblanks
{
	//printf("type blanks name -> yeshargs\n");
	char* args[] = {$1, $2, $3, $4, $5};
	$$ = construct(5, args);
	nonterminals[nt++] = "yeshargs";
}
		| yeshargs COMA mblanks type BLANKS name mblanks
{
	//printf("hargs argument -> hargs\n");
	char* args[] = {$1, $2, $3, $4, $5, $6, $7};
	$$ = construct(7, args);
	nonterminals[nt++] = "yeshargs";
};




return:
	RETURN BLANKS rvalue
{
	char* args[] = {$1, $2, $3};
	$$ = construct(3, args);
};




mblanks: 
{
	$$ = "";
	nonterminals[nt++] = "mblanks";
}

	| BLANKS
{
	char* args[] = {$1};
	$$ = construct(1, args);
	nonterminals[nt++] = "mblanks";
}	





%%

/* Лексический анализатор возвращает вещественное число
   с двойной точностью в стеке и лексему NUM, или прочитанную
   литеру ASCII, если это не число. Все пробелы и знаки
   табуляции пропускаются, в случае конца файла возвращается 0. */

FILE* f;

char* types[] = {"int", "char", "double", "float", "bool", "long", "void"};

int ntypes = 7;


bool isWordSym(char c) {
    return c <= 'z' && c >= 'a' || c >= 'A' && c <= 'Z' || c == '_' || c >= '0' && c <= '9';
}




char* readWord() {
    char* res = malloc(MAX_SIZE);
    for (int i = 0; i < MAX_SIZE; i++) {
	int c = fgetc(f);	
	if (!isWordSym(c)) {
	    ungetc(c, f);
	    break;
	}
	res[i] = c;
    }
    return res;
}


int yylex(void) {
    int c;
    
    c = fgetc(f);

    //printf("lex enter: %c\n", c);

    yylval = malloc(MAX_SIZE);
    int bi = 0;
    while (c == '\n' || c == '\t' || c == ' ') {
	yylval[bi++] = c;
	c = fgetc(f);
    }

    if (bi != 0) {
	ungetc(c, f);
	return BLANKS;
    }

    if (c == '=' || c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']' || c == ';' || c == ',' || c == '*') {
	yylval = malloc(1);
	yylval[0] = c;
	//printf("lex: %c\n", c);
	switch (c) {
	    case '=': return EQUALS;
	    case '(': return LPAREN;
	    case ')': return RPAREN;
	    case '{': return LFPAREN;
	    case '}': return RFPAREN;
	    case '[': return LSPAREN;
	    case ']': return RSPAREN;
	    case ';': return SEMICOLON;
	    case ',': return COMA;
	    case '*': return PTR;
	}
    }

    if (isWordSym(c) && (c < '0' || c > '9')) {
        ungetc (c, f);
        char* s = readWord();
	yylval = s;

	//printf("lex readWord: %s\n", s);

	for (int i = 0; i < ntypes; i++) {
	    if (strcmp(s, types[i]) == 0) {
		//printf("lex: %s\n", s);
		return TYPE;
	    }
	}

	if (strcmp(s, "return") == 0) {
	    return RETURN;
	}
	
	//printf("lex: %s\n", s);
	return NAME;
    }

    if (c >= '0' && c <= '9') {
        ungetc(c, f);
        yylval = malloc(MAX_SIZE);
        for (int i = 0; i < MAX_SIZE; i++) {
	    int c = fgetc(f);	
	    if (c < '0' || c > '9') {
	        ungetc(c, f);
	        break;
	    }
	    yylval[i] = c;
        }
        return NUMBER;
    }

    if (c == '"') {
	yylval = malloc(MAX_SIZE);
	int si = 0;
        yylval[si++] = c;
	c = fgetc(f);
	
	while (c != '"') {
	    yylval[si++] = c;
	    c = fgetc(f);
	}	
        yylval[si] = c;
	
	return STRING;
    }

    return 0;
}

bool errorOccured = false;


int main(int argn, char** args) {
    f = fopen(args[1], "r");
    nonterminals = malloc(MAX_SIZE);

    srand(time(NULL));
    

    char* output = malloc(MAX_SIZE);
    strcpy(output, "o");
    strcat(output, args[1]);
    output[1] = 'u';
    output[2] = 't';
    
    out = fopen(output, "w");
    int result = yyparse();
    fclose(f);
    if (errorOccured) {
        printf(ANSI_COLOR_RED "\n======== FAIL ========\n\n" ANSI_COLOR_RESET);
    } else {
	printf(ANSI_COLOR_GREEN "\n======== OK ========\n\n" ANSI_COLOR_RESET);
    }

    fclose(out);
    return result;
}



void yyerror(const char *s)  /* вызывается yyparse в случае ошибки */ {
    printf ("%s\n", s);
    errorOccured = true;
    printf("NONTERMINALS\n");
    for (int i = 0; i < nt; i++) {
	printf("%s\n", nonterminals[i]);
    }

}
