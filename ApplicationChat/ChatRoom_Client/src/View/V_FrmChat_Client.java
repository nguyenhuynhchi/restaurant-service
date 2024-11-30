package View;

import Controller.ControllerFormChat_Clients;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class V_FrmChat_Client extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    public JPanel panel_TaoNhom;
    public JPanel panel_caiDat;
    public JTextField tf_message;

    private DefaultListModel<String> model_clients;
    private DefaultListModel<String> model_groups;
    private DefaultListModel<String> model_clientGroup;
    public JList<String> list_UIDName_onl;
    public JList<String> list_UIDName_onl_taoNhom;
    public JList<String> list_GroupName;

    public JPanel panel_ThongBaoKetNoi;
    public JPanel panel_Chinh;
    public JPanel panel_nguoidung;
    public JPanel panel_chucNang;

    public String userName;
    public String ID;

    private JPanel panel_TinNhan;
    private JTextArea messageArea;
    private JScrollPane scrollPane_TinNhan;
    public JScrollPane scrollPane_listUIDName;
    private JScrollPane scrollPane_listUIDName_taoNhom;
    public JScrollPane scrollPane_listGroupName;
    public JTextField textField_TenNhom;
    public JPanel panel_chat;

    public JButton btn_taoNhom;
    public JButton btn_guiTin;
    public JButton btn_xacNhanTaoNhom;

    public JLabel lbl_IDClientChat;
    public JLabel lbl_nameClientChat;
    private JScrollPane scrollPane_clientsGroup;
    private JList list_clientsGroup;
    public JButton btn_viewClientsGroup;
    public JPanel panel_clientsGroup;
//	private static ChatClient chatClient;

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    V_FrmChat_Client frame = new V_FrmChat_Client();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Create the frame.
     */
    public V_FrmChat_Client() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("LAN Chat Application - Client");

        URL urlIconFrame = V_FrmChat_Client.class.getResource("/Images/App_server.png");
        Image img = Toolkit.getDefaultToolkit().createImage(urlIconFrame);
        setIconImage(img);

        try {
            // Lấy địa chỉ IP của máy tính
            InetAddress ip = InetAddress.getLocalHost();
            String ipAddress = ip.getHostAddress();

            // Chuyển đổi IP thành số 3 chữ số
            int ipHash = ipAddress.hashCode(); // Lấy giá trị băm của địa chỉ IP
            int ipNumber = Math.abs(ipHash) % 1000;
            ID = String.format("%03d", ipNumber);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            ID = 000 + "";
        }

//        nhapTen();

        V_FrmUserAccess vFU = new V_FrmUserAccess(this);
        ActionListener ac = new ControllerFormChat_Clients(this);
//        ControllerFormChat_Clients mouse_Ctrl = new ControllerFormChat_Clients(this);  

        setBounds(0, 0, 1460, 830);

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

        panel_chucNang = new JPanel();
        panel_chucNang.setBackground(new Color(255, 255, 255));
        panel_chucNang.setBounds(0, 700, 300, 60);
        contentPane.add(panel_chucNang);
        panel_chucNang.setLayout(null);
        // btn_guiNhieuTin.addActionListener(ac);

        btn_taoNhom = new JButton("Tạo nhóm");
        btn_taoNhom.setFont(new Font("Arial", Font.BOLD, 14));
        btn_taoNhom.setBounds(160, 0, 120, 50);
        btn_taoNhom.setActionCommand("Tạo nhóm");
        btn_taoNhom.addActionListener(ac);
        panel_chucNang.add(btn_taoNhom);

        panel_Chinh = new JPanel();
        panel_Chinh.setBackground(new Color(128, 128, 128));
        panel_Chinh.setBounds(300, 0, 1150, 761);
        panel_Chinh.setLayout(null);
        contentPane.add(panel_Chinh);

        panel_TaoNhom = new JPanel();
        panel_TaoNhom.setBounds(0, 0, 1150, 760);
        panel_TaoNhom.setBackground(new Color(255, 255, 255));
        panel_TaoNhom.setVisible(false);

        panel_chat = new JPanel();
        panel_chat.setBounds(0, 0, 1150, 760);
        panel_chat.setLayout(null);
        panel_chat.setVisible(true);
        panel_Chinh.add(panel_chat);

        tf_message = new JTextField();
        tf_message.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tf_message.setBounds(100, 710, 938, 45);
        tf_message.setColumns(10);
        tf_message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf_message.getText().length() >= 1000) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        panel_chat.add(tf_message);

        panel_clientsGroup = new JPanel();
        panel_clientsGroup.setForeground(new Color(128, 128, 128));
        panel_clientsGroup.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        panel_clientsGroup.setBounds(750, 50, 390, 600);
        panel_clientsGroup.setLayout(null);
        panel_clientsGroup.setVisible(false);
        panel_chat.add(panel_clientsGroup);

        scrollPane_clientsGroup = new JScrollPane();
        scrollPane_clientsGroup.setBounds(3, 30, 384, 567);
        panel_clientsGroup.add(scrollPane_clientsGroup);

