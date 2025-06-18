package com.hodbenor.project.shapes.rectangle;

import java.util.List;

public interface IRectanglesStore {
    void initialize(IRectangle bounds, List<IRectangle> rectangles);
    IRectangle findRectangleAt(int x, int y);
}

