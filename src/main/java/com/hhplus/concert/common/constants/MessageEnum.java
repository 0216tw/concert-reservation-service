package com.hhplus.concert.common.constants;

public enum MessageEnum {

    SELECT_OK("조회완료") ,
    RESERVATION_OK("예약완료") ,
    BAD_REQUEST("잘못된 요청입니다. 요청 데이터를 확인해주세요.") ,
    NOT_FOUND("올바르지 않은 경로입니다."),
    METHOD_NOT_ALLOWED("올바르지 않은 HTTP 메서드입니다.") ,
    ALREADY_RESERVED("이미 예약된 좌석입니다.") ,

    IMPOSSIBLE_RESERVATION("예약 불가능한 좌석입니다.") ,

    INVALID_RESERVATION_ID("유효하지 않은 예약번호입니다.") ,
    TOKEN_CREATEION_SUCCESS("토큰 발급에 성공했습니다."),
    TOKEN_CREATION_FAILURE("토큰 발급에 실패했습니다."),

    TOKEN_VALIDATION_FAILURE("유효하지 않은 토큰입니다."),
    INVALID_DATE_FORMAT("올바르지 않은 날짜형식입니다.") ,

    NOT_CHARGE_UNDER_ZERO("0원 이하로 충전할 수 없습니다.") ,

    INSUFFICIENT_BALANCE("잔액이 불충분합니다.") ,

    CHARGE_SUCCESS("충전 완료") ,

    CHARGE_FAILURE("충전 실패") ,
    TOKEN_DUPLICATE("중복된 토큰입니다.") ,
    USER_INSERT_FAILURE("사용자 등록에 실패하였습니다.") ,
    USER_NOT_FOUND("존재하지 않는 사용자입니다"),
    TOKEN_EXPIRED("만료된 토큰입니다. 재발급해주세요.") ,

    TOKEN_STATUS_FAILURE("토큰 상태 조회에 실패했습니다.") ,
    TOKEN_NOW_WAIT_STATUS("현재 작업 대기중입니다.") ,

    PAYMENT_SUCCESS("결제가 완료되었습니다.") ,

    PAYMENT_FAILURE("결제에 실패했습니다.") ,

    RESERVATION_SUCCESS("예약이 완료되었습니다.") ,

    RESERVATION_TIME_EXCEEDED("예약가능 시간이 만료되었습니다.");


    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
