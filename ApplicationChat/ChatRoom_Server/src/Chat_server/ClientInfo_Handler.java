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
public class ClientInfo_Handler implements Runnable {

	private Socket socket;
	private ChatServer chatServer;
	private String clientID;
	private String clientName;

	private InputStream input;
	private OutputStream output;

	byte[] buffer = new byte[2048];

	public ClientInfo_Handler(Socket socket, ChatServer chatServer) {
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
			System.out.println("(ClientInfo_Handler)");
		}
	}

	@Override
	public void run() { // đang xử lý

		try {

			int bytesRead;

			// Liên tục nhận tin nhắn từ client và chuyển tiếp tới các client khác
			while ((bytesRead = input.read(buffer)) != -1) {
				String message = new String(buffer, 0, bytesRead).trim();

				if (message.startsWith("GROUP#")) {
					// Tách lấy tên nhóm và danh sách client
					String[] parts = message.split("\\#");
					String groupName = parts[1].trim();
					String quantityInGroup = parts[2];
					List<String> clientsInGroup = new ArrayList<>(Arrays.asList(parts[3].split(" \\+\\+ ")));
					clientsInGroup.add(this.clientID + " | " + this.clientName); // Thêm thành viên tạo nhóm vào list
																					// nhóm

					// In ra thông tin nhóm mới được tạo
					System.out.println(this.clientName + " vừa tạo nhóm \n" + " Tên nhóm: \"" + groupName + "\", với "
							+ quantityInGroup + " thành viên: ");
					for (String clients : clientsInGroup) {
						System.out.println(clients);
					}

					// Xử lý thêm nhóm mới, thêm client vào nhóm
					chatServer.createGroup(groupName, quantityInGroup, clientsInGroup);
					// Kiểm tra đã có group mới trong danh sách groups ở chatServer chưa
					System.out.println("Groups trong chatServer: "+ chatServer.groups);

				} else { // Gửi tin nhắn nếu không phải thông báo client mới hay tạo group
					chatServer.broadcastMessage(this.clientID, this.clientName, message);
					System.out.println("Tin nhắn từ " + this.clientName + "(" + this.clientID + "): " + message);
				}

			}
		} catch (SocketException e) {
			System.out.println(clientName + "(" + clientID + ") " + "vừa ngắt kết nối");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Xử lý ngắt kết nối và xoá client khỏi danh sách khi ngoại lệ xảy ra
			chatServer.removeClient(this.clientName); // xóa client khỏi danh sách ở Chatserver
			closeConnection();
		}
	}

	public void sendMessage(String message) {
		try {
			output.write(message.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() { // đóng kết nói để giải phóng tài nguyên của client đó
		try {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInfo(String info) {
		try {
			output.write(("InfoNewClients|" + info).getBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendDisconnectNotification(String disconnectedClientName) { // Gửi thông điệp client đã ngắt kết nối để
		try {
			output.write(("DISCONNECT|" + disconnectedClientName + "\n").getBytes());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

}
