package com.autodoor.autoscrew.ActSetScrew;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.autodoor.autoscrew.ActivitySetScrew;
import com.autodoor.autoscrew.R;
import com.autodoor.autoscrew.dialog.DialogAdjust;
import com.autodoor.autoscrew.dialog.DialogNumber;

import java.util.ArrayList;
import java.util.List;

public class ActivityCorrection extends AppCompatActivity
        implements DialogNumber.NoticeDialogListener, DialogAdjust.NoticeDialogListener {
    ImageView imageView3;
    Bitmap bitmap;
    List<Model> list;
    int totalPointNumber;

    DialogNumber dialogNumber;
    EditText etCurrent;

    private EditText currentEditText;
    private LinearLayout currentLinearLayout;
    DialogAdjust dialogAdjust;


    EditText editTextNum;
    EditText editTextX;
    EditText editTextY;
    EditText editTextZ;

    EditText editTextNum2;
    EditText editTextX2;
    EditText editTextY2;
    EditText editTextZ2;

    List<Model> correctionModel;//校正后保存的坐标点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);


        Intent intent = getIntent();
        totalPointNumber = Integer.valueOf(intent.getStringExtra("totalPointNumber"));

        list = ActivitySetScrew.list;

        imageView3 = (ImageView) findViewById(R.id.imageView3);
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.matrix);
        bitmap = getBitmap(list);
        imageView3.setImageBitmap(bitmap);

        InitView();


        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCorrection.this.finish();
                //Toast.makeText(v.getContext(), this.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "确定按钮", Toast.LENGTH_SHORT).show();
                //数据是使用Intent返回
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Button btnPreview = (Button) findViewById(R.id.btnPreview);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "预览按钮", Toast.LENGTH_SHORT).show();
                GetTheCorrectionBitmap();
            }
        });


    }

    private void InitView() {
        Model temp = list.get(0);
        editTextNum = (EditText) findViewById(R.id.editTextNum);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);
        editTextZ = (EditText) findViewById(R.id.editTextZ);
        editTextNum.setInputType(InputType.TYPE_NULL);
        editTextX.setInputType(InputType.TYPE_NULL);
        editTextY.setInputType(InputType.TYPE_NULL);
        editTextZ.setInputType(InputType.TYPE_NULL);
        editTextNum.setText(String.valueOf(temp.getId()));
        editTextX.setText(String.valueOf(temp.getX()));
        editTextY.setText(String.valueOf(temp.getY()));
        editTextZ.setText(String.valueOf(temp.getZ()));

        editTextX.setOnClickListener(getListenDialogAjust());
        editTextY.setOnClickListener(getListenDialogAjust());
        editTextZ.setOnClickListener(getListenDialogAjust());
        editTextNum.setOnClickListener(getL());
        /*-----------*/


        temp = list.get(totalPointNumber - 1);
        editTextNum2 = (EditText) findViewById(R.id.editTextNum2);
        editTextX2 = (EditText) findViewById(R.id.editTextX2);
        editTextY2 = (EditText) findViewById(R.id.editTextY2);
        editTextZ2 = (EditText) findViewById(R.id.editTextZ2);
        editTextNum2.setInputType(InputType.TYPE_NULL);
        editTextNum2.setOnClickListener(getL());
        editTextX2.setInputType(InputType.TYPE_NULL);
        editTextY2.setInputType(InputType.TYPE_NULL);
        editTextZ2.setInputType(InputType.TYPE_NULL);
        editTextNum2.setText(String.valueOf(temp.getId()));
        editTextX2.setText(String.valueOf(temp.getX()));
        editTextY2.setText(String.valueOf(temp.getY()));
        editTextZ2.setText(String.valueOf(temp.getZ()));

        editTextX2.setOnClickListener(getListenDialogAjust());
        editTextY2.setOnClickListener(getListenDialogAjust());
        editTextZ2.setOnClickListener(getListenDialogAjust());


    }

    @NonNull
    private View.OnClickListener getListenDialogAjust() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentEditText = (EditText) v;
                currentLinearLayout = (LinearLayout) currentEditText.getParent();
                currentEditText.setEnabled(false);


                dialogAdjust = new DialogAdjust();
                Bundle args = new Bundle();

                EditText editTextX = (EditText) currentLinearLayout.findViewById(R.id.editTextX);
                EditText editTextX2 = (EditText) currentLinearLayout.findViewById(R.id.editTextX2);
                if (editTextX != null) {
                    args.putString("numberX", editTextX.getText().toString());
                    EditText editTextY = (EditText) currentLinearLayout.findViewById(R.id.editTextY);
                    args.putString("numberY", editTextY.getText().toString());
                    EditText editTextZ = (EditText) currentLinearLayout.findViewById(R.id.editTextZ);
                    args.putString("numberZ", editTextZ.getText().toString());

                } else if (editTextX2 != null) {
                    args.putString("numberX", editTextX2.getText().toString());
                    EditText editTextY2 = (EditText) currentLinearLayout.findViewById(R.id.editTextY2);
                    args.putString("numberY", editTextY2.getText().toString());
                    EditText editTextZ2 = (EditText) currentLinearLayout.findViewById(R.id.editTextZ2);
                    args.putString("numberZ", editTextZ2.getText().toString());

                }

                dialogAdjust.setArguments(args);
                dialogAdjust.show(getSupportFragmentManager(), "DialogAdjust");
            }
        };
    }

    @NonNull
    private View.OnClickListener getL() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCurrent = ((EditText) v);
                etCurrent.setEnabled(false);

                dialogNumber = new DialogNumber();
                Bundle args = new Bundle();
                args.putString("number", ((EditText) v).getText().toString());
                args.putString("min", "1");
                args.putString("max", String.valueOf(totalPointNumber));
                dialogNumber.setArguments(args);
                dialogNumber.show(getSupportFragmentManager(), "DialogNumber");
            }
        };
    }

    @NonNull
    private Bitmap getBitmap(List<Model> listModel) {
        Log.d("listModel总点数：",String.valueOf(listModel.size()));

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
            if (m.getId() >= totalPointNumber) {
                break;
            }
        }

        Log.d("pointList总点数：",String.valueOf(pointList.size()));
        for (int i = 0; i < pointList.size(); i++) {
            float[] p = pointList.get(i);
            canvas.drawPoint(p[0] * scaleRate, (y - p[1]) * scaleRate, mPaint2);
        }

        ActivitySetScrew.listMatrixBack = listModel;

        return bitmap;
    }


    //region 根据一直线的两个坐标点，求直线的斜率
    private double GetSlope(double pointX, double pointY, double pointX2, double pointY2) {
        Log.d("斜率：", "x,y,x2,y2 =" + String.valueOf(pointX) + "," + String.valueOf(pointY) + "," + String.valueOf(pointX2) + "," + String.valueOf(pointY2));
        double result = 0.0;
        result = (pointY2 - pointY) / (pointX2 - pointX);
        Log.d("斜率：", String.valueOf(result));
        return result;
    }

    private void GetTheCorrectionBitmap() {
        Model model = list.get(Integer.valueOf(editTextNum.getText().toString()) - 1);
        Model model2 = list.get(Integer.valueOf(editTextNum2.getText().toString()) - 1);
        double k1 = GetSlope(model.getX(), model.getY(), model2.getX(), model2.getY());
        Log.d("夹角k1", String.valueOf(Math.atan(k1) * 180.0 / Math.PI));

        int x1 = Integer.parseInt(editTextX.getText().toString());
        int y1 = Integer.parseInt(editTextY.getText().toString());
        int x2 = Integer.parseInt(editTextX2.getText().toString());
        int y2 = Integer.parseInt(editTextY2.getText().toString());
        double k2 = GetSlope(x1, y1, x2, y2);
        Log.d("夹角k2", String.valueOf(Math.atan(k2) * 180.0 / Math.PI));

        //计算平移
        int offX = x1 - model.getX();
        int offY = y1 - model.getY();
        //先把所有点平移过去。。。
        ArrayList<Model> translationModel = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Model oModel = list.get(i);

            Model newModel = new Model(i+1, oModel.getX() + offX, oModel.getY() + offY, oModel.getZ(),
                    oModel.getDirection(), oModel.isAvailable());
            translationModel.add(newModel);
        }

        //仅做了平移
        if((Math.atan(k2) * 180.0 / Math.PI) == (Math.atan(k1) * 180.0 / Math.PI)){
            bitmap = getBitmap(translationModel);
            imageView3.setImageBitmap(bitmap);
            return;
        }

        //两直线的夹角
        double angle = Math.atan((k2 - k1) / (1 + k1 * k2));
            Log.d("夹角弧度制？", String.valueOf(angle));
        double a = angle * 180.0 / Math.PI;
            Log.d("夹角2角度制？", String.valueOf(a));
        //Toast.makeText(this, "角度是" + String.valueOf(a), Toast.LENGTH_SHORT).show();



        //知道角度了，坐标旋转公式
        correctionModel = new ArrayList<>();


        Model startPoint = list.get(0);
        //计算原点平移了多少，用新的原点
        int newX = startPoint.getX()+offX;
        int newY = startPoint.getY()+offY;

        for (int i = 0; i < translationModel.size(); i++) {
            if(i>=totalPointNumber)break;

            Model oModel = translationModel.get(i);
            Log.d("oringal point", String.valueOf(oModel.getX()) + " == " + String.valueOf(oModel.getY()));

            double tempX2 = (double) ((oModel.getX() - newX) * Math.cos(angle)
                    - (oModel.getY() - newY) * Math.sin(angle) + newX);

            double tempY2 = (double) ((oModel.getX() - newX) * Math.sin(angle)
                    + (oModel.getY() - newY) * Math.cos(angle) + newY);

            Log.d("new point", String.valueOf(tempX2) + " == " + String.valueOf(tempY2));


            Model newModel = new Model(i+1, (int) tempX2, (int) tempY2, oModel.getZ(),
                    oModel.getDirection(), oModel.isAvailable());
            correctionModel.add(newModel);
        }

        bitmap = getBitmap(correctionModel);
        imageView3.setImageBitmap(bitmap);

    }
    //endregion


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_correction, menu);
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

        Model tempModel = list.get(Integer.valueOf(temp) - 1);
        if (etCurrent.getId() == R.id.editTextNum) {
            EditText editTextX = (EditText) findViewById(R.id.editTextX);
            editTextX.setText(String.valueOf(tempModel.getX()));
            EditText editTextY = (EditText) findViewById(R.id.editTextY);
            editTextY.setText(String.valueOf(tempModel.getY()));
            EditText editTextZ = (EditText) findViewById(R.id.editTextZ);
            editTextZ.setText(String.valueOf(tempModel.getZ()));
        } else if (etCurrent.getId() == R.id.editTextNum2) {
            EditText editTextX2 = (EditText) findViewById(R.id.editTextX2);
            editTextX2.setText(String.valueOf(tempModel.getX()));
            EditText editTextY2 = (EditText) findViewById(R.id.editTextY2);
            editTextY2.setText(String.valueOf(tempModel.getY()));
            EditText editTextZ2 = (EditText) findViewById(R.id.editTextZ2);
            editTextZ2.setText(String.valueOf(tempModel.getZ()));
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

    @Override
    public void DialogAdjustOnConfirm(DialogInterface dialogInterface, Bundle bundle) {
        //赋值回原来控件
        EditText editTextX = (EditText) currentLinearLayout.findViewById(R.id.editTextX);
        EditText editTextX2 = (EditText) currentLinearLayout.findViewById(R.id.editTextX2);
        if (editTextX != null) {
            editTextX.setText(bundle.getString("x"));

            EditText editTextY = (EditText) currentLinearLayout.findViewById(R.id.editTextY);
            editTextY.setText(bundle.getString("y"));

            EditText editTextZ = (EditText) currentLinearLayout.findViewById(R.id.editTextZ);
            editTextZ.setText(bundle.getString("z"));
        } else if (editTextX2 != null) {
            editTextX2.setText(bundle.getString("x"));

            EditText editTextY2 = (EditText) currentLinearLayout.findViewById(R.id.editTextY2);
            editTextY2.setText(bundle.getString("y"));

            EditText editTextZ2 = (EditText) currentLinearLayout.findViewById(R.id.editTextZ2);
            editTextZ2.setText(bundle.getString("z"));

        }

        //更改里面的数据源内容

        //adapter.notifyDataSetChanged();

        if (currentEditText != null) {
            currentEditText.setEnabled(true);
        }

        RemoveDialog(dialogAdjust);
    }

    @Override
    public void DialogAdjustOnCancle(DialogInterface dialogInterface, Bundle bundle) {
        if (currentEditText != null) {
            currentEditText.setEnabled(true);
        }

        RemoveDialog(dialogAdjust);
    }

    @Override
    public void DialogAdjustOnDismiss(DialogInterface dialogInterface, Bundle bundle) {
        if (currentEditText != null) {
            currentEditText.setEnabled(true);
        }

        RemoveDialog(dialogAdjust);
    }
}
