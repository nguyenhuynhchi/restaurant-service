package test;

import javax.swing.UIManager;

import Chat_server.ChatServer;
import View.V_FrmChat_Server;

public class test_frmChat_Server {
//	public V_FrmChat_Server vFC;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			V_FrmChat_Server vFC = new V_FrmChat_Server();
			vFC.setVisible(true);
			
			// Khởi tạo server chat và truyền viewFormChat vào
//	        ChatServer chatServer = new ChatServer(vFC);
//	        chatServer.startServer();
			
			ChatServer chatServer = ChatServer.getInstance(vFC);
	        chatServer.startServer();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
