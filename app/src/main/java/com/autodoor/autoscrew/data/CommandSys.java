package com.autodoor.autoscrew.data;

import android.util.Log;

import com.autodoor.autoscrew.MainActivity;

import java.math.BigInteger;

/**
 * Created by yangyi on 2016/1/14.
 */
public class CommandSys {

    /// <summary>
    /// 字节组
    /// </summary>
    public byte[] byteList;

    public CommandSys(byte[] bytes) {
        byteList = bytes;
    }

    public CommandSys() {

    }


    //region Fields
    /// <summary>
    /// 配方
    /// </summary>
    public int sysPara_formulaNum;

    /// <summary>
    /// 产量
    /// </summary>
    public int sysPara_outPut;

    /// <summary>
    /// 取螺丝端
    /// </summary>
    public int arc_getScrewSide;

    /// <summary>
    /// 锁螺丝端
    /// </summary>
    public int arc_lockScrewSide;

    /// <summary>
    /// on/off 开关
    /// </summary>
    public boolean arc_onOrOff;

    /// <summary>
    /// 负限位X
    /// </summary>
    public int sys_minusX;
    public int sys_minusY;
    public int sys_minusZ;

    /// <summary>
    /// 正限位X
    /// </summary>
    public int sys_plusX;
    public int sys_plusY;
    public int sys_plusZ;

    /// <summary>
    /// 步距
    /// </summary>
    public int sys_step;

    public String getSys_stepStr() {
        return String.format("%.2f", (double) sys_step / 1000);
    }

    /// <summary>
    /// 电机速度X
    /// </summary>
    public int para1_motorSpeedX;
    public int para1_motorSpeedY;
    public int para1_motorSpeedZ;
    public int para1_motorSpeedZGetScrew;
    public int para1_motorSpeedZLockScrew;

    /// <summary>
    /// 取螺丝时间
    /// </summary>
    public int para2_getScrewTime;

    public String GetPara2_getScrewTime() {
        return String.format("%1$d", para2_getScrewTime * 12);
    }

    /// <summary>
    /// 滑牙限时
    /// </summary>
    public int para3_screwLooseTime;

    public String GetPara3_screwLooseTime() {
        return String.format("%1$d", para3_screwLooseTime * 12);
    }

    /// <summary>
    /// 左仓参数螺丝长度
    /// </summary>
    public int para4_leftBoxScrewLength;
    public int para4_leftBoxGetScrewHeight;
    public int para4_leftBoxX;
    public int para4_leftBoxZ;
    public int para4_leftBoxForceDegree;

    /// <summary>
    /// 右仓参数螺丝长度
    /// </summary>
    public int para5_rightBoxScrewLength;
    public int para5_rightBoxGetScrewHeight;
    public int para5_rightBoxX;
    public int para5_rightBoxZ;
    public int para5_rightBoxForceDegree;
    //endregion


