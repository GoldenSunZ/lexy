/**
 * Created by margie on 16/7/25.
 */
$(function () {
    $.fn.extend({
        sidebar_toggleAnimate: function (paddingRight, aTime, html, addClass) {
            $(this).animate({
                paddingRight: paddingRight
            }, aTime, function () {
                $(this).find('.txt').html(html);
                $(this).find('[class^="icon-"]').removeClass().addClass(addClass);
            });
        },
        eachHasOn: function () {
            var $this = $(this);
            if ($this.hasClass('on')) {
                $this.find('.sidebar_Child_ol').height('auto');
            }
        }
    });

    var sidebarClose = function (closeWidth) {
        var $sidebar = $('.sidebar');
        $sidebar.removeClass('open').addClass('close').animate({
            width: closeWidth
        }, 300, function () {
            //$sidebar.find('.sidebar_title').removeClass('noHover');
        });

        $sidebar.parent().find('.content').animate({
            left: closeWidth
        }, 300);

        $sidebar.find('li').each(function () {
            var $this = $(this);
            if (!$this.hasClass('on')) {
                $this.removeAttr('style');
            }
        });
    };

    var $sidebar_toggle, $sidebar = $('.sidebar'), $sidebar_menu, $sidebar_menu_li, $sidebar_title, $sidebar_Child_ol, closeWidth, openWidth;

    if ($sidebar.length > 0) {
        $sidebar_toggle = $sidebar.find('.sidebar_toggle');
        $sidebar_menu = $sidebar.find('.sidebar_menu');
        $sidebar_menu_li = $sidebar_menu.find('li');
        $sidebar_title = $sidebar_menu_li.find('.sidebar_title');
        $sidebar_Child_ol = $sidebar_menu_li.find('.sidebar_Child_ol');

        var $w = $(window).width();

        if ($w <= 1280) {
            $sidebar_toggle.sidebar_toggleAnimate('5px', '300', '打开', 'icon-openNav');
            sidebarClose(60);
        }

        $sidebar_menu_li.each(function () {
            $(this).eachHasOn();
        });
        //sidebar toggle
        $sidebar_toggle.click(function () {
            var $this = $(this);
            closeWidth = 60;
            openWidth = 245;

            if ($sidebar.hasClass('open')) {
                $this.sidebar_toggleAnimate('5px', '300', '打开', 'icon-openNav');
                sidebarClose(closeWidth);
            } else if ($sidebar.hasClass('close')) {
                $this.sidebar_toggleAnimate('40px', '300', '关闭', 'icon-closeNav');
                sidebarClose();
                $sidebar.animate({
                    width: openWidth
                }, 300, function () {
                    $(this).removeClass('close').addClass('open');
                    //$sidebar_title.addClass('noHover');
                });

                $sidebar.parent().find('.content').animate({
                    left: openWidth
                }, 300);

                $sidebar_menu_li.each(function () {
                    $(this).eachHasOn();
                });
            }

        });

        $sidebar_title.click(function () {
            var $this = $(this);
            $sidebar = $this.parents('.sidebar');
            if ($sidebar.hasClass('open')) {
                $sidebar_menu_li = $this.parent('li');
                if ($sidebar_menu_li.hasClass('sidebar_hasChild')) {
                    $sidebar_Child_ol = $sidebar_menu_li.find('.sidebar_Child_ol');
                    if ($sidebar_menu_li.hasClass('on')) {
                        $sidebar_menu_li.removeClass('on');
                        $sidebar_Child_ol.animate({
                            height: 0
                        }, 300, function () {
                            $(this).removeAttr('style');
                        });
                    } else {
                        var height = $sidebar_Child_ol.find('li').length * 48;
                        $sidebar_menu_li.addClass('on').siblings().removeClass('on').find('.sidebar_Child_ol').animate({
                            height: 0
                        }, 300);
                        $sidebar_Child_ol.animate({
                            height: height
                        }, 300, function () {
                            $(this).show();
                        });
                    }
                }
            }
        });
        $sidebar_menu_li.hover(function () {
            var $this = $(this);
            $sidebar = $this.parents('.sidebar');
            if ($sidebar.hasClass('close')) {
                if ($this.hasClass('sidebar_hasChild')) {
                    $sidebar_Child_ol = $this.find('.sidebar_Child_ol');
                    $sidebar_Child_ol.height('auto').show();
                    $this.siblings().find('.sidebar_Child_ol').removeAttr('style');
                }
            }
        }).mouseleave(function () {
            var $this = $(this);
            $sidebar = $this.parents('.sidebar');
            if ($sidebar.hasClass('close')) {
                $sidebar_Child_ol = $this.find('.sidebar_Child_ol');
                $sidebar_Child_ol.removeAttr('style');
            }
        });
    }

    var $content_wrapper = $('.content-wrapper'), $pop_innerBox = $('.pop_innerBox');
    $content_wrapper.height($(window).height() - 66);

    $(window).resize(function () {
        $content_wrapper.height($(window).height() - 66);
        $pop_innerBox.css("marginTop", ($(window).height() - 66 - $pop_innerBox.height()) / 2);
    });
});