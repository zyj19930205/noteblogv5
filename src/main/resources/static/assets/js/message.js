/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */

layui.define(['laytpl', 'timeago', 'laypage'], function (exports) {
    var tpl = layui.laytpl,
        timeago = layui.timeago,
        laypage = layui.laypage;
    var _tpl = '' +
        '<div class="article-detail layui-row layui-col-space15">' +


        '<div class="layui-col-md9 animated fadeInUp">' +

        '<div class="layui-card" id="message-panel">' +
        '<div class="layui-card-body" style="padding: 20px 15px !important;">' +
        '<div class="layui-row">' +
        '<blockquote class="layui-elem-quote">{{d.settings.message_top_notice}}</blockquote> ' +
        '</div>' +
        '<div class="layui-row layui-form layui-mt20">' +
        '<div class="layui-col-sm1 layui-hide-xs layui-show-sm-inline-block layui-user-avatar">' +
        '       {{# if(d.nbv5su != null){ }}' +
        '<img src="{{d.nbv5su.avatar}}" class="layui-circle">' +
        '       {{# }else{ }}' +
        '<a href="{{ loginUrl()}}" id="nologin"><img src="/static/assets/img/nologin.jpg" class="layui-circle"></a>' +
        '       {{# } }}' +
        '</div>' +
        '<div class="layui-col-sm11 layui-col-xs12">' +
        '<input type="hidden" name="replyId">' +
        '<input type="hidden" name="userId" value="{{d.nbv5su!=null?d.nbv5su.id:""}}">' +
        '<input type="hidden" name="nickname" value="{{d.nbv5su!=null?d.nbv5su.nickname:""}}">' +
        '<textarea id="message-reply" name="comment" required lay-verify="required" placeholder="{{d.settings.message_onoff == "1"?"输入你想说的话":"未开放留言功能"}}" ' +
        '       {{# if( d.settings.message_onoff == "0"){ }}' +
        ' disabled="disabled"' +
        '       {{# } }}' +
        ' class="layui-textarea comment" onfocus="commentFocus(this);" onblur="commentBlur(this);"></textarea>' +
        '       {{# if( d.settings.message_onoff == "1"){ }}' +
        '<p class="layui-mt15"><button class="layui-btn layui-btn-normal layui-btn-sm" onclick="submitComment(this);">发表留言</button>' +
        '<button class="layui-btn layui-btn-primary layui-btn-sm" style="display: none;" id="cancelReply" onclick="cancelReply();">取消回复</button></p>' +
        '       {{# } }}' +
        '       {{# if(d.settings.message_onoff == "0"){ }}' +
        '<p class="layui-mt15"><button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-disabled">未开放留言</button>' +
        '       {{# } }}' +
        '</div>' +
        '</div>' +

        '<div id="message-list">' +
        '{{# layui.each(d.messages.records, function(index, item){ }}' +
        '<div class="layui-row comment-html">' +
        '<div class="layui-col-xs1 layui-user-avatar" style="min-width: 45px;">' +
        '<img src="{{item.avatar}}" class="layui-circle" style="min-width: 40px;display: inline-block;">' +
        '</div>' +
        '<div class="layui-col-xs10 comment-content" style="color: #929292;">' +
        '<label>{{item.nickname}} (#{{item.id}}</label>,' +
        '<span class="timeago" datetime="{{ nbv5front.timeAgo(item.post) }}"></span>)：' +
        '<span class="comment-txt" style="color: #000;line-height: 24px;word-break: break-all;">{{item.comment}}</span>' +
        '<a class="reply" data-comment-id="{{item.id}}" style="display: none;">回复</a> ' +
        '</div>' +
        '</div>' +
        '{{# });  }}' +
        '</div>' +

        '</div>' +
        '<div class="row layui-container">' +
        '       {{# if(d.messages.total>0){ }}' +
        '     <p id="message-page"></p>' +
        '       {{# } }}' +
        '</div>' +
        '</div>' +

        '</div>' +

        '<div class="layui-col-md3">' +
        '<div id="affix-side">' +

        '<div class="layui-card" id="info-panel">' +
        '<div class="layui-card-body text-center" style="padding: 20px;">' +
        '<a href="/login" target="_blank"><img src="{{d.settings.info_label_logo}}" class="layui-circle" style="max-width: 150px;" alt="{{d.settings.info_label_nickname}}"></a>' +
        '<p class="layui-text" style="margin: 10px;color: #F44336;"><b>{{d.settings.info_label_nickname}}</b></p>' +
        '<hr>' +
        '<div>{{d.settings.info_label_text}}</div>' +
        '</div>' +
        '</div>' +

        '<div class="layui-card" id="search-panel">' +
        '<div class="layui-card-body">' +
        ' <p class="title">搜索 <small style="float: right;">' +
        '   <a style="cursor: pointer;" onclick="aboutSearch();" target="_blank"><i>关于 <i class="fa fa-info-circle"></i></i></a></small> </p>' +
        '       <hr>' +
        '       <input name="words" onkeypress="searchAll(event);"' +
        '           placeholder="键入Enter键以搜索" autocomplete="off" class="layui-input search-box">' +
        '</div>' +
        '</div>' +

        '<div class="layui-card" id="desc-panel">' +
        '<div class="layui-card-body">' +
        '<div class="layui-text">' +
        '<p>QQ群：<a href="https://jq.qq.com/?_wv=1027&k=5ZEGGl8" target="_blank">697053454</a> </p>' +
        '<p class="layui-mt5">GitHub：<a href="https://github.com/miyakowork/noteblogv5" target="_blank">noteblogv5</a> </p>' +
        '<p class="layui-mt5">Gitee：<a href="https://gitee.com/wuwenbn/" target="_blank">noteblogv5</a> </p>' +
        '<p class="layui-mt5">官网：<a href="https://noteblog.vip" target="_blank">noteblog.vip</a> </p>' +
        '</div>' +
        '</div>' +
        '</div>' +


        '<div class="layui-card layui-tags" id="link-panel">' +
        '<div class="layui-card-body">' +
        '<p class="title">友情链接</p>' +
        '<hr>' +
        '{{# layui.each(d.linkList, function(index, item){ }}' +
        '<a href="{{linkSplit(item.name,1)}}" target="_blank" class="layui-text" style="margin-right: 10px;">{{ linkSplit(item.name,0) }}</a>' +
        '{{# });  }}' +
        '</div>' +
        '</div>' +


        '</div>' +
        '</div>' +


        '</div>';


    exports('message', function (settings, linkList, nbv5su, messages) {

        var obj = {
            settings: settings,
            linkList: linkList,
            nbv5su: nbv5su,
            messages: messages
        };

        tpl(_tpl).render(obj, function (html) {
            $("#main-body").prepend(html);
            stickySideBar();
            loginTips();
            hoverReply();
            replyOne(nbv5su);
            timeago.render($(".timeago"));
            messagePage(laypage, messages, tpl, timeago);
        });

    });

});


