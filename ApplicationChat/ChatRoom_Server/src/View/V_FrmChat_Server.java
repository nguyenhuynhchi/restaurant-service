package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Controller.ControllerFormChat_Server;

public class V_FrmChat_Server extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private DefaultListModel<String> model_Clients;
	private DefaultListModel<String> model_Groups;
//	public DefaultListModel<String> model_ClientsInGroup;
	public JList<String> list_UIDName_onl;
	public JList<String> list_GroupName;
	public JList<String> list_UIDNameInGr;
	public JScrollPane scrollPane_listUIDName;
	public JScrollPane scrollPane_listGroupName;
	public JButton btn_Clients;
	public JButton btn_Nhom;
	public JPanel panel_thongTinNhom;
	public JPanel panel;
	private String userName = "Server";
	public int soLuongConnect = 0;
	public JLabel lbl_tenNhom;
	public int port;
	public boolean connect = false;
	private JLabel lbl_port;
	public JLabel lbl_soLuongClient;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					V_FrmChat_Server frame = new V_FrmChat_Server();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public V_FrmChat_Server() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("LAN Chat Application - Server");
		URL urlIconFrame = V_FrmChat_Server.class.getResource("/Images/App_server.png");
		Image img = Toolkit.getDefaultToolkit().createImage(urlIconFrame);
		setIconImage(img);

//		nhapTen();

		ActionListener ac = new ControllerFormChat_Server(this);

		setBounds(0, 0, 1500, 800);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JMenu jm_Options = new JMenu("Options");
		jm_Options.setMnemonic(KeyEvent.VK_O);
		JMenuItem jmit_display = new JMenuItem("DisPlay");
		jm_Options.add(jmit_display);

		JMenu jm_Help = new JMenu("Help");
		jm_Help.setMnemonic(KeyEvent.VK_H);
		JMenuItem jmit_welcome = new JMenuItem("Welcome");
		jm_Help.add(jmit_welcome);

		menuBar.add(jm_Options);
		menuBar.add(jm_Help);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_Chinh = new JPanel();
		panel_Chinh.setBackground(new Color(128, 128, 128));
		panel_Chinh.setBounds(300, 0, 1236, 740);
		panel_Chinh.setLayout(null);
		contentPane.add(panel_Chinh);

		JPanel panel_TinNhan = new JPanel();
		panel_TinNhan.setBackground(new Color(119, 173, 183));
		panel_TinNhan.setForeground(new Color(255, 255, 255));
//		panel_TinNhan.setBounds(0, 44, 1236, 697);
		panel_TinNhan.setLayout(new BoxLayout(panel_TinNhan, BoxLayout.Y_AXIS));
//		panel_chat.add(panel_TinNhan);

		JPanel panel_caiDat = new JPanel();
		panel_caiDat.setBounds(0, 0, 1190, 740);
		panel_caiDat.setVisible(false);

		panel_thongTinNhom = new JPanel();
		panel_thongTinNhom.setBounds(0, 0, 1190, 740);
		panel_thongTinNhom.setVisible(false);

		panel = new JPanel();
		panel.setBackground(new Color(128, 128, 128));
		panel.setBounds(0, 0, 1190, 740);
		panel_Chinh.add(panel);
		panel_Chinh.add(panel_thongTinNhom);
		panel_thongTinNhom.setLayout(null);

