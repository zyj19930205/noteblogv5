package me.wuwenbin.noteblogv5.service.interfaces.msg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.wuwenbin.noteblogv5.model.bo.CommentBo;
import me.wuwenbin.noteblogv5.model.entity.Comment;

import java.util.List;

/**
 * @author wuwen
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查找评论页面
     *
     * @param page
     * @param nickname
     * @param clearComment
     * @param articleIds
     * @param enable
     * @return
     */
    IPage<CommentBo> findCommentPage(IPage<CommentBo> page, String nickname, String clearComment, List<Long> articleIds, boolean enable);

    /**
     * 查找最新的评论
     *
     * @return
     */
    CommentBo findLatestComment();

    /**
     * 统计今日评论数量
     * @return
     */
    long findTodayComment();
}
