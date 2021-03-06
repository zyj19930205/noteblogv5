/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.define(['element', 'form', 'upload'], function (exports) {
    var form = layui.form;
    var element = layui.element;
    var upload = layui.upload;
    element.render();
    form.render();


    form.on('switch(switchFilter)', function (data) {
        var status = data.elem.checked ? 1 : 0;
        var name = $(data.elem).attr("name");
        NBV5.ajax("/management/settings/update", {
            type: "switch"
            , name: name
            , value: status
        }, function (resp) {
            if (resp.code === NBV5.status.ok) {
                layer.tips('修改成功！', data.othis);
                if (name === 'global_comment_onoff') {
                    $("#comment-open").css("display", data.elem.checked ? 'block' : 'none');
                } else if (name === 'is_open_message') {
                    $("#message-open").css("display", data.elem.checked ? 'block' : 'none');
                } else if (name === 'qq_login_onoff') {
                    $("#qq-login-id").css("display", data.elem.checked ? 'block' : 'none');
                    $("#qq-login-key").css("display", data.elem.checked ? 'block' : 'none');
                } else if (name === 'github_login_onoff') {
                    $("#github-login-id").css("display", data.elem.checked ? 'block' : 'none');
                    $("#github-login-key").css("display", data.elem.checked ? 'block' : 'none');
                } else if (name === 'is_open_oss_upload') {
                    $("#qiniu-access-key").css("display", data.elem.checked ? 'block' : 'none');
                    $("#qiniu-bucket").css("display", data.elem.checked ? 'block' : 'none');
                    $("#qiniu-domain").css("display", data.elem.checked ? 'block' : 'none');
                    $("#qiniu-secret-key").css("display", data.elem.checked ? 'block' : 'none');
                }
            }
        })
    });


    form.on('submit(settings)', function (data) {
        var $this = $(data.elem);
        var $data = $this.parent(".layui-inline").prev(".layui-inline").find("input.layui-input:eq(0)");
        var name = $data.attr("name");
        name = name === undefined ? $this.parent("p").prev("textarea").attr("name") : name;
        var value = $data.val();
        value = value === undefined ? $this.parent("p").prev("textarea").val() : value;
        NBV5.post("/management/settings/update", {
            name: name,
            value: value
        }, function (json) {
            NBV5.okMsgHandle(json);
        });
        return false;
    });

    form.on('submit(bottom-logo)', function () {
        NBV5.post("/management/settings/update", {
            name: 'bottom_logo',
            value: $("#logo-avatar").find("img").attr("src")
        }, function (json) {
            NBV5.okMsgHandle(json);
        });
        return false;
    });

    form.on('radio(typesetting)', function (data) {
        NBV5.post("/management/settings/update", {
            type: 'text'
            , name: 'typesetting'
            , value: data.value
        }, function (json) {
            NBV5.okMsgHandle(json);
        })
    });


    form.on('radio(article_page_style)', function (data) {
        NBV5.post("/management/settings/update", {
            type: 'text'
            , name: 'article_page_style'
            , value: data.value
        }, function (json) {
            NBV5.okMsgHandle(json);
        })
    });

    upload.render({
        elem: '#logo-avatar' //绑定元素
        , url: '/management/upload' //上传接口
        , data: {
            reqType: 'lay'
        }
        , done: function (res) {
            if (res.code === 0) {
                $("#logo-avatar").find("img").attr("src", res.data.src);
                layer.msg(res.msg || res.message);
            } else {
                layer.msg(res.message)
            }
        }
        , error: function () {
            layer.msg("上传失败！");
        }
    });

    exports('website', {});

});


