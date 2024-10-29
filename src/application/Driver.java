package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {
	private LinkedLists<District> districtList = new LinkedLists<>();
	private static MartyrHash<MartyrDate> hashMartyr = new MartyrHash<>(11);

	private FileChooser fileChooser = new FileChooser();

	@Override
	public void start(Stage stage) {
		Button loadButton = createButton("Load Data");

		Label label = createLabel("");

		loadButton.setOnAction(event -> {
			File file = fileChooser.showOpenDialog(stage);
			if (file != null) {
				processFile(file, label);
				label.setText("" + hashMartyr.getSize());
				districtList.traverse();
				districtList.getHead().getData().getLocations().traverse();
				MainScreen(stage);

			} else {

			}
		});

		VBox vBox = new VBox(20, loadButton, label);
		vBox.setAlignment(Pos.CENTER);
		vBox.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");

		Scene scene = new Scene(vBox, 500, 500);
		stage.setTitle("Data Processing");
		stage.setScene(scene);
		stage.show();
	}

	public void MainScreen(Stage primaryStage) {
		Button btnToDistrictScreen = createButton("GO TO THE Date Screen");
		Button btnToLocationScreen = createButton("GO TO THE Martyrs Screen");
		Button writeToFile = createButton("Write To File");

		btnToDistrictScreen.setOnAction(e -> {
			DateScreen dateScreen = new DateScreen(hashMartyr, districtList);
			dateScreen.start(primaryStage);
		});

		btnToLocationScreen.setOnAction(e -> {
			HNode<MartyrDate>[] martyrNodes = hashMartyr.getHashTable();
			if (martyrNodes[0] != null) {
				MartyrDate martyrDate = martyrNodes[0].getData();
				Martyrs_Screen martyrsScreen = new Martyrs_Screen(hashMartyr, districtList, martyrDate.getMartyrsAVL());

				martyrsScreen.start(primaryStage);

			}
		});

		writeToFile.setOnAction(e -> {
			writeToFile();
		});

		VBox vbox = new VBox(10, btnToDistrictScreen, btnToLocationScreen, writeToFile);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");

		Scene scene = new Scene(vbox, 500, 500);
		primaryStage.setTitle("Main Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void processFile(File file, Label statusLabel) {
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				try {
					processLine(line);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}
	}

	private boolean processLine(String line) {
		try {
			String[] info = line.split(",");
			if (info.length < 6) {
				return false;
			}

			String name = info[0].trim();
			String date = info[1].trim();
			String age = info[2].trim();
			Integer numAge = Integer.parseInt(age);
			String locationName = info[3].trim();
			String districtName = info[4].trim();
			String gender = info[5].trim();

			Martyr martyrInsert = new Martyr(name, numAge, gender, districtName, locationName, calendarTimes(date));
			addMartyr(martyrInsert);

			District district = new District(districtName);
			Node<District> foundDistrict = districtList.find(district);
			if (foundDistrict == null) {
				districtList.insert(district);

				foundDistrict.setData(district);
			}
			foundDistrict.getData().addLocation(locationName);

			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public static Calendar calendarTimes(String timeString) {
		try {
			String[] time = timeString.split("[,:/ |#]");

			if (time.length < 3) {
				throw new IllegalArgumentException(
						"Input string does not contain enough data. Expected format: 'month, day, year'");
			}

			int year = Integer.parseInt(time[2].trim());
			int num1 = Integer.parseInt(time[0].trim());
			int month = num1 - 1;
			int day = Integer.parseInt(time[1].trim());
			return new GregorianCalendar(year, month, day);
		} catch (NumberFormatException e) {

		} catch (IllegalArgumentException e) {

		} catch (Exception e) {

		}
		return null;
	}

	public static void writeToFile() {
		String filePath = "Data.csv";

		try (FileWriter writer = new FileWriter(filePath)) {
			writer.append("Name,Date,Age,Location,District,Gender\n");

			for (HNode<MartyrDate> node : hashMartyr.getHashTable()) {
				if (node != null && node.getFlag() == 'F') {
					Stack<TNode<Martyr>> stack = new Stack<>();
					TNode<Martyr> currentMartyr = node.getData().getMartyrsAVL().getRoot();

					if (currentMartyr != null) {
						stack.push(currentMartyr);

						while (!stack.isEmpty()) {
							TNode<Martyr> curr = stack.pop();
							Martyr martyr = curr.getData();
							writer.append(martyr.getName()).append(",").append(node.getData().toString()).append(",")
									.append(String.valueOf(martyr.getAge())).append(",").append(martyr.getLocation())
									.append(",").append(martyr.getDistrict()).append(",").append(martyr.getGender())
									.append("\n");

							if (curr.getRight() != null) {
								stack.push(curr.getRight());
							}
							if (curr.getLeft() != null) {
								stack.push(curr.getLeft());
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addMartyrDate(MartyrDate data) {
		hashMartyr.add(data);
	}

	public static boolean deleteMartyrDate(MartyrDate data) {

		return hashMartyr.delete(data);
	}

	public static void addMartyr(Martyr data) {
		MartyrDate MartyrDate = new MartyrDate(data.getDate());
		MartyrDate MartyrDatefound = hashMartyr.find(MartyrDate);
		if (MartyrDatefound != null) {
			MartyrDatefound.getMartyrsAVL().insert(data);
		}
		hashMartyr.add(MartyrDate);
		MartyrDate.getMartyrsAVL().insert(data);
	}

	public static boolean deleteMartyr(Martyr data) {

		MartyrDate MartyrDate = new MartyrDate(data.getDate());
		MartyrDate MartyrDatefound = hashMartyr.find(MartyrDate);
		if (MartyrDatefound != null) {
			MartyrDatefound.getMartyrsAVL().delete(data);
			return true;
		}
		return false;
	}

	static TNode<Martyr> findMartyr(Martyr data) {
		MartyrDate MartyrDate = new MartyrDate(data.getDate());
		MartyrDate MartyrDatefound = hashMartyr.find(MartyrDate);
		if (MartyrDatefound != null) {

			return MartyrDatefound.getMartyrsAVL().find(data);
		}
		return null;

	}

	static MartyrDate findMartyrDate(MartyrDate data) {
		return hashMartyr.find(data);

	}

	public Label createLabel(String text) {
		Label label = new Label(text);
		label.setFont(new Font("Arial", 16));
		label.setTextFill(Color.BLACK);
		label.setPadding(new Insets(12, 24, 12, 24));
		return label;
	}

	public Button createButton(String text) {
		Button button = new Button(text);
		button.setFont(new Font("Arial", 14));
		button.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
		button.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		button.setPrefWidth(250);
		button.setPrefHeight(30);
		return button;
	}

	public void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setPrefSize(400, 200);
		dialogPane.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-padding: 10; "
				+ "-fx-font-size: 14px; -fx-font-family: 'Arial';");

		alert.showAndWait();
	}

	public TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setMaxHeight(25);
		textField.setMaxWidth(350);
		textField.setPromptText(promptText);
		textField.setFont(new Font("Arial", 14));
		textField.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; "
				+ "-fx-border-color: #B0C4DE; -fx-padding: 5;");
		return textField;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
