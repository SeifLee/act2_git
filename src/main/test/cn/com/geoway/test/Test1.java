package cn.com.geoway.test;

import org.junit.Test;

/**
 * 测试
 * 
 * @author lvxiaofei
 * @version 1.0，2017-04-05 09:42:50
 */
public class Test1 {
	
	@Test
	public void testname() throws Exception {
		String name = this.getClass().getName();
		System.out.println(name);
	}
}
