use assignment01;
 
-- PRODUCTS, PRODUCT ITEMS, VARIATIONS, OPTIONS & CONFIG
IF OBJECT_ID('category', 'U') IS NOT NULL
    DROP TABLE category;

CREATE TABLE category (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    parent_category_id INT,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (parent_category_id) REFERENCES category(id)
);

INSERT INTO category (parent_category_id , name) VALUES
(NULL, 'Electronics'),
(NULL, 'Clothing'),
(NULL, 'Books'),
(NULL, 'Furniture'),
(NULL, 'Toys'),
(NULL, 'Sports'),
(NULL, 'Groceries'),
(NULL, 'Beauty'),
(NULL, 'Automotive'),
(NULL, 'Music');

SELECT * FROM category;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('product', 'U') IS NOT NULL
    DROP TABLE product;

CREATE TABLE product (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    category_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    cover_image_link TEXT,
    FOREIGN KEY (category_id) REFERENCES category(id)
);

INSERT INTO product (category_id, name, description, cover_image_link) VALUES
(1, 'Smartphone X1', 'High-end phone', 'url'),
(1, 'Smartphone X2', 'Mid-range phone', 'url'),
(1, 'Tablet A1', 'Portable tablet', 'url'),
(1, 'Laptop Z', 'Gaming laptop', 'url'),
(1, 'Monitor M1', '27 inch monitor', 'url'),
(1, 'Headphone Pro', 'Noise-canceling', 'url'),
(1, 'Camera C1', 'DSLR camera', 'url'),
(1, 'Smartwatch S1', 'Fitness tracker', 'url'),
(1, 'Speaker Boom', 'Bluetooth speaker', 'url'),
(1, 'Drone D1', '4K drone', 'url');

SELECT * FROM product;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('product_item', 'U') IS NOT NULL
    DROP TABLE product_item;

