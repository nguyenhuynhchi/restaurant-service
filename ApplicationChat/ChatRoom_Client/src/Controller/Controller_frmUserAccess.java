/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.V_FrmChat_Client;
import View.V_FrmUserAccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.WindowConstants;

/**
 *
 * @author Nguyen Huynh Chi
 */
public class Controller_frmUserAccess implements ActionListener{
    private V_FrmUserAccess vFU;
    private V_FrmChat_Client vFC;
    
    
    public Controller_frmUserAccess(V_FrmUserAccess vFU, V_FrmChat_Client vFC){
        this.vFU = vFU;
        this.vFC = vFC;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        
        if(actionCommand.equals("đăng ký")){
            vFU.panel_dangKy.setVisible(true);
            vFU.panel_dangNhap.setVisible(false);
        }
        if(actionCommand.equals("quay lại")){
            vFU.panel_dangKy.setVisible(false);
            vFU.panel_dangNhap.setVisible(true);
        }
        if(actionCommand.equals("OK")){
            vFU.dispose();
            vFC.setVisible(true);
        }
    }
    
}
