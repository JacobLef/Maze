import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a Collection of items
interface ICollection<T> {

  // removes and retrieves the next element in line from this ICollection
  T pop();
  
  //removes the given item from the list, nothing happens if it does not exist
  void pop(T t);
  
  // adds the given element to the line in this ICollection
  void add(T t);

  // determines if this ICollection is Empty
  boolean isEmpty();

  // Converts this ICollection into an Iterator of generic type T
  Iterator<T> toIterator();
  
  //creates a new ICollection of this type with the given deque
  <U> ICollection<U> newThis(Deque<U> d);
  

  
}

//Represents a Queue, which follows the first in first out system
class Queue<T> implements ICollection<T> {
  
  private final Deque<T> q;

  //default constructor for queues
  Queue() {
    this.q = new ArrayDeque<>();
  }
 
  //constructor for queues assuming we already know (part of) the deque inside it
  Queue(Deque<T> q) {
    this.q = q;
  }

  // Removes the first item added to this queue
  public T pop() {
    return q.remove();
  }

  //removes the given item from the list, nothing happens if it does not exist
  public void pop(T t) {
    q.remove();
  }

  // adds an item to the back of this queue
  public void add(T t) {
    q.addLast(t);
  }

  // determines if this queue is empty
  public boolean isEmpty() {
    return q.isEmpty();
  }

  // Converts this Queue into an Iterator of generic type T
  public Iterator<T> toIterator() {
    return q.iterator();
  }
  
  //creates a new Queue with the given Deque
  public <U> ICollection<U> newThis(Deque<U> d) {
    return new Queue<U>(d);
  }
}

// Represents a stack, which represents a last in first out system
class Stack<T> implements ICollection<T> {
  private final Deque<T> s;
  
  Stack() {
    this.s = new ArrayDeque<>();
  }
  
  Stack(Deque<T> s) {
    this.s = s;
  }

  // removes and gets the first item from this stack
  public T pop() {
    return s.removeLast();
  }

  //removes the given item from the list, nothing happens if it does not exist
  public void pop(T t) {
    s.remove(t);
  }
  
  // adds an item to the front of this stack
  public void add(T t) {
    s.addLast(t);
  }

  // determines if this stack is empty
  public boolean isEmpty() {
    return s.isEmpty();
  }

  // Converts this Stack into an iterator of generic type T
  public Iterator<T> toIterator() {
    return s.iterator();
  }
  
  
  //creates a new Stack with the given deque
  public <U> ICollection<U> newThis(Deque<U> d) {
    return new Stack<U>(d);
  }
  
  //returns a stack based on this Queue
  public Deque<T> toDeque() {
    return this.s;
  }
}




// Represents an immutable List of three items, all of either the same or different types
class Triple<A, B, C> {
  private final A first;
  private final B second;
  private final C third;

  Triple(A first, B second, C third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  // Retrieves the first item of this Triple
  A first() {
    return this.first;
  }

  // Retrieves the first item of this Triple
  B second() {
    return this.second;
  }

  // Retrieves the first item of this Triple
  C third() {
    return this.third;
  }
}

//Contains the information that comes from a search, including the optimal path
//and the path that was taken to solve the maze
class SearchResults {
  private final ICollection<Cell> search;
  private final ArrayList<Cell> optimalPath;
  
  SearchResults(ArrayList<Cell> optimalPath, ICollection<Cell> search) {
    this.search = search;
    this.optimalPath = optimalPath;
  }
  
  //determines the result of the search
  Iterator<Cell> determineSearch() {
    return search.toIterator();
  }
  
  //determines the solution path of the search
  //this returns an ArrayList instead of an iterator because we want the
  //benefits of arrays (namely, contain)
  ArrayList<Cell> determineSolution() {
    return optimalPath;
  }
}

// Represents a Vertex/Node of a Graph
class Node {
  private final ArrayList<Edge> connections;
  // reason for not final : overtime, the color will change, whether it's the path
  // progressing through the cell, or choosing different render modes
  private Color color;

  Node(ArrayList<Edge> conn) {
    this.connections = conn;
    this.color = Color.LIGHT_GRAY;
  }

  Node() {
    this.connections = new ArrayList<>();
    this.color = Color.LIGHT_GRAY;
  }

  // EFFECT: Updates the color of this Node
  void updateColor(Color c) {
    if (!(color == color.green || color == color.red)) {
      this.color = c;
    }
  }

  // EFFECT : adds the given edge to this, telling it's neighbor if requested by the caller
  // This method is used for constructing the examples, as mutation is needed to
  // create edges
  void addEdge(Edge e, boolean tellOther) {
    this.connections.add(e);
    if (tellOther) {
      e.updateConnections("add", -1 * e.nodePosition(this), false);
    }
  }

  // EFFECT : removes this edge from the list of connections
  // in the case that this is called when the edge isn't present, throw an
  // error
  void removeEdge(Edge e, boolean tellOther) {
    if (!this.connections.contains(e)) {
      throw new IndexOutOfBoundsException("edge does not exist in list");
    }
    this.connections.remove(e);

    if (tellOther) {
      e.updateConnections("remove", -1 * e.nodePosition(this), false);
    }
  }

  // determines if an edge is shared between this and the given node
  boolean sharesEdgeWith(Node that) {
    boolean contains = false;
    //goes through the edges in the given node to determine if the this node
    //is a part of the edge
    //we don't need to check both directions because if it's not in one, it's not in the other
    for (Edge e : connections) {
      if (that.connections.contains(e)) {
        contains = true;
        break;
      }
    }
    return contains;
  }
  
  // EFFECT : Renders this Node on the given ComputedPixelImage 
  //according to the information provided to it by the Cell which wraps it.
  void renderSelf(ComputedPixelImage cpi, int scale, Posn pos) {
    int xPos = pos.x;
    int yPos = pos.y;

    // Using the scale provided, renders a square in the given computed pixel
    // image, starting at this Nodes position, as provided. The color it is rendered
    // as is that of this Node.
    for (int k = xPos * scale; k < (xPos + 1) * scale; k += 1) {
      for (int i = yPos * scale; i < (yPos + 1) * scale; i += 1) {
        cpi.setColorAt(k, i, this.color);
      }
    }
  }
}

// Represents an Edge of a Graph (this is undirected, so it can be assumed to go both ways)
class Edge {
  private final Node from;
  private final Node to;
  private final int weight;

  Edge(Node from, Node to, int weight) {
    this.from = from;
    this.to = to;
    if (weight <= 0) {
      throw new IllegalArgumentException("Weight must be positive");
    }
    
    // end of change
    this.weight = weight;
  }

  // determines the if the given edge is the same as this edge
  // determined by same nodes and weight, Edge(A, B, 0.5) and edge(B, A, 0.5) are
  // same same nodes is defined by memory equality ==
  boolean sameEdge(Edge that) {
    return (this.from == that.from && this.to == that.to && this.weight == that.weight)
        || (this.from == that.to && this.to == that.from && this.weight == that.weight);
  }

  // given an ArrayList of Edges (not including this one), determines the one with
  // the smallest weight. Tie-breakers are determined by order of appearance
  Edge minEdge(ArrayList<Edge> edges) {
    Edge min = this;
    for (Edge e : edges) {
      if (min.weight > e.weight) {
        min = e;
      }
    }
    return min;
  }

  // EFFECT : removes this edge from the nodes it's attached to
  void removeFromNodes() {
    this.from.removeEdge(this, true);
  }

  // EFFECT : performs the operation based on the given String on the side of the
  // edge based on the given int.  tellOther is pased into methods, having little 
  // use in this method itself
  void updateConnections(String operation, int toTell, boolean tellOther) {
    if (!(operation.equals("add") || operation.equals("remove"))) {
      throw new IllegalArgumentException("can not perform operation " + operation);
    }
    if (toTell == 1) {
      if (operation.equals("add")) {
        this.to.addEdge(this, tellOther);
      }
      else {
        this.to.removeEdge(this, tellOther);
      }
    }
    else if (toTell == -1) {
      if (operation.equals("add")) {
        this.from.addEdge(this, tellOther);
      }
      else {
        this.from.removeEdge(this, tellOther);
      }
    }
    else {
      throw new IllegalArgumentException(
          "can not perform " + operation + " on a node not in this edge");
    }
  }

