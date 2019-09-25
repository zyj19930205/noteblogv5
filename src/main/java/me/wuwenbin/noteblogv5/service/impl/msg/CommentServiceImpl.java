package me.wuwenbin.noteblogv5.service.impl.msg;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.wuwenbin.noteblogv5.mapper.CommentMapper;
import me.wuwenbin.noteblogv5.model.bo.CommentBo;
import me.wuwenbin.noteblogv5.model.entity.Comment;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuwen
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public IPage<CommentBo> findCommentPage(IPage<CommentBo> page, String nickname, String clearComment, List<String> articleIds, boolean enable) {
        return commentMapper.findCommentPage(page, nickname, clearComment, articleIds, enable);
    }

    @Override
    public CommentBo findLatestComment() {
        return commentMapper.findLatestComment();
    }

    @Override
    public long findTodayComment() {
        return commentMapper.findTodayComment();
    }
}
