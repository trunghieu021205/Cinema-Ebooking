package com.cinemaebooking.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode - Centralized error definition for entire system (ENTERPRISE V2 - CLEAN REFACTOR).
 * STRICT RULES (unchanged):
 * 1. ONLY Backend Lead/Maintainer is allowed to add or modify error codes.
 * 2. DO NOT create new error enums inside any domain packages.
 * 3. DO NOT modify existing error code numbers once they are released.
 * 4. ALL exceptions MUST be thrown via corresponding Exception Factories.
 * 5. NEVER throw raw RuntimeException, new BaseException(...) directly,
 *    or use ErrorCode directly in business code.
 * 6. Message in ErrorCode is the default. It can be overridden when throwing.
 * ─────────────────────────────────────────────────────────────────
 * CODE RANGE REGISTRY (V2)
 * ┌──────────────┬──────────────────────────────┬───────────────┬────────────────┐
 * │ Group        │ Domain                       │ Sub-range     │ Block          │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ SYSTEM       │ audit_log                    │ 1001–1099     │ 1000–1099      │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ COMMON       │ common                       │ 1100–1199     │ 1100–1199      │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ CORE         │ user                         │ 2001–2008     │ 2000–2199      │
 * │              │ loyalty                      │ 2009–2019     │                │
 * │              │ (membership_tier,            │               │                │
 * │              │  loyalty_account,            │               │                │
 * │              │  loyalty_earning_rule)       │               │                │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ CATALOG      │ movie                        │ 3001–3004     │ 3000–3399      │
 * │              │ cinema                       │ 3005–3007     │                │
 * │              │ room                         │ 3008–3011     │                │
 * │              │ seat                         │ 3012–3020     │                │
 * │              │ (incl. seat_type)            │               │                │
 * │              │ showtime                     │ 3021–3028     │                │
 * │              │ (incl. showtime_format)      │               │                │
 * │              │ showtime_seat                │ 3029–3031     │                │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ PRODUCT      │ combo                        │ 3401–3404     │ 3400–3599      │
 * │              │ coupon                       │ 3405–3409     │                │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ BOOKING      │ booking                      │ 4001–4009     │ 4000–4499      │
 * │              │ booking_combo                │ 4010–4011     │                │
 * │              │ booking_coupon               │ 4012–4014     │                │
 * │              │ seat_lock                    │ 4015–4018     │                │
 * │              │ ticket                       │ 4019–4022     │                │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ PAYMENT      │ payment                      │ 4501–4509     │ 4500–4699      │
 * │              │ refund                       │ 4510–4513     │                │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ INTERACTION  │ review                       │ 4701–4704     │ 4700–4899      │
 * │              │ notification                 │ 4705–4707     │                │
 * ├──────────────┼──────────────────────────────┼───────────────┼────────────────┤
 * │ SUPPORTING   │ user_coupon                  │ 4901–4904     │ 4900–4999      │
 * └──────────────┴──────────────────────────────┴───────────────┴────────────────┘
 * V2 CLEAN IMPROVEMENTS:
 * • Naming 100% consistent: DOMAIN_ACTION_OBJECT
 * • Messages ngắn gọn, rõ ràng
 * • Added ErrorType: BUSINESS / TECHNICAL
 * • Loại bỏ ambiguity, validation naming thống nhất
 *
 * @author Hieu Nguyen
 * @since 2026 (V2 Clean Refactored)
 */
@Getter
public enum ErrorCode {

