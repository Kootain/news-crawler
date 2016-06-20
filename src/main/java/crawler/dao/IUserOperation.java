package crawler.dao;

import java.util.List;

import crawler.model.User;


public interface IUserOperation {
	public User selectUserByID(int id);
	
	public List<User> selectUsers(String userName);
	
	public int addUser(User user);
}
