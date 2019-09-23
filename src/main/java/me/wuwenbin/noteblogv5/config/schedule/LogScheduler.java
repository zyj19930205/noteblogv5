package me.wuwenbin.noteblogv5.config.schedule;

import lombok.extern.slf4j.Slf4j;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.mapper.LogMapper;
import me.wuwenbin.noteblogv5.model.entity.Log;
import me.wuwenbin.noteblogv5.util.NbUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * @author wuwen
 */
@Slf4j
@Component
public class LogScheduler {

    private LogMapper logMapper = NbUtils.getBean(LogMapper.class);
    private ServletContext nbServletContext = NbUtils.getServletContext();

    @Scheduled(cron = "0 0,10,20,30,40,50 * * * ? ")
    public void logInsert() {
        //noinspection unchecked
        List<Log> cacheLogs = (List<Log>) nbServletContext.getAttribute(NBV5.LOG_CACHE_KEY);
        if (cacheLogs != null && !cacheLogs.isEmpty()) {
            for (Log cacheLog : cacheLogs) {
                logMapper.insert(cacheLog);
            }
            nbServletContext.removeAttribute(NBV5.LOG_CACHE_KEY);
        }
    }
}
