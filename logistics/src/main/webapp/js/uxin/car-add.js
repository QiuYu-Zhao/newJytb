/**
 * 优信增加车辆
 * Created by lzj on 2017/6/2.
 */

jQuery(document).ready(function () {
    init();
});

function init() {
    $("#addBtn").click(function(){
        $('#addBtn').prop('disabled', true);
        createDeviceData();
        if( null == checkDevice) {
            $('#addBtn').prop('disabled', false);
            return;
        }

        if(hasAdded()){
            //已经添加了该设备
            tipr('该设备已经加入到列表',"#deviceNoteLabel");
            $('#addBtn').prop('disabled', false);
            return;
        }
        if(checkDevice.code != 3){
            showTips('该设备号无法使用',"#noteTipDiv");
            $('#addBtn').prop('disabled', false);
            return;
        }
        var container = $("#deviceContainer");
        var id = "devoce" + checkDevice.deviceNum;
        var btnId = "btn" + checkDevice.deviceNum;
        var content =
            "<div style='margin:0 auto; width:100%; height:30px; ' id='"+ id + "'>" +
            "       <div style='float: left;width: 160px;'>设备号码：<span style='width: 80px;'>" + checkDevice.deviceNum + "</span></div>" +
            "       <div style='float: left;width: 90px;'> <p style='float: left'>类型：</p><p style='width: 30px;float: left'>" + checkDevice.deviceType + "</p></div>" +
            "       <div style='float: left;width: 60px;'><button type='button' id='" + btnId +"'>删除</button></div>" +
            "   </div>";
        container.append(content);

        $('#addBtn').prop('disabled', false);
        $("#"+btnId).click(function(){
            delDevice(id);
        });
    });

    $("#checkBtn").click(checkDeviceNum);
    $("#btnSave").click(submitHandler);
}

//移除设备
function delDevice(id){
    $("#" + id).remove();
}

function getDeviceList() {
    var s = $("#deviceContainer").find("span");
    var deviceNums = new Array();
    for (var i=0;i<s.length;i++){
        deviceNums.push(s[i].innerText.trim());
    }
    return deviceNums;
}

function hasAdded(){
    if(null == checkDevice) return false;
    var devices = getDeviceList();
    return $.inArray(checkDevice.deviceNum.trim(), devices) != -1;
}

//检索出来的设备
var checkDevice = null;

function checkDeviceNum(){
    checkDevice = null;
    $.ajax({
        type : "post",
        dataType : "JSON",
        url : "/mk/uxin/gongdan/customer/checkDeviceNo.html",
        data : {
            "deviceNum": function() {
                return $("#deviceNum").val();
            }
        },
        success : function(result) {
            $('#checkBtn').prop('disabled', false);
            checkDevice = result;
            var note = "";
            if(checkDevice.code == 3){
                note = "设备号码：" + checkDevice.deviceNum +"，类型：" + getDeviceType(checkDevice.deviceType) +"，状态：未占用";
                $("#notyP").css("color","black");
            }else {
                if(checkDevice.code == 0){
                    note = "设备号为空";
                }else if(checkDevice.code == 2){
                    note = "设备号不存在";
                }else if(checkDevice.code == 1){
                    note = "设备号码：" + checkDevice.deviceNum +"，类型：" + getDeviceType(checkDevice.deviceType) +"，状态：已占用";
                }
                $("#notyP").css("color","red");
            }
            $("#notyP").html(note);
        },
        error : function(msg) {
            $('#checkBtn').prop('disabled', false);
        }
    });
}

function createDeviceData() {
    var selectedTxt = $('#sel_device_num option:selected').text();
    //防止不选择设备点击添加后添加按钮不可用
    if(selectedTxt != "请选择"){
        var args = selectedTxt.split(",");
        checkDevice = new Object();
        checkDevice.deviceNum = args[0].trim();
        checkDevice.deviceType = args[1].trim().replace("类型:","");
        checkDevice.code=3;
    }else {
        //防止下拉框为“请选择”时还能添加上次选择的设备
        checkDevice = null;
    }
}

