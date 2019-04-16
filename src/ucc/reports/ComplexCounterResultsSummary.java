package ucc.reports;

import java.util.ArrayList;

import ucc.datatypes.CmplxDataType;

/**
 * ComplexCounterResultsSummary class contains summary of complexity counter
 * results for each file.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class ComplexCounterResultsSummary
{
   /** File name */
   public String FileName;

   /** Language name */
   public String LangName;

   /** Number of trigonometric operations */
   public int NumTrigOps;

   /** Number of logarithmic operations */
   public int NumLogOps;

   /** Number of calculation operations */
   public int NumCalcOps;

   /** Number of other math operations */
   public int NumMathOps;

   /** Number of conditional statements */
   public int NumCondStmts;

   /** Number of logical operations */
   public int NumLogicOps;

   /** Number of preprocessors */
   public int NumPreprocs;

   /** Number of assignment operations */
   public int NumAssignOps;

   /** Number of pointer operations */
   public int NumPntrOps;

   /** Number of data/reserverd operations */
   public int NumDataOps;

   /** Number of executable operations */
   public int NumExecOps;

   /**
    * Size of array list indicates loop level, value indicates count for that
    * level
    */
   public ArrayList<Integer> LoopLvlCnts;

   /** Number of unique operators */
   public int[] NumUniqueAndTotalOperators;

   /** Number of bool operands */
   public int NumBoolOperands;

   /** Number of char and string operands */
   public int NumCharStringOperands;

   /** Number of digit operands */
   public int NumDigitOperands;

   /** Number of word operands */
   public int NumWordOperands;

   /** Number of unique operators */
   public int[] NumUniqueAndTotalOperands;

   /**
    * Default constructor
    */
   public ComplexCounterResultsSummary()
   {
      FileName = "";
      LangName = "";
      NumTrigOps = 0;
      NumLogOps = 0;
      NumCalcOps = 0;
      NumMathOps = 0;
      NumCondStmts = 0;
      NumLogicOps = 0;
      NumPreprocs = 0;
      NumAssignOps = 0;
      NumPntrOps = 0;
      NumDataOps = 0;
      NumExecOps = 0;
      LoopLvlCnts = new ArrayList<Integer>(0);
      NumUniqueAndTotalOperators = new int[] { 0, 0 };
      NumBoolOperands = 0;
      NumCharStringOperands = 0;
      NumDigitOperands = 0;
      NumWordOperands = 0;
      NumUniqueAndTotalOperands = new int[] { 0, 0 };
   }

   /**
    * Copy constructor - Initialize class member variables with given parameters
    * 
    * @param fileName
    *           File Name
    * @param langName
    *           Language Name
    * @param numTrig
    *           Number of trigonometric operations
    * @param numLog
    *           Number of logarithmic operations
    * @param numCalc
    *           Number of calculations
    * @param numMath
    *           Number of math operations
    * @param numCond
    *           Number of conditional keyword occurrences
    * @param numLogic
    *           Number of logical keyword occurrences
    * @param numPreproc
    *           Number of preprocessors
    * @param numAssign
    *           Number of assignment operations
    * @param numPntr
    *           Number of pointers
    * @param loopCnts
    *           Number of loop levels
    */
   public ComplexCounterResultsSummary(String fileName, String langName, int numTrig, int numLog, int numCalc,
            int numMath, int numCond, int numLogic, int numPreproc, int numAssign, int numPntr, int numData,
            int numExec, ArrayList<CmplxDataType> loopCnts)
   {
      FileName = fileName;
      LangName = langName;
      NumTrigOps = numTrig;
      NumLogOps = numLog;
      NumCalcOps = numCalc;
      NumMathOps = numMath;
      NumCondStmts = numCond;
      NumLogicOps = numLogic;
      NumPreprocs = numPreproc;
      NumAssignOps = numAssign;
      NumPntrOps = numPntr;
      NumDataOps = numData;
      NumExecOps = numExec;
      LoopLvlCnts = new ArrayList<Integer>(0);
      SetLoopLvlCnts(loopCnts);
   }

   /**
    * Set LoopLvlCnts values to provided values
    * 
    * @param loopCnts
    *           loop level counts
    */
   private void SetLoopLvlCnts(ArrayList<CmplxDataType> loopCnts)
   {
      for (int i = 0; i < loopCnts.size(); i++)
      {
         LoopLvlCnts.add(new Integer(loopCnts.get(i).Count));
      }
   }
}
