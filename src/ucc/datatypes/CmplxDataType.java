package ucc.datatypes;

/**
 * CmplxDataType class defines a keyword/count pair data structure for use in
 * storing complexity and cylcomatic complexity results.
 * 
 * @author Integrity Applications Incorporated
 *
 */
public class CmplxDataType
{
   /** Keyword name */
   public String Keyword;

   /** Number of occurrences of keyword */
   public int Count;

   /**
    * Default constructor to initialize class member variables
    */
   public CmplxDataType()
   {
      Keyword = "";
      Count = 0;
   }

   /**
    * Constructor to initialize class member variables with given inputs
    * 
    * @param kw
    *           Keyword value
    */
   public CmplxDataType(String kw)
   {
      Keyword = kw;
      Count = 0;
   }
}
