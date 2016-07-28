/**
 * 
 */
package crawler.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年7月8日
 *
 */
public abstract class Processor {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public abstract void processor(Page page);
	
	public abstract void init(Spider spider);
	
	protected String getSourceFromPage(Page page){
    	String type = page.getUrl().regex("http\\w?://\\S+\\.(\\S+)\\.com").toString();
    	if(type!=null && !"".equals(type)){
    		type = type.substring(0,1).toUpperCase() + type.toLowerCase().substring(1);
    	}
    	return type;
    }
}
