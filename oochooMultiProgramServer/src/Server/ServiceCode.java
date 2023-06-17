package Server;

public class ServiceCode {
	public static final String LOGIN = "[로그인&회원가입 서버]>>> ";
	public static final String BASEBALLGAME = "[야구게임 서버]>>> ";
	public static final String CHATTING = "[채팅 서버]>>> ";
	public static final String CONTACTS = "[연락처 서버]>>> ";
	
	public static final int LOGIN_PORT = 6001;
	public static final int BASEBALLGAME_PORT = 6002;
	public static final int CHATTING_PORT = 6003;
	public static final int CONTACTS_PORT = 6004;
	
	public static final int LOGIN_REQUEST = 101;
	public static final int LOGIN_ACCEPT = 102;
	public static final int LOGIN_SEND = 103;
	public static final int LOGIN_NO_ID = 104;
	public static final int LOGIN_NO_PW = 105;
	public static final int LOGIN_SUCCESS = 106;
	
	public static final int SIGNUP_SEND = 201;
	public static final int SIGNUP_EXIST_ID = 202;
	public static final int SIGNUP_EXIST_EMAIL = 203;
	public static final int SIGNUP_SUCCESS = 204;
	
	public static final int BASEBALLGAME_REQUEST = 301;
	public static final int BASEBALLGAME_ACCEPT = 302;
	public static final int BASEBALLGAME_SEND = 303;
	public static final int BASEBALLGAME_RESTART = 304;
	public static final int BASEBALLGAME_RESET = 305;
	
	public static final int CHATTING_REQUEST = 401;
	public static final String CHATTING_ENTER = "CODE$402";
	public static final int CHATTING_EXIT = 403;
	public static final String CHATTING_QUIT = "CODE$404";
	public static final int CHATTING_WHISPER = 405;
	public static final int CHATTING_SEND = 406;
	public static final String CHATTING_MSG = "CODE$407";
	public static final String CHATTING_EXISTID = "CODE$408";
	public static final String CHATTING_WRONGID = "CODE$409";
	public static final String CHATTING_WHISPER_MSG = "CODE$410";
	
	public static final int CONTACTS_REQUEST_FILE = 501;
	public static final int CONTACTS_REQUEST_DB = 502;
	public static final int CONTACTS_ACCEPT = 503;
	public static final int CONTACTS_EXIT = 504;
	public static final int CONTACTS_QUIT = 505;
	public static final int CONTACTS_ADD = 506;
	public static final int CONTACTS_MODIFY = 507;
	public static final int CONTACTS_DELETE = 508;
	public static final int CONTACTS_SUCCESS = 509;
	public static final int CONTACTS_ALL = 510;
	public static final int CONTACTS_SEARCH_BYGROUP = 511;
	public static final int CONTACTS_SEARCH_BYNAME = 512;
	public static final int CONTACTS_SEARCH_BYTEL = 513;
	public static final int CONTACTS_SEARCH_BYADDR = 514;
	public static final String CONTACTS_RESULT = "CODE$515";
	public static final String CONTACTS_NODATA = "CODE$516";
}