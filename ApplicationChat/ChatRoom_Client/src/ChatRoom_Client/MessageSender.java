/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import View.V_FrmChat_Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class MessageSender implements Runnable {

    private OutputStream output;
    private V_FrmChat_Client vFC;

    public MessageSender(OutputStream output, V_FrmChat_Client vFC) {
        this.output = output;
        this.vFC = vFC;
    }

    @Override
    public void run() {
//        Scanner sc = new Scanner(System.in);
//        try {
//            while (true) {
//                // Đọc dữ liệu từ người dùng và gửi lên server
////                String message = sc.nextLine();
////                vFC.textField.setText(sc.nextLine());
//                String message = vFC.textField.getText();
//                output.write((message + "\n").getBytes());
//                output.flush();
//                vFC.addMessage(message, "out");
//                vFC.textField.setText("");
//            }
//        } catch (Exception e) {
//            System.out.println("Lỗi ở ChatMessageSender");
//        } finally {
//            sc.close();  // Đảm bảo đóng Scanner khi không dùng nữa
//        }
        vFC.btn_guiTin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (vFC.textField.getText().isEmpty()) {
                    return;
                }
                try {
                    String message = vFC.textField.getText();
                    output.write((message + "\n").getBytes());
                    output.flush();
                    vFC.addMessage(message, "out"); // thêm tin nhắn vào panel_tinnhan
                    vFC.textField.setText("");
                } catch (Exception e) {
                    System.out.println("Lỗi ở ChatMessageSender(khi gửi tin nhắn)");
                }
            }
        });
    }

//    public void sendMessage() {
//        try {
//            String message = vFC.textField.getText();
//            output.write((message + "\n").getBytes());
//            output.flush();
//            vFC.addMessage(message, "out"); // thêm tin nhắn vào panel_tinnhan
//            vFC.textField.setText("");
//        } catch (Exception e) {
//            System.out.println("Lỗi ở ChatMessageSender");
//        }
//    }
}