CREATE TABLE product_item (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    product_id INT NOT NULL,
    sku VARCHAR(100) NOT NULL,
    quantity_in_stock INT DEFAULT 0,
    item_image_link TEXT,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO product_item (product_id, sku, quantity_in_stock, item_image_link, price) VALUES
(1, 'X1-BLK-64', 10, 'img', 699),
(1, 'X1-WHT-128', 5, 'img', 799),
(2, 'X2-GRY-64', 7, 'img', 599),
(3, 'TA1-SLV-10', 15, 'img', 399),
(4, 'LZ-16-512', 6, 'img', 1099),
(5, 'MM1-BLK', 12, 'img', 299),
(6, 'HP-RED-BT', 14, 'img', 199),
(7, 'CC1-STD', 3, 'img', 999),
(8, 'SW1-BLK-S', 8, 'img', 149),
(9, 'SPK-BLK-10W', 11, 'img', 99),
(10, 'D1-4K', 4, 'img', 899);


SELECT * FROM product_item;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('variation', 'U') IS NOT NULL
    DROP TABLE variation;

CREATE TABLE variation (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    product_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO variation (product_id, name) VALUES
(1, 'Color'), (1, 'Storage'),
(2, 'Color'), (2, 'Storage'),
(3, 'Color'), (3, 'Size'),
(4, 'RAM'), (4, 'Storage'),
(5, 'Color'),
(6, 'Color'), (6, 'Connectivity'),
(7, 'Lens Type'),
(8, 'Band Color'), (8, 'Size'),
(9, 'Color'), (9, 'Watts'),
(10, 'Camera Quality');

SELECT * FROM variation;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('variation_option', 'U') IS NOT NULL
    DROP TABLE variation_option;

CREATE TABLE variation_option (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    variation_id INT NOT NULL,
    value VARCHAR(100) NOT NULL,
    FOREIGN KEY (variation_id) REFERENCES variation(id)
);

INSERT INTO variation_option(variation_id, value) VALUES
(1, 'Black'), (1, 'White'),
(2, '64GB'), (2, '128GB'),
(3, 'Gray'), (3, 'Blue'),
(4, '64GB'), (4, '256GB'),
(5, 'Silver'), (5, 'Black'),
(6, '10 inch'), (6, '12 inch'),
(7, '16GB'), (7, '32GB'),
(8, '512GB'), (8, '1TB'),
(9, 'Black'), (9, 'White'),
(10, 'Black'), (10, 'Red'),
(11, 'Bluetooth'), (11, 'Wired'),
(12, 'Standard'), (12, 'Wide Angle'),
(13, 'Black'), (13, 'Gray'),
(14, 'S'), (14, 'M'), (14, 'L'),
(15, 'Black'), (15, 'Blue'),
(16, '10W'), (16, '20W'),
(17, '4K'), (17, '1080p');

SELECT * FROM variation_option;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('product_configuration', 'U') IS NOT NULL
    DROP TABLE product_configuration;

CREATE TABLE product_configuration (
    item_id INT, -- Auto-incrementing ID
    option_id INT,
    PRIMARY KEY (item_id, option_id),
    FOREIGN KEY (item_id) REFERENCES product_item(id),
    FOREIGN KEY (option_id) REFERENCES variation_option(id)
);

INSERT INTO product_configuration VALUES
(1, 1), (1, 3),   -- X1 Black 64GB
(2, 2), (2, 4),   -- X1 White 128GB
(3, 5), (3, 7),   -- X2 Gray 64GB
(4, 9), (4, 11),  -- Tablet Silver 10"
(5, 13), (5, 15), -- Laptop 16GB RAM + 512GB
(6, 17),          -- Monitor Black
(7, 20), (7, 21), -- Headphone Red BT
(8, 23),          -- Camera Standard lens
(9, 25), (9, 27), -- Watch Black Small
(10, 30), (10, 32), -- Speaker Black 10W
(11, 34);         -- Drone 4K

SELECT * FROM product_configuration;

-- USER, SHOPPING CART AND SHOPPING CART ITEMS
IF OBJECT_ID('users', 'U') IS NOT NULL
    DROP TABLE users;

CREATE TABLE users (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    email_address VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    hashed_password VARCHAR(255) NOT NULL
);

INSERT INTO users (email_address, phone_number, hashed_password) VALUES
('alice@example.com', '0123456789', 'hashedpwd1'),
('bob@example.com', '0987654321', 'hashedpwd2'),
('charlie@example.com', '0911222333', 'hashedpwd3'),
('david@example.com', '0909876543', 'hashedpwd4'),
('eva@example.com', '0933445566', 'hashedpwd5'),
('frank@example.com', '0944556677', 'hashedpwd6'),
('grace@example.com', '0955667788', 'hashedpwd7'),
('henry@example.com', '0966778899', 'hashedpwd8'),
('irene@example.com', '0977889900', 'hashedpwd9'),
('jack@example.com', '0988990011', 'hashedpwd10');

SELECT * FROM USERS;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('shopping_cart', 'U') IS NOT NULL
    DROP TABLE shopping_cart;

CREATE TABLE shopping_cart (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO shopping_cart (user_id) VALUES
(1), (1),
(2), (2),
(3), (3),
(4), (4),
(5), (5),
(6), (6),
(7), (7),
(8), (8),
(9), (9),
(10), (10);

SELECT * FROM shopping_cart;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('shopping_cart_item', 'U') IS NOT NULL
    DROP TABLE shopping_cart_item;

CREATE TABLE shopping_cart_item (
    id INT PRIMARY KEY IDENTITY(1,1), -- Auto-incrementing ID
    cart_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (cart_id) REFERENCES shopping_cart(id),
    FOREIGN KEY (item_id) REFERENCES product_item(id)
);

INSERT INTO shopping_cart_item (cart_id, item_id, quantity) VALUES
-- Cart 1
(1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1), (1, 5, 1),
(1, 6, 1), (1, 7, 1), (1, 8, 1), (1, 9, 1), (1, 10, 1),

-- Cart 2
(2, 1, 1), (2, 2, 1), (2, 3, 1), (2, 4, 1), (2, 5, 1),
(2, 6, 1), (2, 7, 1), (2, 8, 1), (2, 9, 1), (2, 10, 1),

-- Cart 3
(3, 1, 1), (3, 2, 1), (3, 3, 1), (3, 4, 1), (3, 5, 1),
(3, 6, 1), (3, 7, 1), (3, 8, 1), (3, 9, 1), (3, 10, 1),

-- Cart 4
(4, 1, 1), (4, 2, 1), (4, 3, 1), (4, 4, 1), (4, 5, 1),
(4, 6, 1), (4, 7, 1), (4, 8, 1), (4, 9, 1), (4, 10, 1),

-- Cart 5
(5, 1, 1), (5, 2, 1), (5, 3, 1), (5, 4, 1), (5, 5, 1),
(5, 6, 1), (5, 7, 1), (5, 8, 1), (5, 9, 1), (5, 10, 1),

-- Cart 6
(6, 1, 1), (6, 2, 1), (6, 3, 1), (6, 4, 1), (6, 5, 1),
(6, 6, 1), (6, 7, 1), (6, 8, 1), (6, 9, 1), (6, 10, 1),

-- Cart 7
(7, 1, 1), (7, 2, 1), (7, 3, 1), (7, 4, 1), (7, 5, 1),
(7, 6, 1), (7, 7, 1), (7, 8, 1), (7, 9, 1), (7, 10, 1),

-- Cart 8
(8, 1, 1), (8, 2, 1), (8, 3, 1), (8, 4, 1), (8, 5, 1),
(8, 6, 1), (8, 7, 1), (8, 8, 1), (8, 9, 1), (8, 10, 1),

-- Cart 9
(9, 1, 1), (9, 2, 1), (9, 3, 1), (9, 4, 1), (9, 5, 1),
(9, 6, 1), (9, 7, 1), (9, 8, 1), (9, 9, 1), (9, 10, 1),

-- Cart 10
(10, 1, 1), (10, 2, 1), (10, 3, 1), (10, 4, 1), (10, 5, 1),
(10, 6, 1), (10, 7, 1), (10, 8, 1), (10, 9, 1), (10, 10, 1),

-- Cart 11
(11, 1, 1), (11, 2, 1), (11, 3, 1), (11, 4, 1), (11, 5, 1),
(11, 6, 1), (11, 7, 1), (11, 8, 1), (11, 9, 1), (11, 10, 1),

-- Cart 12
(12, 1, 1), (12, 2, 1), (12, 3, 1), (12, 4, 1), (12, 5, 1),
(12, 6, 1), (12, 7, 1), (12, 8, 1), (12, 9, 1), (12, 10, 1),

-- Cart 13
(13, 1, 1), (13, 2, 1), (13, 3, 1), (13, 4, 1), (13, 5, 1),
(13, 6, 1), (13, 7, 1), (13, 8, 1), (13, 9, 1), (13, 10, 1),

-- Cart 14
(14, 1, 1), (14, 2, 1), (14, 3, 1), (14, 4, 1), (14, 5, 1),
(14, 6, 1), (14, 7, 1), (14, 8, 1), (14, 9, 1), (14, 10, 1),

-- Cart 15
(15, 1, 1), (15, 2, 1), (15, 3, 1), (15, 4, 1), (15, 5, 1),
(15, 6, 1), (15, 7, 1), (15, 8, 1), (15, 9, 1), (15, 10, 1),

-- Cart 16
(16, 1, 1), (16, 2, 1), (16, 3, 1), (16, 4, 1), (16, 5, 1),
(16, 6, 1), (16, 7, 1), (16, 8, 1), (16, 9, 1), (16, 10, 1),

-- Cart 17
(17, 1, 1), (17, 2, 1), (17, 3, 1), (17, 4, 1), (17, 5, 1),
(17, 6, 1), (17, 7, 1), (17, 8, 1), (17, 9, 1), (17, 10, 1),

-- Cart 18
(18, 1, 1), (18, 2, 1), (18, 3, 1), (18, 4, 1), (18, 5, 1),
(18, 6, 1), (18, 7, 1), (18, 8, 1), (18, 9, 1), (18, 10, 1),

-- Cart 19
(19, 1, 1), (19, 2, 1), (19, 3, 1), (19, 4, 1), (19, 5, 1),
(19, 6, 1), (19, 7, 1), (19, 8, 1), (19, 9, 1), (19, 10, 1),

-- Cart 20
(20, 1, 1), (20, 2, 1), (20, 3, 1), (20, 4, 1), (20, 5, 1),
(20, 6, 1), (20, 7, 1), (20, 8, 1), (20, 9, 1), (20, 10, 1);

SELECT * FROM shopping_cart_item;

-- ORDER STATUS
IF OBJECT_ID('order_status', 'U') IS NOT NULL
    DROP TABLE order_status;

CREATE TABLE order_status (
    id INT PRIMARY KEY IDENTITY(1,1),
    status VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO order_status (status) VALUES
('Pending'),        -- Đang chờ xác nhận
('Confirmed'),      -- Đã xác nhận
('Processing'),     -- Đang xử lý
('Shipped'),        -- Đã giao cho đơn vị vận chuyển
('Delivered'),      -- Đã giao thành công
('Cancelled'),      -- Bị hủy bởi người dùng/hệ thống
('Returned'),       -- Trả hàng
('Refunded');       -- Đã hoàn tiền

SELECT * FROM order_status ORDER BY id;

-- PAYMENT TYPE, PAYMENT METHOD, SHIPPING METHOD
IF OBJECT_ID('payment_type', 'U') IS NOT NULL
    DROP TABLE payment_type;

CREATE TABLE payment_type (
    id INT PRIMARY KEY IDENTITY(1,1),
    value VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO payment_type (value) VALUES
('Credit Card'),
('Debit Card'),
('PayPal'),
('Bank Transfer'),
('Cash on Delivery'),
('Apple Pay'),
('Google Pay'),
('ZaloPay');

SELECT * FROM payment_type ORDER BY id;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('payment_method', 'U') IS NOT NULL
    DROP TABLE payment_method;

CREATE TABLE payment_method (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    payment_type_id INT NOT NULL,
    provider VARCHAR(100),
    account_number VARCHAR(50),
    expiry_date DATE,
    is_default BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (payment_type_id) REFERENCES payment_type(ID)
);

INSERT INTO payment_method (user_id, payment_type_id, provider, account_number, expiry_date, is_default) VALUES
(1, 1, 'Visa', '4111111111111111', '2027-12-31', 1),
(1, 3, 'PayPal', 'user1@paypal.com', NULL, 0),

(2, 2, 'Mastercard', '5500000000000004', '2026-11-30', 1),
(2, 4, 'Techcombank', '123456789012', NULL, 0),

(3, 5, 'COD', NULL, NULL, 1),

(4, 6, 'Apple Pay', 'apple-user4', NULL, 1),

(5, 7, 'Google Pay', 'gpay-user5', NULL, 1),

(6, 1, 'Visa', '4111111111110000', '2025-05-31', 1),

(7, 2, 'Mastercard', '5105105105105100', '2026-06-30', 1),

(8, 8, 'ZaloPay', 'zalopay-user8', NULL, 1),

(9, 1, 'Visa', '4111111111112222', '2027-03-31', 1),

(10, 3, 'PayPal', 'user10@paypal.com', NULL, 1);

SELECT * FROM payment_method;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('shipping_method', 'U') IS NOT NULL
    DROP TABLE shipping_method;

CREATE TABLE shipping_method (
    id INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100) NOT NULL UNIQUE,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0)
);

INSERT INTO shipping_method (name, price) VALUES
('Standard Shipping', 5.00),
('Express Shipping', 10.00),
('Same Day Delivery', 20.00),
('Free Shipping', 0.00),
('International Shipping', 25.00);

SELECT * FROM shipping_method;

-- COUNTRY, ADDRESS, USER ADDRESS
IF OBJECT_ID('country', 'U') IS NOT NULL
    DROP TABLE country;

CREATE TABLE country (
    id INT PRIMARY KEY IDENTITY(1,1),
    country_name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO country (country_name) VALUES
('United States'),
('Canada'),
('United Kingdom'),
('Germany'),
('France'),
('Australia'),
('Japan'),
('South Korea'),
('Vietnam'),
('India');

SELECT * FROM country ORDER BY id;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('address', 'U') IS NOT NULL
    DROP TABLE address;

CREATE TABLE address (
    id INT PRIMARY KEY IDENTITY(1,1),
    country_id INT NOT NULL,
    unit_number VARCHAR(20),
    street_number VARCHAR(20),
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    region VARCHAR(100),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

INSERT INTO address (
    country_id, unit_number, street_number, address_line1, address_line2, city, region
) VALUES
(1, 'Apt 101', '123', 'Main St', NULL, 'New York', 'NY'),
(2, 'Unit 202', '456', 'Queen St', 'Building B', 'Toronto', 'Ontario'),
(3, NULL, '789', 'Baker St', NULL, 'London', 'Greater London'),
(4, 'Apt 12', '12A', 'Unter den Linden', NULL, 'Berlin', 'Berlin'),
(5, NULL, '99', 'Champs-Élysées', 'Floor 3', 'Paris', 'Île-de-France'),
(6, 'Suite 300', '321', 'George St', NULL, 'Sydney', 'NSW'),
(7, 'Room 5F', '88', 'Shibuya Crossing', 'Tower 1', 'Tokyo', 'Kanto'),
(8, NULL, '555', 'Gangnam-daero', NULL, 'Seoul', 'Seoul'),
(9, 'Block C', '28', 'Le Duan', NULL, 'Ho Chi Minh City', 'District 1'),
(10, 'Flat 3B', '101', 'MG Road', 'Near Central Mall', 'Bangalore', 'Karnataka');

SELECT * FROM address;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('user_address', 'U') IS NOT NULL
    DROP TABLE user_address;

CREATE TABLE user_address (
    user_id INT NOT NULL,
    address_id INT NOT NULL,
    is_default BIT NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, address_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (address_id) REFERENCES address(id)
);

INSERT INTO user_address (user_id, address_id, is_default) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1),
(7, 7, 1),
(8, 8, 1),
(9, 9, 1),
(10, 10, 1);

SELECT * FROM user_address;

-- SHOPPING ORDER, ORDER LINE
IF OBJECT_ID('shopping_order', 'U') IS NOT NULL
    DROP TABLE shopping_order;

CREATE TABLE shopping_order (
    id INT PRIMARY KEY IDENTITY(1,1),
    order_date DATETIME NOT NULL DEFAULT GETDATE(),
    order_total DECIMAL(12, 2) NOT NULL CHECK (order_total >= 0),
    order_status_id INT NOT NULL,
    payment_method_id INT NOT NULL,
    shipping_method_id INT NOT NULL,
    address_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME NOT NULL DEFAULT GETDATE(),

    FOREIGN KEY (order_status_id) REFERENCES order_status(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(id),
    FOREIGN KEY (shipping_method_id) REFERENCES shipping_method(id),
    FOREIGN KEY (address_id) REFERENCES address(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO shopping_order
    (order_date, order_total, order_status_id, payment_method_id, shipping_method_id, address_id, user_id, created_at, updated_at)
VALUES
    ('2025-05-01 10:30:00', 1498.50, 1, 2, 1, 1, 1, GETDATE(), GETDATE()),
    ('2025-05-02 15:45:00', 799.00, 2, 1, 2, 2, 2, GETDATE(), GETDATE()),
    ('2025-05-03 08:20:00', 399.00, 3, 3, 1, 3, 3, GETDATE(), GETDATE()),
    ('2025-05-04 12:10:00', 2599.00, 1, 1, 3, 4, 4, GETDATE(), GETDATE()),
    ('2025-05-05 17:00:00', 299.00, 4, 2, 2, 5,5, GETDATE(), GETDATE()),
    ('2025-05-06 14:15:00', 1099.99, 1, 3, 1, 6, 6, GETDATE(), GETDATE()),
    ('2025-05-07 09:00:00', 899.00, 2, 1, 3, 7, 7, GETDATE(), GETDATE()),
    ('2025-05-08 16:45:00', 199.99, 3, 2, 2, 8, 8, GETDATE(), GETDATE()),
    ('2025-05-09 11:30:00', 599.50, 1, 3, 1, 9, 9, GETDATE(), GETDATE()),
    ('2025-05-10 18:20:00', 399.99, 4, 1, 3, 10, 10, GETDATE(), GETDATE());

SELECT * FROM shopping_order;

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
IF OBJECT_ID('order_line', 'U') IS NOT NULL
    DROP TABLE order_line;

CREATE TABLE order_line (
    id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(12, 2) NOT NULL CHECK (price >= 0),

    FOREIGN KEY (order_id) REFERENCES shopping_order(id),
    FOREIGN KEY (item_id) REFERENCES product_item(id)
);

INSERT INTO order_line (order_id, item_id, quantity, price) VALUES
(1, 1, 2, 699.00),
(1, 2, 1, 799.00),
(2, 3, 3, 599.00),
(2, 4, 1, 399.00),
(3, 5, 1, 1099.00),
(3, 6, 2, 299.00),
(4, 7, 1, 199.00),
(4, 8, 1, 999.00),
(5, 9, 2, 149.00),
(5, 10, 1, 99.00),
(6, 1, 1, 699.00),
(6, 3, 2, 599.00),
(7, 5, 1, 1099.00),
(7, 7, 1, 199.00),
(8, 2, 1, 799.00),
(8, 4, 3, 399.00),
(9, 6, 2, 299.00),
(9, 8, 1, 999.00),
(10, 9, 1, 149.00),
(10, 10, 2, 99.00);

SELECT * FROM shopping_order;

-- REVIEW
IF OBJECT_ID('review', 'U') IS NOT NULL
    DROP TABLE review;

CREATE TABLE review (
    id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    ordered_product_id INT NOT NULL,
    rating_value INT NOT NULL CHECK (rating_value >= 1 AND rating_value <= 5),
    comment VARCHAR(1000),

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (ordered_product_id) REFERENCES product_item(id)
);

INSERT INTO review (user_id, ordered_product_id, rating_value, comment) VALUES
(1, 1, 5, 'Excellent product, highly recommend!'),
(2, 2, 4, 'Good quality but a bit pricey.'),
(3, 3, 3, 'Average experience, nothing special.'),
(4, 4, 5, 'Loved it, will buy again!'),
(5, 5, 2, 'Not as expected, disappointing.'),
(6, 6, 4, 'Works well, but shipping was slow.'),
(7, 7, 5, 'Perfect for my needs.'),
(8, 8, 3, 'Okay product, decent value.'),
(9, 9, 1, 'Poor quality, do not buy.'),
(10, 10, 4, 'Good product overall, satisfied.');

SELECT * FROM review;