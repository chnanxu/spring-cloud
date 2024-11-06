package com.chen.utils.result;//package com.chen.utils.result;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Collection;
//
//public class JwtAuthentication implements Authentication {
//
//    private Collection<SimpleGrantedAuthority> authorities;
//    private Object details;
//    private boolean authenticated;
//    private Object principal;
//    private Object credentials;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return credentials;
//    }
//
//    @Override
//    public Object getDetails() {
//        return details;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return principal;
//    }
//
//    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }
//
//    public void setDetails(Object details) {
//        this.details = details;
//    }
//
//    public void setPrincipal(Object principal) {
//        this.principal = principal;
//    }
//
//    public void setCredentials(Object credentials) {
//        this.credentials = credentials;
//    }
//
//    public void setAuthenticated(boolean authenticated){
//        this.authenticated=authenticated;
//    }
//
//    @Override
//    public boolean isAuthenticated() {
//        return authenticated;
//    }
//
//
//
//    @Override
//    public String getName() {
//        return null;
//    }
//}
