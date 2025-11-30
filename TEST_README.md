# Test Suite Documentation

## Overview

This project includes a comprehensive test suite for the Humanitarian Support Platform. The tests cover DAO classes, utility functions, and core business logic.

## Test Structure

```
src/test/
├── java/
│   └── com/humanitarian/
│       ├── dao/              # DAO test classes
│       │   ├── UserDAOTest.java
│       │   ├── HelpRequestDAOTest.java
│       │   ├── DocumentDAOTest.java
│       │   ├── DonationDAOTest.java
│       │   └── NotificationDAOTest.java
│       ├── util/             # Utility test classes
│       │   ├── PasswordUtilTest.java
│       │   ├── TestDBConnection.java
│       │   └── TestDataSetup.java
│       └── AllTests.java     # Test suite runner
└── resources/
    └── test-database.properties
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserDAOTest
```

### Run Tests with Coverage
```bash
mvn test jacoco:report
```

### Run Tests in IDE
- **IntelliJ IDEA**: Right-click on test class → Run
- **Eclipse**: Right-click on test class → Run As → JUnit Test

## Test Database Setup

Before running tests, ensure you have a test database:

```sql
-- Create test database
CREATE DATABASE humanitarian_support_test;

-- Run the schema on test database
psql -U postgres -d humanitarian_support_test -f database/schema.sql
```

Or update `TestDBConnection.java` to use your test database configuration.

## Test Classes

### UserDAOTest
Tests user registration, login, email existence checks, and password hashing.

**Test Methods:**
- `testRegisterUser()` - Tests user registration
- `testEmailExists()` - Tests email existence check
- `testLoginUser()` - Tests login functionality
- `testGetUserById()` - Tests user retrieval by ID
- `testPasswordHashing()` - Tests password hashing and verification

### HelpRequestDAOTest
Tests help request creation, retrieval, status updates, and filtering.

**Test Methods:**
- `testCreateHelpRequest()` - Tests help request creation
- `testGetHelpRequestById()` - Tests request retrieval
- `testGetHelpRequestsBySurvivor()` - Tests survivor's request list
- `testGetVerifiedHelpRequests()` - Tests verified requests filtering
- `testUpdateHelpRequestStatus()` - Tests status updates

### DocumentDAOTest
Tests document upload, retrieval, and verification.

**Test Methods:**
- `testSaveDocument()` - Tests document saving
- `testGetDocumentById()` - Tests document retrieval
- `testGetDocumentsByRequestId()` - Tests document list by request
- `testVerifyDocument()` - Tests document verification
- `testGetUnverifiedDocuments()` - Tests unverified documents list

### DonationDAOTest
Tests donation creation, retrieval, and status updates.

**Test Methods:**
- `testCreateDonation()` - Tests donation creation
- `testGetDonationsByHelper()` - Tests helper's donations
- `testGetDonationsByRequest()` - Tests donations by request
- `testUpdateDonationStatus()` - Tests status updates
- `testDonationWithItem()` - Tests item donations
- `testDonationWithService()` - Tests service donations

### NotificationDAOTest
Tests notification creation, retrieval, and status updates.

**Test Methods:**
- `testCreateNotification()` - Tests notification creation
- `testGetNotificationsByUser()` - Tests user notifications
- `testGetUnreadNotifications()` - Tests unread notifications
- `testMarkAsRead()` - Tests marking as read
- `testMarkEmailSent()` - Tests email sent status
- `testNotificationTypes()` - Tests different notification types

### PasswordUtilTest
Tests password hashing and verification utilities.

**Test Methods:**
- `testHashPassword()` - Tests password hashing
- `testHashPasswordConsistency()` - Tests hash consistency
- `testHashPasswordUniqueness()` - Tests hash uniqueness
- `testVerifyPassword()` - Tests password verification
- `testHashPasswordWithSpecialCharacters()` - Tests special character handling

## Test Configuration

### Database Configuration
Update `TestDBConnection.java` with your test database credentials:
```java
private static final String TEST_DB_URL = "jdbc:postgresql://localhost:5432/humanitarian_support_test";
private static final String TEST_DB_USER = "postgres";
private static final String TEST_DB_PASSWORD = "postgres";
```

### Test Data
Tests use the `TestDataSetup` utility class to create test users and data. Test data is created automatically but may need cleanup between test runs.

## Best Practices

1. **Isolation**: Each test should be independent and not rely on other tests
2. **Cleanup**: Clean up test data after tests (if needed)
3. **Naming**: Use descriptive test method names starting with `test`
4. **Assertions**: Use appropriate assertions for each test case
5. **Database**: Use a separate test database to avoid affecting production data

## Troubleshooting

### Tests Fail with Database Connection Error
- Ensure PostgreSQL is running
- Check test database exists: `psql -l | grep humanitarian_support_test`
- Verify database credentials in `TestDBConnection.java`

### Tests Fail with "User Already Exists"
- Tests check for existing users and skip creation if found
- This is expected behavior to allow test re-runs

### Tests Are Slow
- Consider using an in-memory database (H2) for faster tests
- Or optimize test database queries

## Continuous Integration

Tests can be integrated into CI/CD pipelines:

```yaml
# Example GitHub Actions
- name: Run Tests
  run: mvn test
```

## Coverage Goals

Aim for:
- **DAO Classes**: 80%+ coverage
- **Utility Classes**: 90%+ coverage
- **Business Logic**: 70%+ coverage

## Future Enhancements

- Add integration tests for servlets
- Add performance tests
- Add security tests
- Add API endpoint tests
- Add database migration tests

