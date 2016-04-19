package com.autodoor.autoscrew;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.autodoor.autoscrew.data.CommandManager;
import com.autodoor.autoscrew.data.CommandStatus;
import com.autodoor.autoscrew.data.CommandSys;
import com.autodoor.autoscrew.dialog.DialogNumber;

public class ActivityPara extends AppCompatActivity {

    ProgressDialog progress;
    ProgressDialog progressRun;//运行和循环的等待栏
    public boolean isDisplaying = false;
    public static ActivityPara myself;

    EditText etSpeedX;
    EditText etSpeedY;
    EditText etSpeedZ;

    EditText etSpeedZGetScrew;
    EditText etSpeedZLockScrew;

    EditText etGetScrewTime;
    EditText etScrewLooseTime;

    EditText etLeftBoxScrewLength;
    EditText etLeftBoxGetScrewHeight;
    EditText etLeftBoxForceDegree;

    EditText etRightBoxScrewLength;
    EditText etRightBoxGetScrewHeight;
    EditText etRightBoxForceDegree;

    EditText etArcGetScrewSide;
    EditText etArcLockScrewSide;
    ToggleButton tbArcOnOrOff;

    ToggleButton tbD;
    ToggleButton tbE;
    ToggleButton tbA;
    ToggleButton tbB;

