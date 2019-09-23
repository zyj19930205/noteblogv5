package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.User;

/**
 * created by Wuwenbin on 2019-08-05 at 13:37
 *
 * @author wuwenbin
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 今日新增用户
     *
     * @return
     */
    long findTodayUser();
}
