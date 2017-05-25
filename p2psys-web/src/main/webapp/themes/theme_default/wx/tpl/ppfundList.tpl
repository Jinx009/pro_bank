{{#each data.list}}
<div class="item">
	<a href="/wx/account/ppfundDetail.html?id={{id}}" title="{{name}}">
	<table>
		<tr>
			
			<td rowspan="3" class="schedule">
				{{#inequality account 0}}
				<div>
					<div class="circle" id="myStat" data-dimension="80"
						data-text="{{scales}}%" data-info="" data-width="5" data-fontsize="12"
						data-percent="{{scales}}" data-fgcolor="#81D8D0" data-bgcolor="#F1EFEF"
						data-fill="#FFFFFF"></div>
				</div>
				{{else}}
				<div>
					<div class="circle" id="myStat" data-dimension="80"
						data-text="无限制" data-info="" data-width="5" data-fontsize="12"
						data-percent="无限制" data-fgcolor="#81D8D0" data-bgcolor="#F1EFEF"
						data-fill="#FFFFFF"></div>
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