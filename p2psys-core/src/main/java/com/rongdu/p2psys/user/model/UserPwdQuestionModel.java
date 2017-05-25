package com.rongdu.p2psys.user.model;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.domain.UserPwdQuestion;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 密保问题Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月26日
 */
public class UserPwdQuestionModel extends UserPwdQuestion {

	private long id1;
	private String question1;
	private String answer1;
	private long id2;
	private String question2;
	private String answer2;
	private long id3;
	private String question3;
	private String answer3;

	public void validForm() {
		if (StringUtil.isBlank(this.answer1)) {
			throw new UserException("第一个问题答案不能为空！", 1);
		} else if (StringUtil.isBlank(this.answer2)) {
			throw new UserException("第二个问题答案不能为空！", 1);
		} else if (StringUtil.isBlank(this.answer3)) {
			throw new UserException("第三个问题答案不能为空！", 1);
		} else if (question1.equals(question2) || question1.equals(question3) || question2.equals(question3)) {
			throw new UserException("密保问题重复，请重新设置！", 1);
		}
	}

	public long getId1() {
		return id1;
	}

	public void setId1(long id1) {
		this.id1 = id1;
	}

	public String getQuestion1() {
		return question1;
	}

	public void setQuestion1(String question1) {
		this.question1 = question1;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public long getId2() {
		return id2;
	}

	public void setId2(long id2) {
		this.id2 = id2;
	}

	public String getQuestion2() {
		return question2;
	}

	public void setQuestion2(String question2) {
		this.question2 = question2;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public long getId3() {
		return id3;
	}

	public void setId3(long id3) {
		this.id3 = id3;
	}

	public String getQuestion3() {
		return question3;
	}

	public void setQuestion3(String question3) {
		this.question3 = question3;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
}
