package com.autodoor.autoscrew.data;

import android.widget.Toast;

/**
 * Created by yangyi_it on 2016-1-8.
 */
public class CommandHelper {

    public static int byteArrayToInt(byte[] b) {
        int tmp = b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
        if (tmp > 32767) {
            tmp = tmp - 65536;
        }
        return tmp;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /// <summary>
    /// 根据发过来的自己，判断发过来的字节组是否匹配我们定义的命令，即符合字节组格式
    /// 判断是否02开通，是否在命令类型80、81、82里面，长度内容是否符合，校验是否正确
    /// 还有其他的命令，比如错误，比如off..on等处理
    /// </summary>
    /// <param name="recBytes"></param>
    /// <param name="count"></param>
    /// <returns>如果正确了，就返回</returns>
    public static byte[] CheckCommandBytes(byte[] recBytes, int count) {
        //处理 off(0x13) on(0x11) 0x15(ERR)
        if (recBytes[0] == (byte) 0x13 && otherAllIsZero(recBytes, count)) {
            return recBytes;
        }
        if (recBytes[0] == (byte) 0x11 && otherAllIsZero(recBytes, count)) {
            return recBytes;
        }
        if (recBytes[0] == (byte) 0x15 && otherAllIsZero(recBytes, count)) {
            return recBytes;
        }

        if (recBytes[0] == (byte) 0x06 && otherAllIsZero(recBytes, count)) {
            return recBytes;
        }


        //0x02命令类型
        if (count > 3 && recBytes[0] == 0x02) {
            int len = 0;
            if (recBytes[2] < 0x80) {
                len = recBytes[2];
            } else if (recBytes[2] >= 0x80) {
                len = ((recBytes[2] - 0x80)+1) * 128;
            }
            //字节长度符合
            if (count == len + 1 + 1 + 1 + 1) {
                //校验符合
                byte checksum = 0x00;
                for (int i = 0; i < count - 1; i++) {
                    checksum ^= recBytes[i];
                }

                if (checksum == recBytes[count - 1]) {
                    //是否符合格式的字节流
                    byte[] temp = new byte[count];
                    for (int i = 0; i < count; i++) {
                        temp[i] = recBytes[i];
                    }
                    return temp;
                }
            }

        } else {

        }

        return null;
    }

    static boolean otherAllIsZero(byte[] msg, int count) {
        boolean flag = true;
        for (int i = 1; i < count; i++) {
            if (msg[i] != 0x00) {
                flag = false;
            }
        }
        return flag;
    }


}
