package me.wuwenbin.noteblogv5.service.interfaces.log;


import me.wuwenbin.noteblogv5.model.bo.EchartsIpBo;
import me.wuwenbin.noteblogv5.model.bo.EchartsUrlBo;

import java.util.List;

/**
 * @author wuwen
 */
public interface LogService {

    /**
     * 统计ip
     *
     * @return
     */
    List<EchartsIpBo> ipSummary();

    /**
     * url统计
     *
     * @return
     */
    List<EchartsUrlBo> urlSummary();

    /**
     * 今日访问量
     *
     * @return
     */
    long todayVisit();
}
