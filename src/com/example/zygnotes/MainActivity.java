package com.example.zygnotes;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	private NotesDB notesDB;
	private SQLiteDatabase dbReader;
	private Button teButton,iButton,vButton;
	private ListView lv;
	private Intent i;
	private MyAdapter adapter;
	private Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initview();

	}
	public void initview(){
		lv=(ListView) findViewById(R.id.list);
		teButton=(Button) findViewById(R.id.text);
		iButton=(Button) findViewById(R.id.img);
		vButton=(Button) findViewById(R.id.video);
		teButton.setOnClickListener(this);
		iButton.setOnClickListener(this);
		vButton.setOnClickListener(this);
		notesDB=new NotesDB(this);
		dbReader=notesDB.getReadableDatabase();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Log.d("TAG", position+"");
	             Log.d("TAG", id+"");
	            if (cursor!=null) {
	            	cursor.moveToPosition(position);
					Intent intent = new Intent(MainActivity.this, SelectAct.class);
					intent.putExtra(NotesDB.ID,
							cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
					intent.putExtra(NotesDB.CONTENT, cursor.getString(cursor
							.getColumnIndex(NotesDB.CONTENT)));
					intent.putExtra(NotesDB.TIME,
							cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
					intent.putExtra(NotesDB.PATH,
							cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
					intent.putExtra(NotesDB.VIDEO,
							cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
					startActivity(intent);
				}
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		i=new Intent(this,AddContent.class);
		switch (v.getId()) {
		case R.id.text:
			i.putExtra("flag","1");
			startActivity(i);
			break;
		case R.id.img:
			i.putExtra("flag","2");
			startActivity(i);
			break;
		case R.id.video:
			i.putExtra("flag","3");
			startActivity(i);
			break;
		default:
			break;
		}
		
	}
	
	
	public void selectDB(){
		cursor=dbReader.query(NotesDB.TABLE_NAME,null,null,null,
				null,null,null);
		adapter=new MyAdapter(this, cursor);
		lv.setAdapter(adapter);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		selectDB();
	}
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
//	public void addDB() {
//		ContentValues cv=new ContentValues();
//		cv.put(NotesDB.CONTENT,"Hello");
//		cv.put(NotesDB.TIME, Gettime());
//		dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
//	}
//	public String Gettime(){
//		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
//		Date curDate=new Date();
//		String str=format.format(curDate);
//		return str;
//	}

}
