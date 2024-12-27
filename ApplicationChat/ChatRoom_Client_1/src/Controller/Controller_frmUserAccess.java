/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import javax.swing.JOptionPane;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class Controller_frmUserAccess implements ActionListener {

    private V_FrmUserAccess vFU;
    private V_FrmChat_Client vFC;

    public String port;
    public String userID;
    public String userName;
    public String password;

    public Controller_frmUserAccess(V_FrmUserAccess vFU, V_FrmChat_Client vFC) {
        this.vFU = vFU;
        this.vFC = vFC;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("đăng ký")) {  // Chuyển qua phần đăng ký
            vFU.panel_dangKy.setVisible(true);
            vFU.panel_dangNhap.setVisible(false);
            vFU.tf_tenDN_DK.setText("");
            vFU.tf_password_DK.setText("");
            vFU.tf_autPassword_DK.setText("");
        }

        if (actionCommand.equals("tạo")) {
            createAccess();
        }

        if (actionCommand.equals("quay lại")) {  // Quay lại đăng nhập
            vFU.panel_dangKy.setVisible(false);
            vFU.panel_dangNhap.setVisible(true);
        }

        if (actionCommand.equals("đóng")) {  // đóng ứng dụng
            vFU.dispose();
            System.exit(0);
        }

        if (actionCommand.equals("OK")) {  // Kiểm tra thông tin tài khoản đăng nhập, đúng thì vào ứng dụng
            logIn();
        }
    }

    public void createAccess() {
        this.port = vFU.tf_port_DK.getText().trim();
        String uuid = UUID.randomUUID().toString();
        this.userID = uuid.substring(0, 3);
        this.userName = vFU.tf_tenDN_DK.getText().trim();
        this.password = vFU.tf_password_DK.getText().trim();
        String autPassword = vFU.tf_autPassword_DK.getText().trim();
        boolean flat = false;
//            System.out.println("passwword: " + password + "\nautpassword: " + autPassword);

        if (!port.isEmpty()) {
            try {
                int portNumber = Integer.parseInt(port);
                vFC.port = portNumber;
                flat = true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vFU, "Vui lòng nhập cho cổng là một số nguyên hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                flat = false;
                vFU.tf_port_DK.setText("");
            }
        }
        if (port.isEmpty()) {
            // Hiển thị thông báo lỗi nếu port chưa được nhập
            flat = false;
            JOptionPane.showMessageDialog(vFU, "Vui lòng nhập số cổng của server!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
        }
        if (userName.isEmpty()) {
            // Hiển thị thông báo lỗi nếu tên chưa được nhập
            flat = false;
            JOptionPane.showMessageDialog(vFU, "Vui lòng nhập tên cho tài khoản!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
        }
        if (password.isEmpty()) {
            // Hiển thị thông báo lỗi nếu mật khẩu chưa được nhập
            flat = false;
            JOptionPane.showMessageDialog(vFU, "Vui lòng nhập mật khẩu cho tài khoản!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
        } else if (!autPassword.equals(password) || autPassword.isEmpty()) {
            // Hiển thị thông báo lỗi nếu mật khẩu xác nhận không giống
            flat = false;
            JOptionPane.showMessageDialog(vFU, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        if (flat) {
            // Hiển thị thông báo đăng ký thành công và quay lại panel để đăng nhập
            JOptionPane.showMessageDialog(vFU, "Đăng ký thành công! \nVào sử dụng thoi", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            vFU.newCreate = true; // Là tài khoản mới đăng ký
            vFC.updateUserInfo(userID, userName, password, port); // Gán tên đăng nhập cho V_FrmChat_Client
            vFU.dispose();          // Đóng form V_FrmUserAccess
            vFC.setVisible(true);   // Hiển thị form V_FrmChat_Client
            vFC.connect = true;
            vFU.connect = true;     // Cập nhật trạng thái kết nối
        }
    }

    public void logIn() {
        this.port = vFU.tf_port.getText().trim();
        this.userName = vFU.tf_tenDN.getText().trim();
        this.password = vFU.tf_password.getText().trim();
        if (userName.isEmpty()) {
            // Hiển thị thông báo lỗi nếu tên đăng nhập trống
            JOptionPane.showMessageDialog(vFU, "Vui lòng nhập tên đăng nhập!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
        } else if (port.isEmpty()) {
            JOptionPane.showMessageDialog(vFU, "Vui lòng nhập số cổng của server!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
        } else if (password.isEmpty()) {
            JOptionPane.showMessageDialog(vFU, "Vui lòng nhập mật khẩu!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
        } else if (!userName.isEmpty() && !port.isEmpty() && !password.isEmpty()) {
            try {
                vFC.updateUserInfo(null, userName, password, port); // (SỬA)Gán tên đăng nhập cho V_FrmChat_Client
                int portNumber = Integer.parseInt(port);
                vFC.port = portNumber;
                vFU.newCreate = false;
//                vFU.dispose();          // Đóng form V_FrmUserAccess
//                vFC.setVisible(true);   // Hiển thị form V_FrmChat_Client
                vFU.connect = true;     // Cập nhật trạng thái kết nối
                vFC.connect = true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vFU, "Vui lòng nhập cho cổng là một số nguyên hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
