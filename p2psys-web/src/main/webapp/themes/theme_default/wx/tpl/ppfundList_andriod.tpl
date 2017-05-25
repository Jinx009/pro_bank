{{#each data.list}}
<div class="item">
	<a href="/wx/account/ppfundDetail.html?id={{id}}" title="{{name}}">
	<table>
		<tr>
			
			<td rowspan="3" class="schedule">
				{{#inequality account 0}}
				<div class="schedule_andriod">
					<div class="circle_andriod">{{scales}}%</div>
				</div>
				{{else}}
				<div class="schedule_andriod">
					<div class="circle_andriod">无限制</div>
				</div>
				{{/inequality}}
			</td>
			
			<td colspan="2" class="title">
				<div class="title-content">
					<a href="/wx/account/ppfundDetail.html?id={{id}}" style="font-weight:bold;" title="{{name}}">{{name}}</a>
				</div>
			</td>
			<td class="label"><div class="label-content">{{lowestAccount}}起投</div></td>
		</tr>
		<tr class="data">
			{{#inequality account 0}}
			<td>{{ppfundAccountWait account accountYes}}元</td>
			{{else}}
			<td>无限制</td>
			{{/inequality}}
			<td style="padding-left:15px;">{{{apr}}}%</td>
			<td>{{ppfundTimeLimit timeLimit}}</td>
		</tr>
		<tr class="param">
			<td>可投金额</td>
			<td style="padding-left:15px;">利率</td>
			<td>期限</td>
		</tr>
	</table>
	</a>
</div>
{{/each}}