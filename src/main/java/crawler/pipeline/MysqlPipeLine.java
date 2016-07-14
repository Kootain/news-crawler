/**
 * 
 */
package crawler.pipeline;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.text.html.HTML.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private NewsDao newsDao;

	@Override
	public void process(ResultItems resultItems, Task task){
		News news = genNews(resultItems);
		if(news == null){
			return;
		}
		List<Tags> tags = resultItems.get("tags");
		int parentId = 0;
		try {
			newsDao.addNews(news);
			logger.info("插入新闻:"+news.getLink());
			if(null!=tags){
				for(int i=0;i<tags.size();i++){
					Tags tag = tags.get(i);
					Tags tmpTags = newsDao.findTag(tag.getTagName());
					if(tmpTags==null){//新标签
						if(tag.getTagType()==-1){	//如果不是关键字标签,处理父子关系
							tag.setParentTag(-1);
						}else{
							tag.setParentTag(parentId);
						}
						newsDao.addTags(tag);
						logger.info("插入Tag:"+tag.getTagName());
					}else{
						tags.set(i, tmpTags);
					}
					if(tag.getTagType()==0){	//默认传入第一条是父标签,如果是父标签,则余下非关键字标签父id设为第一个标签插入后返回的id
						parentId = tag.getTid();
					}
				}
			}
		} catch (DuplicateKeyException e) {
			logger.warn("重复数据!");
		} catch (MySQLDataException e) {
			logger.error("新建标签失败!");
		} finally{
			if(news.getId()!=0){
				for(Tags tag:tags){
					Map<String, Integer> map = new HashMap<String, Integer>();	//插入新闻和tag的关系
					map.put("tid", tag.getTid());
					map.put("nid", news.getId());
					try {
						newsDao.addT2N(map);
						logger.info(String.format("建立新闻标签关系: news[%d] - tag[%d]", map.get("nid"),map.get("tid")));
					} catch (MySQLDataException e) {
						logger.error("新闻标签关系建立失败");
					}
				}
			}
		}

	}
	
	private News genNews(ResultItems resultItems){
		News news = null;
		try{
			news = new News();
			news.setTitle(resultItems.get("title").toString());
			news.setSubtitle(resultItems.get("subtitle").toString());
			news.setContent(resultItems.get("content").toString());
			news.setResource(resultItems.get("resource").toString());
			news.setLink(resultItems.get("link").toString());
			news.setNewsTime((Date)resultItems.get("newsTime"));
			news.setCreateTime(new Date());
			news.setIshidden(false);
		} catch (NullPointerException e){
			logger.error("新闻初始化错误，Null!");
		}
    	return news;
    }
}
