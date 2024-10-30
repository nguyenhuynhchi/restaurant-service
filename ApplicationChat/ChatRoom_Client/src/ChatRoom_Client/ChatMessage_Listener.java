package ChatRoom_Client;

import View.V_FrmChat_Client;
import java.io.InputStream;
import java.net.Socket;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ChatMessage_Listener implements Runnable {

    private Socket chatSocket;
    private InputStream input;

    public ChatMessage_Listener(Socket chatSocket) {
        this.chatSocket = chatSocket;
        try {
            this.input = chatSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[2048];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead).trim();

                // Hiển thị tin nhắn nhận được
                System.out.println("Tin nhắn từ phòng chat: " + message);
            }
        } catch (Exception e) {
            System.out.println("(ChatMessageListener) Lỗi kết nối server");
        }
    }
}
