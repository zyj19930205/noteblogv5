package me.wuwenbin.noteblogv5.controller.common;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.model.entity.Param;
import me.wuwenbin.noteblogv5.service.interfaces.content.ArticleService;
import me.wuwenbin.noteblogv5.service.interfaces.dict.DictService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import me.wuwenbin.noteblogv5.util.CacheUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by Wuwenbin on 2018/9/7 at 9:35
 *
 * @author wuwenbin
 */
@ControllerAdvice(basePackages = "me.wuwenbin.noteblogv5.controller")
public class GlobalController {

    private final ParamService paramService;
    private final DictService dictService;
    private final ArticleService articleService;
    private final CommentService commentService;

    public GlobalController(ParamService paramService, DictService dictService,
                            ArticleService articleService, CommentService commentService) {
        this.paramService = paramService;
        this.dictService = dictService;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @ModelAttribute("settings")
    public void addSettings(Model model, HttpServletRequest request) {
        String genParams = "gen_params";
        Map settingsMap = CacheUtils.getParamCache().get(genParams, Map.class);
        if (settingsMap != null && settingsMap.size() > 0) {
            model.addAttribute("settings", settingsMap);
        } else {
            List<Param> params = paramService.list(Wrappers.<Param>query().ge("`group`", 0));
            settingsMap = params.stream().collect(Collectors.toMap(Param::getName, p -> p.getValue() == null ? "" : p.getValue()));
            CacheUtils.getParamCache().put(genParams, settingsMap);
            model.addAttribute("settings", settingsMap);
        }
        if (!request.getRequestURL().toString().contains("/management/")) {
            model.addAttribute("cateList", dictService.findList(DictGroup.GROUP_CATE));
            model.addAttribute("blogCount", articleService.count());
            model.addAttribute("blogWords", articleService.sumArticleWords());
            model.addAttribute("runningDays", paramService.calcRunningDays());
            model.addAttribute("commentCount", commentService.count());
        }
    }
}
