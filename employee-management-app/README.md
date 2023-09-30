# Employee Management App

Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [How to Use This App](#how-to-use-this-app)
- [Using the App](#using-the-app)
- [Data Persistence](#data-persistence)
- [CSV File Format](#csv-file-format)
- [Version](#version-)
- [License](#license)


## Overview

This Employee Management App is a simple Java application built using JavaFX. 
It allows users to manage employee records, including adding, updating, deleting, searching, and importing data from CSV files. 
Additionally, the app stores employee data for future use.

## Features

- **Add Employee:** Users can add new employee records with unique IDs, names, and contact details.
- **Update Employee:** Existing employee records can be updated with new information.
- **Delete Employee:** Employees can be removed from the system.
- **Search Employee:** A search feature allows users to find employees by name, contact, or ID.
- **Import Data:** Users can import employee data from CSV files.
- **Data Persistence:** Employee data is saved and loaded when the application is closed and reopened.

## Getting Started

### Prerequisites

To run this application, you need the following:

- Java Development Kit (JDK)
- JavaFX

### How to Use This App

1. **Clone the Repository:**

   Clone this repository to your local machine using the following command:
     ```shell
   git clone https://github.com/SahanLakmalDev/Employee-Management-App.git
   ```
2. **Open the Project:**

Open the project in your preferred Java development environment (e.g., IntelliJ IDEA, Eclipse).

3. **Use the App:**

- Click the "New" button to add a new employee.
- Fill in the employee's ID, name, and contact information.
- Click the "Save" button to save the employee's details.
- Select an employee from the table to update or delete their information.
- Use the search bar to filter employees by name, contact, or ID.
- Drag and drop a CSV file onto the table to import employee data.

## Data Persistence

The application uses serialization to store employee data in a file named `employee.db`.
This file is loaded when the app starts and saved when changes are made to employee records.

## CSV File Format

When importing data from a CSV file, ensure that the file follows this format:

| EmployeeID | EmployeeName | ContactNumber |
|------------|--------------|---------------|
| E-001      | John Doe     | 071-1234567   |
| E-002      | Jane Smith   | 077-9876543   |

## Version 
0.1.0

## License
Copyright &copy: 2023 Sahan Lakmal <br>
This project is license under this [MIT License](License.txt)
