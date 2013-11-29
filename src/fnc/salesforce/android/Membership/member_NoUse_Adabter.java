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

public class member_NoUse_Adabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public member_NoUse_Adabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.size();
				
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
			convertView = (View) inflater.inflate(R.layout.membership_customer_nouse_campaign_list_item, viewgroup, false);
			
		}
		LinearLayout imgProductThumb = ( LinearLayout ) convertView.findViewById(R.id.imgBack);
		
		TextView txtCampaignName 		= ( TextView ) convertView.findViewById(R.id.txtCampaignName);
		TextView txtStartDate 			= ( TextView ) convertView.findViewById(R.id.txtStartDate);
		TextView txtEndDate 			= ( TextView ) convertView.findViewById(R.id.txtEndDate);
		TextView txtCouPhonName 		= ( TextView ) convertView.findViewById(R.id.txtCouPhonName);
		TextView txtUseAmount 			= ( TextView ) convertView.findViewById(R.id.txtUseAmount);

		
		
		if( position % 2 == 1){
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_mem_campaign_grid_odd);
		} else {
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_mem_campaign_grid_even);
		}
		
		try {
			String campaignNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.get( position ).campaignNm;
			if( campaignNm.equals("null") ){
				txtCampaignName.setText( "" );
			} else {
				txtCampaignName.setText( campaignNm );
			}
		} catch (Exception e) {
			txtCampaignName.setText( "" );
		}
		
		try {
			String startDate = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.get( position ).startDate;			
			if( startDate.equals("null") ){
				txtStartDate.setText( "" );
			} else {
				txtStartDate.setText( startDate );
			}
		} catch (Exception e) {
			txtStartDate.setText( "" );
		}
		
		try {
			String endDate = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.get( position ).endDate;
			
			if( endDate.equals("null") ){
				txtEndDate.setText( "" );
			} else {
				txtEndDate.setText( endDate );
			}
		} catch (Exception e) { 
			txtEndDate.setText( "" );
		}
		
		try {
			String couponNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.get( position ).couponNm;
			if( couponNm.equals("null") ){
				txtCouPhonName.setText( "" );
			} else {
				txtCouPhonName.setText( couponNm );
			}
		} catch (Exception e) {
			txtCouPhonName.setText( "" );
		}
		
		try {
			String applcAmount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.get( position ).applcAmount;
			if( applcAmount.equals("null") ){
				txtUseAmount.setText( "" );
			} else {
				txtUseAmount.setText( DC.Numeric3comma( Integer.parseInt( applcAmount ) ) );
			}
		} catch (Exception e) {
			txtUseAmount.setText( "" );
		}

		
		
		return convertView;
	}
	
	private Decimal_Coma DC = new Decimal_Coma();
}