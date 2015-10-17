package com.dlka.psonememorymanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MemoryCard {

    public static final int INITIAL = 81;
    public static final int MEDIAL = 82;
    public static final int FINAL = 83;
    public static final int FORMATTED = 160;
    public static final int DELETEDi = 161;    // initial
    public static final int DELETEDm = 162;    // medial
    public static final int DELETEDf = 163;    // final
    public static final long RESERVED = 0xFFFFFFFF;

    private static FileInputStream inputStream;
    private static DataInputStream dInputStream;

    ArrayList<Integer> empties = new ArrayList<Integer>();

    firstBlock firstblock = new firstBlock();
    saveBlock blocks[] = new saveBlock[15];
    dexHeader dexheader;

    String cardPath;

    private Context context;

    public String getDir() {
        return cardPath;
    }

    public MemoryCard(File file, Context c) {
        context = c;
        cardPath = file.getAbsolutePath();
        try {
            inputStream = new FileInputStream(file);
            dInputStream = new DataInputStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        read();
    }

    public void save() {
        FileOutputStream outStream = null;
        DataOutputStream dOutStream = null;
        FileInputStream intemp = null;

        try {
            intemp = new FileInputStream(cardPath);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        final FileInputStream in = intemp;
        final int extOfs = cardPath.lastIndexOf(".");
        final String ext = cardPath.substring(extOfs, cardPath.length());
        String expoExt;

        switch (Statics.exportFmt) {
            case 0:
                expoExt = new String(".mcd");
                break;
            case 1:
                expoExt = new String(".mcr");
                break;
            default:
                expoExt = new String(".mcd");
                break;
        }

        if (ext.contentEquals(".GME") || ext.contentEquals(".gme")) {
            cardPath = cardPath.substring(0, extOfs) + ".mcd";
        }

        if (Statics.export) {
            cardPath = cardPath.substring(0, extOfs) + expoExt;
        }
        if (Statics.backup) {
            String bakPath = cardPath.substring(0, extOfs) + ".bak" + ext;
            File outFile = new File(bakPath);
            try {
                outFile.createNewFile();

                FileOutputStream out = new FileOutputStream(bakPath);

                BufferedOutputStream dest = new BufferedOutputStream(out, 0x1FFFE);
                byte[] buffer = new byte[0x1FFFE];
                in.read(buffer);
                dest.write(buffer);
                in.close();
                dest.flush();
                dest.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        try {
            outStream = new FileOutputStream(cardPath);
            dOutStream = new DataOutputStream(outStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            // header frame
            dOutStream.writeByte('M');
            dOutStream.writeByte('C');
            for (int i = 0; i < 0x7D; i++) {
                dOutStream.writeByte(0);
            }
            dOutStream.writeByte(0x0E);

            // dir frames
            for (int i = 0; i < 35; i++) {
                dirFrame frame = firstblock.getDirFrame(i);
                frame.updateXor();
                frame.write(dOutStream);
            }
            for (int i = 0; i < 3328; i++) {
                dOutStream.writeByte(0);
            }
            for (int i = 0; i < 128; i++) {
                dOutStream.writeByte(0);
            }
            // header frame
            dOutStream.writeByte('M');
            dOutStream.writeByte('C');
            for (int i = 0; i < 0x7D; i++) {
                dOutStream.writeByte(0);
            }
            dOutStream.writeByte(0x0E);

            // save blocks
            for (int i = 0; i < 15; i++) {
                blocks[i].save(dOutStream, true);
            }


        } catch (IOException e) {

        }

        try {
            dOutStream.flush();
            dOutStream.close();
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public boolean isDeleted(int i) {
        if (firstblock.getDirFrame(i).getType() >= DELETEDi)
            return true;

        return false;
    }

    public DescIconList genList(int i) {
        Bitmap icon = blocks[i].getIcon();
        dirFrame frame = firstblock.getDirFrame(i);

        //DescIconList entry = new DescIconList(getTitleTV(i), icon);
        if (frame.getType() == 82 || frame.getType() == 83) {
            Bitmap outIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.plus);
            icon = Bitmap.createScaledBitmap(outIcon, 64, 64, false);

        }

        DescIconList entry = new DescIconList(blocks[i].getDesc(), frame.getRegion(), frame.getProd(), frame.getID(), icon, isDeleted(i));

        if (frame.getSize() / 8192 > 1 || frame.getType() == MEDIAL || frame.getType() == FINAL) {

            entry.setLinked(true);
        }
        if (frame.getType() == FORMATTED) {
            entry.setEmpty(true);
        }
        return entry;
    }

    public void delete(int num) {
        dirFrame frame = firstblock.getDirFrame(num);

        int numblocks = frame.getSize() / 8192;
        for (int i = 0; i < numblocks; i++) {
            frame = firstblock.getDirFrame(num);

            switch (frame.getType()) {
                case 81:
                    frame.setType(161);
                    break;
                case 82:
                    frame.setType(162);
                    break;
                case 83:
                    frame.setType(163);
                    break;
            }
            frame.updateXor();

            //empties.clear();
            //blocks[num] = updTitle(block, frame, num, true);
            num = frame.getNext();
        }
        updateTitles();
    }

    public String getTitle(int num) {

        return blocks[num].getTitle();
    }

    public TextView getTitleTV(int num) {
        TextView textview = new TextView(context);
        textview.setText(blocks[num].getTitle());
        dirFrame frame = firstblock.getDirFrame(num);
        if (frame.getType() >= 161) {
            textview.setTextColor(0xFFFF0000);
        } else if (frame.getType() == 82 || frame.getType() == 83) {
            textview.setTextColor(0xFF00FF00);
        } else if (frame.getType() == 160) {
            textview.setTextColor(0xFF0000FF);
        }
        return textview;
    }

    public boolean unDelete(int num) {
        int temp = num;
        dirFrame frame = firstblock.getDirFrame(num);

        int numblocks = frame.getSize() / 8192;
        for (int i = 0; i < numblocks; i++) {
            frame = firstblock.getDirFrame(num);
            if (i > 0) {
                int type = frame.getType();
                if (type != 162 && type != 163)
                    return false;
            }


            byte xor = frame.calcXor();
            xor ^= frame.getXor();
            if (xor != 0)
                return false;

            num = frame.getNext();
        }

        num = temp;
        for (int i = 0; i < numblocks; i++) {
            frame = firstblock.getDirFrame(num);


            switch (frame.getType()) {
                case 161:
                    frame.setType(81);
                    break;
                case 162:
                    frame.setType(82);
                    break;
                case 163:
                    frame.setType(83);
                    break;
            }
            //blocks[num] = updTitle(block, frame, num, true);
            num = frame.getNext();
        }
        updateTitles();
        return true;
    }

    public int getEmpty(int i) {
        return empties.get(i);
    }

    public void remEmpty(int i) {
        empties.remove(i);
    }

    public saveBlock getBlock(int num) {
        return blocks[num];
    }

    public dirFrame getFrame(int i) {
        return firstblock.getDirFrame(i);
    }

    public void setFrame(int i, dirFrame frame) {
        firstblock.setFrame(i, frame);
    }

    public void setBlock(int i, saveBlock block) {
        blocks[i] = block;
    }

    public int getNumEmpties() {
        return empties.size();
    }

    public void updateTitles() {
        empties.clear();
        for (int i = 0; i < 15; i++) {
            saveBlock block = blocks[i];
            dirFrame frame = firstblock.getDirFrame(i);
            blocks[i] = updTitle(block, frame, i, true);
        }
    }

    public saveBlock updTitle(saveBlock block, dirFrame frame, int i, boolean doEmpties) {
        String sTitle = null;
        try {
            String title = new String(block.getSJISTitle(), "Shift_JIS");
            int ch = title.indexOf("\u0000");
            if (ch != -1) {
                sTitle = title.substring(0, ch + 1);
            } else
                sTitle = title;


        } catch (UnsupportedEncodingException e) {

        }
        switch (frame.getType()) {
            case 81:
                block.setTitle(sTitle);
                break;
            case 82:
                block.setTitle("Linked - medial");
                break;
            case 83:
                block.setTitle("Linked - final");
                break;
            case 160:
                block.setTitle("Empty");
                if (doEmpties)
                    empties.add(i);
                break;
            case 161:
                block.setTitle("Deleted - " + sTitle);
                if (doEmpties)
                    empties.add(i);
                break;
            case 162:
                block.setTitle("Deleted - Linked - medial");
                if (doEmpties)
                    empties.add(i);
                break;
            case 163:
                block.setTitle("Deleted - Linked - final");
                if (doEmpties)
                    empties.add(i);
                break;
            case 0xFFFFFFFF:
                block.setTitle("Reserved");
                break;
        }
        return block;
    }

    public void read() {
        firstblock.read();
        for (int i = 0; i < 15; i++) {

            saveBlock block = new saveBlock(dInputStream);
            block.read();
            block = updTitle(block, firstblock.getDirFrame(i), i, true);

            blocks[i] = block;
        }
        try {
            dInputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }

    }

    private class dexHeader {
        byte[] magic = new byte[2];
        byte[] magic1 = new byte[14];
        short unk1 = 0;
        short unk2 = 1;
        byte unk3 = 1;
        byte[] dirTypes = new byte[16];
        byte unk = 0;
        byte[] dirNext = new byte[16];
        byte unk4 = 0;
        byte[] unused = new byte[9];
        byte[][] desc = new byte[15][256];

        public byte read() {
            magic[0] = 0x31;
            magic[1] = 0x32;
            try {
                dInputStream.read(magic1, 0, 14);
                unk1 = dInputStream.readShort();
                unk2 = dInputStream.readShort();
                unk3 = dInputStream.readByte();
                dInputStream.read(dirTypes, 0, 16);
                unk = dInputStream.readByte();
                dInputStream.read(dirNext, 0, 16);
                unk4 = dInputStream.readByte();
                dInputStream.read(unused, 0, 9);
                for (int i = 0; i < 15; i++) {
                    dInputStream.read(desc[i], 0, 256);
                }
            } catch (IOException e) {

            }
            byte numblocks = 0;
            for (int i = 0; i < 15; i++) {
                if (dirTypes[i] != 0) {
                    numblocks++;
                }
            }
            return (byte) (numblocks - 1);
        }
    }

    private class firstBlock {
        short magic;
        byte padding[] = new byte[125];
        byte xor;
        dirFrame dirframe[] = new dirFrame[35];
        byte unused[] = new byte[3328];
        byte writeTest[] = new byte[128];
        short magic2;
        byte padding2[] = new byte[125];
        byte xor2;

        public dirFrame getDirFrame(int i) {
            return dirframe[i];
        }

        public void setFrame(int i, dirFrame frame) {
            dirframe[i] = frame;
        }

        public void read() {
            try {
                magic = ByteSwapper.swap((short) dInputStream.readUnsignedShort());
                if (magic == 0x3231) {
                    dexheader = new dexHeader();
                    dexheader.read();
                    //dInputStream.skip(3902);
                    magic = ByteSwapper.swap((short) dInputStream.readUnsignedShort());
                }


                dInputStream.read(padding, 0, 125);
                xor = (byte) dInputStream.readUnsignedByte();
                for (int i = 0; i < 35; i++) {
                    dirFrame frame = new dirFrame(dInputStream);
                    frame.read();
                    dirframe[i] = frame;
                }
                dInputStream.read(unused, 0, 3328);
                dInputStream.read(writeTest, 0, 128);

                // this is really the first frame of save data
                magic2 = ByteSwapper.swap((short) dInputStream.readUnsignedShort());
                dInputStream.read(padding2, 0, 125);
                xor2 = (byte) dInputStream.readUnsignedByte();
            } catch (IOException e) {

            }
        }
    }


}


