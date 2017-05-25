
<div>
{{#with ppfund}}
		<div class="pic_div">
			<img class="pic" alt="图片加载中.." src="{{articleImage borrowImg.picPath url}}">
			<p class="title">{{name}}</p>
			<div class="schedule">
				<div class="progress_bar">
					<div class="progress_img" style="width:{{ppfundScales account accountYes}}%"></div>
				</div>
			</div>
			<div class="progress_val" >{{ppfundScales account accountYes}}%</div>
			<div class="data-tab">
				<table>
					<tr class="tr1">
						<td class="td1">{{ppfundAccountWait account accountYes}}元</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{ppfundTimeLimit timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">剩余金额</td>
						<td class="td2">利率</td>
						<td class="td3">期限</td>
					</tr>
				</table>
			</div>
		</div>
			{{/with}}
			{{#with estate}}
		<div class="pic_div">
			<img class="pic" alt="图片加载中.." src="{{articleImage borrowImg.picPath url}}">
			<p class="title">{{name}}</p>
			<div class="schedule">
				<div class="progress_bar">
					<div class="progress_img" style="width:{{scales}}%;"></div>
				</div>
			</div>
			<div class="progress_val">{{scales}}%</div>
			<div class="data-tab">
				<table>
					<tr class="tr1">
						<td class="td1">{{moneyFormat ../accountWait}}元</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{borrowLimitTime borrowTimeType timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">剩余金额</td>
						<td class="td2">利率</td>
						<td class="td3">期限</td>
					</tr>
				</table>
			</div>
		</div>
		{{/with}}
		{{#with entrust}}
			<div class="pic_div">
			<img class="pic" alt="图片加载中.." src="{{articleImage borrowImg.picPath url}}">
			<p class="title">{{name}}</p>
			<div class="schedule">
				<div class="progress_bar">
					<div class="progress_img" style="width:{{scales}}%;"></div>
				</div>
			</div>
			<div class="progress_val">{{scales}}%</div>
			<div class="data-tab">
				<table>
					<tr class="tr1">
						<td class="td1">{{moneyFormat ../accountWait}}元</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{borrowLimitTime borrowTimeType timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">剩余金额</td>
						<td class="td2">利率</td>
						<td class="td3">期限</td>
					</tr>
				</table>
			</div>
		</div>{{/with}}
				{{#with invest}}
			<div class="pic_div">
			<img class="pic" alt="图片加载中.." src="{{articleImage borrowImg.picPath url}}">
			<p class="title">{{name}}</p>
			<div class="schedule">
				<div class="progress_bar">
					<div class="progress_img" style="width:{{scales}}%;"></div>
				</div>
			</div>
			<div class="progress_val">{{scales}}%</div>
			<div class="data-tab">
				<table>
					<tr class="tr1">
						<td class="td1">{{moneyFormat ../accountWait}}元</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{borrowLimitTime borrowTimeType timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">剩余金额</td>
						<td class="td2">利率</td>
						<td class="td3">期限</td>
					</tr>
				</table>
			</div>
		</div>{{/with}}
		</div>
		