package auca.ac.rw.restfullApiAssignment.model.user;

/**
 * Generic API Response wrapper class for standardized response format.
 * Wraps all responses with success status, message, and data payload.
 *
 * @param <T> The type of data being returned
 */
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // Default constructor
    public ApiResponse() {}

    // Parameterized constructor
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
