{{#each data.list}}
<li class="clearfix">
	<span class="user_pic">
		<img src="/avatar/16303.jpg" />
		<input type="checkbox"  class="regular-checkbox" name="id" value="{{id}}"/><label></label>
	</span>
	<div class="showbox clearfix">
		<span class="box_dot"></span>
		<span class="showbox_left"><b>{{title}}</b><span class="readRemark">{{msgRead status}}</span></span>
		<span class="showbox_right"><em class="addtime">{{timeMsgFormat addTime}}</em></span>
		<p id="msgContent">{{{hideMsgContent content}}}<i class="iconfont showdrop" data-id="{{id}}">&#xe61b;</i></p>
		<div class="hideContent">{{{content}}}</div>
	</div>
</li>
{{/each}}
<input name="type" type="hidden" id="type" />
