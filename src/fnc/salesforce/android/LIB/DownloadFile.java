package fnc.salesforce.android.LIB;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.http.AndroidHttpClient;
import android.util.Log;
public class DownloadFile {
	static Bitmap bm;

	public DownloadFile( ) {
	}
	
	static String saveName = "";

	/**
	 * Event Image 다운로드
	 * 
	 * @param urlPath Image 주소
	 * @param Filepath 저장할 경로
	 * @param saveName 저장할 이름 
	 * */
	public String Download_Image(String urlPath, String Filepath) {

		
		File dir = new File(Filepath);
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 이미지의 절대경로를 구함
		// 파일을 실제로 만듬

//		if ( !file.exists() ) {
//			
//		} else {
//			Log.e("test", " 2222 ") ;
//		}
		String filepath = "";
		try {
			
			filepath = Filepath + saveName;
			
			File file = new File( filepath );
			
			if ( !file.exists() ) {
				InputStream inputStream = ReturnStream(urlPath);
				
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				
				ByteArrayBuffer baf = new ByteArrayBuffer( 1024 );
				
				int curent = 0;
				
				while(( curent = bis.read()) != -1 ){
					baf.append((byte) curent );
				}
				
				FileOutputStream fos = new FileOutputStream( file );
				
				fos.write( baf.toByteArray() );
				fos.close();
			} else {
//				file.delete();
//				
//				FileOutputStream fos = new FileOutputStream( file );
//				
//				fos.write( baf.toByteArray() );
//				fos.close();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filepath;
	}

	public static void saveRemoteFile(InputStream is, OutputStream os) throws IOException   
    {
        int c = 0;
        while((c = is.read()) != -1)
            os.write(c);
        os.flush();   
    }
	
	// URL 부터 이미지를 다운 받아 sdCard 에 저장
	public static String Download_HttpFile(String urlPath, String Filepath, String strSaveName) { 

		File dir = new File(Filepath);
		
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String filepath = "";
		try {
 
			filepath = Filepath + strSaveName;
			
			File file = new File( filepath );

			KumaLog.LogD("test_1"," file.length() :  " + file.length() );
			
			if( file.length() == 0 ){
				file.delete();
			}
			
			if ( !file.exists() ){
				
				InputStream inputStream = ReturnStream_Http(urlPath);
				
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				
				KumaLog.LogD("test_1"," len :  " + len );
				
				ByteArrayBuffer baf = new ByteArrayBuffer( len );
				
				int curent = 0;
				
				while(( curent = bis.read()) != -1 ){
					baf.append((byte) curent );
				}
				
				FileOutputStream fos = new FileOutputStream( file );
				
				fos.write( baf.toByteArray() );
				fos.close();
			}
        } catch(Exception e){
        	e.printStackTrace();
        } 

		return filepath;
	}
	
	public static InputStream ReturnStream_Http(String url) {
		String url_str = url;
		URL m_sConnectUrl;

		String[] aray_url = url_str.split(":");

		try {
			if (aray_url.length > 2) {
				String protocol = aray_url[0].trim();

				String[] host_array = aray_url[1].split("/");
				String host = host_array[2];

				String[] post_url = aray_url[2].split("/");
				int post = Integer.parseInt(post_url[0]);
				String filepath = "";
				for (int i = 1; i < post_url.length; i++) {
					filepath += "/" + post_url[i];
				}

				m_sConnectUrl = new URL(protocol, host, post, filepath);
			} else {
				m_sConnectUrl = new URL(url_str);
			}
			URLConnection conn;

			conn = m_sConnectUrl.openConnection();
			len = conn.getContentLength();
			conn.connect();

			return conn.getInputStream();
		} catch(FileNotFoundException e){
        	Log.e("test", "FileNotFoundException : " + e);
        	return null;
        } catch (IOException e) {
			Log.e("test", "IOException : " + e);
			return null;
		} catch(Exception e){
        	Log.e("test", "Exception : " + e);
        	return null;
        }
	}
		
	public static int len = 0;

	
	public static String[][] Zipent( String folder, String filename){
		
		File file = new File( folder );

		if ( !file.exists() ) {
			file.mkdirs();
		} else {
			file.delete();
		}
		String[][] arrFilePath = null;
		
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<String> b = new ArrayList<String>();
		ArrayList<String> c = new ArrayList<String>();
		try {
			// Open the ZipInputStream
			// FileInputStream �ȿ� �ִ� test zip������
			ZipInputStream inputStream = new ZipInputStream(
					new FileInputStream(folder + filename));
			
			for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream
					.getNextEntry()) {

				String innerFileName = folder + entry.getName();

				File innerFile = new File(innerFileName);

//				if ( !innerFile.exists() ) {
//					innerFile.delete();
//				}
//				if ( entry.isDirectory() ) {
//					innerFile.mkdirs();
//				}
//				String[] str = entry.getName().split("\\.");
				String Savename = "";
				
				Savename = entry.getName();
				
				if( !entry.getName().contains( "__MACOSX/") ){
					try {
//						Integer.parseInt( str[0] );
						String filePath = folder + Savename;
						
						File imageFile = new File(filePath);
						
						if ( !imageFile.exists() ) {
														
							FileOutputStream outputStream = new FileOutputStream( filePath );

							// final int BUFFER = 2048;
							final int BUFFER = 2048;

							// Buffer the output to the file
							BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
									outputStream, BUFFER);

							// Write the contents
							int count = 1;
							byte[] data = new byte[BUFFER];
							while ((count = inputStream.read(data, 0, BUFFER)) != -1) {
								bufferedOutputStream.write(data, 0, count);
							}
							// Flush and close the buffers
							bufferedOutputStream.flush();
							bufferedOutputStream.close();
							
							a.add( filePath );
							
							Bitmap oldBm = BitmapFactory.decodeFile(filePath);
							
							Bitmap NewBm = ReturnBitmap_Rotate90(oldBm, 155, 143);
							
							File dir = new File( folder + File.separator +"thumb");
							
							if (!dir.exists()) {
								dir.mkdirs();
							}
							String strPath = folder +"thumb/" + Savename;
							
							File fileCacheItem = new File( strPath );
							c.add( Savename );
							b.add( strPath );
														
							OutputStream out = null;
					    	
					    	try {
					    		fileCacheItem.createNewFile();
					    		out = new FileOutputStream(fileCacheItem);
					    		            
					    		NewBm.compress(CompressFormat.JPEG, 100, out);
					    	} catch (Exception e)	{
					    		e.printStackTrace();
					    	}

					    	finally {
					    		try {
					    			out.close();
					    		}
					    		catch (IOException e) {
					    			e.printStackTrace();
					    		}
					    	}
							// Close the current entry
							inputStream.closeEntry();
						} else {
							a.add( filePath );
							String strPath = folder +"thumb/" + Savename;
							c.add( Savename );
							b.add( strPath );
						}
					} catch (Exception e) {
						try {
							innerFile.delete();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						e.printStackTrace();
					}
					
				}
				
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		arrFilePath = new String [3][a.size()];
		for(int j = 0; j < 2; j++ ){
			for( int i = 0; i < a.size(); i ++){				
				arrFilePath[j][i] = "";
			}
		}
		
		for( int i = 0; i < a.size(); i ++){				
			arrFilePath[0][i] = a.get( i );
			arrFilePath[1][i] = b.get( i );
			arrFilePath[2][i] = c.get( i );
		}
		
		
		return arrFilePath;
	}

	private int COMPRESSION_LEVEL = 5;
	
	private void zip() {
		
		try {
			String[] source = new String[] {
					".\\resource\\device-2012-02-07-164954.png"
					,".\\resource\\device-2012-02-07-165042.png"
			};
			
			byte[] buffer = new byte[1024];
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("\\test.zip"));
			
			ZipOutputStream zipOs = new ZipOutputStream(bos);
			zipOs.setLevel(COMPRESSION_LEVEL);

			for (int i = 0 ; i < source.length ; i++) {
				FileInputStream in = new FileInputStream(source[i]);
				BufferedInputStream bis = new BufferedInputStream(in);
				
				zipOs.putNextEntry(new ZipEntry(source[i]));
				int len;
				
				while ((len = bis.read(buffer)) > 0) {
					zipOs.write(buffer, 0, len);
				}
				
				zipOs.closeEntry();
				bis.close();
			}
			
			zipOs.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();	  
		}
	}
	
	private static Bitmap ReturnBitmap_Rotate90(Bitmap bitmap, int width, int height) {
		Bitmap bitmapOrg = bitmap;
	
		int Orgwidth = bitmapOrg.getWidth();
		int Orgheight = bitmapOrg.getHeight();

			Log.w("test", " Orgwidth : " + Orgwidth + " Orgheight : " + Orgheight);
		
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) width) / Orgwidth;
		float scaleHeight = ((float) height) / Orgheight;
	
		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
	
		// recreate the new Bitmap
		Bitmap resizeBm = Bitmap.createBitmap(bitmapOrg, 0, 0, Orgwidth,
				Orgheight, matrix, true);
		return resizeBm;
	}
	/**
	 * 스트림 반환 시킴
	 * 
	 * @param url Image 주소
	 * 
	 * @return InputStream
	 * */
	private static InputStream ReturnStream(String url) {
		String url_str = url;
		URL m_sConnectUrl;

		String[] aray_url = url_str.split(":");

		try {
			if (aray_url.length > 2) {
				String protocol = aray_url[0].trim();

				String[] host_array = aray_url[1].split("/");
				String host = host_array[2];

				String[] post_url = aray_url[2].split("/");
				int post = Integer.parseInt(post_url[0]);
				String filepath = "";
				for (int i = 1; i < post_url.length; i++) {
					filepath += "/" + post_url[i];
				}
				m_sConnectUrl = new URL(protocol, host, post, filepath);
			} else {
				m_sConnectUrl = new URL(url_str);
			}
			URLConnection conn;

			conn = m_sConnectUrl.openConnection();
			conn.setReadTimeout(5000);
			
			
			len = conn.getContentLength();
			conn.connect();
			
			try {
				String[] sub = conn.getHeaderField("content-disposition").toString().split("=");
//				Log.w("test", " dispositio " + sub[1]  ) ;
				saveName = sub[1].toString();
//				Log.w("test", " attachment " + conn.getHeaderField("attachment").toString() ) ;
//				Log.w("test", " filename " + conn.getHeaderField("filename").toString() ) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return conn.getInputStream();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("test", " dispositio " + saveName  ) ;
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
