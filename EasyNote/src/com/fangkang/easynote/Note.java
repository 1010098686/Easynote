package com.fangkang.easynote;

import java.util.Arrays;
import java.util.Date;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable{

	
	
	private String title;
	private String content;
	private long time;
	private String[] pic;
	private String[] video;
	private AlarmClock[] alarm;
	
	public Note(String title,String content,Date date,String[] bitmaps,String[] video,AlarmClock[] clocks)
	{
		this.title=title;
		this.content=content;
		this.time=date.getTime();
		this.pic=bitmaps;
		this.video = video;
		this.alarm=clocks;
	}
	
	private Note(Parcel in)
	{
		title = in.readString();
		content = in.readString();
		time=in.readLong();
		int size_photo = in.readInt();
		pic = new String[size_photo];
		in.readStringArray(pic);
		int size_video = in.readInt();
		video = new String[size_video];
		in.readStringArray(video);
		int size_alarm = in.readInt();
		Parcelable[] temp = new Parcelable[size_alarm];
		temp = in.readParcelableArray(AlarmClock.class.getClassLoader());
		if(temp!=null)
		{
			alarm = Arrays.copyOf(temp, temp.length, AlarmClock[].class);
		}
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getContent()
	{
		return this.content;
	}
	
	public Date getDate()
	{
		Date d = new Date();
		d.setTime(time);
		return d;
	}
	
	public String[] getBitmap()
	{
		return this.pic;
	}
	
	public AlarmClock[] getAlarm()
	{
		return alarm;
	}
	
	public void setBitmap(String[] bm)
	{
		this.pic=bm;
	}
	
	public String[] getVideo()
	{
		return video;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(title);
		dest.writeString(content);
		dest.writeLong(time);
		dest.writeInt(pic.length);
		dest.writeStringArray(pic);
		dest.writeInt(video.length);
		dest.writeStringArray(video);
		dest.writeInt(alarm.length);
		dest.writeParcelableArray(alarm, arg1);
	}
	
	public static final Parcelable.Creator<Note> CREATOR  = new Parcelable.Creator<Note>() {

		@Override
		public Note createFromParcel(Parcel arg0) {
			return new Note(arg0);
		}

		@Override
		public Note[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new Note[arg0];
		}
	};
}
