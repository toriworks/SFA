package fnc.salesforce.android.Education;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class slideBar extends SeekBar {
	
    public slideBar(Context context) {
    	super(context);     
    }     
    public slideBar(Context context, AttributeSet attrs, int defStyle) { 
    	super(context, attrs, defStyle);    
    }
    
    public slideBar(Context context, AttributeSet attrs) {
    	super(context, attrs);     
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { 
    	Log.e("test","h : " + h + " w " + w);
    	super.onSizeChanged(h, w, oldh, oldw);    
    }     
    @Override   
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(heightMeasureSpec, widthMeasureSpec);        
    	setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());   
    }
    
	protected void onDraw(Canvas c) {
		c.rotate(-90);         
		c.translate(-getHeight(), 0); 
//		c.rotate(90);
//		c.translate(0,-29);
		super.onDraw(c);
	}
	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		super.setProgress(progress);
		onSizeChanged(getWidth(), getHeight(), 0, 0);
	}
	@Override     
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {             
			return false;         
		}         
		
		switch (event.getAction()) {    
		case MotionEvent.ACTION_DOWN:   
		case MotionEvent.ACTION_MOVE:     
		case MotionEvent.ACTION_UP:          
			setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()) );
			onSizeChanged(getWidth(), getHeight(), 0, 0);          
			break;             
		case MotionEvent.ACTION_CANCEL:       
			break;        
		}        
		return true;    
	} 
}