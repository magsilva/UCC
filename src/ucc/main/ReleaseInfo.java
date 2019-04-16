package ucc.main;

/**
 * ReleaseInfo class contains useful information about the UCC-G application for
 * users. It includes version number of UCC-G, usage information, and a detailed
 * help for each one of the user options/switches.
 *
 * @author Integrity Applications Incorporated
 * 
 */
public class ReleaseInfo
{
   /** Version/release number of UCC-G */
   /** Version and title changed due to USC release of UCC-G */
   // TODO: Update version number before creating the Jar
   private static final String VersionInfo = "USC CSSE Unified Code Counter - Java (UCC-J) 2018.05 (Based on UCC-G v.1.1.1)";

   /** UCC-G usage information */
   private static final String UsageInfo = "\nUsage:\n\n"
            + " UCC-G [-v] [-h] [-d [-i1 <fileListA>] [-i2 <fileListB>] [-t <#>]]\n"
            + "       [-tdup <#>] [-trunc <#>] [-cf]\n" + "       [-dir <dirA> [<dirB>] <filespecs> [-import <file>]]\n"
            + "       [-outdir <outDir>] [-extfile <extFile>] [-unified] [-ascii]\n"
            + "       [-nodup] [-nocomplex] [-nolinks] [-debug <level>] \n"
            + "       [-export <language> -outdir <outDir>]\n\n";

   /**
    * Provides UCC-G version information
    * 
    * @return Returns a String that contains UCC-G version information
    */
   public static String GetVersionInfo()
   {
      return VersionInfo;
   }

   /**
    * Provides UCC-G usage information
    * 
    * @return Returns a String that contains UCC-G usage information
    */
   public static String GetUsageInfo()
   {
      return UsageInfo;
   }

