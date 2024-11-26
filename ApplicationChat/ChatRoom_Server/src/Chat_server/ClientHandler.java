package Chat_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ClientHandler implements Runnable {

	private Socket socket;
	private ChatServer chatServer;
	private String clientID;
	private String clientName;

	private InputStream input;
	private OutputStream output;

	byte[] buffer = new byte[2048];

	public ClientHandler(Socket socket, ChatServer chatServer) {
		this.socket = socket;
		this.chatServer = chatServer;
		try {
			this.input = socket.getInputStream();
			this.output = socket.getOutputStream();

			// Đọc tên và id từ client
			int bytesRead = input.read(buffer);
			String clientData = new String(buffer, 0, bytesRead).trim();

//			 Tách tên và ID bằng ký tự "|"
			if (clientData.startsWith("InfoNewClients|")) {

				String[] dataParts = clientData.split("\\|");

				if (dataParts.length == 3) {
					this.clientName = dataParts[1];
					this.clientID = dataParts[2];
				} else {
					System.out.println("Dữ liệu không hợp lệ từ client.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("(ClientHandler)");
		}
	}

	@Override
	public void run() { // đang xử lý

		try {

			int bytesRead;

			// Liên tục nhận tin nhắn từ client và chuyển tiếp tới các client khác
			while ((bytesRead = input.read(buffer)) != -1) {
				String message = new String(buffer, 0, bytesRead).trim();

				if (message.startsWith("GROUP#")) { // Thông điệp tạo group của client
					// Tách lấy tên nhóm và danh sách client
					String[] parts = message.split("\\#");
					String groupName = parts[1].trim();
					String quantityInGroup = parts[2];
					List<String> clientsInGroup = new ArrayList<>(Arrays.asList(parts[3].split(" \\+\\+ ")));

					// In ra thông tin nhóm mới được tạo
					System.out.println("\n ** '"+this.infoClient() + "' vừa tạo nhóm \n" + "Tên nhóm: \"" + groupName + "\", với "
							+ quantityInGroup + " thành viên: ");
					for (String clients : clientsInGroup) {
						System.out.println(clients);
					}

					// Xử lý thêm nhóm mới, thêm client vào nhóm
					chatServer.createGroup(groupName, quantityInGroup, clientsInGroup);
					// Kiểm tra đã có group mới trong danh sách groups ở chatServer chưa
					System.out.println("\nGroups trong chatServer: " + chatServer.groups);

				}

				else if (message.startsWith("DISCONNECT#")) { // Client vừa disconnect gửi thông báo
					String[] parts = message.split("#");
					if (parts.length == 2) {
						String[] infoClientDisconnect = parts[1].split("\\|");
						clientID = infoClientDisconnect[0].trim();
						clientName = infoClientDisconnect[1].trim();
						System.out.println(clientName + "(" + clientID + ") đã tự ngắt kết nối.");
						handleClientDisconnect();
						break;
					}
				} else if (message.startsWith("MessageOfClient#")) {  // Tin nhắn của client
					String[] parts = message.split("#");
					String clientSend = parts[1];
					String clientReceive = parts[2];
					String messageSendTo = parts[3];
					System.out.println("\n * '"+clientSend + "' gửi 1 tin nhắn đến '" + clientReceive + "': " + parts[3]);

					ClientHandler clientHandler_Re = chatServer.getClientByInfo(clientReceive);

					if (clientHandler_Re == null) {
						System.out.println("Không tìm thấy client cần gửi tin nhắn: ");
					} else {
						chatServer.broadcastMessage(this, clientHandler_Re, messageSendTo);
					}
				} 

//				else { // Gửi tin nhắn nếu không phải thông báo client mới hay tạo group
//					chatServer.broadcastMessage(this.clientID, this.clientName, message);
//					System.out.println(
//							"Tin nhắn từ " + this.clientName + "(" + this.clientID + ") gửi đến tất cả: " + message);
//				}

			}
		} catch (SocketException e) {
			System.out.println(clientID + "|" + clientName +"vừa ngắt kết nối");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		try {
			output.write((message+"\n").getBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleClientDisconnect() {
		try {
			// Xóa client khỏi danh sách server
			chatServer.removeClient(this);
			chatServer.broadcastDisconnect(this.infoClient());
			// Đóng socket và streams
			if (socket != null)
				socket.close();
			if (input != null)
				input.close();
			if (output != null)
				output.close();

			System.out.println("Đã xóa client " + clientID + "|" + clientName + " khỏi server.");
		} catch (IOException e) {
			System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
		}
	}

	
//	public void closeConnection() { // đóng kết nói để giải phóng tài nguyên của client đó
//		try {
//			if (input != null)
//				input.close();
//			if (output != null)
//				output.close();
//			if (socket != null)
//				socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	public void sendInfo(String info) {
//		try {
//			output.write(("InfoNewClients|" + info).getBytes());
//			output.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public void sendDisconnectNotification(String disconnectedClientName) { // Gửi thông điệp client đã ngắt kết nối để
//		try {
//			output.write(("DISCONNECT|" + disconnectedClientName + "\n").getBytes());
//			output.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String infoClient() {
		return this.clientID + "|" + this.clientName;
	}

}
