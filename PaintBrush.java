import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class PaintBrush extends Applet {

  private Color[] colors = {
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.YELLOW,
    Color.ORANGE,
    Color.GRAY,
    Color.PINK,
    Color.CYAN,
    Color.MAGENTA,
    Color.BLACK,
    Color.WHITE,
  };

  ArrayList<Shape> shapesList = new ArrayList<Shape>();
  private Color currentColor = Color.RED;
  private Shape currentDrawingShape = new Rectangle();
  private ShapeType currentSelectedShape = ShapeType.RECTANGLE;
  private Boolean isFilled = false;
  private Boolean isDraggable = false;
  private Stack<Shape> undoStack = new Stack<Shape>();
  private JButton undoButton;
  private JButton redoButton;
  private JButton clearButton;

  public void init() {
    addMouseListener(
      new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
          switch (currentSelectedShape) {
            case PENCIL:
              currentDrawingShape = new Pencil();
              break;
            case EREASER:
              currentDrawingShape = new Ereaser();
              break;
            case LINE:
              currentDrawingShape = new Line();
              break;
            case OVAL:
              currentDrawingShape = new Oval();
              break;
            case RECTANGLE:
              currentDrawingShape = new Rectangle();
              break;
          }

          currentDrawingShape.setColor(currentColor);
          currentDrawingShape.setFilled(isFilled);
          currentDrawingShape.setStartPoint(e.getPoint());
          isDraggable = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
          if (!currentDrawingShape.getStartPoint().equals(e.getPoint())) {
            shapesList.add(currentDrawingShape.copy());
            isDraggable = false;
            currentDrawingShape.reset();
            repaint();
          }
        }
      }
    );

    addMouseMotionListener(
      new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
          if (isDraggable) {
            if (currentSelectedShape == ShapeType.PENCIL) {
              Pencil p = (Pencil) currentDrawingShape;
              p.addPixel(e.getPoint());
            } else if (currentSelectedShape == ShapeType.EREASER) {
              Ereaser e1 = (Ereaser) currentDrawingShape;
              e1.addPixel(e.getPoint());
            } else currentDrawingShape.setEndPoint(e.getPoint());
            repaint();
          }
        }
      }
    );
    drawLayout();
  }

  public void drawLayout() {
    drawColorsButtons();
    drawShapesButtons();
    drawEditButtons();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(currentColor);

    checkEditButtonsState();
    drawCurrentSelectedShapes(g);

    if (currentDrawingShape != null) currentDrawingShape.draw(g);
  }

  void drawColorsButtons() {
    JCheckBox checkBox = new JCheckBox("Filled");
    checkBox.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          isFilled = checkBox.isSelected();
          currentDrawingShape.setFilled(checkBox.isSelected());
        }
      }
    );

    checkBox.setBounds(10, 90, 70, 30);
    add(checkBox);

    for (int i = 0; i < colors.length; i++) {
      JButton button = new JButton();
      button.setBounds(10 + i * 30, 10, 30, 30);
      button.setCursor(new Cursor(Cursor.HAND_CURSOR));
      button.setBackground(colors[i]);
      button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            currentColor = button.getBackground();
          }
        }
      );
      add(button);
    }
  }

  void drawShapesButtons() {
    ButtonGroup buttonGroup = new ButtonGroup();
    for (int i = 0; i < ShapeType.values().length; i++) {
      ShapeType shapeType = ShapeType.values()[i];
      JButton button = new JButton(shapeType.toString());
      button.setBounds(10, 50 + i * 30, 100, 30);
      button.setCursor(new Cursor(Cursor.HAND_CURSOR));

      button.setBorderPainted(true);
      button.setContentAreaFilled(false);
      button.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            currentSelectedShape = shapeType;
          }
        }
      );
      add(button);
      buttonGroup.add(button);
    }
  }

  void drawEditButtons() {
    ButtonGroup buttonGroup = new ButtonGroup();

    undoButton = new JButton("Undo");
    undoButton.setBounds(10, 50, 100, 30);
    undoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    undoButton.setEnabled(false);
    undoButton.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          undo();
        }
      }
    );
    add(undoButton);
    buttonGroup.add(undoButton);

    redoButton = new JButton("Redo");
    redoButton.setBounds(10, 50, 100, 30);
    redoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    redoButton.setEnabled(false);
    redoButton.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          redo();
        }
      }
    );
    add(redoButton);
    buttonGroup.add(redoButton);

    clearButton = new JButton("Clear");
    clearButton.setBounds(10, 50, 100, 30);
    clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    clearButton.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          clear();
        }
      }
    );
    add(clearButton);
    buttonGroup.add(clearButton);
  }

  void drawCurrentSelectedShapes(Graphics g) {
    for (Shape shape : shapesList) {
      shape.draw(g);
    }
    if (isDraggable) currentDrawingShape.draw(g);
  }

  void checkEditButtonsState() {
    if (shapesList.size() > 0) {
      undoButton.setEnabled(true);
    } else {
      undoButton.setEnabled(false);
    }

    if (undoStack.size() > 0) {
      redoButton.setEnabled(true);
    } else {
      redoButton.setEnabled(false);
    }
  }

  void undo() {
    if (shapesList.size() > 0) {
      undoStack.push(shapesList.get(shapesList.size() - 1));
      shapesList.remove(shapesList.size() - 1);
      repaint();
    }
  }

  void redo() {
    if (undoStack.size() > 0) {
      shapesList.add(undoStack.pop());
      repaint();
    }
  }

  void clear() {
    undoStack.clear();
    shapesList.clear();
    repaint();
  }
}
