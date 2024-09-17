# Technical Assessment
## Overview

Framework described below was written for JSONPlaceholder API. It uses WebClient for sending requests and AssertJ for assertions. For working with test data Apache Commons and Jackson libraries were used. Because the framework covers lower level integration tests, test cases were written directly in java instead of using BDD based solutions which are better suited for higher-level, user-centric scenarios. The framework uses design patterns like factory and adapter to create entities such as users and posts, which are utilized in tests.

## Setup

Framework requires JDK v17 and Maven. In order to use the framework download the code from repository, navigate to the main directory in terminal and run it with `mvn test` command. JSONPlaceholder API does not require authentication. 

## Scope

JSONPlaceholder API exposes following endpoints:
- albums
- comments
- photos
- post
- todos
- users

Tests in the framework were grouped in separate test classes per endpoint. For every endpoint basic CRUD operations were covered by tests. Due to the fact that JSONPlaceholder does not provide request's body validation negative test cases we limited to one per endpoint (triggering 404 - NOT FOUND error).

## Reporting

Sample test execution report was generated using IntelliJ and included in reports folder. Given more time more dedicated reporting solution like Allure could be integrated into framework.  
