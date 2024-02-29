import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class OnCopyEvent extends Thread{


    private static String authenticationKey = "";
    private static String lastContent = getClipboard();
    private static String currentContent = getClipboard();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

                currentContent = getClipboard();
                if (!currentContent.equals(lastContent)) {
                    System.out.println("test");
                    lastContent = AI_API.manager(currentContent);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }

    private static String getClipboard() {
        try {

            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

        } catch (IOException | UnsupportedFlavorException e) {
            System.exit(-1);
            return "";
        }
    }

    private static void clearClipboard() {

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(""), null);

    }
}
