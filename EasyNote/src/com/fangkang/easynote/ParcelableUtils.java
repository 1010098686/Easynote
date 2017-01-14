package com.fangkang.easynote;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUtils {

	public static byte[] marshall(Parcelable parcelable)
	{
		Parcel parcel = Parcel.obtain();
		parcel.setDataPosition(0);
		parcelable.writeToParcel(parcel, 0);
		byte[] bytes = parcel.marshall();
		parcel.recycle();
		return bytes;
	}
	
	
	public static Parcel unmarshall(byte[] bytes)
	{
		Parcel parcel = Parcel.obtain();
		parcel.unmarshall(bytes, 0, bytes.length);
		parcel.setDataPosition(0);
		return parcel;
	}
}
