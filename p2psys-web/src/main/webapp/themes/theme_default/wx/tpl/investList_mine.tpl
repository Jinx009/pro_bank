{{#each data.list}}
<div class="show_detail">
	<div class="col w40">
		<div class="list-row overflow">
			<a href="/wx/account/detail.html?id={{borrowId}}">{{borrowName}}</a>
		</div>
		<div class="list-row">{{moneyFormat account}}元</div>
	</div>
	<div class="col w30">
		<div class="list-row list-ltr-10">{{apr}}%</div>
		<div class="list-row list-ltr-10">{{transFormatDate addTime}}</div>
	</div>
	<div class="col w30">
		<div class="list-row list-ltr-10"></div>
		{{#inequality status 1}}
		<div class="list-row list-ltr-10">----</div>
		{{else}}
		<div class="list-row list-ltr-10">{{expirationDate}}</div>
		{{/inequality}}
	</div>
</div>
{{/each}}