function getDeviceType(type){
    return type == 0 ? "无线" : (type == 1 ?  "有线" : "OBD");
}

function submitHandler(){
    var devices = getDeviceList();
    if(null == devices || devices.length == 0){
        layer.msg('请添加设备',{time :3000,offset: '230px'});
        $('#btnSave').prop('disabled', false);
        return;
    }
    if($("#workaddressLng").val() == '' || $("#homeaddressLng").val() == ''){
        layer.msg('请在地图中标注家庭地址和工作地址',{time :3000,offset: '230px'});
        $('#btnSave').prop('disabled', false);
        return;
    }

    $('#btnSave').prop('disabled', true);
    layer.load(1, {shade:0.3, offset: '200px'});
    var args = JSON.stringify({"name": $("#name").val(),"idcard":$("#idcard").val(),"tel":$("#tel").val(),
        "workaddress": $("#workaddress").val(),"homeaddress":$("#homeaddress").val(),"brand":$("#brand").val(),
        "carNum": $("#carNum").val(),"model":$("#model").val(),"vin":$("#vin").val(),
        "deviceNum": $("#deviceNum").val(),"deviceNums":devices,
        "workaddressLat":$("#workaddressLat").val(),"workaddressLng":$("#workaddressLng").val(),
        "homeaddressLat":$("#homeaddressLat").val(),"homeaddressLng":$("#homeaddressLng").val()
        });
    $.ajax({
        type : "post",
        dataType : "JSON",
        url : "/mk/uxin/gongdan/customer/addCar.html",
        data : {
            "info":args
        },
        success : function(result) {
            $('#btnSave').prop('disabled', false);
            if(result == 0) {
                window.location.href = "/mk/uxin/car/list.html";
            }
            else if(result == 1)  showTips("车辆信息为空，添加失败","#btnSave");
            else if(result == 2)  showTips("发生异常，请检查数据信息","#btnSave");
        },
        error : function(msg) {
            $('#btnSave').prop('disabled', false);
            layer.closeAll();
            layer.alert('请稍后重试', {offset: '150px'});
        }
    });
}

function showTips(note,id) {
    layer.tips(note, id, {
        tips: [1, '#000000'],
        time: 3000
    });
}

function tipr(note, id) {
    layer.tips(note, id);
}

