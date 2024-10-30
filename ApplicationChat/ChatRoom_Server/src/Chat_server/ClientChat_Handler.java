package Chat_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ClientChat_Handler implements Runnable {

	private Socket mySocket;
	private ChatServer chatServer;
	private String clientID;
	private String clientName;

	private InputStream input;
	private OutputStream output;

	public ClientChat_Handler(Socket mySocket, ChatServer chatServer) {
		this.mySocket = mySocket;
		this.chatServer = chatServer;
		try {
			this.input = mySocket.getInputStream();
			this.output = mySocket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Lỗi ở ClientHandler");
		}
	}

	@Override
	public void run() {
		try {
			byte[] buffer = new byte[2048];
			int bytesRead;

			// Liên tục nhận tin nhắn từ client và chuyển tiếp tới các client khác
			while ((bytesRead = input.read(buffer)) != -1) {
				String message = new String(buffer, 0, bytesRead).trim();

				chatServer.broadcastMessage(this.clientID, this.clientName, message);
				System.out.println("Tin nhắn từ "+this.clientName + "(" + this.clientID + "): " + message);
			}
		} catch (SocketException e) {
	        System.out.println(clientName + "(" + clientID + ") "+ "vừa ngắt kết nối");
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Lỗi ở ClientChat_Handler (PT run)");
	    } finally {
	        // Xử lý ngắt kết nối và xoá client khỏi danh sách khi ngoại lệ xảy ra
	        chatServer.removeClient(this, this.clientName);  //xóa client khỏi danh sách ở Chatserver 
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
	
	public void closeConnection() {   // đóng kết nói để giải phóng tài nguyên của client đó
	    try {
	        if (input != null) input.close();
	        if (output != null) output.close();
	        if (mySocket != null) mySocket.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


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

//	public void sendInfo(String info) {
//		try {
//			output.write(info.getBytes());
//			output.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	

}
