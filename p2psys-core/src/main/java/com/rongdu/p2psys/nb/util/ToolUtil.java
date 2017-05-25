package com.rongdu.p2psys.nb.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class ToolUtil
{
	public static final double getNeedDouble(double data)
	{
		 DecimalFormat formater = new DecimalFormat();

		 formater.setMaximumFractionDigits(2);
		 formater.setGroupingSize(0);
		 formater.setRoundingMode(RoundingMode.FLOOR);

		 return Double.valueOf(formater.format(data));
	}
}
