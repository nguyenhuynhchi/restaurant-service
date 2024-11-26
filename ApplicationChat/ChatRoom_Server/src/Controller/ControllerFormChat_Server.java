package Controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Chat_server.ChatServer;
import View.V_FrmChat_Server;

public class ControllerFormChat_Server implements ActionListener {
	private V_FrmChat_Server vFC;
	private ChatServer chatServer;

	public ControllerFormChat_Server(V_FrmChat_Server vFC) {
		this.vFC = vFC;
		this.chatServer = new ChatServer(vFC);
		this.chatServer = ChatServer.getInstance(vFC);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand  = e.getActionCommand();

		if (actionCommand.equals("Clients")) {
			vFC.panel.setVisible(true);
			vFC.scrollPane_listUIDName.setVisible(true);
			vFC.panel_thongTinNhom.setVisible(false);
			vFC.scrollPane_listGroupName.setVisible(false);
			vFC.btn_Nhom.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			vFC.btn_Clients.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
			vFC.list_GroupName.clearSelection();
		} else if (actionCommand.equals("Nhóm")) {
			vFC.scrollPane_listGroupName.setVisible(true);
			vFC.panel_thongTinNhom.setVisible(true);
			vFC.scrollPane_listUIDName.setVisible(false);
			vFC.panel.setVisible(false);
			vFC.btn_Nhom.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
			vFC.btn_Clients.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		}

		setupGroupListListener();
	}

	private void setupGroupListListener() {
		vFC.list_GroupName.addListSelectionListener(new ListSelectionListener() { // lắng nghe khi chọn 1 group trong
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (vFC.list_GroupName.getSelectedValue() != null) {
						String selectedGroup = vFC.list_GroupName.getSelectedValue().split("\\|")[0].trim(); // Lấy tên
																												// nhóm
																												// được
																												// chọn
						System.out.println("Groups được chọn: '" + selectedGroup + "'");
						if (selectedGroup != null) {

							// Lấy danh sách tên client trong nhóm từ ChatServer
							List<String> clientNames = chatServer.getClientsInGroup(selectedGroup);

							if (clientNames == null || clientNames.isEmpty()) {
								System.out.println("Không tìm thấy nhóm hoặc nhóm không có client.");
							}

							else {
								System.out.println("Client in group '" + selectedGroup + "': ");
								for (String clients : clientNames) {
									System.out.println(clients);
								}
								vFC.list_UIDNameInGr.getSelectionModel().setValueIsAdjusting(true);
								vFC.lbl_tenNhom.setText(selectedGroup);
								vFC.updateClientListInGroup(clientNames);
								vFC.list_UIDNameInGr.getSelectionModel().setValueIsAdjusting(false);
							}

						}
					} else {
						List<String> listNoClients = new ArrayList<>();
						vFC.updateClientListInGroup(listNoClients); // List Rỗng
					}
				}
			}
		});
		
		
	}

//	private void updateClientListInGroup(String groupName) {
	// Lấy danh sách tên client trong nhóm từ ChatServer
//        List<String> clientNames = chatServer.getClientNamesInGroup(groupName);
//
//        // Cập nhật danh sách clients trong list_UIDNameInGr
//        vFC.updateClientListInGroup(clientNames);
//    }	

}
