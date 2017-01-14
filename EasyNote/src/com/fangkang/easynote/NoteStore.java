package com.fangkang.easynote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.Parcel;

public class NoteStore{
	
	private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Easynote";
	private static final String filepath = dir+"/data.dat";
	
	private static NoteStore instance = new NoteStore();
	
	public static  NoteStore getInstance()
	{
		return instance;
	}
	
	public static String getStoragePath()
	{
		return dir;
	}
	
	private List<Note> list;
	
	private NoteStore()
	{
		list = new ArrayList<Note>();
	}
	
	public void save()
	{
		try {
			File file = new File(dir);
			if(!file.exists())
			{
				file.mkdir();
			}
			File data = new File(filepath);
			if(!data.exists())
			{
				data.createNewFile();
			}
			FileOutputStream filestream = new FileOutputStream(filepath);
			DataOutputStream out = new DataOutputStream(filestream);
			out.writeInt(list.size());
			for(Note note:list)
			{
				byte[] bytes = ParcelableUtils.marshall(note);
				out.writeInt(bytes.length);
				out.write(bytes);
			}
			out.close();
			filestream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load()
	{
		try {
			File file = new File(dir);
			if(!file.exists())
			{
				return;
			}
			File data = new File(filepath);
			if(!data.exists())
			{
				return;
			}
			FileInputStream filestream  = new FileInputStream(data);
			DataInputStream in = new DataInputStream(filestream);
			int size;
			list.clear();
			size = in.readInt();
			for(int i=0;i<size;++i)
			{
				int length = in.readInt();
				byte[] bytes = new byte[length];
				in.read(bytes);
				Parcel parcel = ParcelableUtils.unmarshall(bytes);
				Note note = Note.CREATOR.createFromParcel(parcel);
				list.add(note);
			}
			in.close();
			filestream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addNote(Note note)
	{
		list.add(note);
	}
	
	public void deleteNote(int index)
	{
		list.remove(index);
	}
	
	public void deleteNote(Note note)
	{
		list.remove(note);
	}
	
	public Note getNote(int index)
	{
		return list.get(index);
	}
	
	public int getSize()
	{
		return list.size();
	}
	
	public void modify(int position,Note note)
	{
		list.remove(position);
		list.add(note);
	}
}
