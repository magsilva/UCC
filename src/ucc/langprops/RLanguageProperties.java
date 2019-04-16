package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * JavaLanguageProperties class stores properties of Java programming language that will be used to count different
 * types of code metrics for source code written in Java.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class RLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize Java language properties
    */
   public RLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();
      SetLangVersion("R 3.4.2");
      SetLangName("R");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".R")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList("import","library","::",":::")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "'")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for", "while","repeat")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("if", "else", "else if", "ifelse", "switch", "for", "while")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "!=", "<", ">", "<=", ">=", "!", "&", "|","isTrue","&&","||","xor")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=","<-","<<-","->","->>")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("[","[[","@","$","break", "continue", "tryCatch","finally","return","self", "default")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("mode","length","vector","factor","array","matrix","data.frame","ts","list","read","scan","seq","rep","sequence"
      ,"gl","expand.grid")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("%%","%/%","^", "**", "+","-","*")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log","logb","log10","log2","log1p")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("cos","sin","tan","acos","asin","atan","atan2","cospi","sinpi","tanpi")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("dev.cur","dev.set","dev.off","split.screen","erase.screen","layout.show","plot"
    		  ,"sunflowerplot","pie","boxplot","stripchart","coplot","interaction.plot","dotchart","fourfoldplot","assocplot","mosaicplot","pairs","plot.ts","ts.plot","hist","barplot"
    		  ,"qqnorm","qqplot","contour","filled.contour","image","persp","stars","symbols","termplot","points","lines","text","mtext","segments","arrows","abline","rect","polygon","legend","title","axis","box","rug","locator","barchart","bwplot","densityplot","dotplot","histogram","qqmath","stripplot","qq"
    		  ,"xyplot","levelplot","contourplot","cloud","wireframe","splom","parallel","add1","drop1","step","anova","predict","update","print","summary","df.residual","coef","residuals","deviance","fitted","logLik","AIC"
    		  ,"adj","bg","bty","cex","col","font","las","lty","lwd","mar","mfcol","mfrow","pch","ps","pty","tck","tcl","xaxt","yaxt")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("else")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList("if", "else", "while", "for","tryCatch")));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "tryCatch", "switch", "for", "while","warning","error")));
   }
}
