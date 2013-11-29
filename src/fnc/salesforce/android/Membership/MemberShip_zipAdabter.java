package fnc.salesforce.android.Membership;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.LIB.Decimal_Coma;
import fnc.salesforce.android.LIB.KumaLog;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

public class MemberShip_zipAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public MemberShip_zipAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.size();

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
			convertView = (View) inflater.inflate(R.layout.member_zipcode_list_item, viewgroup, false);
		}
		
		TextView txtZipCode 		= ( TextView ) convertView.findViewById(R.id.txtZipCode);
		TextView txtZipAreaName 	= ( TextView ) convertView.findViewById(R.id.txtZipAreaName);
		
		try {
			String mType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.get( position ).postNo;
			if( mType.equals("null") ){
				txtZipCode.setText( "" );
			} else {
				txtZipCode.setText( mType );
			}
		} catch (Exception e) {
			txtZipCode.setText( "" );
		}
		
		try {
			String mGoods = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.get( position ).postArea;
			
			if( mGoods.equals("null") ){
				txtZipAreaName.setText( "" );
			} else {
				txtZipAreaName.setText( mGoods );
			}
		} catch (Exception e) {
			txtZipAreaName.setText( "" );
		}
		
		return convertView;
	}
}