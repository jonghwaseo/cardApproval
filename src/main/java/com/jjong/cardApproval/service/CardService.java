package com.jjong.cardApproval.service;

import com.jjong.cardApproval.callApi.ExtCallApi;
import com.jjong.cardApproval.exception.BizException;
import com.jjong.cardApproval.model.CardApproval;
import com.jjong.cardApproval.model.CardCancel;
import com.jjong.cardApproval.model.CardInqData;
import com.jjong.cardApproval.repository.CardRepository;
import com.jjong.cardApproval.repository.DupRepository;
import com.jjong.cardApproval.utils.CardUtil;
import com.jjong.cardApproval.utils.RandomUtil;
import com.jjong.cardApproval.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final DupRepository dupRepository;

    @Autowired
    public CardService(CardRepository cardRepository, DupRepository dupRepository) {

        this.cardRepository = cardRepository;
        this.dupRepository = dupRepository;
    }



    /**
     * 카드결재
     */
    public CardApproval savePay(CardApproval cardApproval) throws Exception{

        //TODO:입력값벨리데이션 체크
        if(cardApproval==null || cardApproval.getCardInfo()==null
                || cardApproval.getCardInfo().getCardNo()==null
                || cardApproval.getCardInfo().getCardValid() == null
                || cardApproval.getCardInfo().getCardCvc()==null
        ){
            throw new BizException("카드번호를 확인 하시기 바랍니다.");
        }

        if(cardApproval.getCardInfo().getCardNo().length()<10 || cardApproval.getCardInfo().getCardNo().length()>16){
            throw new BizException("카드번호는 10~16자리 입니다.");
        }
        if(!StringUtil.isNumeric(cardApproval.getCardInfo().getCardNo())){
            throw new BizException("카드번호는 숫자만 입력 가능합니다.");
        }

        if(cardApproval.getCardInfo().getCardValid().length()!=4){
            throw new BizException("카드유효기간을 입력 하시기 바랍니다.");
        }
        if(!StringUtil.isNumeric(cardApproval.getCardInfo().getCardValid())){
            throw new BizException("카드유효기간은 숫자만 입력 가능합니다.");
        }

        if(cardApproval.getCardInfo().getCardCvc().length()!=3){
            throw new BizException("CVC번호를 입력 하시기 바랍니다.");
        }
        if(!StringUtil.isNumeric(cardApproval.getCardInfo().getCardCvc())){
            throw new BizException("CVC번호는 숫자만 입력 가능합니다.");
        }

        if(cardApproval.getPayAmt()<100){
            throw new BizException("최소결재금액은 100원 이상입니다.");
        }

        if(!dupRepository.getCheckValue(cardApproval.getCardInfo().getCardNo())){
            //처리중인 건 없음
        }else{
            throw new BizException("이미 승인중인 카드번호 존재 합니다. 잠시후 다시 시도 하시기 바랍니다.");
        }


        //처리중인 건 저장
        String cardNo = cardApproval.getCardInfo().getCardNo();

        try {
            dupRepository.saveCheckValue(cardNo);

            if(cardApproval.getPayVat()==0 && cardApproval.getPayAmt()>0 && cardApproval.getPayAmt()!=1000){
                //카드결재금액이 1000원일경우는 vat 가 0 일 수 있음
                cardApproval.setPayVat(Math.round(cardApproval.getPayAmt()/11));
            }else if(cardApproval.getPayAmt() > cardApproval.getPayAmt()){
                throw new BizException("부가가치세는 카드결제금액 보다 클 수 없습니다.");
            }


            //관리번호 채번
            String payId = RandomUtil.getManageId();
            cardApproval.setPayId(payId);



            String infMsg =  StringUtil.makeResStr("PAYMENT", payId, cardApproval.getCardInfo(), cardApproval.getCardPlan(), cardApproval.getPayAmt(), cardApproval.getPayVat(), "");

            //데이터저장
            cardRepository.saveInfMsg(payId, infMsg);



            //TODO:카드사API호출
            ExtCallApi extCallApi = new ExtCallApi();
            extCallApi.callAPI(infMsg);

            cardApproval.setCardAprvStat("00");


            //TODO:DB저장
            cardApproval = cardRepository.saveApproval(cardApproval);
            cardApproval.setInfMsg(infMsg);



        }catch (BizException e){
            throw e;
        }catch (Exception e){
            throw new BizException("카드승인중 오류가 발생 했습니다.", e);
        }finally {
            //카드승인 완료시 처리중인건 삭제
            dupRepository.removeCheckValue(cardNo);
        }



        return cardApproval;


    }

    /**
     * 카드취소
     */
    public CardCancel saveCancel(CardCancel cardCancel) throws Exception{

        //TODO:입력값벨리데이션 체크
        if(cardCancel==null || cardCancel.getPayId()==null){
            throw new BizException("기 카드승인 관리번호 미존재");
        }
        if(cardCancel.getCancelAmt()==0){
            throw new BizException("취소처리 금액이 없습니다.");
        }


        if(!dupRepository.getCheckValue(cardCancel.getPayId())){
            //처리중인 건 없음
        }else{
            throw new BizException("이미 처리중인 관리번호가 존재 합니다. 잠시후 다시 시도 하시기 바랍니다.");
        }


        //처리중인 건 저장
        String payId = cardCancel.getPayId();

        try {
            dupRepository.saveCheckValue(payId);

            //기 결재 금액 체크
            CardInqData cardDataPay = cardRepository.inqApprovalData(payId);

            if(cardDataPay==null){
                throw new BizException("기 승인내역이 없습니다. 관리번호를 확인 하시기 바랍니다.");
            }else{
                //기승인 카드금액, 취소된 카드금액, 남은 카드 금액, 취소할 카드금액 체크
                CardInqData cardDataDtl = this.inqCardDtl(payId, false);


                if(cardCancel.getCancelAmt() > cardDataDtl.getValidAmt()){
                    throw new BizException("취소처리가능금액을 초과하여 취소 할 수 없습니다.");
                }

                if(cardCancel.getCancelVat()==0){
                    //취소 vat 가 0 인경우
                    if(cardDataDtl.getAmt() == cardDataDtl.getValidAmt()){
                        //취소가 없는경우

                        if(cardDataDtl.getAmt()  == cardCancel.getCancelAmt()){
                            //전체취소인경우

                            cardCancel.setCancelVat(cardDataDtl.getVat());
                        }else{
                            //부분취소인경우

                            cardCancel.setCancelVat(Math.round(cardCancel.getCancelAmt()/11));
                        }

                    }else{

                        //부분취소 존재시 취소요청 금액/11
                        cardCancel.setCancelVat(Math.round(cardCancel.getCancelAmt()/11));
                    }
                }
            }






            //관리번호 채번
            String cancelId = RandomUtil.getManageId();
            cardCancel.setCancelId(cancelId);



            String infMsg =  StringUtil.makeResStr("CANCEL", cancelId, cardDataPay.getCardInfo(), cardCancel.getCardPlan(), cardCancel.getCancelAmt(), cardCancel.getCancelVat(), payId);


            //데이터저장
            cardRepository.saveInfMsg(cancelId, infMsg);



            //TODO:카드사API호출
            ExtCallApi extCallApi = new ExtCallApi();
            extCallApi.callAPI(infMsg);

            cardCancel.setCardCancelStat("00");


            //TODO:DB저장
            cardCancel = cardRepository.saveCancel(cardCancel);
            cardCancel.setInfMsg(infMsg);




        }catch (BizException e){
            throw  e;
        }catch (Exception e){
            throw  new BizException("카드 취소처리중 에러가 발생 했습니다.");
        }finally {
            //처리완료시 삭제
            dupRepository.removeCheckValue(payId);
        }



        return cardCancel;


    }



    public CardInqData inqCardDtl(String mngId) throws Exception{
        return this.inqCardDtl(mngId, true);    //기본은 마스킹처리

    }

    /**
     * 카드내역조회
     */
    private CardInqData inqCardDtl(String mngId, boolean isMask) throws Exception{

        CardInqData cardData = new CardInqData();

        try{

            //입력값벨리데이션 체크
            if(mngId==null){
                throw new BizException("관리번호 미존재");
            }


            //취소건 조회
            CardInqData cardDataCancel =  cardRepository.inqCancelData(mngId);



            if(cardDataCancel!=null && cardDataCancel.getMngId()!=null){
                //관리번호가 취소관리번호로 조회 해서 취소 데이터가 있을때
                //취소 데이터만 보내주자.
                cardData = cardDataCancel;


            }else{
                //관리번호가 승인관리번호로 조회 한경우
                //해당 승인관리번호로 취소 데이터도 전부 조회

                //승인 데이터 조회
                CardInqData cardDataPay =  cardRepository.inqApprovalData(mngId);

                if(cardDataPay!=null && cardDataPay.getMngId()!=null){


                    //승인내역에서
                    cardData = cardDataPay;

                    //취소내역조회
                    CardInqData cardCancelData = cardRepository.inqCancelDataByPayId(mngId);


                    long validAmt = cardDataPay.getAmt();
                    long validVat = cardDataPay.getVat();


                    long cnlAmt = cardCancelData.getAmt();
                    long cnlVat = cardCancelData.getVat();

                    cardData.setCnlAmt(cnlAmt);
                    cardData.setCnlVat(cnlVat);
                    cardData.setValidAmt(validAmt-cnlAmt);
                    cardData.setCnlVat(validVat-cnlVat);
                }


            }



            if(isMask){
                //마스킹
                cardData.setCardInfo(CardUtil.maskCardInfo(cardData.getCardInfo()));
            }

        }catch (BizException e){
            throw  e;
        }catch (Exception e){
            throw  new BizException("카드 내역 조회 처리중 에러가 발생 했습니다.");
        }

        return cardData;


    }


}
