package com.shaohsiung.shop.service;

import com.shaohsiung.shop.dto.AdministratorDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdministratorServiceTest {
    @Autowired
    private AdministratorService administratorService;

    @Test
    public void login() {
        AdministratorDto admin = administratorService.login("root", "root");
    }
}