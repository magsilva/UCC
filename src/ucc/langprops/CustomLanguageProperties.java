package ucc.langprops;

/**
 * CustomLanguageProperties class stores properties of a custom programming
 * language that will be provided by the user. This will be used to count
 * different types of code metrics for source code written in the custom
 * language.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class CustomLanguageProperties extends LanguageProperties
{

   /**
    * Default constructor to initialize Custom language properties
    */
   public CustomLanguageProperties()
   {
      // Call the super class' constructor to get default values/initializations
      // for the properties
      super();

      // All the properties will be set dynamically based on user provided
      // language properties text file
   }

}
