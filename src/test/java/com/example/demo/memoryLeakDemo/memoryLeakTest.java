package com.example.demo.memoryLeakDemo;

import com.example.demo.dto.ResponseDTO;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class memoryLeakTest {

    @Test
    public void test1(){
        Stack stack = new Stack();
        push(stack);
        pop(stack);

        while(true){

        }
    }

    private void push(Stack stack){
        ResponseDTO dto = null;

        for (int i = 0; i < 1000000; i++) {
            dto = new ResponseDTO();
            dto.setMesg("Memory Leak" + i);
            dto.setCode(i);
            stack.push(dto);
        }
    }

    private void pop(Stack stack){
        for (int i = 0; i < 1000000; i++) {
            System.out.println(stack.pop());
        }
    }
}
