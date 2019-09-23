package me.wuwenbin.noteblogv5.service.impl.log;

import me.wuwenbin.noteblogv5.mapper.LogMapper;
import me.wuwenbin.noteblogv5.model.bo.EchartsIpBo;
import me.wuwenbin.noteblogv5.model.bo.EchartsUrlBo;
import me.wuwenbin.noteblogv5.service.interfaces.log.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuwen
 */
@Service
@Transactional(readOnly = true)
public class LogServiceImpl implements LogService {

    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public List<EchartsIpBo> ipSummary() {
        return logMapper.ipSummary();
    }

    @Override
    public List<EchartsUrlBo> urlSummary() {
        return logMapper.urlSummary();
    }

    @Override
    public long todayVisit() {
        return logMapper.todayVisit();
    }
}
