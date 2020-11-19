#include<stdio.h>
#include<sys/ipc.h>
#include<sys/shm.h>
#include<sys/types.h>
#include<string.h>
#include<errno.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>

#define BUF_SIZE 1024
#define SHM_KEY 0x1234

struct shmseg {
    int cnt;
    int complete;
    char buf[BUF_SIZE];
};

int fill_buffer(char *bufptr, int size, char* word);

int main(int argc, char *argv[]) {

    char* words[2] = {"first", "second"};

    int shmid, numtimes;
    struct shmseg *shmp;
    char *bufptr;
    int spaceavailable;
    shmid = shmget(SHM_KEY, sizeof(struct shmseg), 0644 | IPC_CREAT);
    if (shmid == -1) {
        perror("Shared memory");
        return 1;
    }

    // Attach to the segment to get a pointer to it.
    shmp = shmat(shmid, NULL, 0);
    if (shmp == (void *) -1) {
        perror("Shared memory attach");
        return 1;
    }

    /* Transfer blocks of data from buffer to shared memory */
    bufptr = shmp->buf;
    spaceavailable = BUF_SIZE;
    for (numtimes = 0; numtimes < 2; numtimes++) {
        shmp->cnt = fill_buffer(bufptr, spaceavailable, words[numtimes]);
        shmp->complete = 0;
        printf("Writing Process: Shared Memory Write: Wrote %d bytes\n", shmp->cnt);
        bufptr = shmp->buf;
        spaceavailable = BUF_SIZE;
        sleep(5);
    }
    printf("Writing Process: Wrote %d times\n", numtimes);
    shmp->complete = 1;

    if (shmdt(shmp) == -1) {
        perror("shmdt");
        return 1;
    }

    if (shmctl(shmid, IPC_RMID, 0) == -1) {
        perror("shmctl");
        return 1;
    }
    printf("Writing Process: Complete\n");
    return 0;
}

int fill_buffer(char *bufptr, int size, char* word) {

    int filled_count;

    //printf("size is %d\n", size);

    strcpy(bufptr, word);
    filled_count = strlen(bufptr);

    printf("buffer filled is:%s\n", word);
    printf("buffer count is: %d\n", filled_count);
    printf("buffer filled is:%s\n", bufptr);

    return filled_count;
}