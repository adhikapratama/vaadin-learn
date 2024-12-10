package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final List<String> kalkulatorHistory = new ArrayList<>();
    private final List<String> bmiHistory = new ArrayList<>();

    public MainView() {
        // Label untuk menu utama
        Label menuLabel = new Label("Pilih Menu Utama:");
        menuLabel.getStyle().set("font-size", "24px").set("font-weight", "bold");

        // Membuat tombol menu utama
        Button kalkulatorButton = new Button("Kalkulator Sederhana", event -> showKalkulator());
        Button bmiButton = new Button("Menghitung BMI", event -> showBMI());

        // Menempatkan tombol dalam layout dengan gaya card
        HorizontalLayout menuButtons = new HorizontalLayout(kalkulatorButton, bmiButton);
        menuButtons.setSpacing(true);
        menuButtons.setWidthFull();

        Div menuCard = new Div(menuLabel, menuButtons);
        menuCard.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "10px")
                .set("padding", "20px")
                .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                .set("max-width", "400px")
                .set("margin", "auto");

        add(menuCard); // Menambahkan card ke tampilan utama
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setHeightFull(); // Memastikan konten berada di tengah vertikal
    }

    // Menampilkan sub-menu untuk Kalkulator Sederhana
    private void showKalkulator() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        Label title = new Label("Kalkulator Sederhana");
        title.getStyle().set("font-size", "18px").set("font-weight", "bold");

        ComboBox<String> operasiComboBox = new ComboBox<>("Pilih Operasi");
        operasiComboBox.setItems("+", "-", "*", "/");

        TextField angka1 = new TextField("Angka 1");
        TextField angka2 = new TextField("Angka 2");

        Label resultLabel = new Label();
        resultLabel.getStyle()
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("text-align", "center");

        Button hitungButton = new Button("Hitung");
        Button lihatRiwayatButton = new Button("Lihat Riwayat");
        Button hapusRiwayatButton = new Button("Hapus Semua Riwayat");

        // Tombol untuk menghitung
        hitungButton.addClickListener(e -> {
            try {
                double num1 = Double.parseDouble(angka1.getValue());
                double num2 = Double.parseDouble(angka2.getValue());
                String operasi = operasiComboBox.getValue();
                double hasil = 0;

                if (operasi == null) {
                    Notification.show("Pilih operasi terlebih dahulu");
                    return;
                }

                switch (operasi) {
                    case "+":
                        hasil = num1 + num2;
                        break;
                    case "-":
                        hasil = num1 - num2;
                        break;
                    case "*":
                        hasil = num1 * num2;
                        break;
                    case "/":
                        if (num2 == 0) {
                            Notification.show("Pembagian dengan 0 tidak dapat dilakukan");
                            return;
                        }
                        hasil = num1 / num2;
                        break;
                }

                String hasilText = "Hasil: " + num1 + " " + operasi + " " + num2 + " = " + hasil;
                kalkulatorHistory.add(hasilText);
                resultLabel.setText(hasilText);
            } catch (NumberFormatException ex) {
                Notification.show("Harap masukkan angka yang valid");
            }
        });

        // Tombol untuk melihat riwayat
        lihatRiwayatButton.addClickListener(e -> {
            Dialog riwayatDialog = new Dialog();
            riwayatDialog.setWidth("400px");

            Label riwayatTitle = new Label("Riwayat Perhitungan:");
            riwayatTitle.getStyle().set("font-weight", "bold");

            Grid<String> grid = new Grid<>();
            grid.setItems(kalkulatorHistory);
            grid.addColumn(String::toString).setHeader("Perhitungan");

            riwayatDialog.add(riwayatTitle, grid);
            riwayatDialog.open();
        });

        // Tombol untuk menghapus riwayat
        hapusRiwayatButton.addClickListener(e -> {
            kalkulatorHistory.clear();
            Notification.show("Semua riwayat telah dihapus");
        });

        VerticalLayout contentLayout = new VerticalLayout(
                title, operasiComboBox, angka1, angka2, hitungButton, resultLabel, lihatRiwayatButton, hapusRiwayatButton);
        contentLayout.setAlignItems(Alignment.CENTER);

        Div kalkulatorCard = new Div(contentLayout);
        kalkulatorCard.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "10px")
                .set("padding", "20px")
                .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)");

        dialog.add(kalkulatorCard);
        dialog.open();
    }

    // Menampilkan sub-menu untuk Menghitung BMI
    private void showBMI() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        Label title = new Label("Menghitung BMI");
        title.getStyle().set("font-size", "18px").set("font-weight", "bold");

        TextField beratBadanField = new TextField("Berat Badan (kg)");
        TextField tinggiBadanField = new TextField("Tinggi Badan (cm)");

        Label resultLabel = new Label();
        resultLabel.getStyle()
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("text-align", "center");

        Button hitungBMIButton = new Button("Hitung BMI");
        Button lihatRiwayatButton = new Button("Lihat Riwayat");

        // Tombol untuk menghitung BMI
        hitungBMIButton.addClickListener(e -> {
            try {
                double beratBadan = Double.parseDouble(beratBadanField.getValue());
                double tinggiBadan = Double.parseDouble(tinggiBadanField.getValue()) / 100; // Konversi cm ke meter
                double bmi = beratBadan / (tinggiBadan * tinggiBadan);

                String kategori = getBMICategory(bmi);
                String hasilText = "BMI: " + String.format("%.2f", bmi) + " - Kategori: " + kategori;
                bmiHistory.add(hasilText);
                resultLabel.setText(hasilText);
            } catch (NumberFormatException ex) {
                Notification.show("Harap masukkan angka yang valid");
            }
        });

        // Tombol untuk melihat riwayat BMI
        lihatRiwayatButton.addClickListener(e -> {
            Dialog riwayatDialog = new Dialog();
            riwayatDialog.setWidth("400px");

            Label riwayatTitle = new Label("Riwayat BMI:");
            riwayatTitle.getStyle().set("font-weight", "bold");

            Grid<String> grid = new Grid<>();
            grid.setItems(bmiHistory);
            grid.addColumn(String::toString).setHeader("BMI");

            riwayatDialog.add(riwayatTitle, grid);
            riwayatDialog.open();
        });

        VerticalLayout contentLayout = new VerticalLayout(
                title, beratBadanField, tinggiBadanField, hitungBMIButton, resultLabel, lihatRiwayatButton);
        contentLayout.setAlignItems(Alignment.CENTER);

        Div bmiCard = new Div(contentLayout);
        bmiCard.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "10px")
                .set("padding", "20px")
                .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)");

        dialog.add(bmiCard);
        dialog.open();
    }

    // Menentukan kategori BMI berdasarkan nilai BMI
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Kekurangan Berat Badan";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return "Berat Badan Normal";
        } else if (bmi >= 25 && bmi <= 29.9) {
            return "Berat Badan Berlebih (Overweight)";
        } else {
            return "Obesitas";
        }
    }
}
