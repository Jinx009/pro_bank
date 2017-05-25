
	<div class="invest-main-box"></div>
	<div id="formDiv" class="popup-div">
			<div class="form-div">
					<span class="get-invest" >账户余额：<span>{{account.useMoney}}元</span></span>
					<input name="money" class="input-amount" type="number" id="input-amount" placeholder="投标金额" autocomplete="off" />
					<div class="msg_error"></div>
					{{#equal payPwd false}}
					<input type="password"  name="payPwd" class="pay-pwd" id="pay-pwd" placeholder="交易密码为6位数字" autocomplete="off" />
					<div class="payPwd_error"></div>
					{{else}}
					<a href="/wx/account/setting.html" class="set_pwd">请先设置交易密码</a>
					{{/equal}}
					{{#with account}}
					{{#if ../isDirectional}}
					<input type="password"  name="pwd" class="pay-pwd" placeholder="定向密码" id="traders_pwd" size="11" autocomplete="off" />
					<input type="hidden" name="istraders_pwd" value="1" id="istraders_pwd"/>
					<div class="pwd_error"></div>
					{{/if}}
					{{/with}}
					<input type="button" class="pay-sure" id="pay-sure" value="确定"/>
					<input type="hidden" name="projectType" value="borrow">
					<input type="hidden" name="submitUrl" value="/wx/account/tender.html">
					<i class="btn_close"></i>
			</div>
	</div>
	<div class="header">项目详情</div>

	<div class="cover-div">
		<img class="cover" alt="图片加载中.." src="{{articleImage borrowImg.picPath url}}"/>
	</div>
{{#with borrow}}
 <input type="hidden" name="id" value="{{id}}"/>
	<div class="title">
		<table class="title-tab">
			<tr>
				<td class="label" rowspan="3">
					<div class="label-content" style="line-height: 20px;margin-top: 10px;">理财产品</div>
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
				<td class="tag" style="width:30%;">{{transFormatStyle style}}赎回</td>
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
					<div class="schedule-text">{{expectedLow}}%~{{expectedUp}}%</div>
				</td>
			</tr>
		</table>
	</div>
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
						可投金额：<span class="balance-val"><span id="balance-val">{{moneyFormat ../accountWait}}</span>元</span>
					</div></td>
				<td>
					<div class="progress_val" >{{scales}}%</div>
				</td>
			</tr>
		</table>
	</div>
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
				<td class="content-label" style="font-size:15px;line-height:22px;">投资规模</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">{{moneyFormat account}}元</td>
			</tr>
		</table>
	</div>
	<div class="row5">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">投资期限</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">
					{{borrowLimitTime borrowTimeType timeLimit}}
				</td>
			</tr>
		</table>
	</div>
	<div class="row6">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">计息方式</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">T+1</td>
			</tr>
		</table>
	</div>
	<div class="row7">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">收益方式</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">{{transFormatStyle style}}</td>
			</tr>
		</table>
	</div>
	<div class="row8" style="padding-bottom:10px;">
		<h3 style="font-weight:500;margin-left:5%;">项目详情：</h3>
		<div style="font-size:12px;line-height:22px;margin:15px 0 15% 5%;" id="content">{{borrowDetailContent content}}</div>
	</div>
	<div class="invest-btn">
		{{#inequality scales 100}}	
		<div class="btn" id="btn" style="width:90%;margin-left:5%;font-size:18px;">立即投资</div>
		{{else}}		
		<div class="btn" style="background:#ccc;">投资已结束</div>
		{{/inequality}}
    </div>
 	<input type="hidden" id="lowest_Account" value="{{lowestAccount}}"/>
    <input type="hidden" id="most_Account" value="{{mostAccount}}"/>
 {{/with}}

 <input type="hidden" id="use_money" value="{{account.useMoney}}"/>
