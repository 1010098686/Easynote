package com.fangkang.easynote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ViewSwitcher.ViewFactory;

public class PhotosActivity extends Activity implements ViewFactory{
	
	private String[] pics;
	
	private ImageSwitcher imgSwitcher;
	
	private int index;
	private boolean modified;
	private float pre_x;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photos);
		
		pics = getIntent().getExtras().getStringArray(AddNoteActivity.PHOTOS);
		
		
		index=0;
		imgSwitcher = (ImageSwitcher) findViewById(R.id.imgswitcher);
		imgSwitcher.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action==MotionEvent.ACTION_DOWN)
				{
					pre_x = event.getX();
				}
				if(action==MotionEvent.ACTION_UP)
				{
					float x = event.getX();
					if(x-pre_x>50)
					{
						index=(index-1+pics.length)%pics.length;
						setImageSwitcherContent(index);
					}
					else if(x-pre_x<-50)
					{
						index=(index+1)%pics.length;
						setImageSwitcherContent(index);
					}
				}
				return true;
			}
		});
		
		imgSwitcher.setFactory(this);
		imgSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		imgSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		setImageSwitcherContent(index);
		
		modified=false;
	}
	
	private void setImageSwitcherContent(int index)
	{
		imgSwitcher.setImageDrawable(new BitmapDrawable(pics[index]));
	}
	
	private void backTo()
	{
		Intent i = new Intent();
		Bundle data = new Bundle();
		data.putStringArray(AddNoteActivity.PHOTOS, pics);
		i.putExtras(data);
		setResult(RESULT_OK,i);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(!modified)
			{
				setResult(RESULT_CANCELED);
				finish();
			}
			else
			{
				backTo();
			}
			return true;
		}
		else return super.onKeyDown(keyCode, event);
	}
	
	
	
	
	@Override
	public View makeView() {
		ImageView image = new ImageView(this);
		image.setScaleType(ImageView.ScaleType.FIT_CENTER);
		image.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		return image;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_delete, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==R.id.actionbar_delete)
		{
			String path = pics[index];
			File f = new File(path);
			if(f.exists())
			{
				f.delete();
			}
			List<String> list = new ArrayList<String>();
			Collections.addAll(list,pics);
			list.remove(index);
			int size = list.size();
			pics = list.toArray(new String[size]);
			if(size==0)
			{
				backTo();
				finish();
			}
			modified=true;
			index=0;
			setImageSwitcherContent(index);
		}
		return super.onOptionsItemSelected(item);
	}
}
