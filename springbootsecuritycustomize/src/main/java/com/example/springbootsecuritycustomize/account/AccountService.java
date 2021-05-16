package com.example.springbootsecuritycustomize.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account creatAccount(String username, String password){
        Account account = new Account();
        account.setUsername(username);
//        account.setPassword(password);//이렇게 하면 시큐리티 보안 이슈에 걸림. 인코딩 필수
        account.setPassword(passwordEncoder.encode(password));
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> byUsername = accountRepository.findByUsername(username);
//        byUsername에 실제 데이터가 없으면 UsernameNotFoundException 예외를 던짐
//        있을경우는 리턴값으로 account가 나옴
        Account account = byUsername.orElseThrow(() -> new UsernameNotFoundException(username));
//       서비스마다 제각각으로 구현되어있는 유저정보(여기선 account)를 UserDetails 라는 인터페이스의 구현체를 리턴해야함.
//       스프링 시큐리티에서는 User라는 이름으로 기본 제공해줌.
//       따라서 username,password,authorities를 인자료 User 구현체 리턴
//        우리가 가진 account 정보를 UserDetails로 변환하는 과정.
        return new User(account.getUsername(), account.getPassword(), authorities());
    }

    private Collection<? extends GrantedAuthority> authorities() {
//        ROLE_USER라는 권한을 가진 유저라는 것을 셋팅.
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER "));
    }

}
