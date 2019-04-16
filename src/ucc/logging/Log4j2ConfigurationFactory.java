package ucc.logging;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternSelector;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

/**
 * Log4j2ConfigurationFactory class is the programmatic Log4j2 configuration for
 * the UCC-X application.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class Log4j2ConfigurationFactory
{
   /** Name of log file */
   protected static String fileName;

   /** Instantiate LoggerContext */
   public static LoggerContext ctx;

   /**
    * UCC can be configured to any log4j2 logging level, where that level and
    * all lower levels are logged. The log4j2 logging levels are as follows:
    * 
    * 1. FATAL 100 2. ERROR 200 3. WARN 300 4. INFO 400 5. DEBUG 500 6. TRACE
    * 600
    * 
    * For example, if UCC is set to the �INFO� level, any logger calls
    * throughout the application set to INFO, WARN, ERROR or FATAL levels would
    * be logged (any logger calls using DEBUG or TRACE would be ignored).
    * 
    * 
    * @param uccLog4j2Level
    *           Sets the overall UCC log4j2 Level.
    * @param outdir
    *           Log file output location
    */
   public static void Initialize(Level uccLog4j2Level, String outdir)
   {

      // If false the LoggerContext appropriate for the caller of this method is
      // returned. For example, in a web application if the caller
      // is a class in WEB-INF/lib then one LoggerContext may be returned and if
      // the caller is a class in the container's classpath then a
      // different LoggerContext may be returned. If true then only a single
      // LoggerContext will be returned.
      boolean currentContext = false;
      ctx = (LoggerContext) LogManager.getContext(currentContext);

      final Configuration config = ctx.getConfiguration();

      // PatternLayout Constructor Attributes
      String pattern = PatternLayout.SIMPLE_CONVERSION_PATTERN; // The pattern.
                                                                // If not
                                                                // specified,
                                                                // defaults to
                                                                // DEFAULT_CONVERSION_PATTERN.
      PatternSelector patternSelector = null; // Allows different patterns to be
                                              // used based on some selection
                                              // criteria.
      RegexReplacement replace = null; // A Regex replacement String.
      Charset charset = null; // The character set. The platform default is used
                              // if not specified.
      boolean alwaysWriteExceptions = true; // If true (default) exceptions are
                                            // always written even if the
                                            // pattern contains no exception
                                            // tokens.
      boolean noConsoleNoAnsi = true; // If true (default is false) and
                                      // System.console() is null, do not output
                                      // ANSI escape codes
      String header = null; // The footer to place at the top of the document,
                            // once.
      String footer = null; // The footer to place at the bottom of the
                            // document, once.

      Layout<? extends Serializable> layout = PatternLayout.createLayout(pattern, patternSelector, config, replace,
               charset, alwaysWriteExceptions, noConsoleNoAnsi, header, footer);

      // FileAppender Constructor Attributes
      fileName = GetLogFileName(outdir); // Get log file name
      String append = "false"; // "true" if the file should be appended to,
                               // "false" if it should be overwritten. The
                               // default is "true".
      String locking = "false"; // "true" if the file should be locked. The
                                // default is "false".
      String name = "File"; // The name of the Appender.
      String immediateFlush = "true"; // "true" if the contents should be
                                      // flushed on every write, "false"
                                      // otherwise. The default is "true".
      String ignoreExceptions = "false"; // If "true" (default) exceptions
                                         // encountered when appending events
                                         // are logged; otherwise they are
                                         // propagated to the caller.
      String bufferedIo = "false"; // "true" if I/O should be buffered, "false"
                                   // otherwise. The default is "true".
      String bufferSizeStr = "8192"; // buffer size for buffered IO (default is
                                     // 8192).
      Filter filter = null; // The filter, if any, to use.
      String advertise = "false"; // "true" if the appender configuration should
                                  // be advertised, "false" otherwise.
      String advertiseUri = null; // The advertised URI which can be used to
                                  // retrieve the file contents.

      try
      {
         Appender appender = FileAppender.createAppender(fileName, append, locking, name, immediateFlush,
                  ignoreExceptions, bufferedIo, bufferSizeStr, layout, filter, advertise, advertiseUri, config);
         appender.start();
         config.addAppender(appender);

         AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);

         // LoggerConfig Constructor Attributes
         String additivity = "false"; // True if additive, false otherwise.
         Level level = uccLog4j2Level; // The Level to be associated with the
                                       // Logger.
         String loggerName = LogManager.ROOT_LOGGER_NAME; // The name of the
                                                          // Logger.
         String includeLocation = "true"; // Whether location should be passed
                                          // downstream
         AppenderRef[] refs = new AppenderRef[] { ref }; // An array of Appender
                                                         // names.
         Property[] properties = null; // Properties to pass to the Logger.
         Filter aFilter = null; // The filter, if any, to use.
         LoggerConfig loggerConfig = LoggerConfig.createLogger(additivity, level, loggerName, includeLocation, refs,
                  properties, config, aFilter);
         loggerConfig.addAppender(appender, null, null);

         config.addLogger(LogManager.ROOT_LOGGER_NAME, loggerConfig);
         ctx.updateLoggers();
      }
      catch (java.lang.IllegalStateException ise)
      {
         System.err.println("Error creating logger. Ensure that the user has read/write permission to " + outdir);
      }
   }

   /**
    * Returns time stamped log file name
    * 
    * @param outdir
    *           The path of the log file's output location
    * @return Returns name/path of the log file with time stamp
    */
   public static String GetLogFileName(String outdir)
   {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH\uA789mm\uA789ss");
      return (outdir + dateFormat.format(new Date()) + "-ucc.log");
   }

   /**
    * Public method that gets the stored file Name
    * 
    * @return Returns the stored log file name
    */
   public static String GetFileName()
   {
      return fileName;
   }
}