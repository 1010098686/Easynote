package com.fangkang.easynote;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideo extends Activity {
	
	private String path;
	private VideoView vv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playvideo);
		
		path = getIntent().getExtras().getString(VideoListActivity.PLAYVIDEO);
		
		vv=(VideoView) findViewById(R.id.videoview);
		vv.setMediaController(new MediaController(this));
		vv.setVideoPath(path);
	}
}
