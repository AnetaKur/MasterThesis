package com.example.masterthesis.automat;

import com.example.masterthesis.structure.twoD.BoundaryCondition;
import com.example.masterthesis.structure.twoD.Cell2D;
import com.example.masterthesis.structure.twoD.Neighbourhood2D;
import javafx.scene.paint.Color;

import java.util.*;

public class CABuilder {
    public CelluralAutomata2D CA;
    int height;
    int width;
    Random r;
    Map<Integer, Color> types;
    boolean choiceHeksa;
    int choicePenta;

    List<Cell2D> edgeCellList;
    List<Cell2D> insideCellList;

    public CABuilder(CelluralAutomata2D celluralAutomata2D) {
        this.CA = celluralAutomata2D;
        this.height = celluralAutomata2D.getHeight();
        this.width = celluralAutomata2D.getWidth();
        r = new Random();
        types = new HashMap<>();
    }

    public void setAbsorbic() {
        Cell2D[][] temp = new Cell2D[height][width];
        for (int i = 0; i <= (height - 1); i++) {
            for (int j = 0; j <= (width - 1); j++) {

                if (i == 0) CA.matrix[i][j].setState(CA.matrix[i][j].getState());
                if (i == (height - 1))
                    CA.matrix[i][j].setState(CA.matrix[i][j].getState());
                if (j == 0) CA.matrix[i][j].setState(CA.matrix[i][j].getState());
                if (j == (width - 1))
                    CA.matrix[i][j].setState(CA.matrix[i][j].getState());


            }
        }
    }

    public void setPeriodic() {

        for (int i = 0; i < (height); i++) {
            for (int j = 0; j < (width); j++) {

                if (i == 0 && j > 0 && j < (width - 1)) {
                    CA.matrix[i][j] = CA.matrix[height - 2][j];
                }

                if (i == (height - 1) && j > 0 && j < (width - 1)) {
                    CA.matrix[i][j] = CA.matrix[1][j];
                }

                if (j == 0 && i > 0 && i < (height - 1)) {
                    CA.matrix[i][j] = CA.matrix[i][width - 2];
                }
                if (j == (width - 1) && i > 0 && i < (height - 1)) {
                    CA.matrix[i][j] = CA.matrix[i][1];
                }


            }
        }

        CA.matrix[1][1] = CA.matrix[height - 2][width - 2];
        CA.matrix[0][width - 1] = CA.matrix[height - 2][1];
        CA.matrix[height - 1][0] = CA.matrix[1][width - 2];
        CA.matrix[height - 1][width - 1] = CA.matrix[1][1];

    }

    public void buildHomogenous(int ca, int ra) {
        int h = this.height;
        int w = this.width;
        int r = ((h) / (ra));
        int c = (((w) / (ca)));

        int k = 1;

        for (int i = r - 1; i < (r * ra); i += (r)) {
            for (int j = c - 1; j < (c * ca); j += (c)) {

                CA.matrix[i][j].setState(k);
                k++;

                Color cc = generateColor();
                CA.matrix[i][j].setCellColor(cc);
                types.put(i, cc);
            }
        }

    }

