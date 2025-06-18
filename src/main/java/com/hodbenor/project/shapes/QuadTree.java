package com.hodbenor.project.shapes;

import com.hodbenor.project.shapes.rectangle.IRectangle;
import com.hodbenor.project.shapes.rectangle.RegularRectangle;

import java.util.*;

public class QuadTree {
    private static final int MAX_RECTANGLES_PER_NODE = 4;

    public IRectangle getBounds() {
        return bounds;
    }

    private final IRectangle bounds;
    private final List<IRectangle> rectangles;
    private final QuadTree[] children;
    private final int depth;
    private final double midX;
    private final double midY;

    public QuadTree(IRectangle bounds, int depth) {
        this.bounds = bounds;
        this.depth = depth;
        this.rectangles = new ArrayList<>();
        this.children = new QuadTree[4];
        this.midX = (double) (bounds.getLeft() + bounds.getRight()) / 2;
        this.midY = (double) (bounds.getTop() + bounds.getBottom()) / 2;
    }

    public void insert(IRectangle rect) {
        if (children[0] != null) {
            int index = getChildIndex(rect);
            if (index != -1) {
                children[index].insert(rect);
                return;
            }
        }

        rectangles.add(rect);

        if (rectangles.size() > MAX_RECTANGLES_PER_NODE) {
            if (children[0] == null) {
                subdivide();
            }

            List<IRectangle> remainingRectangles = new ArrayList<>();
            for (IRectangle r : rectangles) {
                int index = getChildIndex(r);
                if (index != -1) {
                    children[index].insert(r);
                } else {
                    remainingRectangles.add(r);
                }
            }
            rectangles.clear();
            rectangles.addAll(remainingRectangles);
        }
    }

    public List<IRectangle> findAllRectangles(int x, int y) {
        List<IRectangle> foundRectangles = new ArrayList<>();

        for (IRectangle rectangle : rectangles) {
            if (rectangle instanceof RegularRectangle && ((RegularRectangle) rectangle).contains(x, y)) {
                foundRectangles.add(rectangle);
            }
        }

        if (children[0] != null) {
            if (isTopLeft(x, y)) {
                foundRectangles.addAll(children[0].findAllRectangles(x, y));
            } else if (isTopRight(x, y)) {
                foundRectangles.addAll(children[1].findAllRectangles(x, y));
            } else if (isBottomLeft(x, y)) {
                foundRectangles.addAll(children[2].findAllRectangles(x, y));
            } else if (isBottomRight(x, y)) {
                foundRectangles.addAll(children[3].findAllRectangles(x, y));
            }
        }

        return foundRectangles;
    }

    private boolean isTopLeft(int x, int y) {
        return x < midX && y > midY;
    }

    private boolean isTopRight(int x, int y) {
        return x >= midX && y > midY;
    }

    private boolean isBottomLeft(int x, int y) {
        return x < midX && y <= midY;
    }

    private boolean isBottomRight(int x, int y) {
        return x >= midX && y <= midY;
    }

    private int getChildIndex(IRectangle rect) {
        boolean fitsTop = rect.getBottom() >= midY;
        boolean fitsBottom = rect.getTop() <= midY;
        boolean fitsLeft = rect.getRight() <= midX;
        boolean fitsRight = rect.getLeft() >= midX;

        if (fitsTop && fitsLeft) return 0;
        else if (fitsTop && fitsRight) return 1;
        else if (fitsBottom && fitsLeft) return 2;
        else if (fitsBottom && fitsRight) return 3;

        return -1;
    }

    private void subdivide() {
        int left = bounds.getLeft();
        int top = bounds.getTop();
        int right = bounds.getRight();
        int bottom = bounds.getBottom();

        int midX = left + (right - left) / 2;
        int midY = top + (bottom - top) / 2;

        children[0] = new QuadTree(new RegularRectangle(left, top, midX, midY, new Properties("TL")), depth + 1);
        children[1] = new QuadTree(new RegularRectangle(midX, top, right, midY, new Properties("TR")), depth + 1);
        children[2] = new QuadTree(new RegularRectangle(left, midY, midX, bottom, new Properties("BL")), depth + 1);
        children[3] = new QuadTree(new RegularRectangle(midX, midY, right, bottom, new Properties("BR")), depth + 1);
    }
}