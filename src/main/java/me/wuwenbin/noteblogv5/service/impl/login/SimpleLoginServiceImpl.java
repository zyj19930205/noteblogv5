package me.wuwenbin.noteblogv5.service.impl.login;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.constant.RoleEnum;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.bo.login.SimpleLoginData;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.service.interfaces.LoginService;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 普通登录方法
 * created by Wuwenbin on 2018/7/21 at 18:05
 *
 * @author wuwenbin
 */
@Service("simpleLogin")
@Transactional(rollbackFor = Exception.class)
public class SimpleLoginServiceImpl implements LoginService<ResultBean, SimpleLoginData> {

    private final UserService userService;

    public SimpleLoginServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResultBean doLogin(SimpleLoginData data) {
        User user = userService.getOne(Wrappers.<User>query()
                .eq("username", data.getNbv5name())
                .eq("password", data.getNbv5pass())
                .eq("enable", true)
        );
        if (user != null) {
            String redirectUrl =
                    user.getRole() == RoleEnum.ADMIN
                            ? NBV5.MANAGEMENT_INDEX
                            : NBV5.FRONTEND_INDEX;
            return ResultBean.ok("登陆成功！", redirectUrl).put("nbv5su", user);
        } else {
            return ResultBean.error("用户名或密码错误！");
        }

    }


}
