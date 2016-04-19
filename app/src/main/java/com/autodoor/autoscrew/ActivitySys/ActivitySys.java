package com.autodoor.autoscrew.ActivitySys;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.autodoor.autoscrew.CustomApplication;
import com.autodoor.autoscrew.MainActivity;
import com.autodoor.autoscrew.R;
import com.autodoor.autoscrew.data.CommandManager;
import com.autodoor.autoscrew.data.CommandMotor;
import com.autodoor.autoscrew.data.CommandStatus;
import com.autodoor.autoscrew.data.CommandSys;
import com.autodoor.autoscrew.data.FileHelper;
import com.autodoor.autoscrew.dialog.DialogNumber;

import java.io.IOException;

public class ActivitySys extends AppCompatActivity implements DialogNumber.NoticeDialogListener {
    public static ActivitySys myself;

    ProgressDialog progress;
    EditText etMinusX;
    EditText etMinusY;
    EditText etMinusZ;
    EditText etPlusX;
    EditText etPlusY;
    EditText etPlusZ;
    EditText etStep;

    EditText etCurrent;
    DialogFragment customDialog;

    Button btnWrite;
    Button btnRead;
    Spinner spnCache;
    Button btnChangePwd;
    Button btnReturn;
    Button btnReset;
    Button btnSave;
    Button btnSetMinus;
    Button btnRunMinus;
    Button btnSetPlus;
    Button btnRunPlus;
    ToggleButton tgElectricity;


    public boolean isDisplaying = false;

