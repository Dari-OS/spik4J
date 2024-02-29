import org.jnativehook.NativeHookException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CopyEventHandler copyEventHandler = new CopyEventHandler();
            if (args.length != 0) {
                copyEventHandler.setAuthenticationKey(args[0]);
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please provide an api key for chatGPT:");
                copyEventHandler.setAuthenticationKey(scanner.next());
            }
        copyEventHandler.start();



    }

}
