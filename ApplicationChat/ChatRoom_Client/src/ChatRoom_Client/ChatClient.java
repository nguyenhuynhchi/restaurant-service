package ChatRoom_Client;

import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.Color;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ChatClient {

    private static final String URL = "localhost";
    private int port;

    private static ChatClient instance;
    private V_FrmChat_Client vFC;
    private V_FrmUserAccess vFU;
    public String clientName;
    public String clientID;
    public String password;
    public boolean connect;

    private Socket socket;
//    private Socket chatSocket;
    private MessageSender messageSender;
    private Client_Listener client_Listener;

    public Map<String, List<String>> listGroups = new HashMap<>();

    public ChatClient(V_FrmChat_Client vFC, V_FrmUserAccess vFU) {
        this.vFC = vFC;
        this.vFU = vFU;
    }
    
//    public ChatClient(V_FrmChat_Client vFC) {
//        this.vFC = vFC;
//    }

    public static synchronized ChatClient getInstance(V_FrmChat_Client vFC, V_FrmUserAccess vFU) {
        if (instance == null) {
            instance = new ChatClient(vFC, vFU);
        }
        return instance;
    }
    
//    public static synchronized ChatClient getInstance(V_FrmChat_Client vFC) {
//        if (instance == null) {
//            instance = new ChatClient(vFC);
//        }
//        return instance;
//    }

    public void StartClient() {
        try {
            port = vFC.port;
            socket = new Socket(URL, port);

            // Tạo và khởi chạy thread cho Client_Listener để nhận thông tin các client từ server gửi về
            client_Listener = new Client_Listener(socket, vFC, vFU);
            new Thread(client_Listener).start();

            // Tạo và khởi chạy thread cho MessageSender để gửi các thông tin đến server
            messageSender = new MessageSender(socket, vFC);
            new Thread(messageSender).start();

//            messageSender.setClientName(clientName);
//            messageSender.setClientID(clientID);
            if (vFU.newCreate == false) {  // Đăng nhập
                while (true) {
                    while (vFU.connect == false) {  // nếu server báo thông tin đăng nhập sai thì lặp lại và đợi sửa thông tin
                        Thread.sleep(1000); // Đợi một chút
                        System.out.println("Đợi nhập lại thông tin đăng nhập!!");
                    }
                    clientName = vFC.userName;
                    password = vFC.password;

                    // gửi thông tin đăng nhập về server
                    messageSender.sendInfo("InfoClientLogIn|" + clientName + "|" + password + "\n");
                    // Gửi thông tin đăng nhập sai về server chỉ được 1 lần 
                    System.out.println("Đã gửi tên và pass về server để kiểm tra: " + clientName + "(" + password + ")");

                    while (client_Listener.successLogIn == -1) {
                        Thread.sleep(1000); // Đợi một chút
                        System.out.println("Đợi kiểm tra!!");
                    }

                    // xử lý nếu đúng hiển thị form sai thì thông báo
                    if (client_Listener.successLogIn == 1) {  // Thông tin đúng đăng nhập thành công
                        clientID = vFC.userID;
                        messageSender.setClientID(clientID);
                        messageSender.setClientName(clientName);
                        vFC.updateUserInfo(clientID, clientName, password, port + "");
                        System.out.println("Cập nhật thông tin:" + clientID + "|" + clientName + "|" + port);
                        vFU.dispose();
                        vFC.setVisible(true);
                        System.out.println("Connected to server");
                        System.out.println("Name(ID) của bạn: " + clientName + "(" + clientID + ")");
                        connect = true; //Kiểm tra kết nối thành công thì không hiện panel thông báo
                        System.out.println("Đóng form đăng nhập và hiện thị form ứng dụng");
                        break;
                    } else if (client_Listener.successLogIn == 0) {   // Thông tin sai đăng nhập không thành công
                        System.out.println("Hiển thị dialog thông báo");
                        JOptionPane.showMessageDialog(vFU, "Sai thông tin đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        vFU.connect = false;
                        client_Listener.successLogIn = -1;
                    }
                }
            } else if (vFU.newCreate == true) {  // Đăng kí
                clientName = vFC.userName;
                clientID = vFC.userID;
                password = vFC.password;

                messageSender.setClientID(clientID);
                messageSender.setClientName(clientName);

                // gửi thông tin đăng kí về server
                messageSender.sendInfo("InfoNewCreateClients|" + clientName + "|" + clientID + "|" + password + "\n");
                System.out.println("Đã gửi thông tin đăng kí về server: " + clientID + "|" + clientName + "|" + password);
                System.out.println("Đăng kí thành công");
                System.out.println("Connected to server");
                System.out.println("Name(ID) của bạn: " + clientName + "(" + clientID + ")");
                connect = true; //Kiểm tra kết nối thành công thì không hiện panel thông báo
                System.out.println("Đã đóng form đăng nhập và hiện thị form ứng dụng");
            }
        } catch (java.net.ConnectException e) {
            System.out.println("Lỗi: Không thể kết nối đến server. Vui lòng kiểm tra server và thử lại.");
            connect = false; //Kiểm tra kết nối không thành công thì hiện panel thông báo
        } catch (Exception e) {
            System.err.println("Lỗi ở chatClient: ");
            e.printStackTrace();
            connect = false; //Kiểm tra kết nối không thành công thì hiện panel thông báo
        }
        vFC.hienThongBaoKetNoi(connect);
    }

    public synchronized void createGroup(String groupName, String quantityInGroup, List<String> clientsInGroup) {
        listGroups.put(groupName, clientsInGroup);
        System.out.println("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên: ");
        for (String clients : clientsInGroup) {
            System.out.println(clients);
        }
        vFC.addGroupToList(groupName, quantityInGroup);
        System.out.println("Danh sách nhóm có bạn là thành viên: " + listGroups);
        vFC.addMessage("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên", "said", 1, new Color(120, 120, 255));
    }

    public List<String> getClientsInGroup(String groupName) {
        if (!listGroups.containsKey(groupName)) {
            System.out.println("Nhóm '" + groupName + "' không tồn tại trong HashMap");
        }
        return listGroups.getOrDefault(groupName, Collections.emptyList());
    }
}
