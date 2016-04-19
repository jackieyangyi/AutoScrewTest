package com.autodoor.autoscrew.data;

import android.util.Log;

/**
 * 1、把命令模型类转换成字节流
 * 2、把字节流转换成命令模型类
 * Created by yangyi on 2016/1/23.
 */
public class CommandManager {

    public byte[] byteReply;
    public CommandSys commandSys;
    public CommandStatus commandStatus;
    public CommandSetScrew commandSetScrew;

    void SendMessage(byte msgID) {
        switch (msgID) {
            case (byte) 0x80:
                Log.d("SendMessage", "0x80");

                break;
            case (byte) 0x81:
                Log.d("SendMessage", "0x81");
                break;
            case (byte) 0x91:
                Log.d("SendMessage", "0x91");
                break;
            default:
                Log.d("SendMessage", "unknow command");
                break;

        }

    }


    void ReadMessage(byte[] msgBytes) {
        byte msgId = msgBytes[1];
        switch (msgId) {
            case (byte) 0x80:
                Log.d("ReadMessage", "0x80");
                break;
            case (byte) 0x81:
                Log.d("ReadMessage", "0x81");
                break;
            case (byte) 0x91:
                Log.d("ReadMessage", "0x91");
                break;
            default:
                Log.d("ReadMessage", "unknow command");
                break;

        }

    }


    public CommandManager GetCommandInfo(byte[] message) {

        if (message[0] == (byte) 0x02 && message[1] == (byte) 0x80) {
            CommandSys commandSys = new CommandSys();
            commandSys.ByteArrayConvertToModel(message, 3);

            this.commandSys = commandSys;

        } else if (message[0] == (byte) 0x02 && message[1] == (byte) 0xB0) {
            CommandStatus commandStatus = new CommandStatus();
            commandStatus.ByteArrayConvertToModel(message, 3);

            this.commandStatus = commandStatus;

        }else if (message[0] == (byte) 0x02 && message[1] >= (byte) 0x80) {
            CommandSetScrew commandSetScrew = new CommandSetScrew();
            commandSetScrew.ByteArrayToModel(message,3);

            this.commandSetScrew = commandSetScrew;

        }

        if (message[0] == (byte) 0x13) {
            this.byteReply = message;
        }
        if (message[0] == (byte) 0x11) {
            this.byteReply = message;
        }
        if (message[0] == (byte) 0x15) {
            this.byteReply = message;
        }
        if (message[0] == (byte) 0x06) {
            this.byteReply = message;
        }

        return this;
    }


    public boolean TestCheckByte() {
        byte[] bytes = new byte[]{0x02, (byte) 0x80, 0x00, (byte) 0x82};
        if (CommandHelper.CheckCommandBytes(bytes, 4) != null) {
            return true;
        } else {
            return false;
        }
    }

}
