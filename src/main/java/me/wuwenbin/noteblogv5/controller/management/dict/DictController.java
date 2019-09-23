package me.wuwenbin.noteblogv5.controller.management.dict;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.entity.Dict;
import me.wuwenbin.noteblogv5.service.interfaces.dict.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/management/dict")
public class DictController extends BaseController {

    private final HttpServletRequest request;
    private final DictService dictService;


    public DictController(HttpServletRequest request, DictService dictService) {
        this.request = request;
        this.dictService = dictService;
    }

    @GetMapping("/catetag")
    public String cateTagPage(String cateName, String tagName) {
        request.setAttribute("cateList",
                dictService.list(Wrappers.<Dict>query()
                        .eq("`group`", DictGroup.GROUP_CATE)
                        .like(StrUtil.isNotEmpty(cateName), "name", cateName)));
        request.setAttribute("searchCate", cateName);
        request.setAttribute("tagList",
                dictService.list(Wrappers.<Dict>query()
                        .eq("`group`", DictGroup.GROUP_TAG)
                        .like(StrUtil.isNotEmpty(tagName), "name", tagName)));
        request.setAttribute("searchTag", tagName);
        return "management/dict/cates_tags";
    }

    @GetMapping("/keyword")
    public String keywordPage(String keyword) {
        request.setAttribute("keywords",
                dictService.list(Wrappers.<Dict>query()
                        .eq("`group`", DictGroup.GROUP_KEYWORD)
                        .like(StrUtil.isNotEmpty(keyword), "name", keyword)));
        request.setAttribute("searchKeyword", keyword);
        return "management/dict/keyword";
    }

    @GetMapping("/link")
    public String linkPage(String name) {
        request.setAttribute("links",
                dictService.list(Wrappers.<Dict>query()
                        .eq("`group`", DictGroup.GROUP_LINK)
                        .like(StrUtil.isNotEmpty(name), "name", StrUtil.format("{},", name))));
        request.setAttribute("searchLinkName", name);
        return "management/dict/link";
    }


    @PostMapping("/cate/add")
    @ResponseBody
    public ResultBean addCate(String cateName) {
        Dict c = dictService.getOne(Wrappers.<Dict>query().eq("`group`", DictGroup.GROUP_CATE).eq("name", cateName));
        if (c == null) {
            boolean res = dictService.save(Dict.builder().name(cateName).group(DictGroup.GROUP_CATE).build());
            return handle(res, "添加成功！", "添加失败！");
        } else {
            return ResultBean.error("已经有此分类，不能重复添加！");
        }
    }

    @PostMapping("/tag/add")
    @ResponseBody
    public ResultBean addTag(String tagName) {
        Dict t = dictService.getOne(Wrappers.<Dict>query().eq("`group`", DictGroup.GROUP_TAG).eq("name", tagName));
        if (t == null) {
            boolean res = dictService.save(Dict.builder().name(tagName).group(DictGroup.GROUP_TAG).build());
            return handle(res, "添加成功！", "添加失败！");
        } else {
            return ResultBean.error("已经有此标签，不能重复添加！");
        }
    }

    @PostMapping("/keyword/add")
    @ResponseBody
    public ResultBean addKeyword(String keyword) {
        Dict t = dictService.getOne(Wrappers.<Dict>query().eq("`group`", DictGroup.GROUP_KEYWORD).eq("name", keyword));
        if (t == null) {
            boolean res = dictService.save(Dict.builder().name(keyword).group(DictGroup.GROUP_KEYWORD).build());
            return handle(res, "添加成功！", "添加失败！");
        } else {
            return ResultBean.error("已经有此关键字，不能重复添加！");
        }
    }

    @PostMapping("/link/add")
    @ResponseBody
    public ResultBean addLink(String linkName, String linkHref) {
        Dict t = dictService.getOne(Wrappers.<Dict>query()
                .eq("`group`", DictGroup.GROUP_LINK)
                .likeRight("name", StrUtil.format("{},", linkName)));
        if (t == null) {
            boolean res = dictService.save(
                    Dict.builder().name(StrUtil.format("{}，{}", linkName, linkHref)).group(DictGroup.GROUP_LINK).build());
            return handle(res, "添加成功！", "添加失败！");
        } else {
            return ResultBean.error("已存在，请检查是否重复添加！");
        }
    }

    @PostMapping("/cate/delete")
    @ResponseBody
    public ResultBean deleteCate(Long id) {
        int c = dictService.articleCateReferCnt(id);
        if (c > 0) {
            return ResultBean.error("请删除所有相关的文章再做操作！");
        } else {
            boolean res = dictService.removeById(id);
            return handle(res, "删除成功！", "删除失败！");
        }
    }

    @PostMapping("/tag/delete")
    @ResponseBody
    public ResultBean deleteTag(Long id) {
        int c = dictService.articleTagReferCnt(id);
        if (c > 0) {
            return ResultBean.error("请删除所有相关的文章再做操作！");
        } else {
            boolean res = dictService.removeById(id);
            return handle(res, "删除成功！", "删除失败！");
        }
    }

    @PostMapping("/keyword/delete")
    @ResponseBody
    public ResultBean deleteKeyword(Long id) {
        boolean res = dictService.removeById(id);
        return handle(res, "删除成功！", "删除失败！");
    }

    @PostMapping("/link/delete")
    @ResponseBody
    public ResultBean deleteLink(Long id) {
        boolean res = dictService.removeById(id);
        return handle(res, "删除成功！", "删除失败！");
    }
}
