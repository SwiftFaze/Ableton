package com.example.javaableton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbletonStatBuilderTest extends AbletonStatBuilder {


    @Test
    void getProjectStats() {
        Project project = getProjectStats("C:\\Users\\Rob\\IdeaProjects\\Ableton_stats\\resource\\ableton_projects\\MOOD3");

    }
}
