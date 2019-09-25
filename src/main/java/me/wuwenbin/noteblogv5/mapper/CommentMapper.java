package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.bo.CommentBo;
import me.wuwenbin.noteblogv5.model.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuwen
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询评论分页
     *
     * @param page
     * @param nickname
     * @param clearComment
     * @param articleIds
     * @param enable
     * @return
     */
    IPage<CommentBo> findCommentPage(IPage<CommentBo> page,
                                     @Param("nickname") String nickname,
                                     @Param("clearComment") String clearComment,
                                     @Param("articleIds") List<String> articleIds,
                                     @Param("enable") boolean enable);

    /**
     * 查找最新的评论
     *
     * @return
     */
    CommentBo findLatestComment();

    /**
     * 统计今日评论数量
     *
     * @return
     */
    long findTodayComment();
}
