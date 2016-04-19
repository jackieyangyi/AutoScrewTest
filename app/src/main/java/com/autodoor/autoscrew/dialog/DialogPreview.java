package com.autodoor.autoscrew.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.autodoor.autoscrew.R;

import java.util.ArrayList;

/**
 * Created by yangyi on 2015/10/25.
 */
public class DialogPreview extends DialogFragment {

    Bundle args;
    View view;
    EditText editTextX;
    EditText editTextY;
    EditText editTextZ;
    String dialogType;


    ImageView imageView2;
    ArrayList<Double> yList;

    public  Bitmap bitmap;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_preview, null);
        builder.setView(view);


       imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        //为ImageView设置图像
        imageView2.setImageBitmap(bitmap);



        //DrawChart();
        return builder.create();
    }




    // This is DialogFragment, not Dialog
    @Override
    public void onDismiss(DialogInterface dialog) {
        ((NoticeDialogListener) getActivity()).DialogPreviewOnDismiss(null, null);
    }


    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void DialogPreviewOnConfirm(DialogInterface dialogInterface, Bundle bundle);

        public void DialogPreviewOnCancle(DialogInterface dialogInterface, Bundle bundle);

        public void DialogPreviewOnDismiss(DialogInterface dialogInterface, Bundle bundle);
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
