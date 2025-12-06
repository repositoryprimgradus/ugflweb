package com.elexyt.ugflweb.cfg;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private  AuthenticationProvider authenticationProvider;



	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf()
				.disable()
				.authorizeHttpRequests()
				.requestMatchers("/authenticate").permitAll()
                .requestMatchers("/api/faq-queries").permitAll()
                .requestMatchers("/api/job-applications").permitAll()
                .requestMatchers("/api/loan-applications").permitAll()
                .requestMatchers("/today-rate").permitAll()
				.requestMatchers("/v2/api-docs","/v3/api-docs/**", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**","/swagger-ui/**").permitAll()
				//.requestMatchers("/registerUser","/sendOtp", "/validateOtp", "/resetPassword", "/event-notice/eventNoticeList", "/event-notice/event-attachment-preview", "/active-member-details", "/uploads/**", "/gallery/all").permitAll()
				.anyRequest()
				.authenticated()
				.and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf().disable()
				.cors().configurationSource(new CorsConfigurationSource() {

					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

						CorsConfiguration cfg=new CorsConfiguration();
						cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000/","http://localhost:5173/","http://72.60.200.193/","https://elexyt2024.site/","http://elexyt2024.site/"));
						//cfg.setAllowedMethods(List.of(List.of("GET", "POST", "PUT", "DELETE", "PATCH").toArray(new String[0])));
                        cfg.setAllowedMethods(Collections.singletonList("*"));
                        cfg.setAllowCredentials(true);
						cfg.setAllowedHeaders(Collections.singletonList("*"));
						cfg.setExposedHeaders(Arrays.asList("Authorization"));
						cfg.setMaxAge(3600L);


						return cfg;
					}
				});

		return http.build();
	}





}
