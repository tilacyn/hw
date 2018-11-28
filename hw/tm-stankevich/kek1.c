#include <string.h>
#include <stdio.h>

int main() {
    char* lol = malloc(10);
    lol[5] = "i";
    lol[4] = "i";
    lol[3] = "i";
    lol[2] = "i";
    lol[1] = "i";
    lol[0] = "i";
    printf("%d\n", strlen(lol));
}
