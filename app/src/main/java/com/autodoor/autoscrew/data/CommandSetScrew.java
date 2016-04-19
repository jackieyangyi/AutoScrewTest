package com.autodoor.autoscrew.data;


import android.util.Log;

import com.autodoor.autoscrew.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyi_it on 2016-1-20.
 */
public class CommandSetScrew {

    public byte[] bytesList = new byte[1024];

    //可用总点数
    public int totalPoint = 127;
    public String name = "A000001";
    public List<SetScrewPoint> pointList = new ArrayList<SetScrewPoint>();


    public void ByteArrayToModel(byte[] bytes, int s) {

        totalPoint = bytes[s + 0];
        int ia = bytes[s + 1];
        char ca = (char) bytes[s + 1];
        Log.d("test", "int char=" + ca + " = " + ia);
        char[] chars = new char[]{(char) bytes[s + 1], (char) bytes[s + 2], (char) bytes[s + 3],
                (char) bytes[s + 4], (char) bytes[s + 5], (char) bytes[s + 6], (char) bytes[s + 7]};
        name = String.valueOf(chars);

        pointList.clear();
        for (int i = s + 8; i < bytes.length-s; i += 8) {
            SetScrewPoint point = new SetScrewPoint();
            point.x = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, bytes[i + 1], bytes[i]});
            point.y = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, bytes[i + 3], bytes[i + 2]});
            point.z = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, bytes[i + 5], bytes[i + 4]});
            point.c = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, bytes[i + 7], bytes[i + 6]});
            if (point.x > 0 || point.y > 0 || point.z > 0) {
                pointList.add(point);
            }
        }


    }

    public void ModelToByteArray() {
        bytesList[0] = CommandHelper.intToByteArray(totalPoint)[3];

        char[] chars = name.toCharArray();
        bytesList[1] = (byte) chars[0];
        Log.d("test", "byte ,char" + bytesList[1] + " == " + chars[0]);
        bytesList[2] = (byte) chars[1];
        bytesList[3] = (byte) chars[2];
        bytesList[4] = (byte) chars[3];
        bytesList[5] = (byte) chars[4];
        bytesList[6] = (byte) chars[5];
        bytesList[7] = (byte) chars[6];

        int j = 8;
        for (int i = 0; i < pointList.size(); i++, j += 8) {

            bytesList[j] = CommandHelper.intToByteArray(pointList.get(i).x)[3];
            bytesList[j + 1] = CommandHelper.intToByteArray(pointList.get(i).x)[2];
            bytesList[j + 2] = CommandHelper.intToByteArray(pointList.get(i).y)[3];
            bytesList[j + 3] = CommandHelper.intToByteArray(pointList.get(i).y)[2];
            bytesList[j + 4] = CommandHelper.intToByteArray(pointList.get(i).z)[3];
            bytesList[j + 5] = CommandHelper.intToByteArray(pointList.get(i).z)[2];
            bytesList[j + 6] = CommandHelper.intToByteArray(pointList.get(i).c)[3];
            bytesList[j + 7] = CommandHelper.intToByteArray(pointList.get(i).c)[2];
        }


    }


    public static void TestConvert() {
        CommandSetScrew testCmd = new CommandSetScrew();
        testCmd.totalPoint = 2;
        testCmd.name = "A000001";

        SetScrewPoint point = new SetScrewPoint();
        point.x = 3000;
        point.y = 2000;
        point.z = 300;
        point.c = 1;
        testCmd.pointList.add(point);

        point = new SetScrewPoint();
        point.x = 3001;
        point.y = 2001;
        point.z = 301;
        point.c = 0;
        testCmd.pointList.add(point);

        /*for (int i = 0; i < 127 - 2; i++) {
            SetScrewPoint empyPoint = new SetScrewPoint();
            testCmd.pointList.add(empyPoint);
        }*/

        testCmd.ModelToByteArray();
        testCmd.ByteArrayToModel(testCmd.bytesList, 0);
        Log.d("test", "totalPoint == " + testCmd.totalPoint);


    }

    //获取保存指令的字节发送
    public String GetSendStringForSaving(int formularIndex) {
        int byteType = 0x90;
        byteType = byteType + formularIndex;

        int length = (pointList.size() + 1) * 8;
        if (length >= 128) {

            int t = length / 128;
            int m = length % 128;
            if (m > 0) {
                t = t + 1;
            }
            length = 128 + t;
        }

        byte[] tempList = new byte[length + 4];
        tempList[0] = (byte) 0x02;
        tempList[1] = (byte) byteType;


        tempList[2] = (byte) length;
        //
        System.arraycopy(bytesList, 0, tempList, 3, length);


        byte checksum = 0x00;
        for (int i = 0; i < tempList.length - 1; i++) {
            checksum ^= tempList[i];
        }
        tempList[tempList.length - 1] = checksum;

        return MainActivity.byteArrayToHexString(tempList);
    }


    //02 80 00 8x, 02 81 00 8x,
    public static String GetSendString(int formularIndex) {
        int byteType = 0x80;
        byteType = byteType + formularIndex;


        byte[] tempList = new byte[4];
        tempList[0] = (byte) 0x02;
        tempList[1] = (byte) byteType;
        tempList[2] = (byte) 0x00;


        byte checksum = 0x00;
        for (int i = 0; i < 3; i++) {
            checksum ^= tempList[i];
        }

        tempList[3] = checksum;

        return MainActivity.byteArrayToHexString(tempList);
    }


}




