package os.toolset.pipeline;

public class ExecutionError extends Exception {
    public ExecutionError() {
    }

    public ExecutionError(String message) {
        super(message);
    }

    public ExecutionError(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionError(Throwable cause) {
        super(cause);
    }

    public ExecutionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
