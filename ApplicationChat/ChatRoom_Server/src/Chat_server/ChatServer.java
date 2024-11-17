package Chat_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import View.V_FrmChat_Server;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ChatServer {

	private static final int PORT_INFO = 5000;
	
	private static ChatServer instance;
	private V_FrmChat_Server vFC;
	private ServerSocket serverInfo_Socket;
	private Socket clientInfo_Socket;
	
	// List chứa các client kết nối
	private List<ClientInfo_Handler> clientsInfo = new ArrayList<>();
	// Tạo hashMap với tên nhóm làm khóa và danh sách các client
	public Map<String, List<String>> groups = new HashMap<>();

	public ChatServer(V_FrmChat_Server vFC) {
		this.vFC = vFC;
	}
	
	// Lấy instance duy nhất
    public static synchronized ChatServer getInstance(V_FrmChat_Server vFC) {
        if (instance == null) {
            instance = new ChatServer(vFC);
        }
        return instance;
    }

	public void startServer() {
		try {
			// webSocket
			serverInfo_Socket = new ServerSocket(PORT_INFO); // PORT để nhận thông tin các client

			System.out.println("Server started, listening on port: " + PORT_INFO);

			// Client kết nối đến server
			while (true) {
				clientInfo_Socket = serverInfo_Socket.accept();

				ClientInfo_Handler clientInfoHandler = new ClientInfo_Handler(clientInfo_Socket, this);

				// thêm client mới kết nối vào list
				clientsInfo.add(clientInfoHandler);

				// Lấy tên và ID từ clientInfo_Handler
				String clientName = clientInfoHandler.getClientName();
				String clientID = clientInfoHandler.getClientID();

				// thêm client mới kết nối vào Jlist
				vFC.addClient_ToJList(clientInfoHandler.getClientID(), clientInfoHandler.getClientName());

				// Gửi thông tin của tất cả client đã kết nối tới client mới để cập nhật lên
				// Jlist
				for (ClientInfo_Handler existingClient : clientsInfo) {
					if (existingClient != clientInfoHandler) { // Chỉ gửi clients hiện có không gửi lại chính client vừa
																// kết nối
						clientInfoHandler
								.sendInfo(existingClient.getClientName() + "|" + existingClient.getClientID() + "\n");
					}
				}

				// Gửi thông tin client mới cho tất cả các client đã kết nối để cập nhật lên
				// Jlist
				for (ClientInfo_Handler client : clientsInfo) {
					if (client != clientInfoHandler) { // Không gửi lại thông tin cho client vừa kết nối
						client.sendInfo(
								clientInfoHandler.getClientName() + "|" + clientInfoHandler.getClientID() + "\n");
					}
				}

				new Thread(clientInfoHandler).start();

				System.out.println("New client connect: " + clientName + "(" + clientID + ") ~~~ "
						+ clientInfo_Socket.getInetAddress().getHostAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi ở chatServer");
		}
	}

	// Gửi tất cả tin nhắn tới client trong phòng chat
	public void broadcastMessage(String clientID, String clientName, String message) {
		for (ClientInfo_Handler client : clientsInfo) {
			if (client.getClientID() != clientID) { // Client gửi tin nhắn không nhận lại tin nhắn của chính client đó
				client.sendMessage("[" + clientName + "(" + clientID + ")] - " + message + "\n");
			}
		}
	}

	public void removeClient(String clientName) {

		// Sử dụng iterator để duyệt qua danh sách và xóa phần tử an toàn
		Iterator<ClientInfo_Handler> iterator = clientsInfo.iterator();

		while (iterator.hasNext()) {
			ClientInfo_Handler clients = iterator.next();
			if (clients.getClientName().equals(clientName)) { // So sánh tên
				iterator.remove(); // Xóa client khỏi danh sách
				break;
			}
		}
		vFC.removeClientFromList(clientName); // Xóa client khỏi Jlist
		// Gửi thông báo ngắt kết nối cho các client khác
//	    broadcastMessage(client.getClientID(), client.getClientName(), " ĐÃ NGẮT KẾT NỐI.");
		broadcastDisconnect(clientName);
	}

	public void broadcastDisconnect(String clientName) {
		for (ClientInfo_Handler client : clientsInfo) {
			try {
				client.sendDisconnectNotification(clientName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void createGroup(String groupName, String quantityInGroup, List<String> clientsInGroup) {
		
		System.out.println("Kiểm tra tạo nhóm");
		System.out.println("Đang thêm group: " + groupName);
		System.out.println("Danh sách client trong nhóm: " + clientsInGroup);
		
		// Thêm tên group được tạo với danh sách client vào hashMap groups
		groups.put(groupName, clientsInGroup);
		System.out.println("Nhóm mới đã được tạo: " + groupName);
		
		// Thêm thông tin nhóm mới vào JList
		vFC.addGroup_ToJList(groupName, quantityInGroup);
	}

	public List<String> getClientNamesInGroup(String groupName) {
		if (!groups.containsKey(groupName)) {
	        System.out.println("Nhóm không tồn tại trong HashMap: '" + groupName + "'");
	    }
		return groups.getOrDefault(groupName, Collections.emptyList());
	}
	
	
	//	public void createGroup_(String groupName, String quantityInGroup, List<String> clientsInGroup) {
//		// Tạo danh sách client cho nhóm
//		List<ClientInfo_Handler> groupClients = new ArrayList<>();
//
//		// Duyệt qua danh sách tên client trong nhóm
//		for (String clientInfo : clientsInGroup) {
//			ClientInfo_Handler clients = getClientByName(clientInfo); // Tìm client theo tên
//			if (clients != null) {
//				groupClients.add(clients);
//			}
//		}
//
//		// Thêm nhóm mới hoặc cập nhật nếu nhóm đã tồn tại
//		if (!groups.containsKey(groupName)) { // Kiểm tra các groups đã có tên nhóm này chưa
//			groups.put(groupName, clientsInGroup);
//			System.out.println("Nhóm mới đã được tạo: " + groupName);
//
//		}
////		else {
////			groups.get(groupName).addAll(groupClients); // Nếu tên nhóm đã có thì sẽ cập nhật
////			System.out.println("Nhóm đã cập nhật thêm thành viên: " + groupName);
////		}
//		vFC.addGroup_ToJList(groupName, quantityInGroup);
//
//		// Thông báo cho các thành viên trong nhóm rằng nhóm đã được tạo
////	    notifyGroupCreation(groupName, groupClients);
//	}


//	private void notifyGroupCreation(String groupName, List<ClientInfo_Handler> groupClients) {
//		ClientChat_Handler clientChat;
//		
//	    String notification = "Nhóm " + groupName + " đã được tạo. Bạn đã tham gia nhóm.";
//	    for (ClientInfo_Handler clientHandler : groupClients) {
//	    	clientChat.sendMessage(notification);
//	    }
//	}

//	private ClientInfo_Handler getClientByName(String clientName) {
//		for (ClientInfo_Handler clientInfo : clientsInfo) { // Duyệt trong list clientsInfo
//			if (clientInfo.getClientName().equals(clientName)) {
//				return clientInfo;
//			}
//		}
//		return null;
//	}
	

}
