package ddd.application;

import ddd.domain.IssueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(IssueRepository.IssueNotFound.class)
    public ResponseEntity<ErrorJson> handleError(IssueRepository.IssueNotFound e) {
        return new ResponseEntity<>(new ErrorJson(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorJson> handleError(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorJson(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorJson> handleError(IllegalStateException e) {
        return new ResponseEntity<>(new ErrorJson(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    public class ErrorJson {
        public String errorMsg;

        public ErrorJson(String message) {
            this.errorMsg = message;

        }
    }
}
