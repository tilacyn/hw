#include <cstdlib>
#include <fstream>
#include <iostream>


using namespace std;

#define UNEXPECTED_TOKEN "UNEXPECTED_TOKEN";
#define UNEXPECTED_EOF "UNEXPECTED_EOF";


string atrtype_ = "ATR_TYPE";

int actionN = 0;


void printAction(FILE* in, FILE* actionFile, int n) {
	putc('\n', actionFile);
	putc('\n', actionFile);

	fputs(atrtype_.c_str(), actionFile);
	putc(' ', actionFile);
	fputs("action", actionFile);
	fprintf(actionFile, "%d", actionN);
	putc('(', actionFile);

	for (int i = 0; i < n; i++) {
		if (i) {
			fputs(", ", actionFile);
			fputs(atrtype_.c_str(), actionFile);
			fputs("& arg", actionFile);
			fprintf(actionFile, "%d", i + 1);
		} else {
			fputs(atrtype_.c_str(), actionFile);
			fputs("& arg", actionFile);
			fprintf(actionFile, "%d", i + 1);
		}
	}
	fputs(") {\n", actionFile);

	char c = 'a';

	fprintf(actionFile, "\t%s zubrResult;", atrtype_.c_str());


	while(c != EOF) {
		c = fgetc(in);
		if (c == '#') {
			if (fgetc(in) == 'a') {
				if (fgetc(in) == 'c') {
					int ii = 5;
					while(ii--) {
						fgetc(in);
					}
					break;
				}
			}
			throw UNEXPECTED_TOKEN;
		}
		if (c == '$') {
			char nc = fgetc(in);
			if (nc == '$') {
				fputs("zubrResult", actionFile);
				continue;
			} else {
				ungetc(nc, in);
			}


			int argn;
			fscanf(in, "%d", &argn);
			fputs("arg", actionFile);
			fprintf(actionFile, "%d", argn);
			c = fgetc(in);
		}
		putc(c, actionFile);
	}
	fputs("return zubrResult;\n", actionFile);
	putc('}', actionFile);
	actionN++;
}

void extract(FILE* in, FILE* actionFile) {
	char c = 'a';
	while(c != EOF) {
		c = fgetc(in);
		if (c == '#') {
			if (fgetc(in) == 'a') {
				if (fgetc(in) == 'c') {
					int ii = 4;
					while(ii--) {
						fgetc(in);
					}
					fgetc(in);
					int n;
					fscanf(in, "%d", &n);
					//cout << "WOW" << n << "\n";
					fgetc(in);
					printAction(in, actionFile, n);
				}
			}
		}
	}
}

/*string atrtype(FILE* in) {
	char c = 'a';
	while(c != EOF) {
		c = fgetc(in);
		if (c == '#') {
			if (fgetc(in) == 'a') {
				if (fgetc(in) == 't') {
					int ii = 5;
					while(ii--) {
						fgetc(in);
					}
					fgetc(in);
					string res = "";
					while (c != EOF) {
						c = fgetc(in);
						//cout << c;
						if (c == '}') {
							//cout << res;
							return res;
						}
						res += c;
					}
					throw UNEXPECTED_EOF;
				}
			}
		}
	}
	return "loh\n";
}*/



int main(int argc, char** args) {
	FILE* in = fopen(args[1], "r");
	//FILE* grammarFile = fopen("tmp/grammar", "w");

	system("cat decl.cpp > include/actions.h");
	FILE* actionFile = fopen("include/actions.h", "a");
	
	//atrtype_ = atrtype(in);

	//cout << atrtype_;

	extract(in, actionFile);


	fclose(actionFile);

	return 0;
}