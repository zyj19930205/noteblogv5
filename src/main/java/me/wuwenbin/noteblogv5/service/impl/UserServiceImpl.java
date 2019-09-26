package me.wuwenbin.noteblogv5.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.wuwenbin.noteblogv5.constant.RoleEnum;
import me.wuwenbin.noteblogv5.mapper.UserMapper;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * created by Wuwenbin on 2019-08-14 at 15:31
 *
 * @author wuwenbin
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User findByQqOpenId(String openId, Boolean enable) {
        return super.getOne(Wrappers.<User>query()
                .eq("open_id", openId)
                .eq("role", RoleEnum.QQ_USER.getValue())
                .eq("enable", enable), true);
    }

    @Override
    public int countNickname(String nickname) {
        return super.count(Wrappers.<User>query().eq("nickname", nickname));
    }

    @Override
    public User findByGithub(String username, Boolean enable) {
        return super.getOne(Wrappers.<User>query()
                .eq("username", username)
                .eq("role", RoleEnum.GITHUB_USER.getValue())
                .eq("enable", enable), true);
    }

    @Override
    public long findTodayUser() {
        return userMapper.findTodayUser();
    }

    @Override
    public int countEmailAndUsername(String email, String username) {
        return userMapper.countRegUserEmailAndUsername(email, username);
    }

    @Override
    public int userRegister(String username, String password, String email, String nickname) {
        User regUser = User.builder()
                .avatar("/static/assets/img/noavatar.png")
                .nickname(nickname)
                .createDate(new Date())
                .enable(true)
                .username(username)
                .email(email)
                .password(SecureUtil.md5(password))
                .role(RoleEnum.REG_USER)
                .build();
        return userMapper.insert(regUser);
    }
}
