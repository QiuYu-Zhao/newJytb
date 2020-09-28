var tools = window.tools,
    app = window.app;
app.parking = function(){
  var deviceNum = tools.linkPar('deviceNum'),
      pid = tools.linkPar('pid'),
      time = tools.linkPar('time'),
      stopTime = tools.linkPar('stopTime'),
      $parkingPop = $('#parking-pop'),
      dateFormat = function(a,b){
        var sT = new Date(a.replace(/-/g,"/")),
            eT = new Date(b.replace(/-/g,"/")),
            t = parseInt((eT-sT)/1000,10),
            d = parseInt(t/24/3600,10),
            h = parseInt((t-d*24*3600)/3600,10),
            i = parseInt((t-d*24*3600-h*3600)/60,10),
            s = t-d*24*3600-h*3600-i*60,
            date = (d?d+'天':'')+(h?h+'小时':'')+(i?i+'分钟':'')+(s?s+'秒':'');
        return date;
      },

      postUrl = '/mk/map/parking/getMapStopPoint',//请求链接
      postData = {deviceNum:deviceNum,time:time,stopTime:stopTime};//请求参数
  app.ajax(postUrl,postData,function(reslut){
    /* 测试数据 车牌号、开始时间、结束时间、停车时长、位置 */

      var json = JSON.parse(reslut)
    /* 测试数据 */
    if(json&&json.length>0){
      var markers = [],
          pois = [],
          onePoi = '',
          oneMarker = '';
      $.each(json,function(i,v){
        var poi = new BMap.Point(v.lng,v.lat),
            marker = new BMap.Marker(poi);
        marker.addEventListener("click",function(addressCallBack){
          /* 配置坐标点信息弹层 */
          var jsonI = json[i];
            var location = jsonI.location;
            app.getCityName(json[i],function(addressCallBack){
                if('' == location) {
                    location = addressCallBack;
                }
                var popHtml = '<p>车牌号：'+jsonI.carnum+'</p><p>开始时间：'+jsonI.sTime+'</p><p>结束时间：'+jsonI.eTime+'</p><p>停车时长：'+dateFormat(jsonI.sTime,jsonI.eTime)+'</p><p>位置：'+location+'</p>';
                $parkingPop.find('.bd').html(popHtml);
                $parkingPop.popShow({mask:false});
            })
        });
        pois.push(poi);
        markers.push(marker);
        if(v.pid==pid){//查看单个停车点
          onePoi = [poi];
          oneMarker = marker;
            console.log()
        }
      });
      var viewport = map.getViewport(!onePoi?pois:onePoi);
      map.centerAndZoom(viewport.center,viewport.zoom);
      var markerClusterer = new BMapLib.MarkerClusterer(map,{markers:markers});
      if(oneMarker){
          //添加动画效果
          oneMarker.setAnimation(BMAP_ANIMATION_BOUNCE);
          map.addEventListener("zoomend",function(){
              oneMarker.setAnimation();
              oneMarker.setAnimation(BMAP_ANIMATION_BOUNCE);
          });
      }
    }
  });
};
