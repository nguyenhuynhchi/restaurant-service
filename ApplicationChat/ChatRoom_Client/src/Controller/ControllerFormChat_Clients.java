package Controller;

import ChatRoom_Client.ChatClient;
import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControllerFormChat_Clients implements ActionListener, MouseListener {

    private V_FrmChat_Client vFC;
    private V_FrmUserAccess vFU;
    
    private ChatClient chatClient;

    public ControllerFormChat_Clients(V_FrmChat_Client vFC, V_FrmUserAccess vFU) {
        this.vFC = vFC;
        this.vFU = vFU;
        this.chatClient = chatClient.getInstance(vFC, vFU);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("Clients")) {
            vFC.scrollPane_listGroupName.setVisible(false);
            vFC.scrollPane_listUIDName.setVisible(true);
            vFC.list_GroupName.clearSelection();
            vFC.btn_viewClientsGroup.setVisible(false);
        } else if (actionCommand.equals("Nhóm")) {
            vFC.scrollPane_listUIDName.setVisible(false);
            vFC.scrollPane_listGroupName.setVisible(true);
//            vFC.btn_viewClientsGroup.setVisible(true);
            vFC.list_UIDName_onl.clearSelection();
        }

        if (actionCommand.equals("Tạo nhóm")) {
            vFC.panel_TaoNhom.setVisible(true);
            vFC.panel_chat.setVisible(false);
        } else if (actionCommand.equals("Đóng")) {
            vFC.panel_TaoNhom.setVisible(false);
            vFC.panel_chat.setVisible(true);
            vFC.textField_TenNhom.setText("");
            vFC.list_UIDName_onl_taoNhom.clearSelection();
        }

        if (actionCommand.equals("Xem thành viên trong nhóm")) {
            vFC.panel_clientsGroup.setVisible(true);
        } else if (actionCommand.equals("Đóng xem thành viên trong nhóm") || vFC.btn_viewClientsGroup.isVisible() == false) {
            vFC.panel_clientsGroup.setVisible(false);
        }

        setupGroupListListener();
//        setupList_UIDName_onl();
    }

//    private void setupList_UIDName_onl() {
//        vFC.list_UIDName_onl.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    if (vFC.list_UIDName_onl.getSelectedValue() != null) {
//                        vFC.lbl_IDClientChat.setVisible(true);
//                        vFC.lbl_IDClientChat.setText("ID: " + vFC.list_UIDName_onl.getSelectedValue().split("\\|")[0].trim());
//                        vFC.lbl_nameClientChat.setText("Tên: " + vFC.list_UIDName_onl.getSelectedValue().split("\\|")[1].trim());
//                    } else {
//                        vFC.lbl_IDClientChat.setText("ID: ");
//                        vFC.lbl_nameClientChat.setText("Tên: ");
//                    }
//                }
//            }
//        });
//
//    }

    private void setupGroupListListener() {
        vFC.list_GroupName.addListSelectionListener(new ListSelectionListener() { // lắng nghe khi chọn 1 group trong JList
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (vFC.list_GroupName.getSelectedValue() != null) {
                        String selectedGroup = vFC.list_GroupName.getSelectedValue().split("\\|")[0].trim(); // Lấy tên nhóm được chọn

                        vFC.lbl_nameClientChat.setText("Tên nhóm: " + selectedGroup);
                        vFC.lbl_IDClientChat.setVisible(false);
                        vFC.list_UIDName_onl.clearSelection();
                        System.out.println("Groups được chọn: '" + selectedGroup + "'");

                        if (selectedGroup != null) {
                            // Lấy danh sách tên client trong nhóm từ ChatServer
                            List<String> clientNames = chatClient.getClientsInGroup(selectedGroup);

                            if (clientNames == null || clientNames.isEmpty()) {
                                System.out.println("Không tìm thấy nhóm hoặc nhóm không có client.");
                            } else {
                                System.out.println("Client in group '" + selectedGroup + "': ");
                                for (String clients : clientNames) {
                                    System.out.println(clients);
                                }
                                vFC.list_GroupName.getSelectionModel().setValueIsAdjusting(true);
                                vFC.updateListClientsGroup(clientNames);
                                vFC.list_GroupName.getSelectionModel().setValueIsAdjusting(false);
                            }
                        }
                        vFC.btn_viewClientsGroup.setVisible(true);
                    }
                }
            }
        });
    }

    @Override

    public void mouseClicked(MouseEvent e) {
        int index = vFC.list_UIDName_onl_taoNhom.locationToIndex(e.getPoint());

        if (index >= 0) { // Kiểm tra xem có item nào được nhấn hay không
            if (vFC.list_UIDName_onl_taoNhom.isSelectedIndex(index)) {
                // Nếu item đã được chọn, bỏ chọn
                vFC.list_UIDName_onl_taoNhom.removeSelectionInterval(index, index);
            } else {
                // Nếu item chưa được chọn, chọn mục đó
                vFC.list_UIDName_onl_taoNhom.addSelectionInterval(index, index);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
