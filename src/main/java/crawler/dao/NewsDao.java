/**
 * 
 */
package crawler.dao;

import com.mysql.jdbc.exceptions.MySQLDataException;

import crawler.model.News;

public interface NewsDao {
	public int addNews(News news) throws MySQLDataException;
}
