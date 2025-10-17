# Product Catalog Service
This project provides a Spring Boot application designed for managing a product catalog, primarily through CSV file ingestion. It is fully containerized using Docker Compose for easy setup and deployment, including a dedicated PostgreSQL database.
### Setup and Running Instructions
This application is designed to run easily using Docker Compose.
#### Note: Please change the database credentials like user, password, db before running below commands in docker-compose.yml, application-create.properties, application-update.properties
#### Prerequisites
You must have Docker and Docker Compose installed on your system and it runs.
#### 1. Build and Run the Stack
Navigate to the root directory of the project where docker-compose.yml and the Dockerfile are located, and run the following command:

#### docker compose up --build
-----------------------------------
This command performs three main actions:
#### i.	Builds the Application Image: 
It uses the multi-stage Dockerfile to compile the Java code and create a lightweight product-catalog-app image.
#### ii.	Starts the Database: 
It launches the PostgreSQL container (streamoid-db).
#### iii.	Starts the Application: 
It launches the product-app container, waiting for the database to be ready before connecting.
#### 2. Access the Application
The application will be accessible at:
🔗 Base URL: http://localhost:8080
#### 3. Stop the Stack
To stop the running services (while preserving the database data in the postgres_data volume):
#### docker compose down
----------------------------------------------
To stop the services and remove all containers and volumes (deleting all database data):
#### docker compose down --volumes
-------------------------------------
### API Documentation
The application exposes REST endpoints for product management and CSV ingestion.
### Base Endpoint
#### 1. Ingest Products via CSV (Main Functionality)
This endpoint allows you to upload a CSV file containing product data.
#### Method	Endpoint	Description
POST /upload Uploads a CSV file, parses the data, and saves products to the database.
#### Sample Request (Using curl)
Send a multipart/form-data request with the CSV file attached under the name file.
Assumed CSV Structure: The application expects the CSV to contain columns that map directly to your product model (e.g., sku, name, brand, price, mrp, color, size etc.).
#### curl -X POST -F "file=@products.csv" http://localhost:8000/upload  
### 
or you can also use post man to hit the endpoints.
###
-F "file=@/path/to/your/products.csv;type=text/csv"
(Replace /path/to/your/products.csv with the actual path on your local machine.)
###
#### Sample Response (Success - Status 200 OK)
{
    "stored": 20,
    "failed": []
}

