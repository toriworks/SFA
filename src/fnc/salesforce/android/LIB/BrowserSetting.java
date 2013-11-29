package fnc.salesforce.android.LIB;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;

public class BrowserSetting {

	public BrowserSetting(){
	}
//	String aaa = "Mozilla/5.0 (Linux; U; Android 2.2; ko-kr; Kolon-p1001 Build/FROYO) AppleWebKit/533.1 (KHTML, like Gecko)  Version/4.0 Safari/533.1";
	
//	String USERAGENT =	"Mozilla/5.0 (X11;Linux x86_64) AppleWebKit/534.24 (KHTML, like Gecko) Chrome/11.0.696.34 Safari/534.24";
	String USERAGENT =	"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; aff-kingsoft-ciba; Tablet PC 2.0)";
	
	private static final String DESKTOP_USERAGENT = "Mozilla/5.0 (X11; " +
	        "Linux x86_64) AppleWebKit/534.24 (KHTML, like Gecko) " +
	        "Chrome/11.0.696.34 Safari/534.24";

    private static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; " +
        "CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 " +
        "(KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";

    private static final String IPAD_USERAGENT = "Mozilla/5.0 (iPad; U; " +
        "CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 " +
        "(KHTML, like Gecko) Version/4.0.4 Mobile/7B367 Safari/531.21.10";

    private static final String FROYO_USERAGENT = "Mozilla/5.0 (Linux; U; " +
        "Android 2.2; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 " +
        "(KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

    private static final String HONEYCOMB_USERAGENT = "Mozilla/5.0 (Linux; U; " +
        "Android 3.1; en-us; Xoom Build/HMJ25) AppleWebKit/534.13 " +
        "(KHTML, like Gecko) Version/4.0 Safari/534.13";
    
	public void Setting(WebView web ){

//		CookieManager cookieManager = CookieManager.getInstance(); 
//		cookieManager.removeAllCookie();
		
		web.clearCache(true);
		web.clearHistory();
		web.getSettings().setJavaScriptEnabled( true );
		
//		web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		web.getSettings().setPluginState(PluginState.ON);
		web.getSettings().setPluginsEnabled(true);
		web.getSettings().setJavaScriptCanOpenWindowsAutomatically( true );
		web.getSettings().setSupportMultipleWindows( true );
		web.getSettings().setDomStorageEnabled( true );
		web.setEnabled( true );
		web.getSettings().setLoadWithOverviewMode( true );	
		web.setHorizontalScrollBarEnabled( false );
		web.setHorizontalScrollbarOverlay( false );
		web.setVerticalScrollBarEnabled( false );
		web.setVerticalScrollbarOverlay( false );
		web.setScrollbarFadingEnabled( false ); 
		
		web.clearFormData();
		web.getSettings().setSaveFormData( false );
		web.getSettings().setSavePassword( false );
		
//		web.getSettings().setUserAgentString( USERAGENT );
		
//		if( str.equals("Char")){
//			web.getSettings().setUserAgent(0);
//			web.getSettings().setSaveFormData( true );
//			web.getSettings().setSavePassword( true );
//		} else {
//			web.getSettings().setUserAgentString( USERAGENT );	
////			web.getSettings().setUserAgent(0);
//			web.getSettings().setSaveFormData( true );
//			web.getSettings().setSavePassword( true );
//		}
			
//		web.getSettings().setUserAgentString( "KOLON" );
//		web.getSettings().setUserAgent( 0 );
//		web.setInitialScale(100);
			
		web.getSettings().setSupportZoom( false );
		web.getSettings().setBuiltInZoomControls( false );
		
		
		web.getSettings().setUseWideViewPort(true);
//		web.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
	}
}
