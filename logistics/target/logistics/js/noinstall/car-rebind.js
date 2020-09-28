
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
        url : "/mk/noinstall/gongdan/customer/checkDeviceNo.html",
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
        layer.tips('请添加设备', '#addBtn', {
            tips: [2, '#e85555'],
            time: 2000,
            anim: 6
        });

        $('#btnRebind').prop('disabled', false);
        return;
    }


    $('#btnRebind').prop('disabled', true);
    layer.load(1, {shade:0.3, offset: '200px'});
    var args = JSON.stringify({"name": $("#name").val(),"idcard":$("#idcard").val(),"tel":$("#tel").val(),
        "workaddress": $("#workaddress").val(),"homeaddress":$("#homeaddress").val(),"brand":$("#brand").val(),
        "carNum": $("#carNum").val(),"model":$("#model").val(),"vin":$("#vin").val(),
        "deviceNum": $("#deviceNum").val(),"deviceNums":devices,"id":$("#id").val(),
        "installProvince": $("#install_province").val(),"installCity":$("#install_city").val(),
        });
    $.ajax({
        type : "post",
        dataType : "JSON",
        url : "/mk/noinstall/gongdan/customer/reBind.html",
        data : {
            "info":args
        },
        success : function(result) {
            $('#btnRebind').prop('disabled', false);
            if(result == 0) {
                window.location.href = "/mk/noinstall/car/list.html";
            }
            else if(result == 1)  showTips("车辆信息为空，添加失败","#btnRebind");
            else if(result == 2)  showTips("发生异常，请检查数据信息","#btnRebind");
        },
        error : function(msg) {
            $('#btnRebind').prop('disabled', false);
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




