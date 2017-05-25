<div class="chy-update-log bdr1-e6e6e6 pl20 pr40 pb10">
    <div class="log-title mb20">
      <i class="log-icon h2-icon"></i><h2 class="pl10">监管车源更新记录</h2>
    </div>
    <div class="chy-detail-item ys ml30">
      <i class="log-icon time-circle">{{updateTime initList.[0].addTime}}</i>
      <i class="log-icon table-arrow"></i>
      <div class="item-wrap bdr1-e6e6e6 pt20 pr20 pl20 pb30">
        <div class="item-title">
          <i class="log-icon h3-icon"></i><h3 class="c315A8B pl10">原始抵押车源( 入 )</h3>
        </div>
        <table class="item-table">
          <thead>
            <tr>
              <th class="first">车型</th>
              <th>抵押价</th>
              <th>评估价</th>
              <th>车源照片</th>
            </tr>
          </thead>
          <tbody>
          {{#each initList}}
            <tr>
              <td class="first">{{carType}}</td>
              <td>￥{{moneyFormat mortgagePrice}}</td>
              <td>￥{{moneyFormat assessPrice}}</td>
              <td class="last"><a href="javascript:void(0)" title="{{carType}}" class="carUpdateImg" data-id="{{id}}">查看</a></td>
            </tr>
          {{/each}}
          </tbody>
          <tfoot>
            <tr>
              <td class="first">入库总价</td>
              <td>￥{{moneyFormat initTotalMortgagePrice}}</td>
              <td>￥{{moneyFormat initTotalAssessPrice}}</td>
              <td class="last"></td>
            </tr>
          </tfoot>
        </table>
        <div class="count-view count-view-position">
            <span class="borrow_money_span">
              <i>抵押价总额</i>
              <em>￥{{moneyFormat initTotalMortgagePrice}}</em>
            </span>
            <span class="span_scale_money">
              <i style="width:{{overPercentWidth initTotalMortgagePrice initTotalAssessPrice}}%"></i>
              <em></em>
            </span>
            <span class="span_estimated_money">
              <i>评估价总额</i>
              <em>￥{{moneyFormat initTotalAssessPrice}}</em>
            </span>
            <span>
              <i>超出抵押价</i>
              <em>{{beyondProportion initTotalMortgagePrice initTotalAssessPrice}}%</em>
            </span>
          </div>
      </div>
    </div>
{{#if list}}
{{#each list}}
    <div class="chy-detail-item sc ml30">
      <i class="log-icon time-circle">{{updateTime updateTime}}</i>
      <i class="log-icon table-arrow"></i>
      <div class="item-wrap bdr1-e6e6e6 pt20 pr20 pl20 pb30">
      
      {{#if outList}}
        <div class="item-title">
          <i class="log-icon h4-icon"></i><h3 class="c315A8B pl10">售出抵押车源( 出 )</h3>
        </div>
        {{#with outList}}
        <table class="item-table">
          <thead>
            <tr>
              <th class="first">车型</th>
              <th>抵押价</th>
              <th>评估价</th>
              <th>车源照片</th>
            </tr>
          </thead>
          <tbody>
          {{#each this}}
            <tr>
              <td class="first">{{carType}}</td>
              <td>￥{{moneyFormat mortgagePrice}}</td>
              <td>￥{{moneyFormat assessPrice}}</td>
              <td class="last"><a href="javascript:void(0)" title="{{carType}}" class="carUpdateImg" data-id="{{id}}">查看</a></td>
            </tr>
            {{/each}}
          </tbody>
          <tfoot>
            <tr>
              <td class="first">出库总价</td>
              <td>￥{{moneyFormat ../outTotalMortgagePrice}}</td>
              <td>￥{{moneyFormat ../outTotalAssessPrice}}</td>
              <td class="last"></td>
            </tr>
          </tfoot>
        </table>
        {{/with}}
        {{/if}}
        
        <div class="item-title">
          <i class="log-icon h3-icon"></i><h3 class="c315A8B pl10">新收抵押车源( 入 )</h3>
        </div>
        {{#with inList}}
        <table class="item-table">
          <thead>
            <tr>
              <th class="first">车型</th>
              <th>抵押价</th>
              <th>评估价</th>
              <th>车源照片</th>
            </tr>
          </thead>
          <tbody>
            {{#each this}}
            <tr>
              <td class="first">{{carType}}</td>
              <td>￥{{moneyFormat mortgagePrice}}</td>
              <td>￥{{moneyFormat assessPrice}}</td>
              <td class="last"><a href="javascript:void(0)" title="{{carType}}" class="carUpdateImg" data-id="{{id}}">查看</a></td>
            </tr>
            {{/each}}
          </tbody>
          <tfoot>
            <tr>
              <td class="first">入库总价</td>
              <td>￥{{moneyFormat ../inTotalMortgagePrice}}</td>
              <td>￥{{moneyFormat ../inTotalAssessPrice}}</td>
              <td class="last"></td>
            </tr>
          </tfoot>
        </table>
        {{/with}}
        <div class="count-view count-view-position">
            <span class="borrow_money_span">
              <i>抵押价总额</i>
              <em>￥{{moneyFormat totalMortgagePrice}}</em>
            </span>
            <span class="span_scale_money">
              <i style="width:{{overPercentWidth totalMortgagePrice totalAssessPrice}}%"></i>
              <em></em>
            </span>
            <span class="span_estimated_money">
              <i>评估价总额</i>
              <em>￥{{moneyFormat totalAssessPrice}}</em>
            </span>
            <span>
              <i>超出抵押额</i>
              <em>{{beyondProportion totalMortgagePrice totalAssessPrice}}%</em>
            </span>
          </div>
      </div>
    </div>
    {{/each}}
    {{/if}}
  </div>