package crawler;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import crawler.processor.ProcessorCenter;

public class Start {

	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-All.xml");
	
	private Logger logger;
	
	Start(){
		DOMConfigurator.configure("config/log4j.xml");
		logger = LoggerFactory.getLogger(getClass());
	}
	
	public static void main(String[] args) {
		
    }
	
	public void daily() {
		ProcessorCenter pc = ctx.getBean(ProcessorCenter.class);
		try{
			logger.info("定时任务启动，新闻爬取中...");
			pc.crawel();
		}catch(IllegalStateException e){
			logger.error("爬虫已在运行中...本次定时任务结束！");
		}
	}
}
