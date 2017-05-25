package com.rongdu.p2psys.core.quartz;

import org.apache.log4j.Logger;

public class AbstractLoanTask implements LoanTask {
	private Logger logger = Logger.getLogger(AbstractLoanTask.class);
	public Thread task = new Thread("Task") {

		private boolean isRun = true;

		@Override
		public void run() {
			while (isRun) {
				logger.debug(this.getName() + " Running!");
				doLoan();
				doWait();
			}
		}

		public void stopThread() {
			isRun = false;
		}

	};

	public AbstractLoanTask() {
		task.start();
		logger.debug("线程启动！");
	}

	@Override
	public void execute() {
		logger.debug("task.execute()");
		Object lock = getLock();
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	@Override
	public void doLoan() {
		logger.debug("AbstractTask开始");
	}

	public void doWait() {
		Object lock = this.getLock();
		synchronized (getLock()) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {

	}

	@Override
	public Object getLock() {
		return LoanTask.LOAN_STATUS;
	}

}
