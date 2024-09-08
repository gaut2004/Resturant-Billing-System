CREATE DATABASE RestaurantBillingSystem;
USE RestaurantBillingSystem;
CREATE TABLE Employees (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    aadhaar_number VARCHAR(12) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, 
    is_admin BOOLEAN DEFAULT FALSE,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE Food_Items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE Bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('Online', 'Cash') NOT NULL,
    emp_id INT,
    FOREIGN KEY (emp_id) REFERENCES Employees(emp_id)
);
CREATE TABLE Bill_Items (
    bill_item_id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT,
    item_id INT,
    quantity INT NOT NULL,
    item_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (bill_id) REFERENCES Bills(bill_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES Food_Items(item_id)
);
CREATE TABLE Employee_Attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    logout_time TIMESTAMP,
    FOREIGN KEY (emp_id) REFERENCES Employees(emp_id)
);
CREATE TABLE Sales (
    sales_id INT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    total_sales DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO Employees (name, phone_number, gender, aadhaar_number, username, password, is_admin) 
VALUES 
('John Doe', '1234567890', 'Male', '111122223333', 'johndoe', 'password123', TRUE),
('Jane Smith', '0987654321', 'Female', '444455556666', 'janesmith', 'password456', FALSE);

INSERT INTO Food_Items (name, price) 
VALUES 
('Pizza', 299.99),
('Burger', 149.99),
('Pasta', 199.99),
('Coke', 49.99);

INSERT INTO Sales (date, total_sales) VALUES 
('2024-08-01', 1500.50),
('2024-08-02', 2000.75);

INSERT INTO Employees (name, phone_number, gender, aadhaar_number, username, password, is_admin)
VALUES ('Gautam', '123456890', 'Male', '11112222333', 'Gaut2004', 'password127', TRUE);

SELECT * FROM Employees;




