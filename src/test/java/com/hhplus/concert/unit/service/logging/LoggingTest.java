package com.hhplus.concert.unit.service.logging;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

    @Test
    public void 로깅_적용_테스트() {

        logger.error("개발자가 의도하지 않은 심각한 에러");
        logger.warn("에러는 아니지만 주의가 필요한 내용");
        logger.info("운영 참고할만한 내용, 추후 분석할 자료 등");
        logger.debug("디버깅 메세지 - 개발용으로 사용");
        logger.trace("모든 레벨 로깅");
    }
}
