/**
 * 
 */
package crawler.pipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.exceptions.MySQLDataException;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import crawler.dao.NewsDao;
import crawler.model.News;
import crawler.model.Tags;

@Component("MysqlPipeLine")
@Scope("prototype")
public class MysqlPipeLine implements Pipeline{
	
	@Autowired
	private NewsDao newsDao;

	@Override
	public void process(ResultItems resultItems, Task task){
//		News news = (News)resultItems.get("itemObject");
		News news = genNews(resultItems);
		List<Tags> tags = resultItems.get("tags");
		int parentId = 0;
		Tags tmpTags = null;
		try {
			newsDao.addNews(news);
			if(null!=tags){
				for(Tags tag:tags){
					tmpTags = newsDao.findTag(tag.getTagName());
					if(tmpTags==null){//新标签
						if(tag.getTagType()==-1){	//如果不是关键字标签,处理父子关系
							tag.setParentTag(-1);
						}else{
							tag.setParentTag(parentId);
						}
						newsDao.addTags(tag);
						if(tag.getTagType()==0){	//默认传入第一条是父标签,如果是父标签,则余下非关键字标签父id设为第一个标签插入后返回的id
							parentId = tag.getTid();
						}	
					}
				}
			}
		} catch (DuplicateKeyException e) {
			System.err.println("重复数据");
		} catch (MySQLDataException e) {
			e.printStackTrace();
		}
		Map<String, Integer> map = new HashMap<String, Integer>();	//插入新闻和tag的关系
		map.put("tid", tmpTags.getTid());
		map.put("nid", news.getId());
		try {
			newsDao.addT2N(map);
		} catch (MySQLDataException e) {
			System.err.println("新建标签失败");
		}	

	}
	
	private News genNews(ResultItems resultItems){
    	News news = new News();
    	news.setTitle(resultItems.get("title").toString());
    	news.setSubtitle(resultItems.get("subtitle").toString());
    	news.setContent(resultItems.get("content").toString());
    	news.setResource(resultItems.get("resource").toString());
    	news.setLink(resultItems.get("link").toString());
    	news.setNewsTime((Date)resultItems.get("newsTime"));
    	news.setCreateTime(new Date());
    	news.setIshidden(false);
    	return news;
    }
}
