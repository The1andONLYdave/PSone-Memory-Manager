package com.dlka.psonememorymanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

public class dirFrame implements Parcelable{
	int type;
	int size;
	int next;
	byte territory[] = new byte[2];
	byte license[] = new byte[10];
	byte saveCode[] = new byte[8];
	byte codeEnd;
	byte padding[] = new byte[96];
	byte xor;
	long parent;
	
	String sLicense;
	String sTerr;
	String sIdent;
	
	public String getProd(){
		return sLicense;
	}
	public String getID(){
		return sIdent.replace("\0", " ").trim();
	}
	public String getRegion(){
		return sTerr;
	}
	
	public void setProd(String s){
		sLicense = s;
		license = new byte[10];
		license = Statics.copyArray(s.getBytes(), 10);
	}
	
	public void setID(String s){
		sIdent = s;
		saveCode = new byte[8];
		saveCode = Statics.copyArray(s.getBytes(), 8);
	}
	
	public void setRegion(int i){
		switch(i){
		case 0:
			territory[1] = 'A';
			break;
		case 1:
			territory[1] = 'I';
			break;
		case 2:
			territory[1] = 'E';
			break;
		default:
			break;
				
		}
		sTerr = new String(territory);
	}

	
	private DataInputStream dInputStream;	
	
	// PARCELABLE CONSTRUCTOR
	public dirFrame(Parcel in){
		type = in.readInt();
		size = in.readInt();
		next = in.readInt();
		in.readByteArray(territory);
		in.readByteArray(license);
		in.readByteArray(saveCode);
		codeEnd = in.readByte();
		in.readByteArray(padding);
		xor = in.readByte();
		parent = in.readLong();
		sLicense = in.readString();
		sTerr = in.readString();
		sIdent = in.readString();
	}
	
	public void updateXor(){
		xor = calcXor();
	}
	
	public byte calcXor(){
		byte xor = 0;
		//byte type = 0;
		/*switch(this.type){
		case 161:
			type = 81;
			break;
		case 162:
			type = 82;
			break;
		case 163:
			type = 83;
			break;
		}*/
		/*xor^=type>>24;
		xor^=(type>>16)&0xff;
		xor^=(type>>8)&0xff;
		xor^=type&0xff;*/
		xor^=type;
		
		xor^=size>>24;
		xor^=(size>>16)&0xff;
		xor^=(size>>8)&0xff;
		xor^=size&0xff;
		
		//xor^=next>>24;
		//xor^=(next>>16)&0xff;
		xor^=(next>>8)&0xff;
		xor^=next&0xff;
		
		xor^=territory[0];
		xor^=territory[1];
		xor^=license[0];
		xor^=license[1];
		xor^=license[2];
		xor^=license[3];
		xor^=license[4];
		xor^=license[5];
		xor^=license[6];
		xor^=license[7];
		xor^=license[8];
		xor^=license[9];
		xor^=saveCode[0];
		xor^=saveCode[1];
		xor^=saveCode[2];
		xor^=saveCode[3];
		xor^=saveCode[4];
		xor^=saveCode[5];
		xor^=saveCode[6];
		xor^=saveCode[7];
		xor^=codeEnd;
		for(int i = 0;i<96;i++){
			xor^=padding[i];
		}
		int temp = xor;
		/*xor^=this.xor;
		if(xor==-1)
			return true;
		else
			return false;*/
		return (byte)temp;
	}
	
	public byte getXor(){
		return xor;
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(int i){
		type = i;
	}
	
	public int getSize(){
		return size;
	}
	
	public int getNext(){
		return next>>8;
	}
	
	public void setNext(short i){
		next = i<<8;
	}
	
	
	
	// DEFAULT CTOR
	public dirFrame(DataInputStream dis){
		dInputStream = dis;
	}
	

	
	@Override
	public int describeContents(){
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeInt(type);
		dest.writeInt(size);
		dest.writeInt(next);
		dest.writeByteArray(territory);
		dest.writeByteArray(license);
		dest.writeByteArray(saveCode);
		dest.writeByte(codeEnd);
		dest.writeByteArray(padding);
		dest.writeByte(xor);
		dest.writeLong(parent);
		
		dest.writeString(sLicense);
		dest.writeString(sTerr);
		dest.writeString(sIdent);
	}
	
	public static final Parcelable.Creator<dirFrame>CREATOR = new
	Parcelable.Creator<dirFrame>(){
		@Override
		public dirFrame createFromParcel(Parcel source){				
			return new dirFrame(source);
		}
		@Override
		public dirFrame[] newArray(int size){
			return new dirFrame[size];
		}
	};
	
	public void write(DataOutputStream outStream){
		try {
			outStream.writeInt(ByteSwapper.swap(type));
			outStream.writeInt(ByteSwapper.swap(size));
			outStream.writeShort((short)(next));
			outStream.write(territory);
			outStream.write(license);
			outStream.write(saveCode);
			outStream.writeByte(codeEnd);
			outStream.write(padding);
			outStream.writeByte(xor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void read()
	{
		try{
			type = ByteSwapper.swap(dInputStream.readInt());
			size = ByteSwapper.swap(dInputStream.readInt());
			//next = ByteSwapper.swap(dInputStream.readUnsignedShort());
			next = dInputStream.readUnsignedShort();
			dInputStream.read(territory, 0, 2);
			dInputStream.read(license, 0, 10);
			dInputStream.read(saveCode, 0, 8);
			codeEnd = (byte) dInputStream.readUnsignedByte();
			dInputStream.read(padding, 0, 96);
			xor = (byte) dInputStream.readUnsignedByte();
		}
		catch(IOException e){
			
		}
		sLicense = new String(license);
		sTerr = new String(territory);
		sIdent = new String(saveCode);
	}
}	