package com.jjong.cardApproval.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjong.cardApproval.vo.ApprovalVo;
import com.jjong.cardApproval.vo.CancelVo;
import com.jjong.cardApproval.vo.CardDataVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CardControllerTest {

	@Autowired MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;


	
	@Test
	void testCase1() throws Exception {

		MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		JsonParser jsonParser = new JacksonJsonParser();


		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
		headers.setContentType(mediaType);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));




		//승인SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
		String payId = "";
		ApprovalVo approvalVo = new ApprovalVo();
		approvalVo.setCardNo("1111222233334444");
		approvalVo.setCardCvc("123");
		approvalVo.setCardValid("0925");
		approvalVo.setCardPayPlan("00");
		approvalVo.setPayAmt(100000);

		String content = mapper.writeValueAsString(approvalVo);


		HttpEntity<String> request = new HttpEntity<String>(content, headers);
		ResponseEntity<String> result = restTemplate.postForEntity( "/card/pay", request, String.class);
		Map<String,Object> parseMap =  jsonParser.parseMap(result.getBody());
		String infMsg = (String)parseMap.get("strData");
		log.info("infMsg승인="+infMsg);
		//승인EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

		payId = (String)parseMap.get("payId");



		//조회SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
		CardDataVo cardDataVo = new CardDataVo();
		cardDataVo.setMngId(payId);
		content = mapper.writeValueAsString(cardDataVo);

		request = new HttpEntity<String>(content, headers);
		result = restTemplate.postForEntity("/card/inq", request, String.class);


		log.info("result조회="+result);
		//조회EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE


		//취소SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
		CancelVo cancelVo = new CancelVo();
		cancelVo.setPayId(payId);
		cancelVo.setCancelAmt(50000);

		content = mapper.writeValueAsString(cancelVo);

		request = new HttpEntity<String>(content, headers);
		result = restTemplate.postForEntity("/card/cancel", request, String.class);

		parseMap =  jsonParser.parseMap(result.getBody());

		log.info("result취소="+result);
		//취소EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

		String cancelId = (String)parseMap.get("cancelId");

		//조회SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
		CardDataVo cardDataVo2 = new CardDataVo();
		cardDataVo2.setMngId(cancelId);
		content = mapper.writeValueAsString(cardDataVo2);

		request = new HttpEntity<String>(content, headers);
		result = restTemplate.postForEntity("/card/inq", request, String.class);

		log.info("result조회="+result);
		//조회EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

	}


	
}
