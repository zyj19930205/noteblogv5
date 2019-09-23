package me.wuwenbin.noteblogv5.config.filter;

import me.wuwenbin.noteblogv5.exception.AppSetException;
import me.wuwenbin.noteblogv5.model.entity.Param;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import me.wuwenbin.noteblogv5.util.NbUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于判断是否已经安装好了笔记博客系统的过滤作用
 * created by Wuwenbin on 2019-08-09 at 10:03
 *
 * @author wuwenbin
 */
public class InitFilter extends HandlerInterceptorAdapter {

    private static final String INIT_SUCCESS = "1";
    private static final String INIT_URL = "/init";
    private ParamService paramService = NbUtils.getBean(ParamService.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Param param = paramService.getInitStatus();
        if (param == null) {
            throw new AppSetException("程序设置初始化异常");
        } else if (!INIT_SUCCESS.equals(param.getValue())) {
            response.sendRedirect(INIT_URL);
            return false;
        } else {
            return true;
        }
    }
}
