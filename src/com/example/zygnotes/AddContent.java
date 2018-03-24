package com.example.zygnotes;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

public class AddContent extends Activity implements OnClickListener{
	private String val;
	private Button saveButton,deleteButton;
	private EditText ettext;
	private ImageView c_img;
	private VideoView v_videoView;
	private NotesDB notesDB;
	private SQLiteDatabase dbWriter;
	private File phoneFile,videoFile;
	private Bitmap bitmap;
	private Date curDate;
	public static String STRING=null;
	public int i=0;
	public int j=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addcontent);
		STRING = Environment .getExternalStorageDirectory().getAbsolutePath();//绝对路径是/storage/emulated/0
		val=getIntent().getStringExtra("flag");//传了空指针
		saveButton=(Button) findViewById(R.id.save);
		deleteButton=(Button) findViewById(R.id.delete);
		ettext=(EditText) findViewById(R.id.ettext);
		c_img=(ImageView) findViewById(R.id.c_img);
		v_videoView=(VideoView) findViewById(R.id.c_video);
		saveButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		notesDB=new NotesDB(this);
		dbWriter=notesDB.getWritableDatabase();
		initview();
	}
	public void initview(){
		if (val.equals("1")) {//文字//问题？
			c_img.setVisibility(View.GONE);
			v_videoView.setVisibility(View.GONE);
					}
		if (val.equals("2")) {//图片
			c_img.setVisibility(View.VISIBLE);
			v_videoView.setVisibility(View.GONE);	
			Intent img=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			phoneFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
					+GetTime()+".jpg");
//			File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
//		    if (!appDir.exists()) {
//		        appDir.mkdir();
//		    }
//		    String fileName=GetTime()+".jpg";
//		    
//		    phoneFile=new File(appDir, fileName);
//		    j++;
//		    System.out.println("ddd"+phoneFile);
//		    try {
//		    	FileOutputStream out=new FileOutputStream(phoneFile);
//		    	bitmap.compress(CompressFormat.PNG,100,out);
//		    	 out.flush();
//		    	 out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			File DatalDir = Environment.getExternalStorageDirectory();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss",
//			Locale.SIMPLIFIED_CHINESE);
//			String midPath = "DCIM/Camera/"+ GetTime()+ ".jpg";
//			phoneFile=new File(DatalDir.getAbsolutePath() + midPath);
			img.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(phoneFile));
			Log.d("TAG", phoneFile + "");
			startActivityForResult(img,1);
			
		}
		if (val.equals("3")) {//视频
			c_img.setVisibility(View.GONE);
			v_videoView.setVisibility(View.VISIBLE);
			Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			videoFile = new File(Environment.getExternalStorageDirectory()
					.getAbsoluteFile() +"/"+curDate+ ".mp4");
			
			video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
//			i++;
			startActivityForResult(video, 2);
			
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			addDB();
			finish();
			break;
		case R.id.delete:
			finish();
			break;
		default:
			break;
		}
		
	}
	public void addDB() {
		ContentValues cv=new ContentValues();
	
		cv.put(NotesDB.CONTENT, ettext.getText().toString());
		cv.put(NotesDB.TIME,GetTime());
		cv.put(NotesDB.PATH,phoneFile+"");
		cv.put(NotesDB.VIDEO, videoFile + "");
		dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
		
	}
	private String GetTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		return str;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			Bitmap bitmap = BitmapFactory.decodeFile(phoneFile
					.getAbsolutePath());
			c_img.setImageBitmap(bitmap);
		}
		if (requestCode == 2) {
			v_videoView.setVideoURI(Uri.fromFile(videoFile));
			v_videoView.start();
		}
	}
}