//        model_clientGroup = new DefaultListModel<>();
        list_clientsGroup = new JList<>();
        list_clientsGroup.setFont(new Font("Tahoma", Font.PLAIN, 20));
        list_clientsGroup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane_clientsGroup.setViewportView(list_clientsGroup);

        JButton btn_dongViewClientsGroup = new JButton("x");
        btn_dongViewClientsGroup.setBounds(0, 0, 40, 30);
        btn_dongViewClientsGroup.setActionCommand("Đóng xem thành viên trong nhóm");
        btn_dongViewClientsGroup.addActionListener(ac);
        panel_clientsGroup.add(btn_dongViewClientsGroup);

        btn_guiTin = new JButton("Gửi");
        btn_guiTin.setFont(new Font("Arial", Font.PLAIN, 12));
        btn_guiTin.setBounds(1054, 710, 75, 45);
        // Sử dụng InputMap và ActionMap để ánh xạ phím Enter
        btn_guiTin.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "doClick");
        btn_guiTin.getActionMap().put("doClick", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn_guiTin.doClick();
            }
        });
        panel_chat.add(btn_guiTin);

        panel_TinNhan = new JPanel();
        panel_TinNhan.setBackground(new Color(192, 192, 192));
        panel_TinNhan.setLayout(new BoxLayout(panel_TinNhan, BoxLayout.Y_AXIS));

        scrollPane_TinNhan = new JScrollPane(panel_TinNhan);
        scrollPane_TinNhan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_TinNhan.setBounds(0, 50, 1150, 650);
        panel_chat.add(scrollPane_TinNhan);

        lbl_IDClientChat = new JLabel("ID:");
        lbl_IDClientChat.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lbl_IDClientChat.setBackground(Color.WHITE);
        lbl_IDClientChat.setBounds(377, 10, 183, 25);
        panel_chat.add(lbl_IDClientChat);

        lbl_nameClientChat = new JLabel("Tên: ");
        lbl_nameClientChat.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lbl_nameClientChat.setBounds(10, 10, 250, 25);
        panel_chat.add(lbl_nameClientChat);

        btn_viewClientsGroup = new JButton("Xem thành viên trong nhóm");
        btn_viewClientsGroup.setToolTipText("Xem thành viên trong nhóm");
        btn_viewClientsGroup.setBounds(881, 7, 170, 35);
        btn_viewClientsGroup.setActionCommand("Xem thành viên trong nhóm");
        btn_viewClientsGroup.addActionListener(ac);
        btn_viewClientsGroup.setVisible(false);
        panel_chat.add(btn_viewClientsGroup);
        panel_TaoNhom.setLayout(null);
        panel_Chinh.add(panel_TaoNhom);

        // ListModel chứa các clients online
        model_clients = new DefaultListModel<>();

        list_UIDName_onl_taoNhom = new JList<>(model_clients);
        list_UIDName_onl_taoNhom.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        list_UIDName_onl_taoNhom.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrollPane_listUIDName_taoNhom = new JScrollPane(list_UIDName_onl_taoNhom);
        scrollPane_listUIDName_taoNhom.setBounds(20, 127, 457, 425);
        panel_TaoNhom.add(scrollPane_listUIDName_taoNhom);

        JLabel lbl_taoNhom = new JLabel("Tạo nhóm");
        lbl_taoNhom.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_taoNhom.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lbl_taoNhom.setBounds(0, 0, 110, 44);
        panel_TaoNhom.add(lbl_taoNhom);

        JLabel lbl_TenNhom = new JLabel("Tên nhóm:");
        lbl_TenNhom.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_TenNhom.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lbl_TenNhom.setBounds(20, 55, 90, 35); // x, y, w, h
        panel_TaoNhom.add(lbl_TenNhom);

        textField_TenNhom = new JTextField();
        textField_TenNhom.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        textField_TenNhom.setBounds(110, 55, 320, 30);
        textField_TenNhom.setColumns(10);
        textField_TenNhom.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField_TenNhom.getText().length() >= 25) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        panel_TaoNhom.add(textField_TenNhom);

        JLabel lbl_ChonThanhVien = new JLabel("Chọn thành viên:");
        lbl_ChonThanhVien.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_ChonThanhVien.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        lbl_ChonThanhVien.setBounds(20, 99, 130, 30);
        panel_TaoNhom.add(lbl_ChonThanhVien);

        JButton btn_dong = new JButton("Đóng");
        btn_dong.setFont(new Font("Arial", Font.BOLD, 14));
        btn_dong.setBounds(512, 700, 90, 50);
        btn_dong.setActionCommand("Đóng");
        btn_dong.addActionListener(ac);
        panel_TaoNhom.add(btn_dong);

        btn_xacNhanTaoNhom = new JButton("Tạo");
        btn_xacNhanTaoNhom.setFont(new Font("Arial", Font.BOLD, 14));
        btn_xacNhanTaoNhom.setBounds(412, 700, 90, 50);
        btn_xacNhanTaoNhom.addActionListener(ac);
        panel_TaoNhom.add(btn_xacNhanTaoNhom);

        messageArea = new JTextArea();
        messageArea.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        messageArea.setLineWrap(true); // Tự động xuống dòng
        messageArea.setWrapStyleWord(true); // Xuống dòng theo từ
        messageArea.setEditable(false);
        messageArea.setBackground(new Color(200, 200, 200));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel_caiDat = new JPanel();
        panel_caiDat.setBounds(0, 82, 1150, 678);
        panel_caiDat.setVisible(false);
        panel_caiDat.setLayout(null);
        panel_Chinh.add(panel_caiDat);

        JLabel lbl_caiDat = new JLabel("Cài đặt: ");
        lbl_caiDat.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lbl_caiDat.setBounds(10, 10, 59, 27);
        panel_caiDat.add(lbl_caiDat);

        panel_nguoidung = new JPanel();
        panel_nguoidung.setBackground(new Color(192, 192, 192));
        panel_nguoidung.setBounds(0, 0, 300, 700);
        contentPane.add(panel_nguoidung);
        panel_nguoidung.setLayout(null);

        JPanel panel_UIDName = new JPanel();
        panel_UIDName.setBackground(new Color(192, 192, 192));
        panel_UIDName.setBounds(0, 0, 300, 50);
        panel_nguoidung.add(panel_UIDName);
        panel_UIDName.setLayout(null);

        JLabel lbl_IDNguoiDung = new JLabel("ID:" + ID);
        lbl_IDNguoiDung.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lbl_IDNguoiDung.setBackground(new Color(255, 255, 255));
        lbl_IDNguoiDung.setBounds(10, 0, 250, 25);
        panel_UIDName.add(lbl_IDNguoiDung);

        JLabel lbl_tenNguoiDung = new JLabel("Tên: " + userName);
        lbl_tenNguoiDung.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lbl_tenNguoiDung.setBounds(10, 25, 250, 25);
        panel_UIDName.add(lbl_tenNguoiDung);

