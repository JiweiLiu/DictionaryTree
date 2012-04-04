import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.PrintStream;
import java.util.Stack;

public class DictionaryTree
{
   private class Node
   {
      public char letter;
      public boolean endsWord;
      public ArrayList<Node> children;
      
      public Node(char letter)
      {
         this.letter = letter;
         this.endsWord = false;
         this.children = new ArrayList<Node>();
      }
      
      public Node()
      {
         this.endsWord = false;
         this.children = new ArrayList<Node>();
      }
      
      public boolean add(String word)
      {
         if (word.length() == 0)
         {
            boolean wordAdded = !endsWord;
            endsWord = true;
            return wordAdded;
         }
         
         char first = word.charAt(0);
         
         for (Node child : children)
         {
            if (child.letter == first)
            {
               return child.add(word.substring(1));
            }
         }
         
         Node n = new Node(first);
         children.add(n);
         return n.add(word.substring(1));
      }
      
      public boolean contains(String word)
      {
         if (word.length() == 0) return endsWord;
         
         char first = word.charAt(0);
         
         for (Node child : children)
         {
            if (child.letter == first)
            {
               return child.contains(word.substring(1));
            }
         }
         return false;
      }
      
      public List<String> getPossibleEndings(String fragment)
      {
         if (fragment.length() == 0)
         {
            List<String> list = new ArrayList<String>();
            for (Node child : children)
            {
               list.addAll(child.getStrings());
            }
            return list;
         }
         
         char first = fragment.charAt(0);
         
         for (Node child : children)
         {
            if (child.letter == first)
            {
               return child.getPossibleEndings(fragment.substring(1));
            }
         }
         throw new IllegalArgumentException("Fragment does not start any words.");
      }
      
      public List<String> getStrings()
      {
         List<String> list = new ArrayList<String>();
         
         if (endsWord) list.add(Character.toString(letter));
         for (Node child : children)
         {
            for (String fragment : child.getStrings())
            {
               list.add(Character.toString(letter) + fragment);
            }
         }
         return list;
      }
      
      public List<String> getPermutations(String letters)
      {
         List<String> list = new ArrayList<String>();
         
         if (endsWord) list.add(Character.toString(letter));
         for (Node child : children)
         {
            int index = letters.indexOf(child.letter);
            if (index >= 0)
            {
               for (String fragment : child.getPermutations(removeLetter(letters, index)))
               {
                  list.add(Character.toString(letter) + fragment);
               }
            }
         }
         return list;
      }
      
      private String removeLetter(String string, int index)
      {
         return string.substring(0, index) + string.substring(index + 1);
      }
      
      /* Builds the code recursively */
      public void writeTreeCode(StringBuffer sb)
      {
         sb.append(endsWord ? Character.toUpperCase(letter) : letter);
         if (children.isEmpty()) return;
         sb.append('(');
         for (Node child : children) child.writeTreeCode(sb);
         sb.append(')');
      }
   }
   
   private Node root;
   
   public DictionaryTree()
   {
      root = new Node();
   }
   
   public DictionaryTree(String treeCode)
   {
      root = new Node();
      loadFromString(treeCode);
   }
   
   /* Builds a tree from the code in one pass */
   private void loadFromString(String treeCode)
   {
      Node c = root;
      Stack<Node> stack = new Stack<Node>();
      stack.push(root);
      char next;
      for (int i = 0; i < treeCode.length(); i++)
      {
         next = treeCode.charAt(i);
         switch (next)
         {
            case '(':
               stack.push(c);
               break;
            case ')':
               stack.pop();
               break;
            default:
               c = new Node(Character.toLowerCase(next));
               c.endsWord = Character.isUpperCase(next);
               stack.peek().children.add(c);
         }
      }
   }
   
   
   public boolean add(String word)
   {
      if (word == null) throw new IllegalArgumentException("Cannot add null.");
      if (word.matches("()")) throw new IllegalArgumentException("String cannot contain parentheses.");
      if (word.length() == 0) throw new IllegalArgumentException("String cannot be empty.");
      return root.add(word.toLowerCase());
   }
   
   public boolean contains(String word)
   {
      if (word == null) return false;
      return root.contains(word.toLowerCase());
   }
   
   public List<String> getPossibleEndings(String fragment)
   {
      if (fragment == null) throw new IllegalArgumentException("Fragment cannot be null");
      return root.getPossibleEndings(fragment);
   }
   
   public List<String> getPermutations(String letters)
   {
      if (letters == null) throw new IllegalArgumentException("Letters cannot be null");
      return root.getPermutations(letters);
   }
   
   public List<String> asList()
   {
      List<String> list = new ArrayList<String>();
      for (Node child : root.children)
      {
         list.addAll(child.getStrings());
      }
      return list;
   }
   
  public String getTreeCode()
  {
      StringBuffer sb = new StringBuffer();
     for (Node child : root.children) child.writeTreeCode(sb);
     return sb.toString();
  }
}
