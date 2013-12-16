package com.gimpel.pirx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class GridElement extends View {
	private int mPositionX;
	private int mPositionY;
	private int mColor;
	private boolean isOccupied;
	
	public int getPositionX() {
		return mPositionX;
	}

	public void setPositionX(int mPositionX) {
		this.mPositionX = mPositionX;
	}

	public int getPositionY() {
		return mPositionY;
	}

	public void setPositionY(int mPositionY) {
		this.mPositionY = mPositionY;
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int mColor) {
		this.mColor = mColor;
		setBackgroundColor(mColor);
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}



	public GridElement(Context context) {
		super(context);
	}

	public GridElement(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GridElement(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public String getInfo() {
		return (String) getTag();
	}
}
