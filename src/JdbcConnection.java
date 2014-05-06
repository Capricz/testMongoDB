public class JdbcConnection {
    private static Connection connection;
    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
 
    private JdbcConnection() {   }
    private static String jdbcUrl;
    private static String userName;
    private static String password;
    private static String query;
    private static String interval;
    private static final Logger logger = LoggerFactory.getLogger(JdbcConnection.class);
 
    static{
        Properties prop = new Properties();
        try {
            prop.load(JdbcConnection.class.getResourceAsStream("/connection.properties"));
            jdbcUrl = prop.getProperty("jdbc.url");
            userName = prop.getProperty("jdbc.username");
            password = prop.getProperty("jdbc.password");
            query = prop.getProperty("jdbc.query");
            interval = prop.getProperty("poll.interval");
             
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
         
    }
     
    public static Connection getConnection() {
        if(connection != null ){
            return connection;
        }
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(getJdbcUrl(),getUserName(),getPassword());
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            // throw the exception
            logger.error(e.getMessage());
        } catch(SQLException e){
            logger.error(e.getMessage());
        }
        return connection;
    }
 
    public static String getJdbcUrl() {
        return jdbcUrl;
    }
 
    public static String getUserName() {
        return userName;
    }
 
    public static String getPassword() {
        return password;
    }
 
    public static String getQuery() {
        return query;
    }
 
    public static String getInterval() {
        return interval;
    }
}