    public void ByteArrayConvertToModel(byte[] msg, int s) {

        sysPara_formulaNum = msg[s + 0];
        Log.d("test", "sysPara_formulaNum=" + sysPara_formulaNum);
        sysPara_outPut = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 2], msg[s + 1]});
        Log.d("test", "sysPara_outPut=" + sysPara_outPut);
        arc_getScrewSide = msg[s + 3];
        arc_lockScrewSide = msg[s + 4];
        arc_onOrOff = msg[s + 5] == 0x01 ? true : false;

        sys_minusX = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 7], msg[s + 6]});
        Log.d("test", "sys_minusX=" + sys_minusX);
        sys_minusY = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 9], msg[s + 8]});
        sys_minusZ = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 11], msg[s + 10]});

        sys_plusX = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 13], msg[s + 12]});
        sys_plusY = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 15], msg[s + 14]});
        sys_plusZ = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 17], msg[s + 16]});

        sys_step = msg[s + 18];

        para1_motorSpeedX = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, 0x00,  msg[s + 19]});
        para1_motorSpeedY = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, 0x00,  msg[s + 20]});
        para1_motorSpeedZ = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, 0x00,  msg[s + 21]});
        para1_motorSpeedZGetScrew = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, 0x00,  msg[s + 22]});
        para1_motorSpeedZLockScrew =  CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, 0x00,  msg[s + 23]});

        para2_getScrewTime = msg[s + 24];
        para3_screwLooseTime = msg[s + 25];

        para4_leftBoxScrewLength = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 27], msg[s + 26]});
        para4_leftBoxGetScrewHeight = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 29], msg[s + 28]});
        para4_leftBoxX = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 31], msg[s + 30]});
        para4_leftBoxZ = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 33], msg[s + 32]});
        para4_leftBoxForceDegree = msg[s + 34];

        para5_rightBoxScrewLength = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 36], msg[s + 35]});
        para5_rightBoxGetScrewHeight = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 38], msg[s + 37]});
        para5_rightBoxX = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 40], msg[s + 39]});
        para5_rightBoxZ = CommandHelper.byteArrayToInt(new byte[]{0x00, 0x00, msg[s + 42], msg[s + 41]});
        para5_rightBoxForceDegree = msg[s + 43];


    }

    public void ModelCovertToByteArray() {
        byte[] bytes = new byte[64];
        //系统参数区
        bytes[0] = CommandHelper.intToByteArray(sysPara_formulaNum)[3];
        bytes[1] = CommandHelper.intToByteArray(sysPara_outPut)[3];
        bytes[2] = CommandHelper.intToByteArray(sysPara_outPut)[2];
        //圆弧设置
        bytes[3] = CommandHelper.intToByteArray(arc_getScrewSide)[3];
        bytes[4] = CommandHelper.intToByteArray(arc_lockScrewSide)[3];
        bytes[5] = arc_onOrOff == true ? (byte) 0x01 : (byte) 0x00;
        //系统设置
        bytes[6] = CommandHelper.intToByteArray(sys_minusX)[3];
        bytes[7] = CommandHelper.intToByteArray(sys_minusX)[2];
        bytes[8] = CommandHelper.intToByteArray(sys_minusY)[3];
        bytes[9] = CommandHelper.intToByteArray(sys_minusY)[2];
        bytes[10] = CommandHelper.intToByteArray(sys_minusZ)[3];
        bytes[11] = CommandHelper.intToByteArray(sys_minusZ)[2];
        bytes[12] = CommandHelper.intToByteArray(sys_plusX)[3];
        bytes[13] = CommandHelper.intToByteArray(sys_plusX)[2];
        bytes[14] = CommandHelper.intToByteArray(sys_plusY)[3];
        bytes[15] = CommandHelper.intToByteArray(sys_plusY)[2];
        bytes[16] = CommandHelper.intToByteArray(sys_plusZ)[3];
        bytes[17] = CommandHelper.intToByteArray(sys_plusZ)[2];
        bytes[18] = CommandHelper.intToByteArray(sys_step)[3];

        //参数设置 1
        bytes[19] = CommandHelper.intToByteArray(para1_motorSpeedX)[3];
        bytes[20] = CommandHelper.intToByteArray(para1_motorSpeedY)[3];
        bytes[21] = CommandHelper.intToByteArray(para1_motorSpeedZ)[3];
        bytes[22] = CommandHelper.intToByteArray(para1_motorSpeedZGetScrew)[3];
        bytes[23] = CommandHelper.intToByteArray(para1_motorSpeedZLockScrew)[3];

        //2
        bytes[24] = CommandHelper.intToByteArray(para2_getScrewTime)[3];
        //3
        bytes[25] = CommandHelper.intToByteArray(para3_screwLooseTime)[3];
        //4左仓参数
        bytes[26] = CommandHelper.intToByteArray(para4_leftBoxScrewLength)[3];
        bytes[27] = CommandHelper.intToByteArray(para4_leftBoxScrewLength)[2];
        bytes[28] = CommandHelper.intToByteArray(para4_leftBoxGetScrewHeight)[3];
        bytes[29] = CommandHelper.intToByteArray(para4_leftBoxGetScrewHeight)[2];
        bytes[30] = CommandHelper.intToByteArray(para4_leftBoxX)[3];
        bytes[31] = CommandHelper.intToByteArray(para4_leftBoxX)[2];
        bytes[32] = CommandHelper.intToByteArray(para4_leftBoxZ)[3];
        bytes[33] = CommandHelper.intToByteArray(para4_leftBoxZ)[2];
        bytes[34] = CommandHelper.intToByteArray(para4_leftBoxForceDegree)[3];
        //5右仓参数
        bytes[35] = CommandHelper.intToByteArray(para5_rightBoxScrewLength)[3];
        bytes[36] = CommandHelper.intToByteArray(para5_rightBoxScrewLength)[2];
        bytes[37] = CommandHelper.intToByteArray(para5_rightBoxGetScrewHeight)[3];
        bytes[38] = CommandHelper.intToByteArray(para5_rightBoxGetScrewHeight)[2];
        bytes[39] = CommandHelper.intToByteArray(para5_rightBoxX)[3];
        bytes[40] = CommandHelper.intToByteArray(para5_rightBoxX)[2];
        bytes[41] = CommandHelper.intToByteArray(para5_rightBoxZ)[3];
        bytes[42] = CommandHelper.intToByteArray(para5_rightBoxZ)[2];
        bytes[43] = CommandHelper.intToByteArray(para5_rightBoxForceDegree)[3];


        byteList = bytes;
    }


    public String getWriteCommandArrayHexString() {

        ModelCovertToByteArray();

        byte[] tempList = new byte[64+4];
        tempList[0] = (byte) 0x02;
        tempList[1] = (byte) 0x90;
        tempList[2] = (byte) 0x40;
        int s = 3;
        for (int i = 0; i < 44; i++) {
            tempList[s + i] = byteList[i];
        }

        byte checksum = 0x00;
        for (int i = 0; i < 47; i++) {
            checksum ^= tempList[i];
        }

        tempList[67] = checksum;

        return MainActivity.byteArrayToHexString(tempList);
    }


    public static void TestConvert() {

        CommandSys cmd = new CommandSys();
        cmd.sysPara_formulaNum = 3;
        cmd.sysPara_outPut = 32767;

        cmd.arc_getScrewSide = 15;
        cmd.arc_lockScrewSide = 25;
        cmd.arc_onOrOff = false;

        cmd.sys_minusX = -11;
        cmd.sys_minusY = 0;
        cmd.sys_minusZ = 0;

        cmd.sys_plusX = 3500;
        cmd.sys_plusY = 0;
        cmd.sys_plusZ = 1444;

        cmd.sys_step = 90;

        cmd.para1_motorSpeedX = 100;
        cmd.para1_motorSpeedY = 100;
        cmd.para1_motorSpeedZ = 100;
        cmd.para1_motorSpeedZGetScrew = 100;
        cmd.para1_motorSpeedZLockScrew = 100;

        cmd.para2_getScrewTime = 70;
        cmd.para3_screwLooseTime = 11;

        cmd.para4_leftBoxScrewLength = 10;
        cmd.para4_leftBoxGetScrewHeight = 25;
        cmd.para4_leftBoxX = 190;
        cmd.para4_leftBoxZ = 160;
        cmd.para4_leftBoxForceDegree = 10;

        cmd.para5_rightBoxScrewLength = 10;
        cmd.para5_rightBoxGetScrewHeight = 25;
        cmd.para5_rightBoxX = 2300;
        cmd.para5_rightBoxZ = 455;
        cmd.para5_rightBoxForceDegree = 10;

        cmd.ModelCovertToByteArray();
        cmd.ByteArrayConvertToModel(cmd.byteList, 0);
        Log.d("test", "配方编号:" + cmd.sysPara_formulaNum);


    }


}
