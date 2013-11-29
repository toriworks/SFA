package fnc.salesforce.android.AD_10003;

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

public class ADGalleryProductListAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public ADGalleryProductListAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
		ID = new ProductDialog( mContext, R.style.Transparent);
	}

	public int getCount() {
		cnt = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.size();
				
		return cnt;
	}
	
	
	
	public Object getItem(int position) {
		return null;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	private ImageDownloader IDL = new ImageDownloader();
	
	
	private ProductDialog ID;
	
	private Decimal_Coma DC = new Decimal_Coma();
	
	@Override
	public View getView(final int position, View convertView, ViewGroup viewgroup) {
		
		if (convertView == null) {
			convertView = (View) inflater.inflate(R.layout.product_list_item, viewgroup, false);
			
		}
		ImageView imgProductThumb = ( ImageView ) convertView.findViewById(R.id.imgProductThumb);
		
		TextView txtProductName = ( TextView ) convertView.findViewById(R.id.txtProductName);
		TextView txtProductCode = ( TextView ) convertView.findViewById(R.id.txtProductCode);
		TextView txtProductState = ( TextView ) convertView.findViewById(R.id.txtProductState);
		
		IDL.download(mContext, CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).thumbUrl, imgProductThumb, null);
		
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).prductNm.equals("null") ){
				txtProductName.setText( "" );
			} else {
				txtProductName.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).prductNm );
			}
		} catch (Exception e) {
			txtProductName.setText( "" );
		}
		
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).prductCd.equals("null") ){
				txtProductCode.setText( "" );
			} else {
				txtProductCode.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).prductCd );
			}
		} catch (Exception e) {
			txtProductCode.setText( "" );
		}
		
		String strPrice = "";
		try {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).copr.equals("null") ){
				strPrice = "0 원  / 수량 ";
			} else {
				String mPrice = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).copr ) );
				strPrice =  mPrice + " 원  / 수량 ";
			}
			
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).jegoTotqy.equals("null") ){
				strPrice = strPrice + "0";
			} else {
				String mLego = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).jegoTotqy ) );
				strPrice = strPrice + mLego;
			}
			
			txtProductState.setText( strPrice );
		} catch (Exception e) {
			txtProductState.setText( "0 원  / 수량  0" );
		}
		
		imgProductThumb.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				ID.SetProduct_CD( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(position).prductCd );
				ID.show();
			}
		});
		
		return convertView;
	}
}