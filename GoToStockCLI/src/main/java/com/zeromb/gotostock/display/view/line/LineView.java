package com.zeromb.gotostock.display.view.line;

import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.Widget;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mr_Told
 */
public class LineView extends Widget {

    Style style;
    List<TextView> views = new LinkedList<>();
    String separator = " ";

    String finalString;
    Style[] finalStyle;
    boolean dirty = true;

    public LineView(Style style) {
        this.style = style;
    }

    public LineView addView(TextView view) {
        views.add(view);
        dirty = true;
        return this;
    }

    public LineView removeView(TextView view) {
        views.remove(view);
        dirty = true;
        return this;
    }

    public LineView clearViews() {
        views.clear();
        dirty = true;
        return this;
    }

    public void markDirty() {
        dirty = true;
    }

    private void calculate() {
        if (!dirty) return;

        StringBuilder sb = new StringBuilder();

        for (TextView view : views)
            sb.append(view.getText()).append(separator);

        sb.setLength(sb.length() - 1);
        finalString = sb.toString();

        finalStyle = new Style[finalString.length()];

        int i = 0;
        for (TextView view : views) {
            String s = view.getText();
            Style style = view.getStyle();

            for (int j = 0; j < s.length(); j++) {
                finalStyle[i] = style;
                i++;
            }
            i += separator.length();
        }

        dirty = false;
    }

    @Override
    public void render(Canvas canvas) {
        calculate();
        int len = finalString.length();

        int startX = pos().getXCoordinate(len, canvas.getWidth());
        int contentX = pos().getContentX(len, canvas.getWidth());
        int contentWidth = pos().getContentWidth(len, canvas.getWidth() - pos().getOffsetX());
        int y = pos().getYCoordinate(1, canvas.getHeight());
        int endX = pos().getEndXCoordinate(startX, len, canvas.getWidth());

        int i;
        for (i = startX; i < endX; ++i) {
            canvas.getCell(y, i).setStyle(this.getBackground());
        }

        for (i = contentX; i < contentWidth + contentX; ++i) {
            int p = i - contentX;
            canvas.getCell(y, i).setChar(finalString.charAt(p)).setStyle(finalStyle[p]);
        }
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

}
