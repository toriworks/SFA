package fnc.salesforce.android.LIB;


import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Production_Detail_ListAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public Production_Detail_ListAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
		
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.size();
				
		imgBack = new ImageView[cnt];
	}

	public int getCount() {
		
		return cnt;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	
	ImageView[] imgBack;
	
	private Decimal_Coma DC = new Decimal_Coma();
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup viewgroup) {
		
		if (convertView == null) {
						
			convertView = (View) inflater.inflate(R.layout.production_item, viewgroup, false);
			
		} 
		
		imgBack[position] = ( ImageView ) convertView.findViewById(R.id.imgBack);
				
		if( position % 2 == 0 ){
			imgBack[position].setBackgroundResource(R.drawable.at_lp_grideven);
		} else {
			imgBack[position].setBackgroundResource(R.drawable.at_lp_gridodd);
		}
		
		TextView  txtAllJaeGo =  ( TextView )convertView.findViewById( R.id.txtAllJaeGo );
		TextView  txtSize = ( TextView ) convertView.findViewById( R.id.txtSize );
		TextView  txtShopJaeGo = ( TextView ) convertView.findViewById( R.id.txtShopJaeGo );
		TextView  txtWhQtyJaeGo = ( TextView ) convertView.findViewById( R.id.txtWhQtyJaeGo );
		
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).sizeCd.equals("null") ){
				txtSize.setText( "0" );
			} else {
				txtSize.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).sizeCd );
			}
		} catch (Exception e) {
			txtSize.setText( "0" );
		}
		
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).jegoQty.equals("null") ){
				txtShopJaeGo.setText( "0" );
			} else {
				String mjegoQty = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).jegoQty ) );
				txtShopJaeGo.setText( mjegoQty );
			}
			
		} catch (Exception e) {
			txtShopJaeGo.setText( "0" );
		}
		
		
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).otherQty.equals("null") ){
				txtAllJaeGo.setText( "0" );
			} else {
				String motherQty = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).otherQty ) );
				txtAllJaeGo.setText( motherQty );
			}
		} catch (Exception e) {
			txtAllJaeGo.setText( "0" );
		}
		
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).whQty.equals("null") ){
				txtWhQtyJaeGo.setText( "0" );
			} else {
				String motherQty = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.get(position).whQty ) );
				txtWhQtyJaeGo.setText( motherQty );
			}
		} catch (Exception e) {
			txtWhQtyJaeGo.setText( "0" );
		}
		
		
		
		return convertView;
	}
}
// }
