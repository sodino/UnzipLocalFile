package lab.sodino.unziplocalzip;

import java.io.File;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	public static final String ZIP_PATH = "/sdcard/test/demo.zip";
	public static final String UNZIP_FOLDER_PATH = "/sdcard/test/demo";
	
	TextView txtZipHint;
	Button btnUnzip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtZipHint = (TextView) findViewById(R.id.txtZipHint);
		btnUnzip = (Button) findViewById(R.id.btnUnzip);
		btnUnzip.setOnClickListener(this);
		
		File f = new File(ZIP_PATH);
		if(f.exists()){
			txtZipHint.setText(ZIP_PATH +" does exist. Click 'Unzip'");
			btnUnzip.setEnabled(true);
		}else{
			txtZipHint.setText(ZIP_PATH +" does not exist.");
			btnUnzip.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v == btnUnzip){
			new Thread(){
				public void run(){
					try {
						FileUtils.uncompressZip(ZIP_PATH, UNZIP_FOLDER_PATH);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

}
