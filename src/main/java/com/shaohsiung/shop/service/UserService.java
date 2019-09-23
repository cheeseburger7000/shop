package com.shaohsiung.shop.service;

import com.shaohsiung.shop.api.request.ResultCode;
import com.shaohsiung.shop.dto.UserDto;
import com.shaohsiung.shop.error.ServiceException;
import com.shaohsiung.shop.mapper.UserMapper;
import com.shaohsiung.shop.model.User;
import com.shaohsiung.shop.model.enums.UserStatus;
import com.shaohsiung.shop.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户模块
 */
@Slf4j
@Service
@Transactional
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 会员登录
     * 前台
     * @param userName
     * @param password
     * @return
     */
    public UserDto login(String userName, String password) {
        String encodedPassword = AppUtils.sha256Encrypt(password);

        User user = userMapper.findUserByUserNameAndPassword(userName, encodedPassword);
        log.info("【会员模块】用户登录：{}", user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    /**
     *  会员注册
     *  前台
     * @param user
     * @return
     */
    public UserDto register(User user) {
        user.setStatus(UserStatus.INACTIVATED);
        // TODO 发送用户激活邮件 rabbitmq

        // SHA256加盐加密用户密码
        String encoded = AppUtils.sha256Encrypt(user.getPassword());
        user.setPassword(encoded);

        userMapper.save(user);
        log.info("【会员模块】用户注册：{}", user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    /**
     * 用户列表
     * 后台
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<UserDto> userList(int pageNum, int pageSize) {
        List<UserDto>  result = new ArrayList();

        List<User> userList = userMapper.findAll(new RowBounds(pageNum, pageSize));
        userList.forEach(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            result.add(userDto);
        });

        return result;
    }

    /**
     * 更改用户状态
     * @param userId
     * @param userStatus
     * @return
     */
    public UserDto updateUserStatus(Long userId, UserStatus userStatus) {
        int updated = userMapper.updateUserStatus(userId, userStatus.getCode());
        if (updated == 1) {
            User user = userMapper.getById(userId);
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }
        throw new ServiceException("用户状态更新失败");
    }
}
