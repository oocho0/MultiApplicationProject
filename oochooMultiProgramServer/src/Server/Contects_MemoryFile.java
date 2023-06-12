package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Contects_MemoryFile implements Contects {
	private Map<String, Contects_Info> contects;
	private final String CONTECTS_FILE;
	private String threadName;
	
	public Contects_MemoryFile(String threadName, String cONTECTS_FILE) {
		this.threadName = threadName;
		CONTECTS_FILE = cONTECTS_FILE;
		contects = new HashMap<>();
		this.loadContects();
	}

	@Override
	public Map<String, Contects_Info> findInfo(Condition condition) {
		Map<String, Contects_Info> resultInfos = new HashMap<>();
		for(Contects_Info info : contects.values()) {
			if(condition.test(info)) {
				resultInfos.put(info.getSerialNo(), info);
			}
		}
		return resultInfos;
	}

	@Override
	public Map<String, Contects_Info> getInfoByGroup(String group) {
		return findInfo((info)->info.getGroup().equals(group));
	}

	@Override
	public Map<String, Contects_Info> getInfoByName(String name) {
		return findInfo((info)->info.getName().equals(name));
	}

	@Override
	public Map<String, Contects_Info> getInfoByTel(String tel) {
		return findInfo((info)->info.getTel().equals(tel));
	}

	@Override
	public Map<String, Contects_Info> getInfoByAddr(String Addr) {
		return findInfo((info)->info.getAddress().equals(Addr));
	}

	@Override
	public void addInfo(Contects_Info info) {
		contects.put(info.getSerialNo(),info);
		FileWriter fw;
		try {
			fw = new FileWriter(CONTECTS_FILE, true);
			fw.write(format(info));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modifyInfo(Contects_Info info) {
		removeInfo(info.getName());
		addInfo(info);
	}

	@Override
	public void removeInfo(String name) {
		for(Contects_Info anInfo : contects.values()) {
			if(anInfo.getName().equals(name)) {
				contects.remove(anInfo.getSerialNo());
			}
		}
		FileWriter fw;
		String all = "";
		try {
			fw = new FileWriter(CONTECTS_FILE, false);
			for(Contects_Info anInfo : contects.values()) {
				all += format(anInfo);
			}
			fw.write(all);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void printAll() {
		if(contects.isEmpty()) {
			
		}
	}
	
	public void loadContects() {
		File file = new File(CONTECTS_FILE);
		BufferedReader reader;
		String line;
		Contects_Info info;
		try {
			file.createNewFile();
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine())!=null) {
				String[] arr = line.split(",");
				info = new Contects_Info(arr[0],arr[1],arr[2],arr[3]);
				contects.put(info.getSerialNo(),info);
			}
			System.out.println(threadName+"파일로부터 데이터를 로드 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String format(Contects_Info info) {
		return info.getSerialNo()+","+info.getGroup()+","+info.getName()+","+info.getTel()+","+info.getAddress()+"\n";
	}
}
