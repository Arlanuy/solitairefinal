package uy;
import java.awt.*;

public class LayeredPaneLayout implements LayoutManager {
    private final static int CARDHEIGHT = 120;
    private final static int CARDWIDTH = 100;
    private final static int CARDDIST = 30;
    private static final Dimension SIZE = new Dimension(CARDWIDTH, CARDHEIGHT);
    private final Container target;
    private Component[] new_components;
    private int target_num = 0;

    public LayeredPaneLayout(final Container target, int target_num) {
        this.target = target;
        this.target_num = target_num;
    }
    //if it is 1 then this is created for suit_pile_panel, sub_draw_pile_panel (1 card) and main_draw_pile_panel
    //but if it is 2 then this is instead created  for pile_panel
    //but if it is 3 then this is created for sub_draw_pile_panel (3 cards)

    @Override
    public void addLayoutComponent(final String name, final Component comp) {
    }

    public Component[] getComponents() {
        Component[] components = target.getComponents();
        return components;
    }

    @Override
    public void layoutContainer(final Container container) {
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (target_num == 1) {
                components[i].setBounds(new Rectangle(0, 0, CARDWIDTH, CARDHEIGHT));
            }

            else if (target_num == 2) {
                if ((i == 0) && (components.length != 1)) {
                    components[i].setBounds(new Rectangle(0, 0 + ((components.length - 2 - i) * CARDDIST), CARDWIDTH, CARDHEIGHT));
                }

                else if (i != components.length - 1) {
                    components[i].setBounds(new Rectangle(0, 0 + ((components.length - 2 - i) * CARDDIST), CARDWIDTH, CARDDIST));
                }

                else {
                    //the last card in the pile as well as the background picture will render this setBounds setting
                    components[i].setBounds(new Rectangle(0, 0, CARDWIDTH, 120));
                }
            }

            else if (target_num == 3) {
                //only the top 3 cards will be displayed in the sub_draw_pile always
                //System.out.println("Layered pane Cards are: ");
                for (i = 0; i < components.length; i++) {
                    components[i].setBounds(new Rectangle(0, 0, 160, CARDHEIGHT));
                }
            }

            else {
                System.out.println("Target num is zero");
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(final Container parent) {
            return preferredLayoutSize(parent);
    }

    @Override
    public Dimension preferredLayoutSize(final Container parent) {
            return SIZE;
    }

    @Override
    public void removeLayoutComponent(final Component comp) {
    }
}
