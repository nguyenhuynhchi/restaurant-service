package test;

import javax.swing.UIManager;

import ChatRoom_Client.ChatClient;
import View.V_FrmChat_Client;
import View.V_FrmUserAccess;

public class test_FrmChat_Client {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            V_FrmChat_Client vFC = new V_FrmChat_Client();
            V_FrmUserAccess vFU = new V_FrmUserAccess(vFC);
            vFU.setVisible(true);
            
            while (!vFU.connect && !vFC.connect) {
                Thread.sleep(100); // Đợi một chút
            }
            
            // Khởi tạo client chat và truyền view vào
            ChatClient chatClient = new ChatClient(vFC);
            chatClient.StartClient();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi ở test");
        }
    }
}
