package ir.maktabsharif.homeservicephase2.exception.global;

import ir.maktabsharif.homeservicephase2.dto.response.ProjectResponse;
import ir.maktabsharif.homeservicephase2.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AddressFormatException.class)
    ResponseEntity<ProjectResponse> AddressFormatHandler(AddressFormatException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("01", e.getMessage()));
    }

    @ExceptionHandler(AlphabetException.class)
    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("02", e.getMessage()));
    }

    @ExceptionHandler(AmountLessExseption.class)
    ResponseEntity<ProjectResponse> AmountLessHandler(AmountLessExseption e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("03", e.getMessage()));
    }

    @ExceptionHandler(CaptchaException.class)
    ResponseEntity<ProjectResponse> CaptchaHandler(CaptchaException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("04", e.getMessage()));
    }

    @ExceptionHandler(ClientNotExistException.class)
    ResponseEntity<ProjectResponse> ClientNotExistHandler(ClientNotExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("05", e.getMessage()));
    }

    @ExceptionHandler(ClientStatusException.class)
    ResponseEntity<ProjectResponse> ClientStatusHandler(ClientStatusException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("06", e.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    ResponseEntity<ProjectResponse> DuplicateEmailHandler(DuplicateEmailException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("07", e.getMessage()));
    }

    @ExceptionHandler(DuplicatePasswordException.class)
    ResponseEntity<ProjectResponse> DuplicatePasswordHandler(DuplicatePasswordException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("08", e.getMessage()));
    }

    @ExceptionHandler(EmailFormatException.class)
    ResponseEntity<ProjectResponse> EmailFormatHandler(EmailFormatException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("09", e.getMessage()));
    }

    @ExceptionHandler(ImageFormatException.class)
    ResponseEntity<ProjectResponse> ImageFormatHandler(ImageFormatException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("10", e.getMessage()));
    }

    @ExceptionHandler(ImageSizeException.class)
    ResponseEntity<ProjectResponse> ImageSizeHandler(ImageSizeException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("11", e.getMessage()));
    }

    @ExceptionHandler(JobIsExistException.class)
    ResponseEntity<ProjectResponse> JobIsExistHandler(JobIsExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("12", e.getMessage()));
    }

    @ExceptionHandler(JobIsNotExistException.class)
    ResponseEntity<ProjectResponse> JobIsNotExistHandler(JobIsNotExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("13", e.getMessage()));
    }

    @ExceptionHandler(MainServiceIsExistException.class)
    ResponseEntity<ProjectResponse> MainServiceIsExistHandler(MainServiceIsExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("14", e.getMessage()));
    }

    @ExceptionHandler(MainServiceIsNotExistException.class)
    ResponseEntity<ProjectResponse> MainServiceIsNotExistHandler(MainServiceIsNotExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("15", e.getMessage()));
    }

    @ExceptionHandler(NotFoundClassException.class)
    ResponseEntity<ProjectResponse> NotFoundClassHandler(NotFoundClassException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("16", e.getMessage()));
    }

    @ExceptionHandler(OfferNotExistException.class)
    ResponseEntity<ProjectResponse> OfferNotExistHandler(OfferNotExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("17", e.getMessage()));
    }

    @ExceptionHandler(OfferStatusException.class)
    ResponseEntity<ProjectResponse> OfferStatusHandler(OfferStatusException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("18", e.getMessage()));
    }

    @ExceptionHandler(OrderIsNotExistException.class)
    ResponseEntity<ProjectResponse> OrderIsNotExistHandler(OrderIsNotExistException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("19", e.getMessage()));
    }

    @ExceptionHandler(OrderStatusException.class)
    ResponseEntity<ProjectResponse> OrderStatusHandler(OrderStatusException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("20", e.getMessage()));
    }

    @ExceptionHandler(PasswordFormatException.class)
    ResponseEntity<ProjectResponse> PasswordFormatHandler(PasswordFormatException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("21", e.getMessage()));
    }

    @ExceptionHandler(PasswordIncorrect.class)
    ResponseEntity<ProjectResponse> PasswordIncorrectHandler(PasswordIncorrect ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("22", ae.getMessage()));
    }

    @ExceptionHandler(PositiveNumberException.class)
    ResponseEntity<ProjectResponse> PositiveNumberHandler(PositiveNumberException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("23", e.getMessage()));
    }

    @ExceptionHandler(ScoreOutOfBoundsException.class)
    ResponseEntity<ProjectResponse> ScoreOutOfBoundsHandler(ScoreOutOfBoundsException e) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("24", e.getMessage()));
    }

    @ExceptionHandler(TextBlankException.class)
    ResponseEntity<ProjectResponse> TextBlankHandler(TextBlankException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("25", ae.getMessage()));
    }

    @ExceptionHandler(TimeException.class)
    ResponseEntity<ProjectResponse> TimeHandler(TimeException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("26", ae.getMessage()));
    }

    @ExceptionHandler(ValidationTokenException.class)
    ResponseEntity<ProjectResponse> ValidationTokenHandler(ValidationTokenException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("27", ae.getMessage()));
    }

    @ExceptionHandler(VerifyCodeException.class)
    ResponseEntity<ProjectResponse> VerifyCodeHandler(VerifyCodeException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("28", ae.getMessage()));
    }

    @ExceptionHandler(WorkerIsHoldsExistException.class)
    ResponseEntity<ProjectResponse> WorkerIsHoldsExistHandler(WorkerIsHoldsExistException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("29", ae.getMessage()));
    }

    @ExceptionHandler(WorkerIsNotExistException.class)
    ResponseEntity<ProjectResponse> WorkerIsNotExistHandler(WorkerIsNotExistException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("30", ae.getMessage()));
    }

    @ExceptionHandler(WorkerNoAccessException.class)
    ResponseEntity<ProjectResponse> WorkerNoAccessHandler(WorkerNoAccessException ae) {
        return ResponseEntity.badRequest().body(
                new ProjectResponse("31", ae.getMessage()));
    }
}


