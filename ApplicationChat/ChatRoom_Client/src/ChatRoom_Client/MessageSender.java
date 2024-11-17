/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import View.V_FrmChat_Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class MessageSender implements Runnable {

    private OutputStream output;
    private V_FrmChat_Client vFC;
    private Socket socket;
    

    public MessageSender(Socket socket, V_FrmChat_Client vFC) {
        this.socket = socket;
        this.vFC = vFC;
        try {
            this.output = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private ChatClient chatClient = new ChatClient(vFC);
//    
//    private String clientID = chatClient.clientID;
//    private String clientName = chatClient.clientName;

    @Override
    public void run() {
//        System.out.println("Chào tôi là: "+clientName+"|"+clientID);
        
        vFC.btn_guiTin.addActionListener(new ActionListener() {  //Lắng nghe nút gửi(ENTER) được nhấn thì gửi tin nhắn
            public void actionPerformed(ActionEvent arg0) {
                if (vFC.textField.getText().isEmpty()) {
                    return;
                }
                try {
                    String message = vFC.textField.getText();
                    output.write((message + "\n").getBytes());
                    output.flush();
                    vFC.addMessage(message, "out"); // thêm tin nhắn vào panel_tinnhan
                    vFC.textField.setText("");
                } catch (Exception e) {
                    System.out.println("Lỗi ở ChatMessageSender(khi gửi tin nhắn)");
                }
            }
        });

        vFC.btn_xacNhanTaoNhom.addActionListener(new ActionListener() {  // Nhấn nút tạo để xác nhận tạo nhóm
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String groupName = vFC.textField_TenNhom.getText();
                    List<String> selectedClients = vFC.list_UIDName_onl_taoNhom.getSelectedValuesList();  // List chứa các clients đã chọn trong list tạo nhóm
                    if (groupName == null || groupName.trim().isEmpty()) { // Chưa nhập tên
                        JOptionPane.showMessageDialog(null, "Bạn chưa nhập tên nhóm", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    } else if (selectedClients == null || selectedClients.isEmpty()) { // Chưa chọn client 
                        JOptionPane.showMessageDialog(null, "Bạn chưa chọn thành viên cho nhóm", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int quantityInGroup = selectedClients.size()+1;  // số lượng trong group
                        System.out.println("Số lượng: " +quantityInGroup);
                        sendGroupInfo(groupName, quantityInGroup, selectedClients);
                        vFC.panel_TaoNhom.setVisible(false);
                        vFC.panel_chat.setVisible(true);
//                      vFC.addGroupToList(groupName, quantityInGroup);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendGroupInfo(String groupName, int quantityInGroup, List<String> clientNames) {
//        if (output == null) {
//            System.out.println("Không thể gửi thông tin nhóm: Kết nối chưa được thiết lập.");
//            return;
//        }
        try {
            if (clientNames == null || clientNames.isEmpty()) {
                System.out.println("Danh sách client cho nhóm chưa được chọn !");
            }
            // Tạo chuỗi thông tin nhóm: "GROUP #groupName# client1 ++ client2 ++ client3,..."
            String message = "GROUP#" + groupName + "#"+quantityInGroup+"#" + String.join(" ++ ", clientNames);
            System.out.println(message);

            // Sử dụng output của `infoSocket` để gửi thông tin nhóm lên server
            output.write((message + "\n").getBytes());
            output.flush();
        } catch (IOException e) {
            System.out.println("Lỗi khi gửi thông tin nhóm về server: " + e.getMessage());
        }
    }

//    public void sendMessage() {
//        try {
//            String message = vFC.textField.getText();
//            output.write((message + "\n").getBytes());
//            output.flush();
//            vFC.addMessage(message, "out"); // thêm tin nhắn vào panel_tinnhan
//            vFC.textField.setText("");
//        } catch (Exception e) {
//            System.out.println("Lỗi ở ChatMessageSender");
//        }
//    }
}
