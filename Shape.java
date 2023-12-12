import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

enum ShapeType {
  PENCIL,
  EREASER,
  LINE,
  RECTANGLE,
  OVAL,
}

public abstract class Shape {

  protected Point startPoint;
  protected Point endPoint;
  private Color color;
  private boolean isFilled;
  private ShapeType shapeType;

  public abstract void draw(Graphics g);

  public abstract void fill(Graphics g);

  public abstract Shape copy();

  public void reset() {
    startPoint = new Point();
    endPoint = new Point();
  }

  public void setEndPoint(Point endPoint) {
    this.endPoint = endPoint;
  }

  public Point getEndPoint() {
    return endPoint;
  }

  public void setShapeType(ShapeType shapeType) {
    this.shapeType = shapeType;
  }

  public ShapeType getShapeType() {
    return shapeType;
  }

  public void setStartPoint(Point startPoint) {
    this.startPoint = startPoint;
  }

  public Point getStartPoint() {
    return startPoint;
  }

  public int getWidth() {
    return Math.abs(endPoint.x - startPoint.x);
  }

  public int getHeight() {
    return Math.abs(endPoint.y - startPoint.y);
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }

  public void setFilled(boolean isFilled) {
    this.isFilled = isFilled;
  }

  public boolean isFilled() {
    return isFilled;
  }
}

class Line extends Shape {

  public Line() {
    startPoint = new Point();
    endPoint = new Point();
    setShapeType(ShapeType.LINE);
  }

  public void draw(Graphics g) {
    g.setColor(getColor());
    g.drawLine(
      getStartPoint().x,
      getStartPoint().y,
      getEndPoint().x,
      getEndPoint().y
    );
  }

  public void fill(Graphics g) {
    draw(g);
  }

  public void reset() {
    startPoint = new Point();
    endPoint = new Point();
  }

  public Shape copy() {
    Line line = new Line();
    line.setStartPoint(getStartPoint());
    line.setEndPoint(getEndPoint());
    line.setColor(getColor());
    line.setFilled(isFilled());
    return line;
  }
}

class Oval extends Shape {

  public Oval() {
    startPoint = new Point();
    endPoint = new Point();
    setShapeType(ShapeType.OVAL);
  }

  public void draw(Graphics g) {
    g.setColor(getColor());

    if (isFilled()) {
      fill(g);
    } else {
      g.drawOval(getStartPoint().x, getStartPoint().y, getWidth(), getHeight());
    }
  }

  public void fill(Graphics g) {
    g.setColor(getColor());
    g.fillOval(getStartPoint().x, getStartPoint().y, getWidth(), getHeight());
  }

  public Shape copy() {
    Oval oval = new Oval();
    oval.setStartPoint(getStartPoint());
    oval.setEndPoint(getEndPoint());
    oval.setColor(getColor());
    oval.setFilled(isFilled());
    return oval;
  }
}

class Rectangle extends Shape {

  public Rectangle() {
    startPoint = new Point();
    endPoint = new Point();
    setShapeType(ShapeType.RECTANGLE);
  }

  public void draw(Graphics g) {
    g.setColor(getColor());

    if (isFilled()) {
      fill(g);
    } else {
      g.drawRect(getStartPoint().x, getStartPoint().y, getWidth(), getHeight());
    }
  }

  public void fill(Graphics g) {
    g.setColor(getColor());
    g.fillRect(getStartPoint().x, getStartPoint().y, getWidth(), getHeight());
  }

  public Shape copy() {
    Rectangle rectangle = new Rectangle();
    rectangle.setStartPoint(getStartPoint());
    rectangle.setEndPoint(getEndPoint());
    rectangle.setColor(getColor());
    rectangle.setFilled(isFilled());
    return rectangle;
  }
}

class Pencil extends Shape {

  ArrayList<Rectangle> lineStream = new ArrayList<Rectangle>();

  public void draw(Graphics g) {
    g.setColor(getColor());

    for (Rectangle rec : lineStream) rec.draw(g);
  }

  public void addPixel(Point p) {
    Rectangle r = new Rectangle();
    r.setFilled(true);
    r.setColor(getColor());
    r.setStartPoint(p);
    r.setEndPoint(new Point((int) p.getX() + 3, (int) p.getY() + 3));
    lineStream.add(r);
  }

  public void fill(Graphics g) {
    draw(g);
  }

  public Shape copy() {
    Pencil pencil = new Pencil();
    pencil.setStartPoint(getStartPoint());
    pencil.setEndPoint(getEndPoint());
    pencil.setColor(getColor());
    pencil.setFilled(isFilled());

    for (Rectangle line : this.lineStream) pencil.lineStream.add(line);
    return pencil;
  }

  public void reset() {
    lineStream.clear();
  }
}

class Ereaser extends Shape {

  ArrayList<Rectangle> lineStream = new ArrayList<Rectangle>();

  public void draw(Graphics g) {
    g.setColor(Color.WHITE);

    for (Rectangle rec : lineStream) rec.draw(g);
  }

  public void addPixel(Point p) {
    Rectangle r = new Rectangle();
    r.setFilled(true);
    r.setColor(Color.WHITE);
    r.setStartPoint(p);
    r.setEndPoint(new Point((int) p.getX() + 3, (int) p.getY() + 3));
    lineStream.add(r);
  }

  public void setColor(Color color) {
    super.setColor(Color.WHITE);
  }

  public void fill(Graphics g) {
    draw(g);
  }

  public Shape copy() {
    Ereaser ereaser = new Ereaser();
    ereaser.setStartPoint(getStartPoint());
    ereaser.setEndPoint(getEndPoint());
    ereaser.setColor(getColor());
    ereaser.setFilled(isFilled());

    for (Rectangle line : this.lineStream) ereaser.lineStream.add(line);
    return ereaser;
  }

  public void reset() {
    lineStream.clear();
  }
}
