# Technical Assessment
## Overview

Framework described below was written for JSONPlaceholder API. It uses WebClient for sending requests and AssertJ for assertions. For working with test data Apache Commons and Jackson libraries were used.

## Scope

Tests were created for all endpoints exposed by the API:
- albums
- comments
- photos
- post
- todos
- users

For every endpoint basic CRUD operations were covered by tests. Due to the fact that JSONPlaceholder does not provide request body validation negative test cases we limited to one per endpoint (triggering 404 - NOT FOUND error).

## Reporting

Sample test execution report was generated using IntelliJ and included in reports folder. Given more time more dedicated reporting solution like Allure could be integrated into framework.  
