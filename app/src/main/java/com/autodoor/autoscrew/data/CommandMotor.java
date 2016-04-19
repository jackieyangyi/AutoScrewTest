package com.autodoor.autoscrew.data;

import com.autodoor.autoscrew.MainActivity;

import java.util.ArrayList;

/**
 * Created by yangyi on 2016/2/16.
 */
public class CommandMotor {

    //运行到坐标 02 0xC6 6 X Y Z chk
    public String RunToXYZString(int x, int y, int z) {
        ArrayList<Byte> bList = new ArrayList<>();
        bList.add((byte) 0x02);
        bList.add((byte) 0xC6);
        bList.add((byte) 0x06);

        bList.add(CommandHelper.intToByteArray(x)[3]);
        bList.add(CommandHelper.intToByteArray(x)[2]);
        bList.add(CommandHelper.intToByteArray(y)[3]);
        bList.add(CommandHelper.intToByteArray(y)[2]);
        bList.add(CommandHelper.intToByteArray(z)[3]);
        bList.add(CommandHelper.intToByteArray(z)[2]);

        byte chksum = CalCheckSum(bList);
        bList.add(chksum);

        byte [] list = ConvertToByteArray(bList);
        return MainActivity.byteArrayToHexString(list);
    }

    public String GetLeftScrewString(int x, int z) {

        ArrayList<Byte> bList = new ArrayList<>();
        bList.add((byte) 0x02);
        bList.add((byte) 0xE4);
        bList.add((byte) 0x04);

        bList.add(CommandHelper.intToByteArray(x)[3]);
        bList.add(CommandHelper.intToByteArray(x)[2]);
        bList.add(CommandHelper.intToByteArray(z)[3]);
        bList.add(CommandHelper.intToByteArray(z)[2]);

        byte chksum = CalCheckSum(bList);
        bList.add(chksum);

        byte [] list = ConvertToByteArray(bList);
        return MainActivity.byteArrayToHexString(list);

    }

    public String SetLeftScrewString(int x, int y, int z) {

        ArrayList<Byte> bList = new ArrayList<>();
        bList.add((byte) 0x02);
        bList.add((byte) 0xE6);
        bList.add((byte) 0x06);

        bList.add(CommandHelper.intToByteArray(x)[3]);
        bList.add(CommandHelper.intToByteArray(x)[2]);
        bList.add(CommandHelper.intToByteArray(y)[3]);
        bList.add(CommandHelper.intToByteArray(y)[2]);
        bList.add(CommandHelper.intToByteArray(z)[3]);
        bList.add(CommandHelper.intToByteArray(z)[2]);

        byte chksum = CalCheckSum(bList);
        bList.add(chksum);

        byte [] list = ConvertToByteArray(bList);
        return MainActivity.byteArrayToHexString(list);


    }

    public String GetRightScrewString(int x, int z) {

        ArrayList<Byte> bList = new ArrayList<>();
        bList.add((byte) 0x02);
        bList.add((byte) 0xF4);
        bList.add((byte) 0x04);

        bList.add(CommandHelper.intToByteArray(x)[3]);
        bList.add(CommandHelper.intToByteArray(x)[2]);
        bList.add(CommandHelper.intToByteArray(z)[3]);
        bList.add(CommandHelper.intToByteArray(z)[2]);

        byte chksum = CalCheckSum(bList);
        bList.add(chksum);

        byte [] list = ConvertToByteArray(bList);
        return MainActivity.byteArrayToHexString(list);
    }

    public String SetRightScrewString(int x, int y, int z) {
        ArrayList<Byte> bList = new ArrayList<>();
        bList.add((byte) 0x02);
        bList.add((byte) 0xF6);
        bList.add((byte) 0x06);

        bList.add(CommandHelper.intToByteArray(x)[3]);
        bList.add(CommandHelper.intToByteArray(x)[2]);
        bList.add(CommandHelper.intToByteArray(y)[3]);
        bList.add(CommandHelper.intToByteArray(y)[2]);
        bList.add(CommandHelper.intToByteArray(z)[3]);
        bList.add(CommandHelper.intToByteArray(z)[2]);

        byte chksum = CalCheckSum(bList);
        bList.add(chksum);

        byte [] list = ConvertToByteArray(bList);
        return MainActivity.byteArrayToHexString(list);
    }

    byte CalCheckSum(ArrayList<Byte> bList) {
        byte result = 0x00;
        for (Byte b : bList) {
            //result = (byte)(result^b.byteValue());
            result ^=b.byteValue();
        }
        return result;
    }

    byte[] ConvertToByteArray(ArrayList<Byte> bList){
        byte[] temp = new byte[bList.size()];
        int i=0;
        for(Byte b : bList){
            temp[i] = b.byteValue();
            i++;
        }
        return temp;
    }

}
