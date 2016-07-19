package crawler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import crawler.processor.ProcessorCenter;

public class Start {
	
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
	
	public static void main(String[] args) {
    	final ProcessorCenter pc = ctx.getBean(ProcessorCenter.class);
    	pc.crawel();
    }
}
