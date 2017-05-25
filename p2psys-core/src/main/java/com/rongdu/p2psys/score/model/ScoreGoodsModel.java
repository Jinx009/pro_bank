package com.rongdu.p2psys.score.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.domain.ScoreGoods;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.user.exception.UserException;

public class ScoreGoodsModel extends ScoreGoods {

    /**
     * 
     */
    private static final long serialVersionUID = -4119105299283247637L;

    /**
     * domain 转 model
     * @param item domain
     * @return model
     */
    public static ScoreGoodsModel instance(ScoreGoods item) {
        ScoreGoodsModel model = new ScoreGoodsModel();
        BeanUtils.copyProperties(item, model);
        return model;
    }

    public ScoreGoods prototype() {
        ScoreGoods item = new ScoreGoods();
        BeanUtils.copyProperties(this, item);
        return item;
    }
    
    /**
     * 兑换用户验证
     * @param userId
     * @return
     */
    public boolean checkUser(long userId){
        if(this.getUserId() != userId || this.getUserId() <= 0){
            throw new UserException("用户信息不正确！", -1);
        }
        return true;
    }
    
    /**
     * 积分验证
     * @param userId
     * @return
     */
    public boolean checkScore(int score){
        if(score == 0 || this.getScore() > score){
            throw new ScoreException("用户积分不足！", -1);
        }
        return true;
    }
    
    /**
     * 商品验证
     * @param userId
     * @return
     */
    public boolean checkGoods(Goods item){
        if(item == null){
            throw new ScoreException("用户兑换的商品不存在！", -1);
        }
        if(item.getScore()* this.getConvertNum() != this.getScore() || this.getScore() < 0){
            throw new ScoreException("用户兑换积分有误！", -1);
        }
        if(item.getStatus() != Goods.PASS_AUDIT){
            throw new ScoreException("此商品目前暂时还不能兑换！", -1);
        }
        if(item.getType() != Goods.UP_SHELVES){
            throw new ScoreException("此商品还未上架，暂时还不能兑换！", -1);
        }
        if(this.getConvertNum() <= 0 || this.getConvertNum() > 
            (item.getStore() - item.getFreezeStore() -item.getSellAcount())){
            throw new ScoreException("兑换商品分额不对！", -1);
        }
        if(this.getReceiveAddress() == null || this.getReceivePhone() == null || this.getReceiveUserName() == null){
            throw new ScoreException("兑换商品联系信息不正确！", -1);
        }
        return true;
    }
    
    /** 商品名称 */
    private String name;
    
    /** 用户名 */
    private String userName;
    
    /** 用户ID */
    private long userId;
    
    /** 商品ID */
    private long goodsId;
    
    /** 第一张图片 */
    private String imgSrc;
    
    /** 开始时间 **/
    private String startTime;
    
    /** 结束时间 **/
    private String endTime;
    
    /** 当前页数 **/
    private int page;
    
    /** 分页数 **/
    private int rows;
    
    /** 条件查询 **/
    private String searchName;

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
    
}
