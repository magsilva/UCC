package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * VBLanguageProperties class stores properties of VB programming language that
 * will be used to count different types of code metrics for source code written
 * in VB.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class VBLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize VB language properties
    */
   public VBLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("Visual Basic 6.0 / 1998");
      SetLangName("VB");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".vb")));
      SetCaseSensitive(true);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList()));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("'")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList()));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList("#")));
      SetCompilerDirKeywords(new ArrayList<String>(
               Arrays.asList("#Const", "#Else", "#ElseIf", "#End", "#ExternalSource", "#If", "#Region")));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"")));
      SetLineContChars(new ArrayList<String>(Arrays.asList("_")));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList("Function")));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("Do", "For", "While", "Do While", "Do Until")));
      SetCondKeywords(new ArrayList<String>(
               Arrays.asList("Case", "Do", "Else", "ElseIf", "For", "If", "Select", "While", "Do While", "Do Until")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("<>", ">", "<", ">=", "<=", "And", "AndAlso", "Not", "Or",
               "Xor", "Andalso", "Orelse", "Isfalse", "Istrue")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
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
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("-", "&", "&=", "*", "*=", "/", "/=", "\\", "\\=", "^", "^=",
               "+", "+=", "<<", "<<=", "=", "-=", ">>", ">>=")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("Log", "Log10")));
      SetTrigOpKeywords(new ArrayList<String>(
               Arrays.asList("Acos", "Asin", "Atan", "Atan2", "Cos", "Cosh", "Sin", "Sinh", "Tan", "Tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("Abs", "BigMul", "Ceiling", "DivRem", "Exp", "Floor",
               "IEEERemainder", "Max", "Min", "Pow", "Round", "Sign", "Sqrt", "Truncate")));
      SetExcludeKeywords(new ArrayList<String>(
               Arrays.asList("End If", "Else", "End Select", "Next", "Wend", "Do", "Loop", "End Sub")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList()));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList()));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }
}
