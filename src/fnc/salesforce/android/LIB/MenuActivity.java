package fnc.salesforce.android.LIB;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.AD_Gallery_10003;
import fnc.salesforce.android.Activity.Catalog__10002;
import fnc.salesforce.android.Activity.EDUCATION_Main;
import fnc.salesforce.android.Activity.Look_10001;
import fnc.salesforce.android.Activity.MemberShip_Main;
import fnc.salesforce.android.Activity.Product_Search;
import fnc.salesforce.android.Activity.Promotion_10004;
import fnc.salesforce.android.Activity.RepairInformaiton_Main;
import fnc.salesforce.android.Activity.ShopInformation;
import fnc.salesforce.android.Activity.ShopStopSystem;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.MenuIconConstance;
import fnc.salesforce.android.LIB.ProductDialog.setProDuctInfor;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuActivity  extends Dialog implements OnClickListener{

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		setScrollViewTAbBar();
	}
	
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
	@Override
	public void onBackPressed() {
		dismiss();
		cancel();
	}

	private RelativeLayout layout_Menu_Area, layoutMenuTouch;
	
	public MenuActivity (Activity context, int theme) {
		super(context, theme);
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.menu_page );
		
		mContext = context;
		layout_Menu_Area			= (RelativeLayout) findViewById(R.id.layout_Menu_Area);
		layoutMenuTouch				= (RelativeLayout) findViewById(R.id.layoutMenuTouch);
		
		layoutMenuTouch.setOnClickListener( this );
		
		
	}	
	private Activity mContext;
	
	private ImageButton[] tabImgBtn;
	private ImageView[] MenuIcon;
	private int MaxTabbCount = 0;
	// 메인 좌측 메뉴 Param
		private static class TabBackImageParam {
			static int width = 146;
			static int height = 100;
			static int margin_Top = 103;
		};
		
		private static class TabTextViewParam {
			static int width = 146;
			static int height = 35;
			static int margin_Top = 60;
			static int TextSize = 18;
		};
		
		private static class TabMenuIconParam {
			static int width = 44;
			static int height = 44;
			static int margin_Top = 10;
			static int margin_Left = 51;
		};
		
		private void setScrollViewTAbBar(){
			
			MaxTabbCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size() + Constance.MenuGojung.length;
			
			
			if( tabImgBtn != null )
				tabImgBtn = null;
			
			if( MenuIcon != null )
				MenuIcon = null;
			
			tabImgBtn	= new ImageButton[ MaxTabbCount ];
			
			MenuIcon	= new ImageView[ MaxTabbCount ];
			
			TextView[] tabtxtView	= new TextView[ MaxTabbCount ];
			
			for( int i = 0; i < MaxTabbCount; i++ ){
				tabImgBtn[i] 	= new ImageButton( mContext );
				MenuIcon[i] 	= new ImageView( mContext );
				tabtxtView[i] 	= new TextView( mContext );
				
				RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
						TabBackImageParam.width, TabBackImageParam.height);
				imglinelayout.setMargins( 0, ( TabBackImageParam.margin_Top ) * i, 0, 0);

				tabImgBtn[i].setLayoutParams(imglinelayout);
				
				int bgPosition = ( i % Constance.MainMenuBG.length );
				
				tabImgBtn[i].setBackgroundResource( Constance.MainMenuBG[ bgPosition ] );
				
				RelativeLayout.LayoutParams txtMenulayout = new RelativeLayout.LayoutParams(
						TabTextViewParam.width, TabTextViewParam.height);
				txtMenulayout.setMargins( 0, ( ( TabBackImageParam.margin_Top ) * i ) + TabTextViewParam.margin_Top, 0, 0);
				
				tabtxtView[i].setLayoutParams(txtMenulayout);
				tabtxtView[i].setGravity( Gravity.CENTER );
				tabtxtView[i].setShadowLayer((float) 0.5, 1, 1, Color.parseColor("#000000"));
				tabtxtView[i].setTypeface( Typeface.DEFAULT_BOLD, Typeface.BOLD );
				tabtxtView[i].setTextColor( Color.parseColor( "#ffffff" ) );

                // 왼쪽 메뉴 그리기
                int sizeOfMainMenu = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size();
                if( i < 3) {
                    // 메뉴명
                    tabtxtView[i].setText(Constance.MenuGojung[i]);
                    // 아이콘
                    MenuIcon[i].setBackgroundResource( setMenuIcon( Constance.MenuGojung_IMG[ i ]) );
                } else if(i < sizeOfMainMenu + 3) {
                    // 메뉴명
                    tabtxtView[i].setText(CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get(i-3).ctgryNm);
                    // 아이콘
                    MenuIcon[i].setBackgroundResource( setMenuIcon( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( i - 3 ).iconTyCd ) );
                } else  {
                    // 메뉴명
                    tabtxtView[i].setText(Constance.MenuGojung[i - sizeOfMainMenu]);
                    // 아이콘
                    MenuIcon[i].setBackgroundResource( setMenuIcon( Constance.MenuGojung_IMG[ i - sizeOfMainMenu ]) );
                }
				
				if( i == ( MaxTabbCount - 1 ) ){
					if( Constance.HEAD_LOGIN  ){
						tabImgBtn[i].setVisibility( View.VISIBLE );
						MenuIcon[i].setVisibility( View.VISIBLE );
						tabtxtView[i].setVisibility( View.VISIBLE );
					} else {
						tabImgBtn[i].setVisibility( View.GONE );
						MenuIcon[i].setVisibility( View.GONE );
						tabtxtView[i].setVisibility( View.GONE );
					}
				}
				
				tabtxtView[i].setTextSize( TabTextViewParam.TextSize );
				
				tabImgBtn[i].setOnClickListener( this );
				
				RelativeLayout.LayoutParams imgIconlayout = new RelativeLayout.LayoutParams(
						TabMenuIconParam.width, TabMenuIconParam.height);
				imgIconlayout.setMargins( TabMenuIconParam.margin_Left, ( ( TabBackImageParam.margin_Top ) * i ) + TabMenuIconParam.margin_Top, 0, 0);
				
				MenuIcon[i].setLayoutParams(imgIconlayout);

				layout_Menu_Area.addView( tabImgBtn[i] );
				layout_Menu_Area.addView( MenuIcon[i] );
				layout_Menu_Area.addView( tabtxtView[i] );
			}
		}

	private int setMenuIcon( String mImgName ){
		
		for ( int i = 0; i < MenuIconConstance.MenuName.length; i++ ){
			if( mImgName.toLowerCase().toString().trim().equals( MenuIconConstance.MenuName[i]) ){
				return MenuIconConstance.MenuID[i];
			}
		}
		return 0;
		
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
				for( int i = 0; i < MaxTabbCount; i++){
					if( v == tabImgBtn[i] ){
						dismiss();
						cancel();
                        if( i < 3){
                            switch(i) {
                                case 0:
                                    Intent intent = new Intent( mContext, MemberShip_Main.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                    break;
                                case 1:
                                    Intent intent2 = new Intent( mContext, ShopStopSystem.class);
                                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent2);
                                    mContext.finish();
                                    break;
                                case 2:
                                    Intent intent3 = new Intent( mContext, Product_Search.class);
                                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent3);
                                    mContext.finish();
                                    break;
                            }
                        } else if ( 1 < i && i < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size() + 3 ) {
                            int position = i - 3;
                            String scrinTyCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).scrinTyCd;
                            String ctgryTyCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryTyCd;

                            if( scrinTyCd.equals("010001") ){
                                if( ctgryTyCd.equals("007003") ){
                                    //교육관리
                                    Intent intent = new Intent( mContext, EDUCATION_Main.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("backState", "main" );
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                } else if( ctgryTyCd.equals("007002") ){
                                    // LookBook ( 콘텐츠 => 상세 )
                                    Intent intent = new Intent( mContext, Look_10001.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
                                    intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
                                    intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);
                                    intent.putExtra("Detail_State", "Main");
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                }
                            } else if( scrinTyCd.equals("010002") ){
                                // 카탈로그 ( 콘텐츠 => 상세 )
                                Intent intent = new Intent( mContext, Catalog__10002.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
                                intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
                                intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);
                                mContext.startActivity(intent);
                                mContext.finish();
                            } else if( scrinTyCd.equals("010003") ){
                                // AD Gallery ( 콘텐츠 + 상세 + 제품  )
                                Intent intent = new Intent( mContext, AD_Gallery_10003.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
                                intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
                                intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);

                                mContext.startActivity(intent);
                                mContext.finish();
                            } else if( scrinTyCd.equals("010004") ){
                                Intent intent = new Intent( mContext, Promotion_10004.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
                                intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
                                intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);

                                mContext.startActivity(intent);
                                mContext.finish();
                            }
                        } else {
                            int sizeOfMainMenu = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size();
                            if( ( i - sizeOfMainMenu)  == 3){
                                Intent intent = new Intent( mContext, RepairInformaiton_Main.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);
                                mContext.finish();
                            } else if( ( i - sizeOfMainMenu )  == 4){
                                Intent intent = new Intent( mContext, ShopInformation.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(intent);
                                mContext.finish();
                            }
                            // TODO : 매장 CHECKLIST 이후에 붙일 것

                        }
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( v == layoutMenuTouch ){
			dismiss();
			cancel();
		}
		
	}

}
