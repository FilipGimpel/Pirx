package com.gimpel.pirx;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PirxFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_pirx, container);
	}

	
	@Override
	public void onStart() {
		super.onStart();

		initializeGridSize();
		initializeGridArray();
		initializeGridClickListeners();
		fillRandom(FILLSIZE);
	}

	/* ---------- Fragments logic ---------- */

	private void initializeGridSize() {
		LinearLayout parentView = (LinearLayout) ((FrameLayout)getView()).getChildAt(0);
		
		Context context = getActivity().getBaseContext();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		int width = display.getWidth();
		int height = display.getHeight();
		int size = width < height ? width : height;
		
		parentView.setLayoutParams(new FrameLayout.LayoutParams(size, size));
		parentView.invalidate();
	}

	private GridElement[][] mGridViews = new GridElement[GRIDSIZE][GRIDSIZE];
	private static final int GRIDSIZE = 7;
	private static final int FILLSIZE = 3;
	private GridElement mActiveView = null;
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
			
			if (mActiveView == null) { // if no grid is currently selected
				if (element.isOccupied()) { // if we select nonempty grid
					mActiveView = element;
					mActiveView.bringToFront();
					Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.pulse);
					mActiveView.startAnimation(hyperspaceJumpAnimation);
				}
			} else { // if a grid is currently selected
				if (!element.isOccupied()) { // if we select nonempty grid
					// 
					element.setColor(mActiveView.getColor());
					
					// deactivate previous active element
					mActiveView.setColor(Color.WHITE);
					mActiveView.clearAnimation();
					mActiveView = null;
					
					// add new elements
					fillRandom(FILLSIZE);
					
					invalidateGrid();
				} else if (element.equals(mActiveView)) { // if we click again on active element
					// deactivate active element
					mActiveView.clearAnimation();
					mActiveView = null;
				}
				
			}
		}
	};

	/**
	 * Split into separate methods
	 */
	public void invalidateGrid() {
		
		// horizontal
		for (int i = 0; i < GRIDSIZE; i++) {
			for (int j = 0; j < GRIDSIZE - 3; j++) {
				if (mGridViews[i][j].getColor() == mGridViews[i][j+1].getColor() && 
				mGridViews[i][j+1].getColor() == mGridViews[i][j+2].getColor() &&
				mGridViews[i][j+2].getColor() == mGridViews[i][j+3].getColor() && 
				mGridViews[i][j+2].getColor() != 0) {
					
				removeAndAnimateGrids(
						mGridViews[i][j],
						mGridViews[i][j+1],
						mGridViews[i][j+2],
						mGridViews[i][j+3]
					);
				}
			}
		}
		
		// vertical
		for (int i = 0; i < GRIDSIZE - 3; i++) {
			for (int j = 0; j < GRIDSIZE; j++) {
				if (mGridViews[i][j].getColor() == mGridViews[i+1][j].getColor() && 
				mGridViews[i+1][j].getColor() == mGridViews[i+2][j].getColor() &&
				mGridViews[i+2][j].getColor() == mGridViews[i+3][j].getColor() && 
				mGridViews[i+3][j].getColor() != 0) {
					
				removeAndAnimateGrids(
						mGridViews[i][j],
						mGridViews[i+1][j],
						mGridViews[i+2][j],
						mGridViews[i+3][j]
					);
				}
			}
		}
		
		// oblique descending right
		for (int i = 0; i < GRIDSIZE - 3; i++) {
			for (int j = 0; j < GRIDSIZE - 3; j++) {
				if (mGridViews[i][j].getColor() == mGridViews[i+1][j+1].getColor() && 
				mGridViews[i+1][j+1].getColor() == mGridViews[i+2][j+2].getColor() &&
				mGridViews[i+2][j+2].getColor() == mGridViews[i+3][j+3].getColor() && 
				mGridViews[i+3][j+3].getColor() != 0) {

				removeAndAnimateGrids(
						mGridViews[i][j],
						mGridViews[i+1][j+1],
						mGridViews[i+2][j+2],
						mGridViews[i+3][j+3]
					);
				}		
			}
		}
		
		// oblique ascending right
		for (int i = 0; i < GRIDSIZE - 3; i++) {
			for (int j = 0; j < GRIDSIZE - 3; j++) {
				if (mGridViews[i+3][j].getColor() == mGridViews[i+2][j+1].getColor() && 
				mGridViews[i+2][j+1].getColor() == mGridViews[i+1][j+2].getColor() &&
				mGridViews[i+1][j+2].getColor() == mGridViews[i][j+3].getColor() && 
				mGridViews[i][j+3].getColor() != 0) {
					
				removeAndAnimateGrids(
						mGridViews[i+3][j], 
						mGridViews[i+2][j+1],
						mGridViews[i+1][j+2],
						mGridViews[i][j+3]
					);
				}		
			}
		}
	}

	/**
	 * @param grids x1, y1, x2, y2... itd coordinates of points that we want to remove
	 * logically, there must be a odd number of arguments
	 * 
	 * TODO DO IT USING PROPERTY ANIMATOR
	 */
	private void removeAndAnimateGrids(GridElement... grids) {
			
			// fading
			AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
			fadeInAnimation.setInterpolator(new DecelerateInterpolator()); //add this
			
			// scaling
			ScaleAnimation scaleAnimation = new ScaleAnimation(
					1, 2, 1, 2, 
					Animation.RELATIVE_TO_SELF, (float)0.5,
					Animation.RELATIVE_TO_SELF, (float)0.5
				);
						
			// Put the animations together
			AnimationSet set = new AnimationSet(false);
			set.setDuration(2000);
			set.addAnimation(fadeInAnimation);
			set.addAnimation(scaleAnimation);
			set.setAnimationListener(new AnimationListener() {
				
				GridElement[] mColorReverseCallbacks;
				
				/**
				 * Method used to pass arguments to anonymous class 
				 * in an elegant way
				 */
				public AnimationListener init(GridElement... elements) {
					mColorReverseCallbacks = elements;
					return this;
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					for (GridElement element : mColorReverseCallbacks) {
						element.setColor(Color.WHITE);
					}
				}


				@Override
				public void onAnimationRepeat(Animation animation) {
					// nothing to do					
				}
				
				@Override
				public void onAnimationStart(Animation animation) {
					// nothing to do
				}
				
			}.init(grids));
			
			for (GridElement gridElement : grids) {
				gridElement.setAnimation(set);
			}
	}
	
	/**
	 * Fills grid with random colors
	 */
	public void fillRandom(int count) {
		ArrayList<GridElement> emptyGrids = getRandomEmptyGrids(count);
		
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setDuration(1000);
		
		for (GridElement element : emptyGrids) {
			element.setColor(Colors.Colors.get(getRandomInt(0, Colors.Colors.size())));
			element.startAnimation(fadeIn);
		}
		
		Log.d("LAKUKARACZA","EHEHE");
	}

	/**
	 * @param count number of random empty grids to return
	 */
	public ArrayList<GridElement> getRandomEmptyGrids(int count) {
		ArrayList<GridElement> emptyGrids = getEmptyGrids();
		ArrayList<GridElement> randomEmptyGrids = new ArrayList<GridElement>();
		
		for (int i = 0; i < count; i++) {
			if (emptyGrids.size() != 0) { // if we still have any empty girds
				// lets fill them with elements
				int rand = getRandomInt(0, emptyGrids.size());
				randomEmptyGrids.add(emptyGrids.remove(rand));	
			} else {
				// otherwise the game is finished!
				Context context = getActivity().getBaseContext();
				CharSequence text = "YOU LOSE BITCH !";
				int duration = Toast.LENGTH_LONG;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();				
			}
				
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
				if (!mGridViews[i][j].isOccupied()) emptyGrids.add(mGridViews[i][j]);
			}
		}
		
		return emptyGrids;
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return returns random int from range [min, max)
	 */
	private int getRandomInt(int min, int max) {
		return mRandom.nextInt(max - min) + min;
	}
	
	/**
	 * Fills grid with random colors. Instead of searching for 
	 * grid elements that are empty and then selecting number
	 * of random ones from them, this method randomly selects
	 * grids and then checks if they are empty. If yes, it
	 * colours them, if not, looks for another. 
	 * 
	 * This approach results in more readable code.
	 * 
	 * (later: not really, when we have last empty grid 
	 * there will be chance 1/Gridsize**2 that random grid
	 * will be the one we are looking for, dumb method)
	 */
	public void fillRandom_2(int count) {
		int x;
		int y;
		
		while(count > 0 && getEmptyGrids().size() > 0) {
			x = getRandomInt(0, GRIDSIZE);
			y = getRandomInt(0, GRIDSIZE);
			
			if (!mGridViews[x][y].isOccupied()) {
				mGridViews[x][y].setColor(Colors.Colors.get(getRandomInt(0, Colors.Colors.size())));
				count--;
			}
			
		}
	}
}
