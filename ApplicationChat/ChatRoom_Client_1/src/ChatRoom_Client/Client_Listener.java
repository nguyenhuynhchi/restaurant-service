/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class Client_Listener implements Runnable {

    private Socket socket;
    private InputStream input;
    private OutputStream output;
    public boolean connect;
    private String clientName;
    private String clientID;
    private V_FrmChat_Client vFC;
    private V_FrmUserAccess vFU;
    private ChatClient chatCLient;

    public int successLogIn = -1;

    private StringBuilder messageBuilder = new StringBuilder(); // Dùng để lưu trữ thông điệp nhận được

    public Client_Listener(Socket socket, V_FrmChat_Client vFC, V_FrmUserAccess vFU) {
        this.socket = socket;
        this.vFC = vFC;
        this.vFU = vFU;
        this.chatCLient = chatCLient.getInstance(vFC, vFU);
        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            int bytesRead;
            byte[] buffer = new byte[524288];

            while ((bytesRead = input.read(buffer)) != -1) {
                messageBuilder.append(new String(buffer, 0, bytesRead));  // Thêm dữ liệu mới vào messageBuilder

                while (messageBuilder.indexOf("\n") != -1) {  // Xử lý thông điệp nếu có dấu xuống dòng \n
                    int endIndex = messageBuilder.indexOf("\n");  // Tìm vị trí dấu xuống dòng
                    String message = messageBuilder.substring(0, endIndex).trim();  // Tách thông điệp đầy đủ
                    messageBuilder.delete(0, endIndex + 1);  // Xóa thông điệp đã xử lý khỏi messageBuilder

                    //Kiểm tra đăng nhập thành công không 
                    if (message.startsWith("UNSUCCESS")) {  // Đăng nhập không thành công
                        System.out.println("Đăng nhập không thành công");
                        successLogIn = 0;
                        System.out.println("success: " + successLogIn);
                    } else if (message.startsWith("SUCCESS#")) {  // đăng nhập thành công
                        String[] part = message.split("\\#");
                        vFC.userID = part[1];

//                        vFC.lbl_IDNguoiDung.setText("ID: "+vFC.userID);
                        System.out.println("Đăng nhập thành công. ID là:" + vFC.userID);
                        successLogIn = 1;
                        System.out.println("success: " + successLogIn);
                    } else if (message.startsWith("InfoClients#")) {  // Nhận thông tin các client kết nối khác để thêm vào JList
                        String[] infoClient = message.split("\\#"); // Tách dữ liệu tên và ID

                        if (infoClient.length == 2) {
                            String clientID = infoClient[1].split("\\|")[0];
                            String clientName = infoClient[1].split("\\|")[1];
                            System.out.println("Client khác đang kết nối: " + clientID + "|" + clientName);

                            // Hiển thị clientName và clientID mới
                            vFC.addClientToList(clientID, clientName);  // Thêm các Client vào list
                        } else {
                            System.out.println("Thông tin client không hợp lệ: " + message);
                        }
                    } // Xử lý thông điệp ngắt kết nối
                    else if (message.startsWith("DISCONNECT#")) { // thông điệp ngắt kết nối của 1 client nào đó
                        String infoClientDisconnect = message.split("\\#")[1]; // Lấy tên Client vừa ngắt kết nối 
                        vFC.removeClientInList(infoClientDisconnect); // Gọi PT xóa client từ JList
                        String messageDisconnect = "<b>!</b> " + infoClientDisconnect + " - ĐÃ NGẮT KẾT NỐI " + "<b>!</b>";
                        System.out.println(messageDisconnect);

                        LocalDateTime now = LocalDateTime.now();
                        Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                        String timeOfMes = formatter.format(time);
                        vFC.addMessage(messageDisconnect, "said", 1, new Color(120, 120, 255), timeOfMes);
                    } // Xử lý thông điệp được thêm vào group
                    else if (message.startsWith("AddedToGroup#")) { // thông điệp được thêm vào nhóm
                        // Tách lấy tên nhóm và danh sách client
                        String[] parts = message.split("\\#");
                        String groupName = parts[1].trim();
                        String quantityInGroup = parts[2];
                        List<String> clientsInGroup = new ArrayList<>(Arrays.asList(parts[3].split(" \\+\\+ ")));
                        chatCLient.createGroup(groupName, quantityInGroup, clientsInGroup);
                    } else if (message.startsWith("IMAGE#")) { // tin nhắn hình ảnh
                        System.out.println("\n - Nhận thông điệp hình ảnh base64Image: \n**" + message);
                        String[] parts = message.split("\\#");
                        String clientOrGroupSend = parts[1];
                        String base64Image = parts[2];
                        vFC.addMessage("IMAGE#" + base64Image, "in", 1, null, null);

                        LocalDateTime now = LocalDateTime.now();
                        Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                        String timeOfMes = formatter.format(time);
                        vFC.addMessage(clientOrGroupSend, "in", 1, new Color(173, 216, 230), timeOfMes);
                    } else if (message.startsWith("UpdateMessage#")) {
                        System.out.println(message);
                        String[] parts = message.split("\\#");
                        if (parts.length == 11) {
                            String senderID = parts[2];
                            String senderName = parts[3];
                            String receiverID = parts[4];
                            String receiverName = parts[5];
                            String receiverGroupID = parts[6];
                            String receiverGroupName = parts[7];
                            String contentMessage = parts[8];
                            String timeOfMes = parts[9] + " ~ " + parts[10];

                            if (this.getClientID() != null && this.getClientName() != null) {

                                if (senderID.equals(this.getClientID())) {
                                    if (!receiverID.equals("null")) {
                                        vFC.addMessage("[Gửi đến {" + receiverID + "|" + receiverName + "}] - " + contentMessage, "out", 1, new Color(144, 238, 144), timeOfMes);
                                    } else if (!receiverGroupID.equals("null")) {
                                        vFC.addMessage("[Gửi đến nhóm {" + receiverGroupName + "}] - " + contentMessage, "out", 1, new Color(144, 238, 144), timeOfMes);
                                    }
                                } else if (!senderID.equals(this.getClientID())) {
                                    if (!receiverID.equals("null")) {
                                        vFC.addMessage("[" + senderName + "(" + senderID + ")] - " + contentMessage, "in", 1, new Color(173, 216, 230), timeOfMes);
                                    } else if (!receiverGroupID.equals("null")) {
                                        vFC.addMessage("[" + receiverGroupName + " {" + senderName + "(" + senderID + ")}] - " + contentMessage, "in", 1, new Color(173, 216, 230), timeOfMes);
                                    }
                                } else{
                                    System.out.println("Dữ liệu không hợp lệ để thêm tin nhắn");
                                }                                
                            }
                        } else{
                            System.out.println("Dữ liệu không hợp lệ để xử lý");
                        }
                    } else { // Nếu không phải các thông điệp thì là tin nhắn nhận được
                        System.out.println("Tin nhắn từ phòng chat: " + message);

                        LocalDateTime now = LocalDateTime.now();
                        Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                        String timeOfMes = formatter.format(time);
                        vFC.addMessage(message, "in", 1, new Color(173, 216, 230), timeOfMes);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("(clientListener) Lỗi kết nối server");  // Xử lý các ngoại lệ I/O khác
            e.printStackTrace();
            connect = false;
        }
    }

//    public synchronized void createGroup(String groupName, String quantityInGroup, List<String> clientsInGroup) {
//        listGroups.put(groupName, clientsInGroup);
//        System.out.println("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên: ");
//        for (String clients : clientsInGroup) {
//            System.out.println(clients);
//        }
//        vFC.addGroupToList(groupName, quantityInGroup);
//        System.out.println("Danh sách nhóm có bạn là thành viên: "+listGroups);
//        vFC.addMessage("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên: ", "in");
//    }
    public String getClientName() {
        return clientName;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

}
