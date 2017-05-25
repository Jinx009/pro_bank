package com.rongdu.p2psys.tpp;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.ips.model.IpsMerchentUserInfo;
import com.rongdu.p2psys.user.domain.User;

/**
 * 调用托管接口的业务处理方法 基类
 * @author lx
 * @version 2.0
 * @since 2014年7月22日
 */
public abstract class BaseTPPWay implements TPPWay {
	
	/**
	 * 是否打开接口
	 * @return 是否打开接口
	 */
	public static boolean isOpenApi() {
		return "1".equals(Global.getValue("is_open_deposit"));
	}

	/**
	 * 是否开启线上配置
	 * @return 是否开启线上配置
	 */
	public static boolean isOnlineConfig() {
		return "1".equals(Global.getValue("config_online"));
	}

    @Override
    public Object doRealname() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Object doCorpRegister() throws Exception {
    	// TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object doRecharge() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object doTender(Object obj) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object bindBank(AccountBank bank, User user, String bankType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Map<String, Object> bankBindRemove(AccountBank ab, User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object doNewCash(AccountCash cash, User user, int cashnum, String province, String city, String bankCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object tradeCreatePoolReverse(String apiId, double money) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object doBorrow() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Object doCancelBorrow(Borrow borrow) throws Exception {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public Object doRepayment(BorrowRepayment repay, byte repayType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object doAutoRepaymentSigning() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object doAutoRecharge(User user, String ipsFeeType, String acctType, double trdAmt, double merFee) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addUserTppConfig(long userId) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Object registerGuarantor(Borrow borrow){
        return null;
    }
    
	/*
	 * 汇付金额格式化，必须保留小数两位
	 */
	public static String formatMoney(double money){
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(money);
	}
	
	@Override
	public IpsMerchentUserInfo queryMerUserInfo() throws Exception {
		return null;
	}    

    @Override
    public Object getTppPay(Map<String, Object> map) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getTppPayById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean doTppPayTask(List<Object> taskList) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Object apiLogin(User user) {
    	// TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public Object getTppAccount(User user){
    	// TODO Auto-generated method stub
    	return null;
    }
}
