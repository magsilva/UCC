package ucc.main;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.counters.*;
import ucc.datatypes.Constants;
import ucc.datatypes.DataTypes.LanguagePropertiesType;
import ucc.datatypes.DiffResultType;
import ucc.datatypes.UCCFile;
import ucc.diff.Differencer;
import ucc.dup.DuplicateFileFinder;
import ucc.langprops.*;
import ucc.reports.CSVCounterReporter;
import ucc.reports.CSVDiffReporter;
import ucc.reports.CounterReporter;
import ucc.reports.DiffReporter;
import ucc.reports.ASCIICounterReporter;
import ucc.reports.ASCIIDiffReporter;
import ucc.utils.FileUtils;
import ucc.utils.ProgressVisualizer;
import ucc.utils.TimeUtils;

/**
 * ProcessController class is the main processing hub of UCC-X application. It
 * counts code and/or differences code based on user request. ProcessController
 * gets called by the {@link MainUCC} class after user request has been parsed.
 * Code count operation is performed on source code files contained in one
 * baseline. Code differencing operation is performed on source code files of
 * two baselines.
 * 
 * <p>
 * This class will also handle multi-threading capability of UCC-X in the
 * future.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class ProcessController
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(ProcessController.class);

   /** An array of all the language properties */
   private LanguageProperties[] LangProps;

   /**
    * An array of CodeCounter objects to be used for generating count metrics
    * for a given baseline. When differencing, this object represents baseline
    * A.
    */
   private CodeCounter[] CntrObjs;

   /**
    * An array of UCCFile objects to store count metrics. When differencing,
    * this object stores results for baseline A.
    */
   private ArrayList<UCCFile> CntrResults;

   /**
    * An object of CounterReporter class to print reports that contain count
    * metrics. When differencing, this object generates reports for baseline A.
    */
   private CounterReporter CntrRprtr;

   /**
    * An array of UCCFile objects to store count metrics for baseline B when
    * when differencing two baselines.
    */
   private ArrayList<UCCFile> CntrResultsB;

   /**
    * An object of CounterReporter class to print reports that contain count
    * metrics for Baseline B when differencing two baselines.
    */
   private CounterReporter CntrRprtrB;

   /**
    * An object of class Differencer to be used for differencing two baselines
    */
   private Differencer DiffObj;

   /**
    * An object of DiffReporter class to print reports that contain differencer
    * metrics.
    */
   private DiffReporter DiffRprtr;

   /**
    * An array of DiffResultType objects to store differencer results data.
    */
   private ArrayList<DiffResultType> DiffResults;

   /**
    * An array of DiffResultType objects to store differencer results data of
    * duplicate files.
    */
   private ArrayList<DiffResultType> DupDiffResults;

   /**
    * A handle to the RuntimeParameters class's single instance
    */
   private static RuntimeParameters RtParams;

   /**
    * Number of supported languages
    */
   private int NumLangs;

   /**
    * Default constructor to instantiate/initialize class member objects
    */
   public ProcessController()
   {
      // Initialize class variables
      RtParams = RuntimeParameters.GetInstance();

      if (RtParams.UseCustomLang)
      {
         NumLangs = LanguagePropertiesType.values().length;
      }
      else
      {
         NumLangs = LanguagePropertiesType.values().length - 1;
      }

      // Initialize language properties objects
      LangProps = new LanguageProperties[NumLangs];
      LangProps[LanguagePropertiesType.ADA.GetIndex()] = new ADALanguageProperties();
      LangProps[LanguagePropertiesType.ASP.GetIndex()] = new ASPLanguageProperties();
      LangProps[LanguagePropertiesType.ASSEMBLY.GetIndex()] = new AssemblyLanguageProperties();
      LangProps[LanguagePropertiesType.BASH.GetIndex()] = new BashLanguageProperties();
      LangProps[LanguagePropertiesType.C_CPP.GetIndex()] = new CCPPLanguageProperties();
      LangProps[LanguagePropertiesType.CSHARP.GetIndex()] = new CSharpLanguageProperties();
      LangProps[LanguagePropertiesType.CSHELL.GetIndex()] = new CShellLanguageProperties();
      LangProps[LanguagePropertiesType.COLDFUSION.GetIndex()] = new ColdFusionLanguageProperties();
      LangProps[LanguagePropertiesType.COLDFUSION_SCRIPT.GetIndex()] = new ColdFusionScriptLanguageProperties();
      LangProps[LanguagePropertiesType.CSS.GetIndex()] = new CSSLanguageProperties();
      LangProps[LanguagePropertiesType.DOS_BATCH.GetIndex()] = new DOSBatchLanguageProperties();
      LangProps[LanguagePropertiesType.FORTRAN.GetIndex()] = new FortranLanguageProperties();
      LangProps[LanguagePropertiesType.HTML.GetIndex()] = new HTMLLanguageProperties();
      LangProps[LanguagePropertiesType.IDL.GetIndex()] = new IDLLanguageProperties();
      LangProps[LanguagePropertiesType.JAVA.GetIndex()] = new JavaLanguageProperties();
      LangProps[LanguagePropertiesType.JAVASCRIPT.GetIndex()] = new JavaScriptLanguageProperties();
      LangProps[LanguagePropertiesType.JSP.GetIndex()] = new JSPLanguageProperties();
      LangProps[LanguagePropertiesType.MAKEFILE.GetIndex()] = new MakefileLanguageProperties();
      LangProps[LanguagePropertiesType.MATLAB.GetIndex()] = new MatlabLanguageProperties();
      LangProps[LanguagePropertiesType.NEXTMIDAS.GetIndex()] = new NextMidasLanguageProperties();
      LangProps[LanguagePropertiesType.PASCAL.GetIndex()] = new PascalLanguageProperties();
      LangProps[LanguagePropertiesType.PERL.GetIndex()] = new PerlLanguageProperties();
      LangProps[LanguagePropertiesType.PHP.GetIndex()] = new PHPLanguageProperties();
      LangProps[LanguagePropertiesType.PYTHON.GetIndex()] = new PythonLanguageProperties();
      LangProps[LanguagePropertiesType.RUBY.GetIndex()] = new RubyLanguageProperties();
      LangProps[LanguagePropertiesType.SCALA.GetIndex()] = new ScalaLanguageProperties();
      LangProps[LanguagePropertiesType.SQL.GetIndex()] = new SQLLanguageProperties();
      LangProps[LanguagePropertiesType.VB.GetIndex()] = new VBLanguageProperties();
      LangProps[LanguagePropertiesType.VB_SCRIPT.GetIndex()] = new VBScriptLanguageProperties();
      LangProps[LanguagePropertiesType.VERILOG.GetIndex()] = new VerilogLanguageProperties();
      LangProps[LanguagePropertiesType.VHDL.GetIndex()] = new VHDLLanguageProperties();
      LangProps[LanguagePropertiesType.XMIDAS.GetIndex()] = new XmidasLanguageProperties();
      LangProps[LanguagePropertiesType.XML.GetIndex()] = new XMLLanguageProperties();
      LangProps[LanguagePropertiesType.R.GetIndex()] = new RLanguageProperties();
      LangProps[LanguagePropertiesType.GO.GetIndex()] = new GoLanguageProperties();


      // Initialize code counter objects
      CntrObjs = new CodeCounter[NumLangs];
      CntrObjs[LanguagePropertiesType.ADA.GetIndex()] =
               new ADACounter(LangProps[LanguagePropertiesType.ADA.GetIndex()]);
      CntrObjs[LanguagePropertiesType.ASP.GetIndex()] =
               new ASPCounter(LangProps[LanguagePropertiesType.ASP.GetIndex()]);
      CntrObjs[LanguagePropertiesType.ASSEMBLY.GetIndex()] =
               new AssemblyCounter(LangProps[LanguagePropertiesType.ASSEMBLY.GetIndex()]);
      CntrObjs[LanguagePropertiesType.BASH.GetIndex()] =
               new BashCounter(LangProps[LanguagePropertiesType.BASH.GetIndex()]);
      CntrObjs[LanguagePropertiesType.C_CPP.GetIndex()] =
               new CCPPCounter(LangProps[LanguagePropertiesType.C_CPP.GetIndex()]);
      CntrObjs[LanguagePropertiesType.CSHARP.GetIndex()] =
               new CSharpCounter(LangProps[LanguagePropertiesType.CSHARP.GetIndex()]);
      CntrObjs[LanguagePropertiesType.CSHELL.GetIndex()] =
               new CShellCounter(LangProps[LanguagePropertiesType.CSHELL.GetIndex()]);
      CntrObjs[LanguagePropertiesType.COLDFUSION.GetIndex()] =
               new ColdFusionCounter(LangProps[LanguagePropertiesType.COLDFUSION.GetIndex()]);
      CntrObjs[LanguagePropertiesType.COLDFUSION_SCRIPT.GetIndex()] =
               new ColdFusionScriptCounter(LangProps[LanguagePropertiesType.COLDFUSION_SCRIPT.GetIndex()]);
      CntrObjs[LanguagePropertiesType.CSS.GetIndex()] =
               new CSSCounter(LangProps[LanguagePropertiesType.CSS.GetIndex()]);
      CntrObjs[LanguagePropertiesType.DOS_BATCH.GetIndex()] =
               new DOSBatchCounter(LangProps[LanguagePropertiesType.DOS_BATCH.GetIndex()]);
      CntrObjs[LanguagePropertiesType.FORTRAN.GetIndex()] =
               new FortranCounter(LangProps[LanguagePropertiesType.FORTRAN.GetIndex()]);
      CntrObjs[LanguagePropertiesType.HTML.GetIndex()] =
               new HTMLCounter(LangProps[LanguagePropertiesType.HTML.GetIndex()]);
      CntrObjs[LanguagePropertiesType.IDL.GetIndex()] =
               new IDLCounter(LangProps[LanguagePropertiesType.IDL.GetIndex()]);
      CntrObjs[LanguagePropertiesType.JAVA.GetIndex()] =
               new JavaCounter(LangProps[LanguagePropertiesType.JAVA.GetIndex()]);
      CntrObjs[LanguagePropertiesType.JAVASCRIPT.GetIndex()] =
               new JavaScriptCounter(LangProps[LanguagePropertiesType.JAVASCRIPT.GetIndex()]);
      CntrObjs[LanguagePropertiesType.JSP.GetIndex()] =
               new JSPCounter(LangProps[LanguagePropertiesType.JSP.GetIndex()]);
      CntrObjs[LanguagePropertiesType.MAKEFILE.GetIndex()] =
               new MakefileCounter(LangProps[LanguagePropertiesType.MAKEFILE.GetIndex()]);
      CntrObjs[LanguagePropertiesType.MATLAB.GetIndex()] =
               new MatlabCounter(LangProps[LanguagePropertiesType.MATLAB.GetIndex()]);
      CntrObjs[LanguagePropertiesType.NEXTMIDAS.GetIndex()] =
               new NextMidasCounter(LangProps[LanguagePropertiesType.NEXTMIDAS.GetIndex()]);
      CntrObjs[LanguagePropertiesType.PASCAL.GetIndex()] =
               new PascalCounter(LangProps[LanguagePropertiesType.PASCAL.GetIndex()]);
      CntrObjs[LanguagePropertiesType.PERL.GetIndex()] =
               new PerlCounter(LangProps[LanguagePropertiesType.PERL.GetIndex()]);
      CntrObjs[LanguagePropertiesType.PHP.GetIndex()] =
               new PHPCounter(LangProps[LanguagePropertiesType.PHP.GetIndex()]);
      CntrObjs[LanguagePropertiesType.PYTHON.GetIndex()] =
               new PythonCounter(LangProps[LanguagePropertiesType.PYTHON.GetIndex()]);
      CntrObjs[LanguagePropertiesType.RUBY.GetIndex()] =
               new RubyCounter(LangProps[LanguagePropertiesType.RUBY.GetIndex()]);
      CntrObjs[LanguagePropertiesType.SCALA.GetIndex()] =
               new ScalaCounter(LangProps[LanguagePropertiesType.SCALA.GetIndex()]);
      CntrObjs[LanguagePropertiesType.SQL.GetIndex()] =
               new SQLCounter(LangProps[LanguagePropertiesType.SQL.GetIndex()]);
      CntrObjs[LanguagePropertiesType.VB.GetIndex()] = new VBCounter(LangProps[LanguagePropertiesType.VB.GetIndex()]);
      CntrObjs[LanguagePropertiesType.VB_SCRIPT.GetIndex()] =
               new VBScriptCounter(LangProps[LanguagePropertiesType.VB_SCRIPT.GetIndex()]);
      CntrObjs[LanguagePropertiesType.VERILOG.GetIndex()] =
               new VerilogCounter(LangProps[LanguagePropertiesType.VERILOG.GetIndex()]);
      CntrObjs[LanguagePropertiesType.VHDL.GetIndex()] =
               new VHDLCounter(LangProps[LanguagePropertiesType.VHDL.GetIndex()]);
      CntrObjs[LanguagePropertiesType.XMIDAS.GetIndex()] =
               new XmidasCounter(LangProps[LanguagePropertiesType.XMIDAS.GetIndex()]);
      CntrObjs[LanguagePropertiesType.XML.GetIndex()] =
               new XMLCounter(LangProps[LanguagePropertiesType.XML.GetIndex()]);
      CntrObjs[LanguagePropertiesType.R.GetIndex()] = new RCounter(
               LangProps[LanguagePropertiesType.R.GetIndex()]);
      CntrObjs[LanguagePropertiesType.GO.GetIndex()] =
               new GoCounter(LangProps[LanguagePropertiesType.GO.GetIndex()]);

      // Setup custom language properties and code counter (if used)
      if (RtParams.UseCustomLang)
      {
         LangProps[LanguagePropertiesType.CUSTOM.GetIndex()] = PropertiesReader.GetCustomLangProps();

         if ((LangProps[LanguagePropertiesType.CUSTOM.GetIndex()].GetSimilarTo()).isEmpty())
         {
            CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                     new CodeCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
         }
         else
         {
            LanguagePropertiesType customLangSimilarTo =
                     LanguagePropertiesType.valueOf(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()].GetSimilarTo());

            // Assign appropriate code counter based on similar to property
            switch (customLangSimilarTo)
            {
               case ADA:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new ADACounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case ASP:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new ASPCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case ASSEMBLY:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new AssemblyCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case BASH:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new BashCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case C_CPP:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new CCPPCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case CSHARP:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new CSharpCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case CSHELL:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new CShellCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case COLDFUSION:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new ColdFusionCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case COLDFUSION_SCRIPT:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new ColdFusionScriptCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case CSS:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new CSSCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case DOS_BATCH:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new DOSBatchCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case FORTRAN:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new FortranCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case HTML:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new HTMLCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case IDL:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new IDLCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case JAVA:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new JavaCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case JAVASCRIPT:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new JavaScriptCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case JSP:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new JSPCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case MAKEFILE:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new MakefileCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case MATLAB:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new MatlabCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case NEXTMIDAS:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new NextMidasCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case PASCAL:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new PascalCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case PERL:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new PerlCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case PHP:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new PHPCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case PYTHON:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new PythonCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case RUBY:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new RubyCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case SCALA:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new ScalaCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case SQL:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new SQLCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case VB:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new VBCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case VB_SCRIPT:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new VBScriptCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case VERILOG:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new VerilogCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case VHDL:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new VHDLCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case XMIDAS:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new XmidasCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case XML:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new XMLCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;

               case R:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] = new RCounter(
                        LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;
                  
               case GO:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new GoCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;
                  
               default:
                  CntrObjs[LanguagePropertiesType.CUSTOM.GetIndex()] =
                           new CodeCounter(LangProps[LanguagePropertiesType.CUSTOM.GetIndex()]);
                  break;
            }
         }
      }

      // Initialize counter results object
      CntrResults = new ArrayList<UCCFile>();

      // Initialize reporter objects based on output format
      if (RtParams.OutputFileFmt == RuntimeParameters.OutputFormat.CSV)
      {
         CntrRprtr = new CSVCounterReporter("", RtParams.OutputDirPath, NumLangs);
      }
      else
      {
         CntrRprtr = new ASCIICounterReporter("", RtParams.OutputDirPath, NumLangs);
      }

      // If differencer operation is selected, initialize additional objects
      if (RtParams.DiffCode)
      {
         // Initialize additional counter results object(s)
         CntrResultsB = new ArrayList<UCCFile>();

         // Initialize differencer object
         DiffObj = new Differencer();

         // Initialize differencer results object
         DiffResults = new ArrayList<DiffResultType>();

         // If duplicate files are to be identified, initialize a differencer
         // results object to store duplicate files differencer results
         if (RtParams.SearchForDups)
         {
            DupDiffResults = new ArrayList<DiffResultType>();
         }

         // Initialize reporter objects based on output format
         if (RtParams.OutputFileFmt == RuntimeParameters.OutputFormat.CSV)
         {
            CntrRprtr = new CSVCounterReporter("Baseline-A-", RtParams.OutputDirPath, NumLangs);
            CntrRprtrB = new CSVCounterReporter("Baseline-B-", RtParams.OutputDirPath, NumLangs);

            DiffRprtr = new CSVDiffReporter(RtParams.OutputDirPath, NumLangs);
         }
         else
         {
            CntrRprtr = new ASCIICounterReporter("Baseline-A-", RtParams.OutputDirPath, NumLangs);
            CntrRprtrB = new ASCIICounterReporter("Baseline-B-", RtParams.OutputDirPath, NumLangs);

            DiffRprtr = new ASCIIDiffReporter(RtParams.OutputDirPath, NumLangs);
         }
      }
   }

   /**
    * Kicks off ProcessController to perform user requested
    * counting/differencing operations
    */
   public void StartProcessController()
   {
      long startTime = 0;
      long endTime = 0;

      startTime = TimeUtils.GetTime();

      // Set user defined language specific file extensions
      SetUserExtMapping();

      if (RtParams.CountSLOC)
      {
         // Display system resources at startup
         SystemInfo.DisplaySysInfo();

         System.out.println("Processing counter request...");
         logger.debug("Processing counter request...");
         if (ProcessCountReq(RtParams.FileListA, CntrResults, CntrRprtr, Constants.BASELINE_A))
         {
            System.out.println("Completed processing of counter request");
            logger.debug("Completed processing of counter request");
         }
         else
         {
            System.err.println("Error!  Counter request could not be processed");
            logger.error("Error!  Counter request could not be processed");
         }
      }
      else if (RtParams.DiffCode)
      {
         // Display system resources at startup
         SystemInfo.DisplaySysInfo();

         System.out.println("Processing differencer request...");
         System.out.println("");
         logger.debug("Processing differencer request...");
         if (ProcessDiffBaselinesReq())
         {
            System.out.println("Completed processing of differencer request");
            logger.debug("Completed processing of differencer request");
         }
         else
         {
            System.err.println("Error!  Differencer request could not be processed");
            logger.error("Error!  Differencer request could not be processed");
         }
      }
      endTime = TimeUtils.GetTime();

      TimeUtils.PrintElapsedTime(startTime, endTime, "Total");
   }

   /**
    * Performs code count operation using given set of parameters
    * 
    * @param fileList
    *           List of files to run code counter on
    * @param cntrResults
    *           Stores code count metrics for given list of files
    * @param cntrRprtr
    *           Prints code count reports for given list of files
    * @param baseline
    *           String containing the baseline (A or B) the counter is operating
    *           on
    * @return True if count request was processed, false otherwise
    */
   private boolean ProcessCountReq(ArrayList<String> fileList, ArrayList<UCCFile> cntrResults,
            CounterReporter cntrRprtr, String baseline)
   {
      ProgressVisualizer progressVisualizer = new ProgressVisualizer("Performing file counting");

      boolean processed = false;

      if (fileList.size() > 0)
      {
         if (cntrResults == null)
         {
            cntrResults = new ArrayList<UCCFile>();
         }

         // Initialize counter results with input file list
         for (int i = 0; i < fileList.size(); i++)
         {
            cntrResults.add(new UCCFile());
            cntrResults.get(i).FileName = fileList.get(i);
            cntrResults.get(i).Idx = i;
            cntrResults.get(i).Baseline = baseline;
         }

         if (cntrResults != null && !cntrResults.isEmpty())
         {
            // Map files in file list with proper language properties
            MapFileExtToLang(cntrResults);

            // First, identify all the duplicate files in the inputs list
            if (RtParams.SearchForDups)
            {
               DuplicateFileFinder.IdentifyDupFiles(cntrResults);

               // Check for duplicate filenames
               FileUtils.CheckForDuplicateFileNames(cntrResults);
            }

            long startTime = 0;
            long endTime = 0;
            startTime = TimeUtils.GetTime();

            UCCFile cntrResult;

            // Count code metrics for each file
            for (int i = 0; i < cntrResults.size(); i++)
            {
               // Output
               cntrResult = cntrResults.get(i);

               if (cntrResult.LangProperty != null && (!cntrResult.IsDup || (cntrResult.IsDup && RtParams.DiffCode)))
               {
                  CntrObjs[cntrResult.LangProperty.GetIndex()].CountSLOC(cntrResults, i);
               }

               // Print the progress to the screen for code counters
               progressVisualizer.printProgressBarWithThreshold(i / ((double) cntrResults.size()));
            }
            // Print the progress to the screen for code counters
            progressVisualizer.printProgressBarWithThreshold(ProgressVisualizer.DONE);

            endTime = TimeUtils.GetTime();
            TimeUtils.PrintElapsedTime(startTime, endTime, "PSLOC and LSLOC Counting");

            // Find duplicate code amongst files
            if (RtParams.SearchForDups && RtParams.DupThreshold > 0)
            {
               DuplicateFileFinder.FindDuplicateCode(cntrResults, RtParams.DupThreshold);
            }

            processed = true;

            // Save counter results in reports
            logger.debug("Generating counter reports...");
            System.out.println("Generating counter reports...");
            SaveCounterResults(cntrResults, cntrRprtr);
         }
      }
      else
      {
         System.err.println("Error processing counter request: file list is empty");
         logger.error("Error processing counter request: file list is empty");
      }

      return processed;
   }

   /**
    * Performs code differencer operation
    * 
    * @return True if differencer request was processed, false otherwise
    */
   private boolean ProcessDiffBaselinesReq()
   {
      boolean processed = false;

      if (RtParams.FileListA.size() > 0 && RtParams.FileListB.size() > 0)
      {
         if (DiffResults == null)
         {
            DiffResults = new ArrayList<DiffResultType>();
         }

         System.out.println("Processing baseline A counter request...");
         logger.debug("Processing baseline A counter request...");
         if (ProcessCountReq(RtParams.FileListA, CntrResults, CntrRprtr, Constants.BASELINE_A))
         {
            System.out.println("");
            System.out.println("");
            System.out.println("Processing baseline B counter request...");
            logger.debug("Processing baseline B counter request...");
            if (ProcessCountReq(RtParams.FileListB, CntrResultsB, CntrRprtrB, Constants.BASELINE_B))
            {
               long startTime = 0;
               long endTime = 0;

               // Difference the two baselines
               startTime = TimeUtils.GetTime();
               DiffObj.DiffBaselines(CntrResults, CntrResultsB, DiffResults, DupDiffResults);
               endTime = TimeUtils.GetTime();
               System.out.println("");
               System.out.println("");
               TimeUtils.PrintElapsedTime(startTime, endTime, "Baseline Differencer");

               processed = true;

               // Write differencer results to report(s)
               System.out.println("Generating differencer reports...");
               logger.debug("Generating differencer reports...");
               SaveDiffResults();
            }
            else
            {
               System.err.println("Error processing counter request for baseline B");
               logger.error("Error processing counter request for baseline B");
            }
         }
         else
         {
            System.err.println("Error processing counter request for baseline A");
            logger.error("Error processing counter request for baseline A");
         }
      }
      else
      {
         System.err.println("Error processing differencer request: invalid file list(s)");
         logger.error("Error processing differencer request: invalid file list(s)");
      }

      return processed;
   }

   /**
    * Sets user defined file extension(s) for given language(s). This will
    * override any built-in file extensions for the language(s).
    */
   private void SetUserExtMapping()
   {
      for (int i = 0; i < NumLangs; i++)
      {
         ArrayList<String> extensions = RtParams.FileExtToLangMap.get(LanguagePropertiesType.values()[i].toString());
         if (extensions != null)
         {
            LangProps[i].SetLangFileExts(extensions);
         }
      }
   }

   /**
    * Maps file extensions of files in the baseline to a language type
    * 
    * @param cntrResult
    *           An array list of UCCFile objects whose file extensions need to
    *           be mapped to appropriate language properties
    */
   private void MapFileExtToLang(ArrayList<UCCFile> cntrResult)
   {
      ArrayList<String> supportedExtensions = new ArrayList<String>();

      for (int i = 0; i < cntrResult.size(); i++)
      {
         // Retrieve the file's extension from its name
         String fileExt = FileUtils.GetFileExt(FileUtils.GetFileName(cntrResult.get(i).FileName)).toLowerCase().trim();

         // If a file extension exists, check if it maps to any of the defined
         // language file extensions
         if (RtParams.HndlClearCaseFiles)
         {
            // remove clear case info from file name extension (does not alter
            // actual filename)
            if (fileExt.contains("@@"))
            {
               fileExt = fileExt.substring(0, fileExt.indexOf("@"));
            }
         }

         if (fileExt != "" && !fileExt.isEmpty())
         {
            for (int k = 0; k < NumLangs; k++)
            {
               // Get all supported file extensions for the language
               supportedExtensions = LangProps[k].GetLangFileExts();

               for (int j = 0; j < supportedExtensions.size(); j++)
               {
                  if (supportedExtensions.get(j).toUpperCase().equals(fileExt.toUpperCase()))
                  {
                     cntrResult.get(i).LangProperty = LanguagePropertiesType.values()[k];
                     break; // Exit the loop once we find a match
                  }
               }
            }
         }
         else // Special case for Makefiles
         {
            String fileName = FileUtils.GetFileName(cntrResult.get(i).FileName).toLowerCase();
            if (fileName.contains("makefile"))
            {
               cntrResult.get(i).LangProperty = LanguagePropertiesType.MAKEFILE;
            }
         }
      }
   }

   /**
    * Generates various reports that contain count metrics
    * 
    * @param cntrResults
    *           Object containing code count results
    * @param cntrRprtr
    *           Object for code count report generation
    */
   private void SaveCounterResults(ArrayList<UCCFile> cntrResults, CounterReporter cntrRprtr)
   {
      cntrRprtr.GenCntrReports(cntrResults);
   }

   /**
    * Generates reports that contain differencer results
    */
   private void SaveDiffResults()
   {
      DiffRprtr.GenDiffReports(DiffResults, false);

      if (RtParams.SearchForDups)
      {
         DiffRprtr.GenDiffReports(DupDiffResults, true);
      }
   }
}
