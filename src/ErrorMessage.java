

public class ErrorMessage {
	private int errorCode;
    private String error;

    public ErrorMessage(int errorCode, String error) {
    	this.errorCode = errorCode;
        this.error = error;
    }
 
    public String getError() {
        return error;
    }
    
    public int getErrorCode() {
        return errorCode;
    }

}
