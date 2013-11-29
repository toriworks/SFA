package fnc.salesforce.android;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.ImageDownloader;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.KumaLog;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BrandItemListAdabter extends BaseAdapter {
	
	private Context mContext;
	private int cnt = 0;
	LayoutInflater inflater;
	boolean D = false;
	
	public BrandItemListAdabter(Context c) {
		mContext = c;
		inflater = ((Activity) mContext).getLayoutInflater();
	}

	public int getCount() {
		cnt = Constance.BrandCodeImage_Small.length;
				
		return 1;
	}
	
	
	
	public Object getItem(int position) {
		return null;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
	
	ImageView imgBrandImg;
	ImageButton btnBrandlist_Item;
	
	@Override
	public View getView(final int position, View convertView, ViewGroup viewgroup) {
		
		if (convertView == null) {
			convertView = (View) inflater.inflate(R.layout.brand_list_item, viewgroup, false);
			btnBrandlist_Item = ( ImageButton ) convertView.findViewById(R.id.btnBrandlist_Item);
			
			imgBrandImg = ( ImageView ) convertView.findViewById(R.id.imgBrandImg);
		}
		
		imgBrandImg.setBackgroundResource( Constance.getBrandImg_Large( Constance.BEANDCD ) );
		
		btnBrandlist_Item.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		return convertView;
	}
}