//		model_ClientsInGroup = new DefaultListModel<>();
		list_UIDNameInGr = new JList<>();
		list_UIDNameInGr.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list_UIDNameInGr.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane_listUIDNameInGr = new JScrollPane(list_UIDNameInGr);
		scrollPane_listUIDNameInGr.setBounds(60, 85, 320, 500);
		panel_thongTinNhom.add(scrollPane_listUIDNameInGr);

		JLabel lbl_thongTinNhom = new JLabel("Thông tin nhóm: ");
		lbl_thongTinNhom.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_thongTinNhom.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbl_thongTinNhom.setBounds(0, 0, 170, 50);
		panel_thongTinNhom.add(lbl_thongTinNhom);

		lbl_tenNhom = new JLabel("");
		lbl_tenNhom.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_tenNhom.setBounds(170, 0, 320, 50);
		panel_thongTinNhom.add(lbl_tenNhom);

		JLabel lbl_thanhVien = new JLabel("Thành Viên trong nhóm");
		lbl_thanhVien.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbl_thanhVien.setBounds(60, 55, 320, 30);
		panel_thongTinNhom.add(lbl_thanhVien);
		panel_caiDat.setLayout(null);
		panel_Chinh.add(panel_caiDat);

		JLabel lbl_caiDat = new JLabel("Cài đặt: ");
		lbl_caiDat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_caiDat.setBounds(10, 10, 59, 27);
		panel_caiDat.add(lbl_caiDat);

		JPanel panel_nguoidung = new JPanel();
		panel_nguoidung.setBackground(new Color(192, 192, 192));
		panel_nguoidung.setBounds(0, 0, 300, 740);
		contentPane.add(panel_nguoidung);
		panel_nguoidung.setLayout(null);

		JPanel panel_UIDName = new JPanel();
		panel_UIDName.setBackground(new Color(86, 171, 171));
		panel_UIDName.setBounds(0, 0, 300, 95);
		panel_nguoidung.add(panel_UIDName);
		panel_UIDName.setLayout(null);

		lbl_port = new JLabel("Số cổng: ");
		lbl_port.setBackground(new Color(255, 255, 255));
		lbl_port.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lbl_port.setBounds(10, 30, 170, 25);
		panel_UIDName.add(lbl_port);

		openPort();

		JLabel lbl_tenNguoiDung = new JLabel(userName);
		lbl_tenNguoiDung.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_tenNguoiDung.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lbl_tenNguoiDung.setBounds(90, 0, 115, 33);
		panel_UIDName.add(lbl_tenNguoiDung);
		
		
		lbl_soLuongClient = new JLabel("Số người kết nối: " + soLuongConnect);
		lbl_soLuongClient.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lbl_soLuongClient.setBackground(Color.WHITE);
		lbl_soLuongClient.setBounds(10, 60, 170, 25);
		panel_UIDName.add(lbl_soLuongClient);
		panel_TinNhan.setLayout(new BoxLayout(panel_TinNhan, BoxLayout.Y_AXIS));

		model_Clients = new DefaultListModel<>();
		list_UIDName_onl = new JList<>(model_Clients);
		list_UIDName_onl.setFont(new Font("Tahoma", Font.PLAIN, 20));

		scrollPane_listUIDName = new JScrollPane(list_UIDName_onl);
		scrollPane_listUIDName.setBounds(0, 130, 300, 610);
		scrollPane_listUIDName.setVisible(true);
		panel_nguoidung.add(scrollPane_listUIDName);

		model_Groups = new DefaultListModel<>();
		list_GroupName = new JList<>(model_Groups);
		list_GroupName.setFont(new Font("Tahoma", Font.PLAIN, 20));

		scrollPane_listGroupName = new JScrollPane(list_GroupName);
		scrollPane_listGroupName.setBounds(0, 140, 300, 545);
		scrollPane_listGroupName.setVisible(false);
		panel_nguoidung.add(scrollPane_listGroupName);

		JPanel panel_Clients_Nhom = new JPanel();
		panel_Clients_Nhom.setBackground(new Color(128, 128, 128));
		panel_Clients_Nhom.setLayout(null);
		panel_Clients_Nhom.setBounds(0, 95, 300, 35);
		panel_nguoidung.add(panel_Clients_Nhom);

		btn_Clients = new JButton("Clients");
		btn_Clients.setFont(new Font("Tahoma", Font.BOLD, 15));
		btn_Clients.setBounds(0, 0, 145, 35);
		btn_Clients.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		btn_Clients.setActionCommand("Clients");
		btn_Clients.addActionListener(ac);
		panel_Clients_Nhom.add(btn_Clients);

		btn_Nhom = new JButton("Nhóm");
		btn_Nhom.setFont(new Font("Tahoma", Font.BOLD, 15));
		btn_Nhom.setBounds(155, 0, 145, 35);
		btn_Nhom.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		btn_Nhom.setActionCommand("Nhóm");
		btn_Nhom.addActionListener(ac);
		panel_Clients_Nhom.add(btn_Nhom);
	}

//	public void addClient_ToJList_(String clientID, String clientName) {
//		String newClient = clientID + " | " + clientName;
//		model_Clients.addElement(newClient); // Thêm clientID vào JList
//	}

	public void openPort() {
		while (true) {
			try {
				String portInput = JOptionPane.showInputDialog(null, "Nhập số cổng: ", "", JOptionPane.PLAIN_MESSAGE);
				
				if (portInput == null) { // Kiểm tra nếu người dùng nhấn "Cancel"
	                System.out.println("Người dùng đã hủy.");
	                System.exit(0); // Thoát chương trình
	            }
				if (portInput != null) { // Kiểm tra người dùng có nhấn "Cancel" hay không
					port = Integer.parseInt(portInput); // Chuyển đổi chuỗi thành số nguyên
					System.out.println("Số cổng bạn đã nhập là: " + port);
					connect = true;
					lbl_port.setText("Số cổng: " + port);
					break;
				} else if (portInput.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Bạn chưa nhập số cổng để mở", "Thông báo",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập một số nguyên hợp lệ!", "Lỗi nhập liệu",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void addClient_ToJList(String infoClient) {
		model_Clients.addElement(infoClient); // Thêm client vào JList
	}

	public void addGroup_ToJList(String groupName, String quantityInGroup) {
		String newGroup = groupName + "|" + quantityInGroup;
		model_Groups.addElement(newGroup);
	}

	public void removeClientFromList(String infoClientDisconnect) {
//		for (int i = 0; i < model_Clients.size(); i++) {
//			if (model_Clients.get(i).contains(infoClientDisconnect)) {
//				model_Clients.remove(i);
//				break;
//			}
//		}
		System.out.println("Đã xóa client " + infoClientDisconnect + " khỏi JList");
		model_Clients.removeElement(infoClientDisconnect);
	}

	public void updateClientListInGroup(List<String> clientNameInGroup) {
		// Cập nhật danh sách client trong nhóm
		DefaultListModel<String> modelClientsGroup = new DefaultListModel<>();
		for (String clientName : clientNameInGroup) {
			modelClientsGroup.addElement(clientName);
		}
		list_UIDNameInGr.setModel(modelClientsGroup);
	}
}
