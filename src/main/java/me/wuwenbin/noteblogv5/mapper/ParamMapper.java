package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.Param;

/**
 * created by Wuwenbin on 2019-07-23 at 14:55
 *
 * @author wuwenbin
 */
@Mapper
public interface ParamMapper extends BaseMapper<Param> {

    /**
     * 清除表数据
     */
    void truncateParam();

    /**
     * 更新参数
     *
     * @param name
     * @param value
     */
    void updateValueByName(String name, Object value);

}
