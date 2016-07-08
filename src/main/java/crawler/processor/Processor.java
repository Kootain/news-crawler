/**
 * 
 */
package crawler.processor;

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
public interface Processor {
	public void processor(Page page);
	
	public void init(Spider spider);
}
