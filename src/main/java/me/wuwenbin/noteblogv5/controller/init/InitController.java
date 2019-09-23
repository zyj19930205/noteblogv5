package me.wuwenbin.noteblogv5.controller.init;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.entity.Param;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import me.wuwenbin.noteblogv5.util.NbUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wuwen
 */
@Controller
public class InitController extends BaseController {

    private final ParamService paramService;
    private final HttpServletRequest request;

    public InitController(ParamService paramService, HttpServletRequest request) {
        this.paramService = paramService;
        this.request = request;
    }

    @RequestMapping("/init")
    public String initPage() {
        request.setAttribute("uploadPathInEnv",
                NbUtils.getEnvPropertyByKey(NBV5.APP_UPLOAD_PATH, String.class));
        return "init";
    }

    @RequestMapping("/init/submit")
    @ResponseBody
    public ResultBean initSubmit(String username, String password, String email) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email)) {
            return ResultBean.error("用户名/密码/邮箱不能为空！");
        } else {
            paramService.saveInitParam(getParameterMap(request.getParameterMap()));
            paramService.initMasterAccount(username, password, email);
            paramService.update(Param.builder().value(email).build(),
                    Wrappers.<Param>update().eq("name", NBV5.MAIL_SERVER_ACCOUNT));
            paramService.update(Param.builder().value("1").build(),
                    Wrappers.<Param>update().eq("name", NBV5.INIT_STATUS));
            return ResultBean.ok("初始化设置成功！");
        }
    }
}
