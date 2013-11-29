package fnc.salesforce.android.LIB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.URLConstance;




public class libJSON_GET {

	private static String[] BufferString;
	
	private static String SITE_URL = "";
	
	public libJSON_GET(){

	}
	
	/** EDUCATION_CONTENTS 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String getVersion( String brandCD ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.VERSION_INFOR;
			
			state = getHttpJson_String(SITE_URL).toString(); 

		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return state;
	}
	
	/** EDUCATION_CONTENTS 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String getFileDownLoad() {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.NEW_FILE;
			
			state = getHttpJson_String(SITE_URL).toString(); 

		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return state;
	}
	
//	최신 버전 조회
//	http://m.kolon.com:8080/business/app/servAppVer.do?appNm=mSalesForceA
//	다운로드 정보 조회
//	http://m.kolon.com:8080/business/app/servAppUrl.do?appNm=mSalesForceA
		
	/** DEFAULT_CONTENTS 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getDEFAULT_CONTENTS( String brandCD ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.EDUCATION_CONTENTS + brandCD;
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			state = jsonChannels.getString("RESULT").toString();
			if ( state.equals("99") ) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** HEAD_OF_LOGIN
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getREMOVE_DEVICE( String[] parameter ) {

		String[] arrReturnParameter = new String[2];

		try { 
			SITE_URL = URLConstance.REMOVE_DEVICE + "accessToken=" + parameter[2]+ "&timestamp=" + parameter[3];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" ); 
			if ( arrReturnParameter[0].equals("200") ) {
				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	
	/** SHOP_AREA_LIST 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getSHOP_AREA_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.SHOP_AREA_LIST + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST.clear();
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					
					chargeConstance.areaCd 	= getParameterString ( nameArray,j,"areaCd");
					chargeConstance.areaNm 	= getParameterString ( nameArray, j,"areaNm");
					
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** REPAIR_GOODS_LIST 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getREPAIR_GOODS_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.REPAIR_GOODS_LIST + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] 
					+"&brandCd=" + parameter[2] + "&rpairsDivCd=" + parameter[3];
			
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_GOODS_LIST.clear();
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					
					chargeConstance.rpairsDivCd 	= getParameterString ( nameArray,j,"rpairsDivCd");
					chargeConstance.rpairsGoodsNm 	= getParameterString ( nameArray, j,"rpairsGoodsNm");
					chargeConstance.rpairsGoodsCd 	= getParameterString ( nameArray,j,"rpairsGoodsCd");
					chargeConstance.brandCd 		= getParameterString ( nameArray, j,"brandCd");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_GOODS_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** REPAIR_SPECIES_LIST 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getREPAIR_SPECIES_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.REPAIR_SPECIES_LIST + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] 
					+"&brandCd=" + parameter[2] + "&rpairsDivCd=" + parameter[3] + "&rpairsGoodsCd=" + parameter[4];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.clear();
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					
					chargeConstance.rpairsDivCd 	= getParameterString ( nameArray,j,"rpairsDivCd");
					chargeConstance.rpairsGoodsCd 	= getParameterString ( nameArray, j,"rpairsGoodsCd");
					chargeConstance.rpairsSpeciesNm = getParameterString ( nameArray,j,"rpairsSpeciesNm");
					chargeConstance.rpairsSpeciesCd = getParameterString ( nameArray, j,"rpairsSpeciesCd");
					chargeConstance.brandCd 		= getParameterString ( nameArray, j,"brandCd");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** REPAIR_RESULT_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getREPAIR_RESULT_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			String contentText2 = java.net.URLEncoder.encode(new String(parameter[9].getBytes("UTF-8")));
			SITE_URL = URLConstance.REPAIR_RESULT_LIST + "currentPage=" + parameter[0]+ "&maxResults=" + parameter[1] +"&maxLinks=" + parameter[2]
					+ "&accessToken=" + parameter[3] +"&timestamp=" + parameter[4]+ "&rpairsDivCd=" + parameter[5] +"&rpairsGoodsCd=" + parameter[6]
				+ "&rpairsSpeciesCd=" + parameter[7] +"&brandCd=" + parameter[8]+ "&rpairsItem=" + contentText2;


			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				
				JSONArray nameArray = jsonChannels.getJSONArray("list");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.div					= getParameterString ( nameArray,j,"div");
					chargeConstance.rpairsGoodsDiv 		= getParameterString ( nameArray, j,"rpairsGoodsDiv");
					chargeConstance.rpairsSpeciesDiv 	= getParameterString ( nameArray,j,"rpairsSpeciesDiv");
					chargeConstance.rpairsItem 			= getParameterString ( nameArray, j,"rpairsItem");
					chargeConstance.rpairsPrice 		= getParameterString ( nameArray,j,"rpairsPrice");
					chargeConstance.rpairsRm			= getParameterString ( nameArray, j,"rpairsRm");
					chargeConstance.chrdYn 				= getParameterString ( nameArray,j,"chrdYn");

					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** REPAIR_IMAGE_SPECIES_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getREPAIR_IMAGE_SPECIES_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.REPAIR_IMAGE_SPECIES_LIST + "accessToken=" + parameter[0] +"&timestamp=" + parameter[1];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.clear();
				JSONArray nameArray = jsonChannels.getJSONArray("results");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.rpairsSpeciesNm			= getParameterString ( nameArray,j,"rpairsSpeciesNm");
					chargeConstance.rpairsSpeciesCd 		= getParameterString ( nameArray, j,"rpairsSpeciesCd");

					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** REPAIR_IMAGE_ITEN_LIST 
	 * 
	 * 
	 * */
	public boolean getREPAIR_IMAGE_ITEN_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.REPAIR_IMAGE_ITEN_LIST + "accessToken=" + parameter[0] +"&timestamp=" + parameter[1];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_ITEM_LIST.clear();
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.rpairsItemNm		= getParameterString ( nameArray,j,"rpairsItemNm");
					chargeConstance.rpairsItemCd 		= getParameterString ( nameArray, j,"rpairsItemCd");

					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_ITEM_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** REPAIR_IMAGE_RESULT_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getREPAIR_IMAGE_RESULT_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.REPAIR_IMAGE_RESULT_LIST + "currentPage=" + parameter[0]+ "&maxResults=" + parameter[1] +"&maxLinks=" + parameter[2]
					+ "&accessToken=" + parameter[3] +"&timestamp=" + parameter[4]+ "&brandCd=" + parameter[5] +"&rpairsSpeciesCd=" + parameter[6]
				+ "&rpairsItemCd=" + parameter[7];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				Constance.Page_ALL_Count	= mPageCount.getString( "countItem" );
				
				JSONArray nameArray = jsonChannels.getJSONArray("list");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.thumbUrl				= getParameterString ( nameArray,j,"thumbUrl");
					chargeConstance.pumCd 					= getParameterString ( nameArray, j,"pumCd");
					chargeConstance.pumNm 					= getParameterString ( nameArray,j,"pumNm");
					chargeConstance.asCd 					= getParameterString ( nameArray, j,"asCd");
					chargeConstance.asNm 					= getParameterString ( nameArray,j,"asNm");
					chargeConstance.brandCd					= getParameterString ( nameArray, j,"brandCd");
					chargeConstance.asMethod 				= getParameterString ( nameArray,j,"asMethod");
					
					chargeConstance.asEffect 				= getParameterString ( nameArray,j,"asEffect");
					chargeConstance.refairAfterTotImage		= getParameterString ( nameArray, j,"refairAfterTotImage");
					chargeConstance.refairBeforePartImage 	= getParameterString ( nameArray,j,"refairBeforePartImage");
					
					chargeConstance.refairAfterPartImage 	= getParameterString ( nameArray,j,"refairAfterPartImage");
					chargeConstance.orgBefore				= getParameterString ( nameArray, j,"orgBefore");
					chargeConstance.orgAfter 				= getParameterString ( nameArray,j,"orgAfter");
					chargeConstance.orgFull 				= getParameterString ( nameArray,j,"orgFull");


					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	
	/** MEMBER_CUSTOMER_SEARCH 
	 * 
	 * 
	 * */
	public boolean getMEMBER_CUSTOMER_SEARCH( String[] parameter ) {
		
		String state = "";
		String contentText2 = "";
		try {
			contentText2 = java.net.URLEncoder.encode(new String( parameter[5].getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try { 
			SITE_URL = URLConstance.MEMBER_CUSTOMER_SEARCH + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] +"&currentPage=" + parameter[2]
					+ "&maxResults=" + parameter[3] +"&maxLinks=" + parameter[4]+ "&csName=" + contentText2 +"&mobileTelNo=" + parameter[6]
				+ "&birthday=" + parameter[7] + "&shopCd=" + Constance.SHOPCD;

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				Constance.Page_ALL_Count	= mPageCount.getString( "countItem" );
				
				JSONArray nameArray = jsonChannels.getJSONArray("list");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.csNo				= getParameterString ( nameArray,j,"csNo");
					try {
						chargeConstance.mobileTelNo 		= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, j,"mobileTelNo") );
					} catch (Exception e) {
						chargeConstance.mobileTelNo 		= "";
					}
					try {
						chargeConstance.csName 				= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray,j,"csName") );
					} catch (Exception e) {
						chargeConstance.csName 		= "";
					}
					
