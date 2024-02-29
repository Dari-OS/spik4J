import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.*;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws NativeHookException {
            OnCopyEvent onCopyEvent = new OnCopyEvent();
            onCopyEvent.start();


    }

}
