package me.wuwenbin.noteblogv5.service.interfaces.mail;

import me.wuwenbin.noteblogv5.model.entity.Article;

/**
 * created by Wuwenbin on 2019-01-08 at 22:27
 *
 * @author wuwenbin
 */
public interface MailService {

    /**
     * 发送评论通知邮件
     *
     * @param site
     * @param article
     * @param comment
     */
    void sendNoticeMail(String site, Article article, String comment);

    /**
     * 发送留言通知
     *
     * @param site
     * @param message
     */
    void sendMessageMail(String site, String message);
}
