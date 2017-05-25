package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.ips.service.IpsService;

/**
 * 
 * @author Jinx
 *
 */
public class NbTenderTask extends AbstractLoanTask
{
	private Logger logger = Logger.getLogger(TenderTask.class);

	public NbTenderTask()
	{
		super();
		task.setName("NbTender.Task");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doLoan()
	{

		JobQueue<ValueEvent> queue = JobQueue.getTenderInstance();
		AutoBorrowService autoBorrowService = (AutoBorrowService) BeanUtil.getBean("autoBorrowService");
		BorrowTenderService borrowTenderService = (BorrowTenderService) BeanUtil.getBean("borrowTenderService");
		ChinapnrService chinapnrService = (ChinapnrService) BeanUtil.getBean("chinapnrService");
		while (!queue.isEmpty())
		{
			ValueEvent event = queue.peek();
			if (event != null)
			{
				String result = "success";
				try
				{
					if ("nbtender".equals(event.getOperate()))
					{
						/**
						 * 正常投标
						 */
						borrowTenderService.addNbTender(event.getBorrow(),event.getBorrowModel());
					} 
					else if ("autoTender".equals(event.getOperate()))
					{
						autoBorrowService.autoDealTender(event.getBorrowModel());
					} 
					else if ("doAddTender".equals(event.getOperate()))
					{
						if (TPPWay.API_CODE == TPPWay.API_CODE_YJF)
						{
						} 
						else if (TPPWay.API_CODE == TPPWay.API_CODE_IPS)
						{
							IpsService ipsService = (IpsService) BeanUtil.getBean("ipsService");
							ipsService.doAddTender(event.getBorrowModel());
						} 
						else if (TPPWay.API_CODE == TPPWay.API_CODE_PNR)
						{
							chinapnrService.addTender(event.getBorrowModel());
						}
					}
				} catch (Exception e)
				{
					logger.error(e.getMessage(), e);
					if (e instanceof BussinessException)
					{
						result = e.getMessage();
					} 
					else
					{
						result = "系统异常，业务处理失败";
					}
				} finally
				{
					queue.remove(event);
				}
				if (event.getResultFlag() != null)
				{
					Global.RESULT_MAP.put(event.getResultFlag(), result);
				}
			}
		}

	}

	@Override
	public Object getLock()
	{
		return TenderTask.TENDER_STATUS;
	}

}
