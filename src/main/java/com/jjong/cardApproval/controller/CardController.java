package com.jjong.cardApproval.controller;

import com.jjong.cardApproval.exception.BizException;
import com.jjong.cardApproval.model.CardApproval;
import com.jjong.cardApproval.model.CardCancel;
import com.jjong.cardApproval.model.CardInfo;
import com.jjong.cardApproval.model.CardInqData;
import com.jjong.cardApproval.service.CardService;
import com.jjong.cardApproval.vo.ApprovalVo;
import com.jjong.cardApproval.vo.CancelVo;
import com.jjong.cardApproval.vo.CardDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService){

        this.cardService = cardService;

    }


    @PostMapping(value="/pay")
    public ApprovalVo payCard(@RequestBody @Validated ApprovalVo reqApprovalVo) throws Exception {

        CardApproval cardApproval = new CardApproval();
        
        try {
            //cardApproval.setPayId(reqApprovalVo);
            CardInfo cardInfo = new CardInfo();
            cardInfo.setCardNo(reqApprovalVo.getCardNo());
            cardInfo.setCardCvc(reqApprovalVo.getCardCvc());
            cardInfo.setCardValid(reqApprovalVo.getCardValid());
            cardApproval.setCardInfo(cardInfo);
            cardApproval.setCardPlan(reqApprovalVo.getCardPayPlan());
            //        cardApproval.setCardAprvStat(reqApprovalVo.getCardAprvStat());
            cardApproval.setPayAmt(reqApprovalVo.getPayAmt());
            cardApproval.setPayVat(reqApprovalVo.getPayVat());
            //        cardApproval.setInfMsg(reqApprovalVo.getInfMsg());

            cardApproval = this.cardService.savePay(cardApproval);

            reqApprovalVo.setPayId(cardApproval.getPayId());
            reqApprovalVo.setStrData(cardApproval.getInfMsg());
            
        }catch ( BizException e){
            throw e;
        }catch ( Exception e){
            throw new BizException("???????????? ??????");
        }

        return reqApprovalVo;
    }



    @PostMapping(value="/cancel")
    public CancelVo cancelCard(@RequestBody @Validated CancelVo reqCancelVo) throws Exception {

        CardCancel cardCancel = new CardCancel();

        try{
    //        cardCancel.setCancelId(reqCancelVo.getCancelId());
            cardCancel.setPayId(reqCancelVo.getPayId());
            cardCancel.setCardPlan("00");
    //        cardCancel.setCardCancelStat(reqCancelVo.getCardCancelStat());
            cardCancel.setCancelAmt(reqCancelVo.getCancelAmt());
            cardCancel.setCancelVat(reqCancelVo.getCancelVat());
    
            cardCancel = this.cardService.saveCancel(cardCancel);
    
            reqCancelVo.setCancelId(cardCancel.getCancelId());
            reqCancelVo.setStrData(cardCancel.getInfMsg());

        }catch ( BizException e){
            throw e;
        }catch ( Exception e){
            throw new BizException("???????????? ??????");
        }

        return reqCancelVo;
    }



    @PostMapping(value="/inq")
    public CardDataVo inqCard(@RequestBody @Validated CardDataVo reqCardDataVo) throws Exception {

        try{
            CardInqData cardInqData = this.cardService.inqCardDtl(reqCardDataVo.getMngId());

            reqCardDataVo.setMngId(cardInqData.getMngId());
            reqCardDataVo.setCardInfo(cardInqData.getCardInfo());
            reqCardDataVo.setCardPayCancelType(cardInqData.getCardPayCancelType());
            reqCardDataVo.setCardPayPlan(cardInqData.getCardPayPlan());
            reqCardDataVo.setAmt(cardInqData.getAmt());   //??????(??????)??????
            reqCardDataVo.setVat(cardInqData.getVat());   //??????(??????)vat
            reqCardDataVo.setCnlAmt(cardInqData.getCnlAmt());  //??????????????? ????????? ???????????? ??????
            reqCardDataVo.setCnlVat(cardInqData.getCnlVat());  //??????????????? ????????? ??????vat ??????
            reqCardDataVo.setValidAmt(cardInqData.getValidAmt());  //??????????????? ????????? ??????????????? ????????????
            reqCardDataVo.setValidVat(cardInqData.getValidVat());  //??????????????? ????????? ??????????????? ??????vat

        }catch ( BizException e){
            throw e;
        }catch ( Exception e){
            throw new BizException("???????????? ??????");
        }

        return reqCardDataVo;
    }

}
