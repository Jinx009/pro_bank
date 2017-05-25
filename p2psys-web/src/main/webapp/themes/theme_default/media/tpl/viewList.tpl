{{#each data.list}}
<li>
	<div class="view-all">
		<div class="view-con-top">
			<div class="view-con-img"><img src="{{../url}}{{picPath}}" width="320" height="200"></div>
			<div class="view-con-title"><a href="/lcschool/detail.html?{{id}}">{{title}}</a></div>
			<input type="hidden" value="{{id}}" class="financearticleid">
		</div>
		<div class="view-con-down">
			<p><a　href="">{{indexContent content}}</a></p>
			<div class="view-con-down-people">
				<a href="">{{autorName}}</a>　|　<span>{{transFormatDate addTime}}</span>
			<div>
		</div>
	</div>
</li>
{{/each}}