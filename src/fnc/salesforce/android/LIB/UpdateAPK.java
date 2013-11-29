package fnc.salesforce.android.LIB;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.URLConstance;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UpdateAPK {
	
	public UpdateAPK( Context context ){
		mContext = context;
		PD = new ProgressDialog( context );
	}
	private Context mContext;
	
	private DownloadManager downloadManager;
    
	private DownloadManager.Request request;
    
	private Uri urlToDownload;
	
	private long latestId = -1;
	
	ProgressDialog PD; 
	public void getFile( ){
		
		
		try {
			new getFileDownLoad().execute();
//			urlToDownload = Uri.parse( mstrDownURL );
//			List<String> pathSegments = urlToDownload.getPathSegments();
//			request = new DownloadManager.Request(urlToDownload);
//			request.setTitle("다운로드 예제");
//			request.setDescription("항목 설명");
//			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, pathSegments.get(pathSegments.size()-1));
//			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
//			latestId = downloadManager.enqueue(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	private libJSON_GET GET_Lib = new libJSON_GET();
	
	public static String SaveName = "SalesForce.apk";
	
	private String InstallPath = "";
	
	private int verValue = 0, verMax = 1;
	String[] DownUrl = new String[2];
	
	private DownloadFile DLF = new DownloadFile();
	
	class getFileDownLoad extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			verValue = 0; 
			PD.setMessage( "새로운 파일을 다운로드 중입니다. 잠시 기다려주십시요." );
			PD.show();
		}
		@Override 
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					String DownURL = "";
					DownURL = GET_Lib.getFileDownLoad();
					InstallPath = Constance.FILEPATH + "/SalesForce/apk/"+ SaveName;
					
					File file = new File( InstallPath );
					
					if ( file.exists() ) {
						file.delete();
					}
//						
					try {
//						fileDownload(DownURL, Constance.FILEPATH + "/SalesForce/apk/", SaveName);
						
						DLF.Download_HttpFile(DownURL, Constance.FILEPATH + "/SalesForce/apk/", SaveName);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				} else {
					break;
				}
				
				verValue++;
			}
			return verValue;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
		protected void onPostExecute(Integer result) {
			handler.post(new Runnable() {
				public void run() {
					PD.dismiss();
					PD.cancel();
					apkInstall();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
  
	private Handler handler = new Handler();

	public void fileDownload(String DownloadURL, String path, String FileName) throws Exception
	{
		InputStream inputStream;
		FileOutputStream fos;

		inputStream = new URL(DownloadURL).openStream();
		fos = new FileOutputStream(path+FileName);
		writeFile(inputStream, fos);
		fos.close();
	}
	
	private void writeFile(InputStream is, FileOutputStream os) throws IOException
	{
		int c = 0;
		long cnt =0;
		while((c = is.read()) != -1)
		{
			os.write(c);

			cnt ++;
		}

		os.flush();
	}
	
  private void apkInstall()
  {
	  try {
		  File decFile = new File( InstallPath );
	      //Uri apkUri = Uri.fromFile(decFile);
	      Intent intent = new Intent(Intent.ACTION_VIEW);
	      intent.setDataAndType( Uri.fromFile(decFile), "application/vnd.android.package-archive");
	      mContext.startActivity(intent);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
}
