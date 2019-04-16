package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * FortranLanguageProperties class stores properties of Fortran programming
 * language that will be used to count different types of code metrics for
 * source code written in Fortran.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class FortranLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Fortran language properties
    */
   public FortranLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Free Form Fortran");
      SetLangName("FORTRAN");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".f90", ".F90", ".f95", ".F95", ".f03", ".F03", ".hpf", ".f",
               ".for", ".ftn", ".f77", ".F77")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("!")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("#")));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("#options", "#include", "#dictionary", "#ifdef",
               "#ifndef", "#define", "#undef", "#if", "#else", "#elif", "#endif", "!dir$&", "!dir$", "!$omp", "!$par&",
               "!$par", "!$pragma", "!$pragma sun", "!$pragma sparc", "!mic$&", "!mic$")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "\'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("&")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function", "program", "subroutine", "module")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("do")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("do", "if", "else", "else if", "elseif", "forall", "select case", "select type")));
      SetLogicalOps(new ArrayList<String>(
               Arrays.asList("==", ".eq.", "!=", ".ne.", "<", ".lt.", ">", ".gt.", "<=", ".le.", ">=", ".ge.", "&&",
                        ".and.", "||", ".or.", ".not.", ".neqv.", ".eqv.", ".true.", ".false.", "/=")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "continue", "goto", "return", "exit")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("allocate", "assign", "associate", "common", "complex",
               "character", "contains", "data", "deallocate", "dimension", "double precision", "enum", "equivalence",
               "external", "final", "function", "generic", "implicit", "import", "integer", "interface", "intrinsic",
               "logical", "module", "namelist", "nullify", "optional", "parameter", "program", "real", "reallocate",
               "recursive", "save", "select type", "subroutine", "type", "use")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "/", "*", "**")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log", "log10")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("acos", "acosh", "asin", "asinh", "atan", "atan2", "atanh",
               "cos", "cosh", "sin", "sinh", "tan", "tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("mod", "ceiling", "floor", "abs", "conjg", "dim",
               "dot_product", "dprod", "exp", "matmul", "max", "min", "modulo", "sign", "sqrt")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("endif", "else", "enddo", "end", "case default", "case")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else if", "case", "do while", "do", "where")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "switch", "do", "do while")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList("=>")));
   }
}
