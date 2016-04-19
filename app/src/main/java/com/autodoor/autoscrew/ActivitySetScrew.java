package com.autodoor.autoscrew;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.autodoor.autoscrew.ActSetScrew.ActivityCorrection;
import com.autodoor.autoscrew.ActSetScrew.ActivityMatrix;
import com.autodoor.autoscrew.ActSetScrew.InteractiveArrayAdapter;
import com.autodoor.autoscrew.ActSetScrew.Model;
import com.autodoor.autoscrew.ActSetScrew.ModelComparator;
import com.autodoor.autoscrew.data.CommandManager;
import com.autodoor.autoscrew.data.CommandMotor;
import com.autodoor.autoscrew.data.CommandSetScrew;
import com.autodoor.autoscrew.data.CommandStatus;
import com.autodoor.autoscrew.data.CommandSys;
import com.autodoor.autoscrew.data.DatabaseHelper;
import com.autodoor.autoscrew.data.FileHelper;
import com.autodoor.autoscrew.data.SetScrewPoint;
import com.autodoor.autoscrew.dialog.DialogAdjust;
import com.autodoor.autoscrew.dialog.DialogNumber;
import com.autodoor.autoscrew.dialog.DialogPreview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivitySetScrew extends AppCompatActivity implements DialogAdjust.NoticeDialogListener
        , DialogPreview.NoticeDialogListener
        , DialogNumber.NoticeDialogListener {
    private Spinner spinnerCache;
    private Spinner spinnerFormular;
    private ArrayAdapter<String> integerArrayAdapter;
    private LinearLayout llItems;
    //private static ActivitySetScrew myselfThis;
    public static List<Model> list = new ArrayList<Model>();
    //矩阵返回
    public static List<Model> listMatrixBack;
    ArrayList<Model> tempList;

    Thread thread;

    private EditText currentEditText;
    private LinearLayout currentLinearLayout;
    private LinearLayout lastLinearLayout;
    //Drawable drawable;

    DialogAdjust dialogAdjust;


    DialogNumber dialogNumber;
    EditText currNum;

    ArrayAdapter<Model> adapter;

    //预览按钮
    Button btnPreviewCach;
    Button btnPreviewFormula;
    DialogPreview dialogPreview;
    Button btnCurrent;
    Button btnReadDBModel;
    Button btnSaveDBModel;
    Button btnMatrix;
    Button btnCorrection;
    ToggleButton tgA;
    ToggleButton tgC;
    Button btnReadFormula;
    Button btnSaveFormular;
    Spinner spFormular;
    Button btnReset;
    Button btnRunXY;
    Button btnRunXYZ;
    Button btnRunAlone;
    Button btnStart;
    Button btnSetPoint;

    Button btnReadCache;
    Button btnSaveCache;
    Spinner spnCache;


    //int CurrentPreviewCacheNumber = 1;

    //总点数
    EditText editTextTotal;
    EditText editTextName;
    public static Integer pointNumber;

    ProgressDialog progress;

    public boolean isDisplaying = false;
    public static ActivitySetScrew myself;

    TextView txtCurrentRowNum;
    Button btnSortY;
    boolean sortFlag = true;


    TextView editFormular;


    //当前点开始运行
    int startPoint = 0;
    int endPoint = 0;
    int nextPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_screw);
        initialMethod();

        editTextTotal = (EditText) findViewById(R.id.editTextTotal);
        editTextTotal.setInputType(InputType.TYPE_NULL);//屏蔽输入法
        editTextTotal.setOnClickListener(getL());

        editFormular = (TextView) findViewById(R.id.editFormular);
        editFormular.setInputType(InputType.TYPE_NULL);//屏蔽输入法
        editFormular.setOnClickListener(getL2());


        //test
        Button btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Log.d("model test:", String.valueOf(adapter.getItem(2).getX())
                                + "--" + String.valueOf(adapter.getItem(2).getY())
                                + "--" + String.valueOf(adapter.getItem(2).getZ())
                                + "--" + adapter.getItem(2).getDirection()
                );*/
                finish();
            }
        });

        myself = this;

        initialListView();
    }

    private void initialListView() {
        ShowProgress();
        //从系统参数数据开始读取
        MainActivity.mainActivity.sendMessage("80", "02 80 00 82 ");
    }

    public void ShowBTMessage(String sourceMsg, CommandManager commandManager) {

        if (sourceMsg == "80") {
            CommandSys commandSys = commandManager.commandSys;
            CustomApplication.mCommandSys = commandSys;

            //etLeftboxX.setText(String.valueOf(commandSys.para4_leftBoxX));
            //etLeftboxZ.setText(String.valueOf(commandSys.para4_leftBoxZ));
            //etRightboxX.setText(String.valueOf(commandSys.para5_rightBoxX));
            //etRightboxZ.setText(String.valueOf(commandSys.para5_rightBoxZ));

            editFormular.setText(String.valueOf(commandSys.sysPara_formulaNum));

            Log.d("ShowBTMessage", "at ActivitySetScrew 80 系统设置。");
            //progress.dismiss();
            MainActivity.mainActivity.sendMessage("80B0", "02 B0 00 B2 ");
            return;
        }

        if (sourceMsg == "80B0") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            if (commandStatus == null) Log.d("null", "!!!! is null");

            tgA.setChecked(commandStatus.a);
            tgC.setChecked(commandStatus.c);

            Log.d("ShowBTMessage", "at ActivitySetScrew  80B0 状态信息");


            String strCmd = CommandSetScrew.GetSendString(CustomApplication.mCommandSys.sysPara_formulaNum);
            MainActivity.mainActivity.sendMessage("8X", strCmd);
            return;
        }

        if (sourceMsg == "B0") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            if (commandStatus == null) Log.e("null", "!!!! is null");

            tgA.setChecked(commandStatus.a);
            tgC.setChecked(commandStatus.c);

            Log.d("ShowBTMessage", "at ActivitySetScrew  B0 状态信息");
            progress.dismiss();

            return;
        }

        if (sourceMsg == "8X") {
            CommandSetScrew commandSetScrew = commandManager.commandSetScrew;
            CustomApplication.mCommandSetScrew = commandSetScrew;
            Log.d("test", "name is " + commandSetScrew.name);

            //读取到点集合数据
            list = getModelByCMD(commandSetScrew);
            ListView lv = (ListView) findViewById(R.id.listview);
            Log.d("test", "load num = " + list.size());
            adapter = new InteractiveArrayAdapter(myself, list);
            lv.setAdapter(adapter);

            editTextTotal.setText(String.valueOf(commandSetScrew.totalPoint));
            pointNumber = commandSetScrew.totalPoint;
            editTextName.setText(commandSetScrew.name);
            UpdateListView();

            progress.dismiss();

            return;
        }

        if (sourceMsg == "9X") {
            if (commandManager.byteReply[0] == (byte) 0x06) {
                //tgElectricity.setChecked(!tgElectricity.isChecked());
                Log.d("ShowBTMessage", "at ActivitySetScrew 9x 系统设置。OK");
                progress.dismiss();
            }
            return;
        }


        if (sourceMsg == "reset") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySys reset 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivityPara reset 系统设置。 on");

                progress.dismiss();
            }

            return;
        }

        //region 该点运行到
        if (sourceMsg == "RunXY") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySetScrew RunXY 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivitySetScrew RunXY 系统设置。 on");
                MainActivity.mainActivity.sendMessage("B0", "02 B0 00 B2 ");
            }

            return;
        }

        if (sourceMsg == "RunXYZ") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySetScrew RunXYZ 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivitySetScrew RunXYZ 系统设置。 on");
                MainActivity.mainActivity.sendMessage("B0", "02 B0 00 B2 ");
            }

            return;
        }

        if (sourceMsg == "RunAlone") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySetScrew RunAlone 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivitySetScrew RunAlone 系统设置。 on");
                MainActivity.mainActivity.sendMessage("B0", "02 B0 00 B2 ");
            }

            return;
        }


        if (sourceMsg == "RunNext") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySetScrew RunAlone 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                //progress.dismiss();
                Log.d("ShowBTMessage", "at ActivitySetScrew RunAlone 系统设置。 on");
                nextPoint = nextPoint + 1;
                if (nextPoint <= endPoint) {
                    //继续运行下一个指令
                    RunThePoint(nextPoint, "RunNext");
                } else {
                    MainActivity.mainActivity.sendMessage("B0", "02 B0 00 B2 ");
                }
            }


            return;
        }


        ///////////////////////////
        ///////////////////////////


        if (sourceMsg == "SetPoint") {
            if (commandManager.byteReply[0] == (byte) 0x13)
                Log.d("ShowBTMessage", "at ActivitySetScrew SetPoint 系统设置。 off");
            if (commandManager.byteReply[0] == (byte) 0x11) {
                Log.d("ShowBTMessage", "at ActivitySetScrew SetPoint 系统设置。 on");

                MainActivity.mainActivity.sendMessage("SetPoint2", "02 B0 00 B2 ");
            }
            return;
        }

        if (sourceMsg == "SetPoint2") {
            CommandStatus commandStatus = commandManager.commandStatus;
            CustomApplication.mCommandStatus = commandStatus;
            if (commandStatus == null) Log.e("null", "!!!! is null");


            Model m = list.get(Integer.parseInt(txtCurrentRowNum.getText().toString()) - 1);
            int x = commandStatus.x;
            int y = commandStatus.y;
            int z = commandStatus.z;
            m.setX(x);
            m.setY(y);
            m.setZ(z);
            adapter.notifyDataSetChanged();

            Log.d("ShowBTMessage", "at ActivitySetScrew 状态信息 SetPoint2");
            progress.dismiss();

            return;
        }


        //endregion


        //region 状态切换
        if (sourceMsg == "status") {
            if (commandManager.byteReply[0] == (byte) 0x06) {
                //tgElectricity.setChecked(!tgElectricity.isChecked());
                Log.d("ShowBTMessage", "at ActivityPara status 系统设置。OK");
                progress.dismiss();
            }
            return;
        }
        //endregion
    }

    private List<Model> getModelByCMD(CommandSetScrew commandSetScrew) {
        List<Model> tempMoel = new ArrayList<Model>();

        Log.d("test getModelByCMD", "commandSetScrew.pointList size = " + commandSetScrew.pointList.size());
        int index = 1;
        for (SetScrewPoint p : commandSetScrew.pointList) {
            int xn = p.x;
            int yn = p.y;
            int zn = p.z;
            String c = p.c == 1 ? "左" : "右";

            Model temp = get(index, xn, yn, zn, c, true);
            //Log.d("test","id = "+index);
            tempMoel.add(temp);
            index++;
        }

        return tempMoel;

    }


    private void ShowProgress() {
        progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true, true);

    }


    private void deleteInit() {
        progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true);
        //新开线程做长数据初始化
        thread = new Thread() {
            @Override
            public void run() {
                doLongOperation();
                try {
                    // code runs in a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ListView lv = (ListView) findViewById(R.id.listview);
                            adapter = new InteractiveArrayAdapter(myself, list);
                            lv.setAdapter(adapter);

                            progress.dismiss();

                        }
                    });
                } catch (final Exception ex) {
                    Log.i("---", "Exception in thread");
                }
            }
        };
        thread.start();
    }

    private void initialMethod() {
        editTextTotal = (EditText) findViewById(R.id.editTextTotal);
        editTextName = (EditText) findViewById(R.id.editTextName);
        pointNumber = Integer.valueOf(editTextTotal.getText().toString());
        Log.d("pointNumber", " n = " + pointNumber);

        spnCache = (Spinner) findViewById(R.id.spCache);

        //预览
        btnPreviewCach = (Button) findViewById(R.id.btnPreviewCach);
        btnPreviewCach.setOnClickListener(getDialogPreview());

        btnReadCache = (Button) findViewById(R.id.btnReadCache);
        btnSaveCache = (Button) findViewById(R.id.btnSaveCache);

        btnReadCache.setOnClickListener(ReadCache());
        btnSaveCache.setOnClickListener(SaveCache());

        btnPreviewFormula = (Button) findViewById(R.id.btnPreviewFormula);
        btnPreviewFormula.setOnClickListener(getDialogPreview());


        /*btnReadDBModel = (Button) findViewById(R.id.btnReadDBModel);
        btnSaveDBModel = (Button) findViewById(R.id.btnSaveDBModel);
        btnReadDBModel.setOnClickListener(ReadDBModel());
        btnSaveDBModel.setOnClickListener(SaveDBModel());*/


        //矩阵、校正
        btnMatrix = (Button) findViewById(R.id.btnMatrix);
        btnCorrection = (Button) findViewById(R.id.btnCorrection);
        btnMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityMatrix.class);
                intent.putExtra("totalPointNumber", editTextTotal.getText().toString());
                startActivityForResult(intent, 123);
                //startActivity(intent);

            }
        });

        btnCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityCorrection.class);
                intent.putExtra("totalPointNumber", editTextTotal.getText().toString());
                startActivityForResult(intent, 456);
            }
        });


        txtCurrentRowNum = (TextView) findViewById(R.id.txtCurrentRowNum);
        btnSortY = (Button) findViewById(R.id.btnSortY);
        btnSortY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sort(new ModelComparator(sortFlag));
                sortFlag = !sortFlag;
                UpdateListView();
            }
        });

        InitSpinner();

        tgA = (ToggleButton) findViewById(R.id.tgA);
        tgC = (ToggleButton) findViewById(R.id.tgC);

        spFormular = (Spinner) findViewById(R.id.spFormular);
        btnReadFormula = (Button) findViewById(R.id.btnReadFormula);
        btnSaveFormular = (Button) findViewById(R.id.btnSaveFormular);
        //读取螺丝机的配方
        btnReadFormula.setOnClickListener(ReadFormular());
        //保存螺丝机的配方
        btnSaveFormular.setOnClickListener(SaveFormular());

        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                //发送复位指令
                MainActivity.mainActivity.sendMessage("reset", "02 A0 00 A2 ");
            }
        });


        RunCurrrentPoint();

        StatusSwitch();

    }


    //region 缓存预览、缓存读取、缓存保存
    @NonNull
    private View.OnClickListener SaveCache() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ActivitySetScrew.this).setTitle("系统提示")//设置对话框标题
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

                                FileHelper fileHelper = new FileHelper(ActivitySetScrew.this);

                                CommandSetScrew cmd = new CommandSetScrew();
                                cmd.totalPoint = Integer.parseInt(editTextTotal.getText().toString());
                                cmd.name = editTextName.getText().toString();

                                for (Model m : list) {
                                    SetScrewPoint point = new SetScrewPoint();
                                    point.x = m.getX();
                                    point.y = m.getY();
                                    point.z = m.getZ();
                                    point.c = m.getDirection() == "左" ? 0 : 1;
                                    cmd.pointList.add(point);
                                }
                                cmd.ModelToByteArray();
                                try {
                                    String temp = new String(cmd.bytesList, "ISO-8859-1");
                                    fileHelper.writeFile("FileSetScrew" + (spnCache.getSelectedItemPosition() + 1), temp);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d("fileHelper", "write over;");

                                Toast.makeText(ActivitySetScrew.this, "缓存写入完成",
                                        Toast.LENGTH_SHORT).show();

                                //finish();
                            }

                        })
                        .show();//在按键响应事件中显示此对话框


            }
        };
    }

    @NonNull
    private View.OnClickListener ReadCache() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(ActivitySetScrew.this)
                        .setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要读取缓存" + (spnCache.getSelectedItemPosition() + 1))//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //取消按钮的响应事件，不错什么事情
                                //finish();
                            }

                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                FileHelper fileHelper = new FileHelper(ActivitySetScrew.this);
                                try {
                                    String temp = fileHelper.readFile("FileSetScrew" + (spnCache.getSelectedItemPosition() + 1));
                                    byte[] cmdBytes = temp.getBytes("ISO-8859-1");
                                    CommandSetScrew cmd = new CommandSetScrew();
                                    cmd.ByteArrayToModel(cmdBytes, 0);

                                    editTextTotal.setText(String.valueOf(cmd.totalPoint));
                                    editTextName.setText(cmd.name);

                                    list.clear();
                                    int index = 0;
                                    for (SetScrewPoint p : cmd.pointList) {
                                        index++;
                                        Model m = new Model(index, p.x, p.y, p.z, p.c == 0 ? "左" : "右", true);
                                        list.add(m);
                                    }
                                    UpdateListView();


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(ActivitySetScrew.this, "缓存读取完成",
                                        Toast.LENGTH_SHORT).show();
                            }

                        })
                        .show();//在按键响应事件中显示此对话框

            }
        };
    }
    //endregion

    //region 按钮操作，该点运行到坐标等
    private void RunCurrrentPoint() {
        //该点运行XY
        btnRunXY = (Button) findViewById(R.id.btnRunXY);
        btnRunXY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                Model m = list.get(Integer.parseInt(txtCurrentRowNum.getText().toString()));
                int x = m.getX();
                int y = m.getY();
                int z = m.getZ();

                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.RunToXYZString(x, y, 0);
                MainActivity.mainActivity.sendMessage("RunXY", cmd);

            }
        });


        //该点运行XYZ
        btnRunXYZ = (Button) findViewById(R.id.btnRunXYZ);
        btnRunXYZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                Model m = list.get(Integer.parseInt(txtCurrentRowNum.getText().toString()));
                int x = m.getX();
                int y = m.getY();
                int z = m.getZ();

                CommandMotor cmdMotor = new CommandMotor();
                String cmd = cmdMotor.RunToXYZString(x, y, z);
                MainActivity.mainActivity.sendMessage("RunXYZ", cmd);
            }
        });


        //该点单独运行
        btnRunAlone = (Button) findViewById(R.id.btnRunAlone);
        btnRunAlone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                int i = Integer.parseInt(txtCurrentRowNum.getText().toString());
                String cmd = "RunAlone";
                RunThePoint(i, cmd);
            }
        });


        //该点开始运行
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                //循环所有点来进行当前点开始
                //当前点
                startPoint = Integer.parseInt(txtCurrentRowNum.getText().toString()) - 1;
                //结束点
                endPoint = Integer.parseInt(editTextTotal.getText().toString()) - 1;
                Log.d("test", "btnStart setOnClickListener startPoint=" + startPoint + ";endPoint=" + endPoint);
                nextPoint = startPoint;
                RunThePoint(nextPoint, "RunNext");

            }
        });


        //当前位置为该点坐标
        btnSetPoint = (Button) findViewById(R.id.btnSetPoint);
        btnSetPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();

                MainActivity.mainActivity.sendMessage("SetPoint", "02 A4 00 A6 ");

            }
        });
    }

    public void RunThePoint(int pointIndex, String nextCmd) {
        Model m = list.get(pointIndex);
        int x = m.getX();
        int y = m.getY();
        int z = m.getZ();
        String c = m.getDirection();

        CommandMotor cmdMotor = new CommandMotor();
        String cmd = null;
        if (c == "右") {
            cmd = cmdMotor.SetRightScrewString(x, y, z);
        } else if (c == "左") {
            cmd = cmdMotor.SetLeftScrewString(x, y, z);
        }
        MainActivity.mainActivity.sendMessage(nextCmd, cmd);
    }

    private void TestCommment() {

    }
    //endregion

    //region 按钮操作，状态切换
    private void StatusSwitch() {

        tgC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandC();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }

        });

        tgA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress();
                String cmd = CustomApplication.mCommandStatus.writeStatusCommandA();
                MainActivity.mainActivity.sendMessage("status", cmd);

            }

        });
    }
    //endregion


    @NonNull
    private View.OnClickListener ReadFormular() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int index = spFormular.getSelectedItemPosition() + 1;

                new AlertDialog.Builder(ActivitySetScrew.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要读取螺丝机配方" + (index) + "?")//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                //finish();
                            }

                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                String strCmd = CommandSetScrew.GetSendString(index);
                                MainActivity.mainActivity.sendMessage("8X", strCmd);
                                ShowProgress();

                            }

                        })
                        .show();//在按键响应事件中显示此对话框


            }
        };
    }

    @NonNull
    private View.OnClickListener SaveFormular() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int index = spFormular.getSelectedItemPosition() + 1;

                new AlertDialog.Builder(ActivitySetScrew.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("确定要保存螺丝机配方" + (index) + "?")//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                //finish();
                            }

                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                CommandSetScrew commandSetScrew = new CommandSetScrew();
                                commandSetScrew.totalPoint = Integer.valueOf(editTextTotal.getText().toString());
                                commandSetScrew.name = editTextName.getText().toString();
                                ListViewToModel(commandSetScrew);
                                commandSetScrew.ModelToByteArray();

                                String strCmd = commandSetScrew.GetSendStringForSaving(index);
                                MainActivity.mainActivity.sendMessage("9X", strCmd);
                                ShowProgress();

                            }

                        })
                        .show();//在按键响应事件中显示此对话框


            }
        };
    }

    private void ListViewToModel(CommandSetScrew commandSetScrew) {
        commandSetScrew.pointList.clear();
        for (Model m : list) {
            SetScrewPoint p = new SetScrewPoint();
            p.x = m.getX();
            p.y = m.getY();
            p.z = m.getZ();
            p.c = m.getDirection() == "左" ? 1 : 0;
            commandSetScrew.pointList.add(p);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("matrix activity 关闭了", " requestCode =" + requestCode + " -- resultCode = " + resultCode);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            String timeNumber = data.getExtras().getString("TimeNumber");//得到新Activity 关闭后返回的数据
            editTextTotal.setText(timeNumber);
            pointNumber = Integer.parseInt(timeNumber);


            SetDataBack();


        } else if (requestCode == 456 && resultCode == RESULT_OK) {

            SetDataBack();
        }
    }

    private void SetDataBack() {
        //重新设置listview为矩阵设置的
        progress = ProgressDialog.show(this, "请稍候",
                "处理数据中，请稍候...", true);
        Log.d("requestCode", "开始线程处理数据");
        //新开线程做长数据初始化
        new Thread() {
            @Override
            public void run() {
                tempList = new ArrayList<Model>();
                for (int i = 0; i < listMatrixBack.size(); i++) {
                    Model p = listMatrixBack.get(i);
                    Model m = get((i + 1), p.getX(), p.getY(), p.getZ(), p.getDirection(), p.isAvailable());
                    tempList.add(m);
                }

                try {
                    // code runs in a thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            list.clear();
                            list.addAll(tempList);
                            //adapter.notifyDataSetChanged();
                            UpdateListView();
                            Log.d("更新listview", " notifyDataSetChanged ");

                            progress.dismiss();
                        }
                    });
                } catch (final Exception ex) {
                    Log.i("线程异常-", "Exception in thread =>" + ex.getMessage());
                }
            }
        }.start();
    }

    @NonNull
    private View.OnClickListener ReadDBModel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(spinnerCache.getSelectedItem().toString());
                List<Model> listDBModel = getModel(n);
                list.clear();
                list.addAll(listDBModel);
                adapter.notifyDataSetChanged();
            }
        };
    }

    @NonNull
    private View.OnClickListener SaveDBModel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(v.getContext(), "请稍候",
                        "数据保存中，请稍候...", true);
                new Thread() {
                    @Override
                    public void run() {

                        //...
                        setModel(list);


                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                }
                            });
                        } catch (final Exception ex) {
                            Log.i("---", "Exception in thread");
                        }
                    }
                }.start();

            }
        };
    }

    @NonNull
    private View.OnClickListener getDialogPreview() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCurrent = (Button) v;
                btnCurrent.setEnabled(false);

                dialogPreview = new DialogPreview();
                Bitmap bitmap = null;
                //如果是预览配方的直接预览当前的
                if (v.getId() == R.id.btnPreviewFormula) {
                    bitmap = getBitmap(list);
                } else if (v.getId() == R.id.btnPreviewCach) {
                    //预览是缓存的读取数据库的
                    int n = Integer.parseInt(spinnerCache.getSelectedItem().toString());
                    List<Model> listDBModel = getModel(n);
                    bitmap = getBitmap(listDBModel);
                }

                dialogPreview.bitmap = bitmap;
                dialogPreview.show(getSupportFragmentManager(), "DialogPreview");
            }
        };
    }


    @NonNull
    private Bitmap getBitmap(List<Model> listModel) {

        float scaleRate = 0.1f;

        Bitmap bitmap = Bitmap.createBitmap(340, 340, Bitmap.Config.ARGB_8888);
        //创建一个canvas对象，并且开始绘图
        Canvas canvas = new Canvas(bitmap);

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        //mPaint.setStrokeWidth(2f);
        Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setStrokeWidth(3f);

        //或整个框框 需要获取两个点
        int negativeX = -10;
        int negativeY = 0;
        int x = 3300;
        int y = 3000;

        canvas.drawLine(0, 0, 0, y * scaleRate, mPaint);
        canvas.drawLine(0, 0, x * scaleRate, 0, mPaint);

        canvas.drawLine(0, y * scaleRate, x * scaleRate, y * scaleRate, mPaint);
        canvas.drawLine(x * scaleRate, 0, x * scaleRate, y * scaleRate, mPaint);

        //其他的点
        ArrayList<float[]> pointList = new ArrayList<float[]>();
        for (int i = 0; i < listModel.size(); i++) {

            Model m = listModel.get(i);
            float[] arr = new float[2];
            arr[0] = m.getX();
            arr[1] = m.getY();
            pointList.add(arr);
            if (m.getId() >= pointNumber) {
                break;
            }
        }


        for (int i = 0; i < pointList.size(); i++) {
            float[] p = pointList.get(i);

            canvas.drawPoint(p[0] * scaleRate, (y - p[1]) * scaleRate, mPaint2);
        }

        return bitmap;
    }


    @NonNull
    private View.OnClickListener getL() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currNum = ((EditText) v);
                currNum.setEnabled(false);
                //弹出数字输入对话框

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "1000");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }

        };
    }

    @NonNull
    private View.OnClickListener getL2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currNum = ((EditText) v);
                currNum.setEnabled(false);
                //弹出数字输入对话框

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "10");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }

        };
    }


    private void InitSpinner() {
        final String[] from = {"配方缓存1", "配方缓存2", "配方缓存3", "配方缓存4", "配方缓存5", "配方缓存6", "配方缓存7", "配方缓存8", "配方缓存9", "配方缓存10",};
        spinnerCache = (Spinner) findViewById(R.id.spCache);
        integerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, from);
        spinnerCache.setAdapter(integerArrayAdapter);


        //
        final String[] from2 = {"配方1", "配方2", "配方3", "配方4", "配方5", "配方6", "配方7", "配方8", "配方9", "配方10",};
        spinnerFormular = (Spinner) findViewById(R.id.spFormular);
        spinnerFormular.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, from2));


    }


    //读取数据库
    protected void doLongOperation() {
        try {
            Thread.sleep(50);
            list = getModel(1);
        } catch (InterruptedException e) {
        }

    }

    private List<Model> getModel(int cacheNum) {

        List<Model> tempMoel = new ArrayList<Model>();

        DatabaseHelper database = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = database.getWritableDatabase();

        Cursor c = db.rawQuery("select * from SetScrew where cacheNum=?", new String[]{String.valueOf(cacheNum)});
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("number"));
            int xn = c.getInt(c.getColumnIndex("xn"));
            int yn = c.getInt(c.getColumnIndex("yn"));
            int zn = c.getInt(c.getColumnIndex("zn"));
            String direction = c.getString(c.getColumnIndex("direction"));
            Model temp = get(id, xn, yn, zn, direction, true);
            tempMoel.add(temp);
        }
        db.close();

        return tempMoel;
    }

    private void setModel(List<Model> listModel) {
        for (int i = 0; i < listModel.size(); i++) {
            int cacheNum = Integer.parseInt(spinnerCache.getSelectedItem().toString());
            int id = listModel.get(i).getId();
            int xn = listModel.get(i).getX();
            int yn = listModel.get(i).getY();
            int zn = listModel.get(i).getZ();
            String direction = listModel.get(i).getDirection();

            //
            DatabaseHelper database = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db = database.getWritableDatabase();
            String sql = "update SetScrew set xn=?,yn=?,zn=?,direction=? where cacheNum=? and number =?";
            db.execSQL(sql, new String[]{String.valueOf(xn), String.valueOf(yn), String.valueOf(zn), direction,
                    String.valueOf(cacheNum), String.valueOf(id)});
            db.close();
        }
        Log.d("db upate SetScrew ", " ... ");
    }


    private Model get(int id, int x, int y, int z, String direction, boolean available) {
        return new Model(id, x, y, z, direction, available);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_screw, menu);
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

    @Override
    public void DialogAdjustOnConfirm(DialogInterface dialogInterface, Bundle bundle) {
        //赋值回原来控件
        EditText editTextX = (EditText) currentLinearLayout.findViewById(R.id.editTextX);
        editTextX.setText(bundle.getString("x"));
        Model element = (Model) editTextX.getTag();
        element.setX(Integer.parseInt(bundle.getString("x")));

        EditText editTextY = (EditText) currentLinearLayout.findViewById(R.id.editTextY);
        editTextY.setText(bundle.getString("y"));
        element.setY(Integer.parseInt(bundle.getString("y")));

        EditText editTextZ = (EditText) currentLinearLayout.findViewById(R.id.editTextZ);
        editTextZ.setText(bundle.getString("z"));
        element.setZ(Integer.parseInt(bundle.getString("z")));


        //更改里面的数据源内容

        //adapter.notifyDataSetChanged();

        SetSelectedStatus();

        RemoveDialog(dialogAdjust);
    }

    //这里直接修改界面或出现滚动后，选中不正确的问题
    private void SetSelectedStatus() {
        if (currentEditText != null) {
            currentEditText.setEnabled(true);
            //currentLinearLayout.setBackground(drawable);
            lastLinearLayout = currentLinearLayout;
        }
    }

    @Override
    public void DialogAdjustOnCancle(DialogInterface dialogInterface, Bundle bundle) {

        SetSelectedStatus();

        RemoveDialog(dialogAdjust);
    }

    @Override
    public void DialogAdjustOnDismiss(DialogInterface dialogInterface, Bundle bundle) {


        SetSelectedStatus();

        RemoveDialog(dialogAdjust);
    }


    //点击只选择，不弹编辑面板
    public void EditTextClickFocus(View v) {
        EditPoint((EditText) v, true);
    }


    public void EditTextClick(View v) {
        EditPoint((EditText) v, false);
    }

    private void EditPoint(EditText v, boolean focus) {
        //if (focus) {
        //取消上次的
        //SetSelectedStatus();
        //}


        currentEditText = v;

        //if (lastLinearLayout != null) lastLinearLayout.setBackground(drawable);
        currentLinearLayout = (LinearLayout) currentEditText.getParent();
        //drawable = currentLinearLayout.getBackground();
        //currentLinearLayout.setBackgroundColor(Color.parseColor("#EEEEEE"));
        currentEditText.setEnabled(false);
        TextView textView = (TextView) currentLinearLayout.getChildAt(0);
        txtCurrentRowNum.setText(textView.getText().toString());


        SetSelectedRow(textView);

        if (focus) {
            //不弹编辑面板
            return;
        }

        dialogAdjust = new DialogAdjust();
        Bundle args = new Bundle();

        EditText editTextX = (EditText) currentLinearLayout.findViewById(R.id.editTextX);
        args.putString("numberX", editTextX.getText().toString());
        EditText editTextY = (EditText) currentLinearLayout.findViewById(R.id.editTextY);
        args.putString("numberY", editTextY.getText().toString());
        EditText editTextZ = (EditText) currentLinearLayout.findViewById(R.id.editTextZ);
        args.putString("numberZ", editTextZ.getText().toString());


        dialogAdjust.setArguments(args);
        dialogAdjust.show(getSupportFragmentManager(), "DialogAdjust");
    }

    private void SetSelectedRow(TextView textView) {
        for (Model m : list) {
            m.setSelected(false);
        }
        int t = Integer.parseInt(textView.getText().toString());
        Model model = list.get(t - 1);
        model.setSelected(true);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onConfirm(DialogInterface dialogInterface, Bundle bundle) {
        currNum.setText(bundle.getString("number"));
        pointNumber = Integer.valueOf(bundle.getString("number"));
        UpdateListView();


        if (currNum != null) {
            currNum.setEnabled(true);
        }
        RemoveDialog(dialogNumber);
    }

    private void UpdateListView() {
        for (int i = 0; i < list.size(); i++) {
            Model m = list.get(i);
            m.setId(i + 1);
            if (i <= pointNumber) {
                m.setAvailable(true);
            } else {
                m.setAvailable(false);
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCancle(DialogInterface dialogInterface, Bundle bundle) {


        if (currNum != null) {
            currNum.setEnabled(true);
        }
        RemoveDialog(dialogNumber);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface, Bundle bundle) {


        if (currNum != null) {
            currNum.setEnabled(true);
        }
        RemoveDialog(dialogNumber);
    }


    ///////////////////////preview dialog
    @Override
    public void DialogPreviewOnConfirm(DialogInterface dialogInterface, Bundle bundle) {
        btnCurrent.setEnabled(true);

        RemoveDialog(dialogPreview);
    }

    @Override
    public void DialogPreviewOnCancle(DialogInterface dialogInterface, Bundle bundle) {
        btnCurrent.setEnabled(true);


        RemoveDialog(dialogPreview);
    }

    private void RemoveDialog(DialogFragment dialogFragment) {
        if (dialogFragment == null) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(dialogFragment);
        ft.commit();

    }

    @Override
    public void DialogPreviewOnDismiss(DialogInterface dialogInterface, Bundle bundle) {
        btnCurrent.setEnabled(true);

        RemoveDialog(dialogPreview);

    }


    ////////////////////

}
