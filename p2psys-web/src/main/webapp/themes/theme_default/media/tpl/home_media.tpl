<ul class="index_media_tab">
	{{#each data}}
		<li>{{title}}</li>	
	{{/each}}

</ul>
<div class="index_media_box">
	{{#each data}}
		<div class="index_media_content" style="display:none;">
			<h2><img src="{{picPath}}" width="184" height="56"></h2>
			<p>
				{{hideMediaIntroduction introduction}}...<a href="/article/detail.html?nid={{nid}}&id={{id}}">更多&gt;&gt;</a>
				<span class="sprite leftIco"></span>
				<span class="sprite rightIco"></span>
			</p>
		</div>
	{{/each}}
</div>

