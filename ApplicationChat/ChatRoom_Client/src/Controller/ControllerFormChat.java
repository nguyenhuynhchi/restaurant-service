package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ChatRoom_Client.ChatClient;
import View.V_FrmChat_Client;

public class ControllerFormChat implements ActionListener {

    private V_FrmChat_Client vFC;
    private ChatClient chatClient;

    public ControllerFormChat(V_FrmChat_Client vFC, ChatClient chatClient) {
        this.vFC = vFC;
        this.chatClient = chatClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String src = e.getActionCommand();
        JButton srcBtn = (JButton) e.getSource();

        
    }

    public void kiemTraKetNoi() {
        if (chatClient.connect == false) {
            vFC.hienThongBaoKetNoi(true);  // Hiển thị thông báo nếu kết nối không thành công
        } else {
            vFC.hienThongBaoKetNoi(false); // Ẩn thông báo nếu kết nối thành công
        }
    }

}
