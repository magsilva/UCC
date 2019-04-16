package ucc.datatypes;

/**
 * This class contains constants used by UCC-G.
 * 
 * @author Integrity Applications Incorporated
 *
 */
public class Constants
{
   /**
    * Minimum modification threshold value allowed, used for differencing two
    * files
    */
   public static final double MIN_MOD_THRESHOLD = 0.0;

   /**
    * Maximum modification threshold value allowed, used for differencing two
    * files
    */
   public static final double MAX_MOD_THRESHOLD = 100.0;

   /** Default modification threshold, used for differencing two files */
   public static final double DEFAULT_MOD_THRESHOLD = 60.0;

   /**
    * Minimum duplication threshold value allowed, used for checking duplicate
    * code between two files
    */
   public static final double MIN_DUP_THRESHOLD = 1.0;

   /**
    * Maximum duplication threshold value allowed, used for checking duplicate
    * code between two files
    */
   public static final double MAX_DUP_THRESHOLD = 100.0;

   /**
    * Default duplication threshold, used for checking duplicate code between
    * two files
    */
   public static final double DEFAULT_DUP_THRESHOLD = -1.0;

   /** Minimum line truncation threshold, used for trimming lines */
   public static final int MIN_TRUNC_THRESHOLD = 0;

   /** Maximum line truncation threshold, used for trimming lines */
   public static final int MAX_TRUNC_THRESHOLD = 10000;

   /** Line truncation threshold, trim a line that exceeds below threshold */
   public static final int DEFAULT_TRUNC_THRESHOLD = 10000;

   /** New line character used by the system */
   public static final String NEW_LINE_SEPARATOR = System.lineSeparator();

   /** Type of character set encoding to use for writing reports */
   public static final String CHARSET_NAME = "UTF-8";

   /** File name suffix for files containing PSLOC */
   public static final String PSLOC_FILE_SUFFIX = "_PSLOC";

   /** File name suffix for files containing LSLOC */
   public static final String LSLOC_FILE_SUFFIX = "_LSLOC";

   /** The baseline being operated on (can be A or B) */
   public static final String BASELINE_A = "A";

   /** The baseline being operated on (can be A or B) */
   public static final String BASELINE_B = "B";
}
