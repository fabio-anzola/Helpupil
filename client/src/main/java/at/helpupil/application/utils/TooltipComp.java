package at.helpupil.application.utils;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import dev.mett.vaadin.tooltip.Tooltips;

/**
 * Tooltip-Class for the simplification of creating supporting Tooltips
 */
public class TooltipComp extends Composite<Div> {
    /**
     * Create Tooltip Icon with Hover-Text
     *
     * @param tooltip tooltip-text
     */
    public TooltipComp(String tooltip) {
        Icon infoIcon = new Icon(VaadinIcon.INFO_CIRCLE_O);
        infoIcon.addClassName("info-icon");
        Tooltips.getCurrent().setTooltip(infoIcon, tooltip);

        getContent().add(infoIcon);
    }
}
