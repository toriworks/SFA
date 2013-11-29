package fnc.salesforce.android.Constance;

public class URLConstance {
	
	public static boolean SOCKETSTATE = false;
	
//	public static String SERVERURL = "http://172.20.60.217:8080/";
//	public static String SERVERURL = "http://203.225.253.171:8080/";
	
//	public static String SERVERURL = "http://203.225.255.146:8081/BenitFnc/";     // Test
	
	public static String SERVERURL = "http://msalesforce.kolon.com/";		// 운영

//	GET 방식

	// 상품조회 리스트
	public static String PRODUCT_SEARCH = SERVERURL +"rest/shopinfo/pagedPrductQuery?";
	// 상품조회 리스트
	public static String PRODUCT_SEARCH_DETAIL = SERVERURL +"rest/shopinfo/getPrductQueryClass?";
	// 상품조회 리스트
	public static String PRODUCT_SEARCH_DETAIL_SIZE = SERVERURL +"rest/shopinfo/getPrductQuerySize?";	
	
	// 수선정보 수선 상품 리스트
	public static String REPAIR_GOODS_LIST = SERVERURL +"rest/repairinfo/listRepairGoods?";
	
	// 수선정보 수선 품종 리스트
	public static String REPAIR_SPECIES_LIST = SERVERURL +"rest/repairinfo/listRepairSpecies?";
	// 수선정보_수선단가표리스트
	public static String REPAIR_RESULT_LIST = SERVERURL +"rest/repairinfo/pagedRepairInfo?";
	
	// 수선정보_전후이미지_품종
	public static String REPAIR_IMAGE_SPECIES_LIST = SERVERURL +"rest/repairinfo/listRepairImageSpecies?";
	
	// 수선정보_전후이미지_항목(A/S)
	public static String REPAIR_IMAGE_ITEN_LIST = SERVERURL +"rest/repairinfo/listRepairImageItem?";
	
	// 수선정보_이미지리스트
	public static String REPAIR_IMAGE_RESULT_LIST = SERVERURL +"rest/repairinfo/pagedRepairImageList?";

	// 멤버십_회원조회
	public static String MEMBER_CUSTOMER_SEARCH = SERVERURL +"rest/membership/pagedCustomerSearch?";
	
	// 멤버십_회원 상세조회
	public static String MEMBER_CUSTOMER_SEARCH_DETAIL = SERVERURL +"rest/membership/getCsDetailInfo?";	

	// 멤버십_회원 구매내역
	public static String MEMBER_CUSTOMER_SEARCH_BUY_STORY = SERVERURL +"rest/membership/pagePurchaseHistory?";

	// 멤버십_회원 미사용 캠페인정보
	public static String MEMBER_CUSTOMER_SEARCH_NONUSE_CAMPAIGN_STORY = SERVERURL +"rest/membership/pagedunusedCampaignInfo?";

	// 멤버십_회원 미사용 캠페인정보
	public static String MEMBER_CUSTOMER_SEARCH_PASSWORD_CHECK = SERVERURL +"rest/membership/getCheckCsPassword?";
	
	// 멤버십_회원 미사용 캠페인정보
	public static String MEMBER_MOBILE_ENTRY = SERVERURL +"rest/membership/getMobileMembership?";	

	// 멤버십_본인인증
	public static String MEMBER_CERTI_AUTH = SERVERURL +"rest/membership/getCsCertification?";

	// 멤버십_본인인증
	public static String MEMBER_ZIP_CODE = SERVERURL +"rest/membership/pagedZipcodeSearch?";
	
	
	
	
	
	
	
