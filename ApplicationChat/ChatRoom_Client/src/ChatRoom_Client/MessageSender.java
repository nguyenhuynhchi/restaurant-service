/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import View.V_FrmChat_Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class MessageSender implements Runnable {

    private OutputStream output;
    private V_FrmChat_Client vFC;
    private Socket socket;
    private String clientID;
    private String clientName;
//    private ChatClient chatClient;

    public MessageSender() {
        // Rỗng
    }

    public MessageSender(Socket socket, V_FrmChat_Client vFC) {
        this.socket = socket;
        this.vFC = vFC;
//        this.chatClient = chatClient.getInstance(vFC);
        try {
            this.output = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private String clientID = chatClient.clientID;
//    private String clientName = chatClient.clientName;
    @Override
    public void run() {

        vFC.btn_guiTin.addActionListener(new ActionListener() {  //Lắng nghe nút gửi(ENTER) được nhấn thì gửi tin nhắn
            public void actionPerformed(ActionEvent arg0) {
                sendMessage();
            }
        });

        vFC.btn_xacNhanTaoNhom.addActionListener(new ActionListener() {  // Nhấn nút tạo để xác nhận tạo nhóm
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String groupName = vFC.textField_TenNhom.getText();
                    List<String> selectedClients = new ArrayList<>(vFC.list_UIDName_onl_taoNhom.getSelectedValuesList());  // List chứa các clients đã chọn trong list tạo nhóm
                    selectedClients.add(clientID + "|" + clientName); // Thêm client tạo nhóm
                    if (groupName == null || groupName.trim().isEmpty()) { // Chưa nhập tên
                        JOptionPane.showMessageDialog(null, "Bạn chưa nhập tên nhóm", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    } else if (selectedClients.size() == 1 || selectedClients.isEmpty()) { // Chưa chọn client 
                        JOptionPane.showMessageDialog(null, "Bạn chưa chọn thành viên cho nhóm", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int quantityInGroup = selectedClients.size();  // số lượng trong group
                        System.out.println("Số lượng: " + quantityInGroup);
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

        vFC.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Bạn vừa đóng ứng dụng. Ngắt kết nối khỏi server");
                sendDisconnect();

                // Đóng ứng dụng
                vFC.dispose();
                System.exit(0);
            }
        });

    }

    public void sendMessage() {
        if (vFC.tf_message.getText().isEmpty()) {
            return;
        }
        try {
            String message = vFC.tf_message.getText();
            System.out.println("Client được chọn để gửi tin: " + vFC.list_UIDName_onl.getSelectedValue());
            output.write(("MessageOfClient#" + clientID + " | " + clientName + "#" + vFC.list_UIDName_onl.getSelectedValue() + "#" + message + "\n").getBytes());
            output.flush();
            System.out.println("MessageOfClient#" + clientID + " | " + clientName + "#" + vFC.list_UIDName_onl.getSelectedValue() + "#" + message);
            vFC.addMessage(message, "out"); // thêm tin nhắn vào panel_tinnhan
            vFC.tf_message.setText("");
        } catch (Exception e) {
            System.out.println("Lỗi ở ChatMessageSender(khi gửi tin nhắn)");
        }
    }

    String selection;

    public String selection() {
        try {
            vFC.list_UIDName_onl.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        if (vFC.list_UIDName_onl.getSelectedValue() != null) {
                            selection = vFC.list_UIDName_onl.getSelectedValue();
                        }
                    }
                }
            });
            System.out.println(selection);
        } catch (Exception e) {
            System.out.println("Lỗi ở ChatMessageSender(khi gửi tin nhắn)");
        }
        return selection;
    }

    public void sendGroupInfo(String groupName, int quantityInGroup, List<String> clients) {
        try {
            if (clients == null || clients.isEmpty()) {
                System.out.println("Danh sách client cho nhóm chưa được chọn !");
            }
            // Tạo chuỗi thông tin nhóm: "GROUP #groupName# client1 ++ client2 ++ client3,..."
            String message = "GROUP#" + groupName + "#" + quantityInGroup + "#" + String.join(" ++ ", clients);
            System.out.println(message);

            // Sử dụng output của `infoSocket` để gửi thông tin nhóm lên server
            output.write((message + "\n").getBytes());
            output.flush();
        } catch (IOException e) {
            System.out.println("Lỗi khi gửi thông tin nhóm về server: " + e.getMessage());
        }
    }

    public void sendDisconnect() {
        try {
            String disconnectMessage = "DISCONNECT#" + clientID + "|" + clientName;
            output.write(disconnectMessage.getBytes());
            output.flush();

        } catch (IOException e) {
            System.err.println("Lỗi khi ngắt kết nối: " + e.getMessage());
        }
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
