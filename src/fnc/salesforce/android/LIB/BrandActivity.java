package fnc.salesforce.android.LIB;

import fnc.salesforce.android.BrandItemListAdabter;
import fnc.salesforce.android.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class BrandActivity extends Dialog  implements OnTouchListener{

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
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

	
	private ListView BrandList;
	
	private RelativeLayout layoutBrandList;
	
	public BrandActivity (Activity context, int theme) {
		super(context, theme);

		requestWindowFeature( Window.FEATURE_NO_TITLE );
		
		setContentView( R.layout.brand_activity );
		
		layoutBrandList			= ( RelativeLayout ) findViewById( R.id.layoutBrandList );
		
		BrandList				= ( ListView ) findViewById( R.id.BrandList );
		
		BrandList.setAdapter( new BrandItemListAdabter( context ) );
		
		layoutBrandList.setOnTouchListener( this );
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
	public boolean onTouch(View v, MotionEvent event) {
		if( v == layoutBrandList ){
			dismiss();
			cancel();
		}
		return true;
	}

}
