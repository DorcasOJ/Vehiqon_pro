package com.vehiqon.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    private final AuditInterceptor auditInterceptor;

//    public WebConfig(AuditInterceptor auditInterceptor, UserRepository userRepository) {
//        this.auditInterceptor = auditInterceptor;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(auditInterceptor)
//                .addPathPatterns("/**");
//    }

    //    public UserDetailsService getUserEntity() {
//        return new UserDetailsService() {
//            @Override
//            pub//lic UserDetails loadUserByUsername(@Nullable String username) throws UsernameNotFoundException {
//                return userRepository.findByEmail(username)
//                        .orElseThrow(() -> new UsernameNotFoundException(""));
//            }
//        };
//    }
}
