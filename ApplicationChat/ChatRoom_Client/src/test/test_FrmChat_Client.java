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
                // Khởi tạo client chat và truyền view vào
                ChatClient chatClient = new ChatClient(vFC);
                chatClient.StartClient();
                
                vFU.setVisible(true);
//                vFC.setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi ở test");
        }
    }
}
