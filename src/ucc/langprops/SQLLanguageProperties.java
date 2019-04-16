package ucc.langprops;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SQLLanguageProperties class stores properties of SQL programming language
 * that will be used to count different types of code metrics for source code
 * written in SQL.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class SQLLanguageProperties extends LanguageProperties
{
   /**
    * Default constructor to initialize SQL language properties
    */
   public SQLLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      SetLangVersion("SQL:2011 SO/IEC 9075");
      SetLangName("SQL");
      SetLangFileExts(new ArrayList<String>(Arrays.asList(".sql", ".SQL")));
      SetCaseSensitive(false);
      SetExecLineTermChars(new ArrayList<String>(Arrays.asList(";")));
      SetSingleLineCmntStartChars(new ArrayList<String>(Arrays.asList("#", "--")));
      SetMultiLineCmntStartChars(new ArrayList<String>(Arrays.asList("/*")));
      SetMultiLineCmntEndChars(new ArrayList<String>(Arrays.asList("*/")));
      SetCompilerDirChars(new ArrayList<String>(Arrays.asList()));
      SetCompilerDirKeywords(new ArrayList<String>(Arrays.asList()));
      SetQuoteStartChars(new ArrayList<String>(Arrays.asList("\"", "\'", "`")));
      SetLineContChars(new ArrayList<String>(Arrays.asList()));
      SetFuncStartKeywords(new ArrayList<String>(Arrays.asList()));
      SetLoopKeywords(new ArrayList<String>(Arrays.asList("loop", "while", "for")));
      SetCondKeywords(new ArrayList<String>(Arrays.asList("except", "group by", "having", "intersect", "join",
               "limit", "order by", "union", "where", "when", "else", "else if", "if", "for", "EXISTS", "DISTINCT",
               "ASC", "DESC")));
      SetLogicalOps(new ArrayList<String>(Arrays.asList("!=", "<>", ">", "<", ">=", "<=", "and", "or", "not", "like")));
      SetAssignmentOps(new ArrayList<String>(Arrays.asList("=")));
      SetOtherExecKeywords(new ArrayList<String>(Arrays.asList("alter", "close", "comment", "commit", "create",
               "declare", "delete", "deny", "drop", "except", "fetch", "grant", "group by", "having", "insert",
               "intersect", "join", "limit", "order by", "rename", "replace", "revoke", "rollback", "select", "set",
               "truncate", "union", "update", "where")));
      SetDataKeywords(new ArrayList<String>(Arrays.asList("bigint", "binary", "bit", "blob", "boolean", "byte", "char",
               "character", "date", "datetime", "decimal", "double", "enum", "float", "image", "int", "integer",
               "interval", "long", "longblob", "longtext", "mediumblob", "mediumint", "mediumtext", "memo", "money",
               "nchar", "ntext", "nvarchar", "numeric", "real", "single", "smalldatetime", "smallint", "smallmoney",
               "text", "time", "timestamp", "tinyint", "tinytext", "uniqueidentifier", "varbinary", "varchar",
               "year")));
      SetCalcKeywords(new ArrayList<String>(Arrays.asList("+", "-", "/", "*")));
      SetLogOpKeywords(new ArrayList<String>(Arrays.asList("ln", "log")));
      SetTrigOpKeywords(new ArrayList<String>(Arrays.asList("acos", "acosh", "asin", "asinh", "atan", "atan2", "atanh",
               "cos", "cosh", "sin", "sinh", "tan", "tanh")));
      SetOtherMathKeywords(new ArrayList<String>(Arrays.asList("abs", "avg", "ceil", "count", "exp", "floor", "max",
               "min", "mod", "power", "round", "sign", "sqrt", "stddev", "sum", "trunc", "variance")));
      SetExcludeKeywords(new ArrayList<String>(Arrays.asList("end", "else")));
      SetExcludeCharacters(new ArrayList<String>(Arrays.asList("\\{", "\\}", "\\(", "\\)")));
      SetCyclCmplexKeywords(new ArrayList<String>(Arrays.asList()));
      SetLslocKeywords(new ArrayList<String>(Arrays.asList("if", "for", "while", "loop")));
      SetPointerKeywords(new ArrayList<String>(Arrays.asList()));
   }

}
