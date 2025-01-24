//-----------------------------------------------------
// Title: Binary Search Tree Implementation
// Author: Ege Yavuz
// ID: 14872032366
// Section: 1
// Assignment: 3
// Description: Implements a generic binary search tree with
// functionality for insertion, removal, and in-order traversal.
//-----------------------------------------------------


import java.util.function.Consumer;

public class BinarySearchTree<T extends Comparable<T>> {

    // Inner Class: Represents a single node in the binary search tree
    //--------------------------------------------------------
    // Summary: Stores the data and pointers to the left and right child
    // nodes of the binary search tree.
    //--------------------------------------------------------

    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node<T> root;

    // Constructor: Initializes an empty binary search tree
    //--------------------------------------------------------
    // Summary: Creates an empty binary search tree with a null root.
    // Precondition: None.
    // Postcondition: The tree is initialized and ready for use.
    //--------------------------------------------------------

    public BinarySearchTree() {
        this.root = null;
    }

    // Method: Inserts a new element into the binary search tree
    //--------------------------------------------------------
    // Summary: Adds a new node containing the specified data. If the
    // element already exists, its data is overwritten.
    // Precondition: The data must implement Comparable.
    // Postcondition: The tree contains the new element in the correct
    // position according to its natural order.
    //--------------------------------------------------------

    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node<T> insertRec(Node<T> root, T data) {
        if (root == null) {
            return new Node<>(data);
        }
        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        } else {
            // Overwrite existing node if the data is equal
            root.data = data;
        }
        return root;
    }

    // Method: Performs an in-order traversal of the tree
    //--------------------------------------------------------
    // Summary: Traverses the tree in-order and prints each node's data
    // in ascending order.
    // Precondition: The tree may be empty or contain elements.
    // Postcondition: The elements are printed in ascending order.
    //--------------------------------------------------------


    private void inOrderRec(Node<T> root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.println(root.data);
            inOrderRec(root.right);
        }
    }

    // Method: Performs an in-order traversal with a callback
    //--------------------------------------------------------
    // Summary: Traverses the tree in-order and applies the specified
    // action to each node's data.
    // Precondition: The action must be a valid Consumer function.
    // Postcondition: The action is applied to all elements in ascending order.
    //--------------------------------------------------------

    public void inOrderTraversal(Consumer<T> action) {
        inOrderRec(root, action);
    }

    private void inOrderRec(Node<T> root, Consumer<T> action) {
        if (root != null) {
            inOrderRec(root.left, action);
            action.accept(root.data); // Execute the action for the current node
            inOrderRec(root.right, action);
        }
    }

    // Method: Checks if the tree contains a specific element
    //--------------------------------------------------------
    // Summary: Searches for the specified data in the binary search tree.
    // Precondition: The data must implement Comparable.
    // Postcondition: Returns true if the element exists, otherwise false.
    //--------------------------------------------------------

    public boolean contains(T data) {
        return containsRec(root, data);
    }

    private boolean containsRec(Node<T> root, T data) {
        if (root == null) {
            return false;
        }
        if (data.compareTo(root.data) == 0) {
            return true;
        }
        if (data.compareTo(root.data) < 0) {
            return containsRec(root.left, data);
        } else {
            return containsRec(root.right, data);
        }
    }

    // Method: Removes an element from the binary search tree
    //--------------------------------------------------------
    // Summary: Deletes the specified node from the tree while maintaining
    // the binary search tree structure.
    // Precondition: The element must exist in the tree for successful removal.
    // Postcondition: The specified element is removed if it exists.
    //--------------------------------------------------------

    public void remove(T data) {
        root = removeRec(root, data);

    }

    private Node<T> removeRec(Node<T> root, T data) {
        if (root == null) {
            return null; // Element not found
        }

        //System.out.println("Visiting: " + root.data); // Debug output

        if (data.compareTo(root.data) < 0) {
            root.left = removeRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = removeRec(root.right, data);
        } else {
            // Element found: handle removal
            //System.out.println("Removing: " + root.data); // Debug output

            if (root.left == null) {
                return root.right; // Replace with right child
            } else if (root.right == null) {
                return root.left; // Replace with left child
            }

            // Node with two children: Replace with inorder successor
            root.data = findMin(root.right);
            root.right = removeRec(root.right, root.data);
        }
        return root;
    }


    // Helper Method: Finds the smallest element in a subtree
    //--------------------------------------------------------
    // Summary: Locates and returns the smallest value in the subtree
    // rooted at the specified node.
    // Precondition: The root node must not be null.
    // Postcondition: Returns the smallest value in the subtree.
    //--------------------------------------------------------

    private T findMin(Node<T> root) {
        T minValue = root.data;
        while (root.left != null) {
            root = root.left;
            minValue = root.data;
        }
        return minValue;
    }
}
