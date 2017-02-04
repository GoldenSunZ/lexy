/**
 * Created by margie on 2016/6/16.
 */
$(function () {
    var $input_filed, $sidebar_hasChild;

    $input_filed = $('.input__field');
    $sidebar_hasChild = $('.sidebar_hasChild');

    $.fn.extend({
        inputAddClass: function () {
            $(this).parent('.input').find('.input__line').animate({left: '0'});
            $(this).parent('.input').find('.input__label-content').animate({marginTop: '-15px'});
        },
        inputRemoveClass: function () {
            $(this).parent('.input').find('.input__line').animate({left: '-100%'});
            $(this).parent('.input').find('.input__label-content').animate({marginTop: ''});
        },
        olAnimate: function ($height) {
            $(this).animate({
                    height: $height
                }, 500
            );
        }
    });

    // login style
    if ($input_filed.length > 0) {
        $input_filed.each(function () {
            if ($(this).val() == '') {
                $(this).inputRemoveClass();
            } else {
                $(this).inputAddClass();
            }
        }).focus(function () {
            $(this).inputAddClass();
        }).blur(function () {
            if ($(this).val() == '') {
                $(this).inputRemoveClass();
            }
        });
    }

//    sidebar_hasChild
    var hasChild_height;
    $sidebar_hasChild.each(function () {
        var $sidebar_hasChild_on = $(this).hasClass('on'), $ol = $(this).find('ol'), $li = $ol.find('li'), len = $li.length, height = $li.height();
        if ($sidebar_hasChild_on) {
            $ol.height(len * height);
            hasChild_height = len * height;
        }
    });

    $('.sidebar_top_title').click(function () {
        var $parent, $ol, $li, $len, $height;

        $parent = $(this).parent();
        $ol = $parent.find('ol');
        $li = $ol.find('li');
        $len = $li.length;
        $height = $li.height() * $len;

        if ($parent.hasClass('on')) {
            $parent.removeClass('on');
            $ol.olAnimate(0);
        } else {
            $parent.addClass('on').siblings().removeClass('on').find('ol').removeAttr('style');
            $ol.olAnimate($height);
        }
    });

//    when screen width < 1280, close sidebar
    var screenWith = $(window).width();
    if (screenWith <= 1280) {
        var $sidebar = $('.sidebar');
        $sidebar.addClass('close').find('.sidebar_toggle').html('打开');
        $sidebar.find('.sidebar_Child_ol').removeAttr('style');
        $sidebar.find('.sidebar_Child_ol').hide();
        $('.content').css('left', '60px');

        $sidebar_hasChild.each(function () {
            $(this).removeClass('on');
            $(this).find('.sidebar_Child_ol').css({'display': 'none'});
        }).hover(function () {
            $(this).find('.sidebar_Child_ol').css({'display': 'block'});
        }).mouseleave(function () {
            $(this).find('.sidebar_Child_ol').css({'display': ''});
        });
        $sidebar.find('.active').closest('.sidebar_hasChild').addClass('on');
    }

//    sidebar_toggle open lose
    $('.sidebar_toggle').click(function () {
        var $sidebar, $this, $ol;

        $this = $(this);
        $sidebar = $this.parent('.sidebar');
        $ol = $sidebar.find('.sidebar_Child_ol');

        if ($sidebar.hasClass('close')) {
            $this.html('关闭').animate({
                    padding: '22px 40px'
                },
                300);
            $sidebar.animate({
                    width: '245px'
                }, 300
            ).removeClass('close').find('.active').closest('.sidebar_Child_ol').show().height(hasChild_height);
            $('.content').animate({
                left: '245px'
            }, 300);
            $sidebar_hasChild.unbind('mouseleave hover');
        } else {
            $this.html('打开').animate({
                    padding: '22px 0'
                },
                150);
            $sidebar.animate({
                    width: '60px'
                }, 300
            ).addClass('close');
            $('.content').animate({
                left: '60px'
            }, 300);

            $ol.removeAttr('style');
            $sidebar.find('.sidebar_Child_ol').hide();

            $sidebar_hasChild.each(function () {
                $(this).removeClass('on');
                $(this).find('.sidebar_Child_ol').css({'display': 'none'});
            }).hover(function () {
                $(this).find('.sidebar_Child_ol').css({'display': 'block'});
            }).mouseleave(function () {
                $(this).find('.sidebar_Child_ol').css({'display': ''});
            });

            $sidebar.find('.active').closest('.sidebar_hasChild').addClass('on');
        }
    });

// department checkbox
    var $department_All = $('#department_All');
    if ($department_All.length > 0) {
        $department_All.click(function () {
            if ($(this).prop('checked')) {
                $('#department').find('input[type=checkbox]').prop('checked', true);
                $('#department').find('.icon-check-empty').addClass('checked');
            } else {
                $('#department').find('input[type=checkbox]').prop('checked', false);
                $('#department').find('.icon-check-empty').removeClass('checked');
            }
        });

    }

    var $checkbok = $('.checkbox');
    if ($checkbok.length > 0) {
        $('.checkbox input').click(function () {
            if($(this).prop('checked')){
                $(this).siblings().addClass('checked');
            }else{
                $(this).siblings().removeClass('checked');
            }
        });
    }

//add new !!!!!!!!!! ****** need delete  $add_new为新增计划按钮 ！！！特别提示：层显示出来的属性为display:flex，重点重点重点 ！！  $cancel 为取消按钮  $remove 为关闭按钮******
    var $add_new = $('#add_new'), $cancel = $('.btn_cancel'), $remove = $('.icon-remove');
    if ($add_new.length > 0) {
        $add_new.click(function () {
            $('.pop_outBox').show().find('.pop_Box').css('marginTop', ($('.pop_outBox').height() - $('.pop_outBox').find('.pop_Box').height()) / 2);
        });
    }
    if ($cancel.length > 0) {
        $cancel.click(function () {
            $(this).closest('.pop_outBox').hide();
        })
    }

    if ($remove.length > 0) {
        $remove.click(function () {
            $(this).closest('.pop_outBox').hide();
        })
    }

    var $pEditTb = $('.p-edit--tb');
    if ($pEditTb.length > 0) {
        var $pEditTr = $pEditTb.find('tr:nth-child(even)');
        $pEditTr.each(function () {
            if ($(this).attr('class') == 'tb-blue' || $(this).attr('class') == 'tb-white') {
            } else {
                $(this).find('td').css('background', '#f6f8f9');
            }
        });
        $pEditTb.find('td').each(function () {
            if ($(this).attr('rowspan') > 1 ) {
                $(this).css('background', 'transparent');
            }
        });
    }
    if($.pnotify) {
        $.pnotify.defaults.history = false;
    }
});