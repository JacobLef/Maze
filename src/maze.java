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