    Button btnReset;
    Button btnRun;
    Button btnCirculation;
    Button btnSave;
    Button btnReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para);

        InitView();

        myself = this;

        ShowProgress();
        MainActivity.mainActivity.sendMessage("create1", "02 80 00 82 ");

    }

    private void InitView() {
        etSpeedX = (EditText) findViewById(R.id.etSpeedX);
        etSpeedY = (EditText) findViewById(R.id.etSpeedY);
        etSpeedZ = (EditText) findViewById(R.id.etSpeedZ);
        tbA = (ToggleButton) findViewById(R.id.tbA);
        tbB = (ToggleButton) findViewById(R.id.tbB);
        tbD = (ToggleButton) findViewById(R.id.tbD);
        tbE = (ToggleButton) findViewById(R.id.tbE);

        etSpeedZGetScrew = (EditText) findViewById(R.id.etSpeedZGetScrew);
        etSpeedZLockScrew = (EditText) findViewById(R.id.etSpeedZLockScrew);

        etGetScrewTime = (EditText) findViewById(R.id.etGetScrewTime);
        etScrewLooseTime = (EditText) findViewById(R.id.etScrewLooseTime);

        etLeftBoxScrewLength = (EditText) findViewById(R.id.etLeftBoxScrewLength);
        etLeftBoxGetScrewHeight = (EditText) findViewById(R.id.etLeftBoxGetScrewHeight);
        etLeftBoxForceDegree = (EditText) findViewById(R.id.etLeftBoxForceDegree);

        etRightBoxScrewLength = (EditText) findViewById(R.id.etRightBoxScrewLength);
        etRightBoxGetScrewHeight = (EditText) findViewById(R.id.etRightBoxGetScrewHeight);
        etRightBoxForceDegree = (EditText) findViewById(R.id.etRightBoxForceDegree);

        etArcGetScrewSide = (EditText) findViewById(R.id.etArcGetScrewSide);
        etArcLockScrewSide = (EditText) findViewById(R.id.etArcLockScrewSide);
        tbArcOnOrOff = (ToggleButton) findViewById(R.id.tbArcOnOrOff);


        btnReset = (Button) findViewById(R.id.btnReset);
        btnRun = (Button) findViewById(R.id.btnRun);
        btnCirculation = (Button) findViewById(R.id.btnCirculation);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnReturn = (Button) findViewById(R.id.btnReturn);



        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                //发送复位指令
                MainActivity.mainActivity.sendMessage("reset", "02 A0 00 A2 ");
            }
        });
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowProgressRun();

                //发送运行指令
                MainActivity.mainActivity.sendMessage("run", "02 A1 00 A3 ");
            }
        });
        btnCirculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgressRun();

                //发送循环指令
                MainActivity.mainActivity.sendMessage("Circulation", "02 A3 00 A1 ");
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                CustomApplication.mCommandSys.para1_motorSpeedX = Integer.parseInt(etSpeedX.getText().toString());
                CustomApplication.mCommandSys.para1_motorSpeedY = Integer.parseInt(etSpeedY.getText().toString());
                CustomApplication.mCommandSys.para1_motorSpeedZ = Integer.parseInt(etSpeedZ.getText().toString());
                CustomApplication.mCommandSys.para1_motorSpeedZGetScrew = Integer.parseInt(etSpeedZGetScrew.getText().toString());
                CustomApplication.mCommandSys.para1_motorSpeedZLockScrew = Integer.parseInt(etSpeedZLockScrew.getText().toString());

                CustomApplication.mCommandSys.para2_getScrewTime = Integer.parseInt(etGetScrewTime.getText().toString());
                CustomApplication.mCommandSys.para3_screwLooseTime = Integer.parseInt(etScrewLooseTime.getText().toString());

                CustomApplication.mCommandSys.para4_leftBoxScrewLength= Integer.parseInt(etLeftBoxScrewLength.getText().toString());
                CustomApplication.mCommandSys.para4_leftBoxGetScrewHeight = Integer.parseInt(etLeftBoxGetScrewHeight.getText().toString());
                CustomApplication.mCommandSys.para4_leftBoxForceDegree= Integer.parseInt(etLeftBoxForceDegree.getText().toString());


                CustomApplication.mCommandSys.para5_rightBoxScrewLength= Integer.parseInt(etRightBoxScrewLength.getText().toString());
                CustomApplication.mCommandSys.para5_rightBoxGetScrewHeight = Integer.parseInt(etRightBoxGetScrewHeight.getText().toString());
                CustomApplication.mCommandSys.para5_rightBoxForceDegree= Integer.parseInt(etRightBoxForceDegree.getText().toString());


                CustomApplication.mCommandSys.arc_getScrewSide =Integer.parseInt(etArcGetScrewSide.getText().toString());
                CustomApplication.mCommandSys.arc_lockScrewSide =Integer.parseInt(etArcLockScrewSide.getText().toString());
                CustomApplication.mCommandSys.arc_onOrOff = tbArcOnOrOff.isChecked();


                String cmd = CustomApplication.mCommandSys.getWriteCommandArrayHexString();
                MainActivity.mainActivity.sendMessage("save", cmd);
            }
        });

        //电批
        tbD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandD();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }
        });

        //电磁铁
        tbE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandE();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }
        });

        //螺丝
        tbA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandA();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }
        });

        //刹车
        tbB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandB();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }
        });




    }

    private void ShowProgressRun() {
        progressRun = new ProgressDialog(ActivityPara.this);
        progressRun.setTitle("处理中，请稍后...");
        progressRun.setButton(DialogInterface.BUTTON_NEUTRAL, "停止", (DialogInterface.OnClickListener) null);
        progressRun.show();
        // Get the button from the view.
        Button dialogButton = progressRun.getButton(DialogInterface.BUTTON_NEUTRAL);
        // Set the onClickListener here, in the view.
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送停止指令
                MainActivity.mainActivity.sendMessage("stop", "5A ");
            }

        });
    }


    public void ShowBTMessage(String sourceMsg, CommandManager commandManager) {
        if (sourceMsg == "create1") {
            CommandSys commandSys = commandManager.commandSys;
            CustomApplication.mCommandSys = commandSys;
            //etStep.setText(String.valueOf(commandSys.sys_step));
            etSpeedX.setText(String.valueOf(commandSys.para1_motorSpeedX));
            etSpeedY.setText(String.valueOf(commandSys.para1_motorSpeedY));
            etSpeedZ.setText(String.valueOf(commandSys.para1_motorSpeedZ));

            etArcGetScrewSide.setText(String.valueOf(commandSys.arc_getScrewSide));
            etArcLockScrewSide.setText(String.valueOf(commandSys.arc_lockScrewSide));
            tbArcOnOrOff.setChecked(commandSys.arc_onOrOff);

            etSpeedZGetScrew.setText(String.valueOf(commandSys.para1_motorSpeedZGetScrew));
            etSpeedZLockScrew.setText(String.valueOf(commandSys.para1_motorSpeedZLockScrew));
            etGetScrewTime.setText(String.valueOf(commandSys.para2_getScrewTime));
            etScrewLooseTime.setText(String.valueOf(commandSys.para3_screwLooseTime));

            etLeftBoxScrewLength.setText(String.valueOf(commandSys.para4_leftBoxScrewLength));
            etLeftBoxGetScrewHeight.setText(String.valueOf(commandSys.para4_leftBoxGetScrewHeight));
            etLeftBoxForceDegree.setText(String.valueOf(commandSys.para4_leftBoxForceDegree));
            etRightBoxScrewLength.setText(String.valueOf(commandSys.para5_rightBoxScrewLength));
            etRightBoxGetScrewHeight.setText(String.valueOf(commandSys.para5_rightBoxGetScrewHeight));
            etRightBoxForceDegree.setText(String.valueOf(commandSys.para5_rightBoxForceDegree));


            Log.d("ShowBTMessage", "at ActivityPara create1 系统设置。");
            MainActivity.mainActivity.sendMessage("create2", "02 B0 00 B2 ");
            return;
        }
        if (sourceMsg == "create2") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            tbA.setChecked(commandStatus.a);
            tbB.setChecked(commandStatus.b);
            tbD.setChecked(commandStatus.d);
            tbE.setChecked(commandStatus.e);

            Log.d("ShowBTMessage", "at ActivityPara create2 系统设置。");

            if (null != progress) progress.dismiss();
            if (null != progressRun) progressRun.dismiss();
            return;
        }


        if (sourceMsg == "reset") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys reset 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivityPara reset 系统设置。 on");

                MainActivity.mainActivity.sendMessage("create2", "02 B0 00 B2 ");
            }

            return;
        }

        if (sourceMsg == "run" || sourceMsg == "Circulation" || sourceMsg == "stop") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys run 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progressRun.dismiss();
                Log.d("ShowBTMessage", "at ActivityPara run 系统设置。 on");
                MainActivity.mainActivity.sendMessage("create2", "02 B0 00 B2 ");
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

        if (sourceMsg == "status") {
            if (commandManager.byteReply[0] == (byte) 0x06) {
                //tgElectricity.setChecked(!tgElectricity.isChecked());
                Log.d("ShowBTMessage", "at ActivityPara status 系统设置。OK");
                progress.dismiss();
            }
            return;
        }

    }

    private void ShowProgress() {
        progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_para, menu);
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
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
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
