package me.wuwenbin.noteblogv5.initialization;

import cn.hutool.setting.Setting;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.exception.AppSetException;
import me.wuwenbin.noteblogv5.mapper.ParamMapper;
import me.wuwenbin.noteblogv5.model.entity.Param;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * created by Wuwenbin on 2019-07-29 at 15:27
 *
 * @author wuwenbin
 */
@Slf4j
@Component
@Order(1)
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ParamMapper paramMapper;
    private final Environment environment;

    public InitListener(ParamMapper paramMapper, Environment environment) {
        this.paramMapper = paramMapper;
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationReadyEvent) {
        boolean appInstalled = environment.getProperty("app.installed", Boolean.class, false);
        Param initStatusParam = paramMapper.selectOne(Wrappers.<Param>query().eq("name", NBV5.INIT_STATUS));
        if (!appInstalled && initStatusParam == null) {
            log.info("开始设置参数表");
            paramMapper.truncateParam();
            List<Param> params = getParams();
            for (Param param : params) {
                paramMapper.insert(param);
            }
            setUpSystemInitTime();
            log.info("设置参数表完成");
        }
        setUploadPath();
        setUpSystemStartedTime();
    }

    private List<Param> getParams() {
        List<Param> params = new ArrayList<>();
        int group = -1, groupMax = 3;
        while (group < groupMax) {
            String groupStr = String.valueOf(group);
            Map<String, String> paramsMap = new Setting("params.setting").getMap(groupStr);
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                String[] remarkAndDefault = mapValue.split(",");
                String remark = remarkAndDefault[0];
                String defaultValue = remarkAndDefault.length > 1 ? remarkAndDefault[1] : null;
                Param param = Param.builder()
                        .name(mapKey).group(groupStr).remark(remark).value(defaultValue).orderIndex(0)
                        .build();
                params.add(param);
            }
            group++;
        }
        return params;
    }

    private void setUploadPath() {
        log.info("开始设置上传文件夹");
        String path = environment.getProperty(NBV5.APP_UPLOAD_PATH);
        if (!StringUtils.isEmpty(path)) {
            log.info("「笔记博客」文件上传目录设置为：「{}」", path);
            path = path.replace("file:", "");
            File filePath = new File(path + "file/");
            File imgPath = new File(path + "img/");
            boolean f = false, i = false;
            if (!filePath.exists() && !filePath.isDirectory()) {
                f = filePath.mkdirs();
            }
            if (!imgPath.exists() && !imgPath.isDirectory()) {
                i = imgPath.mkdirs();
            }
            if (f && i) {
                log.info("「笔记博客」创建上传目录成功：「{}」和「{}」", path + "file/", path + "img/");
            } else if (f) {
                log.info("「笔记博客」创建上传目录失败，原因：「{}」，失败目录：「{}」 ", "已存在文件夹或文件夹创建失败", (path + "img/"));
            } else if (i) {
                log.info("「笔记博客」创建上传目录失败，原因：「{}」，失败目录：「{}」 ", "已存在文件夹或文件夹创建失败", (path + "file/"));
            } else {
                log.info("「笔记博客」创建上传目录失败，原因：「{}」，失败目录：「{}」和「{}」 ", "已存在文件夹或文件夹创建失败", (path + "img/"), (path + "file/"));
            }
        } else {
            log.error("上传路径未正确设置");
            throw new AppSetException("上传路径未正确设置，原因 --> 文件「application-noteblogv5.properties」中属性「app.upload.path」未设置或设置有误！");
        }
        log.info("设置文件夹成功");
    }

    private void setUpSystemStartedTime() {
        LocalDateTime now = LocalDateTime.now();
        paramMapper.updateValueByName("system_started_datetime",
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void setUpSystemInitTime() {
        LocalDateTime now = LocalDateTime.now();
        paramMapper.updateValueByName("system_init_datetime",
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
