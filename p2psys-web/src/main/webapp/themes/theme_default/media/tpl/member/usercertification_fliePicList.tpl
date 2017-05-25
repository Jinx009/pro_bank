{{#each data}}
	<li>
		<a href="{{picPath}}" rel="group1" class="fileshow">
			<img src="{{picPath}}" />
		</a>
		{{#equal ../status -1}}
		<a href="javascript:;" title="删除" class="fileclose"  data-id="{{id}}"><i class="iconfont">&#xe60f;</i></a>
		{{/equal}}
		{{#equal ../status 0}}
		<a href="javascript:;" title="待审" class="veryfiy"  data-id="{{id}}"></a>
		{{/equal}}
	</li>
{{/each}}
{{#equal status -1}}
<li class="addFile"><a href="javascript:;">+</a></li>
{{/equal}}
