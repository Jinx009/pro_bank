package com.rongdu.p2psys.score.model.scorelog.tender;

import java.util.List;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.ScoreInvestSuccessRuleCheck;
import com.rongdu.p2psys.score.constant.ScoreConstant;
import com.rongdu.p2psys.score.constant.ScoreTemplateConstant;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.domain.ScoreType;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.score.model.scorelog.BaseTenderScoreLog;
import com.rongdu.p2psys.score.tool.ScoreCountTool;

/**
 * 投资积分
 */
public class TenderInvestSuccessLog extends BaseTenderScoreLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logTemplateNid = ScoreTemplateConstant.SCORE_INVEST;
	
	public TenderInvestSuccessLog() {
		super();
	}

	public TenderInvestSuccessLog(long userId, Borrow borrow ,BorrowTender t) {
		super(userId, 0, ScoreTypeConstant.SCORE_INVEST);
		int score = this.getScoreValue(borrow,t);
		this.setScore(score);
		Global.setTransfer("score", score);
		this.setLogTemplateNid(logTemplateNid);
	
	}
	
	@Override
	public void modifyScore() {
		Boolean result = scoreDao.updateScore(this.getUser().getUserId(), this.getScore(), this.getScore(), 0, 0);
		if(!result){
			throw new ScoreException("更新用户积分失败！", 1);
		}
	}

	public int getScoreValue (Borrow borrow ,BorrowTender t){
		ScoreType type = scoreTypeDao.getScoreTypeByNid(ScoreTypeConstant.SCORE_INVEST);
		if(type == null){
			throw new ScoreException("积分类型不存在！", 1);
		}
		// 投标积分处理
		ScoreInvestSuccessRuleCheck rule = (ScoreInvestSuccessRuleCheck) Global.getRuleCheck(type.getRuleNid());//查询投标奖励积分规则
		if(rule == null || rule.status != ScoreConstant.STATUS_YES) return 0;
		List<Integer> typeNoList = rule.borrow_type_no;// 从JSON中提取不能奖励积分的标种。
		double month_check_money = rule.month_base_money;//月标投标限制金额基数
		double day_check_money = rule.day_base_money;//天标投标限制金额基数
		byte is_month =  rule.is_month;// 月标赠送积分是否启用
		byte is_day =  rule.is_day;// 天标赠送积分是否启用
		double decimal_manage = rule.decimal_manage;//计算小数限制
		
		byte isDate = (byte) borrow.getBorrowTimeType();
		//提取标的类型，月标or天标
		int time = 0 ;
		double check_money = 0;
		if(isDate == 1){//判断是否是天标，如果是，则提取投标的天数。
			if(is_day != 1) return 0;// 如果规则中，天标奖励积分不启用，则return
			check_money = day_check_money;
		}else if(isDate == 0){//判断是否是月标，如果是，则提取投标的月份。
			if(is_month != 1) return 0;// 如果规则中，月标奖励积分不启用，则return
			time = borrow.getTimeLimit();
			check_money = month_check_money;
		}
		
		Boolean result = isRewardCredits(borrow , typeNoList);
		if(result){//判断是否符合奖励
			//计算会员投标有多少积分
			return ScoreCountTool.getTenderValue(time, t.getAccount(), check_money,decimal_manage);
		}
		return 0;
	}
	
	/**
	 * 是否可以奖励
	 * @param model 当前标种
	 * @param typeNoList 不合适奖励的标
	 * @return
	 */
	private boolean isRewardCredits(Borrow borrow , List<Integer> typeNoList){
		Integer nowBorrowType = 0;//当前标种
		nowBorrowType = borrow.getType();
		if(typeNoList != null && typeNoList.size() > 0){
			for(int i = 0 ; i < typeNoList.size() ; i++){
				Integer borrowType = typeNoList.get(i);
				if(borrowType != null && borrowType > 0){
					if(borrowType.equals(nowBorrowType)){
						return false;
					}
				}
			}
		}
		return true;
	}
}
