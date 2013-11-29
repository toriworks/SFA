package fnc.salesforce.android.PullToList;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.LIB.ImageDownloader;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.Decimal_Coma;
import fnc.salesforce.android.R.id;
import fnc.salesforce.android.R.layout;
import fnc.salesforce.android.R.style;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopListAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public ShopListAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.size();
				
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
			convertView = (View) inflater.inflate(R.layout.shop_list_item, viewgroup, false);
			
		}
		View imgProductThumb = ( View ) convertView.findViewById(R.id.imgBack);
//		txtShopAreaPhone
//	    txtShopAreaAddr
//	    txtShopAreaShopName
//	    txtShopAreaZone
//	    txtShopAreaType
		
		TextView txtShopAreaType = ( TextView ) convertView.findViewById(R.id.txtShopAreaType);
		TextView txtShopAreaZone = ( TextView ) convertView.findViewById(R.id.txtShopAreaZone);
		TextView txtShopAreaShopName = ( TextView ) convertView.findViewById(R.id.txtShopAreaShopName);
		TextView txtShopAreaAddr = ( TextView ) convertView.findViewById(R.id.txtShopAreaAddr);
		TextView txtShopAreaPhone = ( TextView ) convertView.findViewById(R.id.txtShopAreaPhone);
		
		if( position % 2 == 1){
			imgProductThumb.setBackgroundResource( R.drawable.im_sf_store_gridodd);
		} else {
			imgProductThumb.setBackgroundResource( R.drawable.im_sf_store_grideven);
		}
		
		try {
			String shopTy = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.get( position ).shopTy;
			if( shopTy.equals("null") ){
				txtShopAreaType.setText( "" );
			} else {
				txtShopAreaType.setText( shopTy );
			}
		} catch (Exception e) {
			txtShopAreaType.setText( "" );
		}
		
		try {
			String areaNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.get( position ).areaNm;
			
			if( areaNm.equals("null") ){
				txtShopAreaZone.setText( "" );
			} else {
				txtShopAreaZone.setText( areaNm );
			}
		} catch (Exception e) {
			txtShopAreaZone.setText( "" );
		}
		
		try {
			String shopNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.get( position ).shopNm;
			
			if( shopNm.equals("null") ){
				txtShopAreaShopName.setText( "" );
			} else {
				txtShopAreaShopName.setText( shopNm );
			}
		} catch (Exception e) {
			txtShopAreaShopName.setText( "" );
		}
		
		try {
			String addr = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.get( position ).addr;
			
			if( addr.equals("null") ){
				txtShopAreaAddr.setText( "" );
			} else {
				txtShopAreaAddr.setText( addr );
			}
		} catch (Exception e) {
			txtShopAreaAddr.setText( "" );
		}
		
		try {
			String telno = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.get( position ).telno;
			
			if( telno.equals("null") ){
				txtShopAreaPhone.setText( "" );
			} else {
				txtShopAreaPhone.setText( telno );
			}
		} catch (Exception e) {
			txtShopAreaPhone.setText( "" );
		}
		
		
		return convertView;
	}
}