package ChatRoom_Client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import View.V_FrmChat_Client;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ChatClient {

    private static final String URL = "localhost";
    private static final int PORT_INFO = 5000;
    private static final int PORT_CHAT = 5001;
    private V_FrmChat_Client vFC;
    private String clientName;
    private String clientID;
    public boolean connect;

    private Socket infoSocket;
    private Socket chatSocket;

    public ChatClient(V_FrmChat_Client vFC) {
        this.vFC = vFC;
    }

    public void StartClient() {
        try {
            clientName = vFC.userName;
            clientID = vFC.ID;

            infoSocket = new Socket(URL, PORT_INFO);
            chatSocket = new Socket(URL, PORT_CHAT);

            System.out.println("Connected to server");
            connect = true; //Kiểm tra kết nối thành công thì không hiện panel thông báo

            System.out.println("Name(ID) của bạn: " + clientName + "(" + clientID + ")");
            // Gửi tên của client lên server
            OutputStream InfoOutput = infoSocket.getOutputStream();
            InfoOutput.write(("InfoNewClients|"+clientName + "|" + clientID + "\n").getBytes());

            // Tạo và khởi chạy thread cho chatInfo_Listener để nhận thông tin các client từ server gửi về
            ClientInFo_Listener clientInfoListener = new ClientInFo_Listener(infoSocket, vFC);
            new Thread(clientInfoListener).start();

            // Tạo và khởi chạy thread cho chatMessage_Listener để nhận tin nhắn chat từ server gửi về
            ChatMessage_Listener chatMessageListener = new ChatMessage_Listener(chatSocket, vFC);
            new Thread(chatMessageListener).start();

            // Gửi tin nhắn thông qua class MessageSender lên server
            OutputStream chatOutput = chatSocket.getOutputStream();
            MessageSender messageSender = new MessageSender(chatOutput, vFC);
            new Thread(messageSender).start();

        } catch (java.net.ConnectException e) {
            System.out.println("Lỗi: Không thể kết nối đến server. Vui lòng kiểm tra server và thử lại.");
            connect = false; //Kiểm tra kết nối không thành công thì hiện panel thông báo
        } catch (Exception e) {
            System.out.println("Lỗi ở chatClient");  // Xử lý các ngoại lệ I/O khác
            connect = false; //Kiểm tra kết nối không thành công thì hiện panel thông báo
        }
        vFC.hienThongBaoKetNoi(connect);
    }

//    public void sendGroupInfo(String groupName, List<String> clientNames) {
//        if (infoSocket == null || infoSocket.isClosed()) {
//            System.out.println("Không thể gửi thông tin nhóm: Kết nối chưa được thiết lập.");
//            return;
//        }
//
//        try {
//            // Tạo chuỗi thông tin nhóm, ví dụ: "GROUP|groupName|client1,client2,client3,..."
//            String message = "GROUP|" + groupName + "|" + String.join(",", clientNames);
//
//            // Sử dụng output của `infoSocket` để gửi thông tin nhóm lên server
//            OutputStream infoOutput = infoSocket.getOutputStream();
//            infoOutput.write((message + "\n").getBytes());
//            infoOutput.flush();
//        } catch (IOException e) {
//            System.out.println("Lỗi khi gửi thông tin nhóm về server: " + e.getMessage());
//        }
//    }

}


