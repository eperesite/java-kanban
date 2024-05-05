package manager;

import task.Task;

public class Node {
    private Node previous; //ссылка на предыдущий элемент
    private Task object; // текущий объект
    private Node next; // ссылка на следующий элемент

    public Node(Node previous, Task object, Node next) {
        this.previous = previous;
        this.object = object;
        this.next = next;
    }

    public Node getPrevious() { // получить предыдущий элемент
        return previous;
    }

    public void setPrevious(Node previous) { // изменить предыдущий элемент
        this.previous = previous;
    }

    public Task getObject() { // получить текущий объект
        return object;
    }

    public void setObject(Task object) { // изменить текущий объект
        this.object = object;
    }

    public Node getNext() { // получить следующий элемент
        return next;
    }

    public void setNext(Node next) { // изменить следующий элемент
        this.next = next;
    }
}