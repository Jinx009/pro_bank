package com.rongdu.p2psys.crowdfunding.exception;

import com.rongdu.common.exception.BussinessException;

public class ProjectsCommonException extends BussinessException
{
	private static final long serialVersionUID = -1520876598050160950L;

	public ProjectsCommonException()
	{
		super();
	}

	public ProjectsCommonException(String message)
	{
		super(message);
	}

	public ProjectsCommonException(String message, String url)
	{
		super(message, url);
	}

	public ProjectsCommonException(String message, int type)
	{
		super(message, type);
	}
}
