var tools = window.tools,
    app = window.app;
app.highdanger = function(){
  var id = tools.linkPar('id'),
      postUrl = '/mk/admin/danger/getAllEffectiveDangerArea',//请求链接
      postData = {id:id};//请求参数(不需要可忽略)
  app.ajax(postUrl,postData,function(result){
    /* 测试数据 */
    var json = JSON.parse(result);
    /*json = [{
      name:'123',
      posArr:[{
        lat:39.936052,
        lng:116.386246
      },{
        lat:39.936052,
        lng:116.388401
      },{
        lat:39.934281,
        lng:116.388401
      },{
        lat:39.934281,
        lng:116.386246
      }]
    },{
      name:'456',
      posArr:[{
        lat:39.937052,
        lng:116.37246
      },{
        lat:39.937052,
        lng:116.387401
      },{
        lat:39.937281,
        lng:116.387401
      },{
        lat:39.937281,
        lng:116.387246
      }]
    },{
      name:'这辆车真有意思！',
      posArr:[{
        lat:39.931281,
        lng:116.381401
      },{
        lat:39.931281,
        lng:116.381246
      },{
        lat:39.931052,
        lng:116.381246
      },{
        lat:39.931052,
        lng:116.381401
      }]
    }];*/
    /* 测试数据 */
    if(json&&json.length>0){
      var pois = [];//最佳视图点合集（只取每个高危区域第一个点）
      $.each(json,function(m,n){
        var localePoint = [];
        $.each(n.posArr,function(i,v){
          var poi = new BMap.Point(v.lng,v.lat);
          localePoint.push(poi);
          if(i===0){
            pois.push(poi);
          }
        });
        //添加高危区域名称
        var label = new BMap.Label(n.name,{
          position:n.posArr[0],//指定文本标注所在的地理位置
          offset:new BMap.Size(0,-30)//设置文本偏移量
        });
        label.setStyle({
          padding:"0 2px",
          color:"red",
          fontSize:"12px",
          height:"20px",
          lineHeight:"20px",
          fontFamily:"微软雅黑"
         });
        map.addOverlay(label);
        //添加高危区域覆盖物
        var localeOverlays = new BMap.Polygon(localePoint,{strokeColor:"#567efd", strokeWeight:2, strokeOpacity:0.8,fillColor:"#567efd",fillOpacity:0.5});
        map.addOverlay(localeOverlays);
      });
      var viewport = map.getViewport(pois);
      map.centerAndZoom(viewport.center,viewport.zoom);
    }
  });
};
