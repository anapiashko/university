public class MainClass {

    public static void main(String[] args) {
        String message = "Hello world";
        String test = "abc";

        // ----First step-----

       Algorithm_RIPEMD128 algorithm_ripemd128 = new Algorithm_RIPEMD128(test);
       algorithm_ripemd128.start();

//        System.out.println(algorithm_ripemd128.utfToBin("a"));
    }
}
