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
import android.widget.TableRow;
import android.widget.TextView;

public class MemberShipCustomerAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public MemberShipCustomerAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.size();
				
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
			convertView = (View) inflater.inflate(R.layout.membership_customer_list_item, viewgroup, false);
			
		}
		TableRow imgProductThumb = ( TableRow ) convertView.findViewById(R.id.imgBack);
//		txtShopAreaPhone
//	    txtShopAreaAddr
//	    txtShopAreaShopName
//	    txtShopAreaZone
//	    txtShopAreaType
		
		TextView txtMemberName 		= ( TextView ) convertView.findViewById(R.id.txtMemberName);
		TextView txtMemberPhone 	= ( TextView ) convertView.findViewById(R.id.txtMemberPhone);
		TextView txtMemberBirth 	= ( TextView ) convertView.findViewById(R.id.txtMemberBirth);
		TextView txtMemberPoint 		= ( TextView ) convertView.findViewById(R.id.txtMemberPoint);
		TextView txtMemberJoindate 	= ( TextView ) convertView.findViewById(R.id.txtMemberJoindate);
		TextView txtMemberJoinShop 	= ( TextView ) convertView.findViewById(R.id.txtMemberJoinShop);
		
		
		if( position % 2 == 1){
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_mem_03_grid_odd);
		} else {
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_mem_03_grid_even);
		}
		
		try {
			String mType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( position ).csName;
			if( mType.equals("null") ){
				txtMemberName.setText( "" );
			} else {
				txtMemberName.setText( mType );
			}
		} catch (Exception e) {
			txtMemberName.setText( "" );
		}
		
		try {
			String mGoods = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( position ).mobileTelNo;
			
			if( mGoods.equals("null") ){
				txtMemberPhone.setText( "" );
			} else {
				txtMemberPhone.setText( mGoods );
			}
		} catch (Exception e) {
			txtMemberPhone.setText( "" );
		}
		
		try {
			String mSpecies = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( position ).birthday;
			
			if( mSpecies.equals("null") ){
				txtMemberBirth.setText( "" );
			} else {
				txtMemberBirth.setText( mSpecies );
			}
		} catch (Exception e) {
			txtMemberBirth.setText( "" );
		}
		
		try {
			String mItem = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( position ).agencyUsefulPoint;
			
			if( mItem.equals("null") ){
				txtMemberPoint.setText( "" );
			} else {
				txtMemberPoint.setText( DC.Numeric3comma( Integer.parseInt( mItem ) )  + " p");
			}
		} catch (Exception e) {
			txtMemberPoint.setText( "" );
		}
		
		try {
			String mCost = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( position ).entrydate;
			
			if( mCost.equals("null") ){
				txtMemberJoindate.setText( "" );
			} else {
				txtMemberJoindate.setText( mCost );
			}
		} catch (Exception e) {
			txtMemberJoindate.setText( "" );
		}
		
		try {
			String mCostType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( position ).srcNm;
			
			if( mCostType.equals("null") ){
				txtMemberJoinShop.setText( "" );
			} else {
				
				txtMemberJoinShop.setText( mCostType );
			}
		} catch (Exception e) {
			txtMemberJoinShop.setText( "" );
		}
		
		
		return convertView;
	}
	
	private Decimal_Coma DC = new Decimal_Coma();
}