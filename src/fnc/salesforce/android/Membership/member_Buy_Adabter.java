package fnc.salesforce.android.Membership;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.LIB.Decimal_Coma;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class member_Buy_Adabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public member_Buy_Adabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.size();

		return cnt;
	}
	
	public Object getItem(int position) {
		return null;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup viewgroup) {
		
		if (convertView == null) {
			convertView = (View) inflater.inflate(R.layout.member_customer_buy_story_list_item, viewgroup, false);
			
		}
		LinearLayout imgProductThumb = ( LinearLayout ) convertView.findViewById(R.id.imgBack);
//		txtShopAreaPhone
//	    txtShopAreaAddr
//	    txtShopAreaShopName
//	    txtShopAreaZone
//	    txtShopAreaType
		
		TextView txtBuyDate 		= ( TextView ) convertView.findViewById(R.id.txtBuyDate);		
		TextView txtBuyShop 		= ( TextView ) convertView.findViewById(R.id.txtBuyShop);
		TextView txtProductCode 	= ( TextView ) convertView.findViewById(R.id.txtProductCode);		
		TextView txtColorCode 		= ( TextView ) convertView.findViewById(R.id.txtColorCode);
		TextView txtSizeCode 		= ( TextView ) convertView.findViewById(R.id.txtSizeCode);
		TextView txtBuyCount 		= ( TextView ) convertView.findViewById(R.id.txtBuyCount);		
		TextView txtProductname 	= ( TextView ) convertView.findViewById(R.id.txtProductname);		
		TextView txtBuyCost 		= ( TextView ) convertView.findViewById(R.id.txtBuyCost);
		TextView txtAccmlPoint 		= ( TextView ) convertView.findViewById(R.id.txtAccmlPoint);
		TextView txtUsePoint 		= ( TextView ) convertView.findViewById(R.id.txtUsePoint);
		
		if( position % 2 == 1){
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_mem_buy_grid_odd);
		} else {
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_mem_buy_grid_even);
		}
		
		try {
			String saleDat = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).saleDat;
			if( saleDat.equals("null") ){
				txtBuyDate.setText( "" );
			} else {
				txtBuyDate.setText( saleDat );
			}
		} catch (Exception e) {
			txtBuyDate.setText( "" );
		}
		
		try {
			String srcNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).srcNm;
			
			if( srcNm.equals("null") ){
				txtBuyShop.setText( "" );
			} else {
				txtBuyShop.setText( srcNm );
			}
		} catch (Exception e) {
			txtBuyShop.setText( "" );
		}
		
		try {
			String prductCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).prductCd;
			
			if( prductCd.equals("null") ){
				txtProductCode.setText( "" );
			} else {
				txtProductCode.setText( prductCd );
			}
		} catch (Exception e) {
			txtProductCode.setText( "" );
		}
		
		try {
			String colorC = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).colorC;
			
			if( colorC.equals("null") ){
				txtColorCode.setText( "" );
			} else {
				txtColorCode.setText( colorC );
			}
		} catch (Exception e) {
			txtColorCode.setText( "" );
		}
				
		try {
			String sizC = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).sizC;
			
			if( sizC.equals("null") ){
				txtSizeCode.setText( "" );
			} else {
				txtSizeCode.setText( sizC );
			}
		} catch (Exception e) {
			txtSizeCode.setText( "" );
		}
				
		try {
			String qty = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).qty;
			if( qty.equals("null") ){
				txtBuyCount.setText( "" );
			} else {
				txtBuyCount.setText( qty );
			}
		} catch (Exception e) {
			txtBuyCount.setText( "" );
		}
				
		try {
			String prductNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).prductNm;
			if( prductNm.equals("null") ){
				txtProductname.setText( "" );
			} else {
				txtProductname.setText( prductNm );
			}
		} catch (Exception e) {
			txtProductname.setText( "" );
		}

		try {
			String purchaseAmount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).purchaseAmount;
			if( purchaseAmount.equals("null") ){
				txtBuyCost.setText( "" );
			} else {
				txtBuyCost.setText( DC.Numeric3comma( Integer.parseInt( purchaseAmount ) ) );
			}
		} catch (Exception e) {
			txtBuyCost.setText( "" );
		}

		try {
			String accmlPoint = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).accmlPoint;
			if( accmlPoint.equals("null") ){
				txtAccmlPoint.setText( "" );
			} else {
				txtAccmlPoint.setText( DC.Numeric3comma( Integer.parseInt( accmlPoint ) ) );
			}
		} catch (Exception e) {
			txtAccmlPoint.setText( "" );
		}
		
		try {
			String usePoint = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.get( position ).usePoint;
			if( usePoint.equals("null") ){
				txtUsePoint.setText( "" );
			} else {
				txtUsePoint.setText( DC.Numeric3comma( Integer.parseInt( usePoint ) ) );
			}
		} catch (Exception e) {
			txtUsePoint.setText( "" );
		}
		
		return convertView;
	}
	
	private Decimal_Coma DC = new Decimal_Coma();
}