    // ===================== SYSTEM =====================
    // audit_log: 1001–1099
    AUDIT_LOG_NOT_FOUND         (1001, "Audit log not found",                          HttpStatus.NOT_FOUND,      ErrorType.TECHNICAL),
    AUDIT_LOG_WRITE_FAILED      (1002, "Failed to write audit log",                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),
    AUDIT_LOG_INVALID_TYPE      (1003, "Invalid audit log type",                       HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // ===================== COMMON =====================
    // common: 1100–1199
    // ⚠️ ONLY use when no domain-specific error exists
    UNCATEGORIZED_EXCEPTION     (1100, "Uncategorized error",                          HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),
    INVALID_INPUT               (1101, "Invalid input",                                HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    INVALID_REQUEST             (1102, "Invalid request",                              HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    UNAUTHORIZED                (1103, "Unauthorized",                                 HttpStatus.UNAUTHORIZED,   ErrorType.BUSINESS),
    FORBIDDEN                   (1104, "Forbidden",                                    HttpStatus.FORBIDDEN,      ErrorType.BUSINESS),
    RESOURCE_NOT_FOUND          (1105, "Resource not found",                           HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    RESOURCE_ALREADY_EXISTS     (1106, "Resource already exists",                      HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    CONCURRENCY_CONFLICT        (1107, "Concurrency conflict, please try again",       HttpStatus.CONFLICT,       ErrorType.BUSINESS),

    // ===================== CORE =====================
    // user: 2001–2008
    USER_NOT_FOUND              (2001, "User not found",                               HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    USER_ALREADY_EXISTS         (2002, "User already exists",                          HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    USER_INVALID_CREDENTIALS    (2003, "Invalid username or password",                 HttpStatus.UNAUTHORIZED,   ErrorType.BUSINESS),
    USER_ACCOUNT_DISABLED       (2004, "User account is disabled",                     HttpStatus.FORBIDDEN,      ErrorType.BUSINESS),
    USER_INVALID_STATUS         (2005, "Invalid user status",                          HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    USER_INVALID_EMAIL          (2006, "Invalid email format",                         HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    USER_INVALID_PASSWORD       (2007, "Password does not meet requirements",          HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    USER_NOT_VERIFIED           (2008, "User account is not verified",                 HttpStatus.FORBIDDEN,      ErrorType.BUSINESS),

    // loyalty: 2009–2019
    LOYALTY_ACCOUNT_NOT_FOUND   (2009, "Loyalty account not found",                    HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    LOYALTY_ACCOUNT_ALREADY_EXISTS (2010, "Loyalty account already exists",            HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    LOYALTY_ACCOUNT_LOCKED      (2011, "Loyalty account is locked",                    HttpStatus.FORBIDDEN,      ErrorType.BUSINESS),
    LOYALTY_INSUFFICIENT_POINTS (2012, "Insufficient loyalty points",                  HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    LOYALTY_POINTS_EXPIRED      (2013, "Loyalty points have expired",                  HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    LOYALTY_TIER_INVALID        (2014, "Invalid loyalty tier",                         HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    LOYALTY_TIER_NOT_ELIGIBLE   (2015, "User is not eligible for this loyalty tier",   HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    LOYALTY_RULE_INVALID        (2016, "Invalid loyalty earning rule",                 HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    LOYALTY_RULE_CONFLICT       (2017, "Loyalty earning rule conflict",                HttpStatus.CONFLICT,       ErrorType.BUSINESS),

    // ===================== CATALOG =====================
    // movie: 3001–3004
    MOVIE_NOT_FOUND             (3001, "Movie not found",                              HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    MOVIE_ALREADY_EXISTS        (3002, "Movie already exists",                         HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    MOVIE_INVALID_STATUS        (3003, "Invalid movie status",                         HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    MOVIE_INVALID_RELEASE_DATE  (3004, "Invalid movie release date",                   HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // cinema: 3005–3007
    CINEMA_NOT_FOUND            (3005, "Cinema not found",                             HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    CINEMA_ALREADY_EXISTS       (3006, "Cinema already exists",                        HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    CINEMA_INVALID_STATUS       (3007, "Invalid cinema status",                        HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // room: 3008–3011
    ROOM_NOT_FOUND              (3008, "Room not found",                               HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    ROOM_ALREADY_EXISTS         (3009, "Room already exists in this cinema",           HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    ROOM_INVALID_STATUS         (3010, "Invalid room status",                          HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    ROOM_INVALID_CAPACITY       (3011, "Invalid room capacity",                        HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // seat: 3012–3020
    SEAT_NOT_FOUND              (3012, "Seat not found",                               HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    SEAT_ALREADY_EXISTS         (3013, "Seat already exists in this room",             HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SEAT_INVALID_STATUS         (3014, "Invalid seat status",                          HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    SEAT_ALREADY_BOOKED         (3015, "Seat already booked",                          HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SEAT_NOT_AVAILABLE          (3016, "Seat not available",                           HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SEAT_ALREADY_SELECTED_BY_USER (3017, "Seat already selected by user",              HttpStatus.CONFLICT,       ErrorType.BUSINESS),

    SEAT_TYPE_NOT_FOUND         (3018, "Seat type not found",                          HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    SEAT_TYPE_ALREADY_EXISTS    (3019, "Seat type already exists",                     HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SEAT_TYPE_INVALID           (3020, "Invalid seat type",                            HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // showtime: 3021–3028
    SHOWTIME_NOT_FOUND          (3021, "Showtime not found",                           HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    SHOWTIME_ALREADY_EXISTS     (3022, "Showtime already exists for this time slot",   HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SHOWTIME_INVALID_STATUS     (3023, "Invalid showtime status",                      HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    SHOWTIME_EXPIRED            (3024, "Showtime has expired",                         HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    SHOWTIME_ROOM_CONFLICT      (3025, "Room already booked for this time slot",       HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SHOWTIME_NOT_BOOKABLE       (3026, "Showtime is not bookable",                     HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    SHOWTIME_FORMAT_NOT_FOUND   (3027, "Showtime format not found",                    HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    SHOWTIME_FORMAT_INVALID     (3028, "Invalid showtime format",                      HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // showtime_seat: 3029–3031
    SHOWTIME_SEAT_NOT_FOUND     (3029, "Showtime seat not found",                      HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    SHOWTIME_SEAT_ALREADY_LOCKED (3030, "Showtime seat already locked",                HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SHOWTIME_SEAT_UNAVAILABLE   (3031, "Showtime seat unavailable",                    HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    // genre: 3032–3033
    GENRE_NOT_FOUND            (3032, "Genre not found",                               HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    GENRE_ALREADY_EXISTS       (3033, "Genre already exists",                          HttpStatus.CONFLICT,       ErrorType.BUSINESS),

    // ===================== PRODUCT =====================
    // combo: 3401–3404
    COMBO_NOT_FOUND             (3401, "Combo not found",                              HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    COMBO_ALREADY_EXISTS        (3402, "Combo already exists",                         HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    COMBO_INVALID_STATUS        (3403, "Invalid combo status",                         HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    COMBO_OUT_OF_STOCK          (3404, "Combo out of stock",                           HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // coupon: 3405–3409
    COUPON_NOT_FOUND            (3405, "Coupon not found",                             HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    COUPON_ALREADY_EXISTS       (3406, "Coupon with this code already exists",         HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    COUPON_EXPIRED              (3407, "Coupon has expired",                           HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    COUPON_INVALID              (3408, "Coupon not valid for this booking",            HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    COUPON_MAX_USAGE_REACHED    (3409, "Coupon has reached maximum usage limit",       HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // ===================== BOOKING =====================
    // booking: 4001–4009
    BOOKING_NOT_FOUND           (4001, "Booking not found",                            HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    BOOKING_ALREADY_EXISTS      (4002, "Booking already exists",                       HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    BOOKING_INVALID_STATUS      (4003, "Invalid booking status",                       HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    BOOKING_CANCELLED           (4004, "Booking has been cancelled",                   HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    BOOKING_EXPIRED             (4005, "Booking session has expired",                  HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    BOOKING_PROCESS_FAILED      (4006, "Booking process failed",                       HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),
    BOOKING_ALREADY_PAID        (4007, "Booking already paid",                         HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    BOOKING_NOT_OWNED_BY_USER   (4008, "Booking does not belong to current user",      HttpStatus.FORBIDDEN,      ErrorType.BUSINESS),
    BOOKING_TIMEOUT             (4009, "Booking has timed out",                        HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // booking_combo: 4010–4011
    BOOKING_COMBO_NOT_FOUND           (4010, "Booking combo not found",                HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    BOOKING_COMBO_INVALID_QUANTITY    (4011, "Invalid booking combo quantity",         HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // booking_coupon: 4012–4014
    BOOKING_COUPON_NOT_FOUND          (4012, "Booking coupon not found",               HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    BOOKING_COUPON_ALREADY_APPLIED    (4013, "Coupon already applied to booking",      HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    BOOKING_COUPON_NOT_APPLICABLE     (4014, "Coupon not applicable for this booking", HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // seat_lock: 4015–4018
    SEAT_LOCK_NOT_FOUND         (4015, "Seat lock not found",                          HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    SEAT_LOCK_EXPIRED           (4016, "Seat lock has expired",                        HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    SEAT_LOCK_CONFLICT          (4017, "Seat already locked by another user",          HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    SEAT_LOCK_FAILED            (4018, "Failed to lock seat",                          HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),

    // ticket: 4019–4022
    TICKET_NOT_FOUND            (4019, "Ticket not found",                             HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    TICKET_ALREADY_USED         (4020, "Ticket already used",                          HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    TICKET_INVALID_STATUS       (4021, "Invalid ticket status",                        HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    TICKET_GENERATION_FAILED    (4022, "Failed to generate ticket",                    HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),

    // ===================== PAYMENT =====================
    // payment: 4501–4509
    PAYMENT_NOT_FOUND           (4501, "Payment not found",                            HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    PAYMENT_ALREADY_EXISTS      (4502, "Payment already exists for this booking",      HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    PAYMENT_FAILED              (4503, "Payment processing failed",                    HttpStatus.BAD_GATEWAY,    ErrorType.TECHNICAL),
    PAYMENT_INVALID_AMOUNT      (4504, "Invalid payment amount",                       HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    PAYMENT_EXPIRED             (4505, "Payment session has expired",                  HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    PAYMENT_INVALID_STATUS      (4506, "Invalid payment status",                       HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    PAYMENT_METHOD_NOT_SUPPORTED(4507, "Payment method not supported",                 HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    PAYMENT_DUPLICATE_REQUEST   (4508, "Duplicate payment request",                    HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    PAYMENT_GATEWAY_TIMEOUT     (4509, "Payment gateway timeout",                      HttpStatus.BAD_GATEWAY,    ErrorType.TECHNICAL),

    // refund: 4510–4513
    REFUND_NOT_FOUND            (4510, "Refund not found",                             HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    REFUND_ALREADY_PROCESSED    (4511, "Refund already processed",                     HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    REFUND_FAILED               (4512, "Refund processing failed",                     HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),
    REFUND_NOT_ELIGIBLE         (4513, "Booking not eligible for refund",              HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // ===================== INTERACTION =====================
    // review: 4701–4704
    REVIEW_NOT_FOUND            (4701, "Review not found",                             HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    REVIEW_ALREADY_EXISTS       (4702, "Review already exists for this booking",       HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    REVIEW_INVALID_RATING       (4703, "Invalid review rating",                        HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    REVIEW_NOT_ELIGIBLE         (4704, "User not eligible to review this movie",       HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // notification: 4705–4707
    NOTIFICATION_NOT_FOUND      (4705, "Notification not found",                       HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    NOTIFICATION_SEND_FAILED    (4706, "Failed to send notification",                  HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TECHNICAL),
    NOTIFICATION_INVALID_TYPE   (4707, "Invalid notification type",                    HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),

    // ===================== SUPPORTING =====================
    // user_coupon: 4901–4904
    USER_COUPON_NOT_FOUND       (4901, "User coupon not found",                        HttpStatus.NOT_FOUND,      ErrorType.BUSINESS),
    USER_COUPON_ALREADY_EXISTS  (4902, "User already has this coupon",                 HttpStatus.CONFLICT,       ErrorType.BUSINESS),
    USER_COUPON_EXPIRED         (4903, "User coupon has expired",                      HttpStatus.BAD_REQUEST,    ErrorType.BUSINESS),
    USER_COUPON_ALREADY_USED    (4904, "User coupon already used",                     HttpStatus.CONFLICT,       ErrorType.BUSINESS);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
    private final ErrorType type;

    ErrorCode(int code, String message, HttpStatus httpStatus, ErrorType type) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.type = type;
    }
}