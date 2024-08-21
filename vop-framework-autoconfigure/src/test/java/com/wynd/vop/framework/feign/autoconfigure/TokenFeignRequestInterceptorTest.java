package com.wynd.vop.framework.feign.autoconfigure;

import feign.RequestTemplate;
import com.wynd.vop.framework.security.jwt.JwtTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TokenFeignRequestInterceptorTest {

    @Mock
    JwtTokenService tokenService = new JwtTokenService(){
        public Map<String, String> getTokenFromRequest() {
            return new HashMap<String, String>();
        }
    };

    @InjectMocks
    TokenFeignRequestInterceptor tokenFeignRequestInterceptor;

    @Test
    public void testApply(){

        RequestTemplate template = new RequestTemplate();
        Map<String, String> map = new HashMap<String, String>();
        map.put("TestHeader", "TestToken");
        when(tokenService.getTokenFromRequest()).thenReturn(map);
        tokenFeignRequestInterceptor.apply(template);
       assertTrue(template.headers().size() == 1);
    }

    @Test
    public void testApplyNoHeader(){

        RequestTemplate template = new RequestTemplate();
        Map<String, String> map = new HashMap<String, String>();
        when(tokenService.getTokenFromRequest()).thenReturn(map);
        tokenFeignRequestInterceptor.apply(template);
        assertTrue(template.headers().size() == 0);
    }

}
