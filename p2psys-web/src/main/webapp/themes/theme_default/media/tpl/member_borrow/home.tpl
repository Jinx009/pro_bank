{{#with uc}}
<div class="company-banner">
      <img src="/themes/theme_default/images/member_borrow_bg.jpg" alt="" >
  </div>
  <div class="company-info-box bdr1-e6e6e6 mb20 pb20 pl20 pr20 bg-white">
    <img  class="bg-white bdr1-e6e6e6 float_left mr20" src="/themes/theme_default/images/tx_04.jpg" alt="" width="160" height="160">
    <dl class="float_left">
      <dt class="f24 mb35"><i></i>{{companyName}}</dt>
      <dd>公司类型：<b>{{transCompanyType companyType}}</b></dd>
      <dd>所属城市：<b>{{province}} {{city}} {{area}}</b></dd>
      <dd>公司注册号：<b>{{companyRegNo}}</b></dd>
      <dd>税务登记证号：<b>{{taxRegNo}}</b></dd>
      <dd class="address">公司地址：<b>{{address}}</b></dd>
    </dl>
  </div>
  <div id="container" class="container">
    <div class="company-introduce-box bdr1-e6e6e6 bg-white float_left">
      <h3 class="c315A8B f18 mb20 ">公司简介</h3>
      <p class="mb20 pb25">{{description}}</p>
      <h3 class="c315A8B f18 mb20">相关图片</h3>
      <div class="f16 pl40">
      {{#if jyzzPicPath}}
      <span><a class="fancybox" title="经营执照" href="{{urlAdd ../../url jyzzPicPath}}"><img src="{{urlAdd ../../url jyzzPicPath}}" alt="" style="width:200px;height:150px;"></a><br>经营执照</span>
      {{/if}}
      {{#if swdjPicPath}}
      <span><a class="fancybox" title="税务登记证" href="{{urlAdd ../../url swdjPicPath}}"><img src="{{urlAdd ../../url swdjPicPath}}" alt="" style="width:200px;height:150px;"></a><br>税务登记证</span>
      {{/if}}
      {{#if zzjgPicPath}}
      <span><a class="fancybox" title="组织机构证" href="{{urlAdd ../../url zzjgPicPath}}"><img src="{{urlAdd ../../url zzjgPicPath}}" alt="" style="width:200px;height:150px;"></a><br>组织机构证</span>
      {{/if}}
      {{#if grzxPicPath}}
      <span><a class="fancybox" title="个人征信报告" href="{{urlAdd ../../url grzxPicPath}}"><img src="{{urlAdd ../../url grzxPicPath}}" alt="" style="width:200px;height:150px;"></a><br>个人征信报告</span>
      {{/if}}
      {{#if khxkPicPath}}
      <span><a class="fancybox" title="开户许可证" href="{{urlAdd ../../url khxkPicPath}}"><img src="{{urlAdd ../../url khxkPicPath}}" alt="" style="width:200px;height:150px;"></a><br>开户许可证</span>
      {{/if}}
      {{#if dkkPicPath}}
      <span><a class="fancybox" title="贷款卡记录（企业）" href="{{urlAdd ../../url dkkPicPath}}"><img src="{{urlAdd ../../url dkkPicPath}}" alt="" style="width:200px;height:150px;"></a><br>贷款卡记录（企业）</span>
      {{/if}}
      </div>
    </div>
    <div class="company-left-sidebar bdr1-e6e6e6 bg-white float_right">
      <h3 class="c315A8B f18 mb20">相关信息</h3>
      <p>公司电话：{{companyPhone}}</p>
      <p>公司传真：{{companyFax}}</p>
      <p>公司邮箱：{{companyEmail}}</p>
      <p>联系人：{{../realName}}</p>
      <p>联系人电话：{{../mobilePhone}}</p>
      <p>联系人邮箱：{{../email}}</p>
    </div>
  </div> 
{{/with}}