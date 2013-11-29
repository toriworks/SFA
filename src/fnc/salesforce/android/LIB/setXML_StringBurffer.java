package fnc.salesforce.android.LIB;

import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.OZVIEWER_CONSTANCE;

public class setXML_StringBurffer {
	
	public static StringBuffer setXML(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<ecardSubscription>");
		
		String mcardNo = "2154" + OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).crd1 + OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).crd2
				+ OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).crd3; 
		
		buffer.append("<cardNo>").append( mcardNo ).append( "</cardNo>");
		buffer.append("<csNm>").append( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).name ).append( "</csNm>");
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).gender.equals("0") ){
			buffer.append("<csGender>").append("1").append( "</csGender>");
		} else {
			buffer.append("<csGender>").append("2").append( "</csGender>");
		}
		
		buffer.append("<pwd>").append( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).pwd ).append( "</pwd>");
		
		String mMobileTelNo = OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).hp1 + OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).hp2 
				+ OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).hp3;
		
		buffer.append("<mobileTelNo>").append( mMobileTelNo ).append( "</mobileTelNo>");
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).hp_cp.equals("0") ){
			buffer.append("<telecomCd>").append("01").append( "</telecomCd>");
		} else if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).hp_cp.equals("1") ){
			buffer.append("<telecomCd>").append("02").append( "</telecomCd>");
		} else {
			buffer.append("<telecomCd>").append("03").append( "</telecomCd>");
		}
		
		String mJuminBirthDay = OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).birth_dt.replace("-", "");

		buffer.append("<juminBirthday>").append( mJuminBirthDay ).append( "</juminBirthday>");
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).msg_yn.equals("0") ){
			buffer.append("<smsReceiveAgree>").append("1").append( "</smsReceiveAgree>");
		} else {
			buffer.append("<smsReceiveAgree>").append("0").append( "</smsReceiveAgree>");
		}
		
		String mZipCode = OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).zip.replace("-", "");
		
		buffer.append("<zipcode>").append( mZipCode ).append( "</zipcode>");
		
		buffer.append("<addr>").append( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).address ).append( "</addr>");
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).mgr_yn.equals("0") ){
			buffer.append("<marriage>").append("1").append( "</marriage>");
		} else {
			buffer.append("<marriage>").append("0").append( "</marriage>");
		}
		
		String mMarriageAnniversary = OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).mgr_dt.replace("-", "");

		buffer.append("<marriageAnniversary>").append( mMarriageAnniversary ).append( "</marriageAnniversary>");
		
		String mEmail = "";
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).email1.length() > 0 && OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).email2.length() > 0) {
			mEmail = OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).email1 + "@" + OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).email2;
		} else {
			mEmail = "";
		}
		
		buffer.append("<email>").append( mEmail ).append( "</email>");
				
		String mBirthday = OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).rbrth_dt.replace("-", "");
		
		buffer.append("<birthday>").append( mBirthday ).append( "</birthday>");
		
		buffer.append("<telNo1>").append( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).tel1 ).append( "</telNo1>");
		
		buffer.append("<telNo2>").append( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).tel2 ).append( "</telNo2>");
		buffer.append("<telNo3>").append( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).tel3 ).append( "</telNo3>");
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).agr1_yn.equals("0") ){
			buffer.append("<privateInformationAgree>").append("1").append( "</privateInformationAgree>");
		} else {
			buffer.append("<privateInformationAgree>").append("0").append( "</privateInformationAgree>");
		}
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).agr2_yn.equals("0") ){
			buffer.append("<thirdInformationAgree>").append("1").append( "</thirdInformationAgree>");
		} else {
			buffer.append("<thirdInformationAgree>").append("0").append( "</thirdInformationAgree>");
		}
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).solar_yn.equals("0") ){
			buffer.append("<calendarTp>").append("1").append( "</calendarTp>");
		} else {
			buffer.append("<calendarTp>").append("0").append( "</calendarTp>");
		}
		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).mail_yn.equals("0") ){
			buffer.append("<dmReceipt>").append("1").append( "</dmReceipt>");
		} else {
			buffer.append("<dmReceipt>").append("0").append( "</dmReceipt>");
		}
				
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).zip_type.equals("0") ){
			buffer.append("<emailReceipt>").append("1").append( "</emailReceipt>");
		} else {
			buffer.append("<emailReceipt>").append("0").append( "</emailReceipt>");
		}

