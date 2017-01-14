package com.fangkang.easynote;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class AddNoteActivity extends Activity implements TextWatcher, OnClickListener {
	
	public static final String MODE = "mode";
	public static final int MODE_SEE = 4;
	public static final int MODE_ADD = 5;
	
	public static final String INDEX = "index";
	
	public static final String NOTE = "note";
	
	public static final int TAKE_PHOTO = 1;
	public static final int TAKE_VIDEO = 7;
	public static final int CHOOSE_PHOTO = 6;
	
	public static final String PHOTOS = "photos";
	public static final int SEE_PHOTO = 2;
	
	public static final String ALARMS = "alarms";
	public static final int SEE_ALARM = 3;
	
	public static final String VIDEOS = "videos";
	public static final int SEE_VIDEO = 8;
	
	private EditText title;
	private EditText content;
	private Button btnTakephoto,btnSavenote,btnSetalarm,btnRecordvideo;
	private Button btnPhotos,btnAlarms,btnVideos;
	private LinearLayout toolbar;
	
	private Date date;
	private List<String> photos;
	private List<AlarmClock> alarm;
	private List<String> videos;
	
	private Note note;
	private int mode;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);
		
		toolbar = (LinearLayout) findViewById(R.id.toolbar);
		
		title = (EditText) findViewById(R.id.addnote_title);
		title.addTextChangedListener(this);
		
		content = (EditText) findViewById(R.id.addnote_content);
		
		btnTakephoto = (Button) findViewById(R.id.btnTakephoto);
		btnSavenote = (Button) findViewById(R.id.btnSavenote);
		btnSetalarm = (Button) findViewById(R.id.btnSetalarm);
		btnRecordvideo = (Button) findViewById(R.id.btnRecordvideo);
		btnPhotos = (Button) findViewById(R.id.btnPhotos);
		btnAlarms = (Button) findViewById(R.id.btnAlarms);
		btnVideos = (Button) findViewById(R.id.btnVideos);
		btnTakephoto.setOnClickListener(this);
		btnSavenote.setOnClickListener(this);
		btnSetalarm.setOnClickListener(this);
		btnPhotos.setOnClickListener(this);
		btnAlarms.setOnClickListener(this);
		btnRecordvideo.setOnClickListener(this);
		btnVideos.setOnClickListener(this);
		
		initMode();
		
	}
	
	private void initMode()
	{
		Intent i = getIntent();
		Bundle data = i.getExtras();
		mode = data.getInt(MODE);
		if(mode==MODE_ADD)
		{
			date = new Date();
			date.setTime(System.currentTimeMillis());
			photos = new ArrayList<String>();
			alarm = new ArrayList<AlarmClock>();
			videos = new ArrayList<String>();
			
			checkButtons();
			
			checkTitle();
			
			ActionBar actionbar = getActionBar();
			actionbar.hide();
			
			toolbar.setVisibility(View.VISIBLE);
		}
		else if(mode==MODE_SEE)
		{
			index = data.getInt(INDEX);
			note = NoteStore.getInstance().getNote(index);
			date=note.getDate();
			photos = new ArrayList<String>();
			Collections.addAll(photos, note.getBitmap());
			alarm = new ArrayList<AlarmClock>();
			Collections.addAll(alarm, note.getAlarm());
			videos = new ArrayList<String>();
			Collections.addAll(videos, note.getVideo());
			
			title.setText(note.getTitle());
			content.setText(note.getContent());
			title.setFocusable(false);
			title.setFocusableInTouchMode(false);
			content.setFocusable(false);
			content.setFocusableInTouchMode(false);
			
			checkTitle();
			
			checkButtons();
			
			ActionBar actionbar = getActionBar();
			actionbar.show();
			
			toolbar.setVisibility(View.GONE);
			
		}
		
		
	}
	
	private void backToGrid()
	{
		setResult(RESULT_CANCELED);
		finish();
	}
	
	private String saveBitmap(Bitmap bm)
	{
		File path = new File(NoteStore.getStoragePath());
		if(!path.exists())
		{
			path.mkdir();
		}
		File file = new File(path+"/"+System.currentTimeMillis()+".png");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
			return file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void checkTitle()
	{
		String str = title.getText().toString();
		boolean flag = str.length()>0;
		btnSavenote.setEnabled(flag);
	}
	
	private void checkButtons()
	{
		boolean f1 = photos.size()>0;
		if(f1)
		{
			if(!btnPhotos.isEnabled())
			{
				btnPhotos.setVisibility(View.VISIBLE);
				btnPhotos.setEnabled(true);
				Animation aa = AnimationUtils.loadAnimation(this, R.anim.fromleft);
				btnPhotos.startAnimation(aa);
			}
		}
		else
		{
			btnPhotos.setEnabled(false);
			btnPhotos.setVisibility(View.GONE);
			Animation aa = AnimationUtils.loadAnimation(this, R.anim.toleft);
			btnPhotos.startAnimation(aa);
		}
		
		boolean f2 = alarm.size()>0;
		if(f2)
		{
			if(!btnAlarms.isEnabled())
			{
				btnAlarms.setVisibility(View.VISIBLE);
				btnAlarms.setEnabled(true);
				Animation aa  = AnimationUtils.loadAnimation(this, R.anim.fromleft);
				btnAlarms.startAnimation(aa);
			}
		}
		else
		{
			btnAlarms.setEnabled(false);
			btnAlarms.setVisibility(View.GONE);
			Animation aa = AnimationUtils.loadAnimation(this, R.anim.toleft);
			btnAlarms.startAnimation(aa);
		}
		
		boolean f3 = videos.size()>0;
		if(f3)
		{
			if(!btnVideos.isEnabled())
			{
				btnVideos.setVisibility(View.VISIBLE);
				btnVideos.setEnabled(true);
				Animation aa = AnimationUtils.loadAnimation(this, R.anim.fromleft);
				btnVideos.startAnimation(aa);
			}
		}
		else
		{
			btnVideos.setVisibility(View.GONE);
			btnVideos.setEnabled(false);
			Animation aa = AnimationUtils.loadAnimation(this, R.anim.toleft);
			btnVideos.startAnimation(aa);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			backToGrid();
			return true;
		}
		else return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		checkTitle();
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		checkTitle();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==TAKE_PHOTO && resultCode==RESULT_OK)
		{
			Bitmap bm = (Bitmap) data.getExtras().get("data");
			String path = saveBitmap(bm);
			photos.add(path);
			checkButtons();
		}
		else if(requestCode == SEE_PHOTO && resultCode == RESULT_OK)
		{
			Bundle bundle = data.getExtras();
			String[] temp = bundle.getStringArray(PHOTOS);
			photos.clear();
			Collections.addAll(photos, temp);
			checkButtons();
		}
		else if(requestCode == SEE_ALARM && resultCode==RESULT_OK)
		{
			Bundle bundle = data.getExtras();
			Parcelable[] temp = bundle.getParcelableArray(ALARMS);
			if(temp!=null)
			{
				alarm.clear();
				AlarmClock[] clock = Arrays.copyOf(temp, temp.length, AlarmClock[].class);
				Collections.addAll(alarm, clock);
				checkButtons();
			}
		}
		else if(requestCode==CHOOSE_PHOTO && resultCode==RESULT_OK)
		{
			Uri uri = data.getData();
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(index);
			photos.add(path);
			checkButtons();
		}
		else if(requestCode==TAKE_VIDEO)
		{
			if(resultCode==RESULT_OK)
			{
				checkButtons();
			}
			else if(resultCode!=RESULT_OK)
			{
				int size = videos.size();
				String path = videos.get(size-1);
				File f = new File(path);
				if(f.exists())
				{
					f.delete();
				}
				videos.remove(size-1);
				checkButtons();
			}
		}
		else if(requestCode==SEE_VIDEO && resultCode==RESULT_OK)
		{
			Bundle bundle = data.getExtras();
			String[] temp = bundle.getStringArray(VIDEOS);
			videos.clear();
			Collections.addAll(videos, temp);
			checkButtons();
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id)
		{
		case R.id.btnTakephoto:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("please choose");
			builder.setItems(new String[]{"相机","从相册选择"}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which==0)
					{
						Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
						startActivityForResult(i, TAKE_PHOTO);
					}
					else if(which==1)
					{
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.setType("image/*");
						startActivityForResult(i, CHOOSE_PHOTO);
					}
				}
			});
			builder.show();
			break;
		case R.id.btnSavenote:
			int size_photo = photos.size();
			int size_alarm = alarm.size();
			int size_video = videos.size();
			note = new Note(title.getText().toString(), content.getText().toString(), date, (String[])photos.toArray(new String[size_photo]),(String[])videos.toArray(new String[size_video]),(AlarmClock[])alarm.toArray(new AlarmClock[size_alarm]));
			if(mode==MODE_ADD)
			{
				NoteStore.getInstance().addNote(note);
			}
			else if(mode==MODE_SEE)
			{
				NoteStore.getInstance().modify(index, note);
			}
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.btnSetalarm:
			Calendar calendar = Calendar.getInstance();
			int y = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH);
			int d = calendar.get(Calendar.DAY_OF_MONTH);
			final DatePickerDialog date = new DatePickerDialog(AddNoteActivity.this, null, y, m, d);
			date.setButton(DialogInterface.BUTTON_POSITIVE,"ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DatePicker picker = date.getDatePicker();
					final int year = picker.getYear();
					final int month = picker.getMonth();
					final int day = picker.getDayOfMonth();
					AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
					builder.setTitle("choose time please");
					final TimePicker time = new TimePicker(AddNoteActivity.this);
					time.setIs24HourView(true);
					builder.setView(time);
					builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							int hour = time.getCurrentHour();
							int minute = time.getCurrentMinute();
							AlarmClock clock = new AlarmClock(year, month, day, hour, minute,title.getText().toString());
							alarm.add(clock);
							checkButtons();
						}
					});
					builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.show();
				}
			});
			date.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			date.show();
			break;
		case R.id.btnPhotos:
			Intent tar = new Intent(AddNoteActivity.this,PhotosActivity.class);
			Bundle data = new Bundle();
			int size = photos.size();
			data.putStringArray(PHOTOS, photos.toArray(new String[size]));
			tar.putExtras(data);
			startActivityForResult(tar, SEE_PHOTO);
			break;
		case R.id.btnAlarms:
			Intent tar_alarm = new Intent(AddNoteActivity.this,AlarmActivity.class);
			Bundle alarm_data = new Bundle();
			int alarm_size = alarm.size();
			alarm_data.putParcelableArray(ALARMS, alarm.toArray(new AlarmClock[alarm_size]));
			tar_alarm.putExtras(alarm_data);
			startActivityForResult(tar_alarm, SEE_ALARM);
			break;
		case R.id.btnRecordvideo:
			Intent recordVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			String path = createVideoPath();
			videos.add(path);
			recordVideo.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
			startActivityForResult(recordVideo, TAKE_VIDEO);
			break;
		case R.id.btnVideos:
			Intent tar_video = new Intent(AddNoteActivity.this,VideoListActivity.class);
			Bundle video_data = new Bundle();
			int video_size = videos.size();
			video_data.putStringArray(VIDEOS, videos.toArray(new String[video_size]));
			tar_video.putExtras(video_data);
			startActivityForResult(tar_video, SEE_VIDEO);
			break;
		}
		
	}
	
	private String createVideoPath()
	{
		File path = new File(NoteStore.getStoragePath());
		if(!path.exists())
		{
			path.mkdir();
		}
		File file = new File(path+"/"+System.currentTimeMillis()+".mp4");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file.getAbsolutePath();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.actionbar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch(id)
		{
		case R.id.actionbar_edit:
			checkButtons();
			title.setFocusable(true);
			title.setFocusableInTouchMode(true);
			title.requestFocus();
			content.setFocusable(true);
			content.setFocusableInTouchMode(true);
			content.requestFocus();
			Animation aa = AnimationUtils.loadAnimation(this, R.anim.frombuttom);
			toolbar.startAnimation(aa);
			toolbar.setVisibility(View.VISIBLE);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
