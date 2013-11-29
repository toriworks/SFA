package fnc.salesforce.android.LIB;

import java.text.NumberFormat;

public class Decimal_Coma {
	
	public Decimal_Coma(){
		
	}
	
	public String Decimal_coma(float Number){
		String str;
		
		String price_Unit = new java.text.DecimalFormat("#.##").format(Number);
		String[] Unit = price_Unit.split("\\.");
		String Positive_Number = Numeric3comma(Integer.parseInt(Unit[0]));		
		
		if(Unit.length == 2){
			str = Positive_Number+"."+Unit[1];
		}else{
			str = Positive_Number;
		}
		
		return str;
	}
	
	public String Numeric3comma(int str) {
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		String r_str = nf.format(str);

		return r_str; 
	}
}