	// 메뉴 목록조회( 전 체 )
	public static String MAIN_MENU = SERVERURL +"rest/cntntsmgmt/listCntntsMenu?";
	// 교육 컨텐츠 메뉴 목록조회
	public static String EDUCATION_CONTENTS = SERVERURL +"rest/cntntsmgmt/listEdcMenu?";
	// 기본 컨텐츠메뉴 목록조회
	public static String DEFAULT_CONTENTS = SERVERURL +"rest/cntntsmgmt/listBaseCntntsMenu?brandCd=";
	// 메인 로고파일 다운로드
	public static String MAIN_LOGO = SERVERURL +"rest/cntntsmgmt/dwldMainLogoFile?";
	// 메인 정보 조회
	public static String MAIN_INFORMATIO = SERVERURL +"rest/cntntsmgmt/getMainInfo?";
	// 메인 파일 다운로드
	public static String MAIN_FILE_DOWN = SERVERURL +"rest/cntntsmgmt/dwldMainCntntsFile?brandCd=";
	// 썸네일 파일 다운로드
	public static String THUMBNAIL_FILE_DOWN = SERVERURL +"rest/cntntsmgmt/dwldThumbFile?";
	// 인증 조회
	public static String AUTH_INFORMATION = SERVERURL +"rest/crtfcmgmt/getCrtfcInfo?userId=";
	// 카테고리 목록 조회
	public static String CATEGORY_LIST = SERVERURL +"rest/cntntsmgmt/listCtgryInfoData?";
	// 콘텐츠 정보 조회
	public static String CONTENTS_INFORMATION = SERVERURL +"rest/cntntsmgmt/getCntntsInfo?";
	// 콘텐츠 파일 다운로드
	public static String CONTENTS_FILE_DOWNLOAD = SERVERURL +"rest/cntntsmgmt/dwldCntntsFile?";
	// 콘텐츠 페이지 전체 목록 조회ㅏ
	public static String CONTENTS_PAGE_ALL_LIST = SERVERURL +"rest/cntntsmgmt/pagedListAllCntntsInfo?";
	// 콘텐츠 페이지 목록 조회										  rest/cntntsmgmt/pagedListAllCntntsInfo?
	public static String CONTENTS_PAGE_LIST = SERVERURL +"rest/cntntsmgmt/pagedListCntntsInfo?";

	// 매장 정보 리스트
	public static String SHOP_AREA_LIST = SERVERURL +"rest/shopinfo/listArea?";
	// 매장 정보 지역 상세
	public static String SHOP_AREA_LIST_DETAIL = SERVERURL +"rest/shopinfo/pagedListShopInfo?";	
	
	
	// 제품 목록 조회
	public static String PRODUCT_LIST = SERVERURL +"rest/shopinfo/listPrduct?";
	
	// 제품 정보 조회
	public static String PRODUCT_LIST_INFORMATION = SERVERURL +"rest/shopinfo/getPrduct?";	
	
	// 매장 로그인
	public static String SHOP_LOGIN = SERVERURL +"rest/loginaction/getShopLoginInfo?";
	
	// 본사 로그인
	public static String HEAD_OF_LOGIN = SERVERURL +"rest/loginaction/getHedOfcLoginInfo?";
	
	// 디바이스 제거
	public static String REMOVE_DEVICE = SERVERURL +"rest/devicemgmt/removeDeviceInfo?";

//	POST 방식
	// 본사 디바이스 최초 등록
	public static String HEAD_OF_DEVICE_INPUT = SERVERURL +"rest/devicemgmt/addHedOfcDeviceInfo?";
	// 매장  디바이스 최초 등록
	public static String SHOP_DEVICE_INPUT = SERVERURL +"rest/devicemgmt/addShopDeviceInfo?";

	// 업로드
	public static String CUTOMER_FILE_UPLOAD_ORIGINAL = SERVERURL +"rest/membership/ecardmemberOriginal?";
	
	// 업로드
	public static String CUTOMER_FILE_UPLOAD_MASK = SERVERURL +"rest/membership/ecardmemberRedant?";

	// 업로드
	public static String CUTOMER_INSERT = SERVERURL +"rest/membership/ecardmemberXml?";
	
	
	
//	PUT 방식
	// 매장 최초등록시 FA 정보수정
	public static String FA_DEVICE_INPUT = SERVERURL +"rest/devicemgmt/modifyFaDeviceInfo?";
	
	
	public static String VERSION_INFOR = "http://m.kolon.com:8080/business/app/servAppVer.do?appNm=mSalesForceA";
	
	public static String NEW_FILE = "http://m.kolon.com:8080/business/app/servAppUrl.do?appNm=mSalesForceA";
		
	
	
	
} 

 