package com.example.mexpense.fragments.main.balance;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mexpense.R;

public class ViewDialog {

    public void showDialog(Activity activity, String imageUrl){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_bill_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView imgView = (ImageView) dialog.findViewById(R.id.img_bill);
        Glide.with(activity)
                .load(imageUrl)
                .centerCrop()
                .into(imgView);

        dialog.show();
    }
}
