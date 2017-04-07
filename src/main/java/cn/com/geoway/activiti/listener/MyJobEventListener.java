package cn.com.geoway.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

/**
 * 特定的事件监听器
 * 
 * @author lvxiaofei
 * @version 1.0 , 2017-04-06 10:07:21
 */
public class MyJobEventListener implements ActivitiEventListener {

	@Override
	public void onEvent(ActivitiEvent event) {
		System.out.println("特定类型的监听器执行了!!!");
	}

	@Override
	public boolean isFailOnException() {
		System.out.println("特定类型监听器异常!");
		return false;
	}

}