function linkSplit(link, index) {
    var links = link.split("，");
    if (index === 0 || index === 1) {
        return links[index];
    } else {
        return "";
    }
}

function searchAll(e) {
    var keyNum = window.event ? e.keyCode : e.which;
    if (keyNum === 13) {
        var words = $("input[name=words]").val();
        location.href = "/s/w?q=" + encodeURI(words);
    }
}

function aboutSearch() {
    layer.msg("仅做标题和文章原始内容关键字匹配！");
}

function stickySideBar() {
    nbv5front.clearSticky();

    var p = 0, t = 0;
    var sticky;

    $(window).scroll(function () {
        p = $(this).scrollTop();
        if (t < p) {
            if (sticky != null) {
                sticky.destroy();
            }
            sticky = new hcSticky("#affix-side", {
                stickTo: '#main-body',
                top: 15,
                queries: {980: {disable: true}}
            });
            //下滚
        } else {
            if (sticky != null) {
                sticky.destroy();
            }
            sticky = new hcSticky("#affix-side", {
                stickTo: '#main-body',
                top: 65,
                queries: {980: {disable: true}}
            });
            //上滚
        }
        setTimeout(function () {
            t = p;
        }, 0)
    })
}

function commentFocus(t) {
    $(t).css("height", "100px");
}

function commentBlur(t) {
    var txt = $(t).val();
    if (txt.length === 0) {
        $(t).css("height", "40px");
    }
}

function loginUrl() {
    return "/login?redirectUrl=" + location.href;
}

