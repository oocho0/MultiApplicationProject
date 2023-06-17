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
	
	public Contects_MemoryFile(String cONTECTS_FILE) {
		CONTECTS_FILE = cONTECTS_FILE;
		contects = new HashMap<>();
		this.loadContects();
	}

	@Override
	public Map<String, Contects_Info> findInfo(Condition condition, String bytype, String keyword) {
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
		return findInfo((info)->info.getGroup().contains(group), null, null);
	}

	@Override
	public Map<String, Contects_Info> getInfoByName(String name) {
		return findInfo((info)->info.getName().contains(name), null, null);
	}

	@Override
	public Map<String, Contects_Info> getInfoByTel(String tel) {
		return findInfo((info)->info.getTel().contains(tel), null, null);
	}

	@Override
	public Map<String, Contects_Info> getInfoByAddr(String addr) {
		return findInfo((info)->info.getAddress().contains(addr), null, null);
	}
	
	@Override
	public Contects_Info getInfo(String serialNo) {
		Map<String, Contects_Info> resultInfos = findInfo((info)->info.getSerialNo().equals(serialNo), null, null);
		return resultInfos.get(serialNo);
	}
	
	@Override
	public Map<String, Contects_Info> getAll() {
		return contects;
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
		removeInfo(info.getSerialNo());
		addInfo(info);
		contects.put(info.getSerialNo(), info);
	}

	@Override
	public void removeInfo(String serialNo) {
		contects.remove(serialNo);
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
				info = new Contects_Info(arr[0],arr[1],arr[2],arr[3],arr[4]);
				contects.put(info.getSerialNo(),info);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String format(Contects_Info info) {
		return info.getSerialNo()+","+info.getGroup()+","+info.getName()+","+info.getTel()+","+info.getAddress()+"\n";
	}

	@Override
	public void endService() {
		contects.clear();
	}
}
