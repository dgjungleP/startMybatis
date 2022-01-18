package com.jungle.start.mybatis;

import org.junit.Test;

import java.sql.Driver;
import java.util.ServiceLoader;

public class SPITest {

    @Test
    public void testSPI() {
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        for (Driver driver : load) {
            System.out.println(driver.getClass().getName());
        }
    }
}
