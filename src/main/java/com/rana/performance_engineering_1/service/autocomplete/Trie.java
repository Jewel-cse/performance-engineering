package com.rana.performance_engineering_1.service.autocomplete;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Trie {

    private final TrieNode root;

    public Trie() {
        // The root node is empty
        this.root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie.
     */
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            // Get the child node for the current character
            // If it doesn't exist, create a new one
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        // Mark the last node as the end of a word
        current.isEndOfWord = true;
    }

    /**
     * Public-facing search method.
     * Returns all words in the Trie that start with the given prefix.
     */
    public List<String> search(String prefix) {
        List<String> suggestions = new ArrayList<>();

        // 1. Find the node that represents the end of the prefix
        TrieNode prefixNode = findNode(prefix);

        // 2. If the prefix doesn't exist, return an empty list
        if (prefixNode == null) {
            return suggestions;
        }

        // 3. If the prefix itself is a valid word, add it
        if (prefixNode.isEndOfWord) {
            suggestions.add(prefix);
        }

        // 4. Recursively find all words in the subtree below the prefix node
        collectAllWords(prefixNode, prefix, suggestions);

        return suggestions;
    }

    /**
     * Helper method to find the specific node for a given prefix.
     */
    private TrieNode findNode(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                // Prefix not found in the Trie
                return null;
            }
        }
        return current;
    }

    /**
     * Recursive helper to traverse the subtree and collect all words.
     */
    private void collectAllWords(TrieNode node, String currentPrefix, List<String> suggestions) {
        // Iterate over all children of the current node
        for (char c : node.children.keySet()) {
            TrieNode childNode = node.children.get(c);
            String newPrefix = currentPrefix + c;

            // If the child node is the end of a word, add it to the list
            if (childNode.isEndOfWord) {
                suggestions.add(newPrefix);
            }

            // Continue the search recursively down this path
            collectAllWords(childNode, newPrefix, suggestions);
        }
    }
}
