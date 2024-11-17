package Controller;

import ChatRoom_Client.ChatClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import View.V_FrmChat_Client;
import java.util.List;

public class ControllerFormChat_Clients implements ActionListener, MouseListener {

    private V_FrmChat_Client vFC;
    private ChatClient chatClient;

    public ControllerFormChat_Clients(V_FrmChat_Client vFC) {
        this.vFC = vFC;
        this.chatClient = chatClient.getInstance(vFC);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String button = e.getActionCommand();

        if (button.equals("Clients")) {
            vFC.scrollPane_listGroupName.setVisible(false);
            vFC.scrollPane_listUIDName.setVisible(true);
        } else if (button.equals("Nhóm")) {
            vFC.scrollPane_listUIDName.setVisible(false);
            vFC.scrollPane_listGroupName.setVisible(true);
        }

        if (button.equals("Tạo nhóm")) {
            vFC.panel_TaoNhom.setVisible(true);
            vFC.panel_chat.setVisible(false);
        } else if (button.equals("Đóng")) {
            vFC.panel_TaoNhom.setVisible(false);
            vFC.panel_chat.setVisible(true);
            vFC.textField_TenNhom.setText("");
            vFC.list_UIDName_onl_taoNhom.clearSelection();
        }

        
//        if (button.equals("Tạo")) {
//            String groupName = vFC.textField_TenNhom.getText();
//            List<String> selectedClients = vFC.list_UIDName_onl_taoNhom.getSelectedValuesList();
//            chatClient.sendGroupInfo(groupName, selectedClients);
//            vFC.addGroupToList(groupName);
//        }

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
