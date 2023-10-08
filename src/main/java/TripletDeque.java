import java.util.*;
import java.util.function.Consumer;

public class TripletDeque<E> implements Deque<E>, Containerable {

    public class DequeContainer {
        public Object[] container;
        private DequeContainer rightCont;
        private DequeContainer leftCont;
        public DequeContainer() {
            this.container = new Object[5];
        }
        public Object[] getContainer() {
            return this.container;
        }

        public DequeContainer getRightCont() {
            return rightCont;
        }
        public void setRightCont(DequeContainer rightCont) {
            this.rightCont = rightCont;
        }

        public DequeContainer getLeftCont() {
            return leftCont;
        }
        public void setLeftCont(DequeContainer leftCont) {
            this.leftCont = leftCont;
        }
    }

    private DequeContainer firstCont = new DequeContainer();

    private int head = 0;
    private int tail = 0;

    private int currentCont = 0;

    public void addContainer() {
        // Добавление нового контейнера
        DequeContainer lastCont = getCont(tail-1);

        if (lastCont != null) {
            DequeContainer newCont = new DequeContainer();
            newCont.setLeftCont(lastCont);
            lastCont.setRightCont(newCont);
            currentCont++;
        } else {
            firstCont = new DequeContainer();
        }
    }

    public void deleteContainer() {
        // Удаление пустого
        DequeContainer lastCont = getCont(tail-1);
        lastCont.setRightCont(null);
        currentCont--;
    }

    public DequeContainer getCont(int index) {
        if (index > ((currentCont+1)*5)) {
            throw new IndexOutOfBoundsException();
        } else {
            DequeContainer element = firstCont;
            for (int i = 0; i < index/5; i++) {
                element = element.getRightCont();
            }
            return element;
        }
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        if (cIndex > currentCont) {
            return null;
        } else {
            DequeContainer element = firstCont;
            for (int i = 0; i < cIndex; i++) {
                element = element.getRightCont();
            }
            if (element != null){
                return element.container;
            } else {
                return null;
            }
        }
    }

    /**
     * Inserts the specified element at the end of this deque.
     *
     * <p>This method is equivalent to {@link #add}.
     *
     * @param e the element to add
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();

        if (firstCont.container[0]==null){
            firstCont.container[0] = e;
        } else  {
            tail++;
            if (tail%5 == 0 && tail!=0){
                addContainer();
            }
            getCont(tail).container[tail-currentCont*5] = e;
        }
    }

    /**
     * Inserts the specified element at the front of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method {@link #offerFirst}.
     *
     * @param e the element to add
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     */
    @Override
    public void addFirst(E e) {
        if (e == null)
            throw new NullPointerException();

        if (firstCont.container[0]==null){
            firstCont.container[0] = e;
        } else {
            tail++;
            if (tail%5 == 0 && tail!=0) {
                addContainer();
            }
            for (int i = tail; i > 0; i--) {
                getCont(i).container[i-(i/5)*5] = getCont(i-1).container[((i-1))-(((i-1))/5)*5];
            }
            firstCont.container[0] = e;
        }
    }


