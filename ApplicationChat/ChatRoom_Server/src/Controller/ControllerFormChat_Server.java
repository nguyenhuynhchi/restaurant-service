package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import View.V_FrmChat_Server;

public class ControllerFormChat_Server implements ActionListener {
	private V_FrmChat_Server vFC;

	public ControllerFormChat_Server(V_FrmChat_Server vFC) {
		this.vFC = vFC;
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
	}

}