###
#### Sample Response (Error - Status 400 Bad Request)
{
    "stored": 0,
    "failed": [
        {
            "error": "File is empty!"
        }
    ]
}
###
#### 2. Retrieve All Products
This endpoint fetches all products currently stored in the database with pagination support .
#### Method Endpoint Description	
GET	/products	
###
Retrieves a list of all products with page limit 10.	
###
#### Sample Response
{
    "content": [
        {
            "id": 20,
            "sku": "HOODIE-CRM-M",
            "name": "Cozy Hoodie",
            "brand": "SnugWear",
            "color": "Cream",
            "size": "M",
            "mrp": 2199,
            "price": 1699,
            "quantity": 9,
            "valid": true
        },
        {
            "id": 19,
            "sku": "HOODIE-CHR-XL",
            "name": "Cozy Hoodie",
            "brand": "SnugWear",
            "color": "Charcoal",
            "size": "XL",
            "mrp": 2199,
            "price": 1799,
            "quantity": 11,
            "valid": true
        },
        {
            "id": 18,
            "sku": "SHIRT-PLN-L",
            "name": "Plain Oxford Shirt",
            "brand": "ButtonUp",
            "color": "Blue",
            "size": "L",
            "mrp": 1899,
            "price": 1499,
            "quantity": 12,
            "valid": true
        },
        {
            "id": 17,
            "sku": "SHIRT-CHK-M",
            "name": "Checked Casual Shirt",
            "brand": "ButtonUp",
            "color": "Multi",
            "size": "M",
            "mrp": 1799,
            "price": 1399,
            "quantity": 16,
            "valid": true
        },
        {
            "id": 16,
            "sku": "TSHIRT-WHT-XS",
            "name": "Graphic Tee",
            "brand": "UrbanEdge",
            "color": "White",
            "size": "XS",
            "mrp": 899,
            "price": 649,
            "quantity": 14,
            "valid": true
        },
        {
            "id": 15,
            "sku": "TSHIRT-GRY-S",
            "name": "Graphic Tee",
            "brand": "UrbanEdge",
            "color": "Grey",
            "size": "S",
            "mrp": 899,
            "price": 699,
            "quantity": 30,
            "valid": true
        },
        {
            "id": 14,
            "sku": "JKT-OLV-L",
            "name": "Utility Jacket",
            "brand": "UrbanEdge",
            "color": "Olive",
            "size": "L",
            "mrp": 3499,
            "price": 2999,
            "quantity": 6,
            "valid": true
        },
        {
            "id": 13,
            "sku": "KURTA-BLU-M",
            "name": "Cotton Kurta",
            "brand": "Ethniq",
            "color": "Blue",
            "size": "M",
            "mrp": 1599,
            "price": 1299,
            "quantity": 22,
            "valid": true
        },
        {
            "id": 12,
            "sku": "SAREE-RED-001",
            "name": "Banarasi Silk Saree",
            "brand": "Ethniq",
            "color": "Red",
            "size": "Free",
            "mrp": 6999,
            "price": 5999,
            "quantity": 5,
            "valid": true
        },
        {
            "id": 11,
            "sku": "BELT-BRN-38",
            "name": "Leather Belt",
            "brand": "CarryCo",
            "color": "Brown",
            "size": "38",
            "mrp": 1199,
            "price": 899,
            "quantity": 40,
            "valid": true
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": false,
    "totalElements": 20,
    "totalPages": 2,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 10,
    "first": true,
    "empty": false
}
###
#### 3. Search Products by brand
This endpoint fetches all products currently stored in the database with pagination support .
#### Method Endpoint Description	
GET	/products/search?brand=StreamThreads
###
Retrieves a list of all products with brand name StreamThreads with page limit 10.	
###
{
    "content": [
        {
            "id": 3,
            "sku": "POLO-GRN-003",
            "name": "Heritage Polo",
            "brand": "StreamThreads",
            "color": "Green",
            "size": "XL",
            "mrp": 1299,
            "price": 999,
            "quantity": 8,
            "valid": true
        },
        {
            "id": 2,
            "sku": "TSHIRT-BLK-002",
            "name": "Classic Cotton T-Shirt",
            "brand": "StreamThreads",
            "color": "Black",
            "size": "L",
            "mrp": 799,
            "price": 549,
            "quantity": 12,
            "valid": true
        },
        {
            "id": 1,
            "sku": "TSHIRT-RED-001",
            "name": "Classic Cotton T-Shirt",
            "brand": "StreamThreads",
            "color": "Red",
            "size": "M",
            "mrp": 799,
            "price": 499,
            "quantity": 20,
            "valid": true
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 3,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 3,
    "first": true,
    "empty": false
}

###
#### 4. Search Products between min and max price
This endpoint fetches all products currently stored in the database with pagination support .
#### Method Endpoint Description	
GET	/products/search?minPrice=1000&maxPrice=1400	
###
Retrieves a list of all products between max and min price with page limit 10.	
###
{
    "content": [
        {
            "id": 17,
            "sku": "SHIRT-CHK-M",
            "name": "Checked Casual Shirt",
            "brand": "ButtonUp",
            "color": "Multi",
            "size": "M",
            "mrp": 1799,
            "price": 1399,
            "quantity": 16,
            "valid": true
        },
        {
            "id": 13,
            "sku": "KURTA-BLU-M",
            "name": "Cotton Kurta",
            "brand": "Ethniq",
            "color": "Blue",
            "size": "M",
            "mrp": 1599,
            "price": 1299,
            "quantity": 22,
            "valid": true
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
}
### Testing
#### 1. Connectivity Test
Verify the application is running and reachable by accessing a simple endpoint, if available, or just the base URL.
####
curl http://localhost:8080/
#### 2. CSV Ingestion Test
To fully test the ingestion pipeline, ensure you have a sample CSV file prepared and run the curl command provided in the API Documentation section (Endpoint 1).
#### 3. Data Retrieval Test
After successful CSV ingestion, verify the data was saved correctly:
####
curl http://localhost:8080/api/products
The output should list the products you just uploaded.

