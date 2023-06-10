package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Login_CheckDBThread extends Thread{
	private Map<String, Login_UserInfo> userList;
	private Map<String, String> idList;
	private final String USER_DB;
	
	public Login_CheckDBThread(Map<String, Login_UserInfo> userList, Map<String, String> idList, String uSER_DB) {
		super();
		this.userList = userList;
		this.idList = idList;
		USER_DB = uSER_DB;
	}
	
	@Override
	public void run() {
		/* userData파일에서 전체 데이터를 가져와 userList에 저장 */
		File file = new File(USER_DB);
		BufferedReader reader;
		String line;
		Login_UserInfo info;
		try {
			file.createNewFile();
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine())!=null) {
				String[] arr = line.split(",");
				info = new Login_UserInfo(arr[0], arr[1], arr[2], arr[3], arr[4]);
				userList.put(info.getSerialNo(), info);
				idList.put(info.getUserId(), info.getSerialNo());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
