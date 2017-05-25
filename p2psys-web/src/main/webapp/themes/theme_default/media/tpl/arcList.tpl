{{#each data.list}}
	<li data-id="{{id}}">
		<div class="clearfix">
			<div class="article-left"><img src="{{articleImage picPath ../url}}" alt=""></div>
			<div class="article-right">
				<h3><a href="/article/detail.html?nid={{nid}}&id={{id}}" title="{{title}}">
			{{hideNoticeTitle title}}</a></h3>
			    <p><a href="/article/detail.html?nid={{nid}}&id={{id}}" title="{{title}}">{{hideNoticIntroduction introduction}}</a></p>
			    <span>{{{noticeDateFormatNew addTime}}}</span>
			</div>
		</div>
	</li>
{{/each}}