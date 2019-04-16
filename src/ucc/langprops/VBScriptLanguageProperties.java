package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * VBScriptLanguageProperties class stores properties of VBScript programming
 * language that will be used to count different types of code metrics for
 * source code written in VBScript.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class VBScriptLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize VBScript language properties
    */
   public VBScriptLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("VBScript");
      SetLangName("VB_SCRIPT");
      // Embedded Ext: .hta, .htm, .html, .asp, .vbsphp, .vbshtm, .vbshtml,
      // .vbsxml, .vbsjsp, .vbsasps, .vbsaspc,
      // .vbscfm
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".vbs", ".vbe", ".wsf", ".wsc")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("'")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("#")));
      // Source:
      // http://stackoverflow.com/questions/1070698/any-other-preprocessor-directives-in-vbscript-classic-asp
      SetCompilerDirKeywords(
               new ArrayList<String>(Arrays.asList("#include", "#config", "#echo", "#fsize", "#flastmod", "#exec")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("_")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("Function", "Sub")));
      // IMPORTANT NOTE: For Each MUST precede For
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("for each", "for", "while", "until")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("elseif", "if", "select", "case", "for each", "for", "do while", "while", "do until")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("==", "<>", "<=", ">=", "<", ">", "and", "or", "not", "xor")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("="))); // "let" or
                                                                   // "set"?
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("Add", "AddHandler ", "AddressOf ", "Aggregate ",
               "Alias ", "Ansi ", "AppActivate", "As ", "Asc", "Assembly ", "Auto ", "Beep", "Binary ", "ByRef ",
               "ByVal ", "Call", "Case ", "Catch ", "CBool", "CByte ", "CChar ", "CCur", "CDate", "CDbl ", "CDec",
               "ChDir", "CInt", "Class ", "Clear", "CLng ", "Close", "CObj ", "Command", "Compare ", "Const ",
               "Continue ", "CreateObject", "CSByte ", "CShort ", "CSng ", "CStr", "CType ", "CUInt ", "CULng ",
               "CurDir", "CUShort ", "Custom ", "CVar", "Declare ", "Default ", "Delegate ", "Dim ", "Dir",
               "DirectCast ", "Distinct ", "DoEvents", "Each ", "Else", "ElseIf ", "End", "EndIf ", "Enum ", "Environ",
               "Equals ", "Erase ", "Error ", "Event ", "Exit", "Explicit ", "FALSE ", "FileAttr", "FileCopy",
               "FileDateTime", "FileLen", "Finally ", "Fix", "For", "Format", "FreeFile", "Friend ", "From ",
               "Function", "Function ", "Get", "GetAttr", "GetObject", "GetType ", "GetXMLNamespace ", "Global ",
               "GoSub", "GoTo ", "Group By ", "Group Join ", "Handles ", "If", "Implements ", "Imports ", "In ",
               "Inherits ", "Input", "InStr", "Interface ", "Into ", "Is ", "IsError", "IsFalse ", "IsNot ", "IsTrue ",
               "Join ", "Key ", "Kill", "Left", "Len", "Let ", "Lib ", "Like ", "Line", "Loc", "Lock", "LOF", "Loop",
               "Me ", "Mid", "MkDir", "Mod ", "Module ", "MustInherit ", "MustOverride ", "MyBase ", "MyClass ", "Name",
               "Namespace ", "Narrowing ", "New ", "Next", "Nothing ", "NotInheritable ", "NotOverridable ", "Now",
               "Of ", "Off ", "On", "Open", "Operator ", "Option ", "Optional ", "Order By ", "OrElse ", "Overloads ",
               "Overridable ", "Overrides ", "ParamArray ", "Partial ", "Preserve ", "Print", "Private ", "Property ",
               "Protected ", "Public ", "Put", "Raise", "RaiseEvent ", "Randomize", "ReadOnly ", "ReDim ", "REM ",
               "Remove", "RemoveHandler ", "Reset", "Resume", "Return ", "Right", "RmDir", "Rnd", "Seek", "Select",
               "Select ", "SendKeys", "Server", "Set ", "SetAttr", "Shadows ", "Shared ", "Shell", "Skip ",
               "Skip While ", "Spc", "Static ", "Step ", "Stop", "Str", "Strict ", "Structure ", "Sub", "Switch",
               "SyncLock ", "Tab", "Take ", "Take While ", "Text ", "Then ", "Throw ", "Timer", "To ", "TRUE ", "Try ",
               "TryCast ", "TypeOf ", "Unicode ", "Unlock", "Until ", "Using ", "Val", "Variant ", "Wend", "When ",
               "Where ", "While ", "Widening ", "Width", "With ", "WithEvents ", "Write", "WriteOnly ")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("Boolean", "Byte", "Char", "Date", "DateTime", "Decimal",
               "Double", "Int16", "Int32", "Int64", "Integer", "Long", "Object", "SByte", "Short", "Single", "String",
               "UInt16", "UInt32", "UInt64", "UInteger", "ULong", "User-Defined", "UShort", "ValueType")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", "^")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("log")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("atn", "cos", "sin", "tan")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "exp", "sqr", "randomize", "rnd")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("next", "loop", "do", "else")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(
               Arrays.asList("elseif", "if", "select", "case", "for each", "for", "do while", "while", "do until")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}