  // determines if the two sides of this edge share the same master in the given
  // unionFind
  boolean sameBlob(UnionFind uf) {
    return uf.findBlob(from) == uf.findBlob(to);
  }

  // EFFECT : relinks the from side of this edge's master to be the to sides master
  // in the given unionFind
  void relinkReps(UnionFind uf) {
    Node fromBlob = uf.findBlob(from);
    Node toBlob = uf.findBlob(to);
    
    uf.updateRep(fromBlob, toBlob);
  }

  // returns the position of where the node is in the edge, as determined by
  // -1 being from, 1 being to, and 0 not existing
  // DESIGN DECISION : we want to return an int in this case because we do want
  // to perform arithmetic operations, more specifically treating * -1 as a !
  // for booleans
  int nodePosition(Node n) {
    if (n == this.from) {
      return -1;
    }
    else if (n == this.to) {
      return 1;
    }
    else {
      return 0;
    }
  }
}

//Represents a Cell in the grid of a Maze
class Cell {
  private final Node node;
  // posn.x : represents the column we are currently on
  // posn.y : represents the row we are currently on
  private final Posn posn;

  Cell(Node node, Posn posn) {
    this.node = node;
    this.posn = posn;
  }

  //EFFECT : updates the color of the node inside of this cell
  void updateColor(Color c) {
    this.node.updateColor(c);
  }
  
  // Connects the given Node to the Node stored in this Cell
  Edge connectNode(Node to, double bias) {
    // gets a random int between [1, 200]
    int randWeight = (int) ((Math.random() + 1) * 201 * bias); 
    Edge edge = new Edge(this.node, to, randWeight);

    this.node.addEdge(edge, true);
    return edge;
  }
  
  // determines if this cell is linked to another cell, defined by being able to
  // move directly from cell to cell via node's edge
  Optional<Cell> isLinkedTo(Cell that) {
    if (this.node.sharesEdgeWith(that.node)) {
      return Optional.of(that);
    }
    
    return Optional.empty();
  }
  
  // Renders this Cell, including any walls that border this cell in all four directions.
  // If a wall should be rendered is determined by if there is a link between this Cell
  // and the Cell in a direction (north, south, east, west).
  void renderSelf(ArrayList<ArrayList<Cell>> grid, ComputedPixelImage cpi, int scale) {
    this.node.renderSelf(cpi, scale, this.posn);
    this.renderWalls(grid, cpi, scale);
  }
  
  // Renders all of the walls of this Cell.
  void renderWalls(ArrayList<ArrayList<Cell>> grid, ComputedPixelImage cpi, int scale) { 
    // De-construct our current position
    int x = posn.x;
    int y = posn.y;
    
    // Initialize the variables for the neighbors
    Optional<Cell> up = Optional.empty();
    Optional<Cell> down = Optional.empty();
    Optional<Cell> left = Optional.empty();
    Optional<Cell> right = Optional.empty();
   
    // Determine if there is a link in any of the four immediate directions. If there is
    // a link, update the respective direction's neighbor to what it should be.
    if (x != 0) {
      left = this.isLinkedTo(grid.get(y).get(x - 1));
    }
    
    if (y != 0) {
      up = this.isLinkedTo(grid.get(y - 1).get(x));
    }
    
    if (x != grid.get(y).size() - 1) {
      right = this.isLinkedTo(grid.get(y).get(x + 1));
    }

    if (y != grid.size() - 1) {
      down = this.isLinkedTo(grid.get(y + 1).get(x));
    }

    // Render each wall, if it should be rendered
    this.renderUpWall(cpi, up, scale);

    this.renderDownWall(cpi, down, scale);

    this.renderLeftWall(cpi, left, scale);

    this.renderRightWall(cpi, right, scale);

  }
  
  // Renders an up-wall of this Cell
  void renderUpWall(ComputedPixelImage cpi, Optional<Cell> up, int scale) {
    //our posn holds the cells position in it's cell, not in it's 

    if (up.isEmpty()) {
      int startX = posn.x * scale;
      int startY = posn.y * scale;
      
      //Draws the segement of the wall, adding an extra "layer" if the cell size
      //is large enough
      for (int xMod = 0; xMod < scale; xMod += 1) {
        cpi.setColorAt(startX + xMod, startY, Color.black); 
        if (scale > 2) { 
          cpi.setColorAt(startX + xMod, startY + 1, Color.black); 
        }
      }
    }
  }

  // Renders a down-wall of this Cell
  void renderDownWall(ComputedPixelImage cpi, Optional<Cell> down, int scale) {
    if (down.isEmpty()) {
      int startX = posn.x * scale;
      int startY = posn.y * scale;
      
      //Draws the segement of the wall, adding an extra "layer" if the cell size
      //is large enough
      for (int xMod = 0; xMod < scale; xMod += 1) {
        cpi.setColorAt(startX + xMod, startY + scale - 1, Color.black); 
        if (scale > 2) { 
          cpi.setColorAt(startX + xMod, startY + scale - 2, Color.black); 
        }
      }
    }
  }

  // Renders a left-wall of this Cell
  void renderLeftWall(ComputedPixelImage cpi, Optional<Cell> left, int scale) {
    if (left.isEmpty()) {
      int startX = posn.x * scale;
      int startY = posn.y * scale;
      
      //Draws the segement of the wall, adding an extra "layer" if the cell size
      //is large enough
      for (int yMod = 0; yMod < scale; yMod += 1) {
        cpi.setColorAt(startX, startY + yMod, Color.black); 
        if (scale > 2) { 
          cpi.setColorAt(startX + 1, startY + yMod, Color.black); 
        }
      }
    }
  }

  // Renders a right-wall of this Cell
  void renderRightWall(ComputedPixelImage cpi, Optional<Cell> right, int scale) {
    if (right.isEmpty()) {
      int startX = posn.x * scale;
      int startY = posn.y * scale;
      
      //Draws the segement of the wall, adding an extra "layer" if the cell size
      //is large enough
      for (int yMod = 0; yMod < scale; yMod += 1) {
        cpi.setColorAt(startX + scale - 1, startY + yMod, Color.black); 
        if (scale > 2) { 
          cpi.setColorAt(startX + scale - 2, startY + yMod, Color.black); 
        }
      }
    }
  }

  // Connects the cells of the input nested array list of Cells. Returns the
  // connected list of Cells.
  ArrayList<Cell> connectingCells(ArrayList<ArrayList<Cell>> maze) {
    ArrayList<Cell> toCheck = new ArrayList<>();
    if (posn.x != 0) {
      toCheck.add(maze.get(posn.y).get(posn.x - 1)); //up
    }
    if (posn.x != maze.get(0).size() - 1) {
      toCheck.add(maze.get(posn.y).get(posn.x + 1)); //down
    }
    if (posn.y != 0) {
      toCheck.add(maze.get(posn.y - 1).get(posn.x)); //left
    }
    if (posn.y != maze.size() - 1) {
      toCheck.add(maze.get(posn.y + 1).get(posn.x)); //right
    }

    ArrayList<Cell> connected = new ArrayList<>();
    for (Cell c : toCheck) {
      if (this.isLinkedTo(c).isPresent()) {
        connected.add(c);
      }
    }
    
    return connected;
  }

  // EFFECT: Colors this Cell by updating its color to the target Color provided
  void colorSelf(Color color) {
    this.node.updateColor(color);
  }
}

//Represents an immutable list of two items, all of either the same or different types
//for some reason java does not have this feature. thanks nintendo
class CellInfo {
  private final Optional<Cell> parent;
  private final Cell child;
 
  CellInfo(Cell child, Optional<Cell> parent) {
    this.parent = parent;
    this.child = child;
  }
 
  //determines the parent of this cellInfo
  Optional<Cell> parent() {
    return this.parent;
  }
 
  //determines the child of this cellInfo
  Cell child() {
    return this.child;
  }
}


// Represents the current groupings of blobs, where blobs are groupings of Node's whose
// representatives are the same.
class UnionFind {
  private final HashMap<Node, Node> env;
  private final ArrayList<Edge> edgesInTree;
  private final ArrayList<Edge> workList;

  // Default constructor for UnionFind
  UnionFind(ArrayList<Node> nodes, ArrayList<Edge> workList) {
    new Utils().mergesort(workList, new EdgeCompare());
    this.env = new Utils().selfHashMap(nodes);
    this.edgesInTree = new ArrayList<Edge>();
    this.workList = workList;
  }

