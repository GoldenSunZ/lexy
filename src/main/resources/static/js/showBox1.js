/**
 * Created by margie on 16/7/26.
 */
$(function () {

    //弹出升级窗口
    $.fn.showUpGrade = function (options) {
        var $this = $(this), defaults = {
            "title": "提示",
            "width": "w700",
            "hasClose": true,
            "pop_type": "pop_outBox",
            "show_type": "UpGrade"
        };
        defaults = $.extend(defaults, options);
        var $element = $.createPopBox(defaults);
        $this.append($element);
        $this.setPosition();
    };

    // 弹出评审 or 驳回历史
    $.fn.showHistory = function (options) {
        var $this = $(this), defaults = {
            "hasClose": true,
            "pop_type": "pop_rejectBox",
            "outClass": "email_Box",
            "contentList": "",
            "parentElem": $this
        };
        defaults = $.extend(defaults, options);
        var $element = $.createHistoryBox(defaults);
        if (defaults.outClass == "judge_Box") {
            $element.append('<div class="box-arrow"><img src=images/icon-box-arrow.png ></div>');
            defaults.parentElem.append($element);
        } else {
            defaults.parentElem.append($element);
        }
        defaults.parentElem.addClass('onShow');
    };

    // 弹出提示框
    $.fn.showTip = function (options) {
        var $this = $(this), defaults = {
            "title": "提示",
            "width": "w420",
            "content": "",
            "hasClose": false,
            "pop_type": "pop_outBox",
            "show_type": "TipBox",
            "button_box": {
                "cancel_btn": {
                    "btnClass": "cancel_btn marginR10",
                    "txt": "取消"
                },
                "reject_btn": false,
                "confirm_btn": {
                    "btnClass": "confirm_btn",
                    "txt": "确定"
                }
            }
        };
        $this.data = options.data;
        defaults = $.extend(defaults, options);
        var $element = $.createPopBox(defaults);
        $this.append($element);
        $this.setPosition();
    };

    // 评审
    $.fn.showReview = function (options) {
        var $this = $(this), defaults = {
            "title": "员工评审",
            "width": "w700",
            "hasClose": false,
            "pop_type": "pop_outBox",
            "show_type": "ReviewBox",
            "button_box": {
                "cancel_btn": {
                    "btnClass": "cancel_btn marginR10",
                    "txt": "取消"
                },
                "reject_btn": {
                    "btnClass": "reject_btn marginR10",
                    "txt": "驳回"
                },
                "confirm_btn": {
                    "btnClass": "confirm_btn",
                    "txt": "确定"
                }
            }
        };
        defaults = $.extend(defaults, options);
        var $element = $.createPopBox(defaults);
        $this.append($element);
        $this.setPosition();
    };

    // 创建pop_outBox框架
    $.createPopBox = function (options) {
        var $pop_outBox = $('<div/>').addClass('pop_outBox'),
            $pop_innerBox = $('<div/>').addClass('pop_innerBox').addClass(options.width)
                .append($.createTitle(options))
                .append($.createContent(options));
        if (options.hasOwnProperty('button_box')) {
            $pop_innerBox.append($.createButton(options));
        }

        if (options.show_type == "ReviewBox") {
            var $s = $('<div/>').addClass('scroll_left').css({'width': '1360px', 'position': 'relative'}),
                $s1 = $('<div/>').css('width', '660px').append($.createTitle(options))
                    .append($.createContent(options))
                    .append($.createButton(options)),
                $s2 = $('<div/>').css('width', '660px').css({'position': 'absolute', 'right': '0', 'top': 0})
                    .append($.createTitle({"title": "驳回原因"}))
                    .append('<div class="pop_textarea" style="width: 640px; height: 280px;"><textarea rows="3" cols="" class="wp100" style="height: 100%"></textarea></div>')
                    .append($.createButton({
                        "button_box": {
                            "cancel_btn": {
                                "btnClass": "cancel_btn marginR10",
                                "txt": "取消",
                                "reScroll": true
                            },
                            "confirm_btn": {
                                "btnClass": "confirm_btn",
                                "txt": "确定"
                            }
                        }
                    }));
            $s.append($s1).append($s2);
            $pop_innerBox = $('<div/>').addClass('pop_innerBox').addClass(options.width).css('overflow', 'hidden')
                .append($s);
        }
        $pop_outBox.append($pop_innerBox);

        return $pop_outBox;
    };

    // 创建pop_rejectBox框架
    $.createHistoryBox = function (options) {
        var $pop_rejectBox = $('<div/>').addClass('pop_rejectBox').addClass(options.outClass),
            $reject_ul = $('<ul/>').addClass('reject_ul').append($.createList(options.contentList));

        $pop_rejectBox.append($.createTitle(options))
            .append($reject_ul);
        return $pop_rejectBox;
    };

    // 创建标题
    $.createTitle = function (options) {
        if (options.hasOwnProperty('title')) {
            if (options.hasOwnProperty('hasClose') && options.hasClose === true) {
                var $close = $('<span/>').addClass('icon-closeB marginT5 right').click(function () {
                    $.dialogClose(options);
                });
                return $('<h4/>').addClass('outBox-title')
                    .append('<span>' + options.title + '</span>')
                    .append($close);
            } else {
                return $('<h4/>').addClass('outBox-title')
                    .append('<span>' + options.title + '</span>');
            }
        } else {
            $(document).on('click', function (e) {
                if ($(e.target).parent('.icon-envelope').length == 0) {
                    options.parentElem.removeClass('onShow');
                    options.parentElem.find('.pop_rejectBox').remove();
                }
            });
        }
    };

    // 创建弹窗内容
    $.createContent = function (options) {
        if (options.hasOwnProperty('show_type')) {
            switch (options.show_type) {
                case "UpGrade":
                    return $('<div/>').addClass('txt_center lineH30')
                        .append('<span class="red f24">恭喜你!' + options.name + '</span><br>成功升阶到<br><span class="award_bg f16White"><strong>' + options.state + '</strong></span><br>感谢你这段时间以来的努力！');

                    break;
                case "TipBox":
                    return $('<div/>').append(options.content);

                    break;
                case "ReviewBox":
                    var $content = options.reveiwList,
                        $ul = $('<ul/>').addClass('pop_judge_ul').css({'width': '660px'}),
                        $reveiw_file = "", $review_comment = "", $imm_review_comment = "", $review_con;

                    // 成长结果文件
                    $.each($content.reveiw_file, function (i, value) {
                        if (value.hasOwnProperty('isNew') && value.isNew) {
                            $reveiw_file = $reveiw_file + '<a href="' + value.fileAdd + '" class="table-file"><span>' + value.fileName + '</span><span class="icon-download"></span></a><span class="f12Red marginR10">NEW</span>';
                        } else {
                            $reveiw_file = $reveiw_file + '<a href="' + value.fileAdd + '" class="table-file"><span>' + value.fileName + '</span><span class="icon-download"></span></a>';
                        }
                    });

                    // 领导填写评语
                    if ($content.hasOwnProperty('imm_review_comment')) {
                        $imm_review_comment = $content.imm_review_comment;
                    }
                    if ($content.hasOwnProperty('review_comment')) {
                        $review_comment = $content.review_comment;
                    }

                    $ul.append($.createReview_LI('当前成长目标', '', $content.review_target))
                        .append($.createReview_LI('见证资料', '', $content.reveiw_material))
                        .append($.createReview_LI('成长结果', 'borderB1', $reveiw_file))
                        .append('<li><div>领导填写评语：</div><div><div class="pop_textarea"><textarea rows="3" cols="" class="wp100">' + $imm_review_comment + '</textarea></div></div></li>')
                        .append('<li><div>上级追加评语：</div><div><div class="pop_textarea"><textarea rows="3" cols="" class="wp100">' + $review_comment + '</textarea></div></div></li>')
                        .append('<li class="borderB1"><div class="lineH30">对员工评分：</div><div><label class = "pop_textarea"><input type = "text" maxlength = "2"/></label><p class = "f12Grey">① 见证性资料由部门会审；（科长和厂长）按报告的质量给予评分：1分（一般）、2分（良好）、3分（质优）<br/>② 会审评分有2种：项目报告（15分）；一般性报告（13分）</p></div></li>')
                        .append('<li class="borderB1"><div>上次驳回原因：</div><div><div class="left w490">' + $content.review_reason + '</div><div id="reject_history" class="right" style="position:relative;"><span class="icon-history" data-id = "1"></span><span class="red">历史</span></div></div></li>');

                    return $('<div/>').addClass('maxH300').append($ul);
                    break;

                case "editorBox":
                    options.data = options.data||{name:""};
                    var $ul = $('<ul/>').addClass('pop_judge_ul').css({'width': '660px', 'margin-bottom': '15px'}), $value = options.data.name||"";
                    if (""===$value && options.hasOwnProperty('editor_type')) {
                        $value = options.editor_type;
                    }
                    $ul.append('<li class="borderB1"><div class="lineH30">角色名称：</div><div><label class = "pop_textarea" style="display:block; padding: 0 10px; width: 533px;"><input type = "text" class="wp100" value="' + $value +'"/></label></div></li>');
                    return $('<div/>').append($ul);
                    break;

            }
        }
    };

    // 设置弹窗居中
    $.fn.setPosition = function () {
        var $this = this, $inHeight = $this.find('.pop_innerBox').height(), $h = $this.height();
        $this.find('.pop_innerBox').css('marginTop', ($h - $inHeight) / 2);
    };

    // 评审列表
    $.createList = function (options) {
        var $children = [];
        $.each(options, function (key, value) {
            var $li_content;
            if (value.hasOwnProperty('name') && value.hasOwnProperty('reason')) {
                $li_content = $('<li><p class="margin0">' + value.name + '</p><p class="margin0">' + value.reason + '</p></li>');
            } else if (value.hasOwnProperty('time') && value.hasOwnProperty('reason')) {
                $li_content = $('<li><p class="margin0">' + value.reason + '</p><p class="margin0 f12Grey txt_right">' + value.time + '</p></li>');
            } else {
                if (typeof value === "string") {
                    $li_content = $('<li><p class="margin0">' + value + '</p></li>');
                } else if (typeof value === "object") {
                    $li_content = $('<li><p class="margin0">' + value.reason + '</p></li>');
                }
            }
            $children.push($li_content);
        });
        $($children[$children.length - 1]).addClass('noBorder');
        return $children;
    };

    // 创建button
    $.createButton = function (options) {
        var $cancel_btn, $reject_btn, $confirm_btn, $withClass = "";

        if (options.width == 'w420') {
            $withClass = "w185";
        } else if (options.width == 'w700' && options.button_box.hasOwnProperty('reject_btn') && options.button_box.reject_btn) {
            $withClass = "w213";
        } else {
            $withClass = "w325";
        }

        if (options.button_box.hasOwnProperty('cancel_btn')) {
            $cancel_btn = $('<button/>').addClass(options.button_box.cancel_btn.btnClass).addClass($withClass).html(options.button_box.cancel_btn.txt);
            $cancel_btn.click(function () {
                if (options.button_box.cancel_btn.hasOwnProperty('reScroll')) {
                    $('.scroll_left').animate({
                        'marginLeft': '0'
                    }, 500);
                } else {
                    $('.pop_outBox').remove();
                }
            });
        }
        if (options.button_box.hasOwnProperty('reject_btn') && options.button_box.reject_btn) {
            $reject_btn = $('<button/>').addClass(options.button_box.reject_btn.btnClass).addClass($withClass).html(options.button_box.reject_btn.txt);
            $reject_btn.click(function () {
                $('.scroll_left').animate({
                    'marginLeft': '-700px'
                }, 500);
            });
        }
        if (options.button_box.hasOwnProperty('confirm_btn')) {
            $confirm_btn = $('<button/>').addClass(options.button_box.confirm_btn.btnClass).addClass($withClass).html(options.button_box.confirm_btn.txt);
            $confirm_btn.click(function () {
                $('.pop_outBox').remove();
            });
        }

        return $('<div/>').addClass('button_Box').append($cancel_btn).append($reject_btn).append($confirm_btn);
    };

    // 关闭弹出窗口
    $.dialogClose = function (options) {
        if (options.pop_type == "pop_outBox") {
            $('.pop_outBox').remove();
        } else {
            options.parentElem.removeClass('onShow');
            $('.pop_rejectBox').remove();
        }
    };

    // 员工评审列表
    $.createReview_LI = function (text, liClass, value) {
        if (value == undefined) {
            value = "";
        }
        if (liClass != "") {
            return '<li class="className"><div>' + text + '：</div><div>' + value + '</div></li>';
        } else {
            return '<li><div>' + text + '：</div><div>' + value + '</div></li>';
        }

    };

    // 成长详情
    $.showProDetail = function (options) {
        var $tr = $('<tr/>').css('background', '#F6F8F9').addClass('proDetail'),
            $td = $('<td colspan="5"/>'),
            $table = $('<table border="0" cellpadding="0" cellspacing="1"><tbody><tr><td><strong>学习成长目标</strong></td><td><strong>见证资料</strong></td><td width="100" align="center"><strong>最高分</strong></td></tr></tbody></table>');
        $.each(options, function (key, value) {
            $table.append('<tr><td>' + value.Project_target + '</td><td>' + value.Project_material + '</td><td align="center">' + value.Project_code + '</td></tr>');
        });
        $td.append($table);
        return $tr.append('<td></td>').append($td);
    }

    //成长弹窗
    $.createProBox = function (options) {
        var $pop_outBox = $('<div/>').addClass('pop_outBox'),
            $pop_innerBox = $('<div/>').addClass('pop_innerBox').addClass(options.width)
                .append($.createTitle(options))
                .append($.createProContent(options))
                .append($.createButton(options));

        $pop_outBox.append($pop_innerBox);
        return $pop_outBox;
    };

    // 邮件内容
    $('.email').click(function () {
        if (!$(this).hasClass('onShow')) {
            $.ajax({
                type: "GET",
                url: "package.json",
                dataType: "json",
                context: $(this),
                success: function (result) {
                    $(this).showHistory({
                        "contentList": result.email_box
                    });
                }
            });
        }
    });
});