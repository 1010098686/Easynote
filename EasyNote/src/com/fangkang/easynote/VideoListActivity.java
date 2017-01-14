package com.fangkang.easynote;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class VideoListActivity extends ListActivity implements OnItemClickListener, OnItemLongClickListener {
	
	public static final String PLAYVIDEO = "playvideo";
	
	private String[] videos;
	private boolean modified;
	private VideoListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		videos = getIntent().getExtras().getStringArray(AddNoteActivity.VIDEOS);
		modified=false;
		adapter = new VideoListAdapter(this);
		setListAdapter(adapter);
		
		ListView lv = getListView();
		lv.setOnItemClickListener(this);
		lv.setOnItemLongClickListener(this);
	}

	
	class VideoListAdapter extends BaseAdapter
	{
		private Context context;
		
		public VideoListAdapter(Context c)
		{
			context=c;
		}

		@Override
		public int getCount() {
			return videos.length;
		}

		@Override
		public String getItem(int position) {
			return videos[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.videolist_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.videolist_name);
			tv.setText(videos[position]);
			return convertView;
		}
		
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(modified)
			{
				Intent i = new Intent();
				Bundle data = new Bundle();
				data.putStringArray(AddNoteActivity.VIDEOS, videos);
				i.putExtras(data);
				setResult(RESULT_OK,i);
				finish();
			}
			else
			{
				setResult(RESULT_CANCELED);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final int index = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("options:");
		builder.setItems(new String[]{"É¾³ý"}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which==0)
				{
					String path = videos[index];
					File f = new File(path);
					if(f.exists())
					{
						f.delete();
					}
					
					List<String> list = new ArrayList<String>();
					Collections.addAll(list, videos);
					list.remove(index);
					int size = list.size();
					videos = list.toArray(new String[size]);
					modified=true;
					adapter.notifyDataSetChanged();
				}
			}
		});
		builder.show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(VideoListActivity.this,PlayVideo.class);
		Bundle data = new Bundle();
		data.putString(PLAYVIDEO, videos[position]);
		i.putExtras(data);
		startActivity(i);
	}
	
}
