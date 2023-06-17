package Server;

import java.util.Map;

public interface Contects {
	Map<String, Contects_Info> findInfo(Condition condition, String byType, String keyword);
	Map<String, Contects_Info> getInfoByGroup(String group);
	Map<String, Contects_Info> getInfoByName(String name);
	Map<String, Contects_Info> getInfoByTel(String tel);
	Map<String, Contects_Info> getInfoByAddr(String addr);
	Map<String, Contects_Info> getAll();
	Contects_Info getInfo(String serialNo);
	void addInfo(Contects_Info info);
	void modifyInfo(Contects_Info info);
	void removeInfo(String serialNo);
	void endService();
}
