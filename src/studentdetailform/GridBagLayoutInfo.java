package studentdetailform;

import java.awt.*;


public class GridBagLayoutInfo {
    public static int getRow(Container container, Component component) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component c = container.getComponent(i);
            if (c.equals(component)) {
                return i / container.getComponentCount(); // Assuming equal number of columns
            }
        }
        return -1;
    }
}

