package fnc.salesforce.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.util.ByteArrayBuffer;

import fnc.salesforce.android.Constance.Constance;

import oz.api.OZRTextBoxCmd;
import oz.api.OZReportAPI;
import oz.api.OZReportCommandListener;
import oz.api.OZReportViewer;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;

public class OZViewerLocalActivity extends Activity {	
    /** Called when the activity is first created. */
    static 
    {
    	try {
        	System.loadLibrary("ozrv");			
		} catch (Throwable e) {
			Log.e("lib", e.getLocalizedMessage(), e);
		}
    } 
    
    OZReportViewer viewer = null; // 
    
    private void DeleteDir(String path) { 
	    File file = new File(path);
	    File[] childFileList = file.listFiles();
	    
	    try {
	    	for(File childFile : childFileList)
		    {
		        if(childFile.isDirectory()) {
		            DeleteDir(childFile.getAbsolutePath());     //하위 디렉토리 루프 
		        } else {
		            childFile.delete();    //하위 파일삭제
		        }
		    }
//		    file.delete();    //root 삭제 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		
		Log.d("test_1", "local library 'ozrv' loaded");
 
//		String params = "connection.openfile=/sdcard/ozviewer/ozd/smart.ozd";
		String params = "";
		
		AssetManager am = this.getResources().getAssets();
		
		String mFolder = Environment.getExternalStorageDirectory() + "/KumaTest/eForm/";
		File folder = new File( mFolder );
		
		DeleteDir(mFolder);
		
		if (!folder.exists()) {
			folder.mkdirs();
		}
        String FilePath = mFolder + "eKOLON.ozd";
        File file = new File ( FilePath );
        
		try {
			if( !file.exists() ) { 
				   
				InputStream is = am.open("ozd/eKOLON.ozd");
				
				long filesize = is.available();
	            
	            BufferedInputStream bis = new BufferedInputStream(is);
				
				ByteArrayBuffer baf = new ByteArrayBuffer( (int)filesize );
				
				int curent = 0;
				
				while(( curent = bis.read()) != -1 ){
					baf.append((byte) curent );
				}
				
				FileOutputStream fos = new FileOutputStream( file );
				
				fos.write( baf.toByteArray() );
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		
		String ozdFileName = FilePath;

		
		if (ozdFileName != null)
		{
			params = "connection.openfile="+ozdFileName;
		} 
		
		params +="\n" + "information.debug=true";
		params +="\n" + "eform.signpad_type=zoom";
		params +="\n" + "eform.signpad_zoom=1500";
		params +="\n" + "viewer.usetoolbar=true";
		  
		params +="\n" + "export.format=jpg";
		params +="\n" + "export.mode=silent";
		params +="\n" + "export.confirmsave=false";
		  
		params += "\n" + "font.fontnames=font1";
		params += "\n" + "font.font1.url=res://NanumGothic.ttf";
		params += "\n" + "font.font1.name=나눔고딕"; 
		
//		params +="\n" + "global.language=en/us";
		 
		 
		FrameLayout flayout = new FrameLayout(this);
		
		OZReportCommandListener listener = new OZReportCommandListener() {
			
			@Override
			public void OZUserActionCommand(String type, String attrs) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZProgressCommand(String step, String state, String reportname) {
				// TODO Auto-generated method stub
				Log.d("OZViewer", String.format("[Event] OZProgressCommand: %s, %s, %s", step, state, reportname));				
			}
			
			@Override
			public void OZPrintCommand(String msg, String code, String reportname,
					String printername, String printcopy, String printpages,
					String printrange, String username, String printerdrivername,
					String printpagesrange) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZPostCommand(String cmd, String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZMailCommand(String code) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZLinkCommand(String docIndex, String compName, String tag,
					String value, String mouseButton) {
				// TODO Auto-generated method stub
				
				if (compName.equals("KICA"))
				{
					Log.d("OZViewer","KICA----------------------------");
				}else
				{
					Log.d("OZViewer","docIndex="+docIndex+"  compName="+compName+" tag="+tag+" valule="+value);
				}
				
				
				
				
			}
			
			@Override
			public void OZExportCommand(String code, String path, String filename,
					String pagecount, String filenames) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZExitCommand() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZErrorCommand(String code, String errmsg, String detailmsg,
					String reportname) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZCommand(String code, String args) {
				// TODO Auto-generated method stub
				Log.d("OZViewer", String.format("[Event] OZCommand: %s, %s", code, args));
			}
			
			@Override
			public void OZBankBookPrintCommand(String data) {
				// TODO Auto-generated method stub
				
			} 

			@Override
			public String OZUserEvent(String arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println("1 :"+arg0+"\n 2 :"+arg1+"\n 3:"+arg2);
				//viewer.ScriptEx(arg0, arg1, arg2);
				//viewer.Script("save");
				if( arg0.equals("btn_zip") ){
					  viewer.Document().SetGlobal("exportN", "f");
	                    
	                    viewer.GetInformation("INPUT_TRIGGER_CLICK");
	                    
	                    viewer.ScriptEx("save", "export.format=jpg;export.mode=silent;export.confirmsave=false;export.path="+ mFilePAth + ";export.filename=OriginalImage", ";");
	                    
	                    Constance.UPLOAD_FILE_NAME_ORIGINAL = "OriginalImage.jpg";
	                    
	                    String filepath = mFilePAth + "/" + Constance.UPLOAD_FILE_NAME_ORIGINAL;                                        
	                    
	                    viewer.Document().SetGlobal("exportN", "s");
	                    
	                    viewer.GetInformation("INPUT_TRIGGER_CLICK");
	                    
	                    viewer.ScriptEx("save", "export.format=jpg;export.mode=silent;export.confirmsave=false;export.path="+ mFilePAth + ";export.filename=MaskImage", ";");
						
	                    Constance.UPLOAD_FILE_NAME_MASK = "MaskImage.jpg"; 
				}
				return "GOOD!!";
			}

			@Override
			public void OZReportChangeCommand(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean OZWillChangeIndex_Paging(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void OZPageBindCommand(String arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OZPageChangeCommand(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OZTextBoxCommand(OZRTextBoxCmd arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		
//		String res = viewer.GetInformation("test");
//		Log.d("OZViewer", res);
		
//		System.setProperty("R.layout.oz_sign_pad", String.valueOf(oz.viewer.app.R.layout.oz_sign_pad));
//		System.setProperty("R.id.SignPadRelativeLayout", String.valueOf(oz.viewer.app.R.id.SignPadRelativeLayout));
//		System.setProperty("R.id.OZSignPadInitButton", String.valueOf(oz.viewer.app.R.id.OZSignPadInitButton));
//		System.setProperty("R.id.OZSignPadConfirmButton", String.valueOf(oz.viewer.app.R.id.OZSignPadConfirmButton));
		
		
		params += "\n" + "comment.all=true";
		params += "\n" + "comment.autohide=false";

		
		viewer = OZReportAPI.createViewer(flayout, listener, params);
		
		String mJuminBirthDay = "1983-09-14";
		
		viewer.Document().SetGlobal("birth_dt", mJuminBirthDay );
		viewer.Document().SetGlobal("rbrth_dt", mJuminBirthDay );
		viewer.GetInformation("INPUT_TRIGGER_CLICK");
		
		super.onCreate(savedInstanceState);
        setContentView(flayout);
       	
    }
	
	String mFilePAth = Constance.FILEPATH + "/ozviewer";
	
	@Override
	public void onDestroy() 
	{
		Log.d("lib", "OZViewerActivity onDestroy");
		super.onDestroy();
		if(viewer != null) {
			viewer.dispose();
			viewer = null;
		}
	}
}