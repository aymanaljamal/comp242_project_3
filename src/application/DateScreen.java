package application;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateScreen extends Application {

	private MartyrHash<MartyrDate> hashTable = new MartyrHash<>(10);
	private Label statusLabel = new Label();
	private int currentIndex = -1;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private TextField averageField = createTextField("");
	private TextField totalField = createTextField("");
	private TextField districtMaximumField = createTextField("");
	private TextField locationMaximumField = createTextField("");
	private TableView<HNode<MartyrDate>> tableView = new TableView<>();
	private Label averageLabel = createLabel("Average:");
	private Label totalLabel = createLabel("Total:");
	private Label districtMaximumLabel = createLabel("District Maximum:");
	private Label locationMaximumLabel = createLabel("Location Maximum:");
	private Button loadTheCurrent = createButton("load the current :");
	private LinkedLists<District> link = new LinkedLists<>();

	public DateScreen(MartyrHash<MartyrDate> hashTable, LinkedLists<District> link) {
		super();
		this.hashTable = hashTable;
		this.link = link;
	}

	@Override
	public void start(Stage primaryStage) {

		setupTableView(tableView);
		Button writeToFile = createButton("Write To File");
		writeToFile.setOnAction(e -> {
			Driver.writeToFile();

		});
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.setAlignment(Pos.CENTER);

		HBox radioBox = new HBox(10);
		radioBox.setAlignment(Pos.CENTER);
		Label choiceLabel = new Label("Choose Option: ");
		RadioButton includeEmptyRadioButton = new RadioButton("Include Empty Spots");
		RadioButton excludeEmptyRadioButton = new RadioButton("Exclude Empty Spots");
		excludeEmptyRadioButton.setSelected(true);

		ToggleGroup toggleGroup = new ToggleGroup();
		includeEmptyRadioButton.setToggleGroup(toggleGroup);
		excludeEmptyRadioButton.setToggleGroup(toggleGroup);

		toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		        RadioButton selectedRadioButton = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
		        String toggleGroupValue = selectedRadioButton.getText();

		        if (toggleGroupValue.equals("Include Empty Spots")) {
		            updateTableView(tableView, true);
		        } else if (toggleGroupValue.equals("Exclude Empty Spots")) {
		            updateTableView(tableView, false);
		        }
		    }
		});


		radioBox.getChildren().addAll(choiceLabel, includeEmptyRadioButton, excludeEmptyRadioButton);

		VBox tableVBox = new VBox(10);
		tableVBox.setPadding(new Insets(20));
		tableVBox.setAlignment(Pos.CENTER);
		tableVBox.getChildren().addAll(radioBox, tableView);

		HBox averageBox = new HBox(10);
		averageBox.setAlignment(Pos.CENTER);
		averageBox.getChildren().addAll(averageLabel, averageField);

		HBox totalBox = new HBox(10);
		totalBox.setAlignment(Pos.CENTER);
		totalBox.getChildren().addAll(totalLabel, totalField);

		HBox districtMaximumBox = new HBox(10);
		districtMaximumBox.setAlignment(Pos.CENTER);
		districtMaximumBox.getChildren().addAll(districtMaximumLabel, districtMaximumField);

		HBox locationMaximumBox = new HBox(10);
		locationMaximumBox.setAlignment(Pos.CENTER);
		locationMaximumBox.getChildren().addAll(locationMaximumLabel, locationMaximumField);

		vbox.getChildren().addAll(averageBox, totalBox, districtMaximumBox, locationMaximumBox, loadTheCurrent);

		ComboBox<String> dateComboBox = new ComboBox<>();
		dateComboBox.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
		dateComboBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		updateDateComboBox(hashTable, dateComboBox);

		VBox root = new VBox(10);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);

		tableView.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");
		HBox insertSection = new HBox(10);
		insertSection.setAlignment(Pos.CENTER);
		DatePicker insertDatePicker = new DatePicker();
		Button insertButton = createButton("Insert");
		insertSection.getChildren().addAll(insertButton, insertDatePicker);

		ComboBox<String> updateComboBox = new ComboBox<>();
		updateComboBox.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
		updateComboBox.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
		updateComboBox.setPromptText("Select Date");

		HBox updateSection = new HBox(10);
		updateSection.setAlignment(Pos.CENTER);
		DatePicker updateDatePicker = new DatePicker();
		Button updateButton = createButton("Update");
		updateSection.getChildren().addAll(updateButton, updateComboBox, updateDatePicker);
		updateDateComboBox(hashTable, updateComboBox);

		HBox deleteSection = new HBox(10);
		deleteSection.setAlignment(Pos.CENTER);
		Button deleteButton = createButton("Delete");
		deleteSection.getChildren().addAll(deleteButton, dateComboBox);

		Button printButton = createButton("Print Hash Table");

		HBox navigationSection = new HBox(10);
		navigationSection.setAlignment(Pos.CENTER);
		Button navigateUpButton = createButton("Navigate Up");
		Button navigateDownButton = createButton("Navigate Down");
		navigationSection.getChildren().addAll(navigateUpButton, navigateDownButton);

		root.getChildren().addAll(new Label("Insert New Date Record"), insertSection, new Label("Update Date Record"),
				updateSection, new Label("Delete Date Record"), deleteSection, navigationSection, vbox, writeToFile,
				statusLabel);

		HBox mainLayout = new HBox(20);
		mainLayout.setPadding(new Insets(20));
		mainLayout.setAlignment(Pos.CENTER);
		mainLayout.getChildren().addAll(root, tableVBox);
		tableVBox.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");

		Scene scene = new Scene(mainLayout, 800, 600);
		mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");
		primaryStage.setTitle("Martyrs Hash Table Management");
		primaryStage.setScene(scene);
		primaryStage.show();
		root.setStyle("-fx-background-color: linear-gradient(to bottom, #b3cde0, #6497b1);");

		navigateUpButton.setOnAction(event -> navigateUp());
		navigateDownButton.setOnAction(event -> navigateDown());

		updateButton.setOnAction(event -> {
			update(updateDatePicker, updateComboBox, statusLabel, hashTable);
			updateComboBox.getItems().clear();
			dateComboBox.getItems().clear();
			updateDateComboBox(hashTable, dateComboBox);
			updateDateComboBox(hashTable, updateComboBox);
		});

		deleteButton.setOnAction(event -> {
			delete(dateComboBox);
			updateComboBox.getItems().clear();
			dateComboBox.getItems().clear();
			updateDateComboBox(hashTable, dateComboBox);
			updateDateComboBox(hashTable, updateComboBox);
		});

		insertButton.setOnAction(event -> {
			insert(insertDatePicker, statusLabel, hashTable);
			updateComboBox.getItems().clear();
			dateComboBox.getItems().clear();
			updateDateComboBox(hashTable, dateComboBox);
			updateDateComboBox(hashTable, updateComboBox);
		});

		printButton.setOnAction(event -> {
			System.out.println(hashTable);
			statusLabel.setText("Hash Table printed to console");
		});

		loadTheCurrent.setOnAction(event -> {

			HNode<MartyrDate>[] martyrNodes = hashTable.getHashTable();
			if (martyrNodes[currentIndex] != null) {
				MartyrDate martyrDate = martyrNodes[currentIndex].getData();
				Martyrs_Screen martyrsScreen = new Martyrs_Screen(hashTable, link, martyrDate.getMartyrsAVL());

				martyrsScreen.start(primaryStage);

			}

		});
	}

	public void insert(DatePicker insertDatePicker, Label statusLabel, MartyrHash<MartyrDate> hashTable2) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to insert the Date?");

		LocalDate selectedDate = insertDatePicker.getValue();
		if (selectedDate == null) {
			showAlert("Error", "No date selected.");
			return;
		}

		confirmAlert.setContentText("This will insert a new Date: " + selectedDate);

		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					statusLabel.setText("Inserting: " + selectedDate);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
					String date = selectedDate.format(formatter);
					MartyrDate martyrDate = new MartyrDate(Driver.calendarTimes(date));
					hashTable2.add(martyrDate);

					showAlert("Success", "Date inserted successfully.");
				} catch (Exception e) {
					showAlert("Error", "An error occurred while inserting the date.");
					e.printStackTrace();
				}
			} else {
				statusLabel.setText("Insertion cancelled.");
			}
		});
	}

	public void delete(ComboBox<String> dateComboBox) {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to delete the selected Date?");
		confirmAlert.setContentText("This will delete the selected Date: " + dateComboBox.getValue());

		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					String selectedDate = dateComboBox.getValue();
					if (selectedDate == null || selectedDate.isEmpty()) {
						showAlert("Error", "No date selected.");
						return;
					}
					statusLabel.setText("Deleting: " + selectedDate);

					MartyrDate martyrDate = new MartyrDate(Driver.calendarTimes(selectedDate));
					boolean removed = hashTable.delete(martyrDate);
					if (removed) {
						showAlert("Success", "Date deleted successfully.");
					} else {
						showAlert("Error", "Date not found.");
					}

				} catch (Exception e) {
					showAlert("Error", "An error occurred while deleting the date.");
					e.printStackTrace();
				}
			} else {
				statusLabel.setText("Deletion cancelled.");
			}
		});
	}

	public void update(DatePicker updateDatePicker, ComboBox<String> dateComboBox, Label statusLabel,
			MartyrHash<MartyrDate> hashTable) {

		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirmation Needed");
		confirmAlert.setHeaderText("Are you sure you want to update the Date?");

		LocalDate selectedDate = updateDatePicker.getValue();

		if (selectedDate == null) {
			showAlert("Error", "No date selected.");
			return;
		}

		confirmAlert.setContentText("This will update the Date: " + selectedDate);

		confirmAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					String oldDateString = dateComboBox.getValue();
					if (oldDateString == null || oldDateString.isEmpty()) {
						showAlert("Error", "No date selected from the combo box.");
						return;
					}

					statusLabel.setText("Updating: " + selectedDate);

					DateTimeFormatter formatterR = DateTimeFormatter.ofPattern("M/d/yyyy");
					String date = selectedDate.format(formatterR);
					Calendar newDateCalendar = Driver.calendarTimes(date);
					Calendar oldDateCalendar = Driver.calendarTimes(oldDateString);

					if (newDateCalendar == null || oldDateCalendar == null) {
						showAlert("Error", "Invalid date format.");
						return;
					}

					MartyrDate newMartyrDate = new MartyrDate(newDateCalendar);
					MartyrDate oldMartyrDate = new MartyrDate(oldDateCalendar);

					MartyrDate existingMartyrDate = Driver.findMartyrDate(oldMartyrDate);
					if (existingMartyrDate == null) {
						showAlert("Error", "Old date not found.");
						return;
					}

					MartyrDate conflictingMartyrDate = Driver.findMartyrDate(newMartyrDate);
					if (conflictingMartyrDate != null) {
						showAlert("Error", "New date already exists.");
						return;
					}

					hashTable.delete(oldMartyrDate);
					existingMartyrDate.setDate(newDateCalendar);
					updatedate(existingMartyrDate.getMartyrsAVL().getRoot(), oldDateString);
					hashTable.add(existingMartyrDate);

					showAlert("Success", "Date updated successfully.");
				} catch (Exception e) {
					showAlert("Error", "An error occurred while updating the date.");
					e.printStackTrace();
				}
			} else {
				statusLabel.setText("Update cancelled.");
			}
		});
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

	private void setupTableView(TableView<HNode<MartyrDate>> tableView) {
		TableColumn<HNode<MartyrDate>, Number> indexCol = new TableColumn<>("Index");
		indexCol.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(tableView.getItems().indexOf(cellData.getValue())));

		TableColumn<HNode<MartyrDate>, String> dateCol = new TableColumn<>("Date");
		dateCol.setCellValueFactory(cellData -> {
			MartyrDate martyrDate = cellData.getValue().getData();
			if (martyrDate != null) {
				Calendar date = martyrDate.getDate();
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				return new SimpleStringProperty(format.format(date.getTime()));
			} else {
				return new SimpleStringProperty("");
			}
		});

		TableColumn<HNode<MartyrDate>, String> flagCol = new TableColumn<>("Flag");
		flagCol.setCellValueFactory(cellData -> {
			Character flag = cellData.getValue().getFlag();
			return new SimpleStringProperty(flag != null ? flag.toString() : "");
		});

		indexCol.setPrefWidth(50);
		dateCol.setPrefWidth(200);
		flagCol.setPrefWidth(200);
		tableView.getColumns().addAll(indexCol, dateCol, flagCol);
		tableView.setEditable(true);
	}

	public void updateTableView(TableView<HNode<MartyrDate>> tableView2, boolean includeEmptySpots) {
		tableView2.getItems().clear();
		HNode<MartyrDate>[] hashTableArray = hashTable.getHashTable();
		List<HNode<MartyrDate>> dates = new ArrayList<>();

		for (HNode<MartyrDate> node : hashTableArray) {
			if (node != null && node.getFlag() == 'F') {
				dates.add(node);
			} else if (includeEmptySpots) {
				dates.add(new HNode<MartyrDate>(null));
			}
		}

		ObservableList<HNode<MartyrDate>> observableDates = FXCollections.observableArrayList(dates);
		tableView2.setItems(observableDates);
	}

	private void navigateUp() {
		do {
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = hashTable.getSize() - 1;
			}
		} while (hashTable.getHashTable()[currentIndex] == null
				|| hashTable.getHashTable()[currentIndex].getData() == null
				|| hashTable.getHashTable()[currentIndex].getFlag() == 'E');
		updateStatusLabel();
		updateSummaryFields();
	}

	private void navigateDown() {
		do {
			currentIndex++;
			if (currentIndex >= hashTable.getSize()) {
				currentIndex = 0;
			}
		} while (hashTable.getHashTable()[currentIndex] == null
				|| hashTable.getHashTable()[currentIndex].getData() == null
				|| hashTable.getHashTable()[currentIndex].getFlag() == 'E');
		updateStatusLabel();
		updateSummaryFields();
	}

	private void updateSummaryFields() {
		totalField.setText("" + getTotalMartyrs());
		averageField.setText("" + getAverageMartyrs());
		districtMaximumField.setText("" + maxDistrict());
		locationMaximumField.setText("" + maxLocation());
	}

	private void updateStatusLabel() {
		HNode<MartyrDate>[] martyrNodes = hashTable.getHashTable();
		if (martyrNodes[currentIndex] != null) {
			MartyrDate martyrDate = martyrNodes[currentIndex].getData();
			statusLabel.setText("Current Index: " + currentIndex + ", MartyrDate: " + martyrDate);
		} else {
			statusLabel.setText("Current Index: " + currentIndex + ", empty");
		}
	}

	public int getTotalMartyrs() {
		AVLTree<Martyr> martyrAVL = hashTable.getHashTable()[currentIndex].getData().getMartyrsAVL();
		if (martyrAVL == null || martyrAVL.isEmpty()) {
			return 0;
		}
		return martyrAVL.size();
	}

	public double getAverageMartyrs() {
		AVLTree<Martyr> martyrAVL = hashTable.getHashTable()[currentIndex].getData().getMartyrsAVL();
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

	public String maxDistrict() {
		AVLTree<Martyr> martyrAVL = hashTable.getHashTable()[currentIndex].getData().getMartyrsAVL();
		if (martyrAVL == null || martyrAVL.isEmpty()) {
			return "";
		}
		return findMaxDistrict(martyrAVL.getRoot());
	}

	public String maxLocation() {
		AVLTree<Martyr> martyrAVL = hashTable.getHashTable()[currentIndex].getData().getMartyrsAVL();
		if (martyrAVL == null || martyrAVL.isEmpty()) {
			return "";
		}
		return findMaxLocation(martyrAVL.getRoot());
	}

	private void updatedate(TNode<Martyr> root, String date) {
		Stack<TNode<Martyr>> stack = new Stack<>();

		Calendar datee = Driver.calendarTimes(date);

		if (root != null) {
			stack.push(root);
		}

		while (!stack.isEmpty()) {
			TNode<Martyr> node = stack.pop();
			node.getData().setDate(datee);
			if (node.getLeft() != null) {
				stack.push(node.getLeft());
			}
			if (node.getRight() != null) {
				stack.push(node.getRight());
			}
		}

	}

	private String findMaxDistrict(TNode<Martyr> root) {
		Stack<TNode<Martyr>> stack = new Stack<>();
		String maxDistrict = "";
		int maxCount = 0;

		if (root != null) {
			stack.push(root);
		}

		while (!stack.isEmpty()) {
			TNode<Martyr> node = stack.pop();
			String district = node.getData().getDistrict();
			int count = countOccurrences(root, district, true);

			if (count > maxCount) {
				maxCount = count;
				maxDistrict = district;
			}

			if (node.getLeft() != null) {
				stack.push(node.getLeft());
			}
			if (node.getRight() != null) {
				stack.push(node.getRight());
			}
		}

		return maxDistrict;
	}

	private String findMaxLocation(TNode<Martyr> root) {
		Stack<TNode<Martyr>> stack = new Stack<>();
		String maxLocation = "";
		int maxCount = 0;

		if (root != null) {
			stack.push(root);
		}

		while (!stack.isEmpty()) {
			TNode<Martyr> node =

					stack.pop();
			String location = node.getData().getLocation();
			int count = countOccurrences(root, location, false);

			if (count > maxCount) {
				maxCount = count;
				maxLocation = location;
			}

			if (node.getLeft() != null) {
				stack.push(node.getLeft());
			}
			if (node.getRight() != null) {
				stack.push(node.getRight());
			}
		}

		return maxLocation;
	}

	private int countOccurrences(TNode<Martyr> node, String value, boolean isDistrict) {
		if (node == null) {
			return 0;
		}

		int count = 0;
		if ((isDistrict && node.getData().getDistrict().equals(value))
				|| (!isDistrict && node.getData().getLocation().equals(value))) {
			count = 1;
		}

		count += countOccurrences(node.getLeft(), value, isDistrict);
		count += countOccurrences(node.getRight(), value, isDistrict);

		return count;
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
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.setPrefSize(400, 200);
		dialogPane.setStyle("-fx-border-color: red; " + "-fx-border-width: 2; " + "-fx-padding: 10; "
				+ "-fx-font-size: 14px; " + "-fx-font-family: 'Arial';");

		alert.showAndWait();
	}

	public TextField createTextField(String promptText) {
		TextField textField = new TextField();
		textField.setMaxHeight(25);
		textField.setMaxWidth(350);
		textField.setPromptText(promptText);
		textField.setFont(new Font("Arial", 14));
		textField.setStyle("-fx-background-color: #FFFFFF; " + "-fx-background-radius: 10; " + "-fx-border-radius: 10; "
				+ "-fx-border-color: #B0C4DE; " + "-fx-padding: 5;");
		return textField;
	}
}
