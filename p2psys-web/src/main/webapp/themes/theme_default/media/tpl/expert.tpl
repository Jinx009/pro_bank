
   {{#each list}}
	<li>
	  <div class="expert-con clearfix">
	    <div class="expert-left">
	       <img src="{{../url}}{{picPath}}" width="180" height="180">
	       <div class="expert-look">
	         <span>关注他：</span><a href="{{blogUrl}}" class="expert-wb" target="_blank"></a><a  class="expert-wx"></a>
	          <div class="export-look-wx hide"> <img src="{{url}}{{wechatPath}}" width="100" height="100"></div>
	      
	       </div>
	    </div>
	    <div class="expert-right">
	      <h3>{{autorName}}</h3>
	      <p style="color:#888">{{position}}</p>
	      <p>{{expertDetailContent content}}</p>
	    </div>
	  </div>
	</li>
	{{/each}}

