#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>

#define SEMAPHORE_NAME "/semaphore"

int main(int argc, char **argv) {
    sem_t *sem;
    if ((sem = sem_open(SEMAPHORE_NAME, 0)) == SEM_FAILED) {
        return 1;
    }
    srand(time(NULL));
    char *pid_time = (char*)malloc(70 * sizeof(char));
    while (1) {
        sem_wait(sem);
        FILE *f;
        f = fopen("/home/anastasiya/university/OS/lab7/lab7_1/text.txt", "r");
        if (!f) {
            printf("Can't open file\n");
            return 1;
        }
        fgets( pid_time, 70, f);
        printf("Information was received\n");

        printf("%s - %d", pid_time, getpid());
        fclose(f);
        printf("\n");
        sem_post(sem);
        usleep(5000);
    }
    return 0;
}