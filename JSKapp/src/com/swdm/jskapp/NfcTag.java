package com.swdm.jskapp;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NfcTag extends Activity {

	private NfcAdapter nfcAdapter;
	private PendingIntent pendingIntent;
	
	private TextView tagDesc;
	private String Loc;
	private String TagId;
	private String strNow;
	private String ID="";
	private ImageView ox;
	
	Context context;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        
      //  tagDesc = (TextView)findViewById(R.id.tagDesc);
        
       // ox = (ImageView)findViewById(R.id.imageView1);
       // ox.setImageResource(R.drawable.x);
        
       // Intent myIntent = getIntent();
       // ID = myIntent.getStringExtra("ID");
        //Loc = myIntent.getStringExtra("loc");
        //Bundle b = myIntent.getExtras();
        
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

	@Override
	protected void onPause() {
		if (nfcAdapter != null) {
			nfcAdapter.disableForegroundDispatch(this);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcAdapter != null) {
			nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);   /////////////////////////////////
		
		if (tag != null) {
			byte[] tagId = tag.getId();
			Toast.makeText(this, "방문확인!", 1).show();
			
			// 현재 시간을 msec으로 구한다.
//			long now = System.currentTimeMillis();
			// 현재 시간을 저장 한다.
//			Date date = new Date(now);
			// 시간 포맷으로 만든다.
//			SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//			strNow = sdfNow.format(date);
			TagId = toHexString(tagId);
			
//			tagDesc.setText("TagID: " + TagId +"\n현재위치: "+Loc+"\n현재시간:"+strNow);
//			ox.setImageResource(R.drawable.o);
//			 (new informationSet(context)).execute(new Location[] {null});
		}
		
	}
	
//	private class informationSet extends AsyncTask<Location, Void, Void> {
//		
//		public informationSet(Context context) {
//			super();       
//		}
//
//		@SuppressLint("DefaultLocale") @Override
//		protected Void doInBackground(Location... params) { // db update 
//			
//			DBmanager db=new DBmanager("update mobile set attendance='"+ TagId+"|"+Loc+"|"+strNow+"' where id='"+ ID +"'");
//			db.start3();
//
//			return null;
//		}
//
//		protected void onPostExecute(final Void unused) {
//			//finish();
//		}
//	}

	
	
	public static final String CHARS = "0123456789ABCDEF";
	
	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
				.append(CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}
}

