/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class Controller_frmUserAccess implements ActionListener {

    private V_FrmUserAccess vFU;
    private V_FrmChat_Client vFC;

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
            String userName = vFU.tf_tenDN_DK.getText().trim();
            String password = vFU.tf_password_DK.getText().trim();
            String autPassword = vFU.tf_autPassword_DK.getText().trim();
            System.out.println("passwword: " + password + "\nautpassword: " + autPassword);
            if (userName.isEmpty()) {
                // Hiển thị thông báo lỗi nếu mật khẩu xác nhận không giống
                JOptionPane.showMessageDialog(vFU, "Vui lòng nhập tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else if (!autPassword.equals(password)) {
                // Hiển thị thông báo lỗi nếu mật khẩu xác nhận không giống
                JOptionPane.showMessageDialog(vFU, "Sai mật khẩu xác nhận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

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

            String port = vFU.tf_port.getText().trim();
            System.out.println(port);
            String userName = vFU.tf_tenDN.getText().trim();
            if (!userName.isEmpty() && !port.isEmpty()) {
                try {
                    vFC.updateUserName(userName); // Gán tên đăng nhập cho V_FrmChat_Client
                    int portNumber = Integer.parseInt(port); 
                    vFC.port = portNumber;
                    vFU.dispose();          // Đóng form V_FrmUserAccess
                    vFC.setVisible(true);   // Hiển thị form V_FrmChat_Client
                    vFU.connect = true;     // Cập nhật trạng thái kết nối
                    vFC.connect = true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vFU, "Vui lòng nhập một số nguyên hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                }
            } else if (userName.isEmpty()) {
                // Hiển thị thông báo lỗi nếu tên đăng nhập trống
                JOptionPane.showMessageDialog(vFU, "Vui lòng nhập tên đăng nhập!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
            } else if (port.isEmpty()) {
                JOptionPane.showMessageDialog(vFU, "Vui lòng nhập số cổng của server!", "Lỗi nhập thiếu", JOptionPane.ERROR_MESSAGE);
            }

        }

    }
}
