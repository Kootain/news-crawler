package crawler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import crawler.downloader.CharsetConfigDownloader;
import crawler.pipeline.MysqlPipeLine;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;


@Component
public class ProcessorCenter implements PageProcessor {

	private static String CLASS_BASE = "crawler.processor.";
	
	private static String PROCESSOR_METHOD_NAME = "processor";
	
	private static String INIT_METHOD_NAME = "init";
	
	private static String[] SOURCE_LIST = {	
//											"Qq",
//											"163",
//											"Sina",
											"Sohu"
											};
	
	private Map<String, Object> processorInstance= new HashMap<String, Object>();
	
	ProcessorCenter(){
		for(String source:SOURCE_LIST){
			Class<?> clazz;
			try {
				clazz = Class.forName(processorName(source));
				this.processorInstance.put(source, clazz.newInstance());
				System.err.println(source);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
   
	}
	
    public Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);
    
    private void crawel(){
    	Spider spider = Spider.create(new ProcessorCenter())
    						  .addPipeline(new MysqlPipeLine())
    						  .addPipeline(new ConsolePipeline())
    						  .setDownloader(new CharsetConfigDownloader())
    						  .thread(5);
    	

        for(String source:SOURCE_LIST){
        	try {
				Class<?> clazz = Class.forName(processorName(source));
				processorInstance.put(source, clazz.newInstance());
				Class<?>[] argsType = new Class[1];
				Object[] args = new Object[1];
	    		argsType[0] = spider.getClass();
	    		args[0] = spider;
	    		Method method = clazz.getMethod(INIT_METHOD_NAME, argsType);
	    		method.invoke(processorInstance.get(source), args);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				
			}
        }
        spider.run();
        spider.close();
    }

    public static void main(String[] args) {
    	ProcessorCenter pc = new ProcessorCenter();
    	pc.crawel();
    }
    
    @Override
    public void process(Page page) {
    	String webtype = getSourceFromPage(page);
    	page.putField("resource", webtype);
    	try {
    		Class<?> clazz = processorInstance.get(webtype).getClass();
    		Class<?>[] argsType = new Class[1];
    		Object[] args = new Object[1];
    		argsType[0] = page.getClass();
    		args[0] = page;
    		Method method = clazz.getMethod(PROCESSOR_METHOD_NAME, argsType);
    		method.invoke(processorInstance.get(webtype), args);
    	} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }

    @Override
    public Site getSite() {
        return site;
    }
    
    private String getSourceFromPage(Page page){
    	String type = page.getUrl().regex("http\\w?://\\S+\\.(\\S+)\\.com").toString();
    	if(type!=null && !"".equals(type)){
    		type = type.substring(0,1).toUpperCase() + type.toLowerCase().substring(1);
    	}
    	return type;
    }
    
    /**
     * Return the processor class name with package name,
     * for reflect the processor class
     * @param page
     * @return
     */
    private String processorName(Page page){
    	String type = getSourceFromPage(page);
    	page.putField("resource", type);
    	return CLASS_BASE+"Processor"+type;
    }
    
    
    private String processorName(String type){
    	return CLASS_BASE+"Processor"+type;
    }
    
}
