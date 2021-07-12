package com.jjong.cardApproval.repository;

import com.jjong.cardApproval.exception.BizException;
import com.jjong.cardApproval.model.CardApproval;
import com.jjong.cardApproval.model.CardCancel;
import com.jjong.cardApproval.model.CardInfo;
import com.jjong.cardApproval.model.CardInqData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
public class JdbcTemplateCardRepository implements CardRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateCardRepository(DataSource dataSource){
        this.jdbcTemplate  = new JdbcTemplate(dataSource);
    }



    @Override
    public CardInqData inqApprovalData(String mngId) throws Exception {

        CardInqData cardData = new CardInqData();
        try {

            List<CardInqData> result = jdbcTemplate.query("select * from CARD_PAYMENT  where 1=1" +
                    " and PAY_ID  = ?", inqApprovalDataRowMapper(), mngId);

            if (result.size() > 0) {
                cardData = result.get(0);
            }
        }catch (Exception e){
            log.error("카드승인데이터 조회 중 에러");
            throw new BizException("카드승인데이터 조회 중 에러", e);
        }

        return cardData;

    }

    @Override
    public CardInqData inqCancelData(String mngId) throws Exception {

            CardInqData cardData = new CardInqData();

        try {

            List<CardInqData> result = jdbcTemplate.query("select * from CARD_CANCEL   where 1=1" +
                    " and CANCEL_ID   = ?", inqCancelDataRowMapper(), mngId);


            if (result.size() > 0) {
                cardData = result.get(0);
            }
        }catch (Exception e){
            log.error("카드취소데이터 조회 중 에러");
            throw new BizException("카드취소데이터 조회 중 에러", e);
        }

        return cardData;

    }


    @Override
    public CardApproval saveApproval(CardApproval cardApproval) throws Exception  {

        try{
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("CARD_PAYMENT");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("PAY_ID", cardApproval.getPayId());
            parameters.put("CARD_NO", cardApproval.getCardInfo().getCardNo());
            parameters.put("CARD_VALID", cardApproval.getCardInfo().getCardValid());
            parameters.put("CARD_CVC", cardApproval.getCardInfo().getCardCvc());
            parameters.put("CARD_PAY_PLAN", cardApproval.getCardPlan());
            parameters.put("CARD_APRV_STAT", cardApproval.getCardAprvStat());
            parameters.put("PAY_AMT", cardApproval.getPayAmt());
            parameters.put("PAY_VAT", cardApproval.getPayVat());
            jdbcInsert.execute(parameters);
        }catch (Exception e){
            log.error("카드승인데이터 저장 중 에러");
            throw new BizException("카드승인데이터 저장 중 에러", e);
        }

        return cardApproval;
    }


    @Override
    public CardCancel saveCancel(CardCancel cardCancel) throws Exception  {

        try{
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("CARD_CANCEL");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CANCEL_ID", cardCancel.getCancelId());
            parameters.put("PAY_ID", cardCancel.getPayId());
            parameters.put("PAY_PLAN", cardCancel.getCardPlan());
            parameters.put("CARD_CANCEL_STAT", cardCancel.getCardCancelStat());
            parameters.put("CANCEL_AMT", cardCancel.getCancelAmt());
            parameters.put("CANCEL_VAT", cardCancel.getCancelVat());
            jdbcInsert.execute(parameters);
        }catch (Exception e){
            log.error("카드취소데이터 저장 중 에러");
            throw new BizException("카드취소데이터 저장 중 에러", e);
        }

        return cardCancel;
    }




    @Override
    public CardInqData inqCancelDataByPayId(String payId) throws Exception{

        CardInqData cardData = new CardInqData();

        try{

            List<CardInqData> result = jdbcTemplate.query("select max(CANCEL_ID) CANCEL_ID, max(PAY_PLAN) PAY_PLAN, sum(CANCEL_AMT ) CANCEL_AMT , sum(CANCEL_VAT ) CANCEL_VAT\n" +
                    "        from CARD_CANCEL\n" +
                    "        where 1=1\n" +
                    "        and PAY_ID = ?", inqCancelDataRowMapper(), payId);

            if(result.size()>0){
                cardData = result.get(0);
            }
        }catch (Exception e){
            log.error("카드데이터 조회 중 에러");
            throw new BizException("카드데이터 조회 중 에러", e);
        }

        return cardData;

    }



    public String saveInfMsg(String mngId, String infMsg) throws Exception{

        try{
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName("CARD_TRANS");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("MNG_ID", mngId);
            parameters.put("INF_STR", infMsg);
            jdbcInsert.execute(parameters);
        }catch (Exception e){
            log.error("거래데이터 저장 중 에러");
            throw new BizException("거래데이터 저장 중 에러", e);
        }

        return mngId;
    }


    private RowMapper<CardInqData> inqApprovalDataRowMapper() {
        return (rs, rowNum) -> {
            CardInqData cardInqData = new CardInqData();
            CardInfo cardInfo = new CardInfo();

            cardInqData.setMngId(rs.getString("PAY_ID"));

            cardInfo.setCardNo(rs.getString("CARD_NO"));
            cardInfo.setCardValid(rs.getString("CARD_VALID"));
            cardInfo.setCardCvc(rs.getString("CARD_CVC"));
            cardInqData.setCardInfo(cardInfo);

            cardInqData.setCardPayCancelType("PAY");
            cardInqData.setCardPayPlan(rs.getString("CARD_PAY_PLAN"));
            cardInqData.setAmt(rs.getLong("PAY_AMT"));
            cardInqData.setVat(rs.getLong("PAY_VAT"));


            return cardInqData;
        };
    }

    private RowMapper<CardInqData> inqCancelDataRowMapper() {
        return (rs, rowNum) -> {
            CardInqData cardInqData = new CardInqData();

            cardInqData.setMngId(rs.getString("CANCEL_ID"));
            cardInqData.setCardPayCancelType("CANCEL");
            cardInqData.setCardPayPlan(rs.getString("PAY_PLAN"));
            cardInqData.setAmt(rs.getLong("CANCEL_AMT"));
            cardInqData.setVat(rs.getLong("CANCEL_VAT"));

            return cardInqData;
        };
    }




}
