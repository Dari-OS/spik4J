import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CopyEventHandler extends Thread{


    private  String authenticationKey = "";
    private static String lastContent = getClipboard();
    private static String currentContent = getClipboard();

    @Override
    public void run() {
        if (authenticationKey.isEmpty()) throw new IllegalArgumentException("Please provide an authentication key for OpenAI's GPT!");
        while (true) {
            try {
                Thread.sleep(500);
                currentContent = getClipboard();
                if (!currentContent.equals(lastContent)) {
                    lastContent = GPTapiHandler.manager(currentContent, authenticationKey);
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

    private void clearClipboard() {

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(""), null);

    }

    public  CopyEventHandler setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        return this;
    }

    public static void setLastContent(String lastContent) {
        CopyEventHandler.lastContent = lastContent;
    }
}
