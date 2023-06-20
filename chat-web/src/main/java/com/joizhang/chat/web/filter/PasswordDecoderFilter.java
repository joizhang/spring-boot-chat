package com.joizhang.chat.web.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.web.config.ChatConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Deprecated
@Slf4j
@RequiredArgsConstructor
public class PasswordDecoderFilter extends OncePerRequestFilter {

    private static final String PASSWORD = "password";

    private static final String KEY_ALGORITHM = "AES";

    private final ChatConfigProperties chatConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 不是登录请求，直接向下执行
        boolean isAuthToken = StrUtil.containsAnyIgnoreCase(request.getRequestURI(),
                SecurityConstants.OAUTH_TOKEN_URL);
        if (!isAuthToken) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 刷新token类型，直接向下执行
        String grantType = request.getParameter("grant_type");
        if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 前端加密密文解密逻辑
        // 4. 解密生成新的报文
        String body = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
        String modifiedBody = decryptAES(body);

        HttpServletRequest newRequest = new BodyCachingHttpServletRequest(request, modifiedBody);
        filterChain.doFilter(newRequest, response);
    }

    private String decryptAES(String s) {
        // 构建前端对应解密AES 因子
        AES aes = new AES(Mode.CFB, Padding.NoPadding,
                new SecretKeySpec(chatConfig.getEncodeKey().getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(chatConfig.getEncodeKey().getBytes()));
        // 获取请求密码并解密
        Map<String, String> inParamsMap = HttpUtil.decodeParamMap(s, CharsetUtil.CHARSET_UTF_8);
        if (inParamsMap.containsKey(PASSWORD)) {
            String password = aes.decryptStr(inParamsMap.get(PASSWORD));
            // 返回修改后报文字符
            inParamsMap.put(PASSWORD, password);
        } else {
            log.error("非法请求数据:{}", s);
        }
        return HttpUtil.toParams(inParamsMap, Charset.defaultCharset(), true);
    }

    private static class BodyCachingHttpServletRequest extends HttpServletRequestWrapper {
        private final String body;

        public BodyCachingHttpServletRequest(HttpServletRequest request, String body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes());

            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return bis.read();
                }
            };
        }

        @Override
        public int getContentLength() {
            return body.getBytes().length;
        }

        @Override
        public long getContentLengthLong() {
            return body.getBytes().length;
        }
    }
}
