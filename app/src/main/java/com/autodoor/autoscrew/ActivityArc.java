package com.autodoor.autoscrew;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.autodoor.autoscrew.data.CommandSys;
import com.autodoor.autoscrew.dialog.DialogNumber;

public class ActivityArc extends AppCompatActivity implements DialogNumber.NoticeDialogListener {


    EditText editText1;
    EditText editText2;
    EditText etCurrent;
    DialogFragment customDialog;
    Switch aSwitch;

    ProgressDialog progress;

    public static ActivityArc myself;
    public boolean isDisplaying=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);

        //圆弧默认启用
        aSwitch = (Switch) findViewById(R.id.switch1);
        aSwitch.setChecked(true);

        //图片显示
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arcpic);
        imageView.setImageBitmap(bitmap);

        //禁止默认弹出输入键盘
        editText1 = (EditText) findViewById(R.id.etArc1);
        editText1.setInputType(InputType.TYPE_NULL);// 屏蔽输入法
        editText2 = (EditText) findViewById(R.id.etArc2);
        editText2.setInputType(InputType.TYPE_NULL);// 屏蔽输入法

        setOnFocusListener();


        ReadData();

        myself = this;
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

    public void ReadData(){
        progress = ProgressDialog.show(this, "请稍候",
                "初始数据中，请稍候...", true,true);

        MainActivity.mainActivity.sendMessage("delete","02 80 00 82 ");
    }

    public void ShowBTMessage(CommandSys commandSys){


        editText1.setText(String.valueOf(commandSys.arc_getScrewSide));
        editText2.setText(String.valueOf(commandSys.arc_lockScrewSide));
        aSwitch.setChecked(commandSys.arc_onOrOff);

        Log.d("ShowBTMessage", "at ActivityArc 圆弧设置。");
        progress.dismiss();

    }



    public void setOnFocusListener() {

        editText1.setOnClickListener(getL());
        editText2.setOnClickListener(getL());
    }

    @NonNull
    private View.OnClickListener getL() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.hasFocus() && customDialog == null) {
                    etCurrent = ((EditText)v);
                    etCurrent.setEnabled(false);
                    //弹出数字输入对话框

                    customDialog = new DialogNumber();
                    Bundle args = new Bundle();
                    args.putString("number", ((EditText) v).getText().toString());
                    customDialog.setArguments(args);
                    customDialog.show(getSupportFragmentManager(), "DialogNumber");
                }
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arc, menu);
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
    }
}
