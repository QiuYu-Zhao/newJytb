var FormWizard = function () {
    return {
        //main function to initiate the module
        init: function () {
            if (!jQuery().bootstrapWizard) {
                return;
            }

            var form = $('#submit_form');
            var error = $('.alert-error', form);
            var success = $('.alert-success', form);
            var finish = $('.alert-finish', form);

            form.validate({
                doNotHideMessage: true, //this option enables to show the error/success messages on tab switch.
                errorElement: 'span', //default input error message container
                errorClass: 'validate-inline', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                rules: {
                    //第一步
                    username: {
                        required: true
                    },
                    idNum: {
                        required: true,
                    },
                    mobile: {
                        required: true
                    },
                    servicePassword: {
                        required: true
                    }

                },
                messages: { // custom messages for radio buttons and checkboxes
                    username: "！",
                    idNum: "！",
                    mobile: "！",
                    servicePassword: "！",
                },

                errorPlacement: function (error, element) { // render error placement for each input type
                    error.insertAfter(element); // for other inputs, just perform default behavoir
                },

                invalidHandler: function (event, validator) { //display error alert on form submit   
                    success.hide();
                    error.show();
                    App.scrollTo(error, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.help-inline').removeClass('ok'); // display OK icon
                    $(element)
                        .closest('.control-group').removeClass('success').addClass('error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change dony by hightlight
                    $(element)
                        .closest('.control-group').removeClass('error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .addClass('valid ok') // mark the current input as valid and display OK icon
                        .closest('.control-group').removeClass('error').addClass('success'); // set success class to the control group
                },

                submitHandler: function (form) {
                    success.show();
                    error.hide();
                }
            });

            var displayConfirm = function() {
                $('.display-value', form).each(function(){
                    var input = $('[name="'+$(this).attr("data-display")+'"]', form);
                    if (input.is(":text") || input.is("textarea")) {
                        $(this).html(input.val());
                    }
                });
            }

            // default form wizard
            $('#form_wizard_1').bootstrapWizard({
                'nextSelector': '.button-next',
                'previousSelector': '.button-previous',
                onTabClick: function (tab, navigation, index) {
                    return false;
                },
                onNext: function (tab, navigation, index) {
                    success.hide();
                    error.hide();

                    if (form.valid() == false) {
                        return false;
                    }

                    var total = navigation.find('li').length;
                    var step = $("#step").val();
                    // set wizard title
                    $('.step-title', $('#form_wizard_1')).text('第 ' + step + ' 步 ');
                    // set done steps
                    jQuery('li', $('#form_wizard_1')).removeClass("done");
                    var li_list = navigation.find('li');
                    for (var i = 0; i < step-1; i++) {
                        jQuery(li_list[i]).addClass("done");
                    }

                    if (step == 1) { //第一页显示
                        $('#form_wizard_1').find('.button-next').hide();
                        ('#form_wizard_1').find('.button-previous').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                        $('#form_wizard_1').find('#tab2').hide();
                        $('#form_wizard_1').find('#tab3').hide();
                        $('#form_wizard_1').find('#tab4').hide();
                        $('#form_wizard_1').find('#tab5').hide();
                        $('#form_wizard_1').find('#tab1').show();
                        $('#form_wizard_1').find('#number1').css("background-color","#35aa47");
                        $('#form_wizard_1').find('#number2').css("background-color","#eee");
                        $('#form_wizard_1').find('#number3').css("background-color","#eee");
                        $('#form_wizard_1').find('#number4').css("background-color","#eee");
                        $('#form_wizard_1').find('#number5').css("background-color","#eee");
                        $('#form_wizard_1').find('#number1').css("color","#fff");
                    } else if(step == 2 ) {
                        $('#form_wizard_1').find('.button-previous').hide();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                        $('#form_wizard_1').find('#tab1').hide();
                        $('#form_wizard_1').find('#tab3').hide();
                        $('#form_wizard_1').find('#tab4').hide();
                        $('#form_wizard_1').find('#tab5').hide();
                        $('#form_wizard_1').find('#tab2').show();
                        $('#form_wizard_1').find('#number1').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number2').css("background-color","#35aa47");
                        $('#form_wizard_1').find('#number3').css("background-color","#eee");
                        $('#form_wizard_1').find('#number4').css("background-color","#eee");
                        $('#form_wizard_1').find('#number5').css("background-color","#eee");
                        $('#form_wizard_1').find('#number2').css("color","#fff");
                    }  else if(step == 3 ) {
                        $('#form_wizard_1').find('.button-previous').hide();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                        $('#form_wizard_1').find('#tab1').hide();
                        $('#form_wizard_1').find('#tab2').hide();
                        $('#form_wizard_1').find('#tab4').hide();
                        $('#form_wizard_1').find('#tab5').hide();
                        $('#form_wizard_1').find('#tab3').show();
                        $('#shortMsg').val("");
                        $('#form_wizard_1').find('#number1').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number2').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number3').css("background-color","#35aa47");
                        $('#form_wizard_1').find('#number4').css("background-color","#eee");
                        $('#form_wizard_1').find('#number5').css("background-color","#eee");
                        $('#form_wizard_1').find('#number3').css("color","#fff");
                        $("#authBtn3").css('display','block');
                        $("#message2").hide();
                    }  else if(step == 4 ) {
                        $('#form_wizard_1').find('.button-previous').hide();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                        $('#form_wizard_1').find('#tab1').hide();
                        $('#form_wizard_1').find('#tab2').hide();
                        $('#form_wizard_1').find('#tab3').hide();
                        $('#form_wizard_1').find('#tab5').hide();
                        $('#form_wizard_1').find('#tab4').show();
                        $('#form_wizard_1').find('#number1').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number2').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number3').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number4').css("background-color","#35aa47");
                        $('#form_wizard_1').find('#number5').css("background-color","#eee");
                        $('#form_wizard_1').find('#number4').css("color","#fff");
                    }else if(step == 5 ) {
                        $('#form_wizard_1').find('.button-previous').hide();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').show();
                        $('#form_wizard_1').find('#tab1').hide();
                        $('#form_wizard_1').find('#tab2').hide();
                        $('#form_wizard_1').find('#tab3').hide();
                        $('#form_wizard_1').find('#tab4').hide();
                        $('#form_wizard_1').find('#tab5').show();
                        $('#form_wizard_1').find('#number1').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number2').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number3').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number4').css("background-color","#f2ae43");
                        $('#form_wizard_1').find('#number5').css("background-color","#35aa47");
                        $('#form_wizard_1').find('#number5').css("color","#fff");
                    }

                    App.scrollTo($('.page-title'));
                },
                onPrevious: function (tab, navigation, index) {
                    success.hide();
                    error.hide();

                    var total = navigation.find('li').length;
                    var step = $("#step").val();
                    // set wizard title
                    $('.step-title', $('#form_wizard_1')).text('第 ' + step + ' 步 ');
                    // set done steps
                    jQuery('li', $('#form_wizard_1')).removeClass("done");
                    var li_list = navigation.find('li');
                    for (var i = 0; i < step-1; i++) {
                        jQuery(li_list[i]).addClass("done");
                    }

                    if (step == 1) { //第一页显示
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                    } else if(step == 2 ) {
                        $('#form_wizard_1').find('.button-previous').show();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                    }  else if(step == 3 ) {
                        $('#form_wizard_1').find('.button-previous').show();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').hide();
                    }  else if(step == 4 ) {
                        $('#form_wizard_1').find('.button-previous').hide();
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').show();
                    }
                    App.scrollTo($('.page-title'));
                },
                onTabShow: function (tab, navigation, index) {
                    var total = navigation.find('li').length;
                    var step = $("#step").val();
                    if(step == ""){
                        step = 1;
                    }
                    var $percent = (step/ total) * 100;
                    $('#form_wizard_1').find('.bar').css({
                        width: $percent + '%'
                    });
                }
            });
            $('#form_wizard_1').find('.button-previous').hide();
            $('#form_wizard_1').find('.button-next').hide();
            $('#form_wizard_1 .button-submit').click(function () {
               window.open("list.html","_self")
            }).hide();
        }
    };

}();