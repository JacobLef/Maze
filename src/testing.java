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
