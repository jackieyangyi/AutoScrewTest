package com.autodoor.autoscrew.ActSetScrew;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.autodoor.autoscrew.ActivitySetScrew;
import com.autodoor.autoscrew.R;

import java.util.List;

/**
 * Created by yangyi on 2015/11/4.
 */
public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final ActivitySetScrew context;

    private Drawable drawable;

    public InteractiveArrayAdapter(ActivitySetScrew context, List<Model> list) {
        super(context, R.layout.rowbuttonlayout, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected EditText text;
        protected EditText editTextX;
        protected EditText editTextY;
        protected EditText editTextZ;
        protected EditText editTextDirection;
        protected LinearLayout linearLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        // reuse views
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.rowbuttonlayout, null);



            // configure view holder
            final ViewHolder viewHolder = new ViewHolder();

            //viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);
            drawable = view.getBackground();

            viewHolder.editTextX = (EditText) view.findViewById(R.id.editTextX);
            viewHolder.editTextY = (EditText) view.findViewById(R.id.editTextY);
            viewHolder.editTextZ = (EditText) view.findViewById(R.id.editTextZ);
            viewHolder.text = (EditText) view.findViewById(R.id.label);
            viewHolder.editTextDirection = (EditText) view.findViewById(R.id.editTextDirection);

            view.setTag(viewHolder);

            viewHolder.editTextX.setTag(list.get(position));
            viewHolder.editTextY.setTag(list.get(position));
            viewHolder.editTextZ.setTag(list.get(position));
            viewHolder.text.setTag(list.get(position));
            viewHolder.editTextDirection.setTag(list.get(position));

            //viewHolder.linearLayout.setTag(list.get(position));


        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).editTextX.setTag(list.get(position));
            ((ViewHolder) view.getTag()).editTextY.setTag(list.get(position));
            ((ViewHolder) view.getTag()).editTextZ.setTag(list.get(position));
            ((ViewHolder) view.getTag()).text.setTag(list.get(position));
            ((ViewHolder) view.getTag()).editTextDirection.setTag(list.get(position));
            //((ViewHolder) view.getTag()).linearLayout.setTag(list.get(position));
        }

        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.editTextX.setInputType(InputType.TYPE_NULL);// 屏蔽输入法
        holder.editTextY.setInputType(InputType.TYPE_NULL);// 屏蔽输入法
        holder.editTextZ.setInputType(InputType.TYPE_NULL);// 屏蔽输入法
        holder.text.setInputType(InputType.TYPE_NULL);// 屏蔽输入法
        holder.editTextDirection.setInputType(InputType.TYPE_NULL);// 屏蔽输入法


        //赋值
        holder.editTextX.setText(String.valueOf(list.get(position).getX()));
        holder.editTextY.setText(String.valueOf(list.get(position).getY()));
        holder.editTextZ.setText(String.valueOf(list.get(position).getZ()));
        holder.text.setText(String.valueOf(list.get(position).getId()));
        holder.editTextDirection.setText(list.get(position).getDirection());

        if(list.get(position).isSelected()){
            view.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }else{
            view.setBackground(drawable);
        }

        //Log.d("postion value is", String.valueOf(position));


        //holder.text.setOnTouchListener(getL2());
        holder.text.setOnClickListener(getListennerFocus());
        holder.editTextX.setOnClickListener(getL());
        holder.editTextY.setOnClickListener(getL());
        holder.editTextZ.setOnClickListener(getL());


        //Log.d("holder.text:",""+list.get(position).getId() + " =?= " +ActivitySetScrew.pointNumber);
        if (list.get(position).getId() <= ActivitySetScrew.pointNumber) {
            holder.text.setEnabled(true);
            list.get(position).setAvailable(true);
        } else {
            holder.text.setEnabled(false);
            list.get(position).setAvailable(false);
        }


        holder.editTextDirection
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Model element = (Model) holder.editTextDirection.getTag();
                        EditText temp = (EditText) v;
                        if (temp.getText().toString().equals("左")) {
                            temp.setText("右");
                            element.setDirection("右");
                            //Log.d("仓位调试：", temp.getText().toString());
                        } else {
                            temp.setText("左");
                            element.setDirection("左");
                            //Log.d("仓位调试：", temp.getText().toString());
                        }
                    }
                });


        return view;
    }

    @NonNull
    private View.OnTouchListener getL2() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                context.EditTextClick(v);
                return false;
            }
        };
    }

    @NonNull
    private View.OnClickListener getL() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.EditTextClick(v);


            }
        };
    }

    @NonNull
    private View.OnClickListener getListennerFocus() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.EditTextClickFocus(v);


            }
        };
    }


}
