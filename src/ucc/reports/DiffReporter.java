package ucc.reports;

import java.util.ArrayList;

import ucc.datatypes.DiffResultType;
import ucc.main.RuntimeParameters;

/**
 * Abstract DiffReporter class that contains methods to print several types of
 * reports for users. Reports contain differencer metrics data.
 * <p>
 * Subclasses should implement the abstract methods to print reports in a
 * specific output format, for example, ASCII, CSV, etc.
 *
 * @author Integrity Applications Incorporated
 *
 */
public abstract class DiffReporter extends Reporter
{
   /** A handle to the Singleton class containing Runtime Parameters */
   protected RuntimeParameters RtParams = RuntimeParameters.GetInstance();

   /**
    * Default constructor
    */
   public DiffReporter()
   {
      super();
   }

   /**
    * Constructor to instantiate DiffReporter object with given parameters
    *
    * @param outFileExt
    *           Extension of output report file name
    * @param numLangs
    *           Total number of languages for report generation
    */
   public DiffReporter(String outFileExt, int numLangs)
   {
      super(outFileExt, numLangs);
   }

   /**
    * Constructor to instantiate Reporter object with given parameters
    *
    * @param outFileExt
    *           Extension of output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public DiffReporter(String outFileExt, String outDir, int numLangs)
   {
      super(outFileExt, outDir, numLangs);
   }

   /**
    * Constructor to instantiate DiffReporter object with given parameters
    *
    * @param outFilePrefix
    *           Prefix to prepend to output report file name
    * @param outFileExt
    *           Extension of output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public DiffReporter(String outFilePrefix, String outFileExt, String outDir, int numLangs)
   {
      super(outFilePrefix, outFileExt, outDir, numLangs);
   }

   /**
    * High-level method to generate all differencer reports
    *
    * @param diffResults
    *           An ArrayList of DiffResultType objects that contain differencer
    *           results to be used in creation of differencer reports
    * @param dupResults
    *           A boolean to indicate whether given diffResults are for
    *           duplicate files
    */
   public abstract void GenDiffReports(ArrayList<DiffResultType> diffResults, boolean dupResults);

   /**
    * Generates a report that contains matched file pairs of one baseline with a
    * second baseline.
    *
    * @param diffResults
    *           An ArrayList of DiffResultType objects that contain differencer
    *           results to be used in creation of differencer reports
    * @param dupResults
    *           A boolean to indicate whether given diffResults are for
    *           duplicate files
    */
   protected abstract void GenMatchedPairsReport(ArrayList<DiffResultType> diffResults, boolean dupResults);

   /**
    * Generates main differencer report with counts for new lines, modified
    * lines, unmodified lines, and deleted lines between two matched file pair.
    * 
    * @param diffResults
    *           An ArrayList of DiffResultType objects that contain differencer
    *           results to be used in creation of differencer reports
    * @param dupResults
    *           A boolean to indicate whether given diffResults are for
    *           duplicate files
    */
   protected abstract void GenMainDiffReport(ArrayList<DiffResultType> diffResults, boolean dupResults);

}
