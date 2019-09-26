package me.wuwenbin.noteblogv5.service.interfaces.content;

import com.baomidou.mybatisplus.extension.service.IService;
import me.wuwenbin.noteblogv5.model.entity.Hide;


/**
 * @author wuwen
 */
public interface HideService extends IService<Hide> {

    /**
     * 删除文章相关的关联
     *
     * @param articleId
     */
    void deleteArticlePurchaseRefer(String articleId);

    /**
     * 查询用户是否已经购买了
     *
     * @param articleId
     * @param userId
     * @param hideId
     * @return
     */
    boolean userIsBought(String articleId, long userId, String hideId);

    /**
     * 购买文章隐藏内容
     *
     * @param articleId
     * @param hideId
     * @param userId
     * @return
     */
    int purchaseArticleHideContent(String articleId, String hideId, Long userId);
}
