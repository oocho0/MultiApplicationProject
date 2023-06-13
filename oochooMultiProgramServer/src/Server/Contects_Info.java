package Server;

import java.util.UUID;

public class Contects_Info {
	private String serialNo;
	private String group;
	private String name;
	private String tel;
	private String address;
	
	public Contects_Info(String group, String name, String tel, String address) {
		this(UUID.randomUUID().toString(), group, name, tel, address);
	}

	public Contects_Info(String serialNo, String group, String name, String tel, String address) {
		this.serialNo = serialNo;
		this.group = group;
		this.name = name;
		this.tel = tel;
		this.address = address;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public String getGroup() {
		return this.group;
	}

	public String getName() {
		return this.name;
	}
	
	public String getTel() {
		return this.tel;
	}
	
	
	public String getAddress() {
		return this.address;
	}
	@Override
	public String toString() {
		return serialNo+","+group+","+name+","+tel+","+address;
	}
}
