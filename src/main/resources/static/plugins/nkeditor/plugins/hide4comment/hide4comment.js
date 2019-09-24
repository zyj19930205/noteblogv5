KindEditor.plugin('hide4comment', function (K) {
    var editor = this, name = 'hide4comment';
    // 点击图标时执行
    editor.clickToolbar(name, function () {

        // var html = '<div data-type="comment" style="border-left: 3px solid #00a2d4;  padding-left: 5px;">sdadsa</div>';
        // editor.insertHtml(html);

        var dialog = K.dialog({
            width: 600,
            height: 430,
            title: '添加隐藏模块（回复可见）',
            body: '<div style="margin:10px;">' +
                '<div id="hide4comment" style="height: 300px;">' +
                '<p>在此添加隐藏内容（回复可见）</p>' +
                '</div>',
            closeBtn: {
                name: '关闭',
                click: function (e) {
                    dialog.remove();
                }
            },
            yesBtn: {
                name: '确定',
                click: function (e) {
                    var finalHtml = "<div class='hide4comment'>" + hideCommentEditor.txt.html() + "</div><br/>";
                    editor.insertHtml(finalHtml);
                    dialog.remove();
                }
            },
            noBtn: {
                name: '取消',
                click: function (e) {
                    dialog.remove();
                }
            }
        });

        var E = window.wangEditor;
        var hideCommentEditor = new E('#hide4comment');
        hideCommentEditor.create()


    });
});