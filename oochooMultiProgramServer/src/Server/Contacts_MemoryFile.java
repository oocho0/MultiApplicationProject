package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Contacts_MemoryFile implements Contacts {
	private Map<String, Contacts_Info> contacts;
	private final String CONTACTS_FILE;
	
	public Contacts_MemoryFile(String cONTACTS_FILE) {
		CONTACTS_FILE = cONTACTS_FILE;
		contacts = new HashMap<>();
		this.loadContacts();
	}

	@Override
	public Map<String, Contacts_Info> findInfo(Condition condition, String bytype, String keyword) {
		Map<String, Contacts_Info> resultInfos = new HashMap<>();
		for(Contacts_Info info : contacts.values()) {
			if(condition.test(info)) {
				resultInfos.put(info.getSerialNo(), info);
			}
		}
		return resultInfos;
	}

	@Override
	public Map<String, Contacts_Info> getInfoByGroup(String group) {
		return findInfo((info)->info.getGroup().contains(group), null, null);
	}

	@Override
	public Map<String, Contacts_Info> getInfoByName(String name) {
		return findInfo((info)->info.getName().contains(name), null, null);
	}

	@Override
	public Map<String, Contacts_Info> getInfoByTel(String tel) {
		return findInfo((info)->info.getTel().contains(tel), null, null);
	}

	@Override
	public Map<String, Contacts_Info> getInfoByAddr(String addr) {
		return findInfo((info)->info.getAddress().contains(addr), null, null);
	}
	
	@Override
	public Contacts_Info getInfo(String serialNo) {
		Map<String, Contacts_Info> resultInfos = findInfo((info)->info.getSerialNo().equals(serialNo), null, null);
		return resultInfos.get(serialNo);
	}
	
	@Override
	public Map<String, Contacts_Info> getAll() {
		return contacts;
	}

	@Override
	public void addInfo(Contacts_Info info) {
		contacts.put(info.getSerialNo(),info);
		FileWriter fw;
		try {
			fw = new FileWriter(CONTACTS_FILE, true);
			fw.write(format(info));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modifyInfo(Contacts_Info info) {
		removeInfo(info.getSerialNo());
		addInfo(info);
		contacts.put(info.getSerialNo(), info);
	}

	@Override
	public void removeInfo(String serialNo) {
		contacts.remove(serialNo);
		FileWriter fw;
		String all = "";
		try {
			fw = new FileWriter(CONTACTS_FILE, false);
			for(Contacts_Info anInfo : contacts.values()) {
				all += format(anInfo);
			}
			fw.write(all);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadContacts() {
		File file = new File(CONTACTS_FILE);
		BufferedReader reader;
		String line;
		Contacts_Info info;
		try {
			file.createNewFile();
			reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine())!=null) {
				String[] arr = line.split(",");
				info = new Contacts_Info(arr[0],arr[1],arr[2],arr[3],arr[4]);
				contacts.put(info.getSerialNo(),info);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String format(Contacts_Info info) {
		return info.getSerialNo()+","+info.getGroup()+","+info.getName()+","+info.getTel()+","+info.getAddress()+"\n";
	}

	@Override
	public void endService() {
		contacts.clear();
	}
}
