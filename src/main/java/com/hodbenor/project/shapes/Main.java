package com.hodbenor.project.shapes;

import com.hodbenor.project.shapes.rectangle.IRectangle;
import com.hodbenor.project.shapes.rectangle.IRectanglesStore;
import com.hodbenor.project.shapes.rectangle.RectanglesStoreImpl;
import com.hodbenor.project.shapes.rectangle.RegularRectangle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        IRectangle bounds = new RegularRectangle(0, 100, 100, 0, new Properties("Store Bounds"));
        System.out.println("Store Bounds: " + bounds);

        List<IRectangle> rectangles = new ArrayList<>();
        rectangles.add(new RegularRectangle(10, 30, 30, 10, new Properties("Rect A (Small Top-Left)")));
        rectangles.add(new RegularRectangle(50, 80, 80, 50, new Properties("Rect B (Mid)")));
        rectangles.add(new RegularRectangle(20, 60, 60, 20, new Properties("Rect C (Overlaps A, B)")));
        rectangles.add(new RegularRectangle(0, 100, 10, 90, new Properties("Rect D (Bottom-Left Edge)")));
        rectangles.add(new RegularRectangle(90, 10, 100, 0, new Properties("Rect E (Top-Right Edge)")));
        rectangles.add(new RegularRectangle(25, 45, 45, 25, new Properties("Rect F (Inside C, High Z)")));

        System.out.println("Rectangles to add:");
        for (IRectangle rect : rectangles) {
            System.out.println("  " + rect);
        }

        IRectanglesStore store = new RectanglesStoreImpl();

        try {
            store.initialize(bounds, rectangles);
            System.out.println("RectanglesStore initialized successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error during initialization: " + e.getMessage());
            return;
        }

        System.out.println("--- Executing findRectangleAt queries ---");

        queryAndPrint(store, 30, 30, "Point (30, 30) - Inside Rect F, also C and A");
        queryAndPrint(store, 70, 70, "Point (70, 70) - Inside Rect B");
        queryAndPrint(store, 15, 15, "Point (15, 15) - Inside Rect A");
        queryAndPrint(store, 5, 95, "Point (5, 95) - On Rect D's edge");
        queryAndPrint(store, 90, 90, "Point (90, 90) - Outside all rectangles, inside bounds");
        queryAndPrint(store, -5, -5, "Point (-5, -5) - Outside store bounds");
        queryAndPrint(store, 105, 105, "Point (105, 105) - Outside store bounds");

        System.out.println("\nRectanglesStore Test Finished.");
    }

    private static void queryAndPrint(IRectanglesStore store, int x, int y, String description) {
        IRectangle foundRectangle = store.findRectangleAt(x, y);
        System.out.print(description + ": ");
        if (foundRectangle != null) {
            System.out.println("Found " + foundRectangle);
        } else {
            System.out.println("No rectangle found.");
        }
    }
}