    /**
     * Inserts the specified element at the front of this deque unless it would
     * violate capacity restrictions.  When using a capacity-restricted deque,
     * this method is generally preferable to the {@link #addFirst} method,
     * which can fail to insert an element only by throwing an exception.чё
     * @param e the element to add
     * @return {@code true} if the element was added to this deque, else
     * {@code false}

     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * Inserts the specified element at the end of this deque unless it would
     * violate capacity restrictions.  When using a capacity-restricted deque,
     * this method is generally preferable to the {@link #addLast} method,
     * which can fail to insert an element only by throwing an exception.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this deque, else
     * {@code false}
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * Retrieves and removes the first element of this deque.  This method
     * differs from {@link #pollFirst pollFirst} only in that it throws an
     * exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E removeFirst() {
        E e = pollFirst();
        if (e == null)
            throw new NoSuchElementException();
        return e;
    }

    /**
     * Retrieves and removes the last element of this deque.  This method
     * differs from {@link #pollLast pollLast} only in that it throws an
     * exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E removeLast() {
        E e = pollLast();
        if (e == null)
            throw new NoSuchElementException();
        return e;
    }

    /**
     * Retrieves and removes the first element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    @Override
    public E pollFirst() {
        E e = (E) firstCont.container[0];
        if (e != null) {
            firstCont.container[0] = null;
        }

        for (int i = 0; i < tail; i++) {
            getCont(i).container[i-(i/5)*5] = getCont(i+1).container[i+1-(i+1)/5*5];
        }
        getCont(tail).container[tail-(tail/5)*5] = null;

        if (tail%5 == 0 && tail!=0) {
            deleteContainer();
        }
        if (tail == 0) tail++;
        tail--;
        return e;

    }

    /**
     * Retrieves and removes the last element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the tail of this deque, or {@code null} if this deque is empty
     */
    @Override
    public E pollLast() {
        E e = (E) getCont(tail).container[tail-currentCont*5];
        if (e != null) {
            getCont(tail).container[tail-currentCont*5] = null;
        }

        if (tail%5 == 0 && tail!=0) {
            deleteContainer();
        }
        if (tail == 0) tail++;
        tail--;
        return e;
    }

    /**
     * Retrieves, but does not remove, the first element of this deque.
     * <p>
     * This method differs from {@link #peekFirst peekFirst} only in that it
     * throws an exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E getFirst() {
        E e = (E) firstCont.container[0];
        if (e == null)
            throw new NoSuchElementException();
        return e;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     * This method differs from {@link #peekLast peekLast} only in that it
     * throws an exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E getLast() {
        E e = (E) getCont(tail).container[tail-currentCont*5];
        if (e == null)
            throw new NoSuchElementException();
        return e;
    }

    /**
     * Retrieves, but does not remove, the first element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    @Override
    public E peekFirst() {
        return (E) firstCont.container[0];
    }

    /**
     * Retrieves, but does not remove, the last element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the tail of this deque, or {@code null} if this deque is empty
     */
    @Override
    public E peekLast() {
        return (E) getCont(tail).container[tail-currentCont*5];
    }

