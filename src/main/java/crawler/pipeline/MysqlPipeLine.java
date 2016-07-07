/**
 * 
 */
package crawler.pipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
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

@Component("JobInfoDaoPipeline")
public class MysqlPipeLine implements Pipeline{
	private static ApplicationContext ctx;  
	static{  
		ctx = new ClassPathXmlApplicationContext("config/applicationContext.xml");  
	}        
	private NewsDao mapper = (NewsDao)ctx.getBean("newsMapper");
	
	@Override
	public void process(ResultItems resultItems, Task task){
//		News news = (News)resultItems.get("itemObject");
		News news = genNews(resultItems);
		List<Tags> tags = resultItems.get("tags");
		int parentId = 0;
		
		try {
			mapper.addNews(news);
			if(null!=tags){
				for(Tags tag:tags){
					Tags tmpTags = mapper.findTag(tag.getTagName());
					if(tmpTags==null){//新标签
						if(tag.getTagType()==-1){	//如果不是关键字标签,处理父子关系
							tag.setParentTag(-1);
						}else{
							tag.setParentTag(parentId);
						}
						mapper.addTags(tag);
						if(tag.getTagType()==0){	//默认传入第一条是父标签,如果是父标签,则余下非关键字标签父id设为第一个标签插入后返回的id
							parentId = tag.getTid();
						}
						Map<String, Integer> map = new HashMap<String, Integer>();	//插入新闻和tag的关系
						map.put("tid", tag.getTid());
						map.put("nid", news.getId());
						mapper.addT2N(map);	
					}else{		//已存标签
						Map<String, Integer> map = new HashMap<String, Integer>();	//插入新闻和tag的关系
						map.put("tid", tmpTags.getTid());
						map.put("nid", news.getId());
						mapper.addT2N(map);	
					}
				}
			}
		} catch (DuplicateKeyException e) {
			System.err.println("重复数据");
		} catch (MySQLDataException e) {
			e.printStackTrace();
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
