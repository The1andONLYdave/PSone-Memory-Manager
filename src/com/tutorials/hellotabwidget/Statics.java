package com.tutorials.hellotabwidget;

import com.neko68k.psxmc.R;


public class Statics {
	
	static MemoryCard[] cards = new MemoryCard[]{null,null};
	static boolean changed = false;
	static boolean backup = true;
	static boolean export = true;
	static int exportFmt = 0;
	static String defaultPath = null;
	
	static byte[] copyArray(byte[] a, int size){
		byte[] b = new byte[size];
		for(int i = 0;i<a.length;i++){
			b[i] = a[i];
		}
		return b;
	}
}
