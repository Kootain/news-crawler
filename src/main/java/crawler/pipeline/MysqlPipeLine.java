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
import crawler.dao.TestDao;
import crawler.model.TestModel;


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
	private TestDao mapper = (TestDao)ctx.getBean("userMapper");
	
	@Override
	public void process(ResultItems resultItems, Task task) {
		mapper.addUser((TestModel)resultItems.get("itemObject"));
	}
}