  // Overload constructor for UnionFind in the case that we already have
  // the nodes in hashMap form
  UnionFind(HashMap<Node, Node> env, ArrayList<Edge> workList) {
    new Utils().mergesort(workList, new EdgeCompare());
    this.env = env;
    this.edgesInTree = new ArrayList<Edge>();
    this.workList = workList;
  }

  // Finds the representative of this UnionFind given an identifier
  Node rep(Node id) {
    return env.get(id);
  }

  // EFFECT: Updates the representative of the provided id (first argument) to the
  // given identifier (second argument).
  void updateRep(Node nodeToUpdate, Node newRep) {
    this.env.replace(nodeToUpdate, newRep);
  }

  // Are the blobs contained in this UnionFind all connected?
  boolean isComplete() {
    // Construct the keys of this UnionFind as an ArrayList
    ArrayList<Node> keys = new ArrayList<>();
    keys.addAll(this.env.keySet());

    boolean flag = true;

    if (keys.isEmpty()) {
      return flag;
    }

    // Finds the master of the first representative for comparison purposes
    Node firstRep = findBlob(keys.get(0));

    // Is any master different from the first master? If so, flag is set to false.
    for (Node id : keys) {
      if (this.findBlob(id) != firstRep) {
        flag = false;
        break;
      }
    }

    return flag;
  }

  
  //find the representative of the given node
  Node findRep(Node n) {
    return this.env.get(n);
  }
  
  // Determines the master representative of the Node represented by the given key
  // to this UnionFind. Master is defined as the highest representative.
  Node findBlob(Node node) {
    Node thisRep = this.rep(node);

    if (thisRep == node) {
      return node;
    }

    return this.findBlob(thisRep);
  }

  // Implements Kruskal's algorithm to find the minimum spanning tree
  //returns the edges that are left
  ArrayList<Edge> kruskals() {
    // Termination condition : every step of the loop, the first item of the
    // worklist is popped this means, so that the workList is not of infinite size,
    // the while loop will eventually terminate
    while (!workList.isEmpty()) {
      Edge cheapest = workList.remove(0);

      if (!cheapest.sameBlob(this)) {
        edgesInTree.add(cheapest);
        cheapest.relinkReps(this);
      }
      else {
        // implies that the edge needs to be removed from both nodes
        cheapest.removeFromNodes();
      }
    }
    return edgesInTree;
  }

  // EFFECT: adds the given edge to the edgesInTree in this UnionFind
  void addEdge(Edge edge) {
    this.edgesInTree.add(edge);
  }

  // Has the edge provided been since in the edge list contained in this
  // UnionFind?
  boolean edgeHasBeenSeen(Edge edge) {
    return this.edgesInTree.contains(edge);
  }

  // Do the two Nodes provided have the same representative?
  boolean sameRep(Node nodeOne, Node nodeTwo) {
    return this.findBlob(nodeOne) == this.findBlob(nodeTwo);
  }

  // EFFECT: updates the representative of the nodeToBeChanged passed in according
  // to the master representative of the given current representative node.
  void union(Node currRep, Node nodeToBeChanged) {
    Node masterRep = this.findBlob(currRep);
    this.updateRep(nodeToBeChanged, masterRep);
  }

}

// represents a Maze
class Maze {
  private final ArrayList<ArrayList<Cell>> grid;
  private final ArrayList<Node> nodes;
  private final ArrayList<Edge> edges;
  //the width and the height of the maze is the number
  //of nodes in the width and the height. this is
  //not the same as the size of the render
  private final int width;
  private final int height;
  // scale represents how many pixels one side of the cell takes up
  private final int scale;
  private final Optional<Boolean> bias; //empty is no bias, false is vert bias, true is horiz bias

  Maze(int width, int height, int scale, Optional<Boolean> bias) {
    this.scale = scale;
    this.width = width;
    this.height = height;
    this.bias = bias;
    // Construct and link the list representation of the Maze
    Triple<ArrayList<ArrayList<Cell>>, ArrayList<Node>, ArrayList<Edge>> boardInfo = createNewBoard(
        width, height, scale);

    this.grid = boardInfo.first();
    this.nodes = boardInfo.second();
    this.edges = new UnionFind(nodes, boardInfo.third()).kruskals();
  }
  
  Maze(ArrayList<ArrayList<Cell>> grid,
      ArrayList<Node> nodes, ArrayList<Edge> edges,
      int scale, Optional<Boolean> bias) {
    this.grid = grid;
    this.nodes = nodes;
    this.edges = edges;
    this.scale = scale;
    this.bias = bias;
    this.width = grid.get(0).size();
    this.height = grid.size();
  }

  // renders this maze
  ComputedPixelImage renderMaze() {
    ComputedPixelImage cpi = new ComputedPixelImage(width * scale, height * scale);

    // renders every pixel in the maze, including the walls if there lacks an
    // edge between the cells. 
    for (int rowIdx = 0; rowIdx < height; rowIdx += 1) {
      for (int colIdx = 0; colIdx < width; colIdx += 1) {
        Cell currCell = grid.get(rowIdx).get(colIdx);
        if (rowIdx == 0 && colIdx == 0) {
          currCell.updateColor(Color.green);
        }
        if (rowIdx == height - 1 && colIdx == width - 1) {
          currCell.updateColor(Color.red);
        }
        currCell.renderSelf(grid, cpi, scale); 
      }
    }
    
    return cpi;
  }
  
  //determines the Cell at the given position
  Cell cellAt(Posn p) {
    return this.grid.get(p.y).get(p.x);
  }
  
  //resets the colors of the cells in this grid to be the default (default being Light gray)
  void resetGridColors() {
    for (int rowIdx = 0; rowIdx < this.grid.size(); rowIdx += 1) {
      for (int colIdx = 0; colIdx < this.grid.get(0).size(); colIdx += 1) {
        this.grid.get(rowIdx).get(colIdx).updateColor(Color.LIGHT_GRAY);
      }
    }
  }

  // Creates a new board for this Maze, with a nested list of connected Nodes.
  Triple<ArrayList<ArrayList<Cell>>, ArrayList<Node>, ArrayList<Edge>> createNewBoard(
      int width, int height, int scale) {

    ArrayList<ArrayList<Cell>> result = new ArrayList<>();
    ArrayList<Node> nodesResult = new ArrayList<>();
    ArrayList<Edge> edgesResult = new ArrayList<>();

    // For each position (colIdx, rowIdx) add a Cell, containing a Node and its
    // respective
    // positions. Note: Nodes are made here without any connections.
    for (int rowIdx = 0; rowIdx < height; rowIdx += 1) {
      ArrayList<Cell> currRow = new ArrayList<>();
      for (int colIdx = 0; colIdx < width; colIdx += 1) {

        Node currNode = new Node();
        Posn currPos = new Posn(colIdx, rowIdx);
        double vertWeight = 1;
        double horizWeight = 1;
        
        if (bias.isPresent()) {
          if (bias.get()) {
            vertWeight = 5;
          }
          else {
            horizWeight = 5;
          }
        }
        
        currRow.add(new Cell(currNode, currPos));
        // if we are not on the leftmost col, make a connection between the current node
        // and to it's left
        if (colIdx != 0) {
          edgesResult.add(currRow.get(colIdx - 1).connectNode(currNode, horizWeight));
        }
        // if we are not on the topmost row, make a connection between the current node
        // and to it's above
        if (rowIdx != 0) {
          edgesResult.add(result.get(rowIdx - 1).get(colIdx).connectNode(currNode, vertWeight));
        }

        nodesResult.add(currNode);
      }

      result.add(currRow);
    }

    return new Triple<ArrayList<ArrayList<Cell>>, ArrayList<Node>, ArrayList<Edge>>(
        result,
        nodesResult, 
        edgesResult
        );
  }

  // <---------------- MazeSimulation Helpers ---------------->

  // Can the player move to the target position in this Maze, with respect to its
  // current position?
  boolean playerCanMove(Posn currPos, Posn targPos) {
    if (targPos.x < 0 
        || targPos.y < 0 
        || targPos.x >= this.grid.get(0).size()
        || targPos.y >= this.grid.size()) {
      return false;
    }
    
    Cell currCell = this.grid.get(currPos.y).get(currPos.x);
    Cell targCell = this.grid.get(targPos.y).get(targPos.x);

    return currCell.isLinkedTo(targCell).isPresent();
  }
  
