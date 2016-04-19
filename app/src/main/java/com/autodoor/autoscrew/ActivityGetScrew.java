package com.autodoor.autoscrew;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.autodoor.autoscrew.data.CommandManager;
import com.autodoor.autoscrew.data.CommandMotor;
import com.autodoor.autoscrew.data.CommandStatus;
import com.autodoor.autoscrew.data.CommandSys;

public class ActivityGetScrew extends AppCompatActivity {

    public boolean isDisplaying =false;
    public static ActivityGetScrew myself;
    ProgressDialog progress;

    EditText etLeftboxX;
    EditText etLeftboxZ;
    Button btnLeftboxSetPoint;
    Button btnLeftboxGetScrew;
    Button btnLeftboxRunX;
    Button btnLeftboxRunXZ;

    EditText etRightboxX;
    EditText etRightboxZ;
    Button btnRightboxSetPoint;
    Button btnRightboxGetScrew;
    Button btnRightboxRunX;
    Button btnRightboxRunXZ;

    ToggleButton tgElectricity;
    Button btnReset;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_screw);

        etLeftboxX = (EditText)findViewById(R.id.etLeftboxX);
        etLeftboxZ = (EditText)findViewById(R.id.etLeftboxZ);

        btnLeftboxSetPoint = (Button)findViewById(R.id.btnLeftboxSetPoint);
        btnLeftboxGetScrew= (Button)findViewById(R.id.btnLeftboxGetScrew);
        btnLeftboxRunX= (Button)findViewById(R.id.btnLeftboxRunX);
        btnLeftboxRunXZ= (Button)findViewById(R.id.btnLeftboxRunXZ);

        etRightboxX = (EditText)findViewById(R.id.etRightboxX);
        etRightboxZ = (EditText)findViewById(R.id.etRightboxZ);

        btnRightboxSetPoint = (Button)findViewById(R.id.btnRightboxSetPoint);
        btnRightboxGetScrew= (Button)findViewById(R.id.btnRightboxGetScrew);
        btnRightboxRunX= (Button)findViewById(R.id.btnRightboxRunX);
        btnRightboxRunXZ= (Button)findViewById(R.id.btnRightboxRunXZ);

        tgElectricity = (ToggleButton) findViewById(R.id.tgElectricity);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnLeftboxSetPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                MainActivity.mainActivity.sendMessage("LeftboxSetPoint1", "02 A4 00 A6 ");
            }
        });

        btnLeftboxGetScrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowProgress();

                int x = Integer.valueOf(etLeftboxX.getText().toString());
                //int y = Integer.valueOf(etMinusY.getText().toString());
                int z = Integer.valueOf(etLeftboxZ.getText().toString());
                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.GetLeftScrewString(x, z);
                MainActivity.mainActivity.sendMessage("GetLeftboxScrew", cmd);
            }
        });





        btnLeftboxRunX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                int x = Integer.valueOf(etLeftboxX.getText().toString());
                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.RunToXYZString(x, 0, 0);
                MainActivity.mainActivity.sendMessage("LeftboxRunX", cmd);
            }
        });

        btnLeftboxRunXZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                int x = Integer.valueOf(etLeftboxX.getText().toString());
                int z = Integer.valueOf(etLeftboxZ.getText().toString());
                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.RunToXYZString(x, 0, z);
                MainActivity.mainActivity.sendMessage("LeftboxRunXZ", cmd);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                //发送复位指令
                MainActivity.mainActivity.sendMessage("reset", "02 A0 00 A2 ");


            }
        });

        //电机加不加电
        tgElectricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandC();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                CustomApplication.mCommandSys.para4_leftBoxX = Integer.parseInt(etLeftboxX.getText().toString());
                CustomApplication.mCommandSys.para4_leftBoxZ = Integer.parseInt(etLeftboxZ.getText().toString());
                CustomApplication.mCommandSys.para5_rightBoxX = Integer.parseInt(etRightboxX.getText().toString());
                CustomApplication.mCommandSys.para5_rightBoxZ = Integer.parseInt(etRightboxZ.getText().toString());

                String cmd = CustomApplication.mCommandSys.getWriteCommandArrayHexString();
                MainActivity.mainActivity.sendMessage("save", cmd);
            }
        });

        myself = this;
        ShowProgress();
        MainActivity.mainActivity.sendMessage("create1", "02 80 00 82 ");
    }



    private void ShowProgress() {
        progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true, true);
    }

    public void ShowBTMessage(String sourceMsg, CommandManager commandManager) {
        if (sourceMsg == "create1") {
            CommandSys commandSys = commandManager.commandSys;
            CustomApplication.mCommandSys = commandSys;

            etLeftboxX.setText(String.valueOf(commandSys.para4_leftBoxX));
            etLeftboxZ.setText(String.valueOf(commandSys.para4_leftBoxZ));
            etRightboxX.setText(String.valueOf(commandSys.para5_rightBoxX));
            etRightboxZ.setText(String.valueOf(commandSys.para5_rightBoxZ));


            Log.d("ShowBTMessage", "at ActivityGetScrew create1 系统设置。");
            progress.dismiss();
            return;
        }

        if (sourceMsg == "LeftboxSetPoint1") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys LeftboxSetPoint1 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySys LeftboxSetPoint1 系统设置。 on");

                //位置信息通过查询状态来读取

                MainActivity.mainActivity.sendMessage("LeftboxSetPoint2", "02 B0 00 B2 ");
            }
            return;
        }

        if (sourceMsg == "LeftboxSetPoint2") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            if(commandStatus==null)Log.d("null","!!!! is null");

            etLeftboxX.setText(String.valueOf(commandStatus.x));
            etLeftboxZ.setText(String.valueOf(commandStatus.z));

            Log.d("ShowBTMessage", "at ActivityGetScrew 状态信息 LeftboxSetPoint2");
            progress.dismiss();

            return;
        }

        if (sourceMsg == "GetLeftboxScrew") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivityGetScrew RunMinus 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivityGetScrew RunMinus 系统设置。 on");
                //progress.dismiss();

                MainActivity.mainActivity.sendMessage("GetLeftboxScrewStatus", "02 B0 00 B2 ");
            }
            return;
        }

        if (sourceMsg == "GetLeftboxScrewStatus") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            tgElectricity.setChecked(commandStatus.c);
            Log.d("ShowBTMessage", "at ActivityGetScrew 状态信息");
            progress.dismiss();

            return;
        }

        if (sourceMsg == "status") {
            if (commandManager.byteReply[0] == (byte) 0x06) {
                //tgElectricity.setChecked(!tgElectricity.isChecked());
                Log.d("ShowBTMessage", "at ActivitySys status 系统设置。OK");
                progress.dismiss();
            }
            return;
        }

        if (sourceMsg == "reset") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys reset 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivitySys reset 系统设置。 on");

                MainActivity.mainActivity.sendMessage("GetLeftboxScrewStatus", "02 B0 00 B2 ");
            }

            return;
        }

        if (sourceMsg == "LeftboxRunX" ||sourceMsg == "LeftboxRunXZ" ) {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys RunMinus 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySys RunMinus 系统设置。 on");

                MainActivity.mainActivity.sendMessage("GetLeftboxScrewStatus", "02 B0 00 B2 ");
            }
            return;
        }

        if (sourceMsg == "save") {
            if (commandManager.byteReply[0] == (byte) 0x06) {
                //tgElectricity.setChecked(!tgElectricity.isChecked());
                Log.d("ShowBTMessage", "at ActivityPara Save 系统设置。OK");
                progress.dismiss();
            }
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_screw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();

        isDisplaying = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        isDisplaying = false;
    }
}
