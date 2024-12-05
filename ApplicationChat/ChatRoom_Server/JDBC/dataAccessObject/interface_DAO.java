package dataAccessObject;

import java.util.ArrayList;

public interface interface_DAO<T> {
	public int insert(T t);
	public int update(T t);
	public int delete(T t);
	
	public ArrayList<T> selectAll();
	public T selectById(T t);
	public int findByCondition(String condition);
	public ArrayList<T> selectByCondition(String condition);

	
}
