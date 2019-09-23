package me.wuwenbin.noteblogv5.controller.management;

import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.log.LogService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/management")
public class DashboardController extends BaseController {

    private final CommentService commentService;
    private final MessageService messageService;
    private final LogService logService;
    private final UserService userService;

    public DashboardController(CommentService commentService,
                               MessageService messageService, LogService logService, UserService userService) {
        this.commentService = commentService;
        this.messageService = messageService;
        this.logService = logService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("nbv5version", NBV5.VERSION);
        model.addAttribute("latestComment", commentService.findLatestComment());
        model.addAttribute("latestMessage", messageService.findLatestMessage());
        model.addAttribute("ipSummary", logService.ipSummary());
        model.addAttribute("urlSummary", logService.urlSummary());
        model.addAttribute("todayComment", commentService.findTodayComment());
        model.addAttribute("todayMessage", messageService.findTodayMessage());
        model.addAttribute("todayUser", userService.findTodayUser());
        model.addAttribute("todayVisit", logService.todayVisit());
        return "management/dashboard";
    }
}