//        JLabel lbl_chatUID = new JLabel("000");
//        lbl_chatUID.setFont(new Font("Times New Roman", Font.PLAIN, 15));
//        lbl_chatUID.setBounds(10, 10, 76, 29);
//        panel_chat.add(lbl_chatUID);
//
//        JLabel lbl_chatName = new JLabel("Tên gì đó");
//        lbl_chatName.setFont(new Font("Times New Roman", Font.BOLD, 15));
//        lbl_chatName.setBounds(186, 10, 174, 30);
//        panel_chat.add(lbl_chatName);
//        model_clients = new DefaultListModel<>();
        list_UIDName_onl = new JList<>(model_clients);
        list_UIDName_onl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        list_UIDName_onl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        list_UIDName_onl.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    if (list_UIDName_onl.getSelectedValue() != null) {
//                        lbl_IDClientChat.setText("ID: " + list_UIDName_onl.getSelectedValue().split("\\|")[0].trim());
//                        lbl_nameClientChat.setText("Tên: " + list_UIDName_onl.getSelectedValue().split("\\|")[1].trim());
//                    } else {
//                        lbl_IDClientChat.setText("ID: ");
//                        lbl_nameClientChat.setText("Tên: ");
//                    }
//
//                }
//            }
//        });

        scrollPane_listUIDName = new JScrollPane(list_UIDName_onl);
        scrollPane_listUIDName.setBounds(0, 85, 300, 615);
        scrollPane_listUIDName.setVisible(true);
        panel_nguoidung.add(scrollPane_listUIDName);

        model_groups = new DefaultListModel<>();
        list_GroupName = new JList<>(model_groups);
        list_GroupName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        list_GroupName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane_listGroupName = new JScrollPane(list_GroupName);
        scrollPane_listGroupName.setBounds(0, 85, 300, 615);
        scrollPane_listGroupName.setVisible(false);
        panel_nguoidung.add(scrollPane_listGroupName);

        JPanel panel_Client_Nhom = new JPanel();
        panel_Client_Nhom.setLayout(null);
        panel_Client_Nhom.setBackground(new Color(255, 255, 255));
        panel_Client_Nhom.setBounds(0, 50, 300, 35);
        panel_nguoidung.add(panel_Client_Nhom);

        JButton btn_Clients = new JButton("Clients");
        btn_Clients.setFont(new Font("Arial", Font.BOLD, 15));
        btn_Clients.setBounds(0, 0, 145, 35);
        btn_Clients.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn_Clients.setActionCommand("Clients");
        btn_Clients.addActionListener(ac);
        panel_Client_Nhom.add(btn_Clients);

        JButton btn_Nhom = new JButton("Nhóm");
        btn_Nhom.setFont(new Font("Arial", Font.BOLD, 15));
        btn_Nhom.setBounds(155, 0, 145, 35);
        btn_Nhom.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        btn_Nhom.setActionCommand("Nhóm");
        btn_Nhom.addActionListener(ac);
        panel_Client_Nhom.add(btn_Nhom);

        panel_ThongBaoKetNoi = new JPanel();
        panel_ThongBaoKetNoi.setBackground(new Color(128, 128, 128));
        panel_ThongBaoKetNoi.setBounds(0, 0, 1450, 761);
        panel_ThongBaoKetNoi.setLayout(new BorderLayout(0, 0));
        panel_ThongBaoKetNoi.setVisible(false);
        contentPane.add(panel_ThongBaoKetNoi);

        // Kiểm tra nếu client chưa kết nối đến server thì sẽ hiện panel thông báo không
        // kết nối được với server
//        controller.kiemTraKetNoi();
        JLabel lbl_ThongBaoKetNoi = new JLabel("Không thể kết nối đến server");
        lbl_ThongBaoKetNoi.setFont(new Font("Segoe UI Light", Font.BOLD, 20));
        panel_ThongBaoKetNoi.add(lbl_ThongBaoKetNoi, BorderLayout.CENTER);
//		btn_taoNhom.addActionListener(ac);
    }

    // Phương thức để cập nhật trạng thái của panel
    public void hienThongBaoKetNoi(boolean hienThi) {
        if (hienThi) {
            panel_ThongBaoKetNoi.setVisible(false); // Hiển thị thông báo nếu kết nối không thành công
        } else {
            panel_ThongBaoKetNoi.setVisible(true);
            panel_Chinh.setVisible(false);
            panel_chucNang.setVisible(false);
            panel_nguoidung.setVisible(false);
        }
    }

    public void addClientToList(String IDCln, String NameCln) {
        String newClient = IDCln + "|" + NameCln;
        model_clients.addElement(newClient); // Thêm clientID vào JList
    }

    public void addGroupToList(String groupName, String quantityInGroup) {
        String newGroup = groupName + " | " + quantityInGroup;
        System.out.println(newGroup);
        model_groups.addElement(newGroup);
    }

    public void removeClientInList(String infoClientDisconnect) { // cần sửa phương thức này
        for (int i = 0; i < model_clients.getSize(); i++) {
            String clientInfo = model_clients.getElementAt(i);
            if (clientInfo.equals(infoClientDisconnect)) { //kiểm tra tên trong JList có chứa tên client ngắt kết nối không và xóa
                model_clients.remove(i);
                break;
            }
        }
    }

    public void updateListClientsGroup(List<String> clientsInGroup) {
        // Cập nhật danh sách client trong nhóm
        DefaultListModel<String> model_clientGroup = new DefaultListModel<>();
        System.out.println("Client in group : ");
        for (String clients : clientsInGroup) {
            model_clientGroup.addElement(clients);
            System.out.println("- " + clients);
        }
        list_clientsGroup.setModel(model_clientGroup);
    }

    public void nhapTen() {
        while (userName == null || userName.trim().isEmpty()) {
            userName = JOptionPane.showInputDialog(null, "Vui lòng nhập tên của bạn:", "Nhập tên",
                    JOptionPane.PLAIN_MESSAGE);

            if (userName == null || userName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bạn phải nhập tên để sử dụng ứng dụng!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Phương thức thêm tin nhắn vào chatPanel (Chưa hoàn chỉnh)
    public void addMessage_(String message, String inputMessage) {
        // Tạo JPanel cho tin nhắn và cài đặt căn lề
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(192, 192, 192));

        messagePanel.setLayout(new FlowLayout(inputMessage.equals("in") ? FlowLayout.LEFT : FlowLayout.RIGHT));
        // Tạo JLabel với nội dung
        JLabel messageLabel = new JLabel("<html>" + message + "</html>"); // HTML cho phép tự động xuống dòng
        messageLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Đệm xung quanh nội dung
        messageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(200, 200, 200));

        int maxWidth = 500;
        int labelWidth = Math.min(maxWidth, messageLabel.getPreferredSize().width + 20); // Thêm 20 cho khoảng đệm trái
        // và phải
        messageLabel.setPreferredSize(new Dimension(maxWidth, messageLabel.getPreferredSize().height));
        messagePanel.add(messageLabel, BorderLayout.NORTH);
//        panel_TinNhan.add(messagePanel);
//        panel_TinNhan.revalidate();  // Cập nhật layout sau khi thêm label
//        panel_TinNhan.repaint();     // Vẽ lại JPanel để hiển thị thay đổi

        // Cuộn xuống tin nhắn mới nhất
        SwingUtilities.invokeLater(() -> scrollPane_TinNhan.getVerticalScrollBar()
                .setValue(scrollPane_TinNhan.getVerticalScrollBar().getMaximum()));
    }

    public void addMessage__(String message, String inputMessage) {
        // Tạo JTextArea để hiển thị tin nhắn
        messageArea.setText(messageArea.getText() + "\n" + message + "\n");

        // Cuộn xuống tin nhắn mới nhất
        SwingUtilities.invokeLater(() -> scrollPane_TinNhan.getVerticalScrollBar()
                .setValue(scrollPane_TinNhan.getVerticalScrollBar().getMaximum()));
    }

    public void addMessage(String message, String IO_message) {
        // Tạo JPanel cho tin nhắn
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
//        messagePanel.setAlignmentX(IO_message.equals("in") ? Component.LEFT_ALIGNMENT : Component.RIGHT_ALIGNMENT);

        // Tạo JLabel để chứa nội dung tin nhắn
        int maxWidth = 400;
        JLabel messageLabel = new JLabel(
                "<html><body style='width: " + maxWidth + "px'; word-wrap: break-word;>" + message + "</body></html>");
        messageLabel.setOpaque(true);
        messageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Đặt màu nền khác nhau cho tin nhắn gửi đi và nhận vào
        if (IO_message.equals("in")) {
            messageLabel.setBackground(new Color(173, 216, 230)); // Màu xanh nhạt cho tin nhắn nhận
            messagePanel.add(messageLabel, BorderLayout.WEST);
        } else {
            messageLabel.setBackground(new Color(144, 238, 144)); // Màu xanh lá nhạt cho tin nhắn gửi
            messagePanel.add(messageLabel, BorderLayout.EAST);
        }

        // Đặt kích thước tối đa cho tin nhắn
        messageLabel.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        // Thêm JPanel của tin nhắn vào chatPanel
        panel_TinNhan.add(messagePanel);
        panel_TinNhan.add(Box.createVerticalStrut(5)); // Khoảng cách nhỏ giữa các tin nhắn
        panel_TinNhan.revalidate(); // Làm mới giao diện
        panel_TinNhan.repaint();
        // Tự động cuộn xuống dòng cuối
        SwingUtilities.invokeLater(() -> scrollPane_TinNhan.getVerticalScrollBar()
                .setValue(scrollPane_TinNhan.getVerticalScrollBar().getMaximum()));
    }
}
