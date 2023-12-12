import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

class Line {

  public Point startPoint;
  public Point endPoint;

  public Line() {
    startPoint = new Point();
    endPoint = new Point();
  }
}

public class Drawer extends Applet {

  private Boolean isDraggable = false;
  private ArrayList<Line> linesList;
  private Line newLine = null;
  private int currentLines = 0;

  public void init() {
    linesList = new ArrayList<Line>();
    addMouseListener(
      new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
          newLine = new Line();
          isDraggable = true;

          newLine.startPoint.x = e.getPoint().x;
          newLine.startPoint.y = e.getPoint().y;
        }

        public void mouseReleased(MouseEvent e) {
          newLine.endPoint.x = e.getPoint().x;
          newLine.endPoint.y = e.getPoint().y;

          linesList.add(newLine);
          currentLines++;
          isDraggable = false;
        }
      }
    );

    addMouseMotionListener(
      new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
          if (isDraggable) {
            newLine.endPoint.x = e.getPoint().x;
            newLine.endPoint.y = e.getPoint().y;
            repaint();
          }
        }
      }
    );
  }

  public void paint(Graphics g) {
    for (int i = 0; i < currentLines; i++) {
      g.drawLine(
        linesList.get(i).startPoint.x,
        linesList.get(i).startPoint.y,
        linesList.get(i).endPoint.x,
        linesList.get(i).endPoint.y
      );
    }
    if (newLine != null) g.drawLine(
      newLine.startPoint.x,
      newLine.startPoint.y,
      newLine.endPoint.x,
      newLine.endPoint.y
    );
  }
}
