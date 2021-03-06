package edu.uma.umasimple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class BtnCustomFont extends Button {

	private static final String TAG = "Button";
	
	public BtnCustomFont(Context context) {
		super(context);
	}
	 public BtnCustomFont(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        setCustomFont(context, attrs);
	    }

    public BtnCustomFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.BtnCustomFont);
        String customFont = a.getString(R.styleable.BtnCustomFont_BtnCustomFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
        tf = Typeface.createFromAsset(ctx.getAssets(), asset);  
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(tf);  
        return true;
    }

}
