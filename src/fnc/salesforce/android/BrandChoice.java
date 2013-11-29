package fnc.salesforce.android;

import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.KumaLog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class BrandChoice extends Activity implements OnClickListener{

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private RelativeLayout layoutBrandImg;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.brandchoice );
		
		mContext = this;
		
		layoutBrandImg = ( RelativeLayout ) findViewById( R.id.layoutBrandImg );
		setScrollViewTAbBar();
	}

	ImageButton[] tabImgBtn;
	ImageView[] tabImgLogo;
	private int AllCategoryContents = 0, One_lineCount = 3;;
	
	private void setScrollViewTAbBar(){
		
		AllCategoryContents = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.size();
		
//		AllCategoryContents = Constance.BrandCodeImage_Large.length;
		Constance.SHOPNAME = "KOLON SPORT";
		
		if( AllCategoryContents == 1 ){
			String strBrandCD 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( 0 ).brandCd;
			Constance.SHOPCD 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( 0 ).shopCd;
//			Constance.SHOPNAME 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( 0 ).brandNm;
			
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("brandCd", strBrandCD );
			startActivity(intent);
			finish();
		}
		
		tabImgBtn	= new ImageButton[ AllCategoryContents ];
		tabImgLogo	= new ImageView[ AllCategoryContents ];
				
//		TabLayout = new RelativeLayout( mContext ); 
		
		for( int i = 0; i < AllCategoryContents; i++ ){
			tabImgBtn[i] = new ImageButton( mContext );
			tabImgLogo[i]  = new ImageView( mContext );
			
			RelativeLayout.LayoutParams imgContentslayout = new RelativeLayout.LayoutParams(
					228, 101);

			if( ( i % AllCategoryContents ) / One_lineCount == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgContentslayout.setMargins( ( 228 * ( i % One_lineCount ) ) + 10, 0, 0, 0);	
				} else {
					imgContentslayout.setMargins( ( 228 * i ) + 10, 0, 0, 0);
				}
			} else {
				imgContentslayout.setMargins( ( 228 * ( i % One_lineCount ) )  + 10, ( 111 * ( ( i % AllCategoryContents ) / One_lineCount ) ), 0, 0);
			}
			
//			imgContentslayout.setMargins( b, a, 0, 0);
			
			tabImgBtn[i].setLayoutParams(imgContentslayout);
			tabImgBtn[i].setBackgroundResource( R.drawable.at_login_brandbox );
			tabImgBtn[i].setOnClickListener( this );

			RelativeLayout.LayoutParams imgLogolayout = new RelativeLayout.LayoutParams(
					220, 42);

			if( ( i % AllCategoryContents ) / One_lineCount == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgLogolayout.setMargins( ( 232 * ( i % One_lineCount ) ) + 10, 30, 0, 0);	
				} else {
					imgLogolayout.setMargins( ( 232 * i ) + 10, 30, 0, 0);
				}
			} else {
				imgLogolayout.setMargins( ( 232 * ( i % One_lineCount ) )  + 10, ( 111 * ( ( i % AllCategoryContents ) / One_lineCount ) ) + 30, 0, 0);
			}
			
			tabImgLogo[i].setLayoutParams(imgLogolayout);
			try {
				tabImgLogo[i].setBackgroundResource( Constance.getBrandImg_Small( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( i ).brandCd) );
//				tabImgLogo[i].setBackgroundResource( Constance.BrandCodeImage_Small[i] );
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			tabImgLogo[i].setScaleType( ScaleType.FIT_CENTER );
			
			layoutBrandImg.addView( tabImgBtn[i] );
			layoutBrandImg.addView( tabImgLogo[i] );
			
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	public void onClick(View v) {
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < tabImgBtn.length; i++ ){
					if( v == tabImgBtn[i] ){
						String strBrandCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( i ).brandCd;
						Constance.SHOPCD =CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( i ).shopCd;
						Constance.SHOPNAME 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.get( i ).shopNm;
						
						Intent intent = new Intent( mContext, Main_Page.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("brandCd", strBrandCD );
						startActivity(intent);
						finish();
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
