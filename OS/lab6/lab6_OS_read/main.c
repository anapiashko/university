/* Filename: shm_read.c */
#include<stdio.h>
#include<sys/ipc.h>
#include<sys/shm.h>
#include<sys/types.h>
#include<string.h>
#include<errno.h>
#include<stdlib.h>
#include <zconf.h>

#define BUF_SIZE 1024
#define SHM_KEY 0x1234

struct shmseg {
    int cnt;
    int complete;
    char buf[BUF_SIZE];
};

int main(int argc, char *argv[]) {

    char* words[2] = {"", ""};

    int shmid;
    struct shmseg *shmp;
    shmid = shmget(SHM_KEY, sizeof(struct shmseg), 0644|IPC_CREAT);
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

    int num_words = 0;
    /* Transfer blocks of data from shared memory to stdout*/
    while (shmp->complete != 1) {

        char* temp_buf = shmp->buf;
        int size_buf = shmp->cnt;

        char* temp_array = (char*)malloc(size_buf * sizeof(char));;
        for (int i = 0; i < size_buf; i++) {
            char t = temp_buf[i];
            temp_array[i] = t;
        }
        words[num_words] = temp_array;

        printf("segment contains : \n\"%s\"\n", temp_buf);
        num_words++;
        if (shmp->cnt == -1) {
            perror("read");
            return 1;
        }
        printf("Reading Process: Shared Memory: Read %d bytes\n", size_buf);
        sleep(5);
    }
    printf("Reading Process: Reading Done, Result :%s, %s\n", words[0], words[1]);
    printf("Reading Process: Reading Done, Result :%s\n", strcat(words[0], words[1]));
    if (shmdt(shmp) == -1) {
        perror("shmdt");
        return 1;
    }
    printf("Reading Process: Complete\n");
    return 0;
}