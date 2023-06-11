package Client;

import java.util.regex.Pattern;

public class CheckLetters {
	
	/**
	 * 아이디 형식 검사
	 * 영문(소대문자), 숫자만 (특수문자 안됨)
	 * 3~10글자
	 * @param id
	 * @return 올바르지 않은 입력 true
	 */
	public static boolean isWrongId(String id) {
		String regex = "^[a-zA-Z0-9]{3,10}$";
		return checkIsWrong(regex, id);
	}
	
	/**
	 * 비밀번호 형식 검사
	 * 최소 하나의 문자, 하나의 숫자, 하나의 특수문자
	 * 영문(소대문자), 숫자, 특수문자 만
	 * 4~12글자
	 * @param pw
	 * @return 올바르지 않은 입력 true
	 */
	public static boolean isWrongPW(String pw) {
		String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{4,12}$";
		return checkIsWrong(regex, pw);
	}
	
	/**
	 * 이름 형식 검사
	 * 국어, 영문(소대문자), 숫자만 (특수문자 안됨)
	 * @param name
	 * @return 올바르지 않은 입력 true
	 */
	public static boolean isWrongName(String name) {
		String regex = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$";
		return checkIsWrong(regex, name);
	}
	
	/**
	 * 이메일 형식 검사
	 * @param email
	 * @return 올바르지 않은 입력 true
	 */
	public static boolean isWrongEmail(String email) {
		String regex = "^[\\w.%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,3}$";
		return checkIsWrong(regex, email);
	}
	
	/**
	 * BaseballGame 입력값 형식 검사
	 * 한자리 숫자만
	 * @param num
	 * @return 올바르지 않은 입력 true
	 */
	public static boolean isWrongGameInput(String num) {
		String regex = "[\\d{1}]";
		return checkIsWrong(regex, num);
	}
	
	public static boolean checkIsWrong(String regex, String id) {
		if(Pattern.compile(regex).matcher(id.trim()).matches()) {
			return false;
		}
		return true;
	}
	
}
