package com.example.springbootjpa.account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
//슬라이스 테스트
//인메모리 데이터 베이스가 반드시 필요
@DataJpaTest
//@SpringBootTest > 메인클래스의 @SpringBootApplication 을 찾아서 모든 빈설정을 함. 따라서 h2가아닌 mysql로 테스트 수행하게됨. 권장X
//임베디드 방식으로 돌리는게 훨씬 빠르고 안전
public class AccountRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository accountRepository;

    /*
    비어있는 테스트를 만들면 의존성 주입이 잘 동작하는지, 테스트 애플리케이션이 실행되는지 테스트 할수 있음.
    * */
    @Test
    public void di() throws SQLException {
        //트라이로 묶어서 사용해야 자원 반납이 됨.
        //테스트에서 h2를 사용하는것을 확인
//        try(Connection connection = dataSource.getConnection()){
//            DatabaseMetaData metaData = connection.getMetaData();
//            System.out.println(metaData.getURL());
//            System.out.println(metaData.getDriverName());
//            System.out.println(metaData.getUserName());
//        }
         Account account = new Account();
         account.setUsername("gimun");
         account.setPassword("pass");
         //저장
         Account newAccount =  accountRepository.save(account);
         assertThat(newAccount).isNotNull();

        Optional<Account> existingAccount =  accountRepository.findByUsername(newAccount.getUsername());
         assertThat(existingAccount).isNotNull();

        Optional<Account> nonExistingAccount =  accountRepository.findByUsername("nonUser");
         assertThat(nonExistingAccount).isEmpty();
    }
}