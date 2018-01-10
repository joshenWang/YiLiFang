package com.njupt.cart.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Title:
 * description:
 * Create Time: 2017/12/24 21:23 星期日
 *
 * @author: WJZ
 **/
public class start_cart {

    @Test
    public void startServer() throws IOException {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
        System.in.read();



    }
}
