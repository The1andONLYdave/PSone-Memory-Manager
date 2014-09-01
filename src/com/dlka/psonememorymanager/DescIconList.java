package com.dlka.psonememorymanager;

import android.graphics.Bitmap;
import android.widget.TextView;

public class DescIconList implements Comparable<DescIconList>{
	
	  private String mText = "";
	  private String mProd = "";
	  private String mRegion = "";
	  private String mID = "";
      private Bitmap mIcon;
      private boolean deleted = false;
      private boolean linked = false;
      private boolean empty = false;
      private boolean mSelectable = true;
      TextView textview;
      
      public DescIconList(String text, String reg, String prod, String id, Bitmap icon, boolean deleted)
      {
    	  mIcon = icon;
    	  mText = text;
    	  mID = id;
    	  mProd = prod;
    	  mRegion = reg;
    	  this.deleted = deleted;
      }
      
      public DescIconList(TextView view, Bitmap icon){
    	  mIcon = icon;
    	  textview = view;
      }
      
      public DescIconList()
      {
    	  
      }      
      
      public boolean isDeleted(){
    	  return deleted;
      }
	public boolean isSelectable() {
        return mSelectable;
	}
	
	public void setSelectable(boolean selectable) {
	        mSelectable = selectable;
	}
	
	public void setTV(TextView view){
		textview = view;
	}
	
	public TextView getTV(){
		return textview;
	}
	
	public String getText() {
	        return mText;
	}
	
	public void setText(String text) {
	        mText = text;
	}
	
	public void setIcon(Bitmap icon) {
	        mIcon = icon;
	}
	
	public Bitmap getIcon() {
	        return mIcon;
	}
	
	
	/** Make IconifiedText comparable by its name */
	@Override
	public int compareTo(DescIconList other) {
	        if(this.mText != null)
	                return this.mText.compareTo(other.getText());
	        else
	                throw new IllegalArgumentException();
	}

	public void setLinked(boolean linked) {
		this.linked = linked;
	}

	public boolean isLinked() {
		return linked;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setmID(String mID) {
		this.mID = mID;
	}

	public String getmID() {
		return mID;
	}

	public void setmRegion(String mRegion) {
		this.mRegion = mRegion;
	}

	public String getmRegion() {
		return mRegion;
	}

	public void setmProd(String mProd) {
		this.mProd = mProd;
	}

	public String getmProd() {
		return mProd;
	}
}
