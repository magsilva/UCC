package ucc.utils;

import java.util.ArrayList;

/**
 * This class provides a list of methods to use for handling special cases of
 * arguments.
 */
public class ArgumentHandler
{
   /** no arguments provided condition **/
   public final static int NO_ARGS = -1;
   /** multiple (non-target) arguments provided condition **/
   public final static int MULTI_ARGS = -2;

   /**
    * This method find the indices of the targets in the list.
    * 
    * @param list
    *           (ArrayList&lt;String&gt;) - a general list of Strings
    * @param targets
    *           (String[]) - the Strings for which we are looking
    * @return the indices (if found) in list
    */
   public static ArrayList<Integer> find(ArrayList<String> list, String... targets)
   {
      ArrayList<Integer> indices = new ArrayList<Integer>();
      for (String indicator : targets)
      {
         indices.add(list.indexOf(indicator));
      }
      return indices;
   }

   /**
    * This method determines if an index is valid or not.
    * 
    * @param list
    *           (ArrayList&lt;String&gt;) - a list of indices
    * @return an indicator ArrayList
    */
   public static ArrayList<Integer> indicator(ArrayList<Integer> list)
   {
      ArrayList<Integer> indicators = new ArrayList<Integer>();
      int length = list.size();
      for (int i = 0; i < length; i++)
      {
         if (list.get(i) < 0)
         {
            indicators.add(i, 0);
         }
         else
         {
            indicators.add(i, 1);
         }
      }
      return indicators;
   }

   /**
    * This method verifies if any of the targets are found in the provided
    * argsList.
    * 
    * @param argsList
    *           (ArrayList&lt;String&gt;) - a list of arguments (command
    *           sequence)
    * @param targets
    *           (String[]) - the Strings for which we are looking
    * @return the index of the found target in argsList or some condition
    */
   public static int verifySoloArguments(ArrayList<String> argsList, String... targets)
   {
      ArrayList<Integer> indices = find(argsList, targets);
      ArrayList<Integer> indicators = indicator(indices);

      int index = -1;
      int sum = 0;
      int length = indicators.size();
      for (int i = 0; i < length; i++)
      {
         if (indicators.get(i) > 0)
         {
            index = indices.get(i);
         }
         sum += indicators.get(i);
      }

      if (sum == 0 && argsList.size() == 0)
      {
         return NO_ARGS;
      }
      else if (sum == 1 && argsList.size() == 1)
      {
         return index;
      }
      else
      {
         return MULTI_ARGS;
      }
   }
}