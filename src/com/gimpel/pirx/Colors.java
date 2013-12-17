package com.gimpel.pirx;

import java.util.Arrays;
import java.util.List;

/**
 * Shouldnt the whole class be static?
 * @author Filip Gimpel
 *
 */
public class Colors {
	static final int red = 0xffff0000;
	static final int green = 0xff00ff00;
	static final int blue = 0xff0000ff;
	static final int yellow = 0xffffff00;
	static final int violet = 0xffff00ff;
	static final int gray = 0xffbcbdc2;
	
	static List<Integer> Colors = 
			Arrays.asList(red, green, blue, yellow, violet);
}
