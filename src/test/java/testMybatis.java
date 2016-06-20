import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import crawler.dao.TestDao;
import crawler.model.TestModel;

/**
 * 
 */

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016��6��15��
 *
 */
public class testMybatis {
	
	private static ApplicationContext ctx;  
	static{  
		ctx = new ClassPathXmlApplicationContext("config/applicationContext.xml");  
	}        

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestDao mapper = (TestDao)ctx.getBean("userMapper");
		TestModel testModel = new TestModel();
		testModel.setDescription("ceshiD");
		testModel.setName("呵呵呵呵");
		mapper.addUser(testModel);
		System.out.print(testModel.getId());
	}

}
