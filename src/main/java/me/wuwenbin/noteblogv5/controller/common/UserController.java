package me.wuwenbin.noteblogv5.controller.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.code.kaptcha.Constants;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.bo.login.GithubLoginData;
import me.wuwenbin.noteblogv5.model.bo.login.QqLoginData;
import me.wuwenbin.noteblogv5.model.bo.login.SimpleLoginData;
import me.wuwenbin.noteblogv5.model.entity.Param;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.service.interfaces.LoginService;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuwen
 */
@Controller
public class UserController extends BaseController {

    private static final String MANAGEMENT_INDEX = "/management/index";
    private static final String FRONTEND_INDEX = "/";

    private final HttpServletRequest request;
    private final ParamService paramService;
    private final LoginService<ResultBean, QqLoginData> qqLoginService;
    private final LoginService<ResultBean, GithubLoginData> githubLoginService;
    private final LoginService<ResultBean, SimpleLoginData> simpleLoginService;

    public UserController(HttpServletRequest request, ParamService paramService,
                          LoginService<ResultBean, QqLoginData> qqLoginService, LoginService<ResultBean, GithubLoginData> githubLoginService, LoginService<ResultBean, SimpleLoginData> simpleLoginService) {
        this.request = request;
        this.paramService = paramService;
        this.qqLoginService = qqLoginService;
        this.githubLoginService = githubLoginService;
        this.simpleLoginService = simpleLoginService;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(String redirectUrl, HttpServletRequest request) {
        request.getSession().setAttribute("tempUrl", redirectUrl);
        User sessionUser = getSessionUser(request);
        if (sessionUser != null) {
            if (StrUtil.isNotEmpty(redirectUrl)) {
                return new ModelAndView(new RedirectView(redirectUrl));
            } else {
                return new ModelAndView(new RedirectView(MANAGEMENT_INDEX));
            }
        } else {
            ModelAndView mav = new ModelAndView("login");
            Param qqParam = paramService.findByName(NBV5.QQ_APP_ID);
            Param githubParam = paramService.findByName(NBV5.GITHUB_CLIENT_ID);
            mav.addObject("isOpenQqLogin",
                    (qqParam != null && StrUtil.isNotEmpty(qqParam.getValue())));
            mav.addObject("isOpenGithubLogin",
                    (githubParam != null && StrUtil.isNotEmpty(githubParam.getValue())));
            return mav;
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean login(SimpleLoginData data) {
        if (StringUtils.isEmpty(data.getNbv5code())) {
            return ResultBean.error("验证码为空！");
        } else {
            String code = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (code == null) {
                return ResultBean.custom(-1, "请刷新页面");
            }
            if (!code.equalsIgnoreCase(data.getNbv5code())) {
                return ResultBean.error("验证码错误！");
            }
        }
        data.setNbv5pass(SecureUtil.md5(data.getNbv5pass()));
        ResultBean loginResult = simpleLoginService.doLogin(data);
        if (loginResult.get(ResultBean.CODE).equals(ResultBean.SUCCESS)) {
            User nbv5su = (User) loginResult.get("nbv5su");
            setSessionUser(request, nbv5su);
        }
        return loginResult;
    }

    @RequestMapping("/api/qq")
    public String qqLogin() {
        String callbackDomain = basePath(request).concat("api/qqCallback");
        Param qqAppId = paramService.findByName(NBV5.QQ_APP_ID);
        if (qqAppId == null || StringUtils.isEmpty(qqAppId.getValue())) {
            request.getSession().setAttribute("errorMessage", "未设置QQ登录相关参数！");
            //noinspection SpringMVCViewInspection
            return "redirect:/error?errorCode=403";
        } else {
            return String
                    .format("redirect:https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%d"
                            , qqAppId.getValue(), callbackDomain, System.currentTimeMillis());
        }
    }

    @RequestMapping("/api/github")
    public String githubLogin(HttpServletRequest request) {
        String callbackDomain = basePath(request).concat("api/githubCallback");
        Param githubClientId = paramService.findByName(NBV5.GITHUB_CLIENT_ID);
        if (githubClientId == null || StringUtils.isEmpty(githubClientId.getValue())) {
            request.getSession().setAttribute("errorMessage", "未设置GITHUB登录相关参数！");
            //noinspection SpringMVCViewInspection
            return "redirect:/error?errorCode=403";
        } else {
            return String
                    .format("redirect:https://github.com/login/oauth/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s"
                            , githubClientId.getValue(), callbackDomain, NBV5.GITHUB_AUTH_STATE);
        }
    }

    @RequestMapping("/api/{callbackType}")
    public ModelAndView qqCallback(HttpServletRequest request, String code, @PathVariable("callbackType") String callbackType) {
        String qq = "qqCallback", github = "githubCallback";
        ResultBean r;
        if (qq.equals(callbackType)) {
            String callbackDomain = basePath(request).concat("api/qqCallback");
            r = qqLoginService.doLogin(QqLoginData.builder().callbackDomain(callbackDomain).code(code).build());
        } else if (github.equals(callbackType)) {
            String callbackDomain = basePath(request).concat("api/githubCallback");
            r = githubLoginService.doLogin(GithubLoginData.builder().callbackDomain(callbackDomain).code(code).build());
        } else {
            request.setAttribute("message", "暂未支持其他类型的登录！");
            return new ModelAndView(new RedirectView("error?errorCode=404"));
        }
        if (r.get(ResultBean.CODE).equals(ResultBean.SUCCESS)) {
            setSessionUser(request, (User) r.get(NBV5.SESSION_USER_KEY));
            Object tempUrl = request.getSession().getAttribute("tempUrl");
            String toUrl = !StringUtils.isEmpty(tempUrl) ? tempUrl.toString() : r.get(ResultBean.DATA).toString();
            request.getSession().removeAttribute("tempUrl");
            return new ModelAndView(new RedirectView(toUrl));
        } else {
            return new ModelAndView(new RedirectView("/error?errorCode=404"));
        }
    }

    @GetMapping(value = {"/management/logout", "/token/logout"})
    public ModelAndView logout(HttpServletRequest request, String redirectUrl) {
        invalidSessionUser(request);
        if (StringUtils.isEmpty(redirectUrl)) {
            return new ModelAndView(new RedirectView("/login"));
        } else {
            return new ModelAndView(new RedirectView(redirectUrl));
        }
    }

}
