//#include <stdlib.h>
//#include <stdio.h>
//#include <unistd.h>
//
//int main() {
//
//    printf("+Порождение процесса 0: PID: %d\tPPID: %d\n", getpid(), getppid());
//    if (fork() == 0) {
//        printf("+Порождение процесса 1: PID: %d\tPPID: %d\n", getpid(), getppid());
//        if (fork() == 0) {
//            sleep(4);
//            printf("+Порождение процесса 4: PID: %d\tPPID: %d\n", getpid(), getppid());
//            printf("-Завершение процесса 4: PID: %d\tPPID: %d\n", getpid(), getppid());
//        }
//
//        else if (fork() == 0) {
//            sleep(4);
//            printf("+Порождение процесса 2: PID: %d\tPPID: %d\n", getpid(), getppid());
//            if (fork() == 0) {
//                printf("+Порождение процесса 7: PID: %d\tPPID: %d\n", getpid(), getppid());
//                printf("-Завершение процесса 7: PID: %d\tPPID: %d\n", getpid(), getppid());
//                exit(0);
//            }
//            printf("-Завершение процесса 2: PID: %d\tPPID: %d\n", getpid(), getppid());
//        }
//
//       else if (fork() == 0) {
//            sleep(1);
//            printf("+Порождение процесса 3: PID: %d\tPPID: %d\n", getpid(), getppid());
//            if (fork() == 0) {
//                printf("+Порождение процесса 5: PID: %d\tPPID: %d\n", getpid(), getppid());
//                printf("-Завершение процесса 5: PID: %d\tPPID: %d\n", getpid(), getppid());
//                exit(0);
//            }
//            if (fork() == 0) {
//                printf("+Порождение процесса 6: PID: %d\tPPID: %d\n", getpid(), getppid());
//                printf("-Завершение процесса 6: PID: %d\tPPID: %d\n", getpid(), getppid());
//                sleep(1);
//                exit(0);
//            }
//            sleep(2);
//            printf("-Завершение процесса 3: PID: %d\tPPID: %d\n", getpid(), getppid());
//            exit(0);
//
//        }
//    } else {
//        sleep(5);
//        printf("-Завершение процесса 0: PID: %d\tPPID: %d\n", getpid(), getppid());
//        exit(0);
//    }
//
//    return 0;
//}