    Spinner spinnerCache;
    private ArrayAdapter<String> sgtrArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys);

        //RadioButton radioButton = (RadioButton) findViewById(R.id.rb1);
        //radioButton.setChecked(true);


        InitView();

        ReadData();

        myself = this;


        InitSpinner();


        btnWrite.setOnClickListener(WriteCache());
        btnRead.setOnClickListener(ReadCache());
        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTitleDialog();

            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        //保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                CustomApplication.mCommandSys.sys_minusX = Integer.parseInt(etMinusX.getText().toString());
                CustomApplication.mCommandSys.sys_minusY = Integer.parseInt(etMinusY.getText().toString());
                CustomApplication.mCommandSys.sys_minusZ = Integer.parseInt(etMinusZ.getText().toString());
                CustomApplication.mCommandSys.sys_plusX = Integer.parseInt(etPlusX.getText().toString());
                CustomApplication.mCommandSys.sys_plusY = Integer.parseInt(etPlusY.getText().toString());
                CustomApplication.mCommandSys.sys_plusZ = Integer.parseInt(etPlusZ.getText().toString());
                CustomApplication.mCommandSys.sys_step = Integer.parseInt(etStep.getText().toString());

                String cmd = CustomApplication.mCommandSys.getWriteCommandArrayHexString();
                MainActivity.mainActivity.sendMessage("save", cmd);
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

        btnSetMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                MainActivity.mainActivity.sendMessage("SetMinus1", "02 A4 00 A6 ");

            }
        });
        btnSetPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                MainActivity.mainActivity.sendMessage("SetPlus1", "02 A4 00 A6 ");
            }
        });

        btnRunMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                int x = Integer.valueOf(etMinusX.getText().toString());
                int y = Integer.valueOf(etMinusY.getText().toString());
                int z = Integer.valueOf(etMinusZ.getText().toString());
                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.RunToXYZString(x,y,z);
                MainActivity.mainActivity.sendMessage("RunMinus", cmd);
            }
        });
        btnRunPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                int x = Integer.valueOf(etPlusX.getText().toString());
                int y = Integer.valueOf(etPlusY.getText().toString());
                int z = Integer.valueOf(etPlusZ.getText().toString());
                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.RunToXYZString(x,y,z);
                MainActivity.mainActivity.sendMessage("RunPlus", cmd);
            }
        });



    }

    private void InitView() {
        etMinusX = (EditText) findViewById(R.id.etMinusX);
        etMinusY = (EditText) findViewById(R.id.etMinusY);
        etMinusZ = (EditText) findViewById(R.id.etMinusZ);
        etPlusX = (EditText) findViewById(R.id.etPlusX);
        etPlusY = (EditText) findViewById(R.id.etPlusY);
        etPlusZ = (EditText) findViewById(R.id.etPlusZ);
        etStep = (EditText) findViewById(R.id.etStep);

        etMinusX.setInputType(InputType.TYPE_NULL);// 屏蔽输入法
        etMinusY.setInputType(InputType.TYPE_NULL);
        etMinusZ.setInputType(InputType.TYPE_NULL);
        etPlusX.setInputType(InputType.TYPE_NULL);
        etPlusY.setInputType(InputType.TYPE_NULL);
        etPlusZ.setInputType(InputType.TYPE_NULL);
        etStep.setInputType(InputType.TYPE_NULL);

        //default
        etMinusX.setOnClickListener(getL());
        etMinusY.setOnClickListener(getL());
        etMinusZ.setOnClickListener(getL());

        etPlusX.setOnClickListener(getL2());
        etPlusY.setOnClickListener(getL2());
        etPlusZ.setOnClickListener(getL2());

        etStep.setOnClickListener(getL3());


        tgElectricity = (ToggleButton) findViewById(R.id.tgElectricity);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnChangePwd = (Button) findViewById(R.id.btnChangePwd);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnSave = (Button) findViewById(R.id.btnSave);
        spnCache = (Spinner) findViewById(R.id.spnCache);
        btnSetMinus = (Button) findViewById(R.id.btnSetMinus);
        btnRunMinus = (Button) findViewById(R.id.btnRunMinus);
        btnSetPlus = (Button) findViewById(R.id.btnSetPlus);
        btnRunPlus = (Button) findViewById(R.id.btnRunPlus);

    }

    private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        inputServer.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'
                        , 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return numberChars;
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改密码")
                .setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        Log.d("changePwd", inputName);
                        //保存密码
                        FileHelper fileHelper = new FileHelper(ActivitySys.this);
                        try {
                            fileHelper.writeFile("ActivitySys_Pwd", inputName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.show();
    }

    @NonNull
    private View.OnClickListener ReadCache() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ActivitySys.this)
                        .setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要读取缓存" + (spnCache.getSelectedItemPosition() + 1))//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                //finish();
                            }

                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                FileHelper fileHelper = new FileHelper(ActivitySys.this);
                                try {
                                    String temp = fileHelper.readFile("FileSys" + (spnCache.getSelectedItemPosition() + 1));
                                    byte[] midbytes = temp.getBytes("ISO-8859-1");
                                    CommandSys cmd = new CommandSys();
                                    cmd.ByteArrayConvertToModel(midbytes, 0);
                                    Log.d("fileHelper", cmd.sys_minusX + ";" + cmd.sys_minusY + ";" + cmd.sys_minusZ
                                            + ";" + cmd.sys_plusX + ";" + cmd.sys_plusY + ";" + cmd.sys_plusZ + ";" + cmd.sys_step);

                                    etMinusX.setText(String.valueOf(cmd.sys_minusX));
                                    etMinusY.setText(String.valueOf(cmd.sys_minusY));
                                    etMinusZ.setText(String.valueOf(cmd.sys_minusZ));
                                    etPlusX.setText(String.valueOf(cmd.sys_plusX));
                                    etPlusY.setText(String.valueOf(cmd.sys_plusY));
                                    etPlusZ.setText(String.valueOf(cmd.sys_plusZ));
                                    etStep.setText(String.valueOf(cmd.sys_step));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        })
                        .show();//在按键响应事件中显示此对话框


            }
        };
    }

    @NonNull
    private View.OnClickListener WriteCache() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ActivitySys.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要写入缓存" + (spnCache.getSelectedItemPosition() + 1))//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                //finish();
                            }

                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                FileHelper fileHelper = new FileHelper(ActivitySys.this);

                                CommandSys cmd = new CommandSys();
                                cmd.sys_minusX = Integer.parseInt(etMinusX.getText().toString());
                                cmd.sys_minusY = Integer.parseInt(etMinusY.getText().toString());
                                cmd.sys_minusZ = Integer.parseInt(etMinusZ.getText().toString());

                                cmd.sys_plusX = Integer.parseInt(etPlusX.getText().toString());
                                cmd.sys_plusY = Integer.parseInt(etPlusY.getText().toString());
                                cmd.sys_plusZ = Integer.parseInt(etPlusZ.getText().toString());

                                cmd.sys_step = Integer.parseInt(etStep.getText().toString());

                                cmd.ModelCovertToByteArray();
                                try {
                                    String temp = new String(cmd.byteList, "ISO-8859-1");
                                    fileHelper.writeFile("FileSys" + (spnCache.getSelectedItemPosition() + 1), temp);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("fileHelper", "write over;");


                                //finish();
                            }

                        })
                        .show();//在按键响应事件中显示此对话框


            }
        };
    }

    private void InitSpinner() {
        final String[] from = {"缓存1", "缓存2", "缓存3", "缓存4"};
        spinnerCache = (Spinner) findViewById(R.id.spnCache);
        sgtrArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, from);
        spinnerCache.setAdapter(sgtrArrayAdapter);
    }


    public void ReadData() {
        ShowProgress();
        MainActivity.mainActivity.sendMessage("create", "02 80 00 82 ");
    }

    private void ShowProgress() {
        progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true, true);
    }

    public void ShowBTMessage(String sourceMsg, CommandManager commandManager) {
        if (sourceMsg == "RunMinus") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys RunMinus 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySys RunMinus 系统设置。 on");
                progress.dismiss();
                //MainActivity.mainActivity.sendMessage("SetMinus2", "02 B0 00 B2 ");
            }
            return;
        }
        if (sourceMsg == "RunPlus") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys RunPlus 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySys RunPlus 系统设置。 on");
                progress.dismiss();
                //MainActivity.mainActivity.sendMessage("SetMinus2", "02 B0 00 B2 ");
            }
            return;
        }


        /////////////////////SetMinus SetPlus START
        if (sourceMsg == "SetMinus1") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys SetMinus1 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySys SetMinus1 系统设置。 on");

                MainActivity.mainActivity.sendMessage("SetMinus2", "02 B0 00 B2 ");
            }
            return;
        }

        if (sourceMsg == "SetMinus2") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            if(commandStatus==null)Log.d("null","!!!! is null");

            etMinusX.setText(String.valueOf(commandStatus.x));
            etMinusY.setText(String.valueOf(commandStatus.y));
            etMinusZ.setText(String.valueOf(commandStatus.z));

            Log.d("ShowBTMessage", "at ActivitySys 状态信息 SetMinus2");
            progress.dismiss();

            return;
        }

        if (sourceMsg == "SetPlus1") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys SetPlus1 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySys SetPlus1 系统设置。 on");

                MainActivity.mainActivity.sendMessage("SetPlus2", "02 B0 00 B2 ");
            }
            return;
        }

        if (sourceMsg == "SetPlus2") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;

            etPlusX.setText(String.valueOf(commandStatus.x));
            etPlusY.setText(String.valueOf(commandStatus.y));
            etPlusZ.setText(String.valueOf(commandStatus.z));

            Log.d("ShowBTMessage", "at ActivitySys 状态信息 SetPlus2");
            progress.dismiss();

            return;
        }

        /////////////////////SetMinus SetPlus END



        if (sourceMsg == "save") {
            if (commandManager.byteReply[0] == (byte) 0x06) {
                //tgElectricity.setChecked(!tgElectricity.isChecked());
                Log.d("ShowBTMessage", "at ActivitySys Save 系统设置。OK");
                progress.dismiss();
            }
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

                MainActivity.mainActivity.sendMessage("SetMinus2", "02 B0 00 B2 ");
            }

            return;
        }

        if (sourceMsg == "create2") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            tgElectricity.setChecked(commandStatus.c);
            Log.d("ShowBTMessage", "at ActivitySys 状态信息");
            progress.dismiss();

            return;
        }

        if (sourceMsg == "create") {
            CommandSys commandSys = commandManager.commandSys;
            CustomApplication.mCommandSys = commandSys;
            etMinusX.setText(String.valueOf(commandSys.sys_minusX));
            etMinusY.setText(String.valueOf(commandSys.sys_minusY));
            etMinusZ.setText(String.valueOf(commandSys.sys_minusZ));
            etPlusX.setText(String.valueOf(commandSys.sys_plusX));
            etPlusY.setText(String.valueOf(commandSys.sys_plusY));
            etPlusZ.setText(String.valueOf(commandSys.sys_plusZ));
            etStep.setText(String.valueOf(commandSys.sys_step));

            Log.d("ShowBTMessage", "at ActivitySys 系统设置。");


            MainActivity.mainActivity.sendMessage("create2", "02 B0 00 B2 ");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sys, menu);
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


    @NonNull
    private View.OnClickListener getL() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","customDialog == null？ "+ (customDialog==null? true:false));
                if (v.hasFocus() && customDialog == null) {
                    etCurrent = ((EditText)v);
                    etCurrent.setEnabled(false);
                    //弹出数字输入对话框

                    customDialog = new DialogNumber();
                    Bundle args = new Bundle();
                    args.putString("number", ((EditText) v).getText().toString());
                    args.putString("min", "-200");
                    args.putString("max", "200");

                    customDialog.setArguments(args);
                    customDialog.show(getSupportFragmentManager(), "DialogNumber");
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener getL2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","customDialog == null？ "+ (customDialog==null? true:false));
                if (v.hasFocus() && customDialog == null) {
                    etCurrent = ((EditText)v);
                    etCurrent.setEnabled(false);
                    //弹出数字输入对话框

                    customDialog = new DialogNumber();
                    Bundle args = new Bundle();
                    args.putString("number", ((EditText) v).getText().toString());
                    args.putString("min", "1000");
                    args.putString("max", "6000");

                    customDialog.setArguments(args);
                    customDialog.show(getSupportFragmentManager(), "DialogNumber");
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener getL3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","customDialog == null？ "+ (customDialog==null? true:false));
                if (v.hasFocus() && customDialog == null) {
                    etCurrent = ((EditText)v);
                    etCurrent.setEnabled(false);
                    //弹出数字输入对话框

                    customDialog = new DialogNumber();
                    Bundle args = new Bundle();
                    args.putString("number", ((EditText) v).getText().toString());
                    args.putString("min", "10");
                    args.putString("max", "255");

                    customDialog.setArguments(args);
                    customDialog.show(getSupportFragmentManager(), "DialogNumber");
                }
            }
        };
    }

    @Override
    public void onConfirm(DialogInterface dialogInterface, Bundle bundle) {
        //赋值回原来控件
        String temp = bundle.getString("number");
        etCurrent.setText(temp);


        //
        etCurrent.setEnabled(true);
        RemoveDialog(customDialog);

    }

    @Override
    public void onCancle(DialogInterface dialogInterface, Bundle bundle) {
        //
        etCurrent.setEnabled(true);
        RemoveDialog(customDialog);

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface, Bundle bundle) {

        etCurrent.setEnabled(true);
        RemoveDialog(customDialog);

    }

    private void RemoveDialog(DialogFragment dialogFragment) {
        if (dialogFragment == null) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(dialogFragment);
        ft.commit();
        customDialog = null;

    }

}
