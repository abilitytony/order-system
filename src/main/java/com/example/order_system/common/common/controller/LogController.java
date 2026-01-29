package com.example.order_system.common.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//logback 객체를 만드는 방법2.
@Slf4j
@RestController
public class LogController {

//    logback 객체를 만드는 방법1.
    private static Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/log/test")
    public String logTest() {

//        system println의 문제점
//        1) 출력의 성능이 떨어짐 2) 로그 분류 작업 불가
//        System.out.println("hello world");

//        가장 많이 사용되는 로그 라이브러리 : logback
//        logger.trace("trace 로그 입니다.");
//        logger.debug("debug 로그 입니다.");
//        logger.info("info 로그 입니다.");
//        logger.error("error 로그 입니다.");


//        slf4j 어노테이션을 선언 시, log라는 변수로 객체 사용 가능.
//        log.debug("slf4j 테스트");
//        log.info("slf4j 테스트");
        try {
            log.info("에러 테스트 시작");
            throw new IllegalArgumentException("에러 테스트");
        } catch (IllegalArgumentException e) {
//            e.printStackTrace(); // 성능 문제로 logback 쓰는 게 더 좋음.
            log.error("에러메시지 : ", e);
        }
        return "OK";
        }

}
