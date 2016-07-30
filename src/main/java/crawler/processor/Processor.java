/**
 * 
 */
package crawler.processor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawler.model.News;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

/**
 *************************
 * 新增 Processor 需继承此抽象类；
 * List页URL需通过 initAddRequset 加载；
 * Processor内通过 taskDate 确认爬取任务时间
 ************************* 
 * @author kootain
 * @creation 2016年7月8日
 *
 */
public abstract class Processor {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Date taskDate;
	
	public abstract void processor(Page page);
	
	public abstract void init(Spider spider);
	
	protected String getSourceFromPage(Page page){
    	String type = page.getUrl().regex("http\\w?://\\S+\\.(\\S+)\\.com").toString();
    	if(type!=null && !"".equals(type)){
    		type = type.substring(0,1).toUpperCase() + type.toLowerCase().substring(1);
    	}
    	return type;
    }
	
	protected void initAddRequest(Request request,Spider spider){
		request.setPriority(1);
		spider.addRequest(request);
	}
	
	public void initProcessor(Spider spider){
		this.taskDate = new Date();
		init(spider);
	}
	
	public void initProcessor(Spider spider,Date taskDate){
		this.taskDate = taskDate;
		init(spider);
	}
	
}
