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

    public MessageSender(OutputStream output, V_FrmChat_Client vFC) {
        this.output = output;
        this.vFC = vFC;
    }
    private ChatClient chatClient = new ChatClient(vFC);

    @Override
    public void run() {
        vFC.btn_guiTin.addActionListener(new ActionListener() {
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
                        sendGroupInfo(groupName, selectedClients);
                        vFC.panel_TaoNhom.setVisible(false);
                        vFC.panel_chat.setVisible(true);
//                      vFC.addGroupToList(groupName);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public void sendGroupInfo(String groupName, List<String> clientNames) {
        if (output == null) {
            System.out.println("Không thể gửi thông tin nhóm: Kết nối chưa được thiết lập.");
            return;
        }
        try {
            if (clientNames == null || clientNames.isEmpty()) {
                System.out.println("Danh sách client được chọn để thêm vào nhóm là trống !");
            }
            // Tạo chuỗi thông tin nhóm, ví dụ: "GROUP #groupName# client1 ++ client2 ++ client3,..."
            String message = "GROUP #" + groupName + "# " + String.join(" ++ ", clientNames);
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
