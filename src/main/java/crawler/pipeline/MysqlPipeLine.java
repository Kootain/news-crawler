/**
 * 
 */
package crawler.pipeline;

import javax.annotation.Resource;
import junit.framework.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import crawler.dao.NewsDao;
import crawler.model.News;


/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016��6��13��
 *
 */
@Component("JobInfoDaoPipeline")
public class MysqlPipeLine implements Pipeline{
	private static ApplicationContext ctx;  
	static{  
		ctx = new ClassPathXmlApplicationContext("config/applicationContext.xml");  
	}        
	private NewsDao mapper = (NewsDao)ctx.getBean("newsMapper");
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		mapper.addNews((News)resultItems.get("itemObject"));
	}
}
