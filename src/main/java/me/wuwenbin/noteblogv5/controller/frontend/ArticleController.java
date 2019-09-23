package me.wuwenbin.noteblogv5.controller.frontend;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.bo.CommentBo;
import me.wuwenbin.noteblogv5.model.entity.Article;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.content.ArticleService;
import me.wuwenbin.noteblogv5.service.interfaces.dict.DictService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.Map;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private final ArticleService articleService;
    private final DictService dictService;
    private final UserService userService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, DictService dictService, UserService userService, CommentService commentService) {
        this.articleService = articleService;
        this.dictService = dictService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/{aId}")
    public String article(@PathVariable("aId") Long aId, Model model,
                          @ModelAttribute("settings") Map settingsMap,
                          Page<CommentBo> commentPage) {
        articleService.updateViewsById(aId);

        Article article = articleService.getById(aId);
        model.addAttribute("article", article);
        model.addAttribute("author", userService.getById(article.getAuthorId()).getNickname());

        model.addAttribute("cateList", dictService.findList(DictGroup.GROUP_CATE));
        model.addAttribute("tags", dictService.findTagsByArticleId(aId));

        String articlePageStyle = MapUtil.getStr(settingsMap, "article_page_style");
        if ("-1".equalsIgnoreCase(articlePageStyle) || "1".equalsIgnoreCase(articlePageStyle)) {
            model.addAttribute("tagList", dictService.findTagList30());
            model.addAttribute("linkList", dictService.findList(DictGroup.GROUP_LINK));
            model.addAttribute("randomArticles", articleService.findRandomArticles(8));
        }
        OrderItem oi = OrderItem.desc("post");
        commentPage.addOrder(oi);
        model.addAttribute("comments", commentService.findCommentPage(commentPage, null, null, Collections.singletonList(aId), true));
        return "frontend/article";
    }

    @GetMapping("/u/{urlSeq}")
    public ModelAndView articleUrl(@PathVariable("urlSeq") String urlSeq) {
        urlSeq = StrUtil.isNotEmpty(urlSeq) ? urlSeq : "";
        Article article = articleService.getOne(Wrappers.<Article>query().eq("url_seq", "/" + urlSeq));
        return new ModelAndView(new RedirectView("/article/" + article.getId()));
    }

    @PostMapping("/comments")
    @ResponseBody
    public IPage<CommentBo> comments(Page<CommentBo> page, Long articleId) {
        OrderItem oi = OrderItem.desc("post");
        page.addOrder(oi);
        return commentService.findCommentPage(page, null, null, Collections.singletonList(articleId), true);
    }


    @PostMapping("/approve")
    @ResponseBody
    public ResultBean approve(@RequestParam Long articleId) {
        boolean res = articleService.updateApproveCntById(articleId) == 1;
        return handle(res, "感谢您的点赞！", "请稍后再试！");
    }

}