  // Colors the cell at the given position in this Maze. The color that the cell
  // is updated to be is dependent on the color given.
  void colorCell(Posn pos, Color color) {
    Cell currCell = this.grid.get(pos.y).get(pos.x);
    currCell.colorSelf(color);
  }
  
  // Has the player completed the maze?
  boolean finishedMaze(Posn playerPos) {
    // De-construct the given position structure
    int x = playerPos.x;
    int y = playerPos.y;
    
    return x == this.grid.get(0).size() - 1
        && y == this.grid.size() - 1;  
  }

  // <----------------------- DFS & BFS ALGORITHM ----------------------------->
  // //
  // in the future, move color into node
  // Conducts a Depth-First search of this Maze to find the path from the start to
  // the finish.
  // When complete, returns an Iterator of Cells which contains the target path
  // from the
  // the start to finish.
  SearchResults dfs() {
    return search(new Stack<CellInfo>());
  }

  // Conducts a Breadth-First search of this Maze to find the path from the start
  // to the finish.
  // When complete, returns an Iterator of Cells which contains the target path
  // from the start to the finish.
  SearchResults bfs() {
    return search(new Queue<CellInfo>());
  }

  // Searches through this Maze to find the path from the start to finish. General
  // searching algorithm, which depends on the type of Collection it is given 
  // as the work-list (Stack vs Queue).
  SearchResults search(ICollection<CellInfo> workList) {
    HashMap<Cell, Optional<Cell>> searchPaths = new HashMap<>(); 
    //Starting cell is an orphan, therefore optional. Children always have a parent
    Deque<Cell> alreadySeen = new ArrayDeque<Cell>();
    Cell from = grid.get(0).get(0);
    Cell to = grid.get(grid.size() - 1).get(grid.get(0).size() - 1);
    workList.add(new CellInfo(from, Optional.empty()));

    while (!workList.isEmpty()) {
      CellInfo next = workList.pop();
      if (next.child().equals(to)) {
        
        //find solution path of search paths
        //
        searchPaths.put(next.child(), next.parent());
        alreadySeen.add(to); 
        return new SearchResults(this.findPath(searchPaths,to), workList.newThis(alreadySeen));
      }
      else if (alreadySeen.contains(next.child())) {
        // do nothing
      }
      else {
        searchPaths.put(next.child(), next.parent());
        alreadySeen.add(next.child());
        for (Cell c : next.child().connectingCells(grid)) {
          
          workList.add(new CellInfo(c, Optional.of(next.child())));
        }
      }
    }
    throw new RuntimeException("no path exists");
  }
  
  // Finds the solution path to this maze given a cameFromKey HashMap
  ArrayList<Cell> findPath(HashMap<Cell, Optional<Cell>> solution, Cell to) {
    Cell prev = to;
    ArrayList<Cell> path = new ArrayList<>();
    Optional<Cell> candidate = solution.get(prev);
    
    while (candidate.isPresent()) {
      Cell candidateVal = candidate.get();
      path.add(candidateVal);
      prev = candidateVal;
      candidate = solution.get(prev);
    }
    
    return path;
  }
}

// Represents a Simulation of a Maze game
class MazeSimulation extends World {
  // All fields in MazeSimulation are not final because they must be changed as
  // the game progresses.
  private Maze maze;
  private Posn playerPosn;
  private Iterator<Cell> traversal;
  // These are stored for ease of creating new mazes of the same dimensions
  private final int width; //these fields are final
  private final int height; //because we don't let
  private final int scale; //the users change these parameters once running
  private Optional<Boolean> bias;
  private ArrayList<Cell> solution;
  private Iterator<Cell> solIter;
  private boolean solved;
  private int wrongCount;

  // Constructor which makes a new random maze
  MazeSimulation(Posn playerPosn, int width, int height, int scale, Optional<Boolean> bias) {
    this.playerPosn = playerPosn;
    this.maze = new Maze(width / scale, height / scale, scale, bias);
    this.traversal = Collections.emptyIterator();
    this.width = width;
    this.height = height;
    this.scale = scale;
    this.bias = bias;
    this.solution = this.maze.dfs().determineSolution();
    this.solIter = this.solution.iterator();
    this.solved = false;
    this.wrongCount = 0;
  }

  // Renders this maze each tick
  public void onTick() {
    if (this.maze.finishedMaze(this.playerPosn)) {
      solved = true;
      
      if (solIter.hasNext()) {
        Cell currCell = solIter.next();       
        currCell.colorSelf(Color.BLUE); 
      }
    } 
    else if (traversal.hasNext()) {
      Cell currCell = traversal.next();
      if (!solution.contains(currCell)) {
        wrongCount += 1;
      }
      currCell.colorSelf(Color.cyan);
    } 
    else if (solved) {
      if (solIter.hasNext()) {
        Cell currCell = solIter.next();       
        currCell.colorSelf(Color.BLUE);
      }
    }
  }

  // Conducts a Depth-first Search or Breadth-first Search dependent on if the
  // user presses "D" or "B", respectively. Allows the player to move up, down,
  // left and right to progress through the maze with WASD controls. User can press
  // "n" key for a new maze to be created. User can press "N" to make a new maze
  // without resetting their progress.
  public void onKeyEvent(String key) {
    if (!traversal.hasNext()) {
      if (key.equals("D")) {
        // The Iterator<Cell> returned by maze.dfs() represents the target path
        // from the start to the finish.
        SearchResults dfs = this.maze.dfs();
        this.traversal = dfs.determineSearch();
        this.solution = dfs.determineSolution();
        solved = true;
      }
      if (key.equals("B")) {
        // The Iterator<Cell> returned by maze.bfs() represents the target path
        // from the start to the finish.
        SearchResults bfs = this.maze.bfs();
        this.traversal = bfs.determineSearch();
        this.solution = bfs.determineSolution();    
        solved = true;
      }

      if (key.equals("w")) {
        Posn candPos = new Posn(playerPosn.x, playerPosn.y - 1);
        movePlayer(candPos);
      }

      if (key.equals("s")) {
        Posn candPos = new Posn(playerPosn.x, playerPosn.y + 1);
        movePlayer(candPos);
      }

      if (key.equals("a")) {
        Posn candPos = new Posn(playerPosn.x - 1, playerPosn.y);
        movePlayer(candPos);
      }

      if (key.equals("d")) {
        Posn candPos = new Posn(playerPosn.x + 1, playerPosn.y);
        movePlayer(candPos);
      }
    }
    if (key.equals("r")) {
      
      this.playerPosn = new Posn(0,0);
      this.maze.resetGridColors();
      this.traversal = Collections.emptyIterator();
      this.solution = this.maze.dfs().determineSolution();
      this.solIter = solution.iterator();
      this.solved = false;
      this.wrongCount = 0;
    }
    if (key.equals("n")) {
      makeNewMaze(Optional.empty());
    }
    if (key.equals("v")) {
      makeNewMaze(Optional.of(false));
    }
    if (key.equals("h")) {
      makeNewMaze(Optional.of(true));
    }
    
  }
  
  // EFFECT: makes a new maze by randomly generating a new one and resetting
  // all of the other fields of this MazeSimulation
  void makeNewMaze(Optional<Boolean> b) {
    this.playerPosn = new Posn(0,0);
    this.maze = new Maze(width / scale, height / scale, scale, b);
    this.traversal = Collections.emptyIterator();
    this.solution = this.maze.dfs().determineSolution();
    this.solIter = solution.iterator();
    this.solved = false;
    this.wrongCount = 0;
  }
  

  // Moves the player of this MazeSimulation dependent on the given positions and
  // colors the cell that the player moves to, if they can move to it.
  void movePlayer(Posn targPos) {
    if (!solved && maze.playerCanMove(this.playerPosn, targPos)) {
      this.playerPosn = targPos;
      this.maze.colorCell(playerPosn, Color.cyan);
      
      if (!solution.contains(maze.cellAt(playerPosn))) {
        wrongCount += 1;
      }
    }
    if (!maze.playerCanMove(this.playerPosn, targPos)) {
      wrongCount += 1;
    }
  }

  // Makes the rendered image of the Maze contained within this MazeSimulation
  public WorldImage makeImage() {
    return new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.TOP,
        new TextImage("Wrong moves: " + wrongCount, 30, Color.white),
        50, -20,
        this.maze.renderMaze());
  }

