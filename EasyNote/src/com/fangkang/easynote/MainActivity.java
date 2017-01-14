package com.fangkang.easynote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {
	
	public static final int ADD_NOTE = 1;
	
	private GridView gridView;
	private GridViewAdapter adapter;
	private Button addbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		NoteStore.getInstance().load();
		
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new GridViewAdapter(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);
		
		addbtn = (Button) findViewById(R.id.addnote);
		addbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,AddNoteActivity.class);
				Bundle data = new Bundle();
				data.putInt(AddNoteActivity.MODE, AddNoteActivity.MODE_ADD);
				i.putExtras(data);
				startActivityForResult(i, ADD_NOTE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==ADD_NOTE && resultCode==RESULT_OK)
		{
			adapter.notifyDataSetChanged();
			NoteStore.getInstance().save();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(MainActivity.this,AddNoteActivity.class);
		Bundle data = new Bundle();
		data.putInt(AddNoteActivity.MODE, AddNoteActivity.MODE_SEE);
		data.putInt(AddNoteActivity.INDEX, position);
		i.putExtras(data);
		startActivityForResult(i, ADD_NOTE);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final int index = position;
		PopupMenu menu = new PopupMenu(this, view);
		menu.getMenuInflater().inflate(R.menu.popupmenu, menu.getMenu());
		menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int id = item.getItemId();
				switch(id)
				{
				case R.id.popupmenu_delete:
					NoteStore.getInstance().deleteNote(index);
					adapter.notifyDataSetChanged();
					NoteStore.getInstance().save();
					break;
				}
				return false;
			}
		});
		menu.show();
		return false;
	}
}
