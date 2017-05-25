package com.rongdu.p2psys.cooperation.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rongdu.p2psys.cooperation.domain.CooperationLogin;

public class CooperationSinaLoginWay implements CooperationLoginWay {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public CooperationSinaLoginWay() {

	}

	public CooperationSinaLoginWay(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}


	public void cooperationLogin() throws Exception {

	}

	public CooperationLogin cooperationLoginSuccess() throws Exception {
		CooperationLogin cooperationLogin = new CooperationLogin();
		return cooperationLogin;
	}

}