    public void buildWithRadius(int amount, int radius) {

        int l = 1;
        boolean can = true;

        int possibleAmount;
        if (width > height) possibleAmount = height / (2 * radius);
        else possibleAmount = width / (2 * radius);


        while (l <= amount && l <= possibleAmount) {
            int x = (r.nextInt(height - 1) + 1);
            int y = (r.nextInt(width - 1) + 1);


            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    double dist = CA.matrix[x][y].getDistance(i, j);
                    if (dist < radius && CA.matrix[i][j].getState() != 0) {
                        can = false;
                    }

                }
            }
            if (can) {
                if (CA.matrix[x][y].getState() == 0) {
                    CA.matrix[x][y] = new Cell2D(l, x, y, generateColor());
                    l++;

                }
            }

        }
    }


    public void buildRandom(int amount) {

        for (int i = 1; i < amount + 1; i++) {

            int x = (r.nextInt(height - 1) + 1);
            int y = (r.nextInt(width - 1) + 1);

            CA.matrix[x][y].setState(i);

            Color c = generateColor();
            types.put(i, c);
            CA.matrix[x][y].setColorCode(i);
            System.out.println(CA.matrix[x][y].getColorCode());
            CA.matrix[x][y].setCellColor(c);


        }
    }

    public boolean isOver() {
        int c = 0;
        for (int i = 0; i < (height); i++) {
            for (int j = 0; j < (width); j++) {

                if (CA.matrix[i][j].getState() == 0) c++;


            }
        }
        if (c == 0) return true;
        else return false;
    }

    public void setCAState(int x, int y) {
        CA.matrix[y][x].setPosition(y, x);
        CA.matrix[y][x].setState(1);
        CA.matrix[y][x].setCellColor(generateColor());
    }

    public int getState(int y, int x) {
        return CA.matrix[y][x].getState();
    }

    public Color getColor(int x, int y) {
        return CA.matrix[x][y].getCellColor();
    }

    public Color generateColor() {
        int R = (int) (Math.random() * 256);
        int G = (int) (Math.random() * 256);
        int B = (int) (Math.random() * 256);
        Color color = Color.rgb(R, G, B);
        return color;
    }

    public void simulation(Neighbourhood2D neighbourhood, BoundaryCondition bc) {

        choiceHeksa = r.nextBoolean();
        choicePenta = r.nextInt(4);
        Cell2D[][] tempBoard = new Cell2D[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {


                List<Cell2D> n = new ArrayList<>();
                if (getState(i, j) == 0) {

                    switch (neighbourhood) {
                        case Moore:
                            n = Moore(i, j);
                            break;
                        case vonNeumann:
                            n = vonNeumann(i, j, CA.matrix);
                            break;
                        case PentagonalLeft:
                            n = pentagonalLeft(i, j, CA.matrix);
                            break;
                        case PentagonalRight:
                            n = pentagonalRight(i, j, CA.matrix);
                            break;
                        case PentagonalDown:
                            n = pentagonalDown(i, j, CA.matrix);
                            break;
                        case PentagonalUp:
                            n = pentagonalUp(i, j, CA.matrix);
                            break;
                        case PentagonalRandom:
                            n = pentagonalRandom(i, j, CA.matrix);
                            break;
                        case HeksagonalLeft:
                            n = heksagonalLeft(i, j, CA.matrix);
                            break;
                        case HeksagonalRight:
                            n = hekasgonalRight(i, j, CA.matrix);
                            break;
                        case HeksagonalRandom:
                            n = heksagonalRandom(i, j, CA.matrix);
                            break;
                        default:
                            System.out.println("Nie ma takiego sąsiedztwa");
                    }


                    Cell2D fMax = findMaxCell(n);
                    tempBoard[i][j] = new Cell2D(fMax.getState(), i, j, fMax.getCellColor(), fMax.getColorCode());
                    System.out.println(fMax.getColorCode());


                } else {

                    tempBoard[i][j] = new Cell2D(CA.matrix[i][j].getState(), i, j, CA.matrix[i][j].getCellColor(), CA.matrix[i][j].getColorCode());
                    System.out.println(CA.matrix[i][j].getColorCode());

                }


            }
        }

        CA.matrix = tempBoard;
        if (bc == BoundaryCondition.absorbic) setAbsorbic();
        if (bc == BoundaryCondition.periodic) setPeriodic();
        //lista komórek na krawędziach ziaren

        edgeCellList = createEdgeList();
//        System.out.println("\nEdge:\n");
//        System.out.println(edgeCellList);
//        //lista komórek wewnątrz ziaren
//        System.out.println("\nInside\n");
        insideCellList = createInsideList();
        //   System.out.println(insideCellList);

    }

    public Cell2D findMaxCell(List<Cell2D> neighbours) {
        if (neighbours.isEmpty()) return new Cell2D();
        else {

            Cell2D max = new Cell2D();

            //zliczanie ile jest id takich samych
            Map<Integer, Integer> mapa = new HashMap<>();
            for (Cell2D cc : neighbours) {
                if (mapa.containsKey(cc.getState())) {
                    Integer value = mapa.get(cc.getState());
                    mapa.put(cc.getState(), value + 1);
                } else {
                    mapa.put(cc.getState(), 1);
                }

            }

            //szukanie najczęściej występującego id
            Map.Entry<Integer, Integer> maxEntry = null;
            Random random = new Random();

            for (Map.Entry<Integer, Integer> entry : mapa.entrySet()) {
                if (maxEntry == null || entry.getValue() >= maxEntry.getValue()) {
                    if (maxEntry != null && entry.getValue() == maxEntry.getValue()) {
                        random = new Random();
                        Boolean choice = random.nextBoolean();
                        if (choice) {
                            maxEntry = entry;
                        } else continue;

                    } else {
                        maxEntry = entry;
                    }


                }
            }

            // szukam po ID wybraną komórkę z listy sąsiadów i zwracam ją
            for (Cell2D g : neighbours) {
                if (g.getState() == maxEntry.getKey()) {
                    max = g;
                }
            }

            return max;
        }

    }


    //-----------------------------------------------//
    //sąsiedztwo Moore'a
    public List<Cell2D> Moore(int x, int y) {

        List<Cell2D> neighbours = new ArrayList<>();

        //tworzę listę komórek będących sąsiadami
        for (int i = x - 1; i <= x + 1; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y - 1; j <= y + 1; j++) {

                if (j < 0 || j > width - 1) continue;

                if (CA.matrix[i][j].getState() != 0) {
                    neighbours.add(new Cell2D(CA.matrix[i][j].getState(), CA.matrix[i][j].getX(),
                            CA.matrix[i][j].getY(), CA.matrix[i][j].getCellColor(), CA.matrix[i][j].getColorCode()));
                }

            }
        }


        return neighbours;

    }


    //-------------------------------------//
    //sasiedztwo vonNeumanna
    public List<Cell2D> vonNeumann(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();
        if ((x - 1) >= 0 && matrix[x - 1][y].getState() != 0)
            neighbours.add(new Cell2D(matrix[x - 1][y].getState(), x - 1, y, matrix[x - 1][y].getCellColor(), matrix[x - 1][y].getColorCode()));
        if ((x + 1) < height && matrix[x + 1][y].getState() != 0)
            neighbours.add(new Cell2D(matrix[x + 1][y].getState(), x + 1, y, matrix[x + 1][y].getCellColor(), matrix[x + 1][y].getColorCode()));
        if ((y - 1) >= 0 && matrix[x][y - 1].getState() != 0)
            neighbours.add(new Cell2D(matrix[x][y - 1].getState(), x, y - 1, matrix[x][y - 1].getCellColor(), matrix[x][y - 1].getColorCode()));
        if ((y + 1) < width && matrix[x][y + 1].getState() != 0)
            neighbours.add(new Cell2D(matrix[x][y + 1].getState(), x, y + 1, matrix[x][y + 1].getCellColor(), matrix[x][y + 1].getColorCode()));


        return neighbours;
    }

    //----------------------------//
    //pentagonalne lewe
    public List<Cell2D> pentagonalLeft(int x, int y, Cell2D[][] matrix) {


        List<Cell2D> neighbours = new ArrayList<>();

        for (int i = x - 1; i <= x + 1; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y; j <= y + 1; j++) {

                if (j < 0 || j > width - 1) continue;
                if (i == x && j == y) continue;

                if (CA.matrix[i][j].getState() != 0) {


                    neighbours.add(matrix[i][j]);
//                        neighbours.add(new Cell(matrix[i][j].getState(), matrix[i][j].getX(),
//                                matrix[i][j].getY(), matrix[i][j].getCellColor(), matrix[i][j].isRecristal(), matrix[i][j].getDensdislocation()));}

                }
            }
        }

        return neighbours;
    }

    //--------------------------------------------------------//
    //pentagonlne prawe
    public List<Cell2D> pentagonalRight(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();

        for (int i = x - 1; i <= x + 1; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y - 1; j <= y; j++) {

                if (j < 0 || j > width - 1) continue;

                if ((i == x && j == y)) continue;
                if (CA.matrix[i][j].getState() != 0) {
                    neighbours.add(new Cell2D(CA.matrix[i][j].getState(), CA.matrix[i][j].getX(),
                            CA.matrix[i][j].getY(), CA.matrix[i][j].getCellColor()));
                }

            }
        }

        return neighbours;

    }


    //--------------------------------------------//
    //pentagonalne góra
    public List<Cell2D> pentagonalUp(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();

        for (int i = x; i <= x + 1; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y - 1; j <= y + 1; j++) {

                if (j < 0 || j > width - 1) continue;

                if ((i == x && j == y)) continue;
                if (CA.matrix[i][j].getState() != 0) {
                    neighbours.add(new Cell2D(CA.matrix[i][j].getState(), CA.matrix[i][j].getX(),
                            CA.matrix[i][j].getY(), CA.matrix[i][j].getCellColor()));
                }

            }
        }

        return neighbours;
    }

    //------------------------------------------------------//
    //pentagonalne góra
    public List<Cell2D> pentagonalDown(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();

        for (int i = x - 1; i <= x; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y - 1; j <= y + 1; j++) {

                if (j < 0 || j > width - 1) continue;

                if ((i == x && j == y)) continue;
                if (CA.matrix[i][j].getState() != 0) {
                    neighbours.add(new Cell2D(CA.matrix[i][j].getState(), CA.matrix[i][j].getX(),
                            CA.matrix[i][j].getY(), CA.matrix[i][j].getCellColor()));
                }

            }
        }

        return neighbours;
    }

    //-----------------------------------------------------//
    //penatgonalne losowe
    public List<Cell2D> pentagonalRandom(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();
        switch (choicePenta) {
            case 0:
                neighbours = pentagonalDown(x, y, matrix);
                break;
            case 1:
                neighbours = pentagonalLeft(x, y, matrix);
                break;
            case 2:
                neighbours = pentagonalRight(x, y, matrix);
                break;
            case 3:
                neighbours = pentagonalUp(x, y, matrix);
                break;
            default:
                System.out.println("Wrong choice!!!");

        }

        return neighbours;
    }

    //--------------------------------------------------//
    //hekasgonalne lewe
    public List<Cell2D> heksagonalLeft(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();

        //tworzę listę komórek będących sąsiadami
        for (int i = x - 1; i <= x + 1; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y - 1; j <= y + 1; j++) {

                if (j < 0 || j > width - 1) continue;

                if ((j == y - 1 && i == x - 1) || (j == y + 1 && i == x + 1) || (i == x && j == y)) continue;
                if (CA.matrix[i][j].getState() != 0) {
                    neighbours.add(new Cell2D(CA.matrix[i][j].getState(), CA.matrix[i][j].getX(),
                            CA.matrix[i][j].getY(), CA.matrix[i][j].getCellColor()));
                }

            }
        }


        return neighbours;
    }

    //-----------------------------------------------------//
    //heksagonalne prawe
    public List<Cell2D> hekasgonalRight(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();

        //tworzę listę komórek będących sąsiadami
        for (int i = x - 1; i <= x + 1; i++) {

            if (i < 0 || i > height - 1) continue;

            for (int j = y - 1; j <= y + 1; j++) {

                if (j < 0 || j > width - 1) continue;

                if ((j == y + 1 && i == x - 1) || (j == y - 1 && i == x + 1) || (j == y && i == x)) continue;
                if (CA.matrix[i][j].getState() != 0) {
                    neighbours.add(matrix[i][j]);
//                    neighbours.add(new Cell(matrix[i][j].getState(), matrix[i][j].getX(),
//                            matrix[i][j].getY(), matrix[i][j].getCellColor()));
                }

            }
        }


        return neighbours;
    }

    //------------------------------------------------------//
    //heksagonalne losowe
    public List<Cell2D> heksagonalRandom(int x, int y, Cell2D[][] matrix) {
        List<Cell2D> neighbours = new ArrayList<>();


        if (choiceHeksa) {

            neighbours = hekasgonalRight(x, y, matrix);
        } else {

            neighbours = heksagonalLeft(x, y, matrix);
        }

        return neighbours;
    }

    List<Cell2D> createInsideList() {
        List<Cell2D> listAllCells = new ArrayList<>();
        for (int i = 0; i < (height); i++) {
            for (int j = 0; j < (width); j++) {
                if (!isOnEdge(i, j, CA.matrix) && !listAllCells.contains(CA.matrix[i][j])) {
                    listAllCells.add(CA.matrix[i][j]);
                }

            }
        }

        return listAllCells;
    }

    List<Cell2D> createEdgeList() {
        List<Cell2D> listAllCells = new ArrayList<>();
        for (int i = 0; i < (height); i++) {
            for (int j = 0; j < (width); j++) {
                if (isOnEdge(i, j, CA.matrix) && !listAllCells.contains(CA.matrix[i][j])) {
                    listAllCells.add(CA.matrix[i][j]);
                }

            }
        }

        return listAllCells;
    }

    //sprawdzanie czy komórka leży na granicy ziarna
    boolean isOnEdge(int x, int y, Cell2D[][] matrix) {
        boolean is = false;
        // List<Cell> neighbours = MCMoore(x,y,matrix);
        List<Cell2D> neighbours = new ArrayList<>();
        if ((x - 1) > 0 && CA.matrix[x - 1][y].getState() != 0)
            neighbours.add(new Cell2D(matrix[x - 1][y].getState(), x - 1, y, matrix[x - 1][y].getCellColor()));
        if ((x + 1) < height && matrix[x + 1][y].getState() != 0)
            neighbours.add(new Cell2D(matrix[x + 1][y].getState(), x + 1, y, matrix[x + 1][y].getCellColor()));
        if ((y - 1) > 0 && matrix[x][y - 1].getState() != 0)
            neighbours.add(new Cell2D(matrix[x][y - 1].getState(), x, y - 1, matrix[x][y - 1].getCellColor()));
        if ((y + 1) < width && matrix[x][y + 1].getState() != 0)
            neighbours.add(new Cell2D(matrix[x][y + 1].getState(), x, y + 1, matrix[x][y + 1].getCellColor()));

        for (Cell2D n : neighbours) {
            if (n.getState() != matrix[x][y].getState()) is = true;

        }

        return is;
    }

    public int getTypeSize() {
        return types.size();
    }
}
