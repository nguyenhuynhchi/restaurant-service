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
	private String fullName;
	private String password;
	public boolean newCreate = false;
	private InputStream input;
	private OutputStream output;

	public ClientHandler(Socket socket, ChatServer chatServer) {
		this.socket = socket;
		this.chatServer = chatServer;
		this.clientName = "Chưa biết";
		this.clientID = "000";
		try {
			this.input = socket.getInputStream();
			this.output = socket.getOutputStream();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi ở ClientHandler");
		}
	}

	@Override
	public void run() { // đang xử lý

		try {

			int bytesRead;
			byte[] buffer = new byte[524288];
			// Liên tục nhận tin nhắn từ client và chuyển tiếp tới các client khác
			while ((bytesRead = input.read(buffer)) != -1) {
				String message = new String(buffer, 0, bytesRead).trim();

				if (message.startsWith("InfoClientLogIn|")) { // Client đăng nhập
					String[] dataParts = message.split("\\|");
				
					if (dataParts.length == 3) {
						this.setClientName(dataParts[1]);
						this.setPassword(dataParts[2]);
					} else {
						System.out.println("Dữ liệu không hợp lệ từ client.");
					}
					
					System.out.println("\n~~~  ~~~\n + Tên người dùng: " + this.getClientName() + "\n + Mật khẩu: " + this.password);
					newCreate = false;

					System.out.println("Client này ĐĂNG NHẬP, không thêm vào csdl, kiểm tra thông tin đăng nhập");
					// Kiểm tra có thông tin đăng nhập của client bằng tên và password
					String resultID = chatServer.checkClientIDLogIn(this.getClientName(), this.getPassword());
					
					if (resultID != null) {
						chatServer.logInSuccess(this, resultID);
						Thread.sleep(500);
						chatServer.getMessageOfClient(this); // Cập nhật tin nhắn trước đó
						chatServer.updateGroupForClient(this);
					} else if (resultID == null) {
						sendMessage("UNSUCCESS");
						System.out.println("Client " + this.getClientName() + " đăng nhập không thành công");
					}
				}

				else if (message.startsWith("InfoNewCreateClients")) { // Client đăng kí
					String[] parts = message.split("\\#");

					if (parts.length == 5) {
						this.setClientName(parts[1]);
						this.setFullName(parts[2]);
						this.setClientID(parts[3]);
						this.setPassword(parts[4]);
					} else {
						System.out.println("Dữ liệu không hợp lệ từ client.");
					}

					System.out.println(" - Tên người dùng: " + clientName + "\n - Họ tên: " + fullName + "\n - ID: "
							+ clientID + "\n - Pass: " + password);
					
					
					if(chatServer.checkClientCreate(clientName)) { // Không trùng
						System.out.println("Client này ĐĂNG KÍ, thêm thông tin vào csdl");
						chatServer.addNewCreateClient(this); // Tên user đăng kí không trùng thì thêm vào csdl					
					}else { // trùng
						sendMessage("CREATEUNSUCCESS");
						System.out.println("Client " + this.getClientName() + " đăng kí không thành công");
					}
					
				}

				else if (message.startsWith("CREATEGROUP#")) { // Thông điệp tạo group của client
					// Tách lấy tên nhóm và danh sách client
					String[] parts = message.split("\\#");
					String groupName = parts[1].trim();
					int quantityInGroup = Integer.parseInt(parts[2]);
					List<String> clientsInGroup = new ArrayList<>(Arrays.asList(parts[3].split(" \\+\\+ ")));

					// In ra thông tin nhóm mới được tạo
					System.out.println("\n ** '" + this.infoClient() + "' vừa tạo nhóm \n" + "Tên nhóm: \"" + groupName
							+ "\", với " + quantityInGroup + " thành viên: ");
					for (String clients : clientsInGroup) {
						System.out.println(clients);
					}

					// Xử lý thêm nhóm mới, thêm client vào nhóm
					chatServer.createGroup(groupName, quantityInGroup, clientsInGroup, this.clientID);
					// Kiểm tra đã có group mới trong danh sách groups ở chatServer chưa
					System.out.println("\nGroups trong chatServer: " + chatServer.listGroups);

				}

				else if (message.startsWith("DISCONNECT#")) { // Client vừa disconnect gửi thông báo

					System.out.println("'" + this.infoClient() + "' đã tự ngắt kết nối.");
//					handleClientDisconnect();
				}

				else if (message.startsWith("MessageOfClient#")) { // Tin nhắn client -> client
					String[] parts = message.split("#");
					if (parts.length == 4) {
						if (parts[1].equals("IMAGE")) {
//							String clientSend = parts[2].trim();
							String clientReceive = parts[2].trim();
							String base64Image = parts[3].trim();
							ClientHandler clientHandler_Re = chatServer.getClientByInfo(clientReceive);
							chatServer.broadcastMessage(this, clientHandler_Re, base64Image, "image");
							System.out.println("\n * '" + this.infoClient() + "' gửi 1 tin nhắn đến '" + clientReceive
									+ "': *hình ảnh*");

						} else {
							System.out.println("Gửi tin không hợp lệ:" + message);
						}
					} else {
//						String clientSend = parts[1];
						String clientReceive = parts[1];
						String messageSendTo = parts[2];
						ClientHandler clientHandler_Re = chatServer.getClientByInfo(clientReceive);

						chatServer.broadcastMessage(this, clientHandler_Re, messageSendTo, null);
						System.out.println("\n * '" + this.infoClient() + "' gửi 1 tin nhắn đến '" + clientReceive
								+ "': " + messageSendTo);
					}
				}

				else if (message.startsWith("MessageOfGroup#")) { // Tin nhắn client -> group
					String[] parts = message.split("#");
					if (parts.length == 4) {
						if (parts[1].equals("IMAGE")) {
//							String clientSend = parts[2].trim();
							String groupName = parts[2].trim();
							String base64Image = parts[3].trim();
							chatServer.broadcastMessageToGroup(this, groupName, base64Image, "image");
							System.out.println("\n * '" + this.infoClient() + "' gửi 1 tin nhắn đến nhóm '" + groupName
									+ "': *hình ảnh*");
						} else {
							System.out.println("Gửi tin không hợp lệ:" + message);
						}
					} else {
//						String clientSend = parts[1].trim();
						String groupName = parts[1].trim();
						String messageSendTo = parts[2];
						chatServer.broadcastMessageToGroup(this, groupName, messageSendTo, null);
						System.out.println("\n * '" + this.infoClient() + "' gửi 1 tin nhắn đến nhóm '" + groupName
								+ "': " + messageSendTo);
					}
				}
			}
		} catch (SocketException e) {
//			System.out.println(clientID + "|" + clientName +"vừa ngắt kết nối");
			System.err.println("Lỗi socket ClientHandler: " + e);
		} catch (Exception e) {
			System.err.println("Lỗi ClientHandler: " + e);
		}
	}

	public void sendMessage(String message) {
		try {
			output.write((message + "\n").getBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleClientDisconnect() {
		try {
			// Xóa client khỏi danh sách server
			chatServer.broadcastDisconnect(this.infoClient());
			chatServer.removeClient(this);
			chatServer.updateQuantityConnect();
			// Đóng socket và streams
			if (socket != null)
				socket.close();
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			System.out.println("Đã xóa client " + this.infoClient() + " khỏi server.");
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String infoClient() {
		return this.clientID + "|" + this.fullName;
	}

}
