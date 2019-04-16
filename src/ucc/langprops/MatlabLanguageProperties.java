package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * MatlabLanguageProperties class stores properties of Matlab programming
 * language that will be used to count different types of code metrics for
 * source code written in Matlab.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class MatlabLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Matlab language properties
    */
   public MatlabLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("R2016a (Version 9.0) - 3 Mar 2016");
      SetLangName("MATLAB");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".m")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("%")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("%{")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("%}")));
      SetCompilerDirChars(new ArrayList<String>());
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("import")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("...")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while", "parfor")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("elseif", "if", "switch", "case", "for", "while", "else")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(
               Arrays.asList("all", "break", "case", "catch", "continue", "for", "if", "else", "elseif", "end",
                        "otherwise", "parfor", "return", "switch", "try", "while", "classdef", "enumeration")));
      SetDataKeywords(new ArrayList<String>());
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", ".*", "^", ".^", "/", "./", "\\", ".\\")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "~=", "<=", ">=", "~", "<", ">", "&", "|")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10", "logm", "log1p", "log2", "reallog")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("cos", "sin", "tan", "sec", "csc", "cot", "arcsin",
               "arccos", "arctan", "arcsec", "arccsc", "arctan")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("exp", "sum", "max", "min", "ceil", "floor", "fix",
               "round", "sign", "rand", "randn", "mean", "median", "std", "conv", "deconv", "eig", "poly", "polyfit",
               "polyval", "roots", "interp1", "interp2", "spline", "unmkpp", "fmin", "fmins", "fzeros", "quad", "quad1",
               "trapz", "diff", "polyder", "ode23", "ode45", "ode113", "ode23s", "ode23t", "ode23b", "ode15s",
               "odeset")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("otherwise", "else", "end", "try")));
      SetExcludeCharacters(new ArrayList<String>());
      SetCyclCmplexKeywords(
               new ArrayList<String>(Arrays.asList("if", "elseif", "case", "while", "for", "catch", "parfor")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "catch", "switch", "for", "while")));
      SetPointerKeywords(new ArrayList<String>());
   }
}