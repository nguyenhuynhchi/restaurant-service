package test;

import javax.swing.UIManager;

import ChatRoom_Client.ChatClient;
import Controller.ControllerFormChat;
import View.V_FrmChat_Client;

public class test_FrmChat_Client {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            V_FrmChat_Client vFC = new V_FrmChat_Client();
            vFC.setVisible(true);

            // Khởi tạo client chat và truyền viewFormChat vào
            ChatClient chatClient = new ChatClient(vFC);
            chatClient.StartClient();

            ControllerFormChat controller = new ControllerFormChat(vFC, chatClient);
            controller.kiemTraKetNoi();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi ở test");
        }
    }
}
