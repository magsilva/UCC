package ucc.reports;

import java.util.ArrayList;

import ucc.datatypes.CmplxDataType;

/**
 * CounterResultSummary class contains summary counter results for each language
 * type.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class CounterResultSummary
{
   /** Version of the programming language used for counting rules */
   public String LangVersion;

   /** Number of total files */
   public int NumTotalFiles;

   /** Number of counted files */
   public int NumCountedFiles;

   /** Number of total lines */
   public int NumTotalLines;

   /** Number of blank lines */
   public int NumBlankLines;

   /** Number of full comment lines */
   public int NumWholeCmnts;

   /** Number of embedded comment lines */
   public int NumEmbeddedCmnts;

   /** Number of compiler directives */
   public int NumCompDirs;

   /** Number of physical data declaration lines */
   public int NumDataDeclPhys;

   /** Number of physical executable instruction lines */
   public int NumExecInstrPhys;

   /** Number of logical data declaration lines */
   public int NumDataDeclLog;

   /** Number of logical executable instruction lines */
   public int NumExecInstrLog;

   /** Number of physical source lines of code */
   public int NumPSLOC;

   /** Number of logical source lines of code */
   public int NumLSLOC;

   /** List of complexity keywords and counts pairs */
   public ArrayList<CmplxDataType> CmplxMathCnts;
   public ArrayList<CmplxDataType> CmplxTrigCnts;
   public ArrayList<CmplxDataType> CmplxLogCnts;
   public ArrayList<CmplxDataType> CmplxCalcCnts;
   public ArrayList<CmplxDataType> CmplxCondCnts;
   public ArrayList<CmplxDataType> CmplxLogicCnts;
   public ArrayList<CmplxDataType> CmplxPreprocCnts;
   public ArrayList<CmplxDataType> CmplxAssignCnts;
   public ArrayList<CmplxDataType> CmplxPntrCnts;

   /** List of keyword for logical reporting */
   public ArrayList<CmplxDataType> DataKeywrdCnts;
   public ArrayList<CmplxDataType> ExecKeywrdCnts;

   public ArrayList<CmplxDataType> BoolOperandCnts;

   /**
    * Constructor to initialize class member variables
    */
   public CounterResultSummary()
   {
      LangVersion = "";
      NumTotalFiles = 0;
      NumCountedFiles = 0;
      NumTotalLines = 0;
      NumBlankLines = 0;
      NumWholeCmnts = 0;
      NumEmbeddedCmnts = 0;
      NumDataDeclPhys = 0;
      NumExecInstrPhys = 0;
      NumDataDeclLog = 0;
      NumExecInstrLog = 0;
      NumPSLOC = 0;
      NumLSLOC = 0;

      CmplxMathCnts = new ArrayList<CmplxDataType>();
      CmplxTrigCnts = new ArrayList<CmplxDataType>();
      CmplxLogCnts = new ArrayList<CmplxDataType>();
      CmplxCalcCnts = new ArrayList<CmplxDataType>();
      CmplxCondCnts = new ArrayList<CmplxDataType>();
      CmplxLogicCnts = new ArrayList<CmplxDataType>();
      CmplxPreprocCnts = new ArrayList<CmplxDataType>();
      CmplxAssignCnts = new ArrayList<CmplxDataType>();
      CmplxPntrCnts = new ArrayList<CmplxDataType>();

      DataKeywrdCnts = new ArrayList<CmplxDataType>();
      ExecKeywrdCnts = new ArrayList<CmplxDataType>();

      BoolOperandCnts = new ArrayList<CmplxDataType>();
   }
}
