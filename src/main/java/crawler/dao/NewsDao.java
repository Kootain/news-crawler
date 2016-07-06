/**
 * 
 */
package crawler.dao;

import java.util.List;
import java.util.Map;

import com.mysql.jdbc.exceptions.MySQLDataException;

import crawler.model.News;
import crawler.model.Tags;

public interface NewsDao {
	public int addNews(News news) throws MySQLDataException;
	
	public int addTags(Tags tags) throws MySQLDataException;
	
	public int addT2N(Map<String,Integer> map) throws MySQLDataException;
	
	public List<News> selectall();
	
	public Tags findTag(String tagName);
}
