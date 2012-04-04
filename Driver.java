import java.util.*;
import java.io.*;

public class Driver
{
   public static void outputWordlist()
   {
      try
      {
         DictionaryTree dt = new DictionaryTree();
         Scanner in = new Scanner(new File("wordlist.txt"));
         while (in.hasNext())
         {
            String word = in.nextLine().trim().toLowerCase();
            dt.add(word);
         }
         System.out.print(dt.getTreeCode());
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args)
   {
      try
      {
         Scanner in = new Scanner(new File("words"));
         DictionaryTree dt = new DictionaryTree(in.next());
         
         for (String ending : dt.getPossibleEndings("chee"))
         {
            System.out.println(ending);
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
