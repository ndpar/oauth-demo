package com.ndpar;

import org.junit.Test;

import java.util.Base64;

public class Base64Test {

    @Test
    public void encode() {
        System.out.println(new String(Base64.getEncoder().encode("027aac3a-26d0-4c6f-8c08-e61f297beae1".getBytes())));
    }
}
