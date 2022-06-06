package com.bigdeal.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;

import com.bigdeal.service.FacebookConnectionSignup;
import com.bigdeal.service.FacebookSignInAdapter;
import com.bigdeal.service.UserDetailsServiceImpl;

@Order(1)
@Configuration
@EnableWebSecurity
public class UserWebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Sét đặt dịch vụ để tìm kiếm User trong Database.
		// Và sét đặt PasswordEncoder.
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		// Các yêu cầu phải login với vai trò ROLE_EMPLOYEE hoặc ROLE_MANAGER.
		// Nếu chưa login, nó sẽ redirect tới trang /admin/login.
		http.antMatcher("/user/**").authorizeRequests()//
				.anyRequest().hasRole("USER");

		// Các trang chỉ dành cho MANAGER
		// Khi người dùng đã login, với vai trò XX.
		// Nhưng truy cập vào trang yêu cầu vai trò YY,
		// Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/usr/403");

		// Cấu hình cho Login Form.
		http.authorizeRequests().and().formLogin()//
				.loginProcessingUrl("/usr/j_spring_security_check") // Submit URL
				.loginPage("/usr/login")//
				.defaultSuccessUrl("/")//
				.failureUrl("/usr/login?error=true")//
				.usernameParameter("userName")//
				.passwordParameter("password")
				// Cấu hình cho trang Logout.
				// (Sau khi logout, chuyển tới trang home)
				.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true);

	}

//	@Bean
//	// @Primary
//	public ProviderSignInController providerSignInController() {
//		JdbcUsersConnectionRepository usersConnectionRepository = new JdbcUsersConnectionRepository(dataSource,
//				connectionFactoryLocator, Encryptors.noOpText());
//		usersConnectionRepository.setConnectionSignUp(facebookConnectionSignup);
//		return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository,
//				new FacebookSignInAdapter());
//	}
}