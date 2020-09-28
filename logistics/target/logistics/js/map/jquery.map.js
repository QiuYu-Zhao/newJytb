/**
 * @author  Lyca,qmzmxfy@vip.qq.com
**/
//创建百度地图
var map = new BMap.Map("map",{enableMapClick:false});
map.centerAndZoom(new BMap.Point(116.413554,39.911013),11);
map.enableScrollWheelZoom();
//添加带有定位的导航控件
var navigationControl = new BMap.NavigationControl({
  anchor:BMAP_ANCHOR_TOP_RIGHT,//靠左上角位置
  type:BMAP_NAVIGATION_CONTROL_LARGE, //LARGE类型
  offset: new BMap.Size(5, 100)
});
map.addControl(navigationControl);
//添加地图类型
var mapType = new BMap.MapTypeControl({mapTypes:[BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]});//2D图,卫星图
map.addControl(mapType);

/** 添加比例尺***/
var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左下角，添加比例尺
map.addControl(top_left_control);

/********* 测距工具 ***********/
//定义测距控件
var distanceTool;
//定义测距控件按钮类
var distanceToolControl = function (){
// 默认停靠位置和偏移量
this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT;
this.defaultOffset = new BMap.Size(100,10);
};
//继承BMap.Control
distanceToolControl.prototype = new BMap.Control();
//创建div元素作为控件的容器,并将其添加到地图容器中
distanceToolControl.prototype.initialize = function(map){
// 创建一个DOM元素
var div = document.createElement("div"),
   $div = $(div);
$div.addClass('distance-tool').html('<i></i><span>测距</span>');
// 绑定事件,点击开启测距
$div.click(function(){
 distanceTool.open();
}).hover(function(){
 $(this).addClass('distance-tool-hover');
},function(){
 $(this).removeClass('distance-tool-hover');
});
// 添加DOM元素到地图中
map.getContainer().appendChild(div);
// 将DOM元素返回
return div;
};
//实例化测距相关
tools.plugin({
path:'/js/map/',
arr:['DistanceTool_min'],
success:function(){
 //实例化测距工具和测距工具按钮
 distanceTool = new BMapLib.DistanceTool(map);
 // 创建测距控件按钮
 var mydistanceToolCtrl = new distanceToolControl();
 // 添加到地图当中
 map.addControl(mydistanceToolCtrl);
}
});

/********* 汽车icon ***********/
//创建汽车图标
var ComplexCustomOverlay = function(imgUrl,info,point,rotate){
  this._point = point;
  this._imgUrl = imgUrl;
  this._info = info;
  this._rotate = rotate;
};
//关联遮罩实例
ComplexCustomOverlay.prototype = new BMap.Overlay();
//配置遮罩内容及事件
ComplexCustomOverlay.prototype.initialize = function(map){
  var carWrap = this._carWrap = document.createElement("div"),//添加遮盖物图层
      carImg = this._carImg = document.createElement("img"),//添加汽车图标
      carInfo = this._carInfo = document.createElement("div"),//添加汽车信息弹层
      $carWrap = $(carWrap),
      $carImg = $(carImg),
      $carinfo = $(carInfo);
  carWrap.appendChild(carImg);
  carWrap.appendChild(carInfo);
  $carWrap.addClass('map-car');
  $carImg.addClass('car-img').attr('src',this._imgUrl).rotate(this._rotate-90);
  $carinfo.addClass('car-info').html(this._info);
  //定义点击事件
        $carImg.click(function(){
            $(".map-car .car-info").hide();
            $carinfo.toggle();
        });
  $carWrap.find('.car-info-closed').click(function(){
    $carinfo.hide();
  });
  //向地图添加
  map.getPanes().labelPane.appendChild(carWrap);
  this._map = map;
  return carWrap;
};
//配置遮罩位置
ComplexCustomOverlay.prototype.draw = function(){
  var pixel = this._map.pointToOverlayPixel(this._point),
      $carWrap = $(this._carWrap);
  $carWrap.css({'left':pixel.x-($carWrap.width()/2),'top':pixel.y-($carWrap.height()/2)});
};
/********* 地理原地设防 ***********/
var geofenceOverlays;
function fence(){
  var styleOptions = {
    strokeColor:"#567efd",//边线颜色。
    fillColor:"#567efd",//填充颜色。当参数为空时，圆形将没有填充效果。
    strokeWeight:2,//边线的宽度，以像素为单位。
    strokeOpacity:0.8,//边线透明度，取值范围0 - 1。
    fillOpacity:0.5,//填充的透明度，取值范围0 - 1。
    strokeStyle:'solid'//边线的样式，solid或dashed。
  };
  //实例化鼠标绘制工具
  var drawingManager = new BMapLib.DrawingManager(map,{
    isOpen:false,//是否开启绘制模式
    enableDrawingTool:true,//是否显示工具栏
    drawingToolOptions:{
      anchor:BMAP_ANCHOR_TOP_RIGHT,//位置
      offset:new BMap.Size(10,40),//偏离值
      drawingModes:[BMAP_DRAWING_CIRCLE,BMAP_DRAWING_POLYGON] // BMAP_DRAWING_POLYGON,BMAP_DRAWING_CIRCLE,BMAP_DRAWING_RECTANGLE
    },
    circleOptions:styleOptions,//圆的样式
    polygonOptions:styleOptions,//多边形的样式
    rectangleOptions:styleOptions//矩形的样式
  });
  drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
 //添加鼠标绘制工具监听事件，用于获取绘制结果
  drawingManager.addEventListener('overlaycomplete',function(e){
    map.removeOverlay(geofenceOverlays);
    geofenceOverlays = e.overlay;
    geofenceOverlays.enableEditing();
  });
}
/********* 轨迹追踪 ***********/
var lushu,lushuPoi = [];
function lushuGo(json,step,complete,trackPoint){
  //生成百度坐标数组
  var arrPois = [];
  $.each(json,function(i,v){
    arrPois.push(new BMap.Point(v.lng,v.lat));
  });

  //配置路线
  var lushuLine = new BMap.Polyline(arrPois,{
    strokeColor:'#008ae6',
    strokeWeight:4,
    strokeOpacity:1
  });

  //写入路线
  map.addOverlay(lushuLine);
  //写入坐标点
  lushuPoi = [];
  var maxMarkPoint = 200;//最大绘制点
  var breakPointIndex = 0;//跳过点
  var arrLength = arrPois.length;
  if(arrLength  > maxMarkPoint ) {//确定间隔数
    breakPointIndex = parseInt(arrLength/maxMarkPoint);
  }
  var markIndex = 0;
  var drawPointIndex =0;
  var size = '16';
  if(trackPoint == 1) {//显示轨迹点
    for(var i=0;i<arrLength;i++){
      var iconRotation = 0;
      var marker;
      if(i==0){
        marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/start.png',new BMap.Size(32,32)),rotation:iconRotation});
      }else if(i==arrPois.length-1){
        marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/end.png',new BMap.Size(32,32)),rotation:iconRotation});
      }else if(json[i].pot.indexOf('GPS') == -1){
        marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/tower.png',new BMap.Size(size,size)),rotation:iconRotation});
      }else if(json[i].sp == 0){//speed-sp
        marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/stop.png',new BMap.Size(size,size)),rotation:iconRotation});
      }else{
        iconRotation = json[i].di;//direction-di
        marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/driving.png',new BMap.Size(size,size)),rotation:iconRotation});
      }
      lushuPoi.push(marker);
      if(breakPointIndex !=0) {
        //等于值或者开始值或者结束值都画点
        if(markIndex == breakPointIndex || i == 0 || i == arrLength - 1) {
          map.addOverlay(marker);
          markIndex = 0;
          drawPointIndex++;
          continue;
        }
      } else {//间隔为0的每个都添加
        map.addOverlay(marker);
        drawPointIndex++;
      }
      markIndex++;
    }
  } else {
    for(var i=0;i<arrLength;i++){
      var marker;
      if(i == 0) {
         marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/start.png',new BMap.Size(32,32))});
         map.addOverlay(marker);
      } else if (i == arrLength - 1) {
         marker = new BMap.Marker(arrPois[i],{icon:new BMap.Icon('/image/map/i/end.png',new BMap.Size(32,32))});
         map.addOverlay(marker);
      } else {
         marker = new BMap.Marker(arrPois[i]);
      }

      lushuPoi.push(marker);
    }
  }
  console.log('drawPoint in totalPoint==' + drawPointIndex + ":" + arrLength);
  //最佳视图
  map.setViewport(arrPois);
  //配置路书
  lushu = new BMapLib.LuShu(map,arrPois,{
    defaultContent:"",//提示文字
    enableRotation:true,//marker图标随道路旋转
    autoView:false,//自动视野调整
    icon:new BMap.Icon('/image/map/car.png',new BMap.Size(50,26)),
    speed:2000,
    landmarkPois:[],//途经地点
    step:function(i){
      step(i);
    },
    complete:function(){
      complete(arrPois);
    }
  });
}

/********* 标注icon ***********/
var markOverlayFlag = false;
//创建标注
var markOverlay = function(info,point){
  this._info = info;
  this._point = point;
};
//关联遮罩实例
markOverlay.prototype = new BMap.Overlay();
//配置遮罩内容及事件
markOverlay.prototype.initialize = function(map){
  var wrap = this._wrap = document.createElement("div"),//添加遮盖物图层
      content = this._content = document.createElement("div"),//添加标注信息弹层
      $wrap = $(wrap),
      $content = $(content);
  wrap.appendChild(content);
  $wrap.addClass('map-mark');
  $content.addClass('mark-content').html(this._info);
  //向地图添加
  map.getPanes().labelPane.appendChild(wrap);
  this._map = map;
  return wrap;
};
//配置遮罩位置
markOverlay.prototype.draw = function(){
  var pixel = this._map.pointToOverlayPixel(this._point),
      $wrap = $(this._wrap);
  $wrap.css({'left':pixel.x-($wrap.width()/2),'top':pixel.y-$wrap.height()});
};

