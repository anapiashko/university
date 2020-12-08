#include <fcntl.h>
#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <string.h>

#define SEMAPHORE_NAME "/semaphore"

int main(int argc, char ** argv) {
    sem_t *sem;
    if ((sem = sem_open(SEMAPHORE_NAME, O_CREAT, 0777, 1)) == SEM_FAILED ){
        return 1;
    }
    // Переменная для сохранения текущего времени
    long int ttime;
    srand(time(NULL));
    while(1) {
        sem_wait(sem);
        FILE *f;
        f=fopen("text.txt","w++");
        if(!f) {
            printf("Can't open file\n");
            return 1;
        }
        pid_t pid = getpid();
        // Считываем текущее время
        ttime = time (NULL);
        fprintf(f,"%d - %s",pid, ctime (&ttime));
        fclose(f);
        printf("Information was sent - pid - %d\n", pid);
        sem_post(sem);
        usleep(5000);
    }
    return 0;
}