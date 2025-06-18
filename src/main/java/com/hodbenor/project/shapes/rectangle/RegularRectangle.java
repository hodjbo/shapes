package com.hodbenor.project.shapes.rectangle;

import com.hodbenor.project.shapes.Properties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegularRectangle implements IRectangle {

    private final int left;
    private final int top;
    private final int right;
    private final int bottom;
    private final Properties properties;

    public boolean contains(int x, int y) {
        return x >= left && x < right && y < top && y >= bottom;
    }

    public boolean isOverlap(IRectangle other) {
        return !(other.getRight() < left || other.getLeft() > right ||
                other.getBottom() > top || other.getTop() < bottom);
    }
}
