package ls.lesm.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	//UMER
	@ExceptionHandler(RoleAreadyExistException.class)
	public ResponseEntity<?> roleExistHandler(RoleAreadyExistException roleExist, WebRequest webRequest){
		
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),roleExist.getErrorMessage(),roleExist.getErrorCode(),roleExist.getMessage());
		
		return new ResponseEntity<>(apiErorrs, HttpStatus.ALREADY_REPORTED); 
		
	}
	//UMER
	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<?> duplicateEntryHandler(DuplicateEntryException ex, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),ex.getErrorMessage(), ex.getErrorCode(),ex.getMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.ALREADY_REPORTED); 
		
	}
	//UMER
	@ExceptionHandler(DateMissMatchException.class)
	public ResponseEntity<?> dateMissMatchHandler(DateMissMatchException dmme, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),dmme.getErrorMessage(), dmme.getErrorCode(),dmme.getMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.BAD_REQUEST); 
		
	}
	//UMER
	@ExceptionHandler(RelationNotFoundExceptions.class)
	public ResponseEntity<?> duplicateEntryHandler(RelationNotFoundExceptions ex, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),ex.getErrorMessage(), ex.getErrorCode(),ex.getMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.NOT_FOUND); 
		
	}
	//UMER
	@ExceptionHandler(RecordNotFoundException.class)
	public ResponseEntity<?> notFoundHandler(RecordNotFoundException rnfe, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),rnfe.getErrorMessage(), rnfe.getErrorCode(),rnfe.getErrorMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.NOT_FOUND); 
	}
	//UMER
	@ExceptionHandler(RecordAlredyExistException.class)
	public ResponseEntity<?> recordExistHandler(RecordAlredyExistException raee, WebRequest webRequest){
		ApiErorrs apiErorrs =new ApiErorrs(new Date(),raee.getErrorMessage(), raee.getErrorCode(),raee.getMessage());
		return new ResponseEntity<>(apiErorrs, HttpStatus.NOT_FOUND); 
		
	}
}
