package com.topherlee.psonememorymanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class saveBlock implements Parcelable{
	short magic;
	byte iconDisplay;
	byte numBlocks;
	byte sJisTitle[] = new byte[64];
	byte padding[] = new byte[28];
	short iconPal[] = new short[16]; // 16-bit BGR
	
	byte icon1[] = new byte[128];	// 4-bit, 3 icons @ 16x16
	byte icon2[] = new byte[128];
	byte icon3[] = new byte[128];
	
	byte saveData[] = new byte[128*60];
	
	
	
	String sTitle;
	ConvertIconFormat cif = new ConvertIconFormat();
		
	private static DataInputStream dInputStream;
	
	Bitmap linkIcon = null;
	
	public saveBlock(DataInputStream dis){
		dInputStream = dis;
	}
	
	public void save(DataOutputStream outStream, boolean header){
		try {
			if(header){
				outStream.writeShort(ByteSwapper.swap(magic));
				outStream.writeByte(iconDisplay);
				outStream.writeByte(numBlocks);
				outStream.write(sJisTitle);
				outStream.write(padding);
				for(int i = 0;i<16;i++){
					outStream.writeShort(ByteSwapper.swap(iconPal[i]));
				}
				outStream.write(icon1);
				outStream.write(icon2);
				outStream.write(icon3);
			}
			outStream.write(saveData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setTitle(String title){
		sTitle = title;
	}
	
	public String getTitle(){
		return sTitle;
	}
	
	public void setIcon(Bitmap bitmap){
		linkIcon = bitmap;
	}
	
	public saveBlock(Parcel in){
		magic = (short)in.readInt();
		iconDisplay = in.readByte();
		numBlocks = in.readByte();
		in.readByteArray(sJisTitle);
		in.readByteArray(padding);
		int pal[] = new int[16];
		in.readIntArray(pal);
		iconPal = convPal(pal);
		in.readByteArray(icon1);
		in.readByteArray(icon2);
		in.readByteArray(icon3);
		in.readByteArray(saveData);
		sTitle = in.readString();
	}
	
	private int[] convPal(){
		return new int[]{iconPal[0], iconPal[1], iconPal[2], iconPal[3],
			iconPal[4], iconPal[5], iconPal[6], iconPal[7], iconPal[8], iconPal[9],
			iconPal[10], iconPal[11], iconPal[12], iconPal[13], iconPal[14], iconPal[15]};
		}
		
	private short[] convPal(int[] pal){
		return new short[]{(short)pal[0], (short)pal[1], (short)pal[2], (short)pal[3],
			(short)pal[4], (short)pal[5], (short)pal[6], (short)pal[7], (short)pal[8], (short)pal[9],
			(short)pal[10], (short)pal[11], (short)pal[12], (short)pal[13], (short)pal[14], (short)pal[15]};
		}
	
	@Override
	public int describeContents(){
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeInt((int)magic);
		dest.writeByte(iconDisplay);
		dest.writeByte(numBlocks);
		dest.writeByteArray(sJisTitle);
		dest.writeByteArray(padding);
		dest.writeIntArray(convPal());
		dest.writeByteArray(icon1);
		dest.writeByteArray(icon2);
		dest.writeByteArray(icon3);		
		dest.writeByteArray(saveData);		
		dest.writeString(sTitle);
		
	}
	
	public static final Parcelable.Creator<saveBlock>CREATOR = new
	Parcelable.Creator<saveBlock>(){
		@Override
		public saveBlock createFromParcel(Parcel source){				
			return new saveBlock(source);
		}
		@Override
		public saveBlock[] newArray(int size){
			return new saveBlock[size];
		}
	};
	
	public Bitmap getIcon(){
		
			Bitmap bitmap = cif.convert(icon1, iconPal);
			return(bitmap);
		
	}
	
	public String getDesc(){
		return sTitle;
	}
	
	public byte[] getSJISTitle(){
		return sJisTitle;
	}
	
	public void read()
	{
		try{
			magic = ByteSwapper.swap((short) dInputStream.readUnsignedShort());
			iconDisplay = (byte) dInputStream.readUnsignedByte();
			numBlocks = (byte) dInputStream.readUnsignedByte();
			dInputStream.read(sJisTitle, 0, 64);
			dInputStream.read(padding, 0, 28);
			for(int i = 0; i<16;i++){
				iconPal[i] = ByteSwapper.swap((short) dInputStream.readUnsignedShort());
			}
			dInputStream.read(icon1, 0, 128);
			dInputStream.read(icon2, 0, 128);
			dInputStream.read(icon3, 0, 128);
			dInputStream.read(saveData, 0, 128*60);
		}
		catch(IOException e){
			
		}
		try{
		String title = new String(sJisTitle, "Shift_JIS");
		int ch = title.indexOf("\u0000");
		if(ch!=-1){
			sTitle = title.substring(0, ch+1);
		}
		else
			sTitle = title;
		
		
		}
		catch(UnsupportedEncodingException e){
			
		}
	}
}