	{{#with ppfund}}
	<input type="hidden" id="id" name="id" value="{{id}}"/> 
	<div class="title" style="width:90%;margin-left:5%;">
		<table>
			<tr>
				<td class="td-label">
					<div class="label-content" style="line-height:14px;margin:0;">项目推荐</div>
				</td>
				<td class="td-content">
					<div class="name" style="height:20px;line-height:20px;font-size:16px;padding-left:5px;">{{name}}</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="tab-div-1" style="margin-left:5%;">
		<table>
			<tr>
				<td class="label"></td>
				<td class="content1" style="font-size:12px;line-height:18px;width:50%;">多重保障设置</td>
				<td class="label"></td>
				<td class="content1" style="font-size:12px;line-height:18px;width:50%;">第三方资金监管</td>
			</tr>
			<tr>
				<td class="label"></td>
				<td class="content2" style="font-size:12px;line-height:18px;width:50%;">T+{{interestWay}}赎回</td>
				<td class="label"></td>
				<td class="content2" style="font-size:12px;line-height:18px;width:50%;">{{lowestAccount}}元起申购</td>
			</tr>
		</table>
	</div>
	<div class="sch_andriod">
		<div class="circle_andriod">
			{{scales}}%
		</div>
	</div>
	<div class="tab-div-2" style="width:90%;margin-left:5%;">
		<table>
			<tr class="tr1">
				<td style="width: 33.33%">{{ppfundAccountWait account accountYes}}<span class="units">元</span></td>
				<td style="width: 33.33%">{{apr}}<span class="units">%</span></td>
				<td style="width: 33.34%">{{ppfundTimeLimit timeLimit}}</span></td>
			</tr>
			<tr class="tr2">
				<td>可投金额</td>
				<td>年化收益</td>
				<td>投资期限</td>
			</tr>
		</table>
	</div>
	{{/with}}