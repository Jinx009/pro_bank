package com.rongdu.p2psys.nb.borrow.util;

import java.util.Date;
import java.util.List;

import com.itextpdf.text.log.SysoLogger;
import com.rongdu.p2psys.borrow.model.BorrowProfitModel;

public class Calculate {
	
	//天标
	public static double dayMark(List<BorrowProfitModel> list){
		double earning=0;
		for (BorrowProfitModel model : list) {
			int days =DateHelper.dayInterval(model.getReviewTime(), new Date()); //已过天数
			if(days <=model.getTimeLimit()){
				if(model.getBorrowTimeType() == 1){//天标
					earning += ((model.getInterest()+model.getInterestRate())/model.getTimeLimit()*days);
				}
			}
		}
		return earning;
	}
	
	//月标
	public static double mothMark(List<BorrowProfitModel> list){
		double earning=0;
		for (BorrowProfitModel model : list) {
			int days =DateHelper.dayInterval(model.getReviewTime(),new Date()); //已过天数
			if(model.getBorrowTimeType() == 0){//月标
					earning +=(( (model.getInterest()+model.getInterestRate()) / model.getTimeLimit() ) / 30 ) * days ;
			}
		}
		return earning;
	}
	
	//已还
	public static double Repayment(List<BorrowProfitModel> list){
		double earning =0;
		for (BorrowProfitModel model : list) {
			earning +=(model.getInterest()+model.getInterestRate());
		}
		return earning;
	}
	
	//等额本息
	public static double equalCorpusAndInterest(List<BorrowProfitModel> list){
		double earning =0;
		for (BorrowProfitModel model : list) {
			int days =DateHelper.days(); //已过天数
			earning += (days / 30 ) * (model.getInterest()+model.getInterestRate());
		}
		return earning;
	}
	
	//等息
	public static double equalInterest(List<BorrowProfitModel> list){
		double earning =0;
		for (BorrowProfitModel model : list) {
			Float days =(float) DateHelper.days(); //已过天数
			earning += ((days / 30) * (model.getInterest()+model.getInterestRate()));
		}
		return earning;
	}
	
	//中期
	public static double midInterest(List<BorrowProfitModel> list){
		double earning =0;
		for (BorrowProfitModel model : list) {
			int days =DateHelper.dayInterval(model.getReviewTime(),new Date()); //已过天数
			earning += ((days / model.getMiddleDay()) * (model.getInterest()+model.getInterestRate()));
		}
		return earning;
	}
	
}
