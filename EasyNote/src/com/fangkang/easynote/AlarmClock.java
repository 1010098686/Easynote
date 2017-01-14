package com.fangkang.easynote;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class AlarmClock implements Parcelable{
	
	public static final String TITLE= "title";

	private int year,month,day;
	private int hour,minute;
	private String title;
	
	private boolean seted;
	
	public AlarmClock(int y,int m,int d,int h,int minute,String title)
	{
		this.year=y;
		this.month=m;
		this.day=d;
		this.hour=h;
		this.minute=minute;
		this.title=title;
		seted=false;
	}
	
	private AlarmClock(Parcel in)
	{
		year = in.readInt();
		month = in.readInt();
		day = in.readInt();
		hour = in.readInt();
		minute = in.readInt();
		title = in.readString();
		boolean[] temp = new boolean[1];
		in.readBooleanArray(temp);
		seted=temp[0];
	}
	
	public void set(Context context)
	{
		if(seted==false)
		{
			seted=true;
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE,minute);
			Intent i = new Intent(context,AlarmReceiver.class);
			Bundle data = new Bundle();
			data.putString(TITLE, title);
			i.putExtras(data);
			PendingIntent pi = PendingIntent.getBroadcast(context, (int)calendar.getTimeInMillis(), i, 0);
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
		}
	}
	
	public void cancel(Context context)
	{
		if(seted)
		{
			seted=false;
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE,minute);
			Intent i = new Intent(context,AlarmReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(context, (int)calendar.getTimeInMillis(), i, 0);
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.cancel(pi);
		}
	}
	
	public int getYear()
	{
		return year;
	}
	
	public int getMouth()
	{
		return month;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getMinute()
	{
		return minute;
	}
	
	public int getHour()
	{
		return hour;
	}
	
	public boolean isSeted()
	{
		return seted;
	}
	
	public Date getDate()
	{
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE,minute);
		return new Date(c.getTimeInMillis());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(year);
		dest.writeInt(month);
		dest.writeInt(day);
		dest.writeInt(hour);
		dest.writeInt(minute);
		dest.writeString(title);
		dest.writeBooleanArray(new boolean[]{seted});
	}
	
	public static final Parcelable.Creator<AlarmClock> CREATOR = new Parcelable.Creator<AlarmClock>() {

		@Override
		public AlarmClock createFromParcel(Parcel arg0) {
			return new AlarmClock(arg0);
		}

		@Override
		public AlarmClock[] newArray(int arg0) {
			return new AlarmClock[arg0];
		}
	};
}
