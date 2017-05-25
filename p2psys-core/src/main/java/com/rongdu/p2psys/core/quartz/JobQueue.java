package com.rongdu.p2psys.core.quartz;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.quartz.task.BondTask;
import com.rongdu.p2psys.core.quartz.task.BorrowTask;
import com.rongdu.p2psys.core.quartz.task.CashTask;
import com.rongdu.p2psys.core.quartz.task.NoticeTask;
import com.rongdu.p2psys.core.quartz.task.PpfundTask;
import com.rongdu.p2psys.core.quartz.task.TenderTask;
import com.rongdu.p2psys.core.quartz.task.UserTask;
import com.rongdu.p2psys.core.quartz.task.VerifyBorrowTask;
import com.rongdu.p2psys.core.quartz.task.WechatMessageTask;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;

public class JobQueue<T>
{

	private Queue<T> queue = new ConcurrentLinkedQueue<T>();
	private Lock LOCK = new ReentrantLock();
	private LoanTask task;

	private static class LazyHolder
	{
		// 通知信息队列
		private static final JobQueue<Notice> NOTICE_INSTANCE = new JobQueue<Notice>(new NoticeTask());
		// 投标相关队列
		private static final JobQueue<ValueEvent> TENDER_INSTANCE = new JobQueue<ValueEvent>(new TenderTask());
		// 标相关队列
		private static final JobQueue<ValueEvent> BORROW_INSTANCE = new JobQueue<ValueEvent>(new BorrowTask());
		// 标审核相关队列
		private static final JobQueue<ValueEvent> VERIFY_BORROW_INSTANCE = new JobQueue<ValueEvent>(new VerifyBorrowTask());
		// 充值提现相关队列
		private static final JobQueue<ValueEvent> CASH_INSTANCE = new JobQueue<ValueEvent>(new CashTask());
		// 用户相关队列
		private static final JobQueue<ValueEvent> USER_INSTANCE = new JobQueue<ValueEvent>(new UserTask());
		// 债权相关队列
		private static final JobQueue<ValueEvent> BOND_INSTANCE = new JobQueue<ValueEvent>(new BondTask());
		// PPfund资金管理产品相关队列
		private static final JobQueue<ValueEvent> PPFUND_INSTANCE = new JobQueue<ValueEvent>(new PpfundTask());
		//微信信息队列
		private static final JobQueue<WechatMessage> WECHAT_MESSAGE_INSTANCE = new JobQueue<WechatMessage>(new WechatMessageTask());
		
	}
	
	/**
	 * 微信队列实例化
	 * 
	 * @return
	 */
	public static JobQueue<WechatMessage> getWechatMessageInstance()
	{
		return LazyHolder.WECHAT_MESSAGE_INSTANCE;
	}
	
	/**
	 * 通知队列实例化
	 * 
	 * @return
	 */
	public static JobQueue<Notice> getNoticeInstance()
	{
		return LazyHolder.NOTICE_INSTANCE;
	}

	/**
	 * 投标队列实例化
	 * 
	 * @return
	 */
	public static JobQueue<ValueEvent> getTenderInstance()
	{
		return LazyHolder.TENDER_INSTANCE;
	}
	/**
	 * 标队列实例化
	 * 
	 * @return
	 */
	public static JobQueue<ValueEvent> getBorrowInstance()
	{
		return LazyHolder.BORROW_INSTANCE;
	}

	/**
	 * 标审核处理
	 * 
	 * @return
	 */
	public static JobQueue<ValueEvent> getVerifyBorrowInstance()
	{
		return LazyHolder.VERIFY_BORROW_INSTANCE;
	}

	/**
	 * 用户充值提现处理
	 * 
	 * @return
	 */
	public static JobQueue<ValueEvent> getCashInstance()
	{
		return LazyHolder.CASH_INSTANCE;
	}

	/**
	 * 用户相关处理：注册开户，绑定银行卡等
	 * 
	 * @return
	 */
	public static JobQueue<ValueEvent> getUserInstance()
	{
		return LazyHolder.USER_INSTANCE;
	}

	/**
	 * 债权处理
	 * 
	 * @return
	 */
	public static JobQueue<ValueEvent> getBondInstance()
	{
		return LazyHolder.BOND_INSTANCE;
	}

	public static JobQueue<ValueEvent> getPpfundInstance()
	{
		return LazyHolder.PPFUND_INSTANCE;
	}

	private JobQueue(LoanTask task)
	{
		super();
		this.task = task;
	}

	public void offer(T model)
	{
		if (!queue.contains(model))
		{
			queue.offer(model);
			try
			{
				LOCK.lock();
				task.execute();
			} finally
			{
				LOCK.unlock();
			}

		}
	}

	public void offer(List<T> ts)
	{
		for (int i = 0; i < ts.size(); i++)
		{
			T t = ts.get(i);
			if (!queue.contains(t))
			{
				queue.offer(t);
			}
		}
		try
		{
			LOCK.lock();
			task.execute();
		} finally
		{
			LOCK.unlock();
		}
	}

	public T poll()
	{
		return queue.poll();
	}

	public T peek()
	{
		return queue.peek();
	}

	public void remove(T model)
	{
		queue.remove(model);
	}

	public int size()
	{
		return queue.size();
	}

	public boolean isEmpty()
	{
		try
		{
			LOCK.lock();
			return queue.isEmpty();
		} finally
		{
			LOCK.unlock();
		}

	}

	public void stop()
	{
		task.stop();
	}

}
