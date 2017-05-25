package com.rongdu.p2psys.crowdfunding.exception;

import com.rongdu.common.exception.BussinessException;

public class CrowdFundingException extends BussinessException
{
	private static final long serialVersionUID = 710744405830822046L;

	public CrowdFundingException()
	{
		super();
	}

	public CrowdFundingException(String message)
	{
		super(message);
	}

	public CrowdFundingException(String message, String url)
	{
		super(message, url);
	}

	public CrowdFundingException(String message, int type)
	{
		super(message, type);
	}
}
