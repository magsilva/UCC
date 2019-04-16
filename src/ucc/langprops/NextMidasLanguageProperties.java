package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * NextMidasLanguageProperties class stores properties of NeXtMidas macro
 * language that will be used to count different types of code metrics for
 * source code written in NeXtMidas.
 * 
 * NeXtMidas language properties are derived from Java language properties.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class NextMidasLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize NeXtMidas language properties
    */
   public NextMidasLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();
      SetLangVersion("NeXtMidas 3.7.1");
      SetLangName("NEXTMIDAS");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".mm")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("!")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("include")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'", "`")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("&")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("forall", "foreach", "do", "loop", "while")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("else", "elseif", "forall", "foreach", "do", "if", "loop", "trap", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("and", "or", "gt", "lt", "ge", "le", "eq", "eqs", "eqss", "ngt",
               "nlt", "nge", "nle", "neq", "neqs", "neqss")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("results")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("break", "call", "continue", "else", "elseif", "forall",
               "goto", "if", "loop", "pipe", "procedure", "return", "subroutine", "trap", "while", "jump")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("global")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("**", "+", "-", "*", "/")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("logarithm")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("waveform", "sincosine")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("calc", "fcalc", "fft", "firwind", "histogram", "maxmin",
               "peakpick", "ramp", "firhlbrt", "firparks", "hilbert", "imfft", "invfft", "magnitude", "marray", "mfft",
               "modulo", "morph", "mpoly", "multiply", "normalize", "parray", "passfilt", "phase", "polar", "polyeval",
               "polyfit", "polyphase", "pulse", "random", "sarray", "smooth", "spectra", "subtract", "transfft",
               "transform", "transpose")));
      SetExcludeKeywords(new ArrayList<String>(
               Arrays.asList("else", "endif", "endloop", "endwhile", "enddo", "endfor", "endmacro")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList()));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
