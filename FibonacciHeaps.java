
import java.util.*;

public class FibonacciHeaps<T> {
    private Node<T> min;
    private final Comparator<T> comparator;

    public FibonacciHeaps() {
        this.comparator = null;
    }

    public FibonacciHeaps(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public Node<T> insert(T t) {
        Node<T> newNode = new Node<T>(t);
        this.min = meld(this.min, newNode);
        return newNode;
    }

    public T getMin() {
        if (min == null) return null;
        return min.value;
    }

    public T removeMin() {
        if (min == null) return null;

        T minValue = min.value;

        removeNodeAndAddChildToTopLevel(min);

        pairWiseCombineFibonacciHeap();

        return minValue;
    }

    public T remove(Node<T> nodeToBeRemoved) {
        if (nodeToBeRemoved == min) return removeMin();

        T nodeToBeRemovedValue = nodeToBeRemoved.value;
        Node<T> nodeToBeRemovedParent = nodeToBeRemoved.parent;

        removeNodeAndAddChildToTopLevel(nodeToBeRemoved);

        // Cascading cut
        cascadingCut(nodeToBeRemovedParent);

        return nodeToBeRemovedValue;
    }

    public Node<T> meld(Node<T> node1, Node<T> node2) {
        if (node1 == null) return node2;
        if (node2 == null) return node1;

        Node<T> node1Sibling = node1.rightSibling;
        node1.rightSibling = node2.rightSibling;
        node1.rightSibling.leftSibling = node1;

        node2.rightSibling = node1Sibling;
        node1Sibling.leftSibling = node2;

        return getMinRoot(node1, node2);
    }

    public void decreaseKey(Node<T> targetNode, T newValue) {
        // (ToDO: Add parent check logic)

        targetNode.value = newValue;

        Node<T> targetNodeParent = targetNode.parent;
        if (targetNodeParent == null) {
            min = getMinRoot(min, targetNode);
        } else if (compare(targetNode.value, targetNodeParent.value) < 0) {
            // Remove node from the tree
            separateTreeRootedAt(targetNode);
            // Add to that top level tree
            min = meld(min, targetNode);
            // Cascading cut
            cascadingCut(targetNodeParent);
        }
    }

    private void separateTreeRootedAt(Node<T> nodeToBeRemoved) {
        // Remove node form the tree
        Node<T> nodeToBeRemovedParent = nodeToBeRemoved.parent;
        nodeToBeRemoved.parent = null;
        // Update child pointer if applicable.
        if (nodeToBeRemovedParent != null && nodeToBeRemovedParent.child == nodeToBeRemoved) {
            nodeToBeRemovedParent.child = nodeToBeRemoved.rightSibling == nodeToBeRemoved ? null : nodeToBeRemoved.rightSibling;
        }

        Node<T> leftSibling = nodeToBeRemoved.leftSibling;
        Node<T> rightSibling = nodeToBeRemoved.rightSibling;
        leftSibling.rightSibling = rightSibling;
        rightSibling.leftSibling = leftSibling;

    }

    private void cascadingCut(Node<T> node) {
        if(node == null || node.parent == null) return;
        if (node.childCut) {
            Node<T> nodeParent = node.parent;
            separateTreeRootedAt(node);
            meld(node, node);
            cascadingCut(nodeParent);
        } else {
            node.childCut = true;
        }
    }

    private Node<T> getMinRoot(Node<T> node1, Node<T> node2) {
        if (compare(node1.value, node2.value) < 0) {
            return node1;
        }
        return node2;
    }

    private int compare(T value1, T value2) {
        if (this.comparator == null)
            return ((Comparable) value1).compareTo(value2);
        return this.comparator.compare(value1, value2);
    }

    private void pairWiseCombineFibonacciHeap() {
        if (this.min == null) return;

        LinkedList<Node<T>> topLevelCircularList =  (LinkedList<Node<T>>) getTopLevelList();

        Map<Integer, Node<T>> treeTable = new HashMap<>();

        while (!topLevelCircularList.isEmpty()) {
            pairWiseCombineMinTree(treeTable, topLevelCircularList.removeFirst());
        }

        this.min = getNewFibonacciHeap(treeTable);
    }

    private void pairWiseCombineMinTree(Map<Integer, Node<T>> treeTable, Node<T> minTree) {
        if (treeTable.containsKey(minTree.degree)) {
            Node<T> treeWithSameDegree = treeTable.remove(minTree.degree);
            Node<T> mergedMinTree, childMinTree;

            if (compare(treeWithSameDegree.value, minTree.value) < 0) {
                mergedMinTree = treeWithSameDegree;
                childMinTree = minTree;
            } else {
                mergedMinTree = minTree;
                childMinTree = treeWithSameDegree;
            }

            mergedMinTree.addChild(childMinTree);
            mergedMinTree.leftSibling = mergedMinTree;
            mergedMinTree.rightSibling = mergedMinTree;

            pairWiseCombineMinTree(treeTable, mergedMinTree);
        } else {
            treeTable.put(minTree.degree, minTree);
        }
    }

    private void removeNodeAndAddChildToTopLevel(Node<T> nodeToBeRemoved) {
        Node<T> childStart = nodeToBeRemoved.child;
        Node<T> currentChild = childStart;

        if (nodeToBeRemoved == min) {
            min = min.rightSibling == min ? null : min.rightSibling;
        }

        separateTreeRootedAt(nodeToBeRemoved);

        if(currentChild != null) {
            do {
                currentChild.parent = null;
                currentChild = currentChild.rightSibling;
            } while (currentChild != childStart);
        }

        min = meld(childStart, min);
    }

    private List<Node<T>> getTopLevelList() {
        Node<T> start = min;
        Node<T> current = start;

        // Create a linked list to process all the min trees.
        LinkedList<Node<T>> topLevelCircularList = new LinkedList<>();
        do {
            topLevelCircularList.addLast(current);
            current = current.rightSibling;
        } while (current != start);

        return topLevelCircularList;
    }

    private Node<T> getNewFibonacciHeap(Map<Integer, Node<T>> treeTable) {
        LinkedList<Node<T>> minTreesList = new LinkedList<>(treeTable.values()) ;
        if (minTreesList.isEmpty()) return null;

        Node<T> newMin = null;

        while (!minTreesList.isEmpty()) {
            newMin = meld(newMin, minTreesList.removeFirst());
        }

        return newMin;
    }
}
