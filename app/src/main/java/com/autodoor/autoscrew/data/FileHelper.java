package com.autodoor.autoscrew.data;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.util.EncodingUtils;

/**
 * Created by yangyi on 2016/2/6.
 */
public class FileHelper {

    FragmentActivity v;

    public FileHelper(FragmentActivity view){
        v= view;
    }


    //写数据
    public void writeFile(String fileName,String writestr) throws IOException {
        try{

            FileOutputStream fout =v.openFileOutput(fileName, v.MODE_PRIVATE);

            byte [] bytes = writestr.getBytes();

            fout.write(bytes);

            fout.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    //读数据
    public String readFile(String fileName) throws IOException{
        String res="";
        try{
            FileInputStream fin = v.openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;

    }



}