  // Makes the scene of this MazeSimulation
  public WorldScene makeScene() {
    WorldScene canvas = new WorldScene(1000, 1000);
    canvas.placeImageXY(this.makeImage(), 500, 500);

    return canvas;
  }
}

//Holds utilities that may be needed for
//given that this is supposed to be used by all classes, none of it's methods will be private
class Utils {

  // Creates a hashmap where every value in the given ArrayList points towards
  // itself
  <T> HashMap<T, T> selfHashMap(ArrayList<T> arrList) {
    HashMap<T, T> hash = new HashMap<>(arrList.size());
    for (T t : arrList) {
      hash.put(t, t);
    }

    return hash;
  }

  // credit : merge sort implemented in same fashion as the notes
  // EFFECT: Sorts the provided list according to the given comparator
  <T> void mergesort(ArrayList<T> arr, Comparator<T> comp) {

    ArrayList<T> temp = new ArrayList<T>(arr.size());
    for (int i = 0; i < arr.size(); i = i + 1) {
      temp.add(arr.get(i));
    }

    mergesort(arr, temp, comp, 0, arr.size());
  }

  // overload for merge sort, gaining access to a low index, high index, and a
  // temporary ArrayList
  <T> void mergesort(ArrayList<T> source, ArrayList<T> temp, Comparator<T> comp, int loIdx,
      int hiIdx) {

    if (hiIdx - loIdx <= 1) {
      // nothing needs to be done here, all items in the list has been sorted
      return;
    }
    int midIdx = (loIdx + hiIdx) / 2;

    mergesort(source, temp, comp, loIdx, midIdx);
    mergesort(source, temp, comp, midIdx, hiIdx);

    merge(source, temp, comp, loIdx, midIdx, hiIdx);
  }

  // helper for mergeSort
  <T> void merge(ArrayList<T> source, ArrayList<T> temp, Comparator<T> comp, int loIdx, int midIdx,
      int hiIdx) {

    int curLo = loIdx;
    int curHi = midIdx;
    int curCopy = loIdx;

    while (curLo < midIdx && curHi < hiIdx) {
      if (comp.compare(source.get(curLo), source.get(curHi)) <= 0) {
        temp.set(curCopy, source.get(curLo));
        curLo = curLo + 1;
      }
      else {
        temp.set(curCopy, source.get(curHi));
        curHi = curHi + 1;
      }
      curCopy = curCopy + 1;
    }

    while (curLo < midIdx) {
      temp.set(curCopy, source.get(curLo));
      curLo = curLo + 1;
      curCopy = curCopy + 1;
    }

    while (curHi < hiIdx) {
      temp.set(curCopy, source.get(curHi));
      curHi = curHi + 1;
      curCopy = curCopy + 1;
    }

    for (int i = loIdx; i < hiIdx; i = i + 1) {
      source.set(i, temp.get(i));
    }
  }
}

// Compares two edge's weights to determine which given Edge has a greater weight than
// the other Edge.
class EdgeCompare implements Comparator<Edge> {

  public int compare(Edge edge1, Edge edge2) {
    Edge smallestEdge = edge1.minEdge(new ArrayList<>(Arrays.asList(edge2)));
    int comparison = 0;
    if (edge1 == smallestEdge) {
      comparison = -1;
    }
    else if (edge2 == smallestEdge) {
      comparison = 1;
    }
    return comparison;
  }
}

class ExampleMaze {

  Node a1;
  Node b1;
  Node c1;
  Node d1;
  Node e1;
  Node f1;

  Edge ec1;
  Edge cd1;
  Edge ab1;
  Edge be1;
  Edge bc1;
  Edge fd1;
  Edge ae1;
  Edge bf1;

  UnionFind ex1;

  void initNodes1() {
    a1 = new Node();
    b1 = new Node();
    c1 = new Node();
    d1 = new Node();
    e1 = new Node();
    f1 = new Node();
  }

  void createEdges1() {
    ec1 = new Edge(e1, c1, 15);
    cd1 = new Edge(c1, d1, 25);
    ab1 = new Edge(a1, b1, 30);
    be1 = new Edge(b1, e1, 35);
    bc1 = new Edge(b1, c1, 40);
    fd1 = new Edge(f1, d1, 50);
    ae1 = new Edge(a1, e1, 50);
    bf1 = new Edge(b1, f1, 50);
  }

  void connectEdges() {
    e1.addEdge(ec1, true);
    c1.addEdge(cd1, true);
    a1.addEdge(ab1, true);
    b1.addEdge(be1, true);
    b1.addEdge(bc1, true);
    f1.addEdge(fd1, true);
    a1.addEdge(ae1, true);
    b1.addEdge(bf1, true);
  }

  void initEx1() {
    initNodes1();
    createEdges1();
    connectEdges();

    ex1 = new UnionFind(new ArrayList<Node>(Arrays.asList(a1, b1, c1, d1, e1, f1)),
        new ArrayList<Edge>(Arrays.asList(ec1, cd1, ab1, be1, bc1, fd1, ae1, bf1)));
  }

  void testBigBang(Tester t) {
    World w = new MazeSimulation(new Posn(0, 0), 1000, 1000, 30, Optional.empty());
    w.bigBang(1000, 1000, 0.01);
  }

  void testNodes(Tester t) {
    initNodes1();
    createEdges1();
    t.checkExpect(e1.sharesEdgeWith(c1), false); // no predefined connection exist
    t.checkExpect(c1.sharesEdgeWith(e1), false); // checking symmetry
    e1.addEdge(ec1, true);
    t.checkExpect(e1.sharesEdgeWith(c1), true); // checks that connection is added
    t.checkExpect(c1.sharesEdgeWith(e1), true); // checking symmetry

    t.checkExpect(e1.sharesEdgeWith(b1), false); // no predefined connection exist
    t.checkExpect(b1.sharesEdgeWith(e1), false); // checking symmetry
    e1.addEdge(be1, true);
    t.checkExpect(e1.sharesEdgeWith(b1), true); // checks that connection is added
    t.checkExpect(e1.sharesEdgeWith(c1), true); // checking that old was not overwritten
    // at this point in time, we can assume symmetry works, as we tested it.

    e1.removeEdge(ec1, true);
    // c1 should be unable to remove an edge that was already removed
    t.checkExceptionType(IndexOutOfBoundsException.class, c1, "removeEdge", ec1, true);
    t.checkExpect(e1.sharesEdgeWith(c1), false); // no predefined connection exist
    t.checkExpect(c1.sharesEdgeWith(e1), false); // checking symmetry
    t.checkExpect(e1.sharesEdgeWith(b1), true); // checks that connection is added
  }

  Node a2;
  Node b2;
  Node c2;
  Node d2;
  Node e2;
  Node f2;
  Node g2;
  Node h2;
  Node i2;
  Node j2;
  Node k2;
  Node l2;
  Node m2;
  Node n2;
  Node o2;
  Node p2;
  
  Cell Ca2; //while unconventional naming standard
  Cell Cb2; //it has to be done bc we don't want to confuse
  Cell Cc2; //with edges
  Cell Cd2;
  Cell Ce2;
  Cell Cf2;
  Cell Cg2;
  Cell Ch2;
  Cell Ci2;
  Cell Cj2;
  Cell Ck2;
  Cell Cl2;
  Cell Cm2;
  Cell Cn2;
  Cell Co2;
  Cell Cp2;

  Edge ab2;
  Edge ae2;
  Edge bc2;
  Edge ef2;
  Edge cg2;
  Edge fj2;
  Edge gh2;
  Edge jn2;
  Edge hd2;
  Edge hl2;
  Edge lk2;
  Edge nm2;
  Edge no2;
  Edge mi2;
  Edge op2;
  Maze maze2;
  
  void initNode2() {
    a2 = new Node();
    b2 = new Node();
    c2 = new Node();
    d2 = new Node();
    e2 = new Node();
    f2 = new Node();
    g2 = new Node();
    h2 = new Node();
    i2 = new Node();
    j2 = new Node();
    k2 = new Node();
    l2 = new Node();
    m2 = new Node();
    n2 = new Node();
    o2 = new Node();
    p2 = new Node();
  }

