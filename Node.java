public class Node<T> {
    int degree;
    T value;
    Node<T> parent;
    Node<T> child;
    Node<T> leftSibling;
    Node<T> rightSibling;
    boolean childCut;

    public Node(T t) {
        this.value = t;
        this.leftSibling = this;
        this.rightSibling = this;
    }

    public void addChild(Node<T> child) {
        child.leftSibling = child;
        child.rightSibling = child;

        // Update parent pointer
        child.parent = this;

        // Add child
        if(this.degree == 0) {
            this.child = child;
        } else {
            this.child.addSibling(child);
        }

        // Increment the degree
        this.degree += 1;
    }

    public void addSibling(Node<T> newSibling) {
        Node<T> oldSiblingRight = this.rightSibling;
        this.rightSibling = newSibling;
        newSibling.leftSibling = this;

        newSibling.rightSibling = oldSiblingRight;
        oldSiblingRight.leftSibling = newSibling;
    }
}