package ChatRoom_Client;

import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.Color;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    public String fullName;
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
                    this.setClientName(vFC.getUserName());
                    password = vFC.password;

                    // gửi thông tin đăng nhập về server
                    messageSender.sendInfo("InfoClientLogIn|" + clientName + "|" + password + "\n");
                    // Gửi thông tin đăng nhập sai về server chỉ được 1 lần 
                    System.out.println("Đã gửi tên và pass về server để kiểm tra: " + clientName + "(" + password + ")");

                    while (client_Listener.successLogIn == -1) {
                        Thread.sleep(100); // Đợi một chút
                        System.out.println("Đợi kiểm tra!!");
                    }

                    // xử lý nếu đúng hiển thị form sai thì thông báo
                    if (client_Listener.successLogIn == 1) {  // Thông tin đúng đăng nhập thành công
                        this.setClientID(vFC.getUserID());
                        this.setFullName(vFC.getFullName());

                        // set ID và name cho messageSender và client_Listener
                        messageSender.setClientID(clientID);
                        messageSender.setClientName(clientName); // không cần
                        messageSender.setFullName(fullName);
                        System.out.println("\t + Cập nhật thông tin cho messageSender: " + messageSender.getClientName() + "(" + messageSender.getClientID() + ") - " + messageSender.getFullName());

                        client_Listener.setClientID(clientID);  // không cần
                        client_Listener.setClientName(clientName);  // không cần
                        client_Listener.setFullName(fullName);  // không cần

                        System.out.println("ChatClient: " + client_Listener.getClientName() + "|" + client_Listener.getClientID());

                        vFC.updateUserInfo(clientID, clientName, fullName, password, port + "");
                        System.out.println("Cập nhật thông tin: " + clientID + "|" + clientName + "|" + port);
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
                while (true) {
                    while (vFU.connect == false) {  // nếu server báo thông tin đăng nhập sai thì lặp lại và đợi sửa thông tin
                        Thread.sleep(1000); // Đợi một chút
                        System.out.println("Đợi nhập lại tên đăng nhập!!");
                    }
                    this.setClientName(vFC.getUserName());
                    this.setFullName(vFC.getFullName());
                    this.setClientID(vFC.getUserID());
                    password = vFC.password;

                    // gửi thông tin đăng kí về server
                    messageSender.sendInfo("InfoNewCreateClients#" + clientName + "#" + fullName + "#" + clientID + "#" + password + "\n");

                    while (client_Listener.successLogIn == -1) {
                        Thread.sleep(100); // Đợi một chút
                        System.out.println("Đợi kiểm tra!!");
                    }

                    if (client_Listener.successLogIn == 1) {

                        messageSender.setClientID(clientID);
                        messageSender.setClientName(clientName);

                        System.out.println("Đã gửi thông tin đăng kí về server: " + clientID + "#" + clientName + "#" + password);
                        System.out.println("Đăng kí thành công");
                        System.out.println("Connected to server");
                        System.out.println("Name(ID) của bạn: " + clientName + "(" + clientID + ")");
                        connect = true; //Kiểm tra kết nối thành công thì không hiện panel thông báo
                        // Hiển thị thông báo đăng ký thành công
                        JOptionPane.showMessageDialog(vFU, "Đăng ký thành công! \nVào sử dụng thoi", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        vFU.dispose();          // Đóng form V_FrmUserAccess
                        vFC.setVisible(true);   // Hiển thị form V_FrmChat_Client
                        System.out.println("Đã đóng form đăng nhập và hiện thị form ứng dụng");
                        break;
                    } else if (client_Listener.successLogIn == 0) {
                        System.out.println("Hiển thị dialog thông báo");
                        JOptionPane.showMessageDialog(vFU, "Tên đăng nhập trùng với người khác. Vui lòng nhập lại !!!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        vFU.connect = false;
                        client_Listener.successLogIn = -1;
                    }
                }
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

    public synchronized void createGroup(String groupName, String quantityInGroup, List<String> clientsInGroup, String CreareOrUpdate) {
        if (CreareOrUpdate.equals("create")) {
            try {
                listGroups.put(groupName, clientsInGroup);
                System.out.println("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên: ");
                for (String clients : clientsInGroup) {
                    System.out.println(clients);
                }
                vFC.addGroupToList(groupName, quantityInGroup);
                System.out.println("Danh sách nhóm có bạn là thành viên: " + listGroups);

                LocalDateTime now = LocalDateTime.now();
                Timestamp time = Timestamp.valueOf(now); // lấy thời gian hiện tại
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ~ HH:mm:ss"); // định dạng thời gian
                String timeOfMes = formatter.format(time);
                vFC.addMessage("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên", "said", 1, new Color(120, 120, 255), timeOfMes);
            } catch (Exception e) {
                System.out.println("Lỗi tạo nhóm: ");
                e.printStackTrace();
            }
        } else if (CreareOrUpdate.equals("update")) {
            try {
                listGroups.put(groupName, clientsInGroup);
                System.out.println("\n🔔 Bạn vừa được thêm vào nhóm '" + groupName + "', với " + quantityInGroup + " thành viên: ");
                for (String clients : clientsInGroup) {
                    System.out.println(clients);
                }
                vFC.addGroupToList(groupName, quantityInGroup);
                System.out.println("Danh sách nhóm có bạn là thành viên: " + listGroups);

            } catch (Exception e) {
                System.out.println("Lỗi tạo nhóm: ");
                e.printStackTrace();
            }
        }
    }

    public List<String> getClientsInGroup(String groupName) {
        if (!listGroups.containsKey(groupName)) {
            System.out.println("Nhóm '" + groupName + "' không tồn tại trong HashMap");
        }
        return listGroups.getOrDefault(groupName, Collections.emptyList());
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientID() {
        return clientID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

}
