/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import View.V_FrmChat_Client;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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
    private ChatClient chatClient;

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

    @Override
    public void run() {

        vFC.btn_guiTin.addActionListener(new ActionListener() {  //Lắng nghe nút gửi(ENTER) được nhấn thì gửi tin nhắn
            public void actionPerformed(ActionEvent arg0) {
                sendMessage();
            }
        });

        vFC.btn_xacNhanTaoNhom.addActionListener(new ActionListener() {  // Nhấn nút tạo để xác nhận tạo nhóm
            public void actionPerformed(ActionEvent arg0) {
                sendGroupInfo();
            }
        });

        vFC.btn_openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName().toLowerCase();
                    System.out.println("File được chọn: " + fileName);

                    // Kiểm tra phần mở rộng của file
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".png") || fileName.endsWith(".gif")) {
                        sendImage(selectedFile); // Gửi file dưới dạng hình ảnh
                    } else {
                        System.out.println("Không thể gừi file");
//                        sendFile(selectedFile); // Gửi file thông thường
                    }
                }
            }
        });

        vFC.addWindowListener(new WindowAdapter() {  // Đóng ứng dụng gửi thông điệp về server
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

    public void sendFile(File file) {
        try {
            if (vFC.list_UIDName_onl.getSelectedValue() == null && vFC.list_GroupName.getSelectedValue() == null) {
                return;
            } else if (vFC.list_UIDName_onl.getSelectedValue() != null) {

                String clientSelected = vFC.list_UIDName_onl.getSelectedValue();
                System.out.println("Client được chọn để gửi tin: " + clientSelected);

                // Đọc file vào mảng byte
                byte[] fileBytes = Files.readAllBytes(file.toPath());

                // Gửi header chứa thông tin file
                String messageFile = "MessageOfClient#FILE#" + clientID + "|" + clientName + "#" + clientSelected + "#" + file.getName() + "#" + fileBytes.length;
                output.write((messageFile + "\n").getBytes());
                output.flush();

                // Gửi nội dung file
                output.write(fileBytes);
                output.flush();

                System.out.println(" - Đã gửi thông điệp: MessageOfClient#FILE#" + clientID + "|" + clientName + "#" + clientSelected + "#" + file.getName() + "#" + fileBytes.length);

                LocalDateTime now = LocalDateTime.now();
                Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                String timeOfMes = formatter.format(time);
                vFC.addMessage(messageFile, "out", 1, null, timeOfMes);
                vFC.tf_message.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(File imageFile) {
        try {
            if (vFC.list_UIDName_onl.getSelectedValue() == null && vFC.list_GroupName.getSelectedValue() == null) {
                return;
            } else if (vFC.list_UIDName_onl.getSelectedValue() != null) {

                String clientSelected = vFC.list_UIDName_onl.getSelectedValue();
                System.out.println("Client được chọn để gửi tin: " + clientSelected);

                // Đọc file vào mảng byte
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

                // Mã hóa dữ liệu Base64
                String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

                String messageImage = "MessageOfClient#IMAGE#" + clientSelected + "#" + base64Image;

                output.write((messageImage + "\n").getBytes());
                output.flush();

                // Thông điệp khi client gửi tin nhắn riêng: MessageOfClient # ID|name client gửi # client được chọn # tin nhắn
                System.out.println(" - Đã gửi thông điệp: \nMessageOfClient#IMAGE#" + clientSelected + "#\n**" + base64Image);
                vFC.addMessage("IMAGE#" + base64Image, "out", 1, null, null);

                LocalDateTime now = LocalDateTime.now();
                Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                String timeOfMes = formatter.format(time);
                vFC.addMessage("[Gửi hình ảnh đến {" + clientSelected + "}]", "out", 1, new Color(144, 238, 144), timeOfMes);

                vFC.tf_message.setText("");
            } else if (vFC.list_GroupName.getSelectedValue() != null) {

                String groupSelected = vFC.list_GroupName.getSelectedValue().split("\\|")[0].trim();
                System.out.println("Nhóm được chọn để gửi tin: " + groupSelected);

                // Đọc file vào mảng byte
                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

                // Mã hóa dữ liệu Base64
                String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

                String messageImage = "MessageOfGroup#IMAGE#" + groupSelected + "#" + base64Image;

                output.write((messageImage + "\n").getBytes());
                output.flush();

                // Thông điệp khi client gửi tin nhắn riêng: MessageOfClient # ID|name client gửi # client được chọn # tin nhắn
                System.out.println(" - Đã gửi thông điệp: MessageOfGroup#IMAGE#" + groupSelected + "#" + imageFile.getName() + "#" + base64Image);
                vFC.addMessage("IMAGE#" + base64Image, "out", 1, null, null);

                LocalDateTime now = LocalDateTime.now();
                Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                String timeOfMes = formatter.format(time);
                vFC.addMessage("[Gửi hình ảnh đến nhóm {" + groupSelected + "}]", "out", 1, new Color(144, 238, 144), timeOfMes);
                vFC.tf_message.setText("");
            }
        } catch (Exception e) {
            System.out.println("Lỗi ở ChatMessageSender(khi gửi tin nhắn)");
        }
    }

    public void sendMessage() {
        if (vFC.tf_message.getText().isEmpty()) {
            return;
        }
        try {
            if (vFC.list_UIDName_onl.getSelectedValue() == null && vFC.list_GroupName.getSelectedValue() == null) {
                return;
            } else if (vFC.list_UIDName_onl.getSelectedValue() != null) {
                String clientSelected = vFC.list_UIDName_onl.getSelectedValue();
                String message = vFC.tf_message.getText();
                System.out.println("Client được chọn để gửi tin: " + clientSelected);
                output.write(("MessageOfClient#" + clientSelected + "#" + message + "\n").getBytes());
                output.flush();

                // Thông điệp khi client gửi tin nhắn riêng: MessageOfClient # ID|name client gửi # client được chọn # tin nhắn
                System.out.println(" - Đã gửi thông điệp: MessageOfClient#" + clientSelected + "#" + message);
                // thêm tin nhắn vào panel_tinnhan (màu xanh lá)

                LocalDateTime now = LocalDateTime.now();
                Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                String timeOfMes = formatter.format(time);
                vFC.addMessage("[Gửi đến {" + clientSelected + "}] - " + message, "out", 1, new Color(144, 238, 144), timeOfMes);

                vFC.tf_message.setText("");
            } else if (vFC.list_GroupName.getSelectedValue() != null) {
                String message = vFC.tf_message.getText();
                String groupSelected = vFC.list_GroupName.getSelectedValue().split("\\|")[0].trim();
                System.out.println("Nhóm được chọn để gửi tin: " + groupSelected);
                output.write(("MessageOfGroup#" + groupSelected + "#" + message).getBytes());
                output.flush();

                // Thông điệp khi client gửi tin nhắn vào nhóm: MessageOfGroup # ID|name client gửi # tên nhóm được chọn # tin nhắn
                System.out.println(" - Đã gửi thông điệp: MessageOfGroup#" + groupSelected + "#" + message);

                LocalDateTime now = LocalDateTime.now();
                Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                String timeOfMes = formatter.format(time);
                vFC.addMessage("[Gửi đến nhóm {" + groupSelected + "}] - " + message, "out", 1, new Color(144, 238, 144), timeOfMes); // thêm tin nhắn vào panel_tinnhan
                vFC.tf_message.setText("");
            }
        } catch (Exception e) {
            System.out.println("Lỗi ở ChatMessageSender(khi gửi tin nhắn)");
        }
    }

    public void sendNewCreateClient(String clientName, String clientID) {

    }

    public void sendInfo(String info) {
        try {
            output.write(info.getBytes());
            output.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi kết nối: " + e.getMessage());
        }
    }

    public void sendGroupInfo() {
        try {
            String groupName = vFC.textField_TenNhom.getText();
            List<String> selectedClients = new ArrayList<>(vFC.list_UIDName_onl_taoNhom.getSelectedValuesList());  // List chứa các clients đã chọn trong list tạo nhóm
            selectedClients.add(clientID + "|" + clientName); // Thêm client tạo nhóm
            int quantityInGroup = selectedClients.size();  // số lượng trong group
            System.out.println("Số lượng: " + quantityInGroup);

            if (groupName == null || groupName.trim().isEmpty()) { // Chưa nhập tên
                JOptionPane.showMessageDialog(null, "Bạn chưa nhập tên nhóm", "Thông báo", JOptionPane.ERROR_MESSAGE);
            } else if (selectedClients.size() == 1 || selectedClients.isEmpty()) { // Chưa chọn client 
                JOptionPane.showMessageDialog(null, "Bạn chưa chọn thành viên cho nhóm", "Thông báo", JOptionPane.ERROR_MESSAGE);
            } else {
                // Tạo thông điệp tạo nhóm gửi về server
                String message = "CREATEGROUP#" + groupName + "#" + quantityInGroup + "#" + String.join(" ++ ", selectedClients);
                System.out.println(" - Đã gửi thông điệp: " + message);
                // Sử dụng output của `infoSocket` để gửi thông tin nhóm lên server
                output.write((message + "\n").getBytes());
                output.flush();
            }
            vFC.panel_TaoNhom.setVisible(false);
            vFC.panel_chat.setVisible(true);
        } catch (IOException e) {
            System.out.println("Lỗi khi gửi thông tin nhóm về server: " + e.getMessage());
        }
    }

    public void sendDisconnect() {
        try {
            String disconnectMessage = "DISCONNECT#";
            output.write(disconnectMessage.getBytes());
            output.flush();

        } catch (IOException e) {
            System.err.println("Lỗi khi ngắt kết nối: " + e.getMessage());
        }
    }

    public void sendGroupInfo_(String groupName, int quantityInGroup, List<String> clients) {
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