    /**
     * Removes the first occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the first element {@code e} such that
     * {@code Objects.equals(o, e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException   if the class of the specified element
     *                              is incompatible with this deque
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              deque does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        if (o != null) {
            for (int i = 0; i < tail; i++) {
                if (o.equals(getCont(i).container[i-(i/5)*5])) {
                    getCont(i).container[i-(i/5)*5] = null;

                    for (int j = i; j < tail; j++) {
                        getCont(j).container[j-(j/5)*5] = getCont(j+1).container[((j+1))-(((j+1))/5)*5];
                    }
                    getCont(tail).container[((tail))-(((tail))/5)*5] = null;

                    if (tail%5 == 0 && tail!=0) {
                        deleteContainer();
                    }
                    tail--;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes the last occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the last element {@code e} such that
     * {@code Objects.equals(o, e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws ClassCastException   if the class of the specified element
     *                              is incompatible with this deque
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              deque does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        if (o != null) {
            int last = 0;
            for (int i = 0; i < tail; i++) {
                if (o.equals(getCont(i).container[i-(i/5)*5])){
                    last = i;
                }
            }
            getCont(last).container[last-(last/5)*5] = null;
            for (int j = last; j < tail; j++) {
                 getCont(j).container[j-(j/5)*5] = getCont(j+1).container[((j+1))-(((j+1))/5)*5];
            }
            getCont(tail).container[((tail))-(((tail))/5)*5] = null;

            if (tail%5 == 0 && tail!=0) {
                deleteContainer();
            }
            tail--;
            return true;
        }
        return false;
    }

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an
     * {@code IllegalStateException} if no space is currently available.
     * When using a capacity-restricted deque, it is generally preferable to
     * use {@link #offer(Object) offer}.
     *
     * <p>This method is equivalent to {@link #addLast}.
     *
     * @param e the element to add
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws NullPointerException     if the specified element is null and this
     * element prevents it from being added to this deque
     */
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    /**
     * Inserts the specified element into the queue represented by this deque
     * (in other words, at the tail of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and {@code false} if no space is currently
     * available.  When using a capacity-restricted deque, this method is
     * generally preferable to the {@link #add} method, which can fail to
     * insert an element only by throwing an exception.
     *
     * <p>This method is equivalent to {@link #offerLast}.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this deque, else
     * {@code false}
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null element
     */
    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque).
     * This method differs from {@link #poll() poll()} only in that it
     * throws an exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #removeFirst()}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E remove() {
        return removeFirst();
    }

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), or returns
     * {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #pollFirst()}.
     *
     * @return the first element of this deque, or {@code null} if
     * this deque is empty
     */
    @Override
    public E poll() {
        return pollFirst();
    }

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque).
     * This method differs from {@link #peek peek} only in that it throws an
     * exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #getFirst()}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E element() {
        return getFirst();
    }

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque), or
     * returns {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #peekFirst()}.
     *
     * @return the head of the queue represented by this deque, or
     * {@code null} if this deque is empty
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * Adds all of the elements in the specified collection at the end
     * of this deque, as if by calling {@link #addLast} on each one,
     * in the order that they are returned by the collection's iterator.
     *
     * <p>When using a capacity-restricted deque, it is generally preferable
     * to call {@link #offer(Object) offer} separately on each element.
     *
     * <p>An exception encountered while trying to add an element may result
     * in only some of the elements having been successfully added when
     * the associated exception is thrown.
     *
     * @param c the elements to be inserted into this deque
     * @return {@code true} if this deque changed as a result of the call
     * @throws NullPointerException     if the specified collection contains a
     *                                  null element and this deque does not permit null elements,
     *                                  or if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::addLast);
        return true;
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this collection
     * @return {@code true} if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the {@code retainAll} operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not permit null
     *                                       elements
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }


    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear() {
        int amount = size();
        for (int i = 0; i < amount; i++) {
            pollLast();
        }
    }

    /**
     * Pushes an element onto the stack represented by this deque (in other
     * words, at the head of this deque) if it is possible to do so
     * immediately without violating capacity restrictions, throwing an
     * {@code IllegalStateException} if no space is currently available.
     *
     * <p>This method is equivalent to {@link #addFirst}.
     *
     * @param e the element to push
     * @throws NullPointerException     if the specified element is null and this
     *                                  deque does not permit null elements
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    /**
     * Pops an element from the stack represented by this deque.  In other
     * words, removes and returns the first element of this deque.
     *
     * <p>This method is equivalent to {@link #removeFirst()}.
     *
     * @return the element at the front of this deque (which is the top
     * of the stack represented by this deque)
     * @throws NoSuchElementException if this deque is empty
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    /**
     * Removes the first occurrence of the specified element from this deque.
     * If the deque does not contain the element, it is unchanged.
     * More formally, removes the first element {@code e} such that
     * {@code Objects.equals(o, e)} (if such an element exists).
     * Returns {@code true} if this deque contained the specified element
     * (or equivalently, if this deque changed as a result of the call).
     *
     * <p>This method is equivalent to {@link #removeFirstOccurrence(Object)}.
     *
     * @param o element to be removed from this deque, if present
     * @return {@code true} if an element was removed as a result of this call
     * @throws NullPointerException if the specified element is null and this
     *                              deque does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }


    /**
     * Returns {@code true} if this deque contains the specified element.
     * More formally, returns {@code true} if and only if this deque contains
     * at least one element {@code e} such that {@code Objects.equals(o, e)}.
     *
     * @param o element whose presence in this deque is to be tested
     * @return {@code true} if this deque contains the specified element
     * @throws NullPointerException if the specified element is null and this
     *                              deque does not permit null elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(Object o) {
        if (o != null) {
            for (int i = 0; i < tail; i++) {
                if (o.equals(getCont(i).container[i-(i/5)*5])) return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of elements in this deque.
     *
     * @return the number of elements in this deque
     */
    @Override
    public int size() {
        if (firstCont.container[0]==null){
            return 0;
        } else {
            return tail+1;
        }
    }

    /**
     * Returns {@code true} if this collection contains no elements.
     *
     * @return {@code true} if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return firstCont.container[0]==null;
    }

    /**
     * Returns an iterator over the elements in this deque in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this deque in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new TripletDeque.DeqIterator();
    }

    private class DeqIterator implements Iterator<E> {
        /** Index of element to be returned by subsequent call to next. */
        int cursor;

        /** Number of elements yet to be returned. */
        int remaining = size();

        /**
         * Index of element returned by most recent call to next.
         * Reset to -1 if element is deleted by a call to remove.
         */
        int lastRet = -1;

        DeqIterator() { cursor = head; }

        public final boolean hasNext() {
            return remaining > 0;
        }

        public E next() {
            if (remaining <= 0)
                throw new NoSuchElementException();

            E e = (E) getCont(cursor).container[cursor-(cursor/5)*5];
            cursor ++;
            remaining--;
            return e;
        }

        void postDelete(boolean leftShifted) {
            if (leftShifted)
                cursor--;
        }

        public final void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            postDelete(TripletDeque.this.remove(lastRet));
            lastRet = -1;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            int r;
            if ((r = remaining) <= 0)
                return;
            remaining = 0;
            if (getCont(cursor).container[cursor-(cursor/5)*5] == null || tail-cursor != r)
                throw new ConcurrentModificationException();
            for (int i = cursor; i < tail; i++) {
                action.accept((E) getCont(i).container[i-(i/5)*5]);
            }
        }
    }

    /**
     * Returns an iterator over the elements in this deque in reverse
     * sequential order.  The elements will be returned in order from
     * last (tail) to first (head).
     *
     * @return an iterator over the elements in this deque in reverse
     * sequence
     */
    @Override
    public Iterator<E> descendingIterator() {
        throw new UnsupportedOperationException();

    }


    //методы, не подлежащие реализации по заданию//

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).  After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param c collection containing elements to be removed from this collection
     * @return {@code true} if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the {@code removeAll} method
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not support
     *                                       null elements
     *                                       (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Returns {@code true} if this collection contains all of the elements
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return {@code true} if this collection contains all of the elements
     * in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              collection
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does not permit null
     *                              elements
     *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order. The returned array's {@linkplain Class#getComponentType
     * runtime component type} is {@code Object}.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * @return an array, whose {@linkplain Class#getComponentType runtime component
     * type} is {@code Object}, containing all of the elements in this collection
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It returns an array whose runtime type is {@code Object[]}.
     * Use {@link #toArray(Object[]) toArray(T[])} to reuse an existing
     * array, or use {@link //#toArray(IntFunction)} to control the runtime type
     * of the array.
     */
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     *
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * {@code null}.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any {@code null} elements.)
     *
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     *
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the same
     *          runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of any element in this
     *                              collection is not assignable to the {@linkplain Class#getComponentType
     *                              runtime component type} of the specified array
     * @throws NullPointerException if the specified array is null
     * @apiNote This method acts as a bridge between array-based and collection-based APIs.
     * It allows an existing array to be reused under certain circumstances.
     * Use {@link #toArray()} to create an array whose runtime type is {@code Object[]},
     * or use {@link //#toArray(IntFunction)} to control the runtime type of
     * the array.
     *
     * <p>Suppose {@code x} is a collection known to contain only strings.
     * The following code can be used to dump the collection into a previously
     * allocated {@code String} array:
     *
     * <pre>
     *     String[] y = new String[SIZE];
     *     ...
     *     y = x.toArray(y);</pre>
     *
     * <p>The return value is reassigned to the variable {@code y}, because a
     * new array will be allocated and returned if the collection {@code x} has
     * too many elements to fit into the existing array {@code y}.
     *
     * <p>Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

}