package me.wuwenbin.noteblogv5.service.interfaces;

import com.baomidou.mybatisplus.extension.service.IService;
import me.wuwenbin.noteblogv5.model.entity.User;

/**
 * created by Wuwenbin on 2019-08-14 at 15:29
 *
 * @author wuwenbin
 */
public interface UserService extends IService<User> {

    /**
     * 根据openId查找用户
     *
     * @param openId
     * @param enable
     * @return
     */
    User findByQqOpenId(String openId, Boolean enable);

    /**
     * 查找GitHub用户
     *
     * @param username
     * @param enable
     * @return
     */
    User findByGithub(String username, Boolean enable);

    /**
     * 统计nickname
     *
     * @param nickname
     * @return
     */
    int countNickname(String nickname);

    /**
     * 统计今日用户数量
     * @return
     */
    long findTodayUser();
}
