package com.autodoor.autoscrew.data;

import android.util.Log;

import com.autodoor.autoscrew.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyi on 2016/1/26.
 */
public class CommandStatus {
    byte[] IDver = new byte[2];
    byte[] sCHK = new byte[2];
    public int x, y, z;


    //状态(a螺丝检测,b刹车检测,c电机加电,d电批运转,e磁吸动作)
    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public boolean e;

    public byte[] byteList = new byte[11];
    public byte status;

    public String SendMessage() {
        return "02 B0 00 B2 ";
    }


    public void ByteArrayConvertToModel(byte[] msg, int s) {
        IDver[0] = msg[s + 0];
        IDver[1] = msg[s + 1];
        sCHK[0] = msg[s + 2];
        sCHK[1] = msg[s + 3];

        x = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 5], msg[s + 4]});
        y = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 7], msg[s + 6]});
        z = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 9], msg[s + 8]});
        status = msg[s + 10];
        StatusByteToModel(status);

    }

    void StatusByteToModel(byte status) {
        a = getBit(status, 5);
        b = getBit(status, 4);
        c = getBit(status, 3);
        d = getBit(status, 2);
        e = getBit(status, 1);
    }

    public boolean getBit(byte byteStatus, int position) {
        return ((byteStatus >> position) & 1) == 1 ? true : false;
    }

    void StatusModelToByte() {
        if (e) {
            status = (byte) (status | (1 << 1));
        } else {
            status = (byte) (status & ~(1 << 1));
        }
        if (d) {
            status = (byte) (status | (1 << 2));
        } else {
            status = (byte) (status & ~(1 << 2));
        }
        if (c) {
            status = (byte) (status | (1 << 3));
        } else {
            status = (byte) (status & ~(1 << 3));
        }
        if (b) {
            status = (byte) (status | (1 << 4));
        } else {
            status = (byte) (status & ~(1 << 4));
        }
        if (a) {
            status = (byte) (status | (1 << 5));
        } else {
            status = (byte) (status & ~(1 << 5));
        }


    }

    //D
    public String writeStatusCommandD() {
        d = !d;
        return BuildCmdString();
    }

    //E
    public String writeStatusCommandE() {
        e = !e;
        return BuildCmdString();
    }

    //A
    public String writeStatusCommandA() {
        a = !a;
        return BuildCmdString();
    }

    //B
    public String writeStatusCommandB() {
        b = !b;
        return BuildCmdString();
    }

    //c
    public String writeStatusCommandC() {
        c = !c;

        return BuildCmdString();
    }

    private String BuildCmdString() {
        StatusModelToByte();

        byte[] tempList = new byte[5];
        tempList[0] = (byte) 0x02;
        tempList[1] = (byte) 0xb1;
        tempList[2] = (byte) 0x01;
        tempList[3] = this.status;


        byte checksum = 0x00;
        for (int i = 0; i < 4; i++) {
            checksum ^= tempList[i];
        }

        tempList[4] = checksum;

        return MainActivity.byteArrayToHexString(tempList);
    }

    public static void TestStatus() {
        CommandStatus commandStatus = new CommandStatus();
        boolean temp = true;
        commandStatus.a = true;
        commandStatus.b = false;
        commandStatus.c = true;
        commandStatus.d = false;
        commandStatus.e = false;
        commandStatus.StatusModelToByte();

        commandStatus.StatusByteToModel(commandStatus.status);

        if (temp == commandStatus.a) temp = true;
        Log.d("TestStatus", "true ? = " + temp);

    }

    public void ModelConvertToByteArray() {


    }


}
