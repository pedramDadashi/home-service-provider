package ir.maktabsharif.homeservicephase2.exception.global;

import ir.maktabsharif.homeservicephase2.dto.response.ProjectResponse;
import ir.maktabsharif.homeservicephase2.exception.AlphabetException;
import ir.maktabsharif.homeservicephase2.exception.ImageFormatException;
import ir.maktabsharif.homeservicephase2.exception.PasswordFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlphabetException.class)
    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
        return ResponseEntity.badRequest().body(pr);
    }
    @ExceptionHandler(PasswordFormatException.class)
    ResponseEntity<ProjectResponse> PasswordHandler(PasswordFormatException ae) {
        ProjectResponse pr = new ProjectResponse("02", ae.getMessage());
        return ResponseEntity.badRequest().body(pr);
    }
    @ExceptionHandler(ImageFormatException.class)
    ResponseEntity<ProjectResponse> AlphabetHandler(ImageFormatException ae) {
        ProjectResponse pr = new ProjectResponse("03", ae.getMessage());
        return ResponseEntity.badRequest().body(pr);
    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }
//    @ExceptionHandler(AlphabetException.class)
//    ResponseEntity<ProjectResponse> AlphabetHandler(AlphabetException ae) {
//        ProjectResponse pr = new ProjectResponse("01", ae.getMessage());
//        return ResponseEntity.badRequest().body(pr);
//    }


}


