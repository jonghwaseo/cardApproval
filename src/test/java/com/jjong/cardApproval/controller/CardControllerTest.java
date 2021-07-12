package com.jjong.cardApproval.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjong.cardApproval.vo.ApprovalVo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CardController.class)
public class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;


    @Test
    public void save_테스트() throws Exception {
        // given(준비)
        ApprovalVo approvalVo = new ApprovalVo();
        approvalVo.setCardNo("1111222233334444");
        approvalVo.setCardCvc("123");
        approvalVo.setCardValid("0925");
        approvalVo.setCardPayPlan("00");
        approvalVo.setPayAmt(100000);


        String content = mapper.writeValueAsString(approvalVo);



        // when(테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/card/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)



        );

        // then (검증)
        resultAction
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }
}
