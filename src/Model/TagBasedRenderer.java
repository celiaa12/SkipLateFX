package Model;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.util.Callback;

public class TagBasedRenderer<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    @Override
    public TableCell<S, T> call(TableColumn<S, T> column) {
        return new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setBackground(null);
                    return;
                }

                // Ambil data baris
                S rowData = getTableView().getItems().get(getIndex());

                int idTag = 0;
                try {
                    // Gunakan refleksi atau casting ke model tertentu
                    idTag = (int) rowData.getClass().getMethod("getIdTag").invoke(rowData);
                } catch (Exception e) {
                    System.err.println("Gagal membaca idTag: " + e.getMessage());
                }

                setText(item.toString());

                // Tentukan warna berdasarkan idTag
                Color bgColor = switch (idTag) {
                    case 1 -> Color.rgb(255, 204, 204); // Merah
                    case 2 -> Color.rgb(204, 255, 153); // Hijau
                    case 3 -> Color.rgb(255, 255, 102); // Kuning
                    case 4 -> Color.rgb(102, 204, 255); // Biru
                    default -> Color.rgb(102, 204, 255); // Default
                };

                setBackground(new Background(
                        new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
    }
}

