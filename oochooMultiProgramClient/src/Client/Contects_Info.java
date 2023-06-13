package Client;

public class Contects_Info {
	private int no;
	private String group;
	private String name;
	private String tel;
	private String address;
	private String serialNo;
	
	public Contects_Info(int no, String serialNo, String group, String name, String tel, String address) {
		this.no = no;
		this.serialNo = serialNo;
		this.group = group;
		this.name = name;
		this.tel = tel;
		this.address = address;
	}

	public int getNo() {
		return this.no;
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
	
	public String getSerialNo() {
		return this.serialNo;
	}
	@Override
	public String toString() {
		return serialNo+","+group+","+name+","+tel+","+address;
	}
}