   /**
    * Prints detailed help information on user selected option. If no option is
    * provided, print help information for all options.
    * 
    * @param option
    *           A String that contains user provided switch option
    */
   public static void PrintHelpInfo(String option)
   {
      if (option.equals("-v"))
      {
         System.out.println("\nUsage: UCC-G -v\n");
         System.out.println(" -v: Displays the current version of UCC being executed");
      }
      else if (option.equals("-d") || option.equals("-i1") || option.equals("-i2") || option.equals("-t"))
      {
         System.out.println("\nUsage: UCC-G -d [-i1 <fileListA>] [-i2 <fileListB>] [-t <#>]\n");
         System.out.println(" -d: Enables the differencing function. If not specified, only the");
         System.out.println("     counting functions will execute.");
         System.out.println("\nRelated Options:\n");
         System.out.println(" -i1 <fileListA>: File containing a list of files to be used as");
         System.out.println("                  Baseline A for counting or comparison if -d is specified.");
         System.out.println("                  If -i1 is not specified, the file 'fileListA.txt' will be");
         System.out.println("                  used as the default if -d is specified, and the file");
         System.out.println("                  'fileList.txt' will be used as the default without -d.\n");
         System.out.println(" -i2 <fileListB>: File containing a list of files to be be used as");
         System.out.println("                  Baseline B for comparison to Baseline A.");
         System.out.println("                  If -i2 is not specified, the file 'fileListB.txt' will be");
         System.out.println("                  used as the default if -d is specified.\n");
         System.out.println(" -t <#>:          Specifies the percentage of common characters between two");
         System.out.println("                  lines of code that are being compared in order to determine");
         System.out.println("                  whether the line is modified or replaced. If the percentage");
         System.out.println("                  of common characters between the compared lines is greater");
         System.out.println("                  than the specified threshold, the line is considered ");
         System.out.println("                  replaced andwill be counted as one line deleted and one");
         System.out.println("                  line added. Otherwise, it will be counted as one modified");
         System.out.println("                  line. The valid range is 0 to 100 and defaults to 60.");
      }
      else if (option.equals("-tdup"))
      {
         System.out.println("\nUsage: UCC-G -tdup <#>\n");
         System.out.println(" -tdup <#>: Specifies the percentage of logical source lines of code (LSLOC)");
         System.out.println("            that are same between two files of the same name in order to");
         System.out.println("            determine whether the files are duplicates. If the percentage of");
         System.out.println("            common LSLOC between two files is greater than or equal to the");
         System.out.println("            specified threshold, the files are considered duplicates.");
         System.out.println("            This method compares LSLOC similar to the differencing function");
         System.out.println("            and ignores formatting including blank lines and comments.");
         System.out.println("            Note that files of different names may be checked for an exact");
         System.out.println("            physical match. The valid range is 1 to 100 and defaults to 60.");
      }
      else if (option.equals("-trunc"))
      {
         System.out.println("\nUsage: UCC-G -trunc <#>\n");
         System.out.println(" -trunc <#>: Specifies the maximum number of characters allowed in a logical");
         System.out.println("             source line of code (LSLOC). Any characters beyond the specified");
         System.out.println("             threshold will be truncated and ignored when compared.");
         System.out.println("             If the truncation is disabled by setting the threshold to 0");
         System.out.println("             or the threshold is set too high, very long LSLOC may");
         System.out.println("             significantly degrade performance.");
      }
      else if (option.equals("-cf"))
      {
         System.out.println("\nUsage: UCC-G -cf\n");
         System.out.println(" -cf: Indicates that the target files were retrieved from");
         System.out.println("      IBM Rational ClearCase. ClearCase appends information at the end");
         System.out.println("      of file names beginning with '@@'. Use of this option strips");
         System.out.println("      all characters after the last '@@' sequence from the file name.");
      }
      else if (option.equals("-dir"))
      {
         System.out.println("\nUsage: UCC-G -dir <dirA> [<dirB>] <fileSpecs>\n");
         System.out.println(" -dir: Specifies the directories and file types to be searched for files");
         System.out.println("       to be counted or compared. The directories dirA and dirB indicate");
         System.out.println("       the directories to be searched for each baseline. The fileSpecs indicate");
         System.out.println("       the file type specifications (typically containing search wildcards).");
         System.out.println("       The specified directories will be searched recursively.");
         System.out.println("\nRequired Parameters:\n");
         System.out.println(" <dirA>:      Specifies the directory of Baseline A to be searched for files");
         System.out.println("              to be counted or compared.\n");
         System.out.println("\nOptional Parameters:\n");
         System.out.println(" <dirB>:      If the -d option is specified, this specifies the directory");
         System.out.println("              of Baseline B to be searched for files to be compared.\n");
         System.out.println(" <fileSpecs>: One or more specification of file types to be included");
         System.out.println("              in the file search. Each file specification is separated");
         System.out.println("              by whitespace and typically contains search wildcards.");
         System.out.println("              For example: *.cpp *.h *.java\n");
         System.out.println(" -import <file>: Imports specified file containing language properties. ");
         System.out.println("                 for a custom language. The custom language is included ");
         System.out.println("                 when code counter is run on a given directory or a file list.");
         System.out.println("                 The properties will be stored in memory.");
      }
      else if (option.equals("-outdir"))
      {
         System.out.println("\nUsage: UCC-G -outdir <outDir>\n");
         System.out.println(" -outdir <outDir>: Specifies the directory where output files will be written.");
         System.out.println("                   If this is not specified, the output files will be written");
         System.out.println("                   to the working directory by default. This option prevents");
         System.out.println("                   overwriting output files from multiple runs and allows for");
         System.out.println("                   batch execution and output organization.");
      }
      else if (option.equals("-extfile"))
      {
         System.out.println("\nUsage: UCC-G -extfile <extFile>\n");
         System.out.println(" -extfile <extFile>: Specifies a file containing user specified file extensions");
         System.out.println("                     for any of the available language counters. Any language");
         System.out.println("                     counter specified within this file will have its");
         System.out.println("                     associated extensions replaced. If a language is specified");
         System.out.println("                     with no extensions, the language counter will be disabled.");
         System.out.println("                     The file format contains a single line entry for each");
         System.out.println("                     language. Single or multi-line comments may be included");
         System.out.println("                     with square brackets []. For example:");
         System.out.println("                       C_CPP=*.cpp,*.h  [C/C++ extensions]");
         System.out.println("                     Please see the user manual for available language counter");
         System.out.println("                     names.");
      }
      else if (option.equals("-unified"))
      {
         System.out.println("\nUsage: UCC-G -unified\n");
         System.out.println(" -unified: Prints language report files to a single unified report file.");
         System.out.println("           The results are written to 'TOTAL_outfile.csv' or");
         System.out.println("           'TOTAL_outfile.txt'. In the absence of this option, results for each");
         System.out.println("            language are written to separate files.");
      }
      else if (option.equals("-ascii"))
      {
         System.out.println("\nUsage: UCC-G -ascii\n");
         System.out.println(" -ascii: Prints ASCII text (*.txt) report files instead of CSV (*.csv) files.");
         System.out.println("         The content of the ASCII format is identical to the CSV format.");
      }
      else if (option.equals("-nodup"))
      {
         System.out.println("\nUsage: UCC-G -nodup\n");
         System.out.println(" -nodup: Prevents separate processing of duplicate files. This avoids extra");
         System.out.println("         processing time to determine the presence of duplicate files within");
         System.out.println("         each baseline. With this option, all files will be counted and");
         System.out.println("         reported together, regardless of whether they are duplicates.");
         System.out.println("         Otherwise, files within a baseline will be checked for duplicates and");
         System.out.println("         results willbe reported separately. Please see the user manual for");
         System.out.println("         details.");
      }
      else if (option.equals("-nocomplex"))
      {
         System.out.println("\nUsage: UCC-G -nocomplex\n");
         System.out.println(" -nocomplex: Disables printing of keyword counts and processing of complexity");
         System.out.println("             metrics. This can reduce processing time and limit reports.");
      }
      else if (option.equals("-nolinks"))
      {
         System.out.println("\nUsage: UCC-G -nolinks\n");
         System.out.println(" -nolinks: Disables following symbolic links to directories and files.");
         System.out.println("           For UNIX systems only.\n");
      }
      else if (option.equals("-h"))
      {
         System.out.println("\nUsage: UCC-G -h <option>\n");
         System.out.println(" -h <option>: Without a specified option, this displays the list of command");
         System.out.println("              line options. If a command line option is specified, detailed");
         System.out.println("              usage information for the specified option is displayed.");
      }
      else if (option.equals("-debug"))
      {
         System.out.println("\nUsage: UCC-G -debug\n");
         System.out.println(" -debug <level>: Enables the UCC-G debugger.\n");
         System.out.println("                 Outputs are directed to the <time_stamp>_ucc.log file");
         System.out.println("                 which will appear in the same folder as the UCC-G ");
         System.out.println("                 executable by default if -outdir is not specified.");
         System.out.println("                 Valid levels are FATAL, ERROR, WARN, INFO, DEBUG or TRACE.");
         System.out.println("                 By default, the application is set to the ERROR level.");
         System.out.println("                 All levels under the specified level will be logged.");
         System.out.println("                 For example, if the level is set to INFO, any log");
         System.out.println("                 outputs with INFO, WARN, ERROR, or FATAL ratings would");
         System.out.println("                 be outputted and log statements with DEBUG or TRACE ");
         System.out.println("                 would be ignored.");
      }
      else if (option.equals("-import"))
      {
         System.out.println("\nUsage: UCC-G -import\n");
         System.out.println(" -import <file>: Imports the specified file containing language properties ");
         System.out.println("                 for a custom language. The custom language is included ");
         System.out.println("                 when code counter is run on a given directory or a file list.");
         System.out.println("                 The properties will be stored in memory.\n");
         System.out.println("                 This is not a stand-alone switch. It can only be used with");
         System.out.println("                 a counter or a differencer operation");
      }
      else if (option.equals("-export"))
      {
         System.out.println("\nUsage: UCC-G -export\n");
         System.out.println(" -export <language_name> : Exports language properties for the specified");
         System.out.println("                           language to a text file.\n");
         System.out.println("                           The output text file is stored in the");
         System.out.println("                           directory specified with -outdir ");
         System.out.println("                           option or defaults to current working directory.\n");
         System.out.println("                           Output file name will be:");
         System.out.println("                           [language_name]LanguageProperties.txt\n");
         System.out.println("                           language_name used with the switch must match");

         System.out.println("                           one of the following: ADA, ASP, ASSEMBLY, BASH, ");
         System.out.println("                           C_CPP, CSHARP, CSHELL, COLDFUSION, ");
         System.out.println("                           COLDFUSION_SCRIPT, CSS, DOS_BATCH, FORTRAN, HTML, ");
         System.out.println("                           IDL, JAVA, JAVASCRIPT, JSP, MAKEFILE, MATLAB, ");
         System.out.println("                           NEXTMIDAS, PASCAL, PERL, PHP, PYTHON, RUBY, ");
         System.out.println("                           SCALA, SQL, VB, VB_SCRIPT, VERILOG, VHDL, ");
         System.out.println("                           XMIDAS, XML, XSL.");
      }
      else
      {
         System.out.print(ReleaseInfo.GetUsageInfo());
         System.out.println("Options:\n");
         System.out.println(" -v                  Lists the current version number.\n");
         System.out.println(" -h <option>         Displays this usage or usage for a specified option.\n");
         System.out.println(" -d                  Runs the differencing function.");
         System.out.println("                     If not specified, runs the counting function.\n");
         System.out.println(" -i1 <fileListA>     Filename containing filenames in the Baseline A.\n");
         System.out.println(" -i2 <fileListB>     Filename containing filenames in the Baseline B.\n");
         System.out.println(" -t <#>              Specifies the threshold percentage for a modified line.");
         System.out.println("                     (DEFAULTS TO 60).\n");
         System.out.println(" -tdup <#>           Specifies the threshold percentage for duplicated files -");
         System.out.println("                     the maximum percent match between two files of the");
         System.out.println("                     same name in a baseline to be considered duplicates.");
         System.out.println("                     (DEFAULTS TO 60).\n");
         System.out.println(" -trunc <#>          Specifies the maximum number of characters allowed in a");
         System.out.println("                     logical SLOC. Additional characters will be truncated.");
         System.out.println("                     (DEFAULTS TO 10,000, use 0 for no truncation).\n");
         System.out.println(" -cf                 Indicated that target files were retrieved from ClearCase.");
         System.out.println("                     Restored the original filename before counting.\n");
         System.out.println(" -dir                Specifies the directories and file specifications:\n");
         System.out.println(" Required Parameters:\n");
         System.out.println("      <dirA>         Name of the directory containing source files.");
         System.out.println("                     If -d is given, dirA is the directory for Baseline A.\n");
         System.out.println(" Optional Parameters:\n");
         System.out.println("      <dirB>         Name of the directory for Baseline B only if -d is given.\n");
         System.out.println("      <fileSpecs>    File specifications, wildcard chars '?' and '*' are");
         System.out.println("                     allowed. For example, *.cpp *.h\n");
         System.out.println("     -import <file>  Imports specified file containing language properties");
         System.out.println("                     for custom language.\n");
         System.out.println(" -outdir <outDir>    Specifies the directory to store the output files.\n");
         System.out.println(" -extfile <extFile>  Indicates language extension mapping filename.\n");
         System.out.println(" -unified            Prints language report files to a unified report file.\n");
         System.out.println(" -ascii              Prints ASCII text report files instead of CSV files.\n");
         System.out.println(" -nodup              Disables separate processing of duplicate files.\n");
         System.out.println(" -nocomplex          Disables printing complexity reports or keyword counts.\n");
         System.out.println(" -nolinks            Disables following symbolic links to directories and files.");
         System.out.println("                     For UNIX systems only.\n");
         System.out.println(" -debug <level>      Enables the UCC-G debugger. Outputs are directed to the");
         System.out.println("                     <time_stamp>_ucc.log file which will appear in the same");
         System.out.println("                     folder as the UCC-G executable by default if -outdir is");
         System.out.println("                     unspecified. Valid levels are FATAL, ERROR, WARN, INFO, ");
         System.out.println("                     DEBUG or TRACE. By default, the application is set to ");
         System.out.println("                     the ERROR level.\n");
         System.out.println(" -export <language>  Exports language properties for specified language to text");
         System.out.println("                     file.");
         System.out.println("                     Output text file is stored in the directory specified");
         System.out.println("                     with -outdir or deafaults to current working directory.");
      }
   }
}