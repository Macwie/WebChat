package backend.server;

public abstract class AbstractLogger {
    public static final int LOG = 1;
    public static final int INFO = 2;

    protected int level;

    //next element in chain or responsibility
    protected AbstractLogger nextLogger;
    public static final AbstractLogger loggerChain = getChainOfLoggers();

    private void setNextLogger(AbstractLogger nextLogger){
        this.nextLogger = nextLogger;
    }

    public void logMessage(int level, String message){
        if(this.level <= level){
            write(message);
        }
        if(nextLogger !=null){
            nextLogger.logMessage(level, message);
        }
    }

    private static AbstractLogger getChainOfLoggers(){
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.LOG);
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);
        consoleLogger.setNextLogger(fileLogger);
        return consoleLogger;
    }

    abstract protected void write(String message);

}