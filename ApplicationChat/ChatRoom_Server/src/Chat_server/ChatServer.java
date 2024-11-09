package Chat_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import View.V_FrmChat_Server;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ChatServer {

	private static final int PORT_INFO = 5000;
	private static final int PORT_CHAT = 5001;
	private V_FrmChat_Server vFC;
	// List chứa các client kết nối
	private List<ClientInfo_Handler> clientsInfo = new ArrayList<>();
	private List<ClientChat_Handler> clientsChat = new ArrayList<>();
	
	// Tạo hashMap với tên nhóm làm khóa và danh sách các client
	private Map<String, List<ClientInfo_Handler>> groups = new HashMap<>();

	

	private ServerSocket serverInfo_Socket;
	private ServerSocket serverChat_Socket;

	private Socket clientInfo_Socket;
	private Socket clientChat_Socket;

	public ChatServer(V_FrmChat_Server vFC) {
		this.vFC = vFC;
	}

	public void startServer() {
		try {
			// webSocket
			serverInfo_Socket = new ServerSocket(PORT_INFO); //PORT để nhận thông tin các client
			serverChat_Socket = new ServerSocket(PORT_CHAT); //PORT để nhận tin nhắn từ các client

			System.out.println("Server started, listening on port: " + PORT_INFO + " and " + PORT_CHAT);

			// Client kết nối đến server
			while (true) {
				clientInfo_Socket = serverInfo_Socket.accept();
				clientChat_Socket = serverChat_Socket.accept();


				ClientInfo_Handler clientInfoHandler = new ClientInfo_Handler(clientInfo_Socket, this);
				ClientChat_Handler clientChatHandler = new ClientChat_Handler(clientChat_Socket, this);

				// Lấy tên và ID từ clientInfo_Handler
				String clientName = clientInfoHandler.getClientName();
				String clientID = clientInfoHandler.getClientID();

				// Gán tên và ID cho clientChatHandler
				clientChatHandler.setClientName(clientName);
				clientChatHandler.setClientID(clientID);

				// thêm 1 client vào list
				clientsInfo.add(clientInfoHandler);
				clientsChat.add(clientChatHandler);

				// Cập nhật khi có client mới kết nối vào Jlist của viewServer
				vFC.addClientToList(clientInfoHandler.getClientID(), clientInfoHandler.getClientName());

				// Gửi thông tin của tất cả client đã kết nối tới client mới để cập nhật lên Jlist
				for (ClientInfo_Handler existingClient : clientsInfo) {
					if (existingClient != clientInfoHandler) {
						clientInfoHandler.sendInfo(existingClient.getClientName() + "|" + existingClient.getClientID() + "\n");
					}
				}

				// Gửi thông tin client mới cho tất cả các client đã kết nối để cập nhật lên Jlist
				for (ClientInfo_Handler client : clientsInfo) {
					if (client != clientInfoHandler) { // Không gửi lại thông tin cho client vừa kết nối
						client.sendInfo(clientInfoHandler.getClientName() + "|" + clientInfoHandler.getClientID() + "\n");
					}
				}

				new Thread(clientInfoHandler).start();
				new Thread(clientChatHandler).start();

				System.out.println("New client connect: "+clientName+"(" + clientID + ") ~~~ " + clientInfo_Socket.getInetAddress().getHostAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi ở chatServer");
		}
	}

	// Gửi tất cả tin nhắn tới client trong phòng chat
	public void broadcastMessage(String clientID, String clientName, String message) {
		for (ClientChat_Handler client : clientsChat) {
			if (client.getClientID() != clientID) { // Client gửi tin nhắn không nhận lại tin nhắn của chính client đó
				client.sendMessage("["+clientName + "(" + clientID + ")] - " + message + "\n");
			}
		}
	}
	
	public void removeClient(ClientChat_Handler client, String clientName) {
		
		// Sử dụng iterator để duyệt qua danh sách và xóa phần tử an toàn
	    Iterator<ClientInfo_Handler> iterator = clientsInfo.iterator();

	    while (iterator.hasNext()) {
	        ClientInfo_Handler clients = iterator.next();
	        if (clients.getClientName().equals(clientName)) { // So sánh tên
	            iterator.remove(); // Xóa client khỏi danh sách
	            break;
	        }
	    }
	    clientsChat.remove(client);
	    vFC.removeClientFromList(clientName); // Xóa client khỏi Jlist 
	    // Gửi thông báo ngắt kết nối cho các client khác 
	    broadcastMessage(client.getClientID(), client.getClientName(), " ĐÃ NGẮT KẾT NỐI.");
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
	
	public void createGroup(String groupName, List<String> clientsInGroup) {
	    // Tạo danh sách client cho nhóm
	    List<ClientInfo_Handler> groupClients = new ArrayList<>();

	    // Duyệt qua danh sách tên client trong nhóm
	    for (String clientName : clientsInGroup) {
	        ClientInfo_Handler clients= getClientByName(clientName); // Tìm client theo tên
	        if (clients != null) {
	            groupClients.add(clients);
	        }
	    }

	    // Thêm nhóm mới hoặc cập nhật nếu nhóm đã tồn tại
	    if (!groups.containsKey(groupName)) {  // Kiểm tra groups đã có tên nhóm này chưa
	        groups.put(groupName, groupClients);
	        System.out.println("Nhóm mới đã được tạo: " + groupName);
	    } else {
	        groups.get(groupName).addAll(groupClients);
	        System.out.println("Nhóm đã cập nhật thêm thành viên: " + groupName);
	    }

	    // Thông báo cho các thành viên trong nhóm rằng nhóm đã được tạo
//	    notifyGroupCreation(groupName, groupClients);
	}
	
//	private void notifyGroupCreation(String groupName, List<ClientInfo_Handler> groupClients) {
//		ClientChat_Handler clientChat;
//		
//	    String notification = "Nhóm " + groupName + " đã được tạo. Bạn đã tham gia nhóm.";
//	    for (ClientInfo_Handler clientHandler : groupClients) {
//	    	clientChat.sendMessage(notification);
//	    }
//	}
	
	private ClientInfo_Handler getClientByName(String clientName) {
	    for (ClientInfo_Handler clientInfo : clientsInfo) { // Duyệt trong list clientsInfo
	        if (clientInfo.getClientName().equals(clientName)) {
	            return clientInfo;
	        }
	    }
	    return null;
	}
	
	
	

}
