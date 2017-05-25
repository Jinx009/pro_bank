 <div class="invest-main-left">
    <div class="bid_info mb20 bdr1-e6e6e6 pt40 pl40 pr40 pb30 bg-white">
      <img src="/themes/theme_default/images/wytz_face.png" class="face mr30 mb20">
      {{#with bond}}
      <dl class="clearfix float_left">
        <dt class="c315A8B">{{borrowName}} <div class="investSchedule"><span>{{progress soldCapital bondMoney}}%</span><input type="text" class="knob" value="{{progress soldCapital bondMoney}}" /></div></dt>
        <dd class="bid_type">{{borrowDetailsTypeName type}} 债权转让编号：{{name}}&nbsp;&nbsp;&nbsp;{{{protocolPreview status}}}</dd>
        <dd class="bid_intro"> 
          <div class="first"><span><i class="icon money"></i>债权总价</span><br><b class="c315A8B f24">{{moneyFormat bondMoney}} <em>元</em></b></div>
          <div class="second"><span><i class="icon pecent"></i>年化收益</span><br><b class="c315A8B f24">{{apr}}<em>%</em></b></div>
          <div class="last"><span><i class="icon clock-gray"></i>剩余期限</span><br>{{remainDays}}天</div>
        </dd>
      </dl>
      <ul class="bdr1-e6e6e6 pt10 pb10 pl20 pr20">
        <li><label>还款方式</label><span>{{transFormatStyle borrowStyle}}</span></li>
        <li><label class="w80">发布时间</label><span>{{timeMonthFormat addTime}}</span></li>
        <li><label>剩余债权</label><span>{{transRemainMoney bondMoney soldCapital}}元</span></li>
        <li>
        <label>最小投资金额</label><span>{{moneyFormat lowestAccount}}</span>
        </li>
        <li><label class="w80">结束时间</label><span>{{timeMonthFormat lastRepaymentTime}}</span></li>
        <li><label>折让率<i class="iconfont" title="出让人在转让该债权时，折让的本金占本金的比例。">&#xe641;</i></label><span class="discount">{{bondApr}}</span>%</li>
      </ul>
      {{/with}}
    </div>

    <div class="mb20 bdr1-e6e6e6 bg-white">
      <div class="detail-menus clearfix">
        <div class="detail_menus_center">
          {{#with userBaseInfo}}<a href="javascript:void(0)" class="a1">借款方详情</a>{{/with}}
          <a href="javascript:void(0)" class="a2">借款描述</a>
          {{#inequality borrow.type 101}}<a href="javascript:void(0)" class="a4">借款资料</a>{{/inequality}}
          <a href="javascript:void(0)" class="a5">债权转让记录</a>
          <p id="invest_btn" class="js_hide" href="javascript:;">立即投资</p>
        </div>
      </div>
      <div class="invest_detail_content">
      {{#with userBaseInfo}}
        <div class="borrower-info mb30">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>借款方详情</h3>
          <h4>借款人信息</h4>
          <ul class="clearfix">
            <li><label for="">用户名：</label>{{../borrow/userName}}</li>
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
          <h3 class="invest-detail-h3"><i class="icon clip"></i>借款描述</h3>
          <div class="pl30 pt20">
            <p style="text-indent:2em">{{borrowDetailContent content}}</p>
          </div>
        </div>
        {{/with}}
        {{!-- 秒标不显示借款资料--}}
        {{#inequality borrow.type 101}}
        <div class="borrow-detail mb30">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>借款资料</h3>
          <div class="tab-content">
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
        {{/inequality}}
        <div class="invest-record">
          <h3 class="invest-detail-h3"><i class="icon clip"></i>债权转让记录</h3>
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
            {{#if bondTenderList.list}}
            {{#each bondTenderList.list}}
              <tr>
                <td>{{addOne @index}}</td>
                <td>{{userName}}</td>
                <td>￥{{tenderMoney}}</td>
                <td>{{transFormatDate addTime}}</td>
                <td>{{transFormatTenderType 0}}</td>
                <td><i class="icon success"></i></td>
              </tr>
            {{/each}}
            {{else}}
            <tr><td colspan="6">暂无债权转让记录</td></tr>
            {{/if}}
            </tbody>
          </table>
          <div id="kkpager" class="ml20 mr20"></div>
        </div>
      </div>
    </div>
  </div>

  <div class="invest-main-right">
    <div class="mb20 bdr1-e6e6e6 bg-white clearfix">
      <h3 class="invest-rightside-h3 float_right" style="width:318px;">债权转让</h3>
      {{! S-判断标是否投满}}
      {{#inequality bond.bondMoney bond.soldCapital}}
        <div class="amount-form-box pt10 pl30 pr30 bg-white float_right">
            
           {{! S-判断是否登录}}
           {{#if account}}
                   {{#with bond}}
                        <p class="amount-money">可用余额：<b class="pageUseMoney">{{../account.useMoney}}</b>元</p>
                        <p class="amount-money">可投金额：<b class="pageactualDelivery">{{transRemainMoney bondMoney soldCapital}}</b>元</p>
                        <form action="/bond/bondTender.html" name="form1" id="invest_detail_form1" method="post" class="amount-form" onkeydown="if(event.keyCode==13) return false; " autocomplete="off" target="_blank">
                          <div class="form-group">
                             <em class="iconfont">&#xe63b;</em><input maxlength="70" type="text" id="money"  name="money" size="11" onkeyup="if(this.value==this.value2);if(this.value.search(/^\d*(?:\.\d{0,2})?$/)==-1)this.value=(this.value2)?this.value2:'';else this.value2=this.value;" placeholder="投标金额" autocomplete="off" style="border:1px solid #f63;"/>
                             <div class="form_group_tip form_mony_tip hide"><b class="iconfont">&#xe60f;</b>请输入投标金额进行投标</div><b>元</b>
                          </div>
                           {{#if ../packets}}
                            <div class="form-red">
                            <a href="javascript:;" class="choiceRedPacket" title="使用红包">使用红包</a>
                             </div>
                           {{/if}}
                           <div class="form-money" style="padding-top:10px;">
                               折让金额：<span class="discountMoney">0</span>元
                           </div>
                           <div class="form-money" style="padding-top:10px; margin-bottom:20px;">
                           实际应付：<span class="actualDelivery">0</span>元<!--<a class="textTips" title="实际应付=投标金额*（1-折让率）<br>- 选中红包金额+应付收益" href="javascript:;"><i class="iconfont">&#xe641;</i></a>-->
                           </div>

                          {{! S-交易密码 托管没有交易密码}}
                              {{#equal ../isOpenApi false}}
                              {{! S-判断是否设置交易密码}}
                               {{#equal ../../payPwd false}}
                              <div class="form-group">
                                  <em class="iconfont">&#xe617;</em><input type="text" class="payPwdText"  size="11" autocomplete="off"/><input type="password" class="payPwd"  name="payPwd" size="11" placeholder="交易密码" autocomplete="off" style="display:none;"/>
                                  <div class="form_group_tip form_trade_tip hide"></div>
                              </div>
                              {{else}}
                              <div class="form-group">
                                    交易密码：<a href="/member/security/setting.html" target="_blank" style="color:#c83e41">请设置交易密码</a>
                              </div>
                              {{/equal}}
                              {{! E-判断是否设置交易密码}}
                              {{/equal}}
                         {{! E-交易密码 托管没有交易密码}}
                          <div class="form-action">
                            <input type="hidden" id="bid_id" name="id" value="{{../bond/id}}"/>
                            <input type="hidden" name="bondTenderToken" value="{{../bondTenderToken}}">
                            <input type="button" value="立即受让" class="green-btn sub-invest-btn">
                          </div>
                            {{! S-红包dom结构}}
                              <div  id="choiceRedPacket" class="hide choiceRedPacketBox">
                              <label class="checkAllRedPacket" for="checkAllRedPacket"><input type="checkbox" id="checkAllRedPacket"/>全选</label>
                                <ul class="clearfix">
                                {{#each ../packets}}
                                  <li>
                                  <label for="choiceRedPacket{{@index}}">
                                  <p  class="redPacketName">{{name}}</p>
                                    <p class="redPacketMoneyP">￥<i class="redPacketMoney">{{amount}}</i><input type="checkbox" name="ids" value="{{id}}" id="choiceRedPacket{{@index}}"/></p>
                                    <p class="expirationTimeP">{{lastDays}}天后过期</p>
                                    </label>
                                  </li>
                                  {{/each}}
                                </ul>
                                <p>选中红包总额：<span class="choiceRedPacketMoney">0.00</span>元</p>
                                <p >实际支付金额：<span class="actualDelivery">0.00</span>元</p>
                                <div class="redPacketConfirm"><a href="javascript:;" id="redPacketConfirm" class="green-btn">确定</a></div>
                              </div>
                              {{! E-红包dom结构}}
                        </form>
                      {{/with}}
           {{else}}
           请立刻登录
           {{/if}}      
           {{! E-判断是否登录}}     

            </div>
      {{else}}
          {{#with bond}}
          <div class="amount-form-box borrow_full pt10 pl30 pr30 bdr1-e6e6e6 bg-white float_right">
            <p>投资笔数：<em>{{../bondTenderList.page.total}}笔</em></p>
            <p>当前状态：<em>转让完成</em></p>
            <p class="disabled_btn">转让已结束</p>
            <p>欢迎进入<a href="/bond/index.html">转让专区</a>，投资其他债权。</p>
          </div>
          {{/with}}
          <input type="hidden" id="bid_id" value="{{../bond/id}}" />
      {{/inequality}}
      {{! E-判断标是否投满}}
      <!-- 当前账号不能投资
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>当前账号无投资权限，不能进行投资。</dd>
        </dl>
        <a class="login_account" href="/member/main.html">进入账户中心</a>
      </div>
      <input type="hidden" id="bid_id" value="{{../bond/id}}"/> -->
    <!-- 预览页面
      <div class="amount-form-box borrow_user_tip pt10 pl30 pr30">
        <dl>
          <dt>对不起！</dt>
          <dd>此页面为预览页面，不能进行投资。</dd>
        </dl>
      </div>
      <input type="hidden" id="bid_id" value="{{../bond/id}}" />
     -->
    </div>
    
<div class="guarantee mb20 bdr1-e6e6e6 bg-white">
       <span class="title">借款人信息</span>
        <div class="detial-peoplenews clearfix">
           <img src="{{webroot}}/avatar/{{borrow.userId}}.jpg">
           <p>
            借款人：<em>{{borrow.userName}}</em><br/>
       {{#with userBaseInfo}}
            所属城市：{{province}} {{city}}
       {{/with}}
          </p>
        </div>
        <ul class="clearfix">
           <li><em class="detail-peopleico1"></em>身份证</li>
           <li><em class="detail-peopleico2"></em>收入证明</li>
           <li><em class="detail-peopleico3"></em>抵押资料</li>
           <li><em class="detail-peopleico4"></em>担保资料</li>
           <li><em class="detail-peopleico5"></em>信用报告</li>
        </ul>
    </div>

  </div>