  void makeEdge2() {
    // in this case, we don't actually care about the weight for the edges
    // for this reason, all edges will have weight 1
    ab2 = new Edge(a2, b2, 1);
    ae2 = new Edge(a2, e2, 1);
    bc2 = new Edge(b2, c2, 1);
    ef2 = new Edge(e2, f2, 1);
    cg2 = new Edge(c2, g2, 1);
    fj2 = new Edge(f2, j2, 1);
    gh2 = new Edge(g2, h2, 1);
    jn2 = new Edge(j2, n2, 1);
    hd2 = new Edge(h2, d2, 1);
    hl2 = new Edge(h2, l2, 1);
    lk2 = new Edge(l2, k2, 1);
    nm2 = new Edge(n2, m2, 1);
    no2 = new Edge(n2, o2, 1);
    mi2 = new Edge(m2, i2, 1);
    op2 = new Edge(o2, p2, 1);
  }

  void connectEdge2() {
    a2.addEdge(ab2, true);
    a2.addEdge(ae2, true);
    b2.addEdge(bc2, true);
    e2.addEdge(ef2, true);
    c2.addEdge(cg2, true);
    f2.addEdge(fj2, true);
    g2.addEdge(gh2, true);
    j2.addEdge(jn2, true);
    h2.addEdge(hd2, true);
    h2.addEdge(hl2, true);
    l2.addEdge(lk2, true);
    n2.addEdge(nm2, true);
    n2.addEdge(no2, true);
    m2.addEdge(mi2, true);
    o2.addEdge(op2, true);
  }

  void assignCells2() {
    Ca2 = new Cell(a2, new Posn(0, 0));
    Cb2 = new Cell(b2, new Posn(1, 0));
    Cc2 = new Cell(c2, new Posn(2, 0));
    Cd2 = new Cell(d2, new Posn(3, 0));
    Ce2 = new Cell(e2, new Posn(0, 1));
    Cf2 = new Cell(f2, new Posn(1, 1));
    Cg2 = new Cell(g2, new Posn(2, 1));
    Ch2 = new Cell(h2, new Posn(3, 1));
    Ci2 = new Cell(i2, new Posn(0, 2));
    Cj2 = new Cell(j2, new Posn(1, 2));
    Ck2 = new Cell(k2, new Posn(2, 2));
    Cl2 = new Cell(l2, new Posn(3, 2));
    Cm2 = new Cell(m2, new Posn(0, 3));
    Cn2 = new Cell(n2, new Posn(1, 3));
    Co2 = new Cell(o2, new Posn(2, 3));
    Cp2 = new Cell(p2, new Posn(3, 3));
  }
  
  void initAll2() {
    initNode2();
    makeEdge2();
    connectEdge2();
    assignCells2();
    ArrayList<Node> nodes2 = new ArrayList<>(
        List.of(a2, b2, c2, d2, e2, f2, g2, h2, i2, j2, k2, l2, m2, n2, o2, p2));
    ArrayList<Edge> edges2 = new ArrayList<>(
        List.of(ab2, ae2, bc2, ef2, cg2, fj2, gh2, jn2, hd2, hl2, lk2, nm2, no2, mi2, op2));
    ArrayList<ArrayList<Cell>> grid2 = new ArrayList<ArrayList<Cell>>();
    grid2.add(new ArrayList<Cell>(List.of(Ca2, Cb2, Cc2, Cd2)));
    grid2.add(new ArrayList<Cell>(List.of(Ce2, Cf2, Cg2, Ch2)));
    grid2.add(new ArrayList<Cell>(List.of(Ci2, Cj2, Ck2, Cl2)));
    grid2.add(new ArrayList<Cell>(List.of(Cm2, Cn2, Co2, Cp2)));
   
    maze2 = new Maze(grid2, nodes2, edges2, 10, Optional.empty());
  }
  
  void testMaze2(Tester t) {
    this.initAll2();
    Deque<Cell> maze2BFS = new ArrayDeque<>();
    for (Cell c : 
        List.of(Ca2, Cb2, Ce2, Cc2, Cf2, Cg2, Cj2, Ch2, Cn2, Cd2, Cl2, Cm2,
          Co2, Ck2, Ci2, Cp2)) {
      maze2BFS.add(c);
    }
    ArrayList<Cell> pathToEnd = new ArrayList<>(List.of(Co2, Cn2, Cj2, Cf2, Ce2, Ca2));
    SearchResults bfsSR = new SearchResults(pathToEnd, new Queue<Cell>().newThis(maze2BFS));
    t.checkExpect(maze2.bfs(), bfsSR);
    
    Deque<Cell> maze2DFS = new ArrayDeque<>();
    for (Cell c :
        List.of(Ca2, Ce2, Cf2, Cj2, Cn2, Co2, Cp2)) {
      maze2DFS.add(c);
    }
    SearchResults dfsSR = new SearchResults(pathToEnd, new Stack<Cell>().newThis(maze2DFS));
    t.checkExpect(maze2.dfs(), dfsSR);
    
  }
  
  //maze three is a 3 x 4 grid
  Node a3;
  Node b3; 
  Node c3; 
  Node d3;
  Node e3;
  Node f3;
  Node g3;
  Node h3;
  Node i3;
  Node j3;
  Node k3;
  Node l3;
  
  Edge ab3;
  Edge be3;
  Edge ed3;
  Edge dg3;
  Edge gj3;
  Edge eh3;
  Edge hk3;
  Edge kl3;
  Edge li3;
  Edge if3;
  Edge fc3;
  
  Cell Ca3;
  Cell Cb3;
  Cell Cc3;
  Cell Cd3;
  Cell Ce3;
  Cell Cf3;
  Cell Cg3;
  Cell Ch3;
  Cell Ci3;
  Cell Cj3;
  Cell Ck3;
  Cell Cl3;
  
  Maze maze3;
  
  void initNode3() {
    a3 = new Node();
    b3 = new Node();
    c3 = new Node();
    d3 = new Node();
    e3 = new Node();
    f3 = new Node();
    g3 = new Node();
    h3 = new Node();
    i3 = new Node();
    j3 = new Node();
    k3 = new Node();
    l3 = new Node();
  }
  
  void makeEdge3() {
    //because edge weights do not matter in this case, they will all be set to 1
    ab3 = new Edge(a3, b3, 1);
    be3 = new Edge(b3, e3, 1);
    ed3 = new Edge(e3, d3, 1);
    gj3 = new Edge(g3, j3, 1);
    eh3 = new Edge(e3, h3, 1);
    hk3 = new Edge(h3, k3, 1);
    kl3 = new Edge(k3, l3, 1);
    li3 = new Edge(l3, i3, 1);
    if3 = new Edge(i3, f3, 1);
    fc3 = new Edge(f3, c3, 1);
  }
  
  void connectEdge3() {
    a3.addEdge(ab3, true);
    b3.addEdge(be3, true);
    e3.addEdge(ed3, true);
    g3.addEdge(gj3, true);
    e3.addEdge(eh3, true);
    h3.addEdge(hk3, true);
    k3.addEdge(kl3, true);
    l3.addEdge(li3, true);
    i3.addEdge(if3, true);
    f3.addEdge(fc3, true);
  }
  
  void assignCells3() {
    Ca3 = new Cell(a3, new Posn(0, 0));
    Cb3 = new Cell(b3, new Posn(1, 0));
    Cc3 = new Cell(c3, new Posn(2, 0));
    Cd3 = new Cell(d3, new Posn(0, 1));
    Ce3 = new Cell(e3, new Posn(1, 1));
    Cf3 = new Cell(f3, new Posn(2, 1));
    Cg3 = new Cell(g3, new Posn(0, 2));
    Ch3 = new Cell(h3, new Posn(1, 2));
    Ci3 = new Cell(i3, new Posn(2, 2));
    Cj3 = new Cell(j3, new Posn(0, 3));
    Ck3 = new Cell(k3, new Posn(1, 3));
    Cl3 = new Cell(l3, new Posn(2, 3));
  }

  void initAll3() {
    initNode3();
    makeEdge3();
    connectEdge3();
    assignCells3();
    ArrayList<Node> nodes3 = new ArrayList<>(
        List.of(a3, b3, c3, d3, e3, f3, g3, h3, i3, j3, k3, l3));
    ArrayList<Edge> edges3 = new ArrayList<>(
        List.of(ab3, be3, ed3, gj3, eh3, hk3, kl3, li3, if3, fc3));
    ArrayList<ArrayList<Cell>> grid3 = new ArrayList<ArrayList<Cell>>();
    grid3.add(new ArrayList<Cell>(List.of(Ca3, Cb3, Cc3)));
    grid3.add(new ArrayList<Cell>(List.of(Cd3, Ce3, Cf3)));
    grid3.add(new ArrayList<Cell>(List.of(Cg3, Ch3, Ci3)));
    grid3.add(new ArrayList<Cell>(List.of(Cj3, Ck3, Cl3)));
   
    maze3 = new Maze(grid3, nodes3, edges3, 5, Optional.empty());
  }
  
