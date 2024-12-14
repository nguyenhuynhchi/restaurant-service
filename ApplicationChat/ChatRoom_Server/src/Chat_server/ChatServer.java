package Chat_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import View.V_FrmChat_Server;
import dataAccessObject.DAO_GROUPMEMBERS;
import dataAccessObject.DAO_GROUPS;
import dataAccessObject.DAO_MES;
import dataAccessObject.DAO_USERS;
import model.GROUPMEMBERS_model;
import model.GROUPS_model;
import model.MES_model;
import model.USERS_model;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ChatServer {

	private int port;

	private static ChatServer instance;
	private V_FrmChat_Server vFC;
	private ServerSocket serverInfo_Socket;
	private Socket clientInfo_Socket;

	// List chứa các client kết nối
	public List<ClientHandler> listClientHandler = new ArrayList<>();
	// Tạo hashMap với tên nhóm làm khóa và danh sách các client
	public Map<String, List<String>> listGroups = new HashMap<>();

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
			port = vFC.port;

			// webSocket
			serverInfo_Socket = new ServerSocket(port); // PORT để nhận thông tin các client

			System.out.println("\nServer đã được khởi động, lắng nghe ở cổng: " + port + "\n");

			// Client kết nối đến server
			while (true) {
				clientInfo_Socket = serverInfo_Socket.accept();

				ClientHandler clientHandler = new ClientHandler(clientInfo_Socket, this);
				// Tạo và khởi chạy thread ClientHandler để tiếp nhận các thông điệp của client
				// rồi xử lý
				new Thread(clientHandler).start();

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi ở chatServer");
		}
	}

	public void updateQuantityConnect() {
		vFC.soLuongConnect--;
		vFC.lbl_soLuongClient.setText("Số người kết nối: " + vFC.soLuongConnect);
	}

	// Kiểm tra trong bảng dữ liệu users có tài khoản với ID này không và password
	// có đúng không
	public String checkClientIDLogIn(String userName, String password) {
		String checkCondition = userName + "#" + password;
		String result = DAO_USERS.getInstance().findByCondition(checkCondition);
		if (result != null) {
			System.out.println(" ! Tên: " + userName + "\n ! Password: " + password);
			System.out.println("Có '" + userName + "' trong bảng. Với ID là: " + result);
			return result;
		} else {
			System.out.println("Không có '" + userName + "' trong bảng");
			return null;
		}
	}

	public void logInSuccess(ClientHandler clientHandler, String clientID) {
		clientHandler.setClientID(clientID); // Gán ID của client đăng nhập (khi được tìm kiếm bằng tên và password

		// Tạo và khởi chạy thread ClientHandler để tiếp nhận các thông điệp của client
		// rồi xử lý
//		new Thread(clientHandler).start();

		// thêm client mới kết nối vào list
		listClientHandler.add(clientHandler);

		// thêm client mới kết nối vào Jlist
		vFC.addClient_ToJList(clientHandler.infoClient().trim());

		// Gửi thông điệp cho biết đăng nhập thành công
		String logInSuccess = "SUCCESS#" + clientID;
		clientHandler.sendMessage(logInSuccess);

		System.out.println("-Client mới kết nối: " + clientHandler.getClientName() + "(" + clientHandler.getClientID()
				+ ") ~~~ " + clientInfo_Socket.getInetAddress().getHostAddress() + "\n");

		sendInfo(clientHandler); // Gửi thông tin cho các client để cập nhật JList

		// Tăng số lượng client kết nối để hiển thị
		vFC.soLuongConnect++;
		vFC.lbl_soLuongClient.setText("Số người kết nối: " + vFC.soLuongConnect);
	}

	public void logInUnsuccess(ClientHandler clientHandler) {
		// Gửi thông điệp đăng nhập không thành công về client
//		new Thread(clientHandler).start(); // Xem xét có nên để không
		clientHandler.sendMessage("UNSUCCESS");
		System.out.println("Client " + clientHandler.getClientName() + " đăng nhập không thành công");
	}

	// Nếu là client mới đăng kí (mới tạo tài khoản) thì thêm thông tin vào bảng
	// users
	public void addNewCreateClient(ClientHandler clientHandler) {
		LocalDateTime now = LocalDateTime.now();
		Timestamp createTime = Timestamp.valueOf(now); // Lấy thời gian hiện tại tức lúc vừa tạo

		// Thêm thông tin client vào csdl
		USERS_model user = new USERS_model(clientHandler.getClientID(), clientHandler.getClientName(),
				clientHandler.getPassword(), createTime);
		int result = DAO_USERS.getInstance().insert(user);

		if (result == 1) {
			System.out.println("Thêm tài khoản client mới đăng kí thành công");
		}else {
			System.out.println("Thêm tài khoản client mới đăng kí không thành công");
		}

		// thêm client mới kết nối vào list
		listClientHandler.add(clientHandler);

		// thêm client mới kết nối vào Jlist
		vFC.addClient_ToJList(clientHandler.infoClient().trim());

		sendInfo(clientHandler); // Gửi thông tin cho các client để cập nhật JList

		System.out.println("-Client mới kết nối: " + clientHandler.getClientName() + "(" + clientHandler.getClientID()
				+ ") ~~~ " + clientInfo_Socket.getInetAddress().getHostAddress() + "\n");

		// Tăng số lượng client kết nối để hiển thị
		vFC.soLuongConnect++;
		vFC.lbl_soLuongClient.setText("Số người kết nối: " + vFC.soLuongConnect);
	}

	// Gửi tin nhắn từ client tới client
	public void broadcastMessage(ClientHandler clientSend, ClientHandler clientReceive, String message) {
		clientReceive
				.sendMessage("[" + clientSend.getClientName() + "(" + clientSend.getClientID() + ")] - " + message);
		System.out.println("*ĐÃ GỬI* ! [" + clientSend.getClientName() + "(" + clientSend.getClientID() + ")] - " + message);
		
		// Thêm thông tin tin nhắn vào bảng mes
		String senderID = clientSend.getClientID();
		String receiverID = clientReceive.getClientID();
		
		LocalDateTime now = LocalDateTime.now();
		Timestamp timeReceive = Timestamp.valueOf(now); // lấy thời gian hiện tại
		
		MES_model mes = new MES_model(senderID, receiverID, null, message, timeReceive);
		int result = DAO_MES.getInstance().insert(mes);
	}
	
	// Gửi tin nhắn từ client trong group
	public void broadcastMessageToGroup(ClientHandler clientSend, String groupName, String message) {
		List<String> clientsInGroup = getClientsInGroup(groupName);
		System.out.println("client gửi tin: " + clientSend.infoClient());
		for (String client : clientsInGroup) {
			System.out.println("Gửi tới client: " + client);
			if (!client.trim().equals(clientSend.infoClient().trim())) {
				// [ten nhom {tenclient(123)}] - mes
				getClientByInfo(client).sendMessage("[" + groupName + " {" + clientSend.getClientName() + "("+ clientSend.getClientID() + ")}] - " + message);
			}
		}
		
		// Thêm thông tin tin nhắn vào bảng mes
		String senderID = clientSend.getClientID();
		
		String condition = groupName;
		String groupID = DAO_GROUPS.getInstance().findByCondition(condition); // Lấy ID của group
		
		LocalDateTime now = LocalDateTime.now();
		Timestamp timeReceive = Timestamp.valueOf(now); // lấy thời gian hiện tại
		
		MES_model mes = new MES_model(senderID, null, groupID, message, timeReceive);
		int result = DAO_MES.getInstance().insert(mes);
		if(result == 1) {
			System.out.println("Thêm thông tin tin nhắn vào bảng mes thành công");
		}else {
			System.out.println("Thêm thông tin tin nhắn vào bảng mes không thành công");
		}
	}

	public void removeClient(ClientHandler clientDisconnect) {
		listClientHandler.remove(clientDisconnect);
		vFC.removeClientFromList(clientDisconnect.infoClient().trim()); // Xóa client khỏi Jlist
	}

	// Gửi thông điệp ngắt kết nối đến client khác để xóa client ngắt kết nối khỏi
	// huihihi JList
	public void broadcastDisconnect(String infoClientDisconnect) {
		for (ClientHandler client : listClientHandler) {
			try {
				client.sendMessage("DISCONNECT#" + infoClientDisconnect);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

	public void sendInfo(ClientHandler clientHandler) {
		// Gửi thông tin của tất cả client đã kết nối tới client mới để cập nhật lên
		// Jlist
		for (ClientHandler existingClient : listClientHandler) {
			if (existingClient != clientHandler) { // Chỉ gửi clients hiện có không gửi lại chính client vừa kết nối
				clientHandler.sendMessage(
						"InfoClients#" + existingClient.getClientID() + "|" + existingClient.getClientName());
			}
		}

		// Gửi thông tin client mới cho tất cả các client đã kết nối để cập nhật lên
		// Jlist
		for (ClientHandler client : listClientHandler) {
			if (client != clientHandler) { // Không gửi lại thông tin cho client vừa kết nối
				client.sendMessage("InfoClients#" + clientHandler.getClientID() + "|" + clientHandler.getClientName());
			}
		}
	}

	public synchronized void createGroup(String groupName, int quantityInGroup, List<String> clientsInGroup,
			String CreateByClientID) {
		System.out.println("Kiểm tra tạo nhóm");
		System.out.println("Đang thêm nhóm mới tạo vào hashmap groups: " + groupName);
		System.out.println("Danh sách client trong nhóm: " + clientsInGroup);

		// Thêm tên group được tạo với danh sách client vào hashMap groups
		listGroups.put(groupName, clientsInGroup);
		System.out.println("Nhóm mới đã được tạo: " + groupName);

//		String groupID = "0" + (vFC.list_GroupName.getLastVisibleIndex() + 1) + ""; // Tạo ID cho nhóm: 01; 02
//		System.out.println("ID nhóm: " + groupID);
//		String groupID = DAO_GROUPS.getInstance().getIDGroup();  // Lấy ID group trước đó
//		groupID = (Integer.parseInt(groupID)+1)+"";
//		System.out.println("ID của group vừa tạo: "+groupID);

		// Thêm thông tin group vào csdl
		LocalDateTime now = LocalDateTime.now();
		Timestamp createTime = Timestamp.valueOf(now); // lấy thời gian hiện tại

		GROUPS_model group = new GROUPS_model(groupName, quantityInGroup, CreateByClientID, createTime);
		int result = DAO_GROUPS.getInstance().insert(group);
		if (result == 1) {
			System.out.println("Thêm thông tin vào bảng groups thành công");
		}else {
			System.out.println("Thêm thông tin vào bảng groups không thành công");
		}

		// Thêm thông tin nhóm mới vào JList
		vFC.addGroup_ToJList(groupName, quantityInGroup);
		notifyGroupCreation(groupName, quantityInGroup, clientsInGroup);
	}

	public List<String> getClientsInGroup(String groupName) {
		if (!listGroups.containsKey(groupName)) {
			System.out.println("Nhóm không tồn tại trong HashMap: '" + groupName + "'");
		}
		return listGroups.getOrDefault(groupName, Collections.emptyList());
	}

	public ClientHandler getClientByInfo(String infoClient) {
		String[] parts = infoClient.split("\\|");

		for (ClientHandler client : listClientHandler) { // Duyệt trong listClientHandler
			if (client.getClientID().equals(parts[0].trim()) && client.getClientName().equals(parts[1].trim())) {
				
				return client;
			}
		}
		System.out.println("Không có client " + infoClient);
		return null;
	}

	private void notifyGroupCreation(String groupName, int quantityInGroup, List<String> groupClients) {
		String notification = "AddedToGroup#" + groupName + "#" + quantityInGroup + "#"
				+ String.join(" ++ ", getClientsInGroup(groupName));
		System.out.println(notification);
		System.out.println("Các client trong group '" + groupName + "': ");

		String condition = groupName;
		String groupID = DAO_GROUPS.getInstance().findByCondition(condition); // Lấy ID của group
		int memberID = 0;
		for (String client : groupClients) {
			//Gửi thông điệp tạo nhóm đến các client trong nhóm được tạo
			getClientByInfo(client).sendMessage(notification);
			System.out.println("\tĐã gửi -> " + getClientByInfo(client).infoClient());
			
			// Thêm các thành viên của nhóm vào csdl
			String userID = getClientByInfo(client).getClientID();
			memberID++;
			String groupMemberID = null;
			if (memberID < 10) {
				groupMemberID = groupID + "0" + memberID;
			} else if (memberID >= 10) {
				groupMemberID = groupID + memberID;
			}

			GROUPMEMBERS_model member = new GROUPMEMBERS_model(groupMemberID, groupID, userID);
			int result = DAO_GROUPMEMBERS.getInstance().insert(member);
			if(result ==1) {
				System.out.println("Thêm thông tin vào bảng GROUPMEMBERS thành công");
			}else {
				System.out.println("Thêm thông tin vào bảng GROUPMEMBERS không thành công");
			}
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
