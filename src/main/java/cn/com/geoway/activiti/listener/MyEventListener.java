package cn.com.geoway.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;

/**
 * 自定义流程事件监听器
 * 
 * @author lvxiaofei
 * @version 1.0, 2017-04-06 09:42:51
 */
public class MyEventListener implements ActivitiEventListener {

	/**
	 * 监听流程事件
	 * 
	 * @param event
	 *            参数:流程执行过程中发生的事件
	 */
	@Override
	public void onEvent(ActivitiEvent event) {
		// 判断事件类型
		switch (event.getType()) {
		// 任务成功执行
		case JOB_EXECUTION_SUCCESS:
			System.out.println("一个任务执行成功!!!");
			break;
		// 任务执行失败
		case JOB_EXECUTION_FAILURE:
			System.out.println("一个任务执行失败!!!");
			break;
		// 监听到某个事件
		default:
			System.out.println("事件监听到了 " + event.getType());
		}
	}

	/**
	 * 当onEvent方法抛出异常时执行
	 * 
	 * @return 返回值:返回false忽略异常;返回true异常不会忽略，继续向上传播，迅速导致当前命令失败
	 */
	@Override
	public boolean isFailOnException() {
		System.out.println("事件监听器遇到异常!!!");
		return false;
	}

}
