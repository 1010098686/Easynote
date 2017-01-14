package com.fangkang.easynote;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AlarmActivity extends ListActivity {

	AlarmClock[] alarm;
	
	private boolean modified;
	
	private ListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Parcelable[] temp = getIntent().getExtras().getParcelableArray(AddNoteActivity.ALARMS);
		if(temp!=null)
		{
			alarm = Arrays.copyOf(temp, temp.length, AlarmClock[].class);
		}
		
		modified=false;
		
		adapter = new ListAdapter(this);
		setListAdapter(adapter);	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(modified==false)
			{
				setResult(RESULT_CANCELED);
				finish();
			}
			else
			{
				Intent i = new Intent();
				Bundle data = new Bundle();
				data.putParcelableArray(AddNoteActivity.ALARMS, alarm);
				i.putExtras(data);
				setResult(RESULT_OK, i);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	class ListAdapter extends BaseAdapter
	{
		private Context context;
		
		public ListAdapter(Context c)
		{
			this.context=c;
		}

		@Override
		public int getCount() {
			return alarm.length;
		}

		@Override
		public AlarmClock getItem(int arg0) {
			return alarm[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null)
			{
				arg1 = LayoutInflater.from(context).inflate(R.layout.alarm_item, null);
			}
			final AlarmClock clock = alarm[arg0];
			TextView text = (TextView) arg1.findViewById(R.id.alarmlist_text);
			final Switch button = (Switch) arg1.findViewById(R.id.alarmlist_button);
			Date date = clock.getDate();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			text.setText(df.format(date));
			button.setChecked(clock.isSeted());
			button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if(arg1) clock.set(context);
					else clock.cancel(context);
					button.setChecked(arg1);
					modified=true;
				}
			});
			return arg1;
		}
		
	}
}
