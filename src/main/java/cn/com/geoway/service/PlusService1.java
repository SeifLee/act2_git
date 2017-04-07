package cn.com.geoway.service;

import java.io.FileInputStream;
import java.util.Properties;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 加法服务类--Activiti的ServiceTask的实现,必须实现JavaDelegate接口
 * 
 * @author lvxiaofei
 * @version 1.0 ,2017-04-01 12:33:21
 */
public class PlusService1 implements JavaDelegate {

	/**
	 * 执行方法
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//Properties properties = PropertiesUtil.getProperties("D:/abc.properties");
		Properties properties = new Properties();
		// 获取参数文件的输入流
		FileInputStream in = new FileInputStream("D:/abc.properties");
		// 加载文件
		properties.load(in);
		// 关闭流
		in.close();
		int ab = (int) execution.getVariable("ab");
		int c = Integer.parseInt(properties.getProperty("c"));
		System.out.println("a + b + c的值为" + (ab + c));
		execution.setVariable("c", c);
		execution.setVariable("a+b+c", (ab + c));
	}

	

}
