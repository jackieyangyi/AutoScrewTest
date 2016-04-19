package com.autodoor.autoscrew.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autodoor.autoscrew.R;

/**
 * Created by yangyi on 2015/10/25.
 */
public class DialogNumber extends DialogFragment {

    Bundle args;
    View view;
    EditText etNumber;
    EditText etMin;
    EditText etMax;
    String dialogType;

    boolean neverClick = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_num, null);
        builder.setView(view);


        etNumber = (EditText) view.findViewById(R.id.etNumber);
        etMin = (EditText) view.findViewById(R.id.etMin);
        etMax = (EditText) view.findViewById(R.id.etMax);


        //接收传过来的值
        args = getArguments();
        dialogType = args.getString("dialogType");
        etNumber.setText(args.getString("number"));

        //有指定最大值最小值设定
        if (args.getString("min") != null) {
            etMin.setText(args.getString("min"));
        }
        if (args.getString("max") != null) {
            etMax.setText(args.getString("max"));
        }


        //禁止默认键盘输入
        etNumber.setInputType(InputType.TYPE_NULL);
        etMin.setInputType(InputType.TYPE_NULL);
        etMax.setInputType(InputType.TYPE_NULL);
        etMin.setFocusable(false);
        etMax.setFocusable(false);


        //按钮事件
        DoNumberAction();

        return builder.create();


    }

    private void DoNumberAction() {
        Button btn1 = (Button) view.findViewById(R.id.btn1);
        Button btn2 = (Button) view.findViewById(R.id.btn2);
        Button btn3 = (Button) view.findViewById(R.id.btn3);
        Button btn4 = (Button) view.findViewById(R.id.btn4);
        Button btn5 = (Button) view.findViewById(R.id.btn5);
        Button btn6 = (Button) view.findViewById(R.id.btn6);
        Button btn7 = (Button) view.findViewById(R.id.btn7);
        Button btn8 = (Button) view.findViewById(R.id.btn8);
        Button btn9 = (Button) view.findViewById(R.id.btn9);
        Button btn10 = (Button) view.findViewById(R.id.btn10);//.
        Button btn11 = (Button) view.findViewById(R.id.btn11);//0
        Button btn12 = (Button) view.findViewById(R.id.btn12);//x

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = ((Button) v);
                if (neverClick) {
                    etNumber.setText(btn.getText().toString());
                    neverClick = false;
                } else {
                    etNumber.append(btn.getText().toString());
                }
            }
        };
        btn1.setOnClickListener(clickListener);
        btn2.setOnClickListener(clickListener);
        btn3.setOnClickListener(clickListener);
        btn4.setOnClickListener(clickListener);
        btn5.setOnClickListener(clickListener);
        btn6.setOnClickListener(clickListener);
        btn7.setOnClickListener(clickListener);
        btn8.setOnClickListener(clickListener);
        btn9.setOnClickListener(clickListener);

        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etNumber.getText().toString();
                if (temp.startsWith("-")) {
                    etNumber.setText(etNumber.getText().toString().substring(1));
                } else {
                    etNumber.setText("-" + etNumber.getText().toString());
                }
            }
        });//

        btn11.setOnClickListener(clickListener);

        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etNumber.getText().toString();
                if (temp.length() > 0) {
                    etNumber.setText(etNumber.getText().delete(etNumber.getText().length() - 1, etNumber.getText().length()));
                }

            }
        });//x 还没操作


        //取消
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((NoticeDialogListener) getActivity()).onCancle(null, null);
            }
        });
        //清除
        Button btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNumber.setText("");
            }
        });
        //确定
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //这里要验证当前的字符串要在指定的大小范围内
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
                }

                dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("number", etNumber.getText().toString());
                ((NoticeDialogListener) getActivity()).onConfirm(null, bundle);
            }
        });


    }


    // This is DialogFragment, not Dialog
    @Override
    public void onDismiss(DialogInterface dialog) {
        ((NoticeDialogListener) getActivity()).onCancle(null, null);
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onConfirm(DialogInterface dialogInterface, Bundle bundle);

        public void onCancle(DialogInterface dialogInterface, Bundle bundle);

        public void onDismiss(DialogInterface dialogInterface, Bundle bundle);
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
