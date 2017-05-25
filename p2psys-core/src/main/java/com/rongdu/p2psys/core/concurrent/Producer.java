package com.rongdu.p2psys.core.concurrent;

import com.lmax.disruptor.RingBuffer;
import com.rongdu.p2psys.core.Global;

public class Producer implements Runnable {

	private ValueEvent event = null;
	private RingBuffer<ValueEvent> ringBuffer = null;

	public Producer(ValueEvent e, RingBuffer<ValueEvent> rb) {
		this.event = e;
		this.ringBuffer = rb;
	}

	@Override
	public void run() {
		// Publishers claim events in sequence
		if (this.event != null) {
			if (ringBuffer.remainingCapacity() < Integer.parseInt(Global.getValue("disruptor_ringbuffer_size")) * 0.1) {
				// SmsOpenUtils.sendSms("http://smsopen.erongdu.com/sendsms.php",
				// "ywdjava", "3gpOtlJ3", "15968892760",
				// "宜城贷Disruptor BUFFER_SIZE只剩10%,请适时增加。");
			} else {
				long sequence = ringBuffer.next();
				ValueEvent e = ringBuffer.get(sequence);
				e.setOperate(event.getOperate());
				e.setValue(event.getValue());
				e.setUser(event.getUser());
				e.setBorrowModel(event.getBorrowModel());
				e.setBorrowRepayment(event.getBorrowRepayment());
				e.setId(event.getId());
				e.setUserId(event.getUserId());
				e.setEmailStatus(event.getEmailStatus());
				e.setPhoneStatus(event.getPhoneStatus());
				e.setRealnameStatus(event.getRealnameStatus());
				e.setBorrow(event.getBorrow());
				e.setSubstationId(event.getSubstationId());
				e.setAccountLog(event.getAccountLog());
				e.setRechargeModel(event.getRechargeModel());
				e.setCashModel(event.getCashModel());
				e.setIpsRegister(event.getIpsRegister());
				e.setIpsRecharge(event.getIpsRecharge());
				e.setResultFlag(event.getResultFlag());
				ringBuffer.publish(sequence);
			}
		}
	}
}