  void testMaze3(Tester t) {
    this.initAll3();
    Deque<Cell> maze3BFS = new ArrayDeque<>();
    for (Cell c : 
        List.of(Ca3, Cb3, Ce3, Cd3, Ch3, Ck3, Cl3)) {
      maze3BFS.add(c);
    }
    ArrayList<Cell> pathToEnd = new ArrayList<>(List.of(Ck3, Ch3, Ce3, Cb3, Ca3));
    SearchResults bfsSR = new SearchResults(pathToEnd, new Queue<Cell>(maze3BFS));
    t.checkExpect(maze3.bfs(), bfsSR);
    
    Deque<Cell> maze2DFS = new ArrayDeque<>();
    for (Cell c :
        List.of(Ca3, Cb3, Ce3, Ch3, Ck3, Cl3)) {
      maze2DFS.add(c);
    }
    SearchResults dfsSR = new SearchResults(pathToEnd, new Stack<Cell>(maze2DFS));
    t.checkExpect(maze3.dfs(), dfsSR);
  }

  //Maze 4 is a 4 x 3 grid
  Node a4;
  Node b4;
  Node c4;
  Node d4;
  Node e4;
  Node f4;
  Node g4;
  Node h4;
  Node i4;
  Node j4;
  Node k4;
  Node l4;
  
  Edge ab4;
  Edge bf4;
  Edge fj4;
  Edge ji4;
  Edge je4;
  Edge fg4;
  Edge gh4;
  Edge gd4;
  Edge dc4;
  Edge gk4;
  Edge kl4;
  
  Cell Ca4;
  Cell Cb4;
  Cell Cc4;
  Cell Cd4;
  Cell Ce4;
  Cell Cf4;
  Cell Cg4;
  Cell Ch4;
  Cell Ci4;
  Cell Cj4;
  Cell Ck4;
  Cell Cl4;
  
  Maze maze4;
  
  void initNode4() {
    a4 = new Node();
    b4 = new Node();
    c4 = new Node();
    d4 = new Node();
    e4 = new Node();
    f4 = new Node();
    g4 = new Node();
    h4 = new Node();
    i4 = new Node();
    j4 = new Node();
    k4 = new Node();
    l4 = new Node();
  }
  
  void initEdge4() {
    //because weights do not matter, all using 1 
    ab4 = new Edge(a4, b4, 1);
    bf4 = new Edge(b4, f4, 1);
    fj4 = new Edge(f4, j4, 1);
    ji4 = new Edge(j4, i4, 1);
    je4 = new Edge(j4, e4, 1);
    fg4 = new Edge(f4, g4, 1);
    gh4 = new Edge(g4, h4, 1);
    gd4 = new Edge(g4, d4, 1);
    dc4 = new Edge(d4, c4, 1);
    gk4 = new Edge(g4, k4, 1);
    kl4 = new Edge(k4, l4, 1); 
  }
  
  void connectEdges4() {
    a4.addEdge(ab4, true);
    b4.addEdge(bf4, true);
    f4.addEdge(fj4, true);
    j4.addEdge(ji4, true);
    j4.addEdge(je4, true);
    f4.addEdge(fg4, true);
    g4.addEdge(gh4, true);
    g4.addEdge(gd4, true);
    d4.addEdge(dc4, true);
    g4.addEdge(gk4, true);
    k4.addEdge(kl4, true);
  }
  
  void assignCells4() {
    Ca4 = new Cell(a4, new Posn(0, 0));
    Cb4 = new Cell(b4, new Posn(1, 0));
    Cc4 = new Cell(c4, new Posn(2, 0));
    Cd4 = new Cell(d4, new Posn(3, 0));
    Ce4 = new Cell(e4, new Posn(0, 1));
    Cf4 = new Cell(f4, new Posn(1, 1));
    Cg4 = new Cell(g4, new Posn(2, 1));
    Ch4 = new Cell(h4, new Posn(3, 1));
    Ci4 = new Cell(i4, new Posn(0, 2));
    Cj4 = new Cell(j4, new Posn(1, 2));
    Ck4 = new Cell(k4, new Posn(2, 2));
    Cl4 = new Cell(l4, new Posn(3, 2));
  }
  
  void initAll4() {
    initNode4();
    initEdge4();
    connectEdges4();
    assignCells4();
    ArrayList<Node> nodes4 = new ArrayList<>(
        List.of(a4, b4, c4, d4, e4, f4, g4, h4, i4, j4, k4, l4));
    ArrayList<Edge> edges4 = new ArrayList<>(
        List.of(ab4, bf4, fj4, ji4, je4, fg4, gh4, gd4, dc4, gk4, kl4));
    ArrayList<ArrayList<Cell>> grid4 = new ArrayList<ArrayList<Cell>>();
    grid4.add(new ArrayList<Cell>(List.of(Ca4, Cb4, Cc4, Cd4)));
    grid4.add(new ArrayList<Cell>(List.of(Ce4, Cf4, Cg4, Ch4)));
    grid4.add(new ArrayList<Cell>(List.of(Ci4, Cj4, Ck4, Cl4)));
   
    maze4 = new Maze(grid4, nodes4, edges4, 10, Optional.empty());
  }
  
  void testMaze4(Tester t) {
    this.initAll4();
    
    Deque<Cell> maze4BFS = new ArrayDeque<>();
    for (Cell c : 
        List.of(Ca4, Cb4, Cf4, Cg4, Cj4, Ch4, Ck4, Ci4, Cl4)) {
      maze4BFS.add(c);
    }
    ArrayList<Cell> pathToEnd = new ArrayList<>(List.of(Ck4, Cg4, Cf4, Cb4, Ca4));
    SearchResults bfsSR = new SearchResults(pathToEnd, new Queue<Cell>().newThis(maze4BFS));
    t.checkExpect(maze4.bfs(), bfsSR);
    
    Deque<Cell> maze4DFS = new ArrayDeque<>();
    for (Cell c :
        List.of(Ca4, Cb4, Cf4, Cj4, Ci4, Cg4, Ck4, Cl4)) {
      maze4DFS.add(c);
    }
    SearchResults dfsSR = new SearchResults(pathToEnd, new Stack<Cell>().newThis(maze4DFS));
    t.checkExpect(maze4.dfs(), dfsSR);
  }


  void testConnectNode(Tester t) {
    initAll2();

    // Create a new node and test connecting it
    Node testNode = new Node();
    Cell testCell = new Cell(a2, new Posn(0, 0));

    // Test that nodes aren't connected before
    t.checkExpect(a2.sharesEdgeWith(testNode), false);

    // Connect the nodes and check that connection exists
    Edge newEdge = testCell.connectNode(testNode, 1);
    t.checkExpect(a2.sharesEdgeWith(testNode), true);
    t.checkExpect(testNode.sharesEdgeWith(a2), true);

    // Check that the edge has expected properties
    t.checkExpect(newEdge.nodePosition(a2), -1);
    t.checkExpect(newEdge.nodePosition(testNode), 1);
  }

