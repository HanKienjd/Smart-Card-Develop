package connect_ex_gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kienp
 */
public class SmartCardWord {
    public static final byte[] AID_APPLET = {(byte)0x99, (byte)0x88, (byte)0x77, (byte)0x66, (0x55), (byte)0x00};
    private Card card;
    private TerminalFactory factory;
    private CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private ResponseAPDU response;

    public SmartCardWord() {

    }

    public static void main(String[] args){


    }

    public boolean connectCard(){
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=0");
            channel = card.getBasicChannel();
            if(channel == null){
                return false;
            }
            response = channel.transmit(new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, AID_APPLET));
            String check = Integer.toHexString(response.getSW());
            if(check.equals("9000")){
                return true;
            }
            else if(check.equals("6400")){
                JOptionPane.showMessageDialog(null, "Thẻ đã bị vô hiệu hoá");
                return true;
            }else {
                return  false;
            }
        }
        catch(Exception ex){

        }
        return false;
    }

    public boolean disconnect(){
        try{
            card.disconnect(false);
            return true;
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
        return false;
    }

    private void bt_cntActionPerformed(ActionEvent evt){
        if(!isConnect){
            if(card.connectCard()){
                isConnect = true;
                bt_cnt.setText("Disconnect");
                bt_cnt.setForeground(Color.RED);
                bt_show.setEnabled(true);
            }else{
                JOptionPane.showMessageDialog(this, "Chưa connect được đến applet");
                isConnect = false;
                bt_cnt.setText("Connect");
                bt_cnt.setForeground(Color.BLACK);
            }
        }else {
            if(card.disconnect()){
                isConnect = false;
                bt_btn.setText("Connect");
                bt_btn.setForeground(Color.BLACK);
                btn_show.setEnabled(false);
                resetButton();
            }
        }
    }

    
}

