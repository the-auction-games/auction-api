# Auction API
This is the Auction API for The Auction Games. It provides functionality for creating, updating, querying, and deleting auctions. In addition, there are endpoints for creating bids or purchasing auctions. The API utilizes Dapr for service discovery and state management.

## Project Structure
The project is structured as follows:
```
.
├── src/main/java/com/theauctiongames/activityapi/      'Source files'
│   ├── business/                                       'Business logic'                           
│   │   ├── controllers/                                'Business Controllers'
│   │   ├── models/                                     'Business Models'
│   │   └── services/                                   'Business Services'
│   ├── config/                                         'Configuration files'
│   ├── data/                                           'Data access'
│   │   ├── daos/                                       'Data Access Objects'
│   │   └── entities/                                   'Data Entities'
│   └── ActivityApiApplication.java                     'Application entry point'
├── pom.xml                                             'Maven dependencies'
└── README.md                                           'This file'
```

## API Documentation
https://app.swaggerhub.com/apis/JOELSMITH2019/auction-api/1.0.0
