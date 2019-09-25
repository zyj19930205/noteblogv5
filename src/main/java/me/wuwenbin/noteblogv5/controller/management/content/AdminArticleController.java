package me.wuwenbin.noteblogv5.controller.management.content;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stuxuhai.jpinyin.PinyinException;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.LayuiTable;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.entity.*;
import me.wuwenbin.noteblogv5.service.interfaces.content.ArticleService;
import me.wuwenbin.noteblogv5.service.interfaces.content.HideService;
import me.wuwenbin.noteblogv5.service.interfaces.dict.DictService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/management/article")
public class AdminArticleController extends BaseController {

    private final ArticleService articleService;
    private final DictService dictService;
    private final HideService hideService;
    private final CommentService commentService;

    public AdminArticleController(ArticleService articleService,
                                  DictService dictService, HideService hideService,
                                  CommentService commentService) {
        this.articleService = articleService;
        this.dictService = dictService;
        this.hideService = hideService;
        this.commentService = commentService;
    }

    @GetMapping("/add")
    public String publishArticlePage(HttpServletRequest request) {
        request.setAttribute("cateList", dictService.list(Wrappers.<Dict>query().eq("`group`", DictGroup.GROUP_CATE)));
        return "management/article/add";
    }

    @GetMapping("/edit")
    public String edit(Model model, String id, HttpServletRequest request) {
        Article article = articleService.getById(id);
        articleService.handleShowArticle(article, getSessionUser(request));
        model.addAttribute("cateList", dictService.list(Wrappers.<Dict>query().eq("`group`", DictGroup.GROUP_CATE)));
        model.addAttribute("editArticle", article);
        model.addAttribute("tags", dictService.findTagsByArticleId(id));
        model.addAttribute("cates", dictService.findCatesByArticleId(id));
        return "management/article/edit";
    }

    @GetMapping("/page")
    public String articleListPage() {
        return "management/article/list";
    }

    @GetMapping("/list")
    @ResponseBody
    public LayuiTable<Article> articleListPage(Page<Article> page, String sort, String order, String title) {
        addPageOrder(page, order, sort);
        IPage<Article> articlePage = articleService.page(page, Wrappers.<Article>query().like(StrUtil.isNotEmpty(title), "title", title));
        return new LayuiTable<>(articlePage.getTotal(), articlePage.getRecords());
    }


    @PostMapping("/create")
    @ResponseBody
    public ResultBean articleCreate(@Valid Article article, BindingResult result, HttpServletRequest request,
                                    @RequestParam(required = false, value = "cateIds[]") List<Integer> cateIds,
                                    @RequestParam(required = false, value = "tagNames[]") List<String> tagNames) {
        if (result.getErrorCount() == 0) {
            if (cateIds.size() > 3) {
                return ResultBean.error("分类选择最多不能超过3个！");
            }
            User su = getSessionUser(request);
            article.setAuthorId(su.getId());
            try {
                int cnt = articleService.createArticle(article, cateIds, tagNames);
                return handle(cnt > 0, "发布成功！", "发布失败！");
            } catch (PinyinException e) {
                return ResultBean.error("自定义文章链接出现非法值，请重新输入！");
            }
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }

    @ResponseBody
    @PostMapping("/update/{field}")
    public ResultBean updateArticle(String id, @PathVariable("field") String field, Boolean status) {
        String appreciable = "appreciable", commented = "commented", top = "top";
        if (top.equalsIgnoreCase(field)) {
            boolean res = articleService.updateTopById(id, status);
            return handle(res, "操作成功！", "操作失败！");
        } else if (appreciable.equalsIgnoreCase(field) || commented.equalsIgnoreCase(field)) {
            boolean res = articleService.update(Wrappers.<Article>update().set(field, status).eq("id", id));
            return handle(res, "修改成功！", "修改失败！");
        } else {
            return ResultBean.error("参数错误！");
        }
    }


    @PostMapping("/update")
    @ResponseBody
    public ResultBean articleUpdate(@Valid Article article, BindingResult result, HttpServletRequest request,
                                    @RequestParam(required = false, value = "cateIds[]") List<Integer> cateIds,
                                    @RequestParam(required = false, value = "tagNames[]") List<String> tagNames) {
        if (result.getErrorCount() == 0) {
            if (cateIds.size() > 3) {
                return ResultBean.error("分类选择最多不能超过3个！");
            }
            User su = getSessionUser(request);
            article.setAuthorId(su.getId());
            try {
                int cnt = articleService.updateArticle(article, cateIds, tagNames);
                return handle(cnt > 0, "修改成功！", "修改失败！");
            } catch (PinyinException e) {
                return ResultBean.error("自定义文章链接出现非法值，请重新定义！");
            }
        } else {
            return ajaxJsr303(result.getFieldErrors());
        }
    }


    @PostMapping("/delete")
    @ResponseBody
    public ResultBean delete(String id) {
        boolean res = articleService.removeById(id);
        dictService.deleteArticleRefer(id);
        hideService.deleteArticlePurchaseRefer(id);
        hideService.remove(Wrappers.<Hide>query().eq("article_id", id));
        commentService.remove(Wrappers.<Comment>query().eq("article_id", id));
        return handle(res, "删除成功！", "删除失败！");
    }
}
