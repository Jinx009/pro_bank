package com.rongdu.p2psys.cooperation.util.qq;

/**
 * QQ互联接口URL
 */
public class OpenQqConstants {

	/** 配置文件名 */
	public static final String QQ_CONFIG_NAME = "cooperation-login.properties";

	/** QQ登陆地址 用于获取Authorization Code 的URL */
	public static final String QQ_LOGIN_URL = "https://graph.qq.com/oauth2.0/authorize";

	/** 通过Authorization Code获取Access Token 的URL */
	public static final String QQ_ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

	/** 使用Access Token来获取用户的OpenID 的URL */
	public static final String QQ_OPEN_ID_URL = "https://graph.qq.com/oauth2.0/me";

	/** 获取用户数据的URL */
	public static final String QQ_USER_INFO_URL = "https://graph.qq.com/user/get_user_info";

	/** 发布一条动态的接口地址 */
	public static final String QQ_ADD_SHARE_URL = "https://graph.qq.com/share/add_share";

	/** 登录用户发布心情接口地址 */
	public static final String QQ_ADD_TOPIC_URL = "https://graph.qq.com/shuoshuo/add_topic";

	/** 用户发表日志接口地址 */
	public static final String QQ_ADD_ONE_BLOG_URL = "https://graph.qq.com/blog/add_one_blog";

	/** 登录用户创建相册接口地址 */
	public static final String QQ_ADD_ALBUM_URL = "https://graph.qq.com/photo/add_album";

	/** 获取登录用户的相册列表接口地址 */
	public static final String QQ_LIST_ALBUM_URL = "https://graph.qq.com/photo/list_album";

	/** 上传照片接口地址 */
	public static final String QQ_UPDATE_PIC_URL = "https://graph.qq.com/photo/upload_pic";

	/** 验证登录的用户是否为某个认证空间的粉丝 接口 */
	public static final String QQ_CHECK_PAGE_FANS_URL = "https://graph.qq.com/user/check_page_fans";

	/** 发表一条微博接口地址 */
	public static final String WEIBO_ADD_T_URL = "https://graph.qq.com/t/add_t";

	/** 根据微博ID删除指定微博 */
	public static final String WEIBO_DEL_T_URL = "https://graph.qq.com/t/del_t";

	/** 获取登录用户的听众列表 */
	public static final String WEIBO_GET_FANSLIST_URL = "https://graph.qq.com/relation/get_fanslist";

	/** 获取腾讯微博其他用户详细信息 */
	public static final String WEIBO_GET_OTHER_INFO_URL = "https://graph.qq.com/user/get_other_info";

	/** 获取登录用户收听的人的列表 */
	public static final String WEIBO_GET_IDOLIST_URL = "https://graph.qq.com/relation/get_idollist";

	/** 获取腾讯微博登录用户的用户资料 */
	public static final String WEIBO_GET_INFO_URL = "https://graph.qq.com/user/get_info";

	/** 收听腾讯微博上的用户 */
	public static final String WEIBO_ADD_IDOL_URL = "https://graph.qq.com/relation/add_idol";

	/** 取消收听腾讯微博上的用户 */
	public static final String WEIBO_DEL_IDOL_URL = "https://graph.qq.com/relation/del_idol";

	/** 获取一条微博的转播或评论信息列表 */
	public static final String WEIBO_GET_REPOST_LIST_URL = "https://graph.qq.com/t/get_repost_list";

	/** 上传一张图片，并发布一条消息到腾讯微博平台上 */
	public static final String WEIBO_ADD_PIC_T_URL = "https://graph.qq.com/t/add_pic_t";
}
