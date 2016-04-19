package com.autodoor.autoscrew;

import android.app.Application;

import com.autodoor.autoscrew.data.CommandSetScrew;
import com.autodoor.autoscrew.data.CommandStatus;
import com.autodoor.autoscrew.data.CommandSys;
import com.autodoor.autoscrew.data.SetScrewPoint;

/**
 * Created by yangyi on 2016/1/19.
 */
public class CustomApplication extends Application {

    public static CommandStatus mCommandStatus;
    public static CommandSys mCommandSys;
    public static CommandSetScrew mCommandSetScrew;

    private String value;

    @Override
    public void onCreate(){
        super.onCreate();

        //CommandSys.TestConvert();

        //SetScrewPoint.TestConvert();

        //CommandSetScrew.TestConvert();

        //CommandStatus.TestStatus();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
