# Oszczedzator3000 - Beckend

# Table of contents 
* [About the project](#about-the-project)
* [Current state of the project](#current-state-of-the-project)
* [Technologies used](#technologies-used)
* [How to run it?](#how-to-run-it?)
* [How to use this API](#how-to-use-this-api?)
    * [Expense](#expense)
    * [User Personal Details](#user-personal-details)
    * [Goal Analyser](#goal-analyser)
    * [Expense Optimiser](#expense-optimiser)

# About the project
Oszczedzator3000 is a web application that allows people to see where they make mistakes managing their money and track their expenses. Users also can see if they can achieve some goal they have to pay for and in case they can't, they can try to change their saving habits. Users can compare their expenses with users with silimilar life parameters(salary, age etc.) without seeing who exactly buy what and for how much.

This is only backend part of the project, frontend can be found there. Work is still in progress. This is also a college related project for one of the courses.

# Current state of the project
Currently project has implemented REST api with all the endpoints for dataaccess user related. Project is also connected to H2 database with PostgreSQL ready to be connected in the future.

# Technologies used
* Java 11
* Spring Boot 2.4.4
* H2 Database 
* Spring Data JPA
* Spring Security 
* Map Struct 1.4.2
* Lombok 1.18.18

# How to run it?

# How to use this API?

Application by default uses port 8080, to change it edit ``application.properties`` by adding line ``server.port=n`` where n is chosen port. 

Values of the json properties in documentation are types that have to be provided to receive 2xx response status. 

## Expense
Expenses are accessible only for the users that owns them. Users can get, post, put or delete their expenses. User can get all of his/her expenses or filter them.

### Data Transfer Objects
#### Request with filters
```
{
    "category": string,
    "min_value": double,
    "max_value": double,
    "start_date": YYYY-MM-DD,
    "end_date": YYYY-MM-DD,
    "name": string
}
```
* ``category`` - one of categories, that are available at the endpoint ``/api/v1/enums/category``
* ``min_value`` - minimum value of the filtered expenses
* ``max_value`` - maximum value of the filtered expenses
* ``start_date`` - minimum date from which expenses will be filtered
* ``end_date`` - maximum date from which expenses will be filtered
* ``name`` - name of the searched expense

#### Request with expense data
```
{
    "category": string,
    "name": string,
    "value": double,
    "date": YYYY-MM-DD
}
```
* ``category`` - one of categories, that are available at the endpoint ``/api/v1/enums/category``
* ``name`` - name of the expense
* ``value`` - value of expense
* ``date`` - date when the expense was recorded

#### Expense Response
```
{
    "category": string,
    "name": string,
    "value": double,
    "date": YYYY-MM-DD,
    "expense_id": long
}
```
All of properties are the same as in [Request with expense data](#request-with-expense-data) with one addition

``expense_id`` - unique id of an expense

### Methods

All requests but POST and DELETE return one or more [Expense response](#expense-response).

#### GET all expenses
```
api/v1/users/{user_id}/expenses
```

``user_id`` - unique id of the user
There are also optional parameters
* ``page`` - id of page, starting from 0, default is 0
* ``size`` - size of page, 10 by default

#### GET filtered expenses
```
api/v1/users/{user_id}/expenses/filtered
```
This request has the same optional parameters as unfiltered one, however it accept a request body. This body is [Request with filters](#request-with-filters)

#### POST expense
```
api/v1/users/{user_id}/expenses
```
``user_id`` - unique id of the user
This request accepts [Request with expense data](#request-with-expense-data) as request body. Expense can be posted only if every 
property is filled. 

#### PUT expense
```
api//users/{user_id}/expenses/{expense_id}
```
* ``user_id`` - unique id of the user
* ``expense`` - unique id of an expense
This request accepts [Request with expense data](#request-with-expense-data) as request body. Fill only properties that you want to have updated. User can update only his expenses. 

#### DELETE expense
```
api/v1/api/users/{user_id}/expenses/{expense_id}
```
* ``user_id`` - unique id of the user
* ``expense`` - unique id of an expense
User can delete only his/her expenses. 


## User Personal Details
### Data Transfer Objects
#### User Personal Details Request/Response
```
{
    "salary": double,
    "profession": string,
    "age": int,
    "sex": string,
    "relationship_status": string,
    "kids": int
}
```
* ``salary`` - salary of an individual
* ``profession`` - one of professions that are available at the endpoint ``api/v1/enums/profession``
* ``age`` - age of a user
* ``sex`` - one of genders that are available at the endpoint ``api/v1/enums/sex``
* ``relationship_status`` - one of relationship statuses that are available at the endpoint ``api/v1/enums/relationship_status``
* ``kids`` - number of kids owned by a user 

### Methods

All responses return [User Personal Details Request/Response](#user-personal-details-requestresponse).

There are availible 3 HTTP methods - GET, POST, PUT. User can only delete his personal details when he deletes his/her account. User can have access only to his/her personal details. 

All methods are available at the endpoint
```
api/v1/users/{user_id}/details
```
``user_id`` - unique id of a user

#### GET user personal details
Simply returns personal details of a user.

#### POST user personal details
User personal details can be posted only if request body doesn't contain empty values.

#### PUT user personal details
Fill only properties that you want to have updated.

## Goal Analyser
Goal Analyser analyses whether user can afford a goal based on his expenses in chosen period of time. 

### Data Transfer Objects
#### Goal Analyser Request
```
{
    initial_contribution: double,
    start_date: YYYY-MM-DD,
    end_date: YYYY-MM-DD
}
```
* ``initial_contribution`` - initial money that can be contributed towards a goal
* ``start_date`` - start date to calculate balance
* ``end_date`` - end date to calculate balance

#### Goal Analyser Response
```
{
    "can_achieve": boolean,
    "money_to_collect": double,
    "average_daily_possible_savings": double,
    "average_daily_necessary_savings": double,
    "can_achieve_before_end_date": boolean
}
```
* ``can_achieve`` - if user earned more than spent in this timeperiod then it's true, else it's false
* ``money_to_collect`` - amount of money that has to be collected after deducting initial contribution
* ``average_daily_possible_savings`` - average amount of money that user can save daily
* ``average_daily_necessary_savings`` - average amount of money that user has to save daily in order to be able to save for this item
* ``can_achieve_before_end_date`` - if user daily saves more or equal to the necessary amount of money than it's true, else it's false

### Methods 
Goal Analyser has only one HTTP method - GET. It can be reached at the endpoint
```
/api/v1/user/{user_id}/goal/{goal_id}/analyser
```
* ``user_id`` - unique id of a user
* ``goal_id`` - unique id of a goal, user can access only his goals

This request accepts [Goal Analyser Request](#goal-analyser-request) as request body and returns [Goal Analyser Reponse](#goal-analyser-response) as response.

## Expense Optimiser
Expense Optimiser analyses user's expenses and returns informations whether user is better at spending money than other people with chosen parameters. The categories have to be present in order to return a response data.
### Data Transfer Objects
#### Filtration Expense Optimiser Request
```
{
    "salary": boolean,
    "profession": string,
    "age": int,
    "sex": string,
    "relationship_status": string,
    "kids": int,
    "start_date": YYYY-MM-DD,
    "end_date": YYYY-MM-DD,
    "categories": [
        string
    ]
}
```
* ``salary`` - salary +/- 500 to filter other users
* ``profession`` - profession that filtered users have. Professions are available at the endpoint ``api/v1/enums/profession``
* ``age`` - age +/- 2 that filtered users have
* ``sex`` - gender that filtered users have. Genders are available at the endpoint ``/api/v1/enums/sex``
* ``relationship_status`` - relationship status that filtered users have. Relationship statuses are available at the endpoint ``/api/v1/enums/relationship_status``
* ``kids`` - amount of kids that filtered users have to have
* ``start_date`` - start date to filter expenses of other users by
* ``end_date`` - end date to filter expenses of other users by
* ``categories`` - categories are available at the endpoint ``/api/v1/enums/category``. Categories should be sent as an array, and there can be multiple of them. 

#### Expense Optimiser Response
Response has two arrays:
* ``user_data`` - data of the user that made request
* ``similar_users_data`` - data of the filtered users
```
{
    "user_data": [
        {
            "category": string,
            "average_spent": double,
            "expense_count": int
        },
        ...
    ],
    "similar_users_data": [
        {
            "category": string,
            "average_spent": double,
            "expense_count": int
        },
        ...
    ]
}
```
* ``category`` - categories are available at the endpoint ``/api/v1/enums/category``. It is an information to which category this array is related to
* ``average_spent`` - average amount of money spent on this category
* ``expense_count`` - number of expenses that this statistic is based on

### Methods
There is only one HTTP method - GET. It can be reached at the endpoint
```
api/v1/users/{user_id}/optimiser
```
``user_id`` - unique id of a user that requests optimiser use. 
This request accepts [Filtration Expense Optimiser Request](#filtration-expense-optimiser-request) as request body and returns as a response [Expense Optimiser Response](#expense-optimiser-response).