package com.hodbenor.project.shapes.rectangle;

import com.hodbenor.project.shapes.QuadTree;

import java.util.List;

public class RectanglesStoreImpl implements IRectanglesStore {
    private QuadTree root;

    @Override
    public void initialize(IRectangle bounds, List<IRectangle> rectangles) {
        if (!isValidBounds(bounds)) {
            throw new IllegalArgumentException("Invalid bounds for the rectangles store");
        }

        this.root = new QuadTree(bounds, 0);

        for (IRectangle rectangle : rectangles) {
            if (!isValidBounds(rectangle)) {
                System.err.println("Error: Skipping invalid rectangle: " + rectangle);
                continue;
            }

            if (!((RegularRectangle) bounds).isOverlap(rectangle)) {
                System.err.println("Error: Skipping rectangle " + rectangle + " that is outside of the store bounds: "
                        + bounds);
                continue;
            }

            root.insert(rectangle);
        }
    }

    private boolean isValidBounds(IRectangle bounds) {
        return bounds != null && bounds.getLeft() < bounds.getRight() && bounds.getBottom() < bounds.getTop();
    }

    @Override
    public IRectangle findRectangleAt(int x, int y) {
        if (root == null) {
            return null;
        }

        if (!((RegularRectangle) root.getBounds()).contains(x, y)) {
            return null;
        }

        List<IRectangle> candidates = root.findAllRectangles(x, y);

        for (int i = candidates.size() - 1; i >= 0; i--) {
            IRectangle rectangle = candidates.get(i);
            if (rectangle instanceof RegularRectangle && ((RegularRectangle) rectangle).contains(x, y)) {
                return rectangle;
            }
        }

        return null;
    }
}

