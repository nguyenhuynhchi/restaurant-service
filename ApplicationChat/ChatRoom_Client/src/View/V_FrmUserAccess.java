package View;

import Controller.Controller_frmUserAccess;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class V_FrmUserAccess extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tf_tenDN;
    private JTextField tf_password;
    private JTextField tf_tenDN_DK;
    private JTextField tf_password_DK;
    private JTextField tf_autPassword_DK;
    public JPanel panel_dangKy;
    public JPanel panel_dangNhap;
    public JButton btn_OK;
    public String userName;
//    private V_FrmChat_Client vFC;

//    public V_FrmUserAccess(V_FrmChat_Client vFC) {
//        this.vFC = vFC;
//    }

    /**
     * Launch the application.
     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    V_FrmUserAccess frame = new V_FrmUserAccess();
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
    public V_FrmUserAccess(V_FrmChat_Client vFC) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("LAN Chat Application - Client");

        URL urlIconFrame = V_FrmChat_Client.class.getResource("/Images/App_server.png");
        Image img = Toolkit.getDefaultToolkit().createImage(urlIconFrame);
        setIconImage(img);

        ActionListener ac = new Controller_frmUserAccess(this, vFC);

        setTitle("Chào!! Đăng nhập/Đăng ký");
        setBounds(100, 100, 500, 245);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        panel_dangNhap = new JPanel();
        panel_dangNhap.setBounds(0, 0, 486, 210);
        contentPane.add(panel_dangNhap);
        panel_dangNhap.setLayout(null);

        JLabel lbl_tenDN = new JLabel("Tên đăng nhập:");
        lbl_tenDN.setBounds(60, 55, 120, 20);
        lbl_tenDN.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_tenDN.setFont(new Font("Tahoma", Font.BOLD, 15));
        panel_dangNhap.add(lbl_tenDN);

        JLabel lbl_password = new JLabel("Mật khẩu:");
        lbl_password.setBounds(104, 95, 76, 20);
        lbl_password.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_password.setFont(new Font("Tahoma", Font.BOLD, 15));
        panel_dangNhap.add(lbl_password);

        tf_tenDN = new JTextField();
        tf_tenDN.setBounds(184, 50, 210, 30);
        tf_tenDN.setFont(new Font("Tahoma", Font.BOLD, 12));
        tf_tenDN.setColumns(10);
        tf_tenDN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf_tenDN.getText().length() >= 30) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        userName = tf_tenDN.getText();
        panel_dangNhap.add(tf_tenDN);

        tf_password = new JTextField();
        tf_password.setBounds(184, 89, 210, 30);
        tf_password.setFont(new Font("Tahoma", Font.BOLD, 12));
        tf_password.setColumns(10);
        tf_password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf_password.getText().length() >= 50) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        panel_dangNhap.add(tf_password);

        btn_OK = new JButton("OK");
        btn_OK.setBounds(110, 155, 55, 27);
        btn_OK.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_OK.setActionCommand("OK");
        btn_OK.addActionListener(ac);

        panel_dangNhap.add(btn_OK);

        JButton btn_dong = new JButton("Đóng");
        btn_dong.setBounds(186, 155, 83, 27);
        btn_dong.setFont(new Font("Tahoma", Font.BOLD, 15));
        panel_dangNhap.add(btn_dong);
        btn_dong.setActionCommand("cancle");

        JButton btn_dangKy = new JButton("Đăng ký");
        btn_dangKy.setBounds(352, 155, 95, 27);
        btn_dangKy.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_dangKy.setActionCommand("đăng ký");
        btn_dangKy.addActionListener(ac);
        panel_dangNhap.add(btn_dangKy);

        JLabel lbl_dangNhap = new JLabel("Đăng nhập");
        lbl_dangNhap.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_dangNhap.setFont(new Font("Tahoma", Font.BOLD, 17));
        lbl_dangNhap.setBounds(193, 10, 100, 30);
        panel_dangNhap.add(lbl_dangNhap);

        panel_dangKy = new JPanel();
        panel_dangKy.setLayout(null);
        panel_dangKy.setBounds(0, 0, 486, 210);
        panel_dangKy.setVisible(false);
        contentPane.add(panel_dangKy);

        JLabel lbl_tenDN_DK = new JLabel("Tên đăng nhập:");
        lbl_tenDN_DK.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_tenDN_DK.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl_tenDN_DK.setBounds(63, 55, 120, 20);
        panel_dangKy.add(lbl_tenDN_DK);

        JLabel lbl_password_DK = new JLabel("Mật khẩu:");
        lbl_password_DK.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_password_DK.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl_password_DK.setBounds(104, 95, 76, 20);
        panel_dangKy.add(lbl_password_DK);

        tf_tenDN_DK = new JTextField();
        tf_tenDN_DK.setFont(new Font("Tahoma", Font.BOLD, 12));
        tf_tenDN_DK.setColumns(10);
        tf_tenDN_DK.setBounds(184, 50, 210, 30);
        tf_tenDN_DK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf_tenDN_DK.getText().length() >= 30) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        panel_dangKy.add(tf_tenDN_DK);

        tf_password_DK = new JTextField();
        tf_password_DK.setFont(new Font("Tahoma", Font.BOLD, 12));
        tf_password_DK.setColumns(10);
        tf_password_DK.setBounds(184, 89, 210, 30);
        tf_password_DK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf_password_DK.getText().length() >= 50) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        panel_dangKy.add(tf_password_DK);

        JButton btn_tao = new JButton("Tạo");
        btn_tao.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_tao.setBounds(137, 173, 83, 27);
        btn_tao.setActionCommand("tạo");
        panel_dangKy.add(btn_tao);

        JButton btn_quayLai = new JButton("Quay lại");
        btn_quayLai.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn_quayLai.setBounds(241, 173, 100, 27);
        btn_quayLai.setActionCommand("quay lại");
        btn_quayLai.addActionListener(ac);
        panel_dangKy.add(btn_quayLai);

        JLabel lbl_dangKy = new JLabel("Đăng ký");
        lbl_dangKy.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_dangKy.setFont(new Font("Tahoma", Font.BOLD, 17));
        lbl_dangKy.setBounds(193, 10, 100, 30);
        panel_dangKy.add(lbl_dangKy);

        JLabel lbl_autPassword_DK = new JLabel("Xác nhận mật khẩu:");
        lbl_autPassword_DK.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_autPassword_DK.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl_autPassword_DK.setBounds(27, 131, 160, 20);
        panel_dangKy.add(lbl_autPassword_DK);

        tf_autPassword_DK = new JTextField();
        tf_autPassword_DK.setFont(new Font("Tahoma", Font.BOLD, 12));
        tf_autPassword_DK.setColumns(10);
        tf_autPassword_DK.setBounds(184, 125, 210, 30);
        tf_autPassword_DK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tf_autPassword_DK.getText().length() >= 50) {
                    e.consume(); // Ngăn không cho nhập thêm ký tự
                }
            }
        });
        panel_dangKy.add(tf_autPassword_DK);
    }
}
