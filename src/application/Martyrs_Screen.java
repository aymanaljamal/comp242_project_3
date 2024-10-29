package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class Martyrs_Screen extends Application {

	private AVLTree<Martyr> avlTree = new AVLTree<>();
	private TableView<Martyr> tableView = new TableView<>();
	private TextField treeSizeField = new TextField();
	private TextField treeHeightField = new TextField();
	private MartyrHash<MartyrDate> hashTable;
	private LinkedLists<District> districtList = new LinkedLists<>();
	private Martyr Martyr = null;
	private Label wr = createLabel(" ");

	public Martyrs_Screen(MartyrHash<MartyrDate> hashTable2, LinkedLists<District> districtList,
			AVLTree<Martyr> avlTree) {
		super();
		this.hashTable = hashTable2;
		this.avlTree = avlTree;
		this.districtList = districtList;
		avlTree.StackFillingLevelByLevel();

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		tableView = new TableView<>();
		setupTableView();

		Button writeToFile = createButton("Write To File");
		writeToFile.setOnAction(e -> Driver.writeToFile());

		Button levelByLevelButton = createButton("LEVEL BY LEVEL");
		levelByLevelButton.setOnAction(e -> TableView());

		Button sortedByAgeMinButton = createButton("sorted by age MIN");
		sortedByAgeMinButton.setOnAction(e -> {
			ObservableList<Martyr> sortedList = sortMartyrsByAge(avlTree, false);
			tableView.setItems(sortedList);
		});

		Button sortedByAgeMaxButton = createButton("sorted by age MAX");
		sortedByAgeMaxButton.setOnAction(e -> {
			ObservableList<Martyr> sortedList = sortMartyrsByAge(avlTree, true);
			tableView.setItems(sortedList);
		});

		HBox radioBox = new HBox(10, levelByLevelButton, sortedByAgeMinButton, sortedByAgeMaxButton);
		radioBox.setAlignment(Pos.CENTER);

		Button printButton = new Button("Print Tree Level-by-Level (Right to Left)");
		printButton.setOnAction(e -> TableView());

		Button insertButton = createButton("Insert Martyr");
		insertButton.setOnAction(e -> insertMartyr(primaryStage));

		Button updateButton = createButton("Update Martyr Info");
		updateButton.setOnAction(e -> updateMartyr(primaryStage));

		Button deleteButton = createButton("Delete Martyr");
		deleteButton.setOnAction(e -> deleteMartyr(primaryStage));

		HBox hbox = new HBox(10, insertButton, updateButton, deleteButton, writeToFile);
		hbox.setAlignment(Pos.CENTER);

		Label treeSizeLabel = createLabel("Show Tree Size");

		treeSizeField.setText(String.valueOf(avlTree.size()));

		Label treeHeightLabel = createLabel("Show Tree Height");

		treeHeightField.setText(String.valueOf(avlTree.height()));
		Label averageLabel = createLabel("Average Martyrs Ages:");
		Label youngestLabel = createLabel("The Youngest Martyrs:");
		Label oldestLabel = createLabel("The Oldest Martyrs:");

		TextField averageTextField = createTextField("=================");
		TextField youngestTextField = createTextField("=================");
		TextField oldestTextField = createTextField("=================");
		averageTextField.setText(String.valueOf(getAverageMartyrs(avlTree)));
		youngestTextField.setText(String.valueOf(youngestMartyr(avlTree)));
		oldestTextField.setText(String.valueOf(oldestMartyr(avlTree)));
		HBox hbox2 = new HBox(10, averageLabel, averageTextField, youngestLabel, youngestTextField, oldestLabel,
				oldestTextField);
		hbox2.setAlignment(Pos.CENTER);

		treeSizeField.setEditable(false);

		treeHeightField.setEditable(false);

		HBox treeInfoBox = new HBox(10, treeSizeLabel, treeSizeField, treeHeightLabel, treeHeightField);
		treeInfoBox.setAlignment(Pos.CENTER);

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");

		Label titleLabel = createLabel("Martyrs Management Screen");
		layout.getChildren().add(titleLabel);
		layout.getChildren().add(hbox);
		layout.getChildren().add(treeInfoBox);
		layout.getChildren().add(hbox2);

		layout.getChildren().addAll(radioBox, tableView);

		avlTree.StackFillingLevelByLevel();

		Scene scene = new Scene(layout, 1000, 700);
		primaryStage.setTitle("Martyrs Management");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void updateMartyr(Stage primaryStage) {
		primaryStage.setTitle("Update Martyr");
		Button goBaekButton = createButton("GO BACK");

		goBaekButton.setOnAction(e -> start(primaryStage));
		ComboBox<String> dateComboBox = new ComboBox<>();
		ComboBox<String> nameComboBox = new ComboBox<>();
		dateComboBox.setStyle(
				"-fx-border-color: red; -fx-border-width: 2; -fx-padding: 10; -fx-font-size: 14px; -fx-font-family: 'Arial';");
		nameComboBox.setStyle(
				"-fx-border-color: red; -fx-border-width: 2; -fx-padding: 10; -fx-font-size: 14px; -fx-font-family: 'Arial';");

		Button findButton = createButton("Find Martyr");

		Button nameButton = createButton("Update Name");
		TextField nameField = createTextField("Enter name");

		Button ageButton = createButton("Update Age");
		Spinner<Integer> ageSpinner = new Spinner<>(0, 120, 30);

		Button genderButton = createButton("Update Gender");
		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton maleRadio = new RadioButton("M");
		maleRadio.setToggleGroup(genderGroup);
		RadioButton femaleRadio = new RadioButton("F");
		femaleRadio.setToggleGroup(genderGroup);

		Button districtButton = createButton("Update District");
		ComboBox<String> districtComboBox = new ComboBox<>();

		Button locationButton = createButton("Update Location");
		ComboBox<String> locationComboBox = new ComboBox<>();

		Button dateButton = createButton("Update Date");
		DatePicker datePicker = new DatePicker();

		districtComboBox.setOnAction(event -> {
			locationComboBox.getItems().clear();
			updateLocationComboBox(districtList, districtComboBox.getValue(), locationComboBox);
		});

		updateDistrictComboBox(districtList, districtComboBox);

		dateComboBox.setOnAction(event -> {
			nameComboBox.getItems().clear();
			String date2 = dateComboBox.getValue();
			MartyrDate martyrDate = new MartyrDate(Driver.calendarTimes(date2));

			MartyrDate martyrDateFound = Driver.findMartyrDate(martyrDate);
			if (martyrDateFound != null) {
				updateNameComboBox1(martyrDateFound.getMartyrsAVL(), nameComboBox);
			}
		});

		findButton.setOnAction(event -> {
			Calendar date3 = Driver.calendarTimes(dateComboBox.getValue());
			String name3 = nameComboBox.getValue();
			Martyr martyr = new Martyr(date3, name3);
			TNode<Martyr> martyrFound = Driver.findMartyr(martyr);
			if (martyrFound != null) {
				Martyr = martyrFound.getData();
			} else {
				showAlert("Not Found", "Martyr not found.");
			}
		});

		nameButton.setOnAction(event -> {
			showAlertConfirmation("Update Name", "Are you sure you want to update the name?", () -> {
				String name = nameField.getText();
				Martyr.setName(name);
				showAlert("Success", "Name updated successfully!");
			});
		});

		ageButton.setOnAction(event -> {
			showAlertConfirmation("Update Age", "Are you sure you want to update the age?", () -> {
				Integer age = ageSpinner.getValue();
				Martyr.setAge(age);
				showAlert("Success", "Age updated successfully!");
			});
		});

		genderButton.setOnAction(event -> {
			showAlertConfirmation("Update Gender", "Are you sure you want to update the gender?", () -> {
				String gender = "";
				if (maleRadio.isSelected()) {
					gender = "M";

				} else {
					gender = "F";
				}
				Martyr.setGender(gender);
				showAlert("Success", "Gender updated successfully!");
			});
		});

		districtButton.setOnAction(event -> {
			showAlertConfirmation("Update District", "Are you sure you want to update the district?", () -> {
				String district = districtComboBox.getValue();
				Martyr.setDistrict(district);
				showAlert("Success", "District updated successfully!");
			});
		});

		locationButton.setOnAction(event -> {
			showAlertConfirmation("Update Location", "Are you sure you want to update the location?", () -> {
				String location = locationComboBox.getValue();
				Martyr.setLocation(location);
				showAlert("Success", "Location updated successfully!");
			});
		});

		dateButton.setOnAction(event -> {
			showAlertConfirmation("Update Date", "Are you sure you want to update the date?", () -> {
				LocalDate date = datePicker.getValue();
				Calendar calendarDate = new GregorianCalendar(date.getYear(), date.getMonthValue() - 1,
						date.getDayOfMonth());

				Driver.deleteMartyr(Martyr);
				Martyr.setDate(calendarDate);
				Driver.addMartyr(Martyr);
				showAlert("Success", "Date updated successfully!");
			});
		});
		updateDateComboBox(hashTable, dateComboBox);
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(8);
		gridPane.setHgap(10);

		gridPane.add(dateComboBox, 0, 0);
		gridPane.add(nameComboBox, 1, 0);
		gridPane.add(findButton, 2, 0);

		gridPane.add(nameButton, 0, 1);
		gridPane.add(nameField, 1, 1);

		gridPane.add(ageButton, 0, 2);
		gridPane.add(ageSpinner, 1, 2);

		gridPane.add(genderButton, 0, 3);
		gridPane.add(maleRadio, 1, 3);
		gridPane.add(femaleRadio, 2, 3);

		gridPane.add(districtButton, 0, 4);
		gridPane.add(districtComboBox, 1, 4);

		gridPane.add(locationButton, 0, 5);
		gridPane.add(locationComboBox, 1, 5);

		gridPane.add(dateButton, 0, 6);
		gridPane.add(datePicker, 1, 6);
		gridPane.add(goBaekButton, 1, 9);
		Scene scene = new Scene(gridPane, 500, 500);
		gridPane.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void insertMartyr(Stage primaryStage) {
		Button goBaekButton = createButton("GO BACK");

		goBaekButton.setOnAction(e -> start(primaryStage));
		Label nameLabel = createLabel("Name:");
		TextField nameField = createTextField("Enter name");

		Label ageLabel = createLabel("Age:");
		Spinner<Integer> ageSpinner = new Spinner<>(0, 120, 30);

		Label genderLabel = createLabel("Gender:");
		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton maleRadio = new RadioButton("M");
		maleRadio.setToggleGroup(genderGroup);
		RadioButton femaleRadio = new RadioButton("F");
		femaleRadio.setToggleGroup(genderGroup);

		Label districtLabel = createLabel("District:");
		ComboBox<String> districtComboBox = new ComboBox<>();

		Label locationLabel = createLabel("Location:");
		ComboBox<String> locationComboBox = new ComboBox<>();

		districtComboBox.setOnAction(event -> {
			locationComboBox.getItems().clear();
			updateLocationComboBox(districtList, districtComboBox.getValue(), locationComboBox);

		});
		updateDistrictComboBox(districtList, districtComboBox);

		Label dateLabel = createLabel("Date:");
		DatePicker datePicker = new DatePicker();

		Button insertButton = createButton("Insert Martyr");

		insertButton.setOnAction(e -> {
			String name = nameField.getText();
			Integer age = ageSpinner.getValue();
			String gender = "";
			if (maleRadio.isSelected()) {
				gender = "M";

			} else {
				gender = "F";
			}
			String district = districtComboBox.getValue();
			String location = locationComboBox.getValue();
			LocalDate date = datePicker.getValue();

			if (name.isEmpty() || gender == null || district == null || location == null || date == null) {
				showAlert("Input Error", "Please fill all fields.");
				return;
			}

			Calendar calendarDate = new GregorianCalendar(date.getYear(), date.getMonthValue() - 1,
					date.getDayOfMonth());
			Martyr newMartyr = new Martyr(name, age, gender, district, location, calendarDate);
			Driver.addMartyr(newMartyr);
			showAlert("Success", "Martyr inserted successfully!");
		});

		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setVgap(8);
		gridPane.setHgap(10);

		gridPane.add(nameLabel, 0, 0);
		gridPane.add(nameField, 1, 0);

		gridPane.add(ageLabel, 0, 1);
		gridPane.add(ageSpinner, 1, 1);

		gridPane.add(genderLabel, 0, 2);
		gridPane.add(maleRadio, 1, 2);
		gridPane.add(femaleRadio, 2, 2);

		gridPane.add(districtLabel, 0, 3);
		gridPane.add(districtComboBox, 1, 3);

		gridPane.add(locationLabel, 0, 4);
		gridPane.add(locationComboBox, 1, 4);

		gridPane.add(dateLabel, 0, 5);
		gridPane.add(datePicker, 1, 5);

		gridPane.add(insertButton, 1, 6);
		gridPane.add(goBaekButton, 1, 9);

		Scene scene = new Scene(gridPane, 500, 500);
		gridPane.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");
		primaryStage.setTitle("Insert New Martyr");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void deleteMartyr(Stage primaryStage) {
		Button goBaekButton = createButton("GO BACK");

		goBaekButton.setOnAction(e -> start(primaryStage));
		primaryStage.setTitle("Delete Martyr");

		ComboBox<String> dateComboBox = new ComboBox<>();
		ComboBox<String> nameComboBox = new ComboBox<>();
		dateComboBox.setStyle(
				"-fx-border-color: red; -fx-border-width: 2; -fx-padding: 10; -fx-font-size: 14px; -fx-font-family: 'Arial';");
		nameComboBox.setStyle(
				"-fx-border-color: red; -fx-border-width: 2; -fx-padding: 10; -fx-font-size: 14px; -fx-font-family: 'Arial';");

		Button deleteButton = createButton("Delete Martyr");
		deleteButton.setOnAction(e -> {
			Calendar date = Driver.calendarTimes(dateComboBox.getValue());
			String name = nameComboBox.getValue();
			Martyr martyr = new Martyr(date, name);
			TNode<Martyr> martyrFound = Driver.findMartyr(martyr);
			if (martyrFound != null) {
				Driver.deleteMartyr(martyrFound.getData());
				nameComboBox.getItems().clear();
				String date2 = dateComboBox.getValue();
				MartyrDate martyrDate = new MartyrDate(Driver.calendarTimes(date2));
				MartyrDate martyrDateFound = Driver.findMartyrDate(martyrDate);
				if (martyrDateFound != null) {
					updateNameComboBox1(martyrDateFound.getMartyrsAVL(), nameComboBox);
				}
				showAlert("Martyr Deleted", "The martyr has been successfully deleted.");
			} else {
				showAlert("Martyr Not Found", "The martyr could not be found.");
			}
		});

		dateComboBox.setOnAction(event -> {
			nameComboBox.getItems().clear();
			String date = dateComboBox.getValue();
			MartyrDate martyrDate = new MartyrDate(Driver.calendarTimes(date));
			MartyrDate martyrDateFound = Driver.findMartyrDate(martyrDate);
			if (martyrDateFound != null) {
				updateNameComboBox1(martyrDateFound.getMartyrsAVL(), nameComboBox);
			}
		});

		updateDateComboBox(hashTable, dateComboBox);

		VBox vbox = new VBox(20, dateComboBox, nameComboBox, deleteButton, goBaekButton);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void setupTableView() {
		TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

		TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

		TableColumn<Martyr, String> districtColumn = new TableColumn<>("District");
		districtColumn.setCellValueFactory(new PropertyValueFactory<>("district"));

		TableColumn<Martyr, String> locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

		tableView.getColumns().addAll(nameColumn, ageColumn, genderColumn, districtColumn, locationColumn);
	}

	private void TableView() {
		ObservableList<Martyr> data = FXCollections.observableArrayList();
		avlTree.populateLevelOrderRightToLeft(data);
		tableView.setItems(data);
	}

	public void updateDateComboBox(MartyrHash<MartyrDate> hashTable2, ComboBox<String> dateComboBox) {
		dateComboBox.getItems().clear();
		HNode<MartyrDate>[] hashTableArray = hashTable2.getHashTable();
		List<String> dates = new ArrayList<>();

		for (HNode<MartyrDate> node : hashTableArray) {
			if (node != null && node.getFlag() == 'F') {
				dates.add(node.getData().toString());
			}
		}

		Collections.sort(dates);
		ObservableList<String> sortedDates = FXCollections.observableArrayList(dates);
		dateComboBox.setItems(sortedDates);
	}

	private void updateNameComboBox1(AVLTree<Martyr> martyrAVL, ComboBox<String> nameComboBox) {
		nameComboBox.getItems().clear();

		ObservableList<String> names = FXCollections.observableArrayList();
		Stack<TNode<Martyr>> stack = new Stack<>();
		stack.push(martyrAVL.getRoot());

		while (!stack.isEmpty()) {
			TNode<Martyr> currentMartyr = stack.pop();
			String name = currentMartyr.getData().getName();

			names.add(name);

			if (currentMartyr.getLeft() != null) {
				stack.push(currentMartyr.getLeft());
			}
			if (currentMartyr.getRight() != null) {
				stack.push(currentMartyr.getRight());
			}
		}

		nameComboBox.setItems(names);
	}

	private static void updateDistrictComboBox(LinkedLists<District> districtList, ComboBox<String> districtComboBox) {
		ObservableList<String> districtNames = FXCollections.observableArrayList();
		Node<District> curr = districtList.getHead();
		while (curr != null) {
			districtNames.add(curr.getData().getName());
			curr = curr.getNext();
		}
		districtComboBox.setItems(districtNames);
	}

	private static void updateLocationComboBox(LinkedLists<District> districtList, String selectedDistrict,
			ComboBox<String> locationComboBox) {
		ObservableList<String> locationNames = FXCollections.observableArrayList();
		District district = new District(selectedDistrict);
		Node<District> currDistrict = districtList.find(district);

		if (currDistrict != null) {
			Node<String> currLocation = currDistrict.getData().getLocations().getHead();
			while (currLocation != null) {
				locationNames.add(currLocation.getData());
				currLocation = currLocation.getNext();
			}
		}

		locationComboBox.setItems(locationNames);
	}

	public double getAverageMartyrs(AVLTree<Martyr> martyrAVLL) {
		AVLTree<Martyr> martyrAVL = martyrAVLL;
		if (martyrAVL == null || martyrAVL.isEmpty()) {
			return 0.0;
		}
		int sum = sumAges(martyrAVL.getRoot());
		int count = martyrAVL.size();
		return (double) sum / count;
	}

	private int sumAges(TNode<Martyr> node) {
		if (node == null) {
			return 0;
		}
		return node.getData().getAge() + sumAges(node.getLeft()) + sumAges(node.getRight());
	}

	public Martyr youngestMartyr(AVLTree<Martyr> martyrAVL) {
		if (martyrAVL == null || martyrAVL.isEmpty()) {
			return null;
		}
		return findYoungestMartyr(martyrAVL.getRoot());
	}

	public Martyr oldestMartyr(AVLTree<Martyr> martyrAVL) {
		if (martyrAVL == null || martyrAVL.isEmpty()) {
			return null;
		}
		return findOldestMartyr(martyrAVL.getRoot());
	}

	private Martyr findYoungestMartyr(TNode<Martyr> node) {
		if (node == null) {
			return null;
		}
		Martyr leftYoungest = findYoungestMartyr(node.getLeft());
		Martyr rightYoungest = findYoungestMartyr(node.getRight());

		Martyr youngest = node.getData();
		if (leftYoungest != null && leftYoungest.getAge() < youngest.getAge()) {
			youngest = leftYoungest;
		}
		if (rightYoungest != null && rightYoungest.getAge() < youngest.getAge()) {
			youngest = rightYoungest;
		}
		return youngest;
	}

	private Martyr findOldestMartyr(TNode<Martyr> node) {
		if (node == null) {
			return null;
		}
		Martyr leftOldest = findOldestMartyr(node.getLeft());
		Martyr rightOldest = findOldestMartyr(node.getRight());

		Martyr oldest = node.getData();
		if (leftOldest != null && leftOldest.getAge() > oldest.getAge()) {
			oldest = leftOldest;
		}
		if (rightOldest != null && rightOldest.getAge() > oldest.getAge()) {
			oldest = rightOldest;
		}
		return oldest;
	};

	private ObservableList<Martyr> sortMartyrsByAge(AVLTree<Martyr> martyrList, boolean ascending) {
		NodeHeap<Martyr>[] martyrsByAge = new NodeHeap[martyrList.size()];
		Stack<TNode<Martyr>> stack = new Stack<>();
		stack.push(martyrList.getRoot());

		int i = 0;
		while (!stack.isEmpty()) {
			TNode<Martyr> currentMartyr = stack.pop();
			martyrsByAge[i] = new NodeHeap<>(currentMartyr.getData());
			i++;
			if (currentMartyr.getLeft() != null) {
				stack.push(currentMartyr.getLeft());
			}
			if (currentMartyr.getRight() != null) {
				stack.push(currentMartyr.getRight());
			}
		}

		if (!ascending) {
			MaxHeap.heapSortDsc(martyrsByAge);
		} else {
			MinHeap.heapSortAesc(martyrsByAge);
		}

		ObservableList<Martyr> sortedList = FXCollections.observableArrayList();
		for (NodeHeap<Martyr> node : martyrsByAge) {
			sortedList.add(node.getDate());
		}
		return sortedList;
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
		button.setPrefWidth(200);
		button.setPrefHeight(30);
		return button;
	}

	private void showAlertConfirmation(String title, String message, Runnable onConfirm) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		ButtonType confirmButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		alert.showAndWait().ifPresent(type -> {
			if (type == confirmButton) {
				onConfirm.run();
			}
		});
	}

	public void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setPrefSize(400, 200);
		dialogPane.setStyle(
				"-fx-border-color: red; -fx-border-width: 2; -fx-padding: 10; -fx-font-size: 14px; -fx-font-family: 'Arial';");

		alert.showAndWait();
	}

	public TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setMaxHeight(25);
		textField.setMaxWidth(150);
		textField.setPromptText(promptText);
		textField.setFont(new Font("Arial", 14));
		textField.setStyle(
				"-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #B0C4DE; -fx-padding: 5;");
		return textField;
	}
}
