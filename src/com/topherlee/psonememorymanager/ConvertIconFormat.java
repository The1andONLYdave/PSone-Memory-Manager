package com.topherlee.psonememorymanager;

import android.graphics.Bitmap;
import com.topherlee.psonememorymanager.R;


// this converts the icon from psx format to 
// android 8888 bitmap
public class ConvertIconFormat {
	static Bitmap outIcon;
	Bitmap convert(byte pixels[], short pal[]){
		int tempPixels[] = new int[256];		
		
		int j =0;
		for(int i = 0; i<128; i++){
			tempPixels[j] =  _555to8888(pal[LONIBBLE(pixels[i])]);
			tempPixels[j+1] =  _555to8888(pal[HINIBBLE(pixels[i])]);
			j+=2;			
		}
		outIcon = Bitmap.createBitmap(tempPixels, 16, 16, Bitmap.Config.ARGB_8888);
		Bitmap scaleIcon = Bitmap.createScaledBitmap(outIcon, 64, 64, false);
		return scaleIcon;
	}		
	
	static private int _555to8888(int in){
		int r;
		int g;
		int b;
		int m;
		int out;
		
		r = in&0x1f;
		g = (in&0x3e0)>>5;
		b = (in&0x7c00)>>10;
		m = (in&0x8000)>>15;
		
		r<<=3;
		g<<=3;
		b<<=3;
		
		out = 0xFF000000 | (r<<16) | (g<<8) | b;
		return out;
		//return ( 0xFF000000 | (in>>8) | ((in&0x3E0)<<5) | ((in&0x1F)<<18) );
	}
		
	static private int HINIBBLE(int in){
			return ((in >>4)&0x0F);
		}
		
	static private int LONIBBLE(int in){			
		return ((in&0x0F));
	}
		
	
}
