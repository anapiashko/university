#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <ctype.h>
#include <stdbool.h>

bool is_vowel(char letter) {
    char vowels[5] = {'a', 'i', 'o', 'e', 'u'};
    for (int i = 0; i < 5; i++) {
        if (letter == vowels[i]) {
            return true;
        }
    }
    return false;
}

int main() {

    int f_even;
    int f_odd;
    size_t sizeRead;
    char read_buf[150];

    /*Считываем данные из стандартного потока ввода*/
    sizeRead = read(0, read_buf, 150);
    if (sizeRead <= 0) {
        printf("Can\'t read from standard flow.\n");
        return(-1);
    }

    /*Pазделим на строки в массив*/
    int num_strings = 0;
    char strings[100][150];
    char *istr = strtok(read_buf, "\n");
    while (istr != NULL) {
        strcpy(strings[num_strings], istr);
        istr = strtok(NULL, "\n");
        num_strings++;
    }

//    for (int i = 0; i < num_strings; i++) {
//        printf("%d - %s\n", i, strings[i]);
//    }

    if (((f_even = open("file_even.txt", O_RDWR | O_CREAT | O_TRUNC, 0666)) < 0) ||
        (f_odd = open("file_odd.txt", O_RDWR | O_CREAT | O_TRUNC, 0666)) < 0){
        printf("Can\'t open file\n");
        return(-1);
    }

    for (int i = 0; i < num_strings; i++) {
        if (is_vowel(strings[i][0])) {
            printf("%s\n", strings[i]);
            fprintf(stderr, "%d\n", i);
        }
        else {
            if (!isdigit(strings[i][0]))
                if (i % 2 == 0) {
                    write(f_even, strings[i],150);
                    write(f_even, "\n", 2);
                }
                else {
                    write(f_odd, strings[i],150);
                    write(f_odd, "\n", 2);
                }
        }
    }
    return 0;
}