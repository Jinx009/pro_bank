
<div>
{{#with ppfund}}
		<div class="pic_div">
			<img class="pic" alt="ͼƬ������.." src="{{articleImage borrowImg.picPath url}}">
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
						<td class="td1">{{ppfundAccountWait account accountYes}}Ԫ</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{ppfundTimeLimit timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">ʣ����</td>
						<td class="td2">����</td>
						<td class="td3">����</td>
					</tr>
				</table>
			</div>
		</div>
			{{/with}}
			{{#with estate}}
		<div class="pic_div">
			<img class="pic" alt="ͼƬ������.." src="{{articleImage borrowImg.picPath url}}">
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
						<td class="td1">{{moneyFormat ../accountWait}}Ԫ</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{borrowLimitTime borrowTimeType timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">ʣ����</td>
						<td class="td2">����</td>
						<td class="td3">����</td>
					</tr>
				</table>
			</div>
		</div>
		{{/with}}
		{{#with entrust}}
			<div class="pic_div">
			<img class="pic" alt="ͼƬ������.." src="{{articleImage borrowImg.picPath url}}">
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
						<td class="td1">{{moneyFormat ../accountWait}}Ԫ</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{borrowLimitTime borrowTimeType timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">ʣ����</td>
						<td class="td2">����</td>
						<td class="td3">����</td>
					</tr>
				</table>
			</div>
		</div>{{/with}}
				{{#with invest}}
			<div class="pic_div">
			<img class="pic" alt="ͼƬ������.." src="{{articleImage borrowImg.picPath url}}">
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
						<td class="td1">{{moneyFormat ../accountWait}}Ԫ</td>
						<td class="td2">{{apr}}%</td>
						<td class="td3">{{borrowLimitTime borrowTimeType timeLimit}}</td>
					</tr>
					<tr>
						<td class="td1">ʣ����</td>
						<td class="td2">����</td>
						<td class="td3">����</td>
					</tr>
				</table>
			</div>
		</div>{{/with}}
		</div>
		