package com.rongdu.p2psys.nb.product.exception;

import com.rongdu.common.exception.BussinessException;

public class ProductException extends BussinessException
{

	private static final long serialVersionUID = 454943376791750375L;

	public ProductException()
	{
		super();
	}

	public ProductException(String message)
	{
		super(message);
	}

	public ProductException(String message, String url)
	{
		super(message, url);
	}

	public ProductException(String message, int type)
	{
		super(message, type);
	}

}