//<ecardSubscription>
//<cardNo>2154210161927900</cardNo>
//<csNm>박재성</csNm>
//<csGender>1</csGender>
//<pwd>5242</pwd>
//<mobileTelNo>01033695508</mobileTelNo>
//<telecomCd>03</telecomCd>
//<juminBirthday>19830914</juminBirthday>
//<smsReceiveAgree>0</smsReceiveAgree>
//<zipcode>152717</zipcode>
//<addr>서울 구로구 개봉1동 한영신학대학교    </addr>
//<marriage>0</marriage>
//<marriageAnniversary>20130912</marriageAnniversary>
//<email></email>
//<birthday>20131012</birthday>
//<telNo1></telNo1><telNo2></telNo2><telNo3></telNo3>
//<privateInformationAgree>1</privateInformationAgree>
//<thirdInformationAgree>1</thirdInformationAgree>
//<calendarTp>0</calendarTp><
//dmReceipt>0</dmReceipt>
//<emailReceipt>1</emailReceipt><
//addrDiv>2</addrDiv><
//<certificationDate>20131112</certificationDate>
//<shopCd>6J1849</shopCd>
//<certificationTp>002</certificationTp>
//<ci>+K+XjCw+Ui7XK0O22HGoKJtlEmaMxgO6D0KaEYYZBEpNptLHdksNON7cfWXYlfe8fRFB9jRvgrFN9y2zUTPl/Q==</ci>
//<di>MC0GCCqGSIb3DQIJAyEAL90E3+1fSgRDcrVDJT1oeRkELRf8f/utvEfnbi58gEg=</di>
//<errorCd>00</errorCd
//><loginID>dev1</loginID>
//<certificationYn>1</certificationYn>
//<certificationResult>1000</certificationResult>
//<nowDate>1000</nowDate>
//<signTp>1</signTp
//></ecardSubscription>

		
		if( OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).email_yn.equals("0") ){
			buffer.append("<addrDiv>").append("1").append( "</addrDiv>");
		} else {
			buffer.append("<addrDiv>").append("2").append( "</addrDiv>");
		}
		
		buffer.append("<imageRedactPath>").append( OZVIEWER_CONSTANCE.mImageRedactPath ).append( "</imageRedactPath>");
		buffer.append("<imageOriginalPath>").append( OZVIEWER_CONSTANCE.mImageOriginalPath ).append( "</imageOriginalPath>");
		buffer.append("<certificationDate>").append( OZVIEWER_CONSTANCE.mCertificationDate ).append( "</certificationDate>");
		
		buffer.append("<shopCd>").append( Constance.SHOPCD ).append( "</shopCd>");
		
		if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).certificationTp.equals("1") ){
			buffer.append("<certificationTp>").append("002").append( "</certificationTp>");
			buffer.append("<ci>").append( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).ci ).append( "</ci>");
			buffer.append("<di>").append( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).di ).append( "</di>");
			buffer.append("<errorCd>").append( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).errorCd ).append( "</errorCd>");
		} else {
			buffer.append("<certificationTp>").append("008").append( "</certificationTp>");
			buffer.append("<errorCd>").append( " " ).append( "</errorCd>");
			buffer.append("<ci>").append(" ").append( "</ci>");
			buffer.append("<di>").append(" ").append( "</di>");
		}
		
		buffer.append("<loginID>").append( Constance.USER_ID).append( "</loginID>");
		buffer.append("<certificationYn>").append("1").append( "</certificationYn>");
		buffer.append("<certificationResult>").append("1000").append( "</certificationResult>");
		
		buffer.append("<nowDate>").append(OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.get(0).now_date).append( "</nowDate>");
		buffer.append("<signTp>").append("1").append( "</signTp>");
		
		
		buffer.append("</ecardSubscription>");
		
		return buffer;
	}

}