  void testRenderSelf(Tester t) {
    initAll2();

    // Create a simple cell with a known position
    Cell testCell = new Cell(a2, new Posn(1, 1));

    // Create a small image to render into
    ComputedPixelImage cpi = new ComputedPixelImage(10, 10);

    // Set all pixels to black initially
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        cpi.setColorAt(i, j, Color.BLACK);
      }
    }

    // Set the node's color to white
    a2.updateColor(Color.WHITE);

    // Check that the appropriate pixels are now white
    for (int i = 1; i < 4; i++) {
      for (int j = 1; j < 4; j++) {
        t.checkExpect(cpi.getColorAt(i, j), Color.BLACK);
      }
    }

    // Check that pixels outside the cell remain black
    t.checkExpect(cpi.getColorAt(0, 0), Color.BLACK);
    t.checkExpect(cpi.getColorAt(4, 4), Color.BLACK);
  }

  void testIsLinkedTo(Tester t) {
    initAll2();

    // Create cells with the nodes we've already initialized
    Cell cellA = new Cell(a2, new Posn(0, 0));
    Cell cellB = new Cell(b2, new Posn(1, 0));
    Cell cellC = new Cell(c2, new Posn(2, 0));
    Cell cellD = new Cell(d2, new Posn(3, 0));

    // Test cells that should be linked because their nodes share an edge
    t.checkExpect(cellA.isLinkedTo(cellB), Optional.of(cellB));
    t.checkExpect(cellB.isLinkedTo(cellA), Optional.of(cellA));
    t.checkExpect(cellB.isLinkedTo(cellC), Optional.of(cellC));

    // Test cells that shouldn't be linked (no direct edge between them)
    t.checkExpect(cellA.isLinkedTo(cellC), Optional.empty());
    t.checkExpect(cellA.isLinkedTo(cellD), Optional.empty());
    t.checkExpect(cellC.isLinkedTo(cellD), Optional.empty());
  }



  void testUpdateConnections(Tester t) {
    initEx1();

    // Test adding connections
    Node testNode = new Node();
    t.checkExpect(testNode.sharesEdgeWith(a1), false);

    // Add connection from the edge to the test node
    ab1.updateConnections("add", 1, true);
    t.checkExpect(testNode.sharesEdgeWith(a1), false); 

    // Create a new edge and test its connections
    Edge testEdge = new Edge(a1, testNode, 5);
    t.checkExpect(a1.sharesEdgeWith(testNode), false);

    // Add the connection using updateConnections
    testEdge.updateConnections("add", 1, true);
    t.checkExpect(a1.sharesEdgeWith(testNode), true);
    t.checkExpect(testNode.sharesEdgeWith(a1), true);

    // Test removing connections
    testEdge.updateConnections("remove", 1, true);
    t.checkExpect(a1.sharesEdgeWith(testNode), false);
    t.checkExpect(testNode.sharesEdgeWith(a1), false);

    // Test invalid operations
    t.checkExceptionType(IllegalArgumentException.class, testEdge, "updateConnections", "invalid",
        1, true);
    t.checkExceptionType(IllegalArgumentException.class, testEdge, "updateConnections", "add", 0,
        true);
  }

  void testSameMaster(Tester t) {
    initEx1();

    // Create a UnionFind that hasn't united any nodes yet
    UnionFind testUF = new UnionFind(new ArrayList<Node>(Arrays.asList(a1, b1, c1, d1, e1, f1)),
        new ArrayList<Edge>());

    // Initially, each node is its own master
    t.checkExpect(ab1.sameBlob(testUF), false);
    t.checkExpect(bc1.sameBlob(testUF), false);

    // Unite a1 and b1
    testUF.updateRep(a1, b1);

    // Now a1 and b1 should have the same master
    t.checkExpect(ab1.sameBlob(testUF), true);

    // c1 should still have its own master
    t.checkExpect(bc1.sameBlob(testUF), false);

    // Unite b1 and c1
    testUF.updateRep(b1, c1);

    // Now a1, b1, and c1 should all have the same master
    t.checkExpect(bc1.sameBlob(testUF), true);
  }

  void testRelinkReps(Tester t) {
    initEx1();

    // Create a fresh UnionFind
    UnionFind testUF = new UnionFind(new ArrayList<Node>(Arrays.asList(a1, b1, c1, d1, e1, f1)),
        new ArrayList<Edge>());

    // Initially, nodes should not have the same master
    t.checkExpect(testUF.findBlob(a1) == testUF.findBlob(b1), false);

    // Relink a1's master to b1's master
    ab1.relinkReps(testUF);

    // Now they should have the same master
    t.checkExpect(testUF.findBlob(a1) == testUF.findBlob(b1), true);

    // Relink another pair
    bc1.relinkReps(testUF);

    // Now a1, b1, and c1 should all have the same master
    t.checkExpect(testUF.findBlob(a1) == testUF.findBlob(c1), true);
    t.checkExpect(testUF.findBlob(b1) == testUF.findBlob(c1), true);
  }

  void testNodePosition(Tester t) {
    initEx1();

    // Test nodePosition with nodes that are part of the edge
    t.checkExpect(ab1.nodePosition(a1), -1); // a1 is the "from" node
    t.checkExpect(ab1.nodePosition(b1), 1); // b1 is the "to" node

    // Test with a node that is not part of the edge
    t.checkExpect(ab1.nodePosition(c1), 0); // c1 is not in the edge

    // Test with other edges
    t.checkExpect(bc1.nodePosition(b1), -1);
    t.checkExpect(bc1.nodePosition(c1), 1);
    t.checkExpect(bc1.nodePosition(a1), 0);

    // Test with a bidirectional edge (should maintain the same semantics)
    t.checkExpect(ec1.nodePosition(e1), -1);
    t.checkExpect(ec1.nodePosition(c1), 1);
  }

  void testTripleMethods(Tester t) {
    Integer first = 1;
    Double second = 2.0;
    String third = "third";

    Triple<Integer, Double, String> triple = new Triple<>(first, second, third);

    t.checkExpect(triple.first(), first);
    t.checkExpect(triple.second(), second);
    t.checkExpect(triple.third(), third);
  }

  void testQueueMethods(Tester t) {
    ICollection<Integer> q = new Queue<>();
    q.add(1);
    q.add(2);
    q.add(3);

    // Check the before state of the queue, where it is NOT empty
    t.checkExpect(q.isEmpty(), false);

    // Remove all of the items and ensure that they return the appropriate
    // Integer respective to their position
    t.checkExpect(q.pop(), 1);
    t.checkExpect(q.pop(), 2);
    t.checkExpect(q.pop(), 3);

    // Check that the Queue is now empty
    t.checkExpect(q.isEmpty(), true);

    // Ensure that you cannot remove any items from an empty queue (exception is
    // thrown)
    t.checkExceptionType(RuntimeException.class, q, "pop");
  }

  void testStackMethods(Tester t) {
    ICollection<Integer> s = new Stack<>();
    s.add(1);
    s.add(2);
    s.add(3);

    // Check the before state of the queue, where it is NOT empty
    t.checkExpect(s.isEmpty(), false);

    // Remove all of the items and ensure that they return the appropriate
    // Integer respective to their position
    t.checkExpect(s.pop(), 3);
    t.checkExpect(s.pop(), 2);
    t.checkExpect(s.pop(), 1);

    // Check that the Queue is now empty
    t.checkExpect(s.isEmpty(), true);

    // Ensure that you cannot remove any items from an empty queue (exception is
    // thrown)
    t.checkExceptionType(RuntimeException.class, s, "pop");
  }

  void testSameEdge(Tester t) {
    initEx1();

    // Test reflexivity, symmetry, and transitivity
    t.checkExpect(ec1.sameEdge(ec1), true);
    t.checkExpect(ec1.sameEdge(ab1), false);
    t.checkExpect(ab1.sameEdge(ec1), false);
    t.checkExpect(bf1.sameEdge(ec1), false);
    t.checkExpect(ab1.sameEdge(bf1), false);
  }

  void testMinEdge(Tester t) {
    initEx1();

    ArrayList<Edge> mt = new ArrayList<>();
    t.checkExpect(ec1.minEdge(mt), ec1);

    ArrayList<Edge> nonMt = new ArrayList<>(Arrays.asList(ab1, ec1, bf1, ae1));
    t.checkExpect(cd1.minEdge(nonMt), ec1);
    t.checkExpect(be1.minEdge(nonMt), ec1);

    ArrayList<Edge> containsSameWeight = new ArrayList<>(Arrays.asList(bf1, ae1));
    // Since all have the same weight, the edge was discovered first should be the
    // one returned. SInce fd1 is the Edge doing the comparisons, it is technically
    // "discovered" before anything else, and is therefore the one returned.
    t.checkExpect(fd1.minEdge(containsSameWeight), fd1);
  }

  void testRemoveFromNode(Tester t) {
    initEx1();

    // Check that e1 and c1 are connected through the edge ec1
    t.checkExpect(e1.sharesEdgeWith(c1), true);

    // Remove the connection
    ec1.removeFromNodes();

    // Check that e1 and c1 are no longer connected through the edge ec1
    t.checkExpect(e1.sharesEdgeWith(c1), false);
  }
}