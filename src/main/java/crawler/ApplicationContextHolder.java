/**
 * 
 */
package crawler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年8月3日
 *
 */
public class ApplicationContextHolder implements ApplicationContextAware{

	private static ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		ctx = arg0;
		
	}
	
	public static ApplicationContext getContext(){
		if(ctx==null){
			
		}
		return ctx;
	}
	
}