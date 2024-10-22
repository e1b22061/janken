package oit.is.z2620.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {
  /**
   * 認可処理に関する設定（認証されたユーザがどこにアクセスできるか）
   *
   * @param http
   * @return
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")) // ログアウト後に / にリダイレクト
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(AntPathRequestMatcher.antMatcher("/janken/**"))
            .authenticated() // /sample3/以下は認証済みであること
            .requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
            .permitAll())// 上記以外は全員アクセス可能
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/*")))// h2-console用にCSRF対策を無効化
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin()));
    return http.build();
  }

  /**
   * 認証処理に関する設定（誰がどのようなロールでログインできるか）
   *
   * @return
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    UserDetails user1 = User.withUsername("user1")
        .password("{bcrypt}$2y$05$93J7nRoI/YS3zT4lKkzY0OCWNH6xApuHXRnBe8IqrFYE1651rgLqC").roles("USER").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2y$05$v/WT6jSLd670edf6nBZ6buNwFv4VH.ac8jRABGfqfR6P70Atcl9tm").roles("USER").build();
    UserDetails user3 = User.withUsername("ほんだ")
        .password("{bcrypt}$2y$05$xaK017RRVqfr1vXqmBPk0Oh4/2NBACmy5E7fBRgOnFYN.W9MrFkoW").roles("USER").build();
    UserDetails user4 = User.withUsername("いがき")
        .password("{bcrypt}$2y$05$xaK017RRVqfr1vXqmBPk0Oh4/2NBACmy5E7fBRgOnFYN.W9MrFkoW").roles("USER").build();

    return new InMemoryUserDetailsManager(user1, user2, user3, user4);
  }

}
