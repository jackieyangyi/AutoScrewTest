package com.autodoor.autoscrew.data;

import android.util.Log;

/**
 * Created by yangyi on 2016/1/20.
 */
public class SetScrewPoint {
    public SetScrewPoint() {
        x = 0;
        y = 0;
        z = 0;
        c = 0;
    }

    public byte[] bytesPoint = new byte[8];
    public int x, y, z, c;

    public void ByteArrayToModel(byte[] bs, int start) {

        x = CommandHelper.byteArrayToInt(new byte[]{0, 0, bytesPoint[1], bytesPoint[0]});
        y = CommandHelper.byteArrayToInt(new byte[]{0, 0, bytesPoint[3], bytesPoint[2]});
        z = CommandHelper.byteArrayToInt(new byte[]{0, 0, bytesPoint[5], bytesPoint[4]});
        c = CommandHelper.byteArrayToInt(new byte[]{0, 0, bytesPoint[7], bytesPoint[6]});


    }

    public void ModelToByteArray() {
        bytesPoint[0] = CommandHelper.intToByteArray(x)[3];
        bytesPoint[1] = CommandHelper.intToByteArray(x)[2];
        bytesPoint[2] = CommandHelper.intToByteArray(y)[3];
        bytesPoint[3] = CommandHelper.intToByteArray(y)[2];
        bytesPoint[4] = CommandHelper.intToByteArray(z)[3];
        bytesPoint[5] = CommandHelper.intToByteArray(z)[2];
        bytesPoint[6] = CommandHelper.intToByteArray(c)[3];
        bytesPoint[7] = CommandHelper.intToByteArray(c)[2];

    }

    public static void TestConvert() {
        SetScrewPoint point = new SetScrewPoint();
        point.x = 10001;
        point.y = 20002;
        point.z = 303;
        point.ModelToByteArray();
        point.ByteArrayToModel(point.bytesPoint, 0);
        Log.d("test", "point is right ?");


    }


}