					chargeConstance.birthday 			= getParameterString ( nameArray, j,"birthday");
					chargeConstance.agencyUsefulPoint 	= getParameterString ( nameArray,j,"agencyUsefulPoint");
					chargeConstance.drtsUsefulPoint		= getParameterString ( nameArray, j,"drtsUsefulPoint");
					chargeConstance.entrydate 			= getParameterString ( nameArray,j,"entrydate");
					
					chargeConstance.srcNm 				= getParameterString ( nameArray,j,"srcNm");
					chargeConstance.cnt					= getParameterString ( nameArray, j,"cnt");

					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** MEMBER_CUSTOMER_SEARCH_DETAIL 
	 * 
	 * 
	 * */
	public boolean getMEMBER_CUSTOMER_SEARCH_DETAIL( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.MEMBER_CUSTOMER_SEARCH_DETAIL + "accessToken=" + parameter[0] +"&timestamp=" + parameter[1] + "&csNo=" + parameter[2];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.clear();
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
								
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
				
				chargeConstance.csNo				= getParameterString ( nameArray,"csNo");
				try {
					chargeConstance.csName 				= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "csName") );
				} catch (Exception e) {
					chargeConstance.csName 		= "";
				}
				
				chargeConstance.agencyUsefulPoint	= getParameterString ( nameArray, "agencyUsefulPoint");
				chargeConstance.drtsUsefulPoint 	= getParameterString ( nameArray, "drtsUsefulPoint");
				chargeConstance.entrydate			= getParameterString ( nameArray, "entrydate");
				chargeConstance.srcNm 				= getParameterString ( nameArray, "srcNm");
				chargeConstance.cardNo				= getParameterString ( nameArray, "cardNo");
				chargeConstance.gender				= getParameterString ( nameArray, "gender");
				
				try {
					chargeConstance.addr 				= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "addr") );
				} catch (Exception e) {
					chargeConstance.addr 		= "";
				}
				
				
				chargeConstance.zipCd				= getParameterString ( nameArray, "zipCd");
				chargeConstance.weddingYn 			= getParameterString ( nameArray, "weddingYn");
				chargeConstance.cnt					= getParameterString ( nameArray, "cnt");
				
				try {
					chargeConstance.mobileTelNo 		= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "mobileTelNo") );
				} catch (Exception e) {
					chargeConstance.mobileTelNo 		= "";
				}
				
				chargeConstance.juminBirthday		= getParameterString ( nameArray, "juminBirthday");
				chargeConstance.birthday 			= getParameterString ( nameArray, "birthday");
				chargeConstance.telNo				= getParameterString ( nameArray, "telNo");
				chargeConstance.weddingAnniversary 	= getParameterString ( nameArray, "weddingAnniversary");
				chargeConstance.companyGrade		= getParameterString ( nameArray, "companyGrade");
				chargeConstance.brandGrade 			= getParameterString ( nameArray, "brandGrade");
				chargeConstance.shopGrade			= getParameterString ( nameArray, "shopGrade");
				chargeConstance.repShopSeq 			= getParameterString ( nameArray, "repShopSeq");

		        
		        
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.add(chargeConstance);
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** MEMBER_CUSTOMER_SEARCH_PASSWORD_CHECK 
	 * 
	 * 
	 * */
	public String getMEMBER_CUSTOMER_SEARCH_PASSWORD_CHECK( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.MEMBER_CUSTOMER_SEARCH_PASSWORD_CHECK + "accessToken=" + parameter[0] +"&timestamp=" + parameter[1]
					+ "&csNo=" + parameter[2] + "&pwd=" + parameter[3] + "&userId=" + parameter[4];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.clear();
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
		        
				
				return getParameterString ( nameArray,"chkPwd").toString().toUpperCase();
			} else {
				return "N";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "N";
		}
	}
	/** MEMBER_MOBILE_ENTRY 
	 * 
	 * 
	 * */
	public String MEMBER_MOBILE_ENTRY( String[] parameter ) {
		
		String state = "";
		
		String contentText2 = "";
		try {
			contentText2 = java.net.URLEncoder.encode(new String( parameter[2].getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try { 
			
			SITE_URL = URLConstance.MEMBER_MOBILE_ENTRY + "accessToken=" + parameter[0] +"&timestamp=" + parameter[1]
					+ "&csName=" + contentText2 + "&mobileTelNo=" + parameter[3] + "&telecomCd=" + parameter[4] + "&shopCd=" + Constance.SHOPCD;
							

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.clear();
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
				
				return getParameterString ( nameArray,"resultCertification").toString().toUpperCase();
			} else {
				return "N";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "N";
		}
	}
	
	
	/** MEMBER_CERTI_AUTH 
	 * 
	 * 
	 * */
	public boolean getMEMBER_CERTI_AUTH( String[] parameter ) {
		String state = "";
		String contentText2 = "";
		try {
			contentText2 = java.net.URLEncoder.encode(new String( parameter[2].getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try { 
			SITE_URL = URLConstance.MEMBER_CERTI_AUTH + "accessToken=" + parameter[0] +"&timestamp=" + parameter[1]
					+ "&csNm=" + contentText2 + "&juminBirthday=" + parameter[3] + "&mobileTelNo=" + parameter[4] + "&certificationTp=" + parameter[5] 
			+ "&csGender=" + parameter[6] + "&shopCd=" + Constance.SHOPCD;
							

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );

			if ( state.equals("200") ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.clear();
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
				
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
				
				chargeConstance.resultCertification	= getParameterString ( nameArray,"resultCertification");
				
				try {
					chargeConstance.csNm 				= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "csNm") );
				} catch (Exception e) {
					chargeConstance.csNm = "";
				}
				
				chargeConstance.certificationTp		= getParameterString ( nameArray, "certificationTp");
				chargeConstance.csGender 			= getParameterString ( nameArray, "csGender");
				
				
				try {
					chargeConstance.juminBirthday		= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "juminBirthday") );
				} catch (Exception e) {
					chargeConstance.juminBirthday = "";
				}
				
				try {
					chargeConstance.mobileTelNo 		= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "mobileTelNo") );
				} catch (Exception e) {
					chargeConstance.mobileTelNo = "";
				}
				
				chargeConstance.certificationNo		= getParameterString ( nameArray, "certificationNo");
				chargeConstance.errorCd				= getParameterString ( nameArray, "errorCd");
				chargeConstance.errorMsg 			= getParameterString ( nameArray, "errorMsg");
				chargeConstance.shopCd				= getParameterString ( nameArray, "shopCd");
				chargeConstance.telecomCd			= getParameterString ( nameArray, "telecomCd");
				chargeConstance.ci 					= getParameterString ( nameArray, "ci");
				chargeConstance.di					= getParameterString ( nameArray, "di");
		        
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.add(chargeConstance);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private CipherUtils mCipher = new CipherUtils();
	
	/** MEMBER_CUSTOMER_SEARCH_BUY_STORY 
	 * 
	 * 
	 * */
	public boolean getMEMBER_CUSTOMER_SEARCH_BUY_STORY( String[] parameter ) {
		
		String state = "";

		try { 
			SITE_URL = URLConstance.MEMBER_CUSTOMER_SEARCH_BUY_STORY + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] +"&currentPage=" + parameter[2]
					+ "&maxResults=" + parameter[3] +"&maxLinks=" + parameter[4]+ "&csNo=" + parameter[5];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				Constance.Page_ALL_Count	= mPageCount.getString( "countItem" );
				
				JSONArray nameArray = jsonChannels.getJSONArray("list");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.csNo				= getParameterString ( nameArray,j,"csNo");
					chargeConstance.saleDat 			= getParameterString ( nameArray, j,"saleDat");
					chargeConstance.srcNm 				= getParameterString ( nameArray,j,"srcNm");
					chargeConstance.prductCd 			= getParameterString ( nameArray, j,"prductCd");
					chargeConstance.prductNm 			= getParameterString ( nameArray,j,"prductNm");
					chargeConstance.colorC				= getParameterString ( nameArray, j,"colorC");
					chargeConstance.sizC 				= getParameterString ( nameArray,j,"sizC");
					
					chargeConstance.qty 				= getParameterString ( nameArray,j,"qty");
					chargeConstance.purchaseAmount		= getParameterString ( nameArray, j,"purchaseAmount");

					chargeConstance.accmlPoint 			= getParameterString ( nameArray,j,"accmlPoint");
					chargeConstance.usePoint			= getParameterString ( nameArray, j,"usePoint");

					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** MEMBER_CUSTOMER_SEARCH_NONUSE_CAMPAIGN_STORY 
	 * 
	 * 
	 * */
	public boolean getMEMBER_CUSTOMER_SEARCH_NONUSE_CAMPAIGN_STORY( String[] parameter ) {
		
		String state = "";

		try { 
			SITE_URL = URLConstance.MEMBER_CUSTOMER_SEARCH_NONUSE_CAMPAIGN_STORY + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] +"&currentPage=" + parameter[2]
					+ "&maxResults=" + parameter[3] +"&maxLinks=" + parameter[4]+ "&csNo=" + parameter[5];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				Constance.Page_ALL_Count	= mPageCount.getString( "countItem" );
				
				JSONArray nameArray = jsonChannels.getJSONArray("list");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.csNo				= getParameterString ( nameArray,j,"csNo");
					chargeConstance.campaignNm 			= getParameterString ( nameArray, j,"campaignNm");
					chargeConstance.startDate 			= getParameterString ( nameArray,j,"startDate");
					chargeConstance.endDate 			= getParameterString ( nameArray, j,"endDate");
					chargeConstance.applcAmount 		= getParameterString ( nameArray,j,"applcAmount");
					chargeConstance.couponNm 			= getParameterString ( nameArray,j,"couponNm");


					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** MEMBER_ZIP_CODE 
	 * 
	 * 
	 * */
	public boolean getMEMBER_ZIP_CODE( String[] parameter ) {
		
		String state = "";

		String contentText2 = "";
		try {
			contentText2 = java.net.URLEncoder.encode(new String( parameter[2].getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		try { 
			SITE_URL = URLConstance.MEMBER_ZIP_CODE + "accessToken=" + "DAA88002E4C06D2EEF7C01690A17C9E4BE721825455A973B59A006C695298A12" + "&timestamp=" + "1377221715487" +"&searchStr=" + contentText2;
//			SITE_URL = URLConstance.MEMBER_ZIP_CODE + "accessToken=" + parameter[0] + "&timestamp=" + parameter[1] +"&searchStr=" + contentText2;
			JSONObject jsonChannels = getHttpJson(SITE_URL);  

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.clear();
			if ( state.equals("200") ) {

				JSONArray nameArray = jsonChannels.getJSONArray("results");
				String mArea = "";
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					mArea = "";
					
					chargeConstance.postNo				= getZipCode ( getParameterString ( nameArray,j,"postNo").toString().trim() );
					mArea = getParameterString ( nameArray, j,"postGwa").toString().trim();
					mArea = mArea + " " + getParameterString ( nameArray, j,"postSigu").toString().trim();
					mArea = mArea + " " + getParameterString ( nameArray, j,"postDong").toString().trim();
					mArea = mArea + " " + getParameterString ( nameArray, j,"postHuge").toString().trim();
					mArea = mArea + " " + getParameterString ( nameArray, j,"postEtc").toString().trim();
					mArea = mArea + " " + getParameterString ( nameArray, j,"postFrAddr").toString().trim();
					mArea = mArea + " " + getParameterString ( nameArray, j,"postToAddr").toString().trim();
					
					chargeConstance.postArea	= mArea;
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String getZipCode ( String strnumber ){
		String mZipCode = "";
		
		for( int i = 0; i < strnumber.length(); i++){
			if( i < 3 ){
				mZipCode = mZipCode + strnumber.charAt(i);
			} else {
				if( i == 3 ){
					mZipCode = mZipCode + "-";
				}
				mZipCode = mZipCode + strnumber.charAt(i);
			}
		}
		return mZipCode;
	}
	
	
	
	/** PRODUCT_SEARCH 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getPRODUCT_SEARCH( String[] brandCd) {
		
		String state = "";
		String contentText2 = "";
		try {
			contentText2 = java.net.URLEncoder.encode(new String( brandCd[9].getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try { 
			SITE_URL = URLConstance.PRODUCT_SEARCH 
					+ "brandCd=" + brandCd[0]  + "&currentPage=" + brandCd[1]
					+ "&maxResults=" + brandCd[2] + "&maxLinks="+ brandCd[3] + "&accessToken=" + brandCd[4]+ "&timestamp="+ brandCd[5]
					+ "&classCd=" + brandCd[6] + "&sizeCd="+ brandCd[7] + "&orderTy=" + brandCd[8]+ "&prductNm="+ contentText2
					+ "&prductCd=" + brandCd[10] + "&shopCd="+ brandCd[11] + "&searchTy="+ brandCd[12];
			
			
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.clear();
			
			if ( state.equals("200") ) {
				JSONArray nameArray = jsonChannels.getJSONArray("list");
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
		            	
					chargeConstance.prductCd 	= getParameterString ( nameArray, j, "prductCd");
					chargeConstance.prductNm 	= getParameterString ( nameArray, j, "prductNm");
					chargeConstance.shopCd 		= getParameterString ( nameArray, j, "shopCd");
					chargeConstance.copr 		= getParameterString ( nameArray, j, "copr");
					chargeConstance.jegoTotqy 	= getParameterString ( nameArray, j, "jegoTotqy");
					chargeConstance.thumbUrl 	= getParameterString ( nameArray, j, "thumbUrl");
					
					chargeConstance.soldoutTy 	= getParameterString ( nameArray, j, "soldoutTy");
					chargeConstance.colorCd 			= getParameterString ( nameArray, j, "colorCd");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");
					chargeConstance.planYy 		= getParameterString ( nameArray, j, "planYy");
					chargeConstance.classCd 	= getParameterString ( nameArray, j, "classCd");
					chargeConstance.saledRate 	= getParameterString ( nameArray, j, "saledRate");
					

					chargeConstance.countTot1 	= getParameterString ( nameArray, j, "countTot1");
					chargeConstance.prductUrl 	= getParameterString ( nameArray, j, "prductUrl");
					chargeConstance.extImgUrl 	= getParameterString ( nameArray, j, "extImgUrl");
		            
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.add(chargeConstance);
				}
				
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				Constance.Page_ALL_Count	= mPageCount.getString( "countItem" );
								
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** PRODUCT_SEARCH_DETAIL 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getPRODUCT_SEARCH_DETAIL( String[] parameter ) {
		
		String state = "";
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.clear();
		
		try {
			
			SITE_URL = URLConstance.PRODUCT_SEARCH_DETAIL + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] +"&brandCd=" + parameter[2];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.classCd	= getParameterString ( nameArray,j,"classCd");
					chargeConstance.classNm 	= getParameterString ( nameArray, j,"classNm");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** PRODUCT_SEARCH_DETAIL_SIZE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getPRODUCT_SEARCH_DETAIL_SIZE( String[] parameter ) {
		
		String state = "";
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.clear();
		
		try {
			
			SITE_URL = URLConstance.PRODUCT_SEARCH_DETAIL_SIZE + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] +"&brandCd=" + parameter[2] + "&classCd=" + parameter[3];

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.sizeCd	= getParameterString ( nameArray,j,"sizeCd");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** SHOP_AREA_LIST_DETAIL 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getSHOP_AREA_LIST_DETAIL( String[] parameter ) {
		
		String state = "";
		try { 
			String contentText2 = java.net.URLEncoder.encode(new String(parameter[5].getBytes("UTF-8")));
			SITE_URL = URLConstance.SHOP_AREA_LIST_DETAIL + "accessToken=" + parameter[0]+ "&timestamp=" + parameter[1] +"&currentPage=" + parameter[2]
					+ "&maxResults=" + parameter[3] +"&maxLinks=" + parameter[4]+ "&searchNm=" + contentText2 +"&areaCd=" + parameter[6] + "&brandCd=" + Constance.BEANDCD;

			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				JSONObject mPageCount = jsonChannels.getJSONObject("pagingProperty");
				
				Constance.PageCount = mPageCount.getString( "countPage" );
				
				JSONArray nameArray = jsonChannels.getJSONArray("list");
								
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.shopSeq	= getParameterString ( nameArray,j,"shopSeq");
					chargeConstance.shopCd 	= getParameterString ( nameArray, j,"shopCd");
					chargeConstance.shopNm 	= getParameterString ( nameArray,j,"shopNm");
					chargeConstance.addr 	= getParameterString ( nameArray, j,"addr");
					chargeConstance.areaCd 	= getParameterString ( nameArray,j,"areaCd");
					chargeConstance.brandCd	= getParameterString ( nameArray, j,"brandCd");
					chargeConstance.areaNm 	= getParameterString ( nameArray,j,"areaNm");
					chargeConstance.deptNm 	= getParameterString ( nameArray, j,"deptNm");
					chargeConstance.telno 	= getParameterString ( nameArray,j,"telno");
					chargeConstance.shopTy 	= getParameterString ( nameArray, j,"shopTy");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** EDUCATION_CONTENTS 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getEDUCATION_CONTENTS( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.EDUCATION_CONTENTS + "brandCd=" + parameter[0] + "&accessToken=" + parameter[1]+ "&timestamp=" + parameter[2];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
	            
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_EDUCATION_CONTENTS.clear();
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					
					chargeConstance.ctgryId 	= getParameterString ( nameArray,j,"ctgryId");
					chargeConstance.regerNm 	= getParameterString ( nameArray, j,"regerNm");
					chargeConstance.regDttm 	= getParameterString ( nameArray, j,"regDttm");
					chargeConstance.upderId 	= getParameterString ( nameArray, j,"upderId");
					chargeConstance.upderNm 	= getParameterString ( nameArray, j,"upderNm");
	
					chargeConstance.updDttm 	= getParameterString ( nameArray, j,"updDttm");
					
					chargeConstance.ctgryNm 	= getParameterString ( nameArray, j,"ctgryNm");
					chargeConstance.ctgryDc 	= getParameterString ( nameArray, j,"ctgryDc");
	
					chargeConstance.ctgryDc 	= getParameterString ( nameArray, j,"ctgryDc");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j,"brandCd");
					chargeConstance.ctgryTyCd 	= getParameterString ( nameArray, j,"ctgryTyCd");
					chargeConstance.ctgryTyNm 	= getParameterString ( nameArray, j,"ctgryTyNm");
					chargeConstance.scrinTyCd 	= getParameterString ( nameArray, j,"scrinTyCd");
					
					chargeConstance.scrinTyNm 	= getParameterString ( nameArray, j,"scrinTyNm");
					chargeConstance.bgnDt 		= getParameterString ( nameArray, j,"bgnDt");
					chargeConstance.endDt 		= getParameterString ( nameArray, j,"endDt");
					chargeConstance.upCtgryId 	= getParameterString ( nameArray,  j,"upCtgryId");
	
	
					chargeConstance.isLeaf 		= getParameterString ( nameArray, j,"isLeaf");
					chargeConstance.expsrOrdr 	= getParameterString ( nameArray, j,"expsrOrdr");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_EDUCATION_CONTENTS.add(chargeConstance);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** HEAD_OF_LOGIN
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getHEAD_OF_LOGIN( String[] parameter ) {

		String[] arrReturnParameter = new String[2];

		try { 
			SITE_URL = URLConstance.HEAD_OF_LOGIN + "userId=" + parameter[0] + "&pwd=" + parameter[1]+ "&accessToken=" + parameter[2]+ "&timestamp=" + parameter[3];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" ); 
			if ( arrReturnParameter[0].equals("200") ) {
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
				
				Constance.userNm 	= getParameterString ( nameArray,"userNm");
				Constance.empNo 	= getParameterString ( nameArray, "empNo");

				JSONArray BrandArry = nameArray.getJSONArray("brands");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.clear();
				
				for (int j = 0; j < BrandArry.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE BrandArryConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					BrandArryConstance.brandCd 		= getParameterString ( BrandArry, j, "brandCd");
					BrandArryConstance.brandNm 		= getParameterString ( BrandArry, j, "brandNm");
					BrandArryConstance.shopCd 		= getParameterString ( BrandArry, j, "shopCd");
					BrandArryConstance.shopNm 		= getParameterString ( BrandArry, j, "shopNm");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.add(BrandArryConstance);
				}

				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	
	/** SHOP_LOGIN
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String[] getSHOP_LOGIN( String[] parameter ) {

		String[] arrReturnParameter = new String[2];

		try { 
			SITE_URL = URLConstance.SHOP_LOGIN + "userId=" + parameter[0] + "&pwd=" + parameter[1]+ "&accessToken=" + parameter[2]+ "&timestamp=" + parameter[3];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			arrReturnParameter[0] = color.getString( "statusCd" ); 
			arrReturnParameter[1] = color.getString( "statusMssage" ); 
			if ( arrReturnParameter[0].equals("200") ) {
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
				
				Constance.userNm 	= getParameterString ( nameArray,"userNm");
				Constance.empNo 	= getParameterString ( nameArray, "empNo");

				JSONArray BrandArry = nameArray.getJSONArray("brands");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.clear();
				
				for (int j = 0; j < BrandArry.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE BrandArryConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					BrandArryConstance.brandCd 		= getParameterString ( BrandArry, j, "brandCd");
					BrandArryConstance.regerNm 		= getParameterString ( BrandArry, j, "brandNm");
					BrandArryConstance.shopCd 		= getParameterString ( BrandArry, j, "shopCd");
					BrandArryConstance.shopNm 		= getParameterString ( BrandArry, j, "shopNm");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOGIN_BRAND_INFOR.add(BrandArryConstance);
				}

				return arrReturnParameter;
			} else {
				return arrReturnParameter;
			}
		} catch (Exception e) {
			arrReturnParameter[0] = "999";
			arrReturnParameter[1] = "Exception"; 
			e.printStackTrace();
			return arrReturnParameter;
		}
	}
	
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getMAIN_INFORMATIO( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.MAIN_INFORMATIO + "brandCd=" + parameter[0] + "&accessToken=" + parameter[1]+ "&timestamp=" + parameter[2];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" ); 
			
			if ( state.equals("200") ) {
				
				JSONObject nameArray = jsonChannels.getJSONObject("result");
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.clear();
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
				
				chargeConstance.regerId 	= getParameterString ( nameArray,"regerId");
				chargeConstance.regerNm 	= getParameterString ( nameArray, "regerNm");
				chargeConstance.regDttm 	= getParameterString ( nameArray, "regDttm");
				chargeConstance.upderId 	= getParameterString ( nameArray, "upderId");
				chargeConstance.upderNm 	= getParameterString ( nameArray, "upderNm");

				chargeConstance.updDttm 	= getParameterString ( nameArray, "updDttm");
				chargeConstance.no 	= getParameterString ( nameArray, "no");
				
				chargeConstance.cntntsId 	= getParameterString ( nameArray, "cntntsId");
				chargeConstance.brandCd 	= getParameterString ( nameArray,  "brandCd");
				chargeConstance.brandNm 	= getParameterString ( nameArray,"brandNm");
				chargeConstance.cntntsNm 	= getParameterString ( nameArray, "cntntsNm");

				chargeConstance.cntntsCn 	= getParameterString ( nameArray, "cntntsCn");
				chargeConstance.cntntsTyCd 	= getParameterString ( nameArray, "cntntsTyCd");
				chargeConstance.cntntsTyNm 	= getParameterString ( nameArray, "cntntsTyNm");
				chargeConstance.bgnDt 		= getParameterString ( nameArray, "bgnDt");
				chargeConstance.endDt 		= getParameterString ( nameArray, "endDt");
				
				chargeConstance.cntntsFileNm 	= getParameterString ( nameArray, "cntntsFileNm");
				chargeConstance.cntntsFileLc 	= getParameterString ( nameArray, "cntntsFileLc");
				chargeConstance.thumbFileNm 	= getParameterString ( nameArray, "thumbFileNm");
				chargeConstance.thumbFileLc 	= getParameterString ( nameArray,  "thumbFileLc");


				chargeConstance.linkUrl 	= getParameterString ( nameArray, "linkUrl");
				chargeConstance.dwldYn 	= getParameterString ( nameArray, "dwldYn");
				chargeConstance.ctgryId 		= getParameterString ( nameArray, "ctgryId");
				chargeConstance.ctgryNm 	= getParameterString ( nameArray, "ctgryNm");
				
				chargeConstance.dwldCntntsPath 		= getParameterString ( nameArray,"dwldCntntsPath");
				chargeConstance.dwldThumbPath 	= getParameterString ( nameArray, "dwldThumbPath");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.add(chargeConstance);

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getMAIN_MENU( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.MAIN_MENU + "brandCd=" + parameter[0] + "&accessToken=" + parameter[1]+ "&timestamp=" + parameter[2];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.clear();
			
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.regerId 	= getParameterString ( nameArray, j, "regerId");
					chargeConstance.regerNm 	= getParameterString ( nameArray, j, "regerNm");
					chargeConstance.regDttm 	= getParameterString ( nameArray, j, "regDttm");
					chargeConstance.upderId 	= getParameterString ( nameArray, j, "upderId");
					chargeConstance.upderNm 	= getParameterString ( nameArray, j, "upderNm");

					chargeConstance.updDttm 	= getParameterString ( nameArray, j, "updDttm");
					
					
					chargeConstance.ctgryId 	= getParameterString ( nameArray, j, "ctgryId");
					chargeConstance.ctgryNm 	= getParameterString ( nameArray, j, "ctgryNm");
					chargeConstance.ctgryDc 	= getParameterString ( nameArray, j, "ctgryDc");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");

					chargeConstance.ctgryTyCd 	= getParameterString ( nameArray, j, "ctgryTyCd");
					chargeConstance.ctgryTyNm 	= getParameterString ( nameArray, j, "ctgryTyNm");
					chargeConstance.scrinTyCd 	= getParameterString ( nameArray, j, "scrinTyCd");
					chargeConstance.bgnDt 		= getParameterString ( nameArray, j, "bgnDt");
					chargeConstance.endDt 		= getParameterString ( nameArray, j, "endDt");
					
					chargeConstance.scrinTyNm 	= getParameterString ( nameArray, j, "scrinTyNm");
					chargeConstance.upCtgryId 	= getParameterString ( nameArray, j, "upCtgryId");
					chargeConstance.isLeaf 		= getParameterString ( nameArray, j, "isLeaf");
					chargeConstance.expsrOrdr 	= getParameterString ( nameArray, j, "expsrOrdr");
					
					chargeConstance.dwldCntntsPath 	= getParameterString ( nameArray, j, "dwldCntntsPath");
					chargeConstance.dwldThumbPath 	= getParameterString ( nameArray, j, "dwldThumbPath");
					
					chargeConstance.cntntsFileNm 	= getParameterString ( nameArray, j, "cntntsFileNm");
					chargeConstance.iconNm 			= getParameterString ( nameArray, j, "iconNm");
					chargeConstance.iconTyCd 		= getParameterString ( nameArray, j, "iconTyCd");
					chargeConstance.iconCd 			= getParameterString ( nameArray,  j, "iconCd");
					
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.add(chargeConstance);
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private String getParameterString(JSONArray nameArray, int position, String mParameter){
		try {
			if( nameArray.getJSONObject(position).getString( mParameter ) == null ){
				return "null";
			} else {
				return nameArray.getJSONObject(position).getString( mParameter ).toString().trim();
			}
		} catch (Exception e) {
			return "null";
		}
	}
	
	private String getParameterString(JSONObject nameArray, String mParameter){
		try {
			if( nameArray.getString( mParameter ) == null ){
				return "null";
			} else {
				return nameArray.getString( mParameter ).toString().trim();
			}
		} catch (Exception e) {
			return "null";
		}
	}
	
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getCATEGORY_LIST( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.CATEGORY_LIST + "brandCd=" + parameter[0] + "&ctgryId=" + parameter[1]
					+ "&accessToken=" + parameter[2]+ "&timestamp=" + parameter[3];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.clear();
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.regerId 	= getParameterString ( nameArray, j, "regerId");
					chargeConstance.regerNm 	= getParameterString ( nameArray, j, "regerNm");
					chargeConstance.regDttm 	= getParameterString ( nameArray, j, "regDttm");
					chargeConstance.upderId 	= getParameterString ( nameArray, j, "upderId");
					chargeConstance.upderNm 	= getParameterString ( nameArray, j, "upderNm");

					chargeConstance.updDttm 	= getParameterString ( nameArray, j, "updDttm");
					
					
					chargeConstance.ctgryId 	= getParameterString ( nameArray, j, "ctgryId");
					chargeConstance.ctgryNm 	= getParameterString ( nameArray, j, "ctgryNm");
					chargeConstance.ctgryDc 	= getParameterString ( nameArray, j, "ctgryDc");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");

					chargeConstance.ctgryTyCd 	= getParameterString ( nameArray, j, "ctgryTyCd");
					chargeConstance.ctgryTyNm 	= getParameterString ( nameArray, j, "ctgryTyNm");
					chargeConstance.scrinTyCd 	= getParameterString ( nameArray, j, "scrinTyCd");
					chargeConstance.bgnDt 		= getParameterString ( nameArray, j, "bgnDt");
					chargeConstance.endDt 		= getParameterString ( nameArray, j, "endDt");
					
					chargeConstance.scrinTyNm 	= getParameterString ( nameArray, j, "scrinTyNm");
					chargeConstance.upCtgryId 	= getParameterString ( nameArray, j, "upCtgryId");
					chargeConstance.isLeaf 		= getParameterString ( nameArray, j, "isLeaf");
					chargeConstance.expsrOrdr 	= getParameterString ( nameArray, j, "expsrOrdr");
					chargeConstance.dwldCntntsPath 	= getParameterString ( nameArray, j, "dwldCntntsPath");
					chargeConstance.dwldThumbPath 	= getParameterString ( nameArray, j, "dwldThumbPath");
					
					chargeConstance.cntntsFileNm 	= getParameterString ( nameArray, j, "cntntsFileNm");
					chargeConstance.cntntsFileLc 	= getParameterString ( nameArray, j, "cntntsFileLc");
					chargeConstance.thumbFileNm 	= getParameterString ( nameArray, j, "thumbFileNm");
					chargeConstance.thumbFileLc 	= getParameterString ( nameArray,  j, "thumbFileLc");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.add(chargeConstance);
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public boolean getCATEGORY_LIST_SUB( String[] parameter ) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.CATEGORY_LIST + "brandCd=" + parameter[0] + "&ctgryId=" + parameter[1]
					+ "&accessToken=" + parameter[2]+ "&timestamp=" + parameter[3];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.clear();
			if ( state.equals("200") ) {
				
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.regerId 	= getParameterString ( nameArray, j, "regerId");
					chargeConstance.regerNm 	= getParameterString ( nameArray, j, "regerNm");
					chargeConstance.regDttm 	= getParameterString ( nameArray, j, "regDttm");
					chargeConstance.upderId 	= getParameterString ( nameArray, j, "upderId");
					chargeConstance.upderNm 	= getParameterString ( nameArray, j, "upderNm");

					chargeConstance.updDttm 	= getParameterString ( nameArray, j, "updDttm");
					
					
					chargeConstance.ctgryId 	= getParameterString ( nameArray, j, "ctgryId");
					chargeConstance.ctgryNm 	= getParameterString ( nameArray, j, "ctgryNm");
					chargeConstance.ctgryDc 	= getParameterString ( nameArray, j, "ctgryDc");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");

					chargeConstance.ctgryTyCd 	= getParameterString ( nameArray, j, "ctgryTyCd");
					chargeConstance.ctgryTyNm 	= getParameterString ( nameArray, j, "ctgryTyNm");
					chargeConstance.scrinTyCd 	= getParameterString ( nameArray, j, "scrinTyCd");
					chargeConstance.bgnDt 		= getParameterString ( nameArray, j, "bgnDt");
					chargeConstance.endDt 		= getParameterString ( nameArray, j, "endDt");
					
					chargeConstance.scrinTyNm 	= getParameterString ( nameArray, j, "scrinTyNm");
					chargeConstance.upCtgryId 	= getParameterString ( nameArray, j, "upCtgryId");
					chargeConstance.isLeaf 		= getParameterString ( nameArray, j, "isLeaf");
					chargeConstance.expsrOrdr 	= getParameterString ( nameArray, j, "expsrOrdr");
					chargeConstance.dwldCntntsPath 	= getParameterString ( nameArray, j, "dwldCntntsPath");
					chargeConstance.dwldThumbPath 	= getParameterString ( nameArray, j, "dwldThumbPath");
					
					chargeConstance.cntntsFileNm 	= getParameterString ( nameArray, j, "cntntsFileNm");
					chargeConstance.cntntsFileLc 	= getParameterString ( nameArray, j, "cntntsFileLc");
					chargeConstance.thumbFileNm 	= getParameterString ( nameArray, j, "thumbFileNm");
					chargeConstance.thumbFileLc 	= getParameterString ( nameArray,  j, "thumbFileLc");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.add(chargeConstance);
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/** THUMBNAIL_FILE 
	 * 
	 * @param brandCD : 브랜드 ID
	 * */
	public String getTHUMBNAIL_FILE( String[] brandCd ) {
		try {
			SITE_URL = URLConstance.THUMBNAIL_FILE_DOWN + "brandCd=" + brandCd[0] + "&cntntsId=" + brandCd[1] + "&accessToken=" + brandCd[2] + "&timestamp=" + brandCd[3];
			
			return SITE_URL;
		} catch (Exception e) {
			e.printStackTrace();
			return "null";
		}
	}
	
	/** CONTENTS_PAGE_ALL_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getCONTENTS_PAGE_ALL_LIST( String[] brandCd) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.CONTENTS_PAGE_ALL_LIST 
					+ "brandCd=" + brandCd[0] + "&ctgryId="+ brandCd[1] + "&currentPage=" + brandCd[2]
							+ "&maxResults=" + brandCd[3] + "&maxLinks="+ brandCd[4] + "&accessToken=" + brandCd[5]+ "&timestamp="+ brandCd[6];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
				
			if ( state.equals("200") ) {
				JSONArray nameArray = jsonChannels.getJSONArray("list");
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.regerId 	= getParameterString ( nameArray, j, "regerId");
					chargeConstance.regerNm 	= getParameterString ( nameArray, j, "regerNm");
					chargeConstance.regDttm 	= getParameterString ( nameArray, j, "regDttm");
					chargeConstance.upderId 	= getParameterString ( nameArray, j, "upderId");
					chargeConstance.upderNm 	= getParameterString ( nameArray, j, "upderNm");

					chargeConstance.updDttm 	= getParameterString ( nameArray, j, "updDttm");
					chargeConstance.no 			= getParameterString ( nameArray, j, "no");
					chargeConstance.cntntsId 	= getParameterString ( nameArray, j, "cntntsId");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");
					chargeConstance.cntntsNm 	= getParameterString ( nameArray, j, "cntntsNm");

					chargeConstance.cntntsCn 	= getParameterString ( nameArray, j, "cntntsCn");
					chargeConstance.cntntsTyCd 	= getParameterString ( nameArray, j, "cntntsTyCd");
					chargeConstance.cntntsTyNm 	= getParameterString ( nameArray, j, "cntntsTyNm");
					chargeConstance.bgnDt 		= getParameterString ( nameArray, j, "bgnDt");
					chargeConstance.endDt 		= getParameterString ( nameArray, j, "endDt");
					
					chargeConstance.linkUrl 	= getParameterString ( nameArray, j, "linkUrl");
					
					chargeConstance.thumbFileNm = getParameterString ( nameArray, j, "thumbFileNm");
					chargeConstance.thumbFileLc	= getParameterString ( nameArray, j, "thumbFileLc");
					chargeConstance.cntntsFileNm 	= getParameterString ( nameArray, j, "cntntsFileNm");
					chargeConstance.cntntsFileLc 	= getParameterString ( nameArray, j, "cntntsFileLc");
					
					chargeConstance.dwldYn 		= getParameterString ( nameArray, j, "dwldYn");
					chargeConstance.ctgryId 	= getParameterString ( nameArray, j, "ctgryId");
					chargeConstance.ctgryNm 	= getParameterString ( nameArray, j, "ctgryNm");

					chargeConstance.dwldCntntsPath 		= getParameterString ( nameArray, j, "dwldCntntsPath");
					chargeConstance.dwldThumbPath 	= getParameterString ( nameArray, j, "dwldThumbPath");
					chargeConstance.hasPrduct 	= getParameterString ( nameArray, j, "hasPrduct");
		            
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.add(chargeConstance);
				}
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.clear();
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeCount = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();

				JSONObject nameSub_Array = jsonChannels.getJSONObject("pagingProperty");
				
				chargeCount.countItem 			= getParameterString ( nameSub_Array, "countItem");
				chargeCount.countPage 			= getParameterString ( nameSub_Array, "countPage");
				chargeCount.maxResults 			= getParameterString ( nameSub_Array, "maxResults");
				chargeCount.maxLinks 			= getParameterString ( nameSub_Array, "maxLinks");
				chargeCount.startPage 			= getParameterString ( nameSub_Array, "startPage");
				
				chargeCount.endPage 			= getParameterString ( nameSub_Array, "endPage");
				chargeCount.currentPage 		= getParameterString ( nameSub_Array, "currentPage");
				chargeCount.previousPage 		= getParameterString ( nameSub_Array, "previousPage");
				chargeCount.nextPage 			= getParameterString ( nameSub_Array, "nextPage");
				chargeCount.startItem 			= getParameterString ( nameSub_Array, "startItem");
				
				chargeCount.endItem 			= getParameterString ( nameSub_Array, "endItem");
				chargeCount.previousLinkPage	= getParameterString ( nameSub_Array, "previousLinkPage");
				chargeCount.nextLinkPage 		= getParameterString ( nameSub_Array, "nextLinkPage");
				chargeCount.skipItems 			= getParameterString ( nameSub_Array, "skipItems");
				chargeCount.dwldCntntsPath 		= getParameterString ( nameSub_Array, "dwldCntntsPath");
				chargeCount.dwldThumbPath 		= getParameterString ( nameSub_Array, "dwldThumbPath");
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.add(chargeCount);
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** CONTENTS_PAGE_ALL_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getCONTENTS_PAGE_LIST( String[] brandCd) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.CONTENTS_PAGE_LIST
					+ "brandCd=" + brandCd[0] + "&ctgryId="+ brandCd[1] + "&currentPage=" + brandCd[2]
							+ "&maxResults=" + brandCd[3] + "&maxLinks="+ brandCd[4] + "&accessToken=" + brandCd[5]+ "&timestamp="+ brandCd[6];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
				
			if ( state.equals("200") ) {
				JSONArray nameArray = jsonChannels.getJSONArray("list");
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.regerId 	= getParameterString ( nameArray, j, "regerId");
					chargeConstance.regerNm 	= getParameterString ( nameArray, j, "regerNm");
					chargeConstance.regDttm 	= getParameterString ( nameArray, j, "regDttm");
					chargeConstance.upderId 	= getParameterString ( nameArray, j, "upderId");
					chargeConstance.upderNm 	= getParameterString ( nameArray, j, "upderNm");

					chargeConstance.updDttm 	= getParameterString ( nameArray, j, "updDttm");
					chargeConstance.no 			= getParameterString ( nameArray, j, "no");
					chargeConstance.cntntsId 	= getParameterString ( nameArray, j, "cntntsId");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");
					chargeConstance.cntntsNm 	= getParameterString ( nameArray, j, "cntntsNm");

					chargeConstance.cntntsCn 	= getParameterString ( nameArray, j, "cntntsCn");
					chargeConstance.cntntsTyCd 	= getParameterString ( nameArray, j, "cntntsTyCd");
					chargeConstance.cntntsTyNm 	= getParameterString ( nameArray, j, "cntntsTyNm");
					chargeConstance.bgnDt 		= getParameterString ( nameArray, j, "bgnDt");
					chargeConstance.endDt 		= getParameterString ( nameArray, j, "endDt");
					
					chargeConstance.linkUrl 	= getParameterString ( nameArray, j, "linkUrl");
					
					chargeConstance.cntntsFileNm 	= getParameterString ( nameArray, j, "cntntsFileNm");
					chargeConstance.cntntsFileLc 	= getParameterString ( nameArray, j, "cntntsFileLc");
					chargeConstance.thumbFileNm 	= getParameterString ( nameArray, j, "thumbFileNm");
					chargeConstance.thumbFileLc 	= getParameterString ( nameArray,  j, "thumbFileLc");
					
					chargeConstance.dwldYn 		= getParameterString ( nameArray, j, "dwldYn");
					chargeConstance.ctgryId 	= getParameterString ( nameArray, j, "ctgryId");
					
					chargeConstance.ctgryNm 	= getParameterString ( nameArray, j, "ctgryNm");
					chargeConstance.dwldCntntsPath 		= getParameterString ( nameArray, j, "dwldCntntsPath");
					chargeConstance.dwldThumbPath 		= getParameterString ( nameArray, j, "dwldThumbPath");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.add(chargeConstance);
				}
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeCount = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();

				JSONObject nameSub_Array = jsonChannels.getJSONObject("pagingProperty");
				
				chargeCount.countItem 			= getParameterString ( nameSub_Array, "countItem");
				chargeCount.countPage 			= getParameterString ( nameSub_Array, "countPage");
				chargeCount.maxResults 			= getParameterString ( nameSub_Array, "maxResults");
				chargeCount.maxLinks 			= getParameterString ( nameSub_Array, "maxLinks");
				chargeCount.startPage 			= getParameterString ( nameSub_Array, "startPage");
				
				chargeCount.endPage 			= getParameterString ( nameSub_Array, "endPage");
				chargeCount.currentPage 		= getParameterString ( nameSub_Array, "currentPage");
				chargeCount.previousPage 		= getParameterString ( nameSub_Array, "previousPage");
				chargeCount.nextPage 			= getParameterString ( nameSub_Array, "nextPage");
				chargeCount.startItem 			= getParameterString ( nameSub_Array, "startItem");
				
				chargeCount.endItem 			= getParameterString ( nameSub_Array, "endItem");
				chargeCount.previousLinkPage	= getParameterString ( nameSub_Array, "previousLinkPage");
				chargeCount.nextLinkPage 		= getParameterString ( nameSub_Array, "nextLinkPage");
				chargeCount.skipItems 			= getParameterString ( nameSub_Array, "skipItems");
				chargeCount.dwldCntntsPath 		= getParameterString ( nameSub_Array, "dwldCntntsPath");
				chargeCount.dwldThumbPath 		= getParameterString ( nameSub_Array, "dwldThumbPath");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.add(chargeCount);
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/** CONTENTS_INFORMATION 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getCONTENTS_INFORMATION( String[] brandCd) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.CONTENTS_INFORMATION + "brandCd=" + brandCd[0] + "&cntntsId=" + brandCd[1] + "&accessToken=" + brandCd[2] + "&timestamp=" + brandCd[3];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
				
			if ( state.equals("200") ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.clear();
				JSONObject mResult = jsonChannels.getJSONObject("result");
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeContentsInfor = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
				
				chargeContentsInfor.regerId 			= getParameterString ( mResult, "regerId");
				chargeContentsInfor.regerNm 			= getParameterString ( mResult, "regerNm");
				chargeContentsInfor.regDttm 			= getParameterString ( mResult, "regDttm");
				chargeContentsInfor.upderId 			= getParameterString ( mResult, "upderId");
				chargeContentsInfor.upderNm 			= getParameterString ( mResult, "upderNm");
				
				chargeContentsInfor.updDttm 			= getParameterString ( mResult, "updDttm");
				chargeContentsInfor.cntntsId 			= getParameterString ( mResult, "cntntsId");
				chargeContentsInfor.brandCd 			= getParameterString ( mResult, "brandCd");
				chargeContentsInfor.cntntsNm 			= getParameterString ( mResult, "cntntsNm");
				chargeContentsInfor.cntntsCn 			= getParameterString ( mResult, "cntntsCn");
				
				chargeContentsInfor.cntntsTyCd 			= getParameterString ( mResult, "cntntsTyCd");
				chargeContentsInfor.cntntsTyNm			= getParameterString ( mResult, "cntntsTyNm");
				chargeContentsInfor.bgnDt 				= getParameterString ( mResult, "bgnDt");
				chargeContentsInfor.endDt 				= getParameterString ( mResult, "endDt");
				
				chargeContentsInfor.cntntsFileNm 		= getParameterString ( mResult, "cntntsFileNm");
				chargeContentsInfor.cntntsFileLc 		= getParameterString ( mResult, "cntntsFileLc");
				chargeContentsInfor.thumbFileNm 		= getParameterString ( mResult, "thumbFileNm");
				chargeContentsInfor.thumbFileLc 		= getParameterString ( mResult, "thumbFileLc");
				chargeContentsInfor.dwldYn 				= getParameterString ( mResult, "dwldYn");
				chargeContentsInfor.linkUrl 			= getParameterString ( mResult, "linkUrl");
				chargeContentsInfor.dwldCntntsPath 		= getParameterString ( mResult, "dwldCntntsPath");
				chargeContentsInfor.dwldThumbPath 		= getParameterString ( mResult, "dwldThumbPath");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.add(chargeContentsInfor);

//			    
//				JSONArray nameArray = mResult.getJSONArray("prductInfos");
//				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PRUDUCT_INFORMATION.clear();
//				for (int j = 0; j < nameArray.length(); j++) {
//					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
//					
//					chargeConstance.regerId 	= getParameterString ( nameArray, j, "regerId");
//					chargeConstance.regerNm 	= getParameterString ( nameArray, j, "regerNm");
//					chargeConstance.regDttm 	= getParameterString ( nameArray, j, "regDttm");
//					chargeConstance.upderId 	= getParameterString ( nameArray, j, "upderId");
//					chargeConstance.upderNm 	= getParameterString ( nameArray, j, "upderNm");
//
//					chargeConstance.updDttm 	= getParameterString ( nameArray, j, "updDttm");
//					chargeConstance.cntntsId 	= getParameterString ( nameArray, j, "cntntsId");
//					chargeConstance.prductCd 	= getParameterString ( nameArray, j, "prductCd");
//					chargeConstance.prductNm 	= getParameterString ( nameArray, j, "prductNm");
//
//					chargeConstance.expsrOrdr 	= getParameterString ( nameArray, j, "expsrOrdr");
//					chargeConstance.dwldCntntsPath 		= getParameterString ( nameArray, j, "dwldCntntsPath");
//					chargeConstance.dwldThumbPath 		= getParameterString ( nameArray, j, "dwldThumbPath");
//					
//					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PRUDUCT_INFORMATION.add(chargeConstance);
//				}
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	/** CONTENTS_PAGE_ALL_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getPRODUCT_LIST_INFORMATION( String[] brandCd) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.PRODUCT_LIST_INFORMATION 
					+ "brandCd=" + brandCd[0] + "&prductCd="+ brandCd[1] + "&shopCd=" + brandCd[2]
								+ "&accessToken=" + brandCd[3]+ "&timestamp="+ brandCd[4];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
				
			if ( state.equals("200") ) {
				
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeCount = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();

				JSONObject nameSub_Array = jsonChannels.getJSONObject("result");
				
				chargeCount.prductItem 			= getParameterString ( nameSub_Array, "prductItem");
				chargeCount.prductCd 			= getParameterString ( nameSub_Array, "prductCd");
				chargeCount.prductNm 			= getParameterString ( nameSub_Array, "prductNm");
				chargeCount.shopCd 				= getParameterString ( nameSub_Array, "shopCd");
				chargeCount.shopNm 				= getParameterString ( nameSub_Array, "shopNm");
				
				chargeCount.prductDesc 			= getParameterString ( nameSub_Array, "prductDesc");
				chargeCount.rspr 				= getParameterString ( nameSub_Array, "rspr");
				chargeCount.copr 				= getParameterString ( nameSub_Array, "copr");
				chargeCount.matt 				= getParameterString ( nameSub_Array, "matt");
				chargeCount.maker 				= getParameterString ( nameSub_Array, "maker");
				
				chargeCount.origNm 				= getParameterString ( nameSub_Array, "origNm");
				chargeCount.brandCd				= getParameterString ( nameSub_Array, "brandCd");
				chargeCount.planYy 				= getParameterString ( nameSub_Array, "planYy");
				chargeCount.colorCd 			= getParameterString ( nameSub_Array, "colorCd");
				chargeCount.pumCd 				= getParameterString ( nameSub_Array, "pumCd");
				chargeCount.repPrductCd 		= getParameterString ( nameSub_Array, "repPrductCd");

				chargeCount.jegoTotqy 			= getParameterString ( nameSub_Array, "jegoTotqy");
				chargeCount.otherTotqy 			= getParameterString ( nameSub_Array, "otherTotqy");
				chargeCount.whQty 				= getParameterString ( nameSub_Array, "whQty");
				
				chargeCount.thumbUrl 			= getParameterString ( nameSub_Array, "thumbUrl");
				chargeCount.prductUrl 			= getParameterString ( nameSub_Array, "prductUrl");
				chargeCount.extImgUrl 			= getParameterString ( nameSub_Array, "extImgUrl");
				
				try {
					JSONArray top3prdArray = nameSub_Array.getJSONArray("top3prd");
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_TOP3.clear();
					
					for (int j = 0; j < top3prdArray.length(); j++) {
						CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
						
						chargeConstance.thumbUrl 	= getParameterString ( top3prdArray, j, "thumbUrl");
						chargeConstance.top3Cp 	= getParameterString ( top3prdArray, j, "top3Cp");
						chargeConstance.prductCd 	= getParameterString ( top3prdArray, j, "prductCd");
		                
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_TOP3.add(chargeConstance);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.clear();
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.add(chargeCount);
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_COLOR.clear();
				
				try {
					JSONArray nameArray = nameSub_Array.getJSONArray("colors");
					
					for (int j = 0; j < nameArray.length(); j++) {
						CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
						
						chargeConstance.colorCd 	= getParameterString ( nameArray, j, "colorCd");
						chargeConstance.repPrductCd = getParameterString ( nameArray, j, "repPrductCd");
						chargeConstance.prductCd 	= getParameterString ( nameArray, j, "prductCd");
						chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");
						chargeConstance.colorUrl 	= getParameterString ( nameArray, j, "colorUrl");
						chargeConstance.planYy 		= getParameterString ( nameArray, j, "planYy");
		                
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_COLOR.add(chargeConstance);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.clear();
				
				try {
					JSONArray nameArray_1 = nameSub_Array.getJSONArray("invents");

					for (int j = 0; j < nameArray_1.length(); j++) {
						CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
						
						chargeConstance.shopCd 		= getParameterString ( nameArray_1, j, "shopCd");
						chargeConstance.prductCd 	= getParameterString ( nameArray_1, j, "prductCd");
						chargeConstance.sizeCd 		= getParameterString ( nameArray_1, j, "sizeCd");
						chargeConstance.jegoQty 	= getParameterString ( nameArray_1, j, "jegoQty");
						chargeConstance.otherQty 	= getParameterString ( nameArray_1, j, "otherQty");
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.add(chargeConstance);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/** CONTENTS_PAGE_ALL_LIST 
	 * 
	 * @param brandCD[0] : 브랜드 ID
	 * @param brandCD[1] : ctgryId
	 * @param brandCD[2] : currentPage
	 * @param brandCD[3] : maxResults
	 * @param brandCD[4] : maxLinks
	 * @param brandCD[5] : accessToken
	 * @param brandCD[6] : timestamp
	 * 
	 * */
	public boolean getPRODUCT_LIST( String[] brandCd) {
		
		String state = "";
		try { 
			SITE_URL = URLConstance.PRODUCT_LIST
					+ "brandCd=" + brandCd[0] + "&cntntsId="+ brandCd[1] + "&shopCd=" + brandCd[2]
								+ "&accessToken=" + brandCd[3]+ "&timestamp="+ brandCd[4];
			
			JSONObject jsonChannels = getHttpJson(SITE_URL); 

			JSONObject color = jsonChannels.getJSONObject("status");
			state = color.getString( "statusCd" );
				
			if ( state.equals("200") ) {
				JSONArray nameArray = jsonChannels.getJSONArray("results");
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.clear();
				
				for (int j = 0; j < nameArray.length(); j++) {
					CONTENTS_PAGE_ALL_LIST_CONSTANCE chargeConstance = new CONTENTS_PAGE_ALL_LIST_CONSTANCE();
					
					chargeConstance.cntntsId 	= getParameterString ( nameArray, j, "cntntsId");
					chargeConstance.prductItem 	= getParameterString ( nameArray, j, "prductItem");
					chargeConstance.prductCd 	= getParameterString ( nameArray, j, "prductCd");
					chargeConstance.prductNm 	= getParameterString ( nameArray, j, "prductNm");
					chargeConstance.expsrOrdr 	= getParameterString ( nameArray, j, "expsrOrdr");
					chargeConstance.shopCd 		= getParameterString ( nameArray, j, "shopCd");
					
					chargeConstance.shopNm 		= getParameterString ( nameArray, j, "shopNm");
					chargeConstance.prductDesc 	= getParameterString ( nameArray, j, "prductDesc");
					chargeConstance.rspr 		= getParameterString ( nameArray, j, "rspr");
					chargeConstance.copr 		= getParameterString ( nameArray, j, "copr");
					chargeConstance.matt 		= getParameterString ( nameArray, j, "matt");
					chargeConstance.maker 		= getParameterString ( nameArray, j, "maker");

					chargeConstance.origNm 		= getParameterString ( nameArray, j, "origNm");
					chargeConstance.brandCd 	= getParameterString ( nameArray, j, "brandCd");
					chargeConstance.planYy 		= getParameterString ( nameArray, j, "planYy");
					chargeConstance.colorCd 	= getParameterString ( nameArray, j, "colorCd");
					chargeConstance.pumCd 		= getParameterString ( nameArray, j, "pumCd");
					chargeConstance.repPrductCd = getParameterString ( nameArray, j, "repPrductCd");

					chargeConstance.jegoTotqy 	= getParameterString ( nameArray, j, "jegoTotqy");
					chargeConstance.otherTotqy 	= getParameterString ( nameArray, j, "otherTotqy");
					chargeConstance.whQty 		= getParameterString ( nameArray, j, "whQty");
					
					chargeConstance.colors 		= getParameterString ( nameArray, j, "colors");
					chargeConstance.invents 	= getParameterString ( nameArray, j, "invents");
					chargeConstance.thumbUrl 	= getParameterString ( nameArray, j, "thumbUrl");
					chargeConstance.prductUrl 	= getParameterString ( nameArray, j, "prductUrl");
					chargeConstance.extImgUrl 	= getParameterString ( nameArray, j, "extImgUrl");

					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.add(chargeConstance);
				}				
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

	
//	PCM다운로드 : /RequestPCM.aspx?PCM=[PCM 파일이름]
	
	private static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		if( is != null ){
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	public static JSONObject getHttpJson(String url) {
		JSONObject json = null;
		try {
			String result = libRequest(url);
			if( result != null ){
				json = new JSONObject(result);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static String getHttpJson_String(String url) {
		String result = "";
		try {
			result = libRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getHttp(String url) {
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();

		// TimeOut ?占쎌젙
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 15000);
		HttpConnectionParams.setSoTimeout(params, 15000);

		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				result = convertStreamToString(instream);
				instream.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	static HttpURLConnection http = null;
	
	public static void NetworkDisconnect(){
		try {
			http.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String libRequest(String url ) {
		InputStream tmp = null;
		String result = "";
		URL m_sConnectUrl = null;
		http = null;
		
		try {
			m_sConnectUrl = new URL( url );
			http = (HttpURLConnection) m_sConnectUrl.openConnection();

			http.setDefaultUseCaches(false);
			// OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
			http.setDoOutput(false);

			// InputStream으로 서버로 부터 응답을 받겠다는 옵션.
			http.setDoInput(true);
			http.setReadTimeout(30000);
			http.setConnectTimeout(30000);

			http.setRequestMethod("GET");
//			http.setRequestProperty("connect-type",
//					"application/x-www-urlencoded");
			// 서버 Response Data를 xml 형식의 타입으로 요청.
			http.setRequestProperty("Accept", "application/xml");
			 
			// 서버 Response Data를 JSON 형식의 타입으로 요청.
			http.setRequestProperty("Accept", "application/json");
			 
			// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 text/html로 서버에 전달.)
			http.setRequestProperty("Content-Type", "text/html");
			 
			// 타입설정(text/html) 형식으로 전송 (Request Body 전달시 application/xml로 서버에 전달.)
			http.setRequestProperty("Content-Type", "application/xml");
			 
			// 타입설정(application/json) 형식으로 전송 (Request Body 전달시 application/json로 서버에 전달.)
			http.setRequestProperty("Content-Type", "application/json");
			
			
			KumaLog.LogD("url : " + url);
			
			tmp = http.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			result = convertStreamToString(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String mMessage = http.getResponseMessage();
			
			if( mMessage.trim().equals("OK")){
				URLConstance.SOCKETSTATE = true;
			} else {
				URLConstance.SOCKETSTATE = false;
			}
			
			KumaLog.LogW("mMessage : " + mMessage);
			
		} catch (Exception e) {
			URLConstance.SOCKETSTATE = false;
			e.printStackTrace();
		}
		try {
			if( tmp != null ){
				tmp.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			http.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		KumaLog.LogW("result : " + result);

		return result;
	}
}