function loginTips() {
    var __loginIndex;
    $("#nologin").hover(function () {
        __loginIndex = layer.tips('请先登录', this, {
            tips: [1, '#F44336']
        });
    }, function () {
        layer.close(__loginIndex);
    });
}

function hoverReply() {
    $(".comment-content").hover(function () {
        $(this).find(".reply").css("display", "inline-block");
    }, function () {
        $(this).find(".reply").css("display", "none");
    })
}

function replyOne(nbv5su) {
    $(".reply").on("click", function () {
        if (nbv5su === null) {
            layer.confirm('请先登录再操作，是否现在登录？', {icon: 4, title: '消息提示'}, function (index) {
                location.href = "/login?redirectUrl=" + location.href;
            });
        }
        var userTxt = $(this).parent(".comment-content").find("label").text();
        var replyId = $(this).attr("data-comment-id");
        $("input[name=replyId]").val(replyId);
        $("input[name=nickname]").val(userTxt);
        $("textarea[name=comment]").attr("placeholder", "回复@" + userTxt + "): ");
        location.href = "#message-reply";
        $("#cancelReply").css("display", "inline-block");
    });
}

function cancelReply() {
    $("textarea[name=comment]").attr("placeholder", "输入你想说的话");
    $("input[name=replyId]").val("");
    $("#cancelReply").css("display", "none");
}

var tempTimes = false;

function submitComment(e) {
    if (!tempTimes) {
        var userId = $("input[name=userId]").val();
        if (userId === "") {
            layer.confirm('请先登录再操作，是否现在登录？', {icon: 4, title: '消息提示'}, function (index) {
                location.href = "/login?redirectUrl=/message";
            });
        } else {
            var comment = $("textarea[name=comment]").val();
            var replyId = $("input[name=replyId]").val();
            var nickname = $("input[name=nickname]").val();
            comment = replyId !== null && replyId !== "" ? "回复@<span style='color: #00c4ff;'>" + nickname + ")</span>: " + comment : comment;
            if (comment === "") {
                layer.msg("请填写你想发表的内容！");
            } else {
                $.ajax({
                    url: "/message/token/sub",
                    type: "post",
                    dataType: "json",
                    data: {
                        userId: userId,
                        replyId: replyId,
                        enable: true,
                        comment: comment
                    },
                    success: function (resp) {
                        layer.msg(resp.message || resp.msg);
                        setTimeout(function () {
                            if (resp.code === 200) {
                                location.reload();
                            }
                        }, 1000);
                    },
                    beforeSend: function () {
                        $(e).text("发送中...")
                        tempTimes = true;
                    },
                    complete: function () {
                        $(e).text("发表留言");
                        tempTimes = false;
                    }
                })

            }
        }
    } else {
        layer.msg("不要着急，请等待一会！");
    }
}

function messagePage(laypage, messages, tpl, timeago) {
    laypage.render({
        elem: "message-page"
        , count: messages.total
        , limit: 20
        , layout: ['prev', 'next']
        , jump: function (obj, first) {
            if (!first) {
                layer.msg('第 ' + obj.curr + ' 页');
                $.post("/message/lists", {
                    current: obj.curr
                }, function (cs) {
                    var pageTpl = '{{# layui.each(d.records, function(index, item){ }}' +
                        '<div class="layui-row comment-html">' +
                        '<div class="layui-col-xs1 layui-user-avatar" style="min-width: 45px;">' +
                        '<img src="{{item.avatar}}" class="layui-circle">' +
                        '</div>' +
                        '<div class="layui-col-xs11 comment-content">' +
                        '<label>{{item.nickname}} (#{{item.id}}</label>,' +
                        '<span class="timeago" datetime="{{ nbv5front.timeAgo(item.post) }}"></span>)：' +
                        '<span class="comment-txt" style="color: #000;line-height: 24px;word-break: break-all;">{{item.comment}}</span>' +
                        '<a class="reply" data-comment-id="{{item.id}}" style="display: none;">回复</a> ' +
                        '</div>' +
                        '</div>' +
                        '{{# });  }}';
                    tpl(pageTpl).render(cs, function (html) {
                        $("#message-list").html(html);
                        timeago.render($(".timeago"));
                    });
                })
            }
        }
    });
}
