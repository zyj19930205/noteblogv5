package me.wuwenbin.noteblogv5.service.impl.mail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.NBV5;
import me.wuwenbin.noteblogv5.constant.RoleEnum;
import me.wuwenbin.noteblogv5.model.entity.Article;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.mail.MailService;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Wuwenbin on 2019-01-08 at 22:27
 *
 * @author wuwenbin
 */
@Service
public class MailServiceImpl implements MailService {

    private final ParamService paramService;
    private final UserService userService;

    @Autowired
    public MailServiceImpl(ParamService paramService, UserService userService) {
        this.paramService = paramService;
        this.userService = userService;
    }

    @Override
    public void sendNoticeMail(String site, Article article, String comment) {
        String host = paramService.findByName(NBV5.MAIL_SMPT_SERVER_ADDR).getValue();
        String port = paramService.findByName(NBV5.MAIL_SMPT_SERVER_PORT).getValue();
        String from = paramService.findByName(NBV5.MAIL_SERVER_ACCOUNT).getValue();
        String user = paramService.findByName(NBV5.MAIL_SENDER_NAME).getValue();
        String pass = paramService.findByName(NBV5.MAIL_SERVER_PASSWORD).getValue();
        if (StrUtil.isNotEmpty(host)
                && StrUtil.isNotEmpty(port)
                && StrUtil.isNotEmpty(from)
                && StrUtil.isNotEmpty(pass)) {
            MailAccount account = new MailAccount();
            account.setHost(host);
            account.setPort(Integer.valueOf(port));
            account.setAuth(true);
            account.setSslEnable(true);
            account.setFrom(from);
            account.setUser(user);
            account.setPass(pass);

            User admin = userService.getOne(Wrappers.<User>query().eq("role", RoleEnum.ADMIN));
            String targetMail = admin.getEmail();

            String subject = "你的文章 - 【{}】 有人发表评论了";
            String content = "<p>" +
                    "您发布的文章<span style='color:red;font-weight:bolder;'>「{}」</span>" +
                    "有人发表了新评论：</p>" +
                    "<p style='font-style:italic;'>{}</p>" +
                    "<p>，请<a href='{}article/{}' target='_blank'>查看</a></p>";
            MailUtil.send(account,
                    CollUtil.newArrayList(targetMail),
                    StrUtil.format(subject, article.getTitle()),
                    StrUtil.format(content, article.getTitle(), comment, site, article.getId()),
                    true);
        }
    }

    @Override
    public void sendMessageMail(String site, String message) {
        String host = paramService.findByName(NBV5.MAIL_SMPT_SERVER_ADDR).getValue();
        String port = paramService.findByName(NBV5.MAIL_SMPT_SERVER_PORT).getValue();
        String from = paramService.findByName(NBV5.MAIL_SERVER_ACCOUNT).getValue();
        String user = paramService.findByName(NBV5.MAIL_SENDER_NAME).getValue();
        String pass = paramService.findByName(NBV5.MAIL_SERVER_PASSWORD).getValue();
        if (StrUtil.isNotEmpty(host)
                && StrUtil.isNotEmpty(port)
                && StrUtil.isNotEmpty(from)
                && StrUtil.isNotEmpty(pass)) {
            MailAccount account = new MailAccount();
            account.setHost(host);
            account.setPort(Integer.valueOf(port));
            account.setAuth(true);
            account.setSslEnable(true);
            account.setFrom(from);
            account.setUser(user);
            account.setPass(pass);

            User admin = userService.getOne(Wrappers.<User>query().eq("role", RoleEnum.ADMIN));
            String targetMail = admin.getEmail();

            String subject = "你的网站 - 【{}】 有人发表新留言了，快去查看吧！";
            String content = "<p>" +
                    "你的博客网站<span style='color:red;font-weight:bolder;'>【{}】 </span>" +
                    "有人发表了新留言：</p>" +
                    "<p style='font-style:italic;'>{}</p>" +
                    "<p>，请<a href='{}message' target='_blank'>查看</a></p>";

            String websiteTitle = paramService.findByName("website_title").getValue();
            MailUtil.send(account,
                    CollUtil.newArrayList(targetMail),
                    StrUtil.format(subject, websiteTitle),
                    StrUtil.format(content, websiteTitle, message, site),
                    true);
        }
    }
}
