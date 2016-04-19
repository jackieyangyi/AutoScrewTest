package com.autodoor.autoscrew.ActSetScrew;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.autodoor.autoscrew.ActivitySetScrew;
import com.autodoor.autoscrew.R;
import com.autodoor.autoscrew.dialog.DialogNumber;

import java.util.ArrayList;
import java.util.List;

public class ActivityMatrix extends AppCompatActivity
        implements DialogNumber.NoticeDialogListener {

    ImageView imageView3;
    public Bitmap bitmap;
    List<Model> list;
    Model selectedModel;
    int totalPointNumber;

    EditText etColumn;
    EditText etRow;

    DialogNumber dialogNumber;
    EditText etCurrent;

    Button btnPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        Intent intent = getIntent();
        totalPointNumber = Integer.valueOf(intent.getStringExtra("totalPointNumber"));

        list = ActivitySetScrew.list;
        selectedModel = list.get(0);

        InitialView();

        imageView3 = (ImageView) findViewById(R.id.imageView3);
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        bitmap = getBitmap(list);
        imageView3.setImageBitmap(bitmap);


        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                int num = totalPointNumber * Integer.parseInt(etColumn.getText().toString()) * Integer.parseInt(etRow.getText().toString());
                intent.putExtra("TimeNumber", String.valueOf(num));
                //设置返回数据
                setResult(RESULT_OK, intent);
                //关闭Activity
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void InitialView() {
        EditText etId = (EditText) findViewById(R.id.etID);
        EditText etXn = (EditText) findViewById(R.id.etXn);
        EditText etYn = (EditText) findViewById(R.id.etYn);
        EditText etZn = (EditText) findViewById(R.id.etZn);
        etId.setText(String.valueOf(selectedModel.getId()));
        etXn.setText(String.valueOf(selectedModel.getX()));
        etYn.setText(String.valueOf(selectedModel.getY()));
        etZn.setText(String.valueOf(selectedModel.getZ()));
        etXn.setEnabled(false);
        etYn.setEnabled(false);
        etZn.setEnabled(false);
        etId.setInputType(InputType.TYPE_NULL);
        etId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrent = ((EditText) v);
                etCurrent.setEnabled(false);

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "100");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }
        });

        etColumn = (EditText) findViewById(R.id.etColumn);
        EditText etCXn = (EditText) findViewById(R.id.etCXn);
        EditText etCYn = (EditText) findViewById(R.id.etCYn);
        EditText etCZn = (EditText) findViewById(R.id.etCZn);
        etColumn.setText("2");
        etCXn.setText(String.valueOf(selectedModel.getX() + 1000));
        etCXn.setInputType(InputType.TYPE_NULL);
        etCXn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrent = ((EditText) v);
                etCurrent.setEnabled(false);

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "5000");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }
        });
        etCYn.setText(String.valueOf(selectedModel.getY()));
        etCYn.setEnabled(false);
        etCZn.setText(String.valueOf(selectedModel.getZ()));
        etCZn.setEnabled(false);
        etColumn.setInputType(InputType.TYPE_NULL);
        etColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrent = ((EditText) v);
                etCurrent.setEnabled(false);

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "10");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }
        });

        etRow = (EditText) findViewById(R.id.etRow);
        EditText etRXn = (EditText) findViewById(R.id.etRXn);
        EditText etRYn = (EditText) findViewById(R.id.etRYn);
        EditText etRZn = (EditText) findViewById(R.id.etRZn);
        etRow.setText("3");
        etRXn.setText(String.valueOf(selectedModel.getX()));
        etRXn.setEnabled(false);
        etRYn.setText(String.valueOf(selectedModel.getY() + 1000));
        etRYn.setInputType(InputType.TYPE_NULL);
        etRYn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrent = ((EditText) v);
                etCurrent.setEnabled(false);

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "5000");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }
        });
        etRZn.setText(String.valueOf(selectedModel.getZ()));
        etRZn.setEnabled(false);
        etRow.setInputType(InputType.TYPE_NULL);
        etRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrent = ((EditText) v);
                etCurrent.setEnabled(false);

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", "10");
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }
        });

        //预览按钮
        btnPreview = (Button) findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = getBitmap(list);
                imageView3.setImageBitmap(bitmap);
            }
        });
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
        ArrayList<Model> pointList = new ArrayList<Model>();
        for (int i = 0; i < listModel.size(); i++) {

            Model m = listModel.get(i);
            float[] arr = new float[2];
            arr[0] = m.getX();
            arr[1] = m.getY();
            pointList.add(new Model(i+1,(int)arr[0],(int)arr[1],m.getZ(),m.getDirection(),m.isAvailable()));
            if (m.getId() >= totalPointNumber) {
                break;
            }
        }


        for (int i = 0; i < pointList.size(); i++) {
            Model p = pointList.get(i);
            canvas.drawPoint(p.getX() * scaleRate, (y - p.getY()) * scaleRate, mPaint2);
        }

        //列处理
        ArrayList<Model> cloumnPointList = new ArrayList<>();
        cloumnPointList.addAll(pointList);
        int CloumnNum = Integer.valueOf(etColumn.getText().toString());
        EditText etCXn = (EditText) findViewById(R.id.etCXn);
        int addNum = Integer.valueOf(etCXn.getText().toString());
        int countNum =0;
        for (int j = 2; j <= CloumnNum; j++) {
            for (int i = 0; i < pointList.size(); i++) {
                countNum++;

                Model p = pointList.get(i);
                float pointX = (p.getX() + addNum * (j - 1)) * scaleRate;
                float pointY = (y - p.getY()) * scaleRate;
                //备份保存使用
                cloumnPointList.add(new Model(countNum,p.getX() + addNum * (j - 1),p.getY(),p.getZ(),p.getDirection(),p.isAvailable()));
                canvas.drawPoint(pointX, pointY, mPaint2);
            }
        }

        //行处理
        ArrayList<Model> RowPointList = new ArrayList<>();
        RowPointList.addAll(cloumnPointList);
        int RowNum = Integer.valueOf(etRow.getText().toString());
        EditText etRYn = (EditText) findViewById(R.id.etRYn);
        addNum = Integer.valueOf(etRYn.getText().toString());
        int countNum2=0;
        for (int j = 2; j <= RowNum; j++) {
            for (int i = 0; i < cloumnPointList.size(); i++) {
                countNum2++;

                Model p = cloumnPointList.get(i);
                float pointX = p.getX() * scaleRate;
                float pointY = ((y - p.getY()) - addNum * (j - 1)) * scaleRate;
                //RowPointList.add(new float[]{p[0], p[1] + addNum * (j - 1)});
                RowPointList.add(new Model(countNum2,p.getX(), p.getY() + addNum * (j - 1),p.getZ(),p.getDirection(),p.isAvailable()));
                canvas.drawPoint(pointX, pointY, mPaint2);
            }
        }

        ActivitySetScrew.listMatrixBack = RowPointList;

        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_matrix, menu);
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

            //关闭Activity
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfirm(DialogInterface dialogInterface, Bundle bundle) {
        //赋值回原来控件
        String temp = bundle.getString("number");
        etCurrent.setText(temp);

        if (etCurrent.getId() == R.id.etID) {
            Model tempModel = list.get(Integer.valueOf(temp) - 1);
            EditText etXn = (EditText) findViewById(R.id.etXn);
            EditText etYn = (EditText) findViewById(R.id.etYn);
            EditText etZn = (EditText) findViewById(R.id.etZn);
            etXn.setText(String.valueOf(tempModel.getX()));
            etYn.setText(String.valueOf(tempModel.getY()));
            etZn.setText(String.valueOf(tempModel.getZ()));

            EditText etCXn = (EditText) findViewById(R.id.etCXn);
            EditText etCYn = (EditText) findViewById(R.id.etCYn);
            EditText etCZn = (EditText) findViewById(R.id.etCZn);
            etCXn.setText(String.valueOf(tempModel.getX() + 1000));
            etCYn.setText(String.valueOf(tempModel.getY()));
            etCZn.setText(String.valueOf(tempModel.getZ()));

            EditText etRXn = (EditText) findViewById(R.id.etRXn);
            EditText etRYn = (EditText) findViewById(R.id.etRYn);
            EditText etRZn = (EditText) findViewById(R.id.etRZn);
            etRXn.setText(String.valueOf(tempModel.getX()));
            etRYn.setText(String.valueOf(tempModel.getY() + 1000));
            etRZn.setText(String.valueOf(tempModel.getZ()));
        }


        etCurrent.setEnabled(true);
        RemoveDialog(dialogNumber);
    }

    @Override
    public void onCancle(DialogInterface dialogInterface, Bundle bundle) {
        etCurrent.setEnabled(true);
        RemoveDialog(dialogNumber);

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface, Bundle bundle) {
        etCurrent.setEnabled(true);
        RemoveDialog(dialogNumber);

    }

    private void RemoveDialog(DialogFragment dialogFragment) {
        if (dialogFragment == null) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(dialogFragment);
        ft.commit();
    }
}
