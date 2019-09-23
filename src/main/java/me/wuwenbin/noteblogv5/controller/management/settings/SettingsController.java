package me.wuwenbin.noteblogv5.controller.management.settings;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.entity.Param;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import me.wuwenbin.noteblogv5.util.CacheUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/management/settings")
public class SettingsController extends BaseController {

    private final HttpServletRequest request;
    private final ParamService paramService;
    private final UserService userService;

    public SettingsController(HttpServletRequest request, ParamService paramService, UserService userService) {
        this.request = request;
        this.paramService = paramService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("alipay", paramService.findByName(NBV5.QRCODE_ALIPAY).getValue());
        model.addAttribute("wechat", paramService.findByName(NBV5.QRCODE_WECHAT).getValue());
        return "management/settings/profile";
    }


    @GetMapping("/website")
    public String websitePage(Model model) {
        List<Param> params = paramService.list(Wrappers.<Param>query().ge("`group`", 0));
        Map<String, Object> attributeMap = params.stream().collect(Collectors.toMap(Param::getName, p -> p.getValue() == null ? "" : p.getValue()));
        model.addAllAttributes(attributeMap);
        return "management/settings/website";
    }


    @RequestMapping("/update")
    @ResponseBody
    public ResultBean update(String name, String value) {
        if (StringUtils.isEmpty(name)) {
            return ResultBean.error("参数名为空！");
        } else {
            return paramService.updateSettings(name, value);
        }
    }


    @RequestMapping("/pay/update")
    @ResponseBody
    public ResultBean updateQrcode(String value, String name, String msg) {
        boolean res = paramService.update(Wrappers.<Param>update().set("value", value).eq("name", name));
        CacheUtils.clearAllParamCache();
        return handle(res, StrUtil.format("修改{}成功！", msg), StrUtil.format("修改{}失败！", msg));
    }


    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean updateProfile(String nickname, String email, String password1, String password2, String avatar) {
        User loginUser = getSessionUser(request);
        if (StrUtil.isNotEmpty(nickname)) {
            userService.update(
                    Wrappers.<User>update().set("nickname", nickname).eq("id", loginUser.getId()));
            paramService.update(NBV5.INFO_LABEL_NICKNAME, nickname);
        }
        if (!StringUtils.isEmpty(password1)) {
            if (password1.equals(password2)) {
                String dbPass = SecureUtil.md5(password1);
                userService.update(
                        Wrappers.<User>update().set("password", dbPass).eq("id", loginUser.getId())
                );
            } else {
                return ResultBean.error("两次输入的密码不一致！");
            }
        }
        if (!StringUtils.isEmpty(avatar)) {
            userService.update(
                    Wrappers.<User>update().set("avatar", avatar).eq("id", loginUser.getId()));
            paramService.update(NBV5.INFO_LABEL_LOGO, avatar);
        }
        if (!StringUtils.isEmpty(email)) {
            userService.update(
                    Wrappers.<User>update().set("email", email).eq("id", loginUser.getId())
            );
        }
        return ResultBean.ok("重新登录生效，更新信息！");
    }


    @RequestMapping("/mail/update")
    @ResponseBody
    public ResultBean updateMailConfig(HttpServletRequest request) {
        return paramService.updateMailConfig(WebUtils.getParametersStartingWith(request, ""));
    }

}
