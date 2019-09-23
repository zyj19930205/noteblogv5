package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.Dict;

import java.util.List;

/**
 * @author wuwen
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 查询标签使用数到首页标签面板上显示
     *
     * @return
     */
    List<Dict> findTagsTab();
}
