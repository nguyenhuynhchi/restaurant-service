package Chat_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class ClientInfo_Handler implements Runnable {

	private Socket mySocket;
	private ChatServer chatServer;
	private String clientID;
	private String clientName;

	private InputStream input;
	private OutputStream output;

	public ClientInfo_Handler(Socket mySocket, ChatServer chatServer) {
		this.mySocket = mySocket;
		this.chatServer = chatServer;
		try {
			this.input = mySocket.getInputStream();
			this.output = mySocket.getOutputStream();

			// Đọc tên và id từ client
			byte[] buffer = new byte[1024];
			int bytesRead = input.read(buffer);
			String clientData = new String(buffer, 0, bytesRead).trim();

			// Tách tên và ID bằng ký tự "|"
			if(clientData.startsWith("InfoNewClients|")) {
				
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
	public void run() {  // chưa cần xử lý
		
	}


	public void sendInfo(String info) {
		try {
			output.write(info.getBytes());
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendDisconnectNotification(String disconnectedClientName) {  // Gửi thông điệp client đã ngắt kết nối để xóa client khỏi list
	    try {
	        output.write(("DISCONNECT|" + disconnectedClientName + "\n").getBytes());
	        output.flush();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public String getClientID() {
		return clientID;
	}

	public String getClientName() {
		return clientName;
	}

}
