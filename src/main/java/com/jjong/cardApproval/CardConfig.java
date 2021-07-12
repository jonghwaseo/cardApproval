package com.jjong.cardApproval;

import com.jjong.cardApproval.repository.CardRepository;
import com.jjong.cardApproval.repository.DupRepository;
import com.jjong.cardApproval.repository.JdbcTemplateCardRepository;
import com.jjong.cardApproval.repository.MemoryDupRepository;
import com.jjong.cardApproval.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CardConfig {

    private DataSource dataSource;

    @Autowired
    public CardConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Bean
    public CardService cardService(){
        return new CardService(cardRepository(), dupRepository());
    }

    @Bean
    public CardRepository cardRepository(){
        return new JdbcTemplateCardRepository(dataSource);
    }

    @Bean
    public DupRepository dupRepository(){
        return new MemoryDupRepository();
    }
}
