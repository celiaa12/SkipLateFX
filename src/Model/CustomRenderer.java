package Model;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.util.Callback;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;

public class CustomRenderer<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    private final Color color;

    public CustomRenderer(Color color) {
        this.color = color;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setBackground(null);
                } else {
                    setText(item.toString());
                    setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        };
    }
}

