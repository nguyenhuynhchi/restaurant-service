/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatRoom_Client;

import java.io.OutputStream;
import java.util.Scanner;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class MessageSender implements Runnable {

    private OutputStream output;

    public MessageSender(OutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        try {
            while (true) {
                // Đọc dữ liệu từ người dùng và gửi lên server
                String message = sc.nextLine();
                output.write((message + "\n").getBytes());
                output.flush();
            }
        } catch (Exception e) {
            System.out.println("Lỗi ở ChatMessageSender");
        } finally {
            sc.close();  // Đảm bảo đóng Scanner khi không dùng nữa
        }
    }
}
