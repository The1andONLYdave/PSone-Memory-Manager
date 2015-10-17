package com.dlka.psonememorymanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DescIconView extends LinearLayout {
    private TextView mText = null;
    private ImageView mIcon = null;
    private TextView prodCode = null;
    private TextView saveID = null;
    private TextView region = null;
    private LinearLayout extras = null;
    private LinearLayout extras2 = null;
    private ColorStateList defaultColors;

    public DescIconView(Context context, DescIconList aIconifiedText) {
        super(context);
                        

            /* First Icon and the Text to the right (horizontal),
             * not above and below (vertical) */
        this.setOrientation(HORIZONTAL);

        mIcon = new ImageView(context);
        mIcon.setImageBitmap(aIconifiedText.getIcon());
        // left, top, right, bottom
        //mIcon.setPadding(0, 2, 5, 0); // 5px to the right
           
            /* At first, add the Icon to ourself
             * (! we are extending LinearLayout) */
        addView(mIcon, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        mText = new TextView(context);
        defaultColors = mText.getTextColors();
        mText.setText(aIconifiedText.getText());
        mText.setGravity(Gravity.LEFT);


        //
        if (aIconifiedText.isDeleted()) {
            mText.setTextColor(0xFFFF0000);
        } else if (aIconifiedText.isLinked()) {
            mText.setTextColor(0xFF00FF00);
        } else if (aIconifiedText.isEmpty()) {
            mText.setTextColor(0xFF0000FF);
        } else {
            mText.setTextColor(defaultColors);
        }
            
            /* Now the text (after the icon) */
        addView(mText, new LinearLayout.LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 3));

        extras = new LinearLayout(context);
        extras.setOrientation(VERTICAL);
        extras2 = new LinearLayout(context);
        extras2.setOrientation(HORIZONTAL);
        prodCode = new TextView(context);
        saveID = new TextView(context);
        region = new TextView(context);


        prodCode.setText(aIconifiedText.getmRegion() + aIconifiedText.getmProd());
        prodCode.setGravity(Gravity.RIGHT);
        saveID.setText(aIconifiedText.getmID());
        saveID.setGravity(Gravity.RIGHT);
        // region.setText(aIconifiedText.getmRegion());
        //region.setGravity(Gravity.LEFT);


        extras2.addView(region, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        extras2.addView(prodCode, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));


        extras.setGravity(Gravity.RIGHT);
        extras.addView(extras2, new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        extras.addView(saveID, new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        addView(extras, new LinearLayout.LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 1));

        class DetailView extends LinearLayout {
            public DetailView(Context context, DescIconList aIconifiedText) {
                super(context);
            }
        }
    }

    public void setProd(String prod) {
        prodCode.setText(prod);
    }

    public void setID(String ID) {
        saveID.setText(ID);
    }

    public void setRegion(String region) {
        this.region.setText(region);
    }

    public void setColor(int color) {
        mText.setTextColor(color);
    }

    public void setColor(ColorStateList color) {
        mText.setTextColor(color);
    }

    public void setText(String words) {
        mText.setText(words);
    }

    public void setIcon(Bitmap bullet) {
        mIcon.setImageBitmap(bullet);
    }
}
