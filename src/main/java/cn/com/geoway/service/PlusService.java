package cn.com.geoway.service;

import java.io.FileInputStream;
import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 加法服务类--Activiti的ServiceTask的实现,必须实现JavaDelegate接口
 * 
 * @author lvxiaofei
 * @version 1.0 ,
 */
public class PlusService implements JavaDelegate {

	/**
	 * 执行方法
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		Properties properties = new Properties();
		// 获取参数文件的输入流
		FileInputStream in = new FileInputStream("D:/abc.properties");
		
		// 加载文件
		properties.load(in);
		// 关闭流
		in.close();
		
		int a = Integer.parseInt(properties.getProperty("a"));
		int b = Integer.parseInt(properties.getProperty("b"));
		System.out.println("a + b的值为" + (a + b));
		
		execution.setVariable("a", a);
		execution.setVariable("b", b);
		execution.setVariable("ab", a + b);
	}

}