/*///The new map page////////////*/
var working = function () {
    // map.clearOverlays();
    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }
    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });
    $("#btn_clear_map_address").on("click",function(){//点击关闭
        $("#suggestId").val("");
    })
    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });
    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击s下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        map.clearOverlays();
        setPlace();
        // $(".tangram-suggestion").hide();
    });
    var arr111 = {};
    var geoc = new BMap.Geocoder();
    function setPlace(){
        function myFun(){
            if(local.getResults().getPoi(0) == null){
                alert("地址不存在，请刷新重新填写");
            }
            var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 11);
            var myIcon = new BMap.Icon("/image/map/workpho1.png", new BMap.Size(30,30));
            var marker = new BMap.Marker(pp,{icon:myIcon});
            marker.setLabel(new BMap.Label("点我确认", {offset: new BMap.Size(33, 4)}));
            map.addOverlay(marker);    //添加标注
            var p = marker.getPosition();

            marker.enableDragging();//小红点拖拽
            marker.addEventListener("click", function(e){ //点击显示成功
                $(".tangram-suggestion").hide();
                $("iframe").hide();
                var pt = e.point;
                geoc.getLocation(pt, function(rs){
                    var addComp = rs.addressComponents;
                    var location_street = addComp.province +  addComp.city +  addComp.district +  addComp.street  + addComp.streetNumber;
                    $("#workaddress").val(location_street);//工作地址显示
                });
                $("#workaddressLat").val(pt.lat);
                $("#workaddressLng").val(pt.lng);//地址坐标
                alert("地址设置成功");
                map.clearOverlays();
                $("#map_wrap_wrap_black").hide();
                arr111.lat = pt.lng;
                arr111.lng =  pt.lat;
            });
            arr111.lat = p.lat;
            arr111.lng =  p.lng;
            //拖拽后的坐标点经纬度
            function showInfo(e){
                arr111.lat = e.point.lat;
                arr111.lng =  e.point.lng;
                $("#map_wrap_wrap_black").hide();
                $(".tangram-suggestion").hide();
                $("iframe").hide();
            }
            marker.addEventListener("click", showInfo);//鼠标点击地图后的经纬度
            //拖拽后点击地图显示具体位置
            marker.addEventListener("click", function(e){
                arr111.lat = e.point.lat;
                arr111.lng =  e.point.lng;
                var pt = e.point;
                geoc.getLocation(pt, function(rs){
                    var addComp = rs.addressComponents;
                    var location_street = addComp.province +  addComp.city +  addComp.district +  addComp.street  + addComp.streetNumber;
                    $("#suggestId").val(location_street);//显示地址
                    $("#workplace").val(location_street);//工作地址显示
                    $("#map_wrap_wrap_black").hide();
                });
                $(".tangram-suggestion").hide();
                $("iframe").hide();
            });
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }
}
var homeing = function () {
    map.addOverlay();
    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }
    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });
    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });
    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        // map.clearOverlays();
        setPlace();
    });
    var arr111 = {};
    var geoc = new BMap.Geocoder();
    function setPlace(){
        function myFun(){
            map.clearOverlays();    //清除地图上所有覆盖物
            if(local.getResults().getPoi(0) == null){
                alert("地址不存在，请刷新重新填写");
            }
            var pp = local.getResults().getPoi(0).point; //获取第一个智能搜索的结果
            map.centerAndZoom(pp,11);
            var myIcon = new BMap.Icon("/image/map/homepho1.png", new BMap.Size(30,30));
            var marker = new BMap.Marker(pp,{icon:myIcon});
            marker.setLabel(new BMap.Label("点我确认", {offset: new BMap.Size(33, 4)}));
            map.addOverlay(marker);    //添加标注
            var p = marker.getPosition();//点坐标经纬度
            console.log("p"+p.lng);
            marker.enableDragging();//小红点拖拽
            marker.addEventListener("click", function(e){		 //点击显示成功
                $(".tangram-suggestion").hide();
                $("iframe").hide();
                var pt = e.point;
                geoc.getLocation(pt, function(rs){
                    var addComp = rs.addressComponents;
                    var location_street = addComp.province +  addComp.city +  addComp.district +  addComp.street  + addComp.streetNumber;
                    $("#homeaddress").val(location_street);//工作地址显示
                });
                $("#homeaddressLat").val(e.point.lat);
                $("#homeaddressLng").val(e.point.lng);//地址坐标
                alert("地址设置成功");
                map.clearOverlays();
                $("#map_wrap_wrap_black").hide();
                arr111.lat = pt.lat;
                arr111.lng =  pt.lng;
            });
            arr111.lat = p.lat;
            arr111.lng =  p.lng;
            //拖拽后的坐标点经纬度
            function showInfo(e){
                arr111.lat = e.point.lat;
                arr111.lng =  e.point.lng;
                $(".tangram-suggestion").hide();
                $("iframe").hide();

            }
            marker.addEventListener("click", showInfo);//鼠标点击地图后的经纬度
            //拖拽后点击地图显示具体位置
            marker.addEventListener("click", function(e){
                arr111.lat = e.point.lat;
                arr111.lng =  e.point.lng;
                var pt = e.point;
                geoc.getLocation(pt, function(rs){
                    var addComp = rs.addressComponents;
                    var location_street = addComp.province +  addComp.city +  addComp.district +  addComp.street  + addComp.streetNumber;
                    $("#suggestId").val(location_street);//显示地址
                    $("#address").val(location_street);//工作地址显示
                });
                $("iframe").hide();
                $(".tangram-suggestion").hide();
            });
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }
}
$(".location_btn_location_btn_homeaddress").on("click",function () {
    console.log($("#homeaddressLat").val());
    console.log($("#homeaddressLng").val());
    $("#map_wrap_wrap_black").show();
    map.clearOverlays();
  var timer = setInterval(function () {
       map.setZoom(20);
       map.setZoom(10);
      clearInterval(timer);
    },100);
    map.setZoom(10);
    if($("#homeaddressLat").val() !=="" && $("#homeaddressLng").val() !== ""){
        var pp = new BMap.Point($("#homeaddressLng").val(), $("#homeaddressLat").val());
        var myIcon = new BMap.Icon("/image/map/homepho1.png", new BMap.Size(30,30));
        var marker = new BMap.Marker(pp,{icon:myIcon});
        var p = marker.getPosition();//点坐标经纬度
        marker.setLabel(new BMap.Label("点我确认", {offset: new BMap.Size(33,4)}));
        map.addOverlay(marker);    //添加标注
        marker.enableDragging();//小红点拖拽
        var arr111 = {};
        var geoc = new BMap.Geocoder();
        marker.addEventListener("click", function(e){ //点击显示成功
            $(".tangram-suggestion").hide();
            var pt = e.point;
            geoc.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                var location_street = addComp.province +  addComp.city +  addComp.district +  addComp.street  + addComp.streetNumber;
                $("#address").val(location_street);//工作地址显示
                $("#suggestId").val(location_street);//显示地址
                $("iframe").hide();
            });
            $("#homeaddressLat").val(e.point.lat);
            $("#homeaddressLng").val(e.point.lng);//地址坐标
            $(".tangram-suggestion-main").hide();
            alert("地址设置成功");
            map.clearOverlays();
            $("#map_wrap_wrap_black").hide();
            $("iframe").hide();
            arr111.lat = pt.lat;
            arr111.lng =  pt.lng;
        });
        map.centerAndZoom(pp,11);
    }
    homeing();
});
$(".location_btn_location_btn_workaddress").on("click",function () {
    $("#map_wrap_wrap_black").show();
    map.clearOverlays();
    var timer = setInterval(function () {
        map.setZoom(20);
        map.setZoom(10);
        clearInterval(timer);
    },100);
    map.setZoom(10);
    if($("#workaddressLat").val() !=="" && $("#workaddressLng").val() !== ""){
        var pp = new BMap.Point($("#workaddressLng").val(), $("#workaddressLat").val());
        map.centerAndZoom(pp,11);
        var myIcon = new BMap.Icon("/image/map/workpho1.png", new BMap.Size(30,30));
        var marker = new BMap.Marker(pp,{icon:myIcon});
        marker.setLabel(new BMap.Label("点我确认", {offset: new BMap.Size(33, 4)}));
        marker.enableDragging();//小红点拖拽
        map.addOverlay(marker);    //添加标注
        var arr111 = {};
        var geoc = new BMap.Geocoder();
        marker.addEventListener("click", function(e){ //点击显示成功
            var pt = e.point;
            geoc.getLocation(pt, function(rs){
                var addComp = rs.addressComponents;
                var location_street = addComp.province +  addComp.city +  addComp.district +  addComp.street  + addComp.streetNumber;
                $("#workplace").val(location_street);//工作地址显示
                $("iframe").hide();
            });
            $("#workaddressLng").val(e.point.lng);
            $("#workaddressLat").val(e.point.lat);
            //地址坐标
            alert("地址设置成功");
            map.clearOverlays();
            $("#map_wrap_wrap_black").hide();
            $("iframe").hide();
            arr111.lat = pt.lat;
            arr111.lng =  pt.lng;
        });
    }
    working();
});
$("#clear_map_clear").on("click",function () {
    $("#map_wrap_wrap_black").hide();
});
$("#btn_clear_map_address").on("click",function(){//点击关闭
    $("#suggestId").val("");
})
$("#img_close").on("click",function () {
    $("#map_wrap_wrap_black").hide();
})



