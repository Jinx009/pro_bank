
	<div class="invest-main-box"></div>
	<div id="formDiv" class="popup-div">
			<div class="form-div">
					<span class="get-invest" >账户余额：<span>{{account.useMoney}}元</span></span>
					<input name="money" class="input-amount"type="number" id="input-amount" placeholder="投标金额" autocomplete="off" />
					<div class="msg_error"></div>
					{{#equal payPwd false}}
					<input type="password"  name="payPwd" class="pay-pwd" id="pay-pwd" placeholder="交易密码" autocomplete="off" />
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
		<img class="cover" alt="图片加载中.." src="{{articleImage borrowImg.picPath url}}"/>
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
				<td class="tag">活期存款利率的13倍</td>
				<td class="check"></td>
				<td class="tag">免手续费</td>
			</tr>
			<tr>
				<td class="check"></td>
				<td class="tag">T+0赎回</td>
				<td class="check"></td>
				<td class="tag">{{bidLimit lowestAccount}}元起申购</td>
			</tr>
		</table>
	</div>
	<div class="row1" >
		<table class="row1-tab" style="height:auto;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">预期收益</td>
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
				<td rowspan="2" class="content-label" style="font-size:15px;line-height:22px;">项目进度</td>
				<td class="content-schedule" style="height:90%;"><div class="tp"></div>
					<div class="schedule">
						<div class="progress_bar">
							<div class="progress_img" style="width:{{ppfundScales account accountYes}}%"></div>
						</div>
					</div>
					<div class="balance">
						剩余金额：<span class="balance-val"><span id="balance-val">{{ppfundAccountWait account accountYes}}</span>元</span>
					</div>
				</td>
				<td>
					<div class="progress_val" >{{ppfundScales account accountYes}}%</div>
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
				<td class="val-font" style="font-size:16px;line-height:22px;">{{moneyFormat account}}元</td>
			</tr>
		</table>
	</div>
	<div class="row5" >
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">投资期限</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">
					<em>{{borrowLimitTime borrowTimeType timeLimit}}</em>
				</td>
			</tr>
		</table>
	</div>
	<div class="row6">
		<table style="height:50px;">
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">起息方式</td>
				<td class="val-font" style="font-size:16px;line-height:22px;">T+0</td>
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
	<div class="row8">
		<table>
			<tr>
				<td class="content-label" style="font-size:15px;line-height:22px;">项目简介</td>
				<td class="val-font" style="font-size:14px;line-height:22px;">
					<div>{{content}}</div>
				</td>
				<td class="content-btn"></td>
			</tr>
		</table>
	</div>
	
 	<input type="hidden" id="lowest_Account" value="{{lowestAccount}}"/>
    <input type="hidden" id="most_Account" value="{{mostAccount}}"/>
 {{/with}}
 <div class="invest-btn">
	{{#inequality ../ppfund.scales 100}}	
	<div class="btn" id="btn" style="width:90%;margin-left:5%;font-size:18px;">立即投资</div>
	{{else}}		
	<div class="btn" style="background:#ccc;">投资已结束</div>
	{{/inequality}}
 </div>
 <input type="hidden" id="use_money" value="{{account.useMoney}}"/>
