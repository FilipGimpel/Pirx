package com.gimpel.pirx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class PirxFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_pirx, container);
	}

	@Override
	public void onStart() {
		super.onStart();

		initializeGridArray();
		initializeGridClickListeners();
		fillRandom(5);
	}

	/* ---------- Fragments logic ---------- */

	private GridElement[][] mGridViews = new GridElement[GRIDSIZE][GRIDSIZE];
	private static final int GRIDSIZE = 7;
	private View mActiveView = null;
	private Random mRandom = new Random();

	/**
	 * Assigns views in correct order into two-dimensional array
	 */
	private void initializeGridArray() {
		LinearLayout parentView = (LinearLayout) ((FrameLayout)getView()).getChildAt(0);

		for (int i = 0; i < parentView.getChildCount(); i++) {
			LinearLayout rowView = (LinearLayout) parentView.getChildAt(i);
			for (int j = 0; j < rowView.getChildCount(); j++) {
				mGridViews[i][j] = (GridElement) rowView.getChildAt(j);
			}
		}
	}

	/**
	 * Assigns clickListeners to each view
	 */
	private void initializeGridClickListeners() {
		for (int i = 0; i < GRIDSIZE; i++) {
			for (int j = 0; j < GRIDSIZE; j++) {
				mGridViews[i][j].setOnClickListener(GridElementClickListener);
			}
		}
	}

	/**
	 * Launched every time a grid element is beign clicked
	 */
	View.OnClickListener GridElementClickListener = new View.OnClickListener() {

		public void onClick(View v) {
			GridElement element = (GridElement)v;
			v.setBackgroundColor(Color.BLUE);
			Log.d("MAMMA MIA", element.getInfo());	
		};
	};

	public void onPlayerAction() {

	}

	public void onGameAction() {

	}

	/**
	 * Fills grid with random colors
	 */
	public void fillRandom(int count) {
		ArrayList<GridElement> emptyGrids = getRandomEmptyGrids(count);
		
		for (GridElement element : emptyGrids) {
			element.setOccupied(true);
			element.setBackgroundColor(Color.GREEN);
			
		}
	}

	/**
	 * @param count number of random empty grids to return
	 */
	public ArrayList<GridElement> getRandomEmptyGrids(int count) {
		ArrayList<GridElement> emptyGrids = getEmptyGrids();
		ArrayList<GridElement> randomEmptyGrids = new ArrayList<GridElement>();
		
		for (int i = 0; i < count; i++) {
			int rand = getRandomInt(0, randomEmptyGrids.size());
			randomEmptyGrids.add(emptyGrids.remove(rand));
		}
		
		return randomEmptyGrids;
	}

	/**
	 * @return Grids that are not being currently active
	 */
	private ArrayList<GridElement> getEmptyGrids() {
		ArrayList<GridElement> emptyGrids = new ArrayList<GridElement>();
		
		for (int i = 0; i < GRIDSIZE; i++) {
			for (int j = 0; j < GRIDSIZE; j++) {
				emptyGrids.add(mGridViews[i][j]);
			}
		}
		
		return emptyGrids;
	}
	
	private int getRandomInt(int min, int max) {
		return mRandom.nextInt(max - min + 1) + min;
	}
	
	/**
	 * Fills grid with random colors. Instead of searching for 
	 * grid elements that are empty and then selecting number
	 * of random ones from them, this method randomly selects
	 * grids and then checks if they are empty. If yes, it
	 * colours them, if not, looks for another. 
	 * 
	 * This approach results in more readable code.
	 */
	public void fillRandom_2(int count) {
		int x;
		int y;
		
		while(count > 0) {
			x = getRandomInt(0, GRIDSIZE);
			y = getRandomInt(0, GRIDSIZE);
			
			if (!mGridViews[x][y].isOccupied()) {
				mGridViews[x][y].setColor(Colors.blue);
			}
			
		}
	}
}
