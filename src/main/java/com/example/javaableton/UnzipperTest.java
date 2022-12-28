package com.example.javaableton;

import org.junit.jupiter.api.Test;

import java.io.IOException;


class UnzipperTest extends Unzipper {

    @Test
    void unzip() throws IOException {
       unzip("hello");
    }


}
