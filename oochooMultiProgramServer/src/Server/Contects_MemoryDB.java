package Server;

import java.util.Map;

public class Contects_MemoryDB implements Contects {
	private String threadName;
	
	public Contects_MemoryDB(String threadName) {
		this.threadName = threadName;
	}
	
	@Override
	public Map<String, Contects_Info> findInfo(Condition condition) {
		return null;
	}

	@Override
	public Map<String, Contects_Info> getInfoByGroup(String group) {
		return null;
	}

	@Override
	public Map<String, Contects_Info> getInfoByName(String name) {
		return null;
	}

	@Override
	public Map<String, Contects_Info> getInfoByTel(String tel) {
		return null;
	}

	@Override
	public Map<String, Contects_Info> getInfoByAddr(String Addr) {
		return null;
	}

	@Override
	public void addInfo(Contects_Info info) {
		
	}
	
	@Override
	public void modifyInfo(Contects_Info info) {
		
	}

	@Override
	public void removeInfo(String name) {
		
	}

	@Override
	public void printAll() {
	}
}
