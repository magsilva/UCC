package ucc.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.DataTypes;
import ucc.langprops.*;

/**
 * This class exports language properties to a language properties file given a
 * language specified in MainUCC.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class PropertiesWriter
{
   /** Instantiate the Log4j logger for this class */
   private static final Logger logger = LogManager.getLogger(PropertiesWriter.class);

   /**
    * Writes a language's properties to an external language properties file
    * 
    * @param language
    *           The language whose properties will be exported
    * @param dir
    *           The user specified output directory
    * @return returns true if operation was successful, false otherwise
    */
   public static boolean Export(String language, String dir)
   {

      String name = language.toUpperCase();
      LanguageProperties langProps = null;
      boolean success = false;

      if (name.equals(DataTypes.LanguagePropertiesType.ADA.toString()))
      {
         langProps = new ADALanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.ASP.toString()))
      {
         langProps = new ASPLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.ASSEMBLY.toString()))
      {
         langProps = new AssemblyLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.BASH.toString()))
      {
         langProps = new BashLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.C_CPP.toString()))
      {
         langProps = new CCPPLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.CSHARP.toString()))
      {
         langProps = new CSharpLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.CSHELL.toString()))
      {
         langProps = new CShellLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.COLDFUSION.toString()))
      {
         langProps = new ColdFusionLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.COLDFUSION_SCRIPT.toString()))
      {
         langProps = new ColdFusionScriptLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.CSS.toString()))
      {
         langProps = new CSSLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.DOS_BATCH.toString()))
      {
         langProps = new DOSBatchLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.FORTRAN.toString()))
      {
         langProps = new FortranLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.HTML.toString()))
      {
         langProps = new HTMLLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.IDL.toString()))
      {
         langProps = new IDLLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.JAVA.toString()))
      {
         langProps = new JavaLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.JAVASCRIPT.toString()))
      {
         langProps = new JavaScriptLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.JSP.toString()))
      {
         langProps = new JSPLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.MAKEFILE.toString()))
      {
         langProps = new MakefileLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.MATLAB.toString()))
      {
         langProps = new MatlabLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.NEXTMIDAS.toString()))
      {
         langProps = new NextMidasLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.PASCAL.toString()))
      {
         langProps = new PascalLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.PERL.toString()))
      {
         langProps = new PerlLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.PHP.toString()))
      {
         langProps = new PHPLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.PYTHON.toString()))
      {
         langProps = new PythonLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.RUBY.toString()))
      {
         langProps = new RubyLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.SCALA.toString()))
      {
         langProps = new ScalaLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.SQL.toString()))
      {
         langProps = new SQLLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.VB.toString()))
      {
         langProps = new VBLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.VB_SCRIPT.toString()))
      {
         langProps = new VBScriptLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.VERILOG.toString()))
      {
         langProps = new VerilogLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.VHDL.toString()))
      {
         langProps = new VHDLLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.XMIDAS.toString()))
      {
         langProps = new XmidasLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.XML.toString()))
      {
         langProps = new XMLLanguageProperties();
      } 
      else if (name.equals(DataTypes.LanguagePropertiesType.R.toString())) {
         langProps = new RLanguageProperties();
      }
      else if (name.equals(DataTypes.LanguagePropertiesType.GO.toString()))
      {
         langProps = new GoLanguageProperties();
      }
      else
      {
         return success;
      }

      StringBuilder sb = new StringBuilder();
      String sep = System.getProperty("line.separator");

      sb.append("### This is a language properties file to use with UCC-G application. ###" + sep);
      sb.append("### Template formatting rules: ###" + sep);
      sb.append("### All comments (single and multi line) begin and end with \"###\". ###" + sep);
      sb.append("### The language property is to the left of the colon and the property's "
               + "values are to the right ###" + sep);
      sb.append("### No spaces in the property name. ###" + sep);
      sb.append("### A property can have zero or more values. ###" + sep);
      sb.append("### There must be only one property per line. ###" + sep);
      sb.append("### The default separator for property values is a comma. A different "
               + "separator can be specified using the \"ValueSeparator\" property ###" + sep);
      sb.append("### ValueSeperator must be the first property listed. ###" + sep + sep);

      sb.append("### Denotes the character(s) that seperate multiple values for a property ###" + sep);
      sb.append("ValueSeparator: " + "," + sep + sep);

      sb.append("### Denotes version number for the language ###" + sep);
      sb.append("LanguageVersion: " + langProps.GetLangVersion() + sep + sep);

      sb.append("### Name of the programming language ###" + sep);
      sb.append("LanguageName: " + langProps.GetLangName() + sep + sep);

      sb.append("### This property is only for custom language types ###" + sep);
      sb.append("### Select the most similar language from: BASH, C_CPP, CSHARP, CSHELL, FORTRAN ###" + sep);
      sb.append("### HTML, JAVA, MATLAB, MAKEFILE, PERL, PYTHON, SQL, VB, XMIDAS, XML ###" + sep);
      sb.append("SimilarTo: " + langProps.GetSimilarTo() + sep + sep);

      sb.append("### File extensions supported by the programming language ###" + sep);
      sb.append("LanguageFileExtensions: " + ToString(langProps.GetLangFileExts()) + sep + sep);

      sb.append("### Boolean for whether the language is case sensitive ###" + sep);
      sb.append("CaseSensitive: " + langProps.IsCaseSensitive() + sep + sep);

      sb.append("### Line termination character(s) for an executable instruction ###" + sep);
      sb.append("ExecutableLineTerminationCharacters: " + ToString(langProps.GetExecLineTermChars()) + sep + sep);

      sb.append("### Character(s) that denote beginning of a single line or embedded comment ###" + sep);
      sb.append("SingleLineCommentStartCharacters: " + ToString(langProps.GetSingleLineCmntStartChars()) + sep + sep);

      sb.append("### Character(s) that denote beginning of a multiple line or embedded comment ###" + sep);
      sb.append("MultiLineCommentStartCharacters: " + ToString(langProps.GetMultiLineCmntStartChars()) + sep + sep);

      sb.append("### Character(s) that denote end of a multiple line or embedded comment ###" + sep);
      sb.append("MultiLineCommentEndCharacters: " + ToString(langProps.GetMultiLineCmntEndChars()) + sep + sep);

      sb.append("### Character(s) for compiler directives ###" + sep);
      sb.append("CompilerDirectiveCharacters: " + ToString(langProps.GetCompilerDirChars()) + sep + sep);

      sb.append("### Keyword(s) for compiler directives ###" + sep);
      sb.append("CompilerDirectiveKeywords: " + ToString(langProps.GetCompilerDirKeywords()) + sep + sep);

      sb.append("### Character(s) that denote beginning of a string literal ###" + sep);
      sb.append("QuoteStartCharacters: " + ToString(langProps.GetQuoteStartChars()) + sep + sep);

      sb.append("### Character(s) that denote continuation of a line to the next line ###" + sep);
      sb.append("LineContinuationCharacters: " + ToString(langProps.GetLineContChars()) + sep + sep);

      sb.append("### Keyword(s) that denote beginning of a function/method ###" + sep);
      sb.append("FunctionStartSymbols/Keywords: " + ToString(langProps.GetFuncStartKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote a loop ###" + sep);
      sb.append("LoopStatementKeywords: " + ToString(langProps.GetLoopKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote a conditional statement ###" + sep);
      sb.append("ConditionStatementKeywords: " + ToString(langProps.GetCondKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote logical operators ###" + sep);
      sb.append("LogicalOperators: " + ToString(langProps.GetLogicalOps()) + sep + sep);

      sb.append("### Keyword(s) that denote assignment operators ###" + sep);
      sb.append("AssignmentOperators: " + ToString(langProps.GetAssignmentOps()) + sep + sep);

      sb.append("### Additional executable keyword(s) not covered anywhere else ###" + sep);
      sb.append("OtherExecutableKeywords: " + ToString(langProps.GetOtherExecKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote data declarations ###" + sep);
      sb.append("DataKeywords: " + ToString(langProps.GetDataKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote calculation operators ###" + sep);
      sb.append("CalculationKeywords: " + ToString(langProps.GetCalcKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote logarithmic operations ###" + sep);
      sb.append("LogOperationKeywords: " + ToString(langProps.GetLogOpKeywords()) + sep + sep);

      sb.append("### Keyword(s) that denote trigonometric operators ###" + sep);
      sb.append("TrigOperationKeywords: " + ToString(langProps.GetTrigOpKeywords()) + sep + sep);

      sb.append("### Additional mathematical operations keyword(s) not covered in above categories ###" + sep);
      sb.append("OtherMathKeywords: " + ToString(langProps.GetOtherMathKeywords()) + sep + sep);

      sb.append("### List of keyword(s) to exclude from count ###" + sep);
      sb.append("Exclude/IgnoreKeywords: " + ToString(langProps.GetExcludeKeywords()) + sep + sep);

      sb.append("### List of character(s) to exclude from count ###" + sep);
      sb.append("Exclude/IgnoreCharacters: " + ToString(langProps.GetExcludeCharacters()) + sep + sep);

      sb.append("### Keyword(s) for cyclomatic complexity count ###" + sep);
      sb.append("CyclomaticComplexityKeywords: " + ToString(langProps.GetCyclCmplexKeywords()) + sep + sep);

      sb.append("### Keyword(s) for logical source lines of code(LSLOC) count ###" + sep);
      sb.append("Lslockeywords: " + ToString(langProps.GetLslocKeywords()) + sep + sep);

      sb.append("### Keyword(s) for pointer ###" + sep);
      sb.append("PointerKeywords: " + ToString(langProps.GetPointerKeywords()) + sep + sep);

      BufferedWriter bw = null;
      String fileName = language + "LanguageProperties.txt";

      // Check if specified output directory exists. If it does not exist ,
      // create it.
      try
      {
         File file = new File(dir + fileName);
         file.getParentFile().mkdir();
         file.createNewFile();
         bw = new BufferedWriter(new FileWriter(file));
         bw.write(sb.toString());
         bw.close();
         return true;
      }

      catch (Exception e)
      {
         logger.error("Error: Could not create file in the specified directory");
         logger.debug(e);
         return false;
      }
   }

   /**
    * Returns String representation of array list with enclosing brackets
    * removed.
    * 
    * @param arrayList
    *           An ArrayList of language properties
    * @return A String representing the ArrayList without enclosing brackets
    */
   public static String ToString(ArrayList<String> arrayList)
   {
      String result = arrayList.toString();
      result = result.substring(1, result.length() - 1);
      return result;
   }
}