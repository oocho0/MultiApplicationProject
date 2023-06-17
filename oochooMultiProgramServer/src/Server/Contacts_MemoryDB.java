package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class Contects_MemoryDB implements Contects {
	
	private static final String JDBC_DRIVER = "org.h2.Driver";
	private static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
	private static final String USER = "sa";
	private static final String PW = "";
	private static final String TABLE = "CONTECTS";
	private Connection conn;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public Contects_MemoryDB() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PW);
			String START = String.format("CREATE TABLE IF NOT EXISTS %s"
					+ "(serialNo VARCHAR(40) PRIMARY KEY, g_roup VARCHAR(30),"
					+ "name VARCHAR(30), tel VARCHAR(15), "
					+ "address VARCHAR(30));", TABLE); 
			stmt  = conn.prepareStatement(START);
			stmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, Contects_Info> findInfo(Condition condition, String byType, String keyword) {
		Map<String, Contects_Info> resultInfos = new HashMap<>();
		String INFO_SEARCH = String.format("SELECT * FROM %s WHERE %s LIKE '%s'",TABLE, byType, "%"+keyword+"%");
		try {
			stmt = conn.prepareStatement(INFO_SEARCH);
			rs = stmt.executeQuery();
			while(rs.next()) {
				resultInfos.put(rs.getString("serialNo"), new Contects_Info(rs.getString(1),rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultInfos;
	}

	@Override
	public Map<String, Contects_Info> getInfoByGroup(String group) {
		return findInfo(null, "g_roup", group);
	}

	@Override
	public Map<String, Contects_Info> getInfoByName(String name) {
		return findInfo(null, "name", name);
	}

	@Override
	public Map<String, Contects_Info> getInfoByTel(String tel) {
		return findInfo(null, "tel", tel);
	}

	@Override
	public Map<String, Contects_Info> getInfoByAddr(String addr) {
		return findInfo(null, "address", addr);
	}
	
	@Override
	public Contects_Info getInfo(String serialNo) {
		Map<String, Contects_Info> resultInfos = findInfo(null, "serialNo", serialNo);
		return resultInfos.get(serialNo);
	}
	
	@Override
	public Map<String, Contects_Info> getAll() {
		String INFO_GETALL = String.format("SELECT * FROM %s", TABLE);
		Map<String, Contects_Info> resultInfos = new HashMap<>();
		try {
			stmt = conn.prepareStatement(INFO_GETALL);
			rs = stmt.executeQuery();
			while(rs.next()) {
				resultInfos.put(rs.getString("serialNo"), new Contects_Info(rs.getString(1),
						rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultInfos;
	}

	@Override
	public void addInfo(Contects_Info info) {
		String INFO_INSERT = String.format("INSERT INTO %s VALUES(?, ?, ?, ?, ?)",TABLE);
		try {
			stmt = conn.prepareStatement(INFO_INSERT);
			stmt.setString(1, info.getSerialNo());
			stmt.setString(2, info.getGroup());
			stmt.setString(3, info.getName());
			stmt.setString(4, info.getTel());
			stmt.setString(5, info.getAddress());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void modifyInfo(Contects_Info info) {
		String INFO_MODIFY = String.format("UPDATE %s SET g_roup = ?, name = ?, "
				+ "tel = ?, address = ? WHERE serialNo = ?",TABLE);
		try {
			stmt = conn.prepareStatement(INFO_MODIFY);
			stmt.setString(1, info.getGroup());
			stmt.setString(2, info.getName());
			stmt.setString(3, info.getTel());
			stmt.setString(4, "'"+info.getAddress()+"'");
			stmt.setString(5, info.getSerialNo());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeInfo(String serialNo) {
		String INFO_DELETE = String.format("DELETE %s WHERE serialNo = ?",TABLE);
		try {
			stmt = conn.prepareStatement(INFO_DELETE);
			stmt.setString(1, serialNo);
			stmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	@Override
	public void endService() {
		try {
			if(stmt!=null) {
				stmt.close();
			}
			if(conn!=null) {
				conn.close();
			}
			if(rs!= null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
