package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.AdministratorDto;
import com.shaohsiung.shop.dto.UserDto;
import com.shaohsiung.shop.mapper.AdministratorMapper;
import com.shaohsiung.shop.model.Administrator;
import com.shaohsiung.shop.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdministratorService {
    @Autowired
    private AdministratorMapper administratorMapper;
    /**
     * 会员登录
     * 前台
     * @param adminName
     * @param password
     * @return
     */
    public AdministratorDto login(String adminName, String password) {
        String encodedPassword = AppUtils.sha256Encrypt(password);

        Administrator admin = administratorMapper.findAdministratorByAdminNameAndPassword(adminName, encodedPassword);
        log.info("【管理员模块】管理员登录：{}", admin);

        AdministratorDto adminDto = new AdministratorDto();
        BeanUtils.copyProperties(admin, adminDto);
        return adminDto;
    }
}
