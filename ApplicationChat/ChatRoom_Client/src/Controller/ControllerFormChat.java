package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ChatRoom_Client.ChatClient;
import ChatRoom_Client.MessageSender;
import View.V_FrmChat_Client;

public class ControllerFormChat implements ActionListener {

    private V_FrmChat_Client vFC;
//    private ChatClient chatClient;

//    public ControllerFormChat(V_FrmChat_Client vFC, ChatClient chatClient) {
//        this.vFC = vFC;
//        this.chatClient = chatClient;
//    }
    
    public ControllerFormChat(V_FrmChat_Client vFC) {
        this.vFC = vFC;
//        this.chatClient = new ChatClient(vFC);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String src = e.getActionCommand();
        processAction(src);
    }
    
    private void processAction(String actionCommand) {
//	System.out.println("\nPRESS " + actionCommand + " !!!");
//        if(actionCommand.equals("Gửi")){
//            vFC.guiTin(messageSender);
//        }
    }



}
