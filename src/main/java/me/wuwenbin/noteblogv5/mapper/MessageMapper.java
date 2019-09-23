package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.bo.MessageBo;
import me.wuwenbin.noteblogv5.model.entity.Message;
import org.apache.ibatis.annotations.Param;

/**
 * @author wuwen
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 查询评论分页
     *
     * @param page
     * @param nickname
     * @param clearComment
     * @param enable
     * @return
     */
    IPage<MessageBo> findMessagePage(IPage<MessageBo> page,
                                     @Param("nickname") String nickname,
                                     @Param("clearComment") String clearComment,
                                     @Param("enable") boolean enable);

    /**
     * 查找最新的留言
     *
     * @return
     */
    MessageBo findLatestMessage();

    /**
     * 统计今日留言数量
     *
     * @return
     */
    long findTodayMessage();
}
