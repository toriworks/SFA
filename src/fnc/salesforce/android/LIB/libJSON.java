package fnc.salesforce.android.LIB;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.OZVIEWER_CONSTANCE;
import fnc.salesforce.android.Constance.URLConstance;




public class libJSON {

	private static String[] BufferString;
	
	private static String SITE_URL = "";
	
	public libJSON(){

	}
	
	private static String USER_ID = "";
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getDEVICE_INPUT( String[] parameter ) {
		RequestTimeOut = 30000;
		RequestMethod = "POST";
		USER_ID = parameter[2];
		
		String[] arrReturnParameter = new String[2];
		
		try { 
			SITE_URL = URLConstance.HEAD_OF_DEVICE_INPUT +"accessToken=" + parameter[0]+ "&timestamp=" + parameter[1];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL, "DEVICE"); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" );

			if ( arrReturnParameter[0].equals("200") ) {

				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getDEVICE_INPUT_SHOP( String[] parameter ) {
		RequestTimeOut = 30000;
		RequestMethod = "POST";
		USER_ID = parameter[2];
		
		String[] arrReturnParameter = new String[2];
		
		try { 
			SITE_URL = URLConstance.SHOP_DEVICE_INPUT +"accessToken=" + parameter[0]+ "&timestamp=" + parameter[1];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL, "DEVICE"); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" );
			
			if ( arrReturnParameter[0].equals("200") ) {

				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	/** CUTOMER_INSERT 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getCUTOMER_INSERT( String[] parameter ) {	
		RequestTimeOut = 30000;
		RequestMethod = "POST";
		USER_ID = parameter[2];
		
		String[] arrReturnParameter = new String[2];
		
		try { 
			SITE_URL = URLConstance.CUTOMER_INSERT +"accessToken=" + parameter[0]+ "&timestamp=" + parameter[1];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL, "XML"); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" );
			
			if ( arrReturnParameter[0].equals("200") ) {

				JSONObject nameArray = jsonChannels.getJSONObject("result");
				arrReturnParameter[0] = nameArray.getString( "errorCd" );
				arrReturnParameter[1] = nameArray.getString( "errorMsg" );
				
				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	
	
	/** FA_DEVICE_INPUT 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getFA_DEVICE_INPUT( String[] parameter ) {
		RequestTimeOut = 30000;
		RequestMethod = "PUT";
		BufferString = new String[parameter.length];
		
		for( int i = 0; i < parameter.length; i++ ){
			BufferString[i] = "";
			BufferString[i] = parameter[i];
		}
		
		String[] arrReturnParameter = new String[2];
		
		try { 
			SITE_URL = URLConstance.FA_DEVICE_INPUT +"accessToken=" + BufferString[0]+ "&timestamp=" + BufferString[1];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL, "FA"); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" );
			
			if ( arrReturnParameter[0].equals("200") ) {

				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	
	/** MIAN_HOSPITAL_MENU */
	public boolean geDeviceInformation( String section ) {
		RequestTimeOut = 30000;
		RequestMethod = "POST";
		String state = "";
		try { 
			SITE_URL = URLConstance.AUTH_INFORMATION;
			
			JSONObject jsonChannels = getHttpJson(SITE_URL, section); 

			state = jsonChannels.getString("RESULT").toString();
			if ( state.equals("99") ) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** FA_DEVICE_INPUT 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getEFORM_UPLOAD_ORIGINAL( String[] parameter ) {
		RequestTimeOut = 30000;
		BufferString = new String[parameter.length];

		for( int i = 0; i < parameter.length; i++ ){
			BufferString[i] = "";
			BufferString[i] = parameter[i];
		}
		
		String[] arrReturnParameter = new String[2];
		
		Constance.UPLOAD_FILE_NAME = Constance.UPLOAD_FILE_NAME_ORIGINAL;
		try { 
			SITE_URL = URLConstance.CUTOMER_FILE_UPLOAD_ORIGINAL +"accessToken=" + parameter[0]+ "&timestamp=" + parameter[1];
//			+ "&csNm=" + "박재성"
//			+ "&juminBirthday=" +"19830914" + "&mobileTelNo=" + "01033695508" + "&certificationTp=" + "1";
						
			JSONObject jsonChannels = getHttpJson_Upload(SITE_URL, parameter[2]); 

			JSONObject color = jsonChannels.getJSONObject("status");
			
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" );
				            
			if ( arrReturnParameter[0].equals("200") ) {
				JSONObject nameArray = jsonChannels.getJSONObject("result");	
				
				OZVIEWER_CONSTANCE.mImageOriginalPath			= nameArray.getString( "imageOriginalPath" );
				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			e.printStackTrace();
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			
			return arrReturnParameter;
		}
	}
	
	/** FA_DEVICE_INPUT 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getEFORM_UPLOAD_MASK( String[] parameter ) {
		
		RequestTimeOut = 30000;
		
		BufferString = new String[parameter.length];
		String state = "";
		for( int i = 0; i < parameter.length; i++ ){
			BufferString[i] = "";
			BufferString[i] = parameter[i];
		}

		String[] arrReturnParameter = new String[2];
		
		Constance.UPLOAD_FILE_NAME = Constance.UPLOAD_FILE_NAME_MASK;
		try { 
			SITE_URL = URLConstance.CUTOMER_FILE_UPLOAD_MASK +"accessToken=" + parameter[0]+ "&timestamp=" + parameter[1];
//			+ "&csNm=" + "박재성"
//			+ "&juminBirthday=" +"19830914" + "&mobileTelNo=" + "01033695508" + "&certificationTp=" + "1";
			
			
			JSONObject jsonChannels = getHttpJson_Upload(SITE_URL, parameter[2]); 

			JSONObject color = jsonChannels.getJSONObject("status");
			
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" );
	            
			if ( arrReturnParameter[0].equals("200") ) {
				JSONObject nameArray = jsonChannels.getJSONObject("result");	
				
				OZVIEWER_CONSTANCE.mImageRedactPath		= nameArray.getString( "imageRedactPath" );
				
				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			e.printStackTrace();
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			
			return arrReturnParameter;
		}
	}
	
	public static JSONObject getHttpJson_Upload(String url, String FilePath) {
		JSONObject json = null;
		try {
			String result = uploadFile(FilePath, url);
			if( result != null ){
				json = new JSONObject(result);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
		
	public static String uploadFile(String sourceFileUri, String mUrl) {
		String result = "";
		InputStream tmp = null;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
        
        Log.i("test_1", "HTTP mUrl  : " + mUrl );
        
        try {
        	// open a URL connection to the Servlet
        	
        	FileInputStream fileInputStream = new FileInputStream( sourceFile );
        	URL url = new URL( mUrl );
           
	          // Open a HTTP  connection to  the URL
	          conn = (HttpURLConnection) url.openConnection(); 
	          conn.setDoInput(true); // Allow Inputs
	          conn.setDoOutput(true); // Allow Outputs
	          conn.setUseCaches(false); // Don't use a Cached Copy
	          conn.setRequestMethod("POST");
	          conn.setRequestProperty("Connection", "Keep-Alive");
	          conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	          conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	          conn.setRequestProperty("uploaded_file", Constance.UPLOAD_FILE_NAME); 
	               			
	          OutputStream outConn = conn.getOutputStream();
				
	          dos = new DataOutputStream( outConn );
	          dos.writeBytes(twoHyphens + boundary + lineEnd);
	
	          dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	                                    + Constance.UPLOAD_FILE_NAME + "\""+ lineEnd);
	          	          
	          dos.writeBytes(lineEnd);
	 
	          // create a buffer of  maximum size
	          bytesAvailable = fileInputStream.available(); 
	 
	          bufferSize = Math.min(bytesAvailable, maxBufferSize);
	          buffer = new byte[bufferSize];
//	 
	          // read file and write it into form...
	          
	          bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	          
	          Log.i("test_1", " bytesRead : " + bytesRead);
	          
	          
	          while (bytesRead > 0) {
	               
	            dos.write(buffer, 0, bufferSize);
	            bytesAvailable = fileInputStream.available();
	            bufferSize = Math.min(bytesAvailable, maxBufferSize);
	            bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	             
	           }
	 
	          // send multipart form data necesssary after file data...
	          dos.writeBytes(lineEnd);
	          dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	 
	          // Responses from the server (code and message)

	          String serverResponseMessage = conn.getResponseMessage();
	            
	          Log.i("test", "HTTP Response is : " + serverResponseMessage );
	          
//	          fileInputStream.close();
	          dos.flush();
	          dos.close();
	          
	          tmp = conn.getInputStream();
	     } catch (MalformedURLException ex) {
	         ex.printStackTrace();
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
        
        try {
			result = convertStreamToString(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
        	conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		KumaLog.LogW("result : " + result);
		
	     return result; 
       }
	
//	PCM다운로드 : /RequestPCM.aspx?PCM=[PCM 파일이름]
	
	private static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		if( is != null ){
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	public static JSONObject getHttpJson(String url, String section) {
		JSONObject json = null;
		try {
			String result = "";
			
			if( section.equals("XML")){
				result = libRequestXML( url );
			} else {
				result = libRequest(url, section);
			}
			
			if( result != null ){
				json = new JSONObject(result);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getHttp(String url) {
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();

		// TimeOut ?占쎌젙
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
		HttpConnectionParams.setSoTimeout(params, 15000);

		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				instream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String RequestMethod = "POST";
	private static int RequestTimeOut = 30000;
	public static String libRequest(String url, String section) {
		InputStream tmp = null;
		String result = "";
		URL m_sConnectUrl = null;
		HttpURLConnection http = null;
		
		try {
			
			m_sConnectUrl = new URL(url);
			http = (HttpURLConnection) m_sConnectUrl.openConnection();

			http.setDefaultUseCaches(false);
			// OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
			http.setDoOutput(true);
//			08-19 11:52:27.845: W/System.err(13166): java.net.ProtocolException: method does not support a request body: GET

			// InputStream으로 서버로 부터 응답을 받겠다는 옵션.
			http.setDoInput(true);
			http.setReadTimeout(RequestTimeOut);
			http.setConnectTimeout(RequestTimeOut);

			http.setRequestMethod( RequestMethod );
//			http.setRequestProperty("connect-type",
//					"application/x-www-urlencoded");
			// 서버 Response Data를 xml 형식의 타입으로 요청.
			http.setRequestProperty("Accept", "application/xml");
			 
			// 서버 Response Data를 JSON 형식의 타입으로 요청.
			http.setRequestProperty("Accept", "application/json");
			 
			// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 text/html로 서버에 전달.)
			http.setRequestProperty("Content-Type", "text/html");
			 
			// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
			http.setRequestProperty("Content-Type", "application/xml");
			 
			// 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.)
			http.setRequestProperty("Content-Type", "application/json");

			// --------------------------
			OutputStream          os   = null;
			
			StringBuffer buffer = new StringBuffer();
			if( section.equals("DEVICE") ){
				buffer.append("{");
				buffer.append("\"userId\"").append(":\"").append( USER_ID );
				buffer.append("\"}");
			} else if( section.equals("FA") ){
				buffer.append("{");
				buffer.append("\"userNm\"").append(":\"").append( BufferString[2] + "\",");
				buffer.append("\"telno1\"").append(":\"").append( BufferString[3] + "\",");
				buffer.append("\"telno2\"").append(":\"").append( BufferString[4] + "\",");
				buffer.append("\"telno3\"").append(":\"").append( BufferString[5] );
				buffer.append("\"}");
			}
			KumaLog.LogD("url : " + url);
			KumaLog.LogD("buffer : " + buffer.toString()); 
				
			os = http.getOutputStream();
			os.write(buffer.toString().getBytes());
			os.flush();

			
			tmp = http.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			result = convertStreamToString(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String mMessage = http.getResponseMessage();
			
			if( mMessage.trim().equals("OK")){
				URLConstance.SOCKETSTATE = true;
			} else {
				URLConstance.SOCKETSTATE = false;
			}
			
			KumaLog.LogW("mMessage : " + mMessage);
			
		} catch (Exception e) {
			URLConstance.SOCKETSTATE = false;
			e.printStackTrace();
		}
		try {
			if( tmp != null ){
				tmp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			http.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		KumaLog.LogW("result : " + result);

		return result;
	}
	
	public static String libRequestXML(String url) {
		InputStream tmp = null;
		String result = "";
		URL m_sConnectUrl = null;
		HttpURLConnection http = null;
		
		try {
			
			m_sConnectUrl = new URL(url);
			http = (HttpURLConnection) m_sConnectUrl.openConnection();

			http.setDefaultUseCaches(false);
			// OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
			http.setDoOutput(true);
//			08-19 11:52:27.845: W/System.err(13166): java.net.ProtocolException: method does not support a request body: GET

			// InputStream으로 서버로 부터 응답을 받겠다는 옵션.
			http.setDoInput(true);
			http.setReadTimeout(RequestTimeOut);
			http.setConnectTimeout(RequestTimeOut);

			http.setRequestMethod( RequestMethod );
//			http.setRequestProperty("connect-type",
//					"application/x-www-urlencoded");
			 
			// 서버 Response Data를 JSON 형식의 타입으로 요청.
			http.setRequestProperty("Accept", "application/json");
			 
			// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
			http.setRequestProperty("Content-Type", "application/xml");

			// --------------------------
			OutputStream          os   = null;
			
			StringBuffer buffer = new StringBuffer();
			
			buffer = setXML_StringBurffer.setXML();
			
			KumaLog.LogD("url : " + url);
			KumaLog.LogD("buffer : " + buffer.toString()); 
				
			os = http.getOutputStream();
			os.write(buffer.toString().getBytes());
			os.flush();

			
			tmp = http.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			result = convertStreamToString(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String mMessage = http.getResponseMessage();
			
			if( mMessage.trim().equals("OK")){
				URLConstance.SOCKETSTATE = true;
			} else {
				URLConstance.SOCKETSTATE = false;
			}
			
			KumaLog.LogW("mMessage : " + mMessage);
			
		} catch (Exception e) {
			URLConstance.SOCKETSTATE = false;
			e.printStackTrace();
		}
		try {
			if( tmp != null ){
				tmp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			http.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		KumaLog.LogW("result : " + result);

		return result;
	}
}
