{{#each data.list}}
	<li data-id="{{id}}" id="arclist-li">
		<div class="clearfix">
		<div class="float_left">
			<h2><a href="/article/detail.html?nid={{nid}}&id={{id}}" title="{{title}}">
			{{hideNoticeTitle title}}</a></h2>
		</div>
		</div>
	</li>
{{/each}}