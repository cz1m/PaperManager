package com.like4u.papermanager;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("junit5测试类")
public class Junit6Test {
    @DisplayName("测试简单断言")
    @Test
    void testJunit(){
        Integer add = add(6, 4);
        assertEquals(10,add);
        Object ob1=new Object();
        Object ob2=new Object();
       // assertSame(ob1,ob2,"不是同一个对象");
        assertAll("test",
                ()->assertEquals(10,add),
                ()->assertNotSame(ob1,ob2));

/*        assertAll("test2", new Executable() {
            @Override
            public void execute() throws Throwable {
                assertEquals(10, add);
            }
        }, new Executable() {
            @Override
            public void execute() throws Throwable {
                assertNotSame(ob1,ob2);
            }
        });*/
    }

    @BeforeEach
    void testBefore(){
        System.out.println("方法开始测试");
    }
    @AfterEach
    void testAfter(){
        System.out.println("方法测试结束");
    }

    Integer add(int i,int j){

        return i+j;
    }

    @Test
    @DisplayName("测试前置条件")
    void testAssu(){
        Assumptions.assumeFalse(true,"期待false返回true");
        System.out.println("1111111");
    }

}
