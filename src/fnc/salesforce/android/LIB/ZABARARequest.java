package fnc.salesforce.android.LIB;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class ZABARARequest {
	
	public static XmlPullParser Return_XML(String url){
		try {
	       
			XmlPullParserFactory factory = XmlPullParserFactory
					.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader (url));
			
			Log.e("test_1","aml  : " + xpp.toString());
			return xpp;
		} catch (Exception e) {
			Log.e("EvnetInformaition", "error : " + e);
			return null;
		}
	}
}
