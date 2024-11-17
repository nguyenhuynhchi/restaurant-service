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
public class ClientInFo_Listener implements Runnable {

    private Socket infoSocket;
    private InputStream input;
    private OutputStream output;
    public boolean connect;
    private String NameCln;
    private String IDCln;
    private V_FrmChat_Client vFC;

    private StringBuilder messageBuilder = new StringBuilder(); // Dùng để lưu trữ thông điệp nhận được

    public ClientInFo_Listener(Socket infoSocket, V_FrmChat_Client vFC) {
        this.infoSocket = infoSocket;
        this.vFC = vFC;
        try {
            this.input = infoSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClientInFo_Listener() {
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
                        String disconnectedClientName = message.split("\\|")[1];
                        vFC.removeClientFromList(disconnectedClientName); // Gọi PT xóa client từ JList
                    } else if (message.contains("|")) {  // Xử lý thông tin client mới
                        String[] dataParts = message.split("\\|"); // Tách dữ liệu tên và ID
                        if (dataParts.length == 2) {
                            this.NameCln = dataParts[0];
                            this.IDCln = dataParts[1];
                            System.out.println("Client khác đã kết nối: " + NameCln + " (" + IDCln + (")"));  // Hiển thị clientName và clientID mới

                            vFC.addClientToList(IDCln, NameCln);  // Thêm các Client vào list
                        } else {
                            System.out.println("Thông tin client không hợp lệ: " + message);
                        }
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
