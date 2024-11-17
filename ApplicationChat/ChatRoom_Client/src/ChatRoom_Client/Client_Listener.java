/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import View.V_FrmChat_Client;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class Client_Listener implements Runnable {

    private Socket socket;
    private InputStream input;
    private OutputStream output;
    public boolean connect;
    private String NameCln;
    private String IDCln;
    private V_FrmChat_Client vFC;

    private StringBuilder messageBuilder = new StringBuilder(); // Dùng để lưu trữ thông điệp nhận được

    public Client_Listener(Socket socket, V_FrmChat_Client vFC) {
        this.socket = socket;
        this.vFC = vFC;
        try {
            this.input = socket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client_Listener() {
        //rỗng
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[2048];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                messageBuilder.append(new String(buffer, 0, bytesRead));  // Thêm dữ liệu mới vào messageBuilder

                while (messageBuilder.indexOf("\n") != -1) {  // Xử lý thông điệp nếu có dấu xuống dòng \n
                    int endIndex = messageBuilder.indexOf("\n");  // Tìm vị trí dấu xuống dòng
                    String message = messageBuilder.substring(0, endIndex).trim();  // Tách thông điệp đầy đủ
                    messageBuilder.delete(0, endIndex + 1);  // Xóa thông điệp đã xử lý khỏi messageBuilder

                    // Xử lý thông báo ngắt kết nối
                    if (message.startsWith("DISCONNECT|")) {
                        String disconnectedClientName = message.split("\\|")[1]; // Lấy tên Client vừa ngắt kết nối phía sau DISCONNECT|
                        vFC.removeClientFromList(disconnectedClientName); // Gọi PT xóa client từ JList
                        System.out.println(disconnectedClientName + " - ĐÃ NGẮT KẾT NỐI");
                    } // 
                    else if (message.startsWith("InfoNewClients|")) {  // Xử lý thông tin client mới
                        String[] infoClient = message.split("\\|"); // Tách dữ liệu tên và ID

                        if (infoClient.length == 3) {
                            this.NameCln = infoClient[1];
                            this.IDCln = infoClient[2];
                            System.out.println("Client khác đang kết nối: " + NameCln + " (" + IDCln + (")"));  // Hiển thị clientName và clientID mới

                            vFC.addClientToList(IDCln, NameCln);  // Thêm các Client vào list
                        } else {
                            System.out.println("Thông tin client không hợp lệ: " + message);
                        }
                    } //
                    else {  // Nếu không phải 2 thông báo ngắt kết nối hay có thêm client mới thì là tin nhắn nhận được
                        // Hiển thị tin nhắn nhận được
                        System.out.println("Tin nhắn từ phòng chat: " + message);
                        vFC.addMessage(message, "in");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("(clientListener) Lỗi kết nối server");  // Xử lý các ngoại lệ I/O khác
            connect = false;
        }
    }

    public String getNameCln() {
        return NameCln;
    }

    public String getIDCln() {
        return IDCln;
    }
}
