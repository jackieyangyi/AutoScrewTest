package com.autodoor.autoscrew.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.autodoor.autoscrew.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangyi on 2015/10/25.
 */
public class DialogAdjust extends DialogFragment {

    Bundle args;
    View view;
    EditText editTextX;
    EditText editTextY;
    EditText editTextZ;
    String dialogType;

    ToggleButton toggleButton;

    ScheduledExecutorService myExecuter;

    ProgressDialog progress;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_adjust, null);
        builder.setView(view);


        editTextX = (EditText) view.findViewById(R.id.editTextX);
        editTextY = (EditText) view.findViewById(R.id.editTextY);
        editTextZ = (EditText) view.findViewById(R.id.editTextZ);

        //接收传过来的值
        args = getArguments();
        dialogType = args.getString("dialogType");
        editTextX.setText(args.getString("numberX"));
        editTextY.setText(args.getString("numberY"));
        editTextZ.setText(args.getString("numberZ"));




        //禁止默认键盘输入
        editTextX.setInputType(InputType.TYPE_NULL);
        editTextY.setInputType(InputType.TYPE_NULL);
        editTextZ.setInputType(InputType.TYPE_NULL);
        editTextX.setFocusable(false);
        editTextY.setFocusable(false);
        editTextZ.setFocusable(false);


        //按钮事件
        DoNumberAction();



        return builder.create();
    }



    private void DoNumberAction() {
        Switch aSwitchh = (Switch) view.findViewById(R.id.swElectricity);//电机加电
        Button btnCalculate = (Button) view.findViewById(R.id.btnCalculate);//测算位置

        Button btnLeftX = (Button) view.findViewById(R.id.btnLeftX);
        Button btnRightX = (Button) view.findViewById(R.id.btnRightX);
        Button btnFrontY = (Button) view.findViewById(R.id.btnFrontY);
        Button btnBackY = (Button) view.findViewById(R.id.btnBackY);
        Button btnUpZ = (Button) view.findViewById(R.id.btnUpZ);
        Button btnDownZ = (Button) view.findViewById(R.id.btnDownZ);

        toggleButton =(ToggleButton) view.findViewById(R.id.tgSpeed);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("test",String.valueOf(buttonView.isChecked()));
            }
        });

        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);//确定
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);//取消

        btnLeftX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNumber("Decrease",editTextX);
            }
        });


        //region 长按时间的重复代码
        btnLeftX.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    updateCounter(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            AddNumber("Decrease", editTextX);
                            return false;
                        }
                    }));
                if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
                    stopCounter();
                return false;
            }
        });
        btnFrontY.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    updateCounter(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            AddNumber("Decrease",editTextY);
                            return false;
                        }
                    }));
                if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP))
                    stopCounter();
                return false;
            }
        });
        btnUpZ.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    updateCounter(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            AddNumber("Decrease",editTextZ);
                            return false;
                        }
                    }));
                if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP))
                    stopCounter();
                return false;
            }
        });
        //endregion

        btnFrontY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNumber("Decrease", editTextY);
            }
        });

        btnUpZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNumber("Decrease", editTextZ);
            }
        });

        //-----------------------
        btnRightX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNumber("Increase", editTextX);
            }
        });
        btnBackY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNumber("Increase", editTextY);
            }
        });
        btnDownZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNumber("Increase", editTextZ);
            }
        });

        //region 长按事件的重复代码
        btnRightX.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    updateCounter(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            AddNumber("Increase", editTextX);
                            return false;
                        }
                    }));
                if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
                    stopCounter();
                return false;
            }
        });
        btnBackY.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    updateCounter(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            AddNumber("Increase", editTextY);
                            return false;
                        }
                    }));
                if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
                    stopCounter();
                return false;
            }
        });
        btnDownZ.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    updateCounter(new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message msg) {
                                    AddNumber("Increase", editTextZ);
                                    return false;
                        }
                    }));
                if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
                    stopCounter();
                return false;
            }
        });
        //endregion



        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = ProgressDialog.show(v.getContext(), "请稍候",
                        "初始数据中，请稍候...", true, true);

                /*dismiss();
                ((NoticeDialogListener) getActivity()).DialogAdjustOnCancle(null, null);*/
            }
        });

        //确定

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*//这里要验证当前的字符串要在指定的大小范围内
                if (etNumber.getText().toString().length() == 0) {
                    Log.d("test", "输入不能为空");
                    Toast.makeText(getContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etNumber.getText().toString().length() > 10) {
                    Log.d("test", "输入的数字太大");
                    Toast.makeText(getContext(), "输入的数字太大", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dialogType == null || !dialogType.equals("password")) {
                    long min = Long.parseLong(etMin.getText().toString());
                    long max = Long.parseLong(etMax.getText().toString());
                    long current = Long.parseLong(etNumber.getText().toString());
                    if (!(current >= min && current <= max)) {
                        Log.d("test", "输入的数字不在有效范围内");
                        Toast.makeText(getContext(), "输入的数字不在有效范围内", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }*/

                dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("x", editTextX.getText().toString());
                bundle.putString("y", editTextY.getText().toString());
                bundle.putString("z", editTextZ.getText().toString());
                ((NoticeDialogListener) getActivity()).DialogAdjustOnConfirm(null, bundle);
            }
        });


    }

    private void AddNumber(String decreaseOrIncrease,EditText editText) {
        int num = Integer.parseInt(editText.getText().toString());
        if(decreaseOrIncrease=="Increase"){
            if(toggleButton.isChecked()){
                num+=10;
            }else {
                num += 1;
            }

        }else{
            if(toggleButton.isChecked()){
                num-=10;
            }else {
                num -= 1;
            }
        }

        editText.setText(String.valueOf(num));
    }

    //长按事件处理
    public void updateCounter(final Handler handler) {
        myExecuter = Executors.newSingleThreadScheduledExecutor();
        myExecuter.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 800, 100, TimeUnit.MILLISECONDS);

    }

    public void stopCounter() {
        if (myExecuter != null) {
            myExecuter.shutdownNow();
            myExecuter = null;
        }
    }


    // This is DialogFragment, not Dialog
    @Override
    public void onDismiss(DialogInterface dialog) {
        ((NoticeDialogListener) getActivity()).DialogAdjustOnDismiss(null, null);
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void DialogAdjustOnConfirm(DialogInterface dialogInterface, Bundle bundle);

        public void DialogAdjustOnCancle(DialogInterface dialogInterface, Bundle bundle);

        public void DialogAdjustOnDismiss(DialogInterface dialogInterface, Bundle bundle);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


}
