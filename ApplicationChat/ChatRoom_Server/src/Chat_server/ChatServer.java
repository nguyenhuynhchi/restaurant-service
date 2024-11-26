package Chat_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<ClientHandler> listClientHandler = new ArrayList<>();
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

			System.out.println("\nServer đã được khởi động, lắng nghe ở cổng: " + PORT_INFO);

			// Client kết nối đến server
			while (true) {
				clientInfo_Socket = serverInfo_Socket.accept();

				ClientHandler clientHandler = new ClientHandler(clientInfo_Socket, this);

				// thêm client mới kết nối vào list
				listClientHandler.add(clientHandler);

				// Lấy tên và ID từ clientInfo_Handler
				String clientName = clientHandler.getClientName();
				String clientID = clientHandler.getClientID();

				// thêm client mới kết nối vào Jlist
//				vFC.addClient_ToJList(clientHandler.getClientID(), clientHandler.getClientName());
				vFC.addClient_ToJList(clientHandler.infoClient().trim());
				// Gửi thông tin của tất cả client đã kết nối tới client mới để cập nhật lên
				// Jlist
				for (ClientHandler existingClient : listClientHandler) {
					if (existingClient != clientHandler) { // Chỉ gửi clients hiện có không gửi lại chính client vừa kết nối
						clientHandler.sendMessage("InfoClients#" + existingClient.getClientID() + "|" + existingClient.getClientName());
					}
				}

				// Gửi thông tin client mới cho tất cả các client đã kết nối để cập nhật lên Jlist
				for (ClientHandler client : listClientHandler) {
					if (client != clientHandler) { // Không gửi lại thông tin cho client vừa kết nối
						client.sendMessage("InfoClients#" + clientHandler.getClientID() + "|" + clientHandler.getClientName());
					}
				}

				new Thread(clientHandler).start();

				System.out.println("-Client mới kết nối: " + clientName + "(" + clientID + ") ~~~ "
						+ clientInfo_Socket.getInetAddress().getHostAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi ở chatServer");
		}
	}

	// Gửi tất cả tin nhắn tới client trong phòng chat
	public void broadcastMessage(ClientHandler clientSend, ClientHandler clientReceive, String message) {
		clientReceive.sendMessage("[" + clientSend.getClientName() + "(" + clientSend.getClientID() + ")] - " + message);
		System.out.println("*ĐÃ GỬI* ! [" + clientSend.getClientName() + "(" + clientSend.getClientID() + ")] - " + message);
	}

	public void removeClient(ClientHandler clientDisconnect) {
		listClientHandler.remove(clientDisconnect);
		vFC.removeClientFromList(clientDisconnect.infoClient().trim()); // Xóa client khỏi Jlist
	}

	// Gửi thông điệp ngắt kết nối đến client khác để xóa client ngắt kết nối khỏi
	// JList
	public void broadcastDisconnect(String infoClientDisconnect) {
		for (ClientHandler client : listClientHandler) {
			try {
				client.sendMessage("DISCONNECT#" + infoClientDisconnect);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void createGroup(String groupName, String quantityInGroup, List<String> clientsInGroup) {
		System.out.println("Kiểm tra tạo nhóm");
		System.out.println("Đang thêm nhóm mới tạo vào hashmap groups: " + groupName);
		System.out.println("Danh sách client trong nhóm: " + clientsInGroup);

		// Thêm tên group được tạo với danh sách client vào hashMap groups
		groups.put(groupName, clientsInGroup);
		System.out.println("Nhóm mới đã được tạo: " + groupName);

		// Thêm thông tin nhóm mới vào JList
		vFC.addGroup_ToJList(groupName, quantityInGroup);
		notifyGroupCreation(groupName, quantityInGroup, clientsInGroup);

//		List<ClientHandler> groupClients = new ArrayList<>();
//		for (String clientsGroup : clientsInGroup) {
//			for (ClientHandler client : listClientHandler) {
//				if (getClientByInfo(clientsGroup) == client) {
//					groupClients.add(client);
//				}
//			}
//		}

	}

	public List<String> getClientsInGroup(String groupName) {
		if (!groups.containsKey(groupName)) {
			System.out.println("Nhóm không tồn tại trong HashMap: '" + groupName + "'");
		}
		return groups.getOrDefault(groupName, Collections.emptyList());
	}

	public ClientHandler getClientByInfo(String infoClient) {
		String[] parts = infoClient.split("\\|");

		for (ClientHandler client : listClientHandler) { // Duyệt trong listClientHandler
			if (client.getClientID().equals(parts[0].trim()) && client.getClientName().equals(parts[1].trim())) {
				System.out.println("\t" + client.infoClient());
				return client;
			}
		}
		System.out.println("Không có client "+infoClient);
		return null;
	}

	private void notifyGroupCreation(String groupName, String quantityInGroup, List<String> groupClients) {
		String notification = "AddedToGroup#" + groupName + "#"+quantityInGroup+"#" + String.join(" ++ ", getClientsInGroup(groupName));
		System.out.println(notification);
		System.out.println("Các client trong group '" + groupName + "': ");
		for (String client : groupClients) {
			getClientByInfo(client).sendMessage(notification);
		}
	}

//	    String notification = "AddedToGroup#" + groupName + "#"+getClientsInGroup(groupName);
//	    for (ClientHandler clientHandler : groupClients) {
//	    	clientHandler.sendMessage(notification);
//	    }

//	private void notifyGroupCreation(String groupName, List<ClientHandler> groupClients) {
//		for (ClientHandler client : groupClients) {
//			System.out.print(client.infoClient() + ", ");
//		}

//	    String notification = "AddedToGroup#" + groupName + "#"+getClientsInGroup(groupName);
//	    for (ClientHandler clientHandler : groupClients) {
//	    	clientHandler.sendMessage(notification);
//	    }
//	}
	// public void createGroup_(String groupName, String quantityInGroup,
	// List<String> clientsInGroup) {
//		// Tạo danh sách client cho nhóm
//		List<ClientHandler> groupClients = new ArrayList<>();
//
//		// Duyệt qua danh sách tên client trong nhóm
//		for (String clientInfo : clientsInGroup) {
//			ClientHandler clients = getClientByName(clientInfo); // Tìm client theo tên
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

//	private ClientHandler getClientByName(String clientName) {
//		for (ClientHandler clientInfo : listClientHandler) { // Duyệt trong list listClientHandler
//			if (clientInfo.getClientName().equals(clientName)) {
//				return clientInfo;
//			}
//		}
//		return null;
//	}

}
