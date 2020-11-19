//#include <stdlib.h>
//#include <stdio.h>
//#include <unistd.h>
//#include <sys/types.h>
//
//int main() {
//    printf("+Порождение процесса 0: PID: %d\tPPID: %d\n", getpid(), getppid());
//    pid_t pid1 = fork();
//    if (pid1 == -1) {
//        sleep(4);
//        printf("Error 1");
//    } else if (pid1 == 0) {
//        /* ребенок 1 */
//        printf("+Порождение процесса 1: PID: %d\tPPID: %d\n", getpid(), getppid());
//        pid_t  pid4 = fork();
//        if (pid4 == -1) {
//            printf("Error 4");
//        } else if(pid4 == 0){
//            printf("+Порождение процесса 4: PID: %d\tPPID: %d\n", getpid(), getppid());
//            printf("-Завершение процесса 4: PID: %d\tPPID: %d\n", getpid(), getppid());
//            exit(0);
//        } else {
//            pid_t  pid3 = fork();
//            if (pid3 == -1){
//                printf("Error 3");
//            } else if(pid3 == 0) {
//                printf("+Порождение процесса 3: PID: %d\tPPID: %d\n", getpid(), getppid());
//                pid_t  pid5 = fork();
//                if (pid5 == -1){
//                    printf("Error 5");
//                } else if(pid5 == 0){
//                    printf("+Порождение процесса 5: PID: %d\tPPID: %d\n", getpid(), getppid());
//                    printf("-Завершение процесса 5: PID: %d\tPPID: %d\n", getpid(), getppid());
//                    exit(0);
//                } else{
//                    pid_t  pid6 = fork();
//                    if (pid6 == -1){
//                        printf("Error 6");
//                    } else if(pid6 == 0) {
//                        printf("+Порождение процесса 6: PID: %d\tPPID: %d\n", getpid(), getppid());
//                        printf("-Завершение процесса 6: PID: %d\tPPID: %d\n", getpid(), getppid());
//                        exit(0);
//                    } else{
//                        /* родитель 5 */
//                        printf("-Завершение процесса 3: PID: %d\tPPID: %d\n", getpid(), getppid());
//                        exit(0);
//                    }
//                }
//            } else {
//                pid_t  pid2 = fork();
//                if (pid2 == -1){
//                    printf("Error 2");
//                }else if(pid2 == 0){
//                    sleep(4);
//                    printf("+Порождение процесса 2: PID: %d\tPPID: %d\n", getpid(), getppid());
//                    pid_t  pid7 = fork();
//                    if (pid7 == -1){
//                        printf("Error 7");
//                    } else if(pid7 == 0) {
//                        printf("+Порождение процесса 7: PID: %d\tPPID: %d\n", getpid(), getppid());
//                        printf("-Завершение процесса 7: PID: %d\tPPID: %d\n", getpid(), getppid());
//                        exit(0);
//                    } else{
//                        /* родитель 7 */
//                        printf("-Завершение процесса 2: PID: %d\tPPID: %d\n", getpid(), getppid());
//                        exit(0);
//                    }
//
//                }else{
//                    /* родитель 2 */
//                    sleep(5);
//                    printf("-Завершение процесса 1: PID: %d\tPPID: %d\n", getpid(), getppid());
//                    exit(0);
//                }
//            }
//        }
//    } else {
//        /* родитель 1 */
//
//    }
//}