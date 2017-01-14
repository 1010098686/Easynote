package com.fangkang.easynote;

import java.io.File;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.LayoutInflaterFactory;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private Context context;
	
	public GridViewAdapter(Context context)
	{
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return NoteStore.getInstance().getSize();
	}

	@Override
	public Note getItem(int position) {
		return NoteStore.getInstance().getNote(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.maingriditem, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.notetitle);
		TextView date = (TextView) convertView.findViewById(R.id.notedate);
		ImageView image = (ImageView) convertView.findViewById(R.id.notepic);
		Note note = NoteStore.getInstance().getNote(position);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		title.setText(note.getTitle());
		date.setText(df.format(note.getDate()));
		if(note.getBitmap().length==0)
		{
			image.setImageResource(R.drawable.default_album);
		}
		else
		{
			image.setImageURI(Uri.fromFile(new File(note.getBitmap()[0])));
		}
		return convertView;
	}

}
