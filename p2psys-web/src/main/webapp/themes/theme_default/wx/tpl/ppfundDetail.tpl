
	<div class="invest-main-box"></div>
	<div id="formDiv" class="popup-div">
			<div class="form-div">
					<span class="get-invest" >账户余额：<span>{{account.useMoney}}元</span></span>
					<input name="money" class="input-amount" type="number" id="input-amount" placeholder="投标金额" autocomplete="off" />
					<div class="msg_error"></div>
					<!--
					{{#with account}}
					{{#equal ../ppfund.isFixedTerm 0}}
					<input class="input-amount" type="text" autocomplete="off" name="outTime" id="outTime" placeholder="预约赎回时间，格式如：2015-04-17" />
					<div class="outTime_error"></div>
					{{/equal}}
					{{/with}}
					-->
					{{#equal payPwd false}}
					<input type="password"  name="payPwd" class="pay-pwd" id="pay-pwd" placeholder="交易密码为6位数字" autocomplete="off" />
					<div class="pwd_error"></div>
					{{else}}
					<a href="/wx/account/setting.html" class="set_pwd">请先设置交易密码</a>
					{{/equal}}
					
					<input type="button" class="pay-sure" id="pay-sure" value="确定"/>
					<input type="hidden" name="projectType" value="ppfund">
					<input type="hidden" name="submitUrl" value="/wx/account/ppfundin.html">
					<i class="btn_close"></i>
			</div>
	</div>
	<div class="header">项目详情</div>

	<div class="cover-div">
		<img class="cover" alt="图片加载中.." src="{{articleImage ppfundImag.picPath url}}"/>
	</div>
{{#with ppfund}}
  <input type="hidden" name="id" value="{{id}}"/>
	<div class="title">
		<table class="title-tab">
			<tr>
				<td class="label" rowspan="3">
					<div class="label-content" style="line-height: 20px;margin-top: 10px;">现金管理</div>
				</td>
				<td colspan="4" class="name">{{name}}</td>
			</tr>
			<tr>
				<td class="check"></td>
				<td class="tag" style="width:30%;">多重保障设置</td>
				<td class="check"></td>
				<td class="tag" style="width:30%;">第三方资金监管</td>
			</tr>
			<tr>
				<td class="check"></td>
				<td class="tag" style="width:30%;">T+{{interestWay}}赎回</td>
				<td class="check"></td>
				<td class="tag" style="width:30%;">{{bidLimit lowestAccount}}元起申购</td>
			</tr>
		</table>
	</div>
	<div class="row1" >
		<table class="row1-tab" style="height:auto;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">年化收益</td>
				<td class="content-text" style="font-size:18px;line-height:24px;">
					<div class="schedule-text">{{apr}}%</div>
				</td>
			</tr>
		</table>
	</div>
	{{#inequality account 0}}
	<div class="row2">
		<table class="row2-tab" style="height:60px;">
			<tr>
				<td rowspan="2" class="content-label" style="font-size:15px;line-height:22px;">投资进度</td>
				<td class="content-schedule" style="height:90%;"><div class="tp"></div>
					<div class="schedule">
						<div class="progress_bar">
							<div class="progress_img" style="width:{{scales}}%"></div>
						</div>
					</div>
					<div class="balance">
						可投金额：<span class="balance-val"><span id="balance-val">{{ppfundAccountWait account accountYes}}</span>元</span>
					</div>
				</td>
				<td>
					<div class="progress_val" >{{scales}}%</div>
				</td>
			</tr>
		</table>
	</div>
	{{/inequality}}
	<div class="row3">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">开标时间</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">{{startTime}}</td>
			</tr>
		</table>
	</div>
	<div class="row4">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">项目规模</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">{{ppfundAccount account}}</td>
			</tr>
		</table>
	</div>
	<div class="row5" >
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">投资期限</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">
					{{ppfundTimeLimit timeLimit}}
				</td>
			</tr>
		</table>
	</div>
	<div class="row6">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">起息方式</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">T+{{interestWay}}</td>
			</tr>
		</table>
	</div>
	<div class="row7">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">收益方式</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">{{ppfundRepaymentWay isFixedTerm}}</td>
			</tr>
		</table>
	</div>
	<div class="row8">
		<h3 style="font-weight:500;margin-left:5%;">项目详情：</h3>
		<div style="font-size:12px;line-height:22px;margin:15px 0 15% 5%;" id="content">{{borrowDetailContent content}}</div>
	</div>
	
 	<input type="hidden" id="lowest_Account" value="{{lowestAccount}}"/>
    <input type="hidden" id="most_Account" value="{{mostAccount}}"/>
 {{/with}}
 <div class="invest-btn">
 	{{#inequality status 1}}
		{{#inequality ../ppfund.scales 100}}	
		<div class="btn" id="btn" style="width:90%;margin-left:5%;font-size:18px;">立即投资</div>
		{{else}}		
		<div class="btn" style="background:#ccc;">投资已结束</div>
		{{/inequality}}
	{{else}}
		<div class="btn" style="background:#ccc;">投资已关闭</div>
	{{/inequality}}
 </div>
 <input type="hidden" id="use_money" value="{{account.useMoney}}"/>
