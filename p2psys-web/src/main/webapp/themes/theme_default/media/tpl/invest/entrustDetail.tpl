 <div class="clearfix invest-border">
 <div class="invest-detail-box clearfix">
 <div class="invest-main-left">
    <div class="bid_info mb20 bdr1-e6e6e6 pt40 pb30 bg-white">
      <img src="{{articleImage borrowImg.picPath url}}" class="face mr30 mb20">
      <div class="invest-mid">
      {{#with borrow}}
      <input value="{{fixedTime}}" class="endtime" type="hidden">
      <dl class="clearfix float_left">
        <dt class="c315A8B">{{name}}</dt>
        
        <dd class="bid_intro"> 
          <div class="first">
                 <span class="detail-money">
                    投资规模<br/>
                    <p><em>￥{{moneyFormat account}}</em></p>
                 </span>
              </div>
              <div class="second">
                 <span class="detail-rate">
                    预期年化收益率<br/>
                    <p><em>{{apr}}</em>%</p>
                 </spn>
              </div>
              <div class="last">
                 <span class="detal-time">
                    投资期限<br/>
                    <p><em>{{borrowLimitTime borrowTimeType timeLimit}}</em></p>
                 </span>
              </div>
             
                <div class="last">
                   <span class="detail-float-rate">
                     预期收益率<br/>
                      <p><em>{{expectedLow}}</em>%~<em>{{expectedUp}}</em>%</p>
                   </span>
                </div>
  

        </dd>
      </dl>
      {{/with}}
      {{#with borrow}}
      <ul class="bdr1-e6e6e6 pt10 pb10 pl20 pr20">
        <li>还款方式：<span>{{transFormatStyle style}}</span></li>
        <li>投资奖励：<span>0%</span></li>
        <li>最少投标金额：<span>￥{{bidLimit lowestAccount}}</span></li>
        <li>最少投标金额：<span>￥{{moneyFormat lowestAccount}}</span></li>
        <!--<li>{{bidMostLimit mostAccount account}}</span></li>-->
        <li class="jd">
          <span class="detail-jd">
              <div class="float_left" style="color:#666;">投资进度：</div>
              <div class="jindu float_left">
                  <div class="rate_tiao" style="width:{{scales}}%"></div>
              </div>
              <div class="float_left" style="margin-left:10px;">{{scales}}%</div>
          </span>
        </li>
      </ul>
      {{/with}}
    </div>
    </div>

  </div>


  {{#less3 borrow.fixedTime}}
  <div class="invest-main-right" id="tz-detail">
    <div class="mb20 bdr1-e6e6e6 bg-white">
      <h3 class="invest-rightside-h3">投资金额</h3>
      {{!判断是否为预览页面  status等于0为预览页面}}
      {{#userTypeval login_user_cache.userType}}
       <div class="amount-form-box borrow_user_tip pt10 pl30 pr30" style="padding-left:30px;">
        <dl style="height:100px;">
          <dt>对不起！</dt>
          <dd>借款账户不能投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{else}}

      {{#inequality userInvestIdentify.realNameStatus 0}}
      {{#inequality borrow.status 59}}
      {{#inequality borrow.status 0}}
      {{#if borrowPreview}}
      {{#equal userStates 1}}
      {{#inequality borrow/scales 100}}
		<div class="amount-form-box pt10 pl20 pr20" style="padding-left:30px;">
		  	{{#with account}}
		        <p class="amount-money">账户余额：<b class="c315A8B">￥<em class="pageUseMoney">{{moneyFormat1 useMoney}}</em></b><br/>
		        可投金额：<b class="c315A8B">￥<span class="ok-money">{{moneyFormat1 ../accountWait}}</span></b></p>
		        <form action="/invest/tender.html" name="form1" id="invest_detail_form1" method="post" class="amount-form" onkeydown="if(event.keyCode==13) return false; " autocomplete="off" target="_blank">
              <input type="hidden" name="borrowId" value="{{../borrow.id}}" id="borrowId">
		          <div class="form-group">
		             投标金额：<input type="text" id="money" autocomplete="off" name="money" size="11" onkeyup="if(this.value==this.value2);if(this.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;" placeholder="输入投标金额" style="width:170px;border:1px solid #f63"/>
                        <div class="form_group_tip form_mony_tip hide">请输入投标金额进行投标</div><i>元</i>
		          </div>
               {{#if ../isDirectional}}
                      <div class="form-group">
                          定向密码：<input type="password" class="isDirectional" autocomplete="off" name="pwd" size="11" placeholder="输入定向密码" style="width:180px; border-radius:4px;"/>
                          <div class="form_group_tip form_directional_tip hide"></div>
                          <i class="lock"></i>
                      </div>
                      {{/if}}
                      <div class="form-group">
                          交易密码：<input type="password" class="payPwd" autocomplete="off" name="payPwd" size="11" placeholder="输入交易密码" />
                          <div class="form_group_tip form_trade_tip hide"></div>
                          <i class="lock"></i>
                      </div>
               <div class="form-group">
              {{#equal ../isOpenAip false}}
                          {{#if ../../packets}}
                          <div class="form-red">
                          <a href="javascript:;" class="choiceRedPacket" title="我的红包">我的红包&nbsp;&gt;&gt;</a>
                           </div>
                           {{/if}}
               {{else}}
                          {{#equal ../../apiCode 3}}
                              {{#if ../../../packets}}
                              <div class="form-red">
                              <a href="javascript:;" class="choiceRedPacket" title="我的红包">我的红包&nbsp;&gt;&gt;</a>
                               </div>
                               {{/if}}
                          {{/equal}}
              {{/equal}}
                      </div>
                       <div class="form-group">
               <input type="checkbox" checked="checked" id="dianji"  name="agree" value="1" style="width:20px;padding:0;">我已阅读并且同意<a id="protocolPreview" href="javascript:;" style="color:#2370b6;">
                &lt;&lt;服务条款协议&gt;&gt;
                 <div class="form_group_tip form_xieyi_tip hide" style="top:32px; left:28px;"></div>
                </div>
                     
		          <div class="form-action" style="text-align:left;">
		            <input type="hidden" id="bid_id" name="id" value="{{../borrow/id}}"/>
		            <input type="hidden" name="tenderToken" value="{{../tenderToken}}"/>
		            <input type="hidden" name="hasTender" value="{{../hasTender}}"/>
		            <input type="button" value="立即投资" class="green-btn sub-invest-btn">
                <span class="all-tb">全投</span>
		          </div>
                    {{!S红包dom结构}}
                      <div  id="choiceRedPacket" class="hide choiceRedPacketBox" style="background:#ffffff;height:420px;overflow-y:scroll;">
                      <label class="checkAllRedPacket" for="checkAllRedPacket"><input type="checkbox" id="checkAllRedPacket"/>全选</label>
                        <ul class="clearfix">
                        {{#each ../packets}}
                          <li>
                          <label for="choiceRedPacket{{@index}}">
                          <p  class="redPacketName">{{name}}</p>
                            <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{amount}}</i><input type="checkbox" name="ids" value="{{id}}" id="choiceRedPacket{{@index}}"/></p>
                            <p class="expirationTimeP">20天后过期</p>
                            </label>
                          </li>
                          {{/each}}
                        </ul>
                        <p>选中红包总额：<span class="choiceRedPacketMoney">0.00</span>元</p>
                        <p >实际支付金额：<span class="investMoney">0.00</span>元</p>
                        <div class="redPacketConfirm"><a href="javascript:;" id="redPacketConfirm" class="green-btn">确定</a></div>
                      </div>
                      {{!E红包dom结构}}
		        </form>
		      {{/with}}
            </div>
      {{else}}
      {{#with borrow}}
      <div class="amount-form-box borrow_full pt10 pl30 pr30">
        <p>投资笔数：<em>{{../data.page.total}}笔</em></p>
        <p>当前状态：<em>{{logBorrowStatusFun status scales type flow}}</em></p>
        <p class="disabled_btn">投资已结束</p>
        <p>欢迎进入<a href="/invest/index.html" id="investDetail">投资列表</a>，投资其他借款。</p>
      </div>
      {{/with}}
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/inequality}}
      {{else}}
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>您当前未登录，不能进行投资。</dd>
        </dl>
        <a class="login_account" href="/member/main.html">进入账户中心</a>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}"/>
      {{/equal}}
      {{else}}
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>此页面为预览页面，不能进行投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/if}}
      {{else}}
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>等待管理员初审，不能进行投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/inequality}}
      {{else}}
        <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl style="height:100px;">
          <dt>对不起！</dt>
          <dd>该标已被管理员撤回。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/inequality}}
       {{else}}
        <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl style="height:100px;">
          <dt>对不起！</dt>
          <dd>该账号未实名认证，不能进行投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../borrow/id}}" />
      {{/inequality}}
      {{/userTypeval}}
    </div>
  
  </div>
  
{{else}}
  {{#with account}}
    <div class="endtime-djs" style="display:none">
      <div class="invest-main-right" id="dsj-detail1">
    <div class="amount-form-box pt10 pl20 pr20" style="padding-left:30px;border-left: 1px solid #e9e7e1;">
       <form action="">
      <div class="mb20 bdr1-e6e6e6 bg-white" style="border:0;">
        <div class="yy-djs clearfix">
           <h3 class="invest-rightside-h3" style="padding:0; margin-bottom:20px;float:left;">开标倒计时</h3>
           <div id="enddjs" style="float:left;position:relative;top:2px;left:17px;"></div>
        </div>
       <p class="amount-money">账户余额：<b class="c315A8B">￥<em class="pageUseMoney">{{moneyFormat1 useMoney}}</em></b><br/>
            可预约金额：<b class="c315A8B">￥<span class="ok-money">
            {{moneyFormat1 ../sumBidMoney}}</span></b></p>
              <div class="form-group">
                 预约金额：<input type="text" id="money" readonly="readonly" autocomplete="off" name="" size="11"  placeholder="输入投标金额" style="width:170px;border:1px solid #f63"/>
                        <div class="form_group_tip form_mony_tip hide">请输入投标金额进行投标</div><i>元</i>
              </div>
             {{#equal ../../payPwd false}}
              <div class="form-group">
                交易密码：<input type="password" readonly="readonly" class="payPwd"  name="" size="11" placeholder="交易密码" autocomplete="off" />
                  <div class="form_group_tip form_trade_tip hide"></div>
                   <i class="lock"></i>
              </div>
              {{else}}
              <div class="form-group">
                    交易密码：<a href="/member/security/setting.html" target="_blank" style="color:#c83e41">请设置交易密码</a>
              </div>
           {{/equal}}
             {{#if ../isDirectional}}
                      <div class="form-group">
                          定向密码：<input type="password" class="isDirectional" autocomplete="off" name="" size="11" placeholder="输入定向密码" style="width:180px; border-radius:4px;"/>
                          <div class="form_group_tip form_directional_tip hide"></div>
                          <i class="lock"></i>
                      </div>
                      {{/if}}
        </div>
         <div  style="text-align:left;">
                <input type="button" value="预约结束" class="green-btn sub-invest-btn2" disabled="disabled">
          </div>
        
         </form>
      </div>
   </div>
  </div>


   <div class="invest-main-right" id="dsj-detail">
    <div class="amount-form-box pt10 pl20 pr20" style="padding-left:30px;border-left: 1px solid #e9e7e1;">
       <form action="" name="form1" id="invest_detail_form" method="post" class="amount-form" onkeydown="if(event.keyCode==13) return false; " autocomplete="off" target="_blank">
        <input type="hidden" name="borrowId" value="{{../borrow.id}}" id="borrowId">
      <div class="mb20 bdr1-e6e6e6 bg-white" style="border:0;">
        <div class="yy-djs clearfix">
           <h3 class="invest-rightside-h3" style="padding:0; margin-bottom:20px;float:left;">开标倒计时</h3>
           <div id="djs" style="float:left;position:relative;top:2px;left:17px;"></div>
        </div>
       <p class="amount-money">账户余额：<b class="c315A8B">￥<em class="pageUseMoney">{{moneyFormat1 useMoney}}</em></b><br/>
            可预约金额：<b class="c315A8B">￥<span class="ok-money">
            {{moneyFormat1 ../sumBidMoney}}</span></b></p>
              <div class="form-group">
                 预约金额：<input type="text" id="money" autocomplete="off" name="money" size="11" onkeyup="if(this.value==this.value2);if(this.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;" placeholder="输入投标金额" style="width:170px;border:1px solid #f63"/>
                        <div class="form_group_tip form_mony_tip hide">请输入投标金额进行投标</div><i>元</i>
              </div>
             {{#equal ../../payPwd false}}
              <div class="form-group">
                交易密码：<input type="text" class="payPwdText"  size="11" autocomplete="off"/><input type="password" class="payPwd"  name="payPwd" size="11" placeholder="交易密码" autocomplete="off" style="display:none;"/>
                  <div class="form_group_tip form_trade_tip hide"></div>
                   <i class="lock"></i>
              </div>
              {{else}}
              <div class="form-group">
                    交易密码：<a href="/member/security/setting.html" target="_blank" style="color:#c83e41">请设置交易密码</a>
              </div>
           {{/equal}}
                {{#if ../isDirectional}}
                      <div class="form-group">
                          定向密码：<input type="password" class="isDirectional" autocomplete="off" name="pwd" size="11" placeholder="输入定向密码" style="width:180px; border-radius:4px;"/>
                          <div class="form_group_tip form_directional_tip hide"></div>
                          <i class="lock"></i>
                      </div>
                      {{/if}}
               <div class="form-group">
               <input type="checkbox" checked="checked" id="dianji"  name="agree" value="1" style="width:20px;padding:0;">我已阅读并且同意<a id="protocolPreview" href="javascript:;" style="color:#2370b6;">
                &lt;&lt;服务条款协议&gt;&gt;
                 <div class="form_group_tip form_xieyi_tip hide" style="top:32px; left:28px;"></div>
                </div>
        </div>
         <div class="form-action" style="text-align:left;">
                <input type="hidden" id="bid_id" name="id" value="{{../borrow/id}}"/>
                <input type="hidden" name="tenderToken" value="{{../tenderToken}}"/>
                <input type="hidden" name="hasTender" value="{{../hasTender}}"/>
                <input type="button" value="预约投资" class="green-btn sub-invest-btn1">
                <span class="all-tb">全投</span>
          </div>
        
         </form>
      </div>
   </div>
   {{/with}}
    {{/less3}}



  </div>
  </div>


    <div class="mb20 bdr1-e6e6e6 bg-white" style="width:1190px; margin:0 auto;">
      <div class="detail-menus clearfix">
        <div class="detail_menus_center">
          {{#if userBaseInfo}}<a href="javascript:void(0)" class="a1">借款方详情</a>{{/if}}
          <a href="javascript:void(0)" class="a2">产品详情</a>
          {{#if borrowUploads}}<a href="javascript:void(0)" class="a4">产品资料</a>{{/if}}
          <a href="javascript:void(0)" class="a5">投资记录</a>
          <p id="invest_btn" class="js_hide" href="javascript:;">立即投资</p>
        </div>
      </div>
      <div class="invest_detail_content">
      {{#with userBaseInfo}}
        <div class="borrower-info mb30">
          <h3 class="invest-detail-h3">借款方详情</h3>
          <h4>借款人信息</h4>
          <ul class="clearfix">
            <li><label for="">用户名：</label>{{../userInvestIdentify/userName}}</li>
            <li><label for="">婚姻状况：</label>{{transMaritalStatus maritalStatus}}</li>
            <li><label for="">收入范围：</label>{{transMonthIncomeRange monthIncomeRange}}</li>
            <li><label for="">年　龄：</label>{{transBirthday birthday}}</li>
            <li><label for="">户籍城市：</label>{{province}} {{city}}</li>
            <li><label for="">车　　产：</label>{{transValue carStatus}}</li>
            <li><label for="">学　历：</label>{{transEducation education}}</li>
            <li><label for="">工作时间：</label>{{transWorkExperience workExperience}}</li>
            <li><label for="">房　　产：</label>{{transValue houseStatus}}</li>
          </ul>
          <h4 class="mt10">资料审核状态</h4> 
          <table>
          <thead>
            <tr>
              <th>审核项目</th>
              <th>状态</th>
              <th>通过时间</th>
            </tr>
          </thead>
          <tbody>
          {{#each ../certificationApply}}
            <tr>
              <td>{{typeName}}</td>
              <td><img src="/themes/theme_default/media/bg/ok.png" alt=""></td>
              <td>{{timeMonthFormat addTime}}</td>
            </tr>
            {{/each}}
          </tbody>
          </table>
        </div>
        {{/with}}
        {{#with borrow}}
        <div class="borrow-desc mb30">
          <h3 class="invest-detail-h3">产品详情</h3>
          <div class="pl30 pt20">
            <p style="text-indent:2em">{{borrowDetailContent content}}</p>
          </div>
        </div>
        {{/with}}
    
    {{#if borrowUploads}}
        <div class="borrow-detail mb30">
          <h3 class="invest-detail-h3">产品资料</h3>
          <div class="tab-content" style="width:800px; margin:50px auto 0;">
            <div class="album">
              <ul class="img-box bdr1-e6e6e6">
              {{#each borrowUploads}}
                {{#equal type 4}}
                  <li><a href="{{picPath}}" class="fancybox" ><img src="{{picPath}}" alt="借款资料"></a></li>
                {{else}}
                  <li><a href="{{../../url}}{{picPath}}" class="fancybox" ><img src="{{../../url}}{{picPath}}" alt="借款资料"></a></li>
                {{/equal}}
              {{/each}}
              </ul>
              <a href="javascript:void(0)" class="prev"></a>
              <a href="javascript:void(0)" class="next"></a>
              <ul class="hd">
              {{#each borrowUploads}}
                <li></li>
              {{/each}}
              </ul>
            </div>
          </div>
        </div>
        {{/if}}
        <div class="invest-record">
          <h3 class="invest-detail-h3">投资记录</h3>
          <table id="invest-txjl-tbl">
            <thead>
              <tr>
                <th>序号</th>
                <th>投资人</th>
                <th>投资金额</th>
                <th>投资时间</th>
                <th>投资方式</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody>
            {{#if data.list}}
            {{#each data.list}}
              <tr>
                <td>{{addOne @index}}</td>
                <td>{{userName}}</td>
                <td>￥{{money}}</td>
                <td>{{transFormatDate addTime}}</td>
                <td>手动投标</td>
                <td><i class="icon success"></i></td>
              </tr>
            {{/each}}
            {{else}}
            <tr><td colspan="6">暂无投资记录</td></tr>
            {{/if}}
            </tbody>
          </table>
          <div id="kkpager" class="ml20 mr20"></div>
        </div>
      </div>
    </div>
