package Server;

import java.util.Map;

public interface Contacts {
	Map<String, Contacts_Info> findInfo(Condition condition, String byType, String keyword);
	Map<String, Contacts_Info> getInfoByGroup(String group);
	Map<String, Contacts_Info> getInfoByName(String name);
	Map<String, Contacts_Info> getInfoByTel(String tel);
	Map<String, Contacts_Info> getInfoByAddr(String addr);
	Map<String, Contacts_Info> getAll();
	Contacts_Info getInfo(String serialNo);
	void addInfo(Contacts_Info info);
	void modifyInfo(Contacts_Info info);
	void removeInfo(String serialNo);
	void endService();
}
