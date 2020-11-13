#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>

int main(void){

    char* argv[] = {"", NULL};

    printf("+ Процесс 1: PID: %d\tPPID: %d\n", getpid(), getppid());
    if(fork() == 0){
        printf("+ Процесс 2: PID: %d\tPPID: %d\n", getpid(), getppid());
        if(fork() == 0){
            printf("+ Процесс 7: PID: %d\tPPID: %d\n", getpid(), getppid());
            execvp("ps",  argv);
            printf("- Процесс 7: PID: %d\tPPID: %d\n", getpid(), getppid());
            exit(0);
        }
        sleep(1);
        printf("- Процесс 2: PID: %d\tPPID: %d\n", getpid(), getppid());
        exit(0);
    }
    else if(fork() == 0){
        sleep(2);
        printf("+ Процесс 3: PID: %d\tPPID: %d\n", getpid(), getppid());
        if(fork() == 0){
            printf("+ Процесс 5: PID: %d\tPPID: %d\n", getpid(), getppid());

            printf("- Процесс 5: PID: %d\tPPID: %d\n", getpid(), getppid());
            exit(0);
        }
        else if(fork() == 0){
            printf("+ Процесс 6: PID: %d\tPPID: %d\n", getpid(), getppid());

            printf("- Процесс 6: PID: %d\tPPID: %d\n", getpid(), getppid());
            exit(0);
        }
        sleep(1);
        printf("- Процесс 3: PID: %d\tPPID: %d\n", getpid(), getppid());
        exit(0);
    }
    else if(fork() == 0){
        sleep(4);
        printf("+ Процесс 4: PID: %d\tPPID: %d\n", getpid(), getppid());

        printf("- Процесс 4: PID: %d\tPPID: %d\n", getpid(), getppid());
        exit(0);
    }
    sleep(5);
    printf("- Процесс 1: PID: %d\tPPID: %d\n", getpid(), getppid());
    exit(0);
}