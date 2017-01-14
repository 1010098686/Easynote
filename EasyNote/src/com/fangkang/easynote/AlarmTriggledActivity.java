package com.fangkang.easynote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AlarmTriggledActivity extends Activity {
	
	private boolean flag;
	
	
	private Vibrator vi;
	
	private static final long[] pattern = {1000,1000};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vi.vibrate(pattern, 0);
		String contentText = getIntent().getExtras().getString(AlarmClock.TITLE);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Easynote");
		builder.setMessage(contentText);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				vi.cancel();
				finish();
			}
		});
		builder.show();
	}

}