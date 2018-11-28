/* Калькулятор обратной польской нотации. */

%{
#define YYSTYPE char*
#include <math.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>

const int MAX_SIZE = 10;
    
char* init(char** lol) {
	char* kek = malloc(MAX_SIZE);
	strcpy(kek, lol[0]);
	return kek;
}


%}

%token IDENT

%% /* Далее следуют правила грамматики и действия */

input:    /* пусто */
        | input line
;

line:   
        | exp  		    {   
               			FILE* out = fopen("output", "w");
               			fprintf(out, "%s\n", $1);
               			printf ("%s\n", $1);
				fclose(out); 
			    }
;

exp:      IDENT             
{ 
	//$$ = malloc(MAX_SIZE);
	//strcpy($$, $1);
	char* kek[] = {$1, "2"};
	$$ = init(kek);

	printf("exp -> IDENT; %s\n", $$);			      
}
	| exp exp           
{ 
	$$ = malloc(MAX_SIZE);
	strcpy($$, $1);
	strcat($$, " ");
	strcat($$, $2);
	printf("exp -> exp exp; s1 = %s, s2 = %s, res = %s\n", $1, $2, $$);
}        
;

%%

/* Лексический анализатор возвращает вещественное число
   с двойной точностью в стеке и лексему NUM, или прочитанную
   литеру ASCII, если это не число. Все пробелы и знаки
   табуляции пропускаются, в случае конца файла возвращается 0. */

FILE* f;


int yylex(void) {
    int c;
    /* пропустить промежутки  */
    while ((c = fgetc(f)) == ' ' || c == '\t');
    /* обработка чисел       */
    if (c <= 'z' && c >= 'a') {
        ungetc (c, f);
        char* s = malloc(MAX_SIZE);
        int read = fscanf(f, "%s", s);

	if (0 == strcmp(s, "int")) {
	    s = "char";
	}

	yylval = s;
	
        printf("return IDENT: %s\n", yylval);
        return IDENT;
    }

    if (c == EOF) {
	printf("EOF reached\n");
    }

    return 0;
}


int main(void) {
    //f = fopen("input", "r");
    f = stdin;

    int result = yyparse();
    fclose(f);
    return result;
}



void yyerror(const char *s)  /* вызывается yyparse в случае ошибки */ {
    printf ("%s\n", s);
}
