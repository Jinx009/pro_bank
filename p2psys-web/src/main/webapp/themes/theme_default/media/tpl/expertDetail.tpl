<li>
{{#with expert}}
  <div class="expert-con clearfix">
    <div class="expert-left">
       <img src="{{../url}}{{picPath}}" width="180" height="180">
       <div class="expert-look">
         <span>关注他：</span><a href="http://{{blogUrl}}" class="expert-wb" target="_blank"></a><a href="javascript;" class="expert-wx"></a> 
       </div>
    </div>
    <div class="expert-right">
      <h3>{{autorName}}</h3>
      <p>{{position}}</p>
      <p>{{expertDetailContent content}}</p>
    </div>
  </div>
  {{/with}}
</li>

