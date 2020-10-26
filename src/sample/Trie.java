package sample;

import java.util.*;

public class Trie {
    private class TrieNode {
        boolean isEndOfWord;
        Map<Character, TrieNode> children;  //Value(TrieNode): child of this node
        String html;

        TrieNode() {
            this.isEndOfWord = false;
            this.children = new HashMap<>();
            this.html = "";
        }
    }

    //Class Trie
    private TrieNode root;

    List<String> result = new ArrayList<>();

    public Trie() {
        root = new TrieNode();
    }

    public Trie(TrieNode root) {
        this.root = root;
    }

    public TrieNode getRoot() {
        return root;
    }

    public void setRoot(TrieNode root) {
        this.root = root;
    }

    public String combineChars(char[] chars, int level) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < level; ++i) {
            sb.append(chars[i]);
        }
        return sb.toString();
    }


    /**
     * chars : lưu các giá ký tự khi duyệt node hiện tại
     * khi chuyển sang node mới thì level tự reset,
     * các ký tự ở nhánh node sau sẽ ghi đè lên nhánh
     * node trước từ index = 0 trong mảng này
     * mỗi lần combine thì lấy được  1 word, duyệt hết trie thì
     * được các từ theo đúng thứ tự bảng chữ cái
     */
    public List<String> getAllWord(TrieNode parent, char[] chars, int level, String prefix) {
        if (parent != null) {
            if (parent.isEndOfWord) {
                String word = combineChars(chars, level);
                if (prefix.equals("")) {
                    result.add(word);
                } else {
                    result.add(prefix + word);
                }
            }
            Map<Character, TrieNode> children = parent.children;
            if (children.size() > 0) {
                //Set = Unsorted List
                Set<Character> keys = children.keySet();

                //Sort list chars
                List<Character> arr = new ArrayList<>(keys);
                Collections.sort(arr);

                for (char ch : arr) {
                    chars[level] = ch;
                    getAllWord(children.get(ch), chars, level + 1, prefix);
                }
            }
        }
        return result;
    }

    /** Inserts a word into the trie. */
    public void insert(String word, String html) {
        if (word == null || word.length() == 0) {
            return;
        }

        TrieNode iterator = root;
        for (int i = 0; i < word.length(); i++) {
            char cur = word.charAt(i);

            TrieNode child = iterator.children.get(cur); // Check if having a TrieNode associated with 'cur'
            if (child == null) {
                child = new TrieNode();
                iterator.children.put(cur, child);
            }
            iterator = child; // Navigate to next level
        }

        iterator.isEndOfWord = true;
        iterator.html = html;
    }

    /**
     * Returns true if the word is in the trie.
     * @param word - word to search.
     * @return true if its trie contains word
     */
    public boolean search(String word) {
        if (word == null) { // Assume that empty string is in the trie
            return false;
        }
        TrieNode iterator = root;

        for (int i = 0; i < word.length(); i++) {
            char cur = word.charAt(i);

            TrieNode child = iterator.children.get(cur); // Check if having a TrieNode associated with 'cur'
            if (child == null) { // null if 'word' is way too long or its prefix doesn't appear in the Trie
                return false;
            }

            iterator = child; // Navigate to next level
        }
        return iterator.isEndOfWord;
    }

    public int size(TrieNode node) {
        int result = 0;
        if (node.isEndOfWord) {
            result++;
        }
        Map<Character, TrieNode> children = node.children;
        for (Map.Entry<Character, TrieNode> entry : children.entrySet()) {
            result += size(entry.getValue());
        }
        return result;
    }

    public String getHtml(String word) {
        if (root == null) {
            return "";
        }
        if (search(word)) {
            TrieNode iterator = root;
            for (int i = 0; i < word.length(); ++i) {
                char key = word.charAt(i);
                iterator = iterator.children.get(key);
            }
            return iterator.html;
        }
        return "";
    }

    /** Returns true if there is any word in the trie that starts with the given prefix. */
    public List<String> startsWith(TrieNode node, String prefix) {
        int level = prefix.length();
        for (int i = 0; i < level; ++i) {
            TrieNode child = node.children.get(prefix.charAt(i));
            if (child == null) {
                return null;
            }
            node = child;
        }
        result.clear();
        getAllWord(node, new char[100], 0, prefix);
        return result;
    }

    /** Deletes a word from the trie if present, and return true if the word is deleted successfully. */
    public boolean delete(String word) {
        if (word == null || word.length() == 0) {
            return false;
        }

        // All nodes below 'deleteBelow' and on the path starting with 'deleteChar' (including itself) will be deleted if needed
        TrieNode deleteBelow = null;
        char deleteChar = '\0';

        // Search to ensure word is present
        TrieNode iterator = root;
        for (int i = 0; i < word.length(); ++i) {
            char cur = word.charAt(i);

            TrieNode child = iterator.children.get(cur);  // Check if having a TrieNode associated with 'cur'
            if (child == null) {  // null if 'word' is way too long or its prefix doesn't appear in the Trie
                return false;
            }

            // Update 'deleteBelow' and 'deleteChar'
            if (iterator.children.size() > 1 || iterator.isEndOfWord) {
                deleteBelow = iterator;
                deleteChar = cur;
            }

            iterator = child;
        }

        if (!iterator.isEndOfWord) { // word isn't in trie
            return false;
        }

        if (iterator.children.isEmpty()) {
            deleteBelow.children.remove(deleteChar);
        } else {
            iterator.isEndOfWord = false; // Delete word by mark it as not the end of a word
        }
        return true;
    }
}