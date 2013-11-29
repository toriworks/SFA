package fnc.salesforce.android.Repair;

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

public class RepairAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public RepairAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.size();
				
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
			convertView = (View) inflater.inflate(R.layout.repair_list_item, viewgroup, false);
			
		}
		TableRow imgProductThumb = ( TableRow ) convertView.findViewById(R.id.imgBack);
//		txtShopAreaPhone
//	    txtShopAreaAddr
//	    txtShopAreaShopName
//	    txtShopAreaZone
//	    txtShopAreaType
		
		TextView txtRepairType 		= ( TextView ) convertView.findViewById(R.id.txtRepairType);
		TextView txtRepairGoods 	= ( TextView ) convertView.findViewById(R.id.txtRepairGoods);
		TextView txtRepairSpecies 	= ( TextView ) convertView.findViewById(R.id.txtRepairSpecies);
		TextView txtRepairItem 		= ( TextView ) convertView.findViewById(R.id.txtRepairItem);
		TextView txtRepairCost 		= ( TextView ) convertView.findViewById(R.id.txtRepairCost);
		TextView txtRepairCost_Type = ( TextView ) convertView.findViewById(R.id.txtRepairCost_Type);
		TextView txtRepair_Note 	= ( TextView ) convertView.findViewById(R.id.txtRepair_Note);
		
		if( position % 2 == 1){
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_as_gridodd);
		} else {
			imgProductThumb.setBackgroundResource( R.drawable.at_sf_as_grideven);
		}
		
		try {
			String mType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).div;
			if( mType.equals("null") ){
				txtRepairType.setText( "" );
			} else {
				txtRepairType.setText( mType );
			}
		} catch (Exception e) {
			txtRepairType.setText( "" );
		}
		
		try {
			String mGoods = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).rpairsGoodsDiv;
			
			if( mGoods.equals("null") ){
				txtRepairGoods.setText( "" );
			} else {
				txtRepairGoods.setText( mGoods );
			}
		} catch (Exception e) {
			txtRepairGoods.setText( "" );
		}
		
		try {
			String mSpecies = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).rpairsSpeciesDiv;
			
			if( mSpecies.equals("null") ){
				txtRepairSpecies.setText( "" );
			} else {
				txtRepairSpecies.setText( mSpecies );
			}
		} catch (Exception e) {
			txtRepairSpecies.setText( "" );
		}
		
		try {
			String mItem = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).rpairsItem;
			
			if( mItem.equals("null") ){
				txtRepairItem.setText( "" );
			} else {
				txtRepairItem.setText( mItem );
			}
		} catch (Exception e) {
			txtRepairItem.setText( "" );
		}
		
		try {
			String mCost = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).rpairsPrice;
			
			if( mCost.equals("null") ){
				txtRepairCost.setText( "" );
			} else {
				txtRepairCost.setText( DC.Numeric3comma( Integer.parseInt( mCost ) )  );
			}
		} catch (Exception e) {
			txtRepairCost.setText( "" );
		}
		
		try {
			String mCostType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).chrdYn;
			
			if( mCostType.equals("null") ){
				txtRepairCost_Type.setText( "" );
			} else {
				
				txtRepairCost_Type.setText( mCostType );
			}
		} catch (Exception e) {
			txtRepairCost_Type.setText( "" );
		}
		
		try {
			String mNote = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.get( position ).rpairsRm;
			
			if( mNote.equals("null") ){
				txtRepair_Note.setText( "" );
			} else {
				txtRepair_Note.setText( mNote );
			}
		} catch (Exception e) {
			txtRepair_Note.setText( "" );
		}
		
		
		return convertView;
	}
	
	private Decimal_Coma DC = new Decimal_Coma();
}