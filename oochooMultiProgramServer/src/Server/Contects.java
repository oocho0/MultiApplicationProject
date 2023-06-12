package Server;

import java.util.Map;

public interface Contects {
	Map<String, Contects_Info> findInfo(Condition condition);
	Map<String, Contects_Info> getInfoByGroup(String group);
	Map<String, Contects_Info> getInfoByName(String name);
	Map<String, Contects_Info> getInfoByTel(String tel);
	Map<String, Contects_Info> getInfoByAddr(String Addr);
	void addInfo(Contects_Info info);
	void modifyInfo(Contects_Info info);
	void removeInfo(String name);
	